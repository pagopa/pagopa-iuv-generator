package it.gov.pagopa.debtposition.iuv.service;

import java.util.logging.Logger;

import com.azure.data.tables.TableClient;
import com.azure.data.tables.TableClientBuilder;
import com.azure.data.tables.models.TableEntity;
import com.azure.data.tables.models.TableServiceException;

import it.gov.pagopa.debtposition.iuv.exception.IuvGeneratorException;
import it.gov.pagopa.debtposition.iuv.generator.IuvCodeBusiness;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class IUVService {
	
	private String storageConnectionString = System.getenv("IUV_STORAGE_CONNECTION_STRING");
    private String iuvsTable = System.getenv("IUV_STORAGE_TABLE");
    private int iuvMaxRetryCount = System.getenv("IUV_MAX_RETRY_COUNT") != null ? Integer.parseInt(System.getenv("IUV_MAX_RETRY_COUNT")) : 0;
    private Logger logger;
    
    public IUVService(Logger logger) {
        this.logger = logger;
    }
	
	public String generateValidIUV(String paIdFiscalCode, int segregationCode, int auxDigit) throws IuvGeneratorException, IllegalArgumentException  {
        int retryCount = 1;
        String iuv = null;
        while (true) {
            try {
            	iuv = this.generateIUV(segregationCode, auxDigit);
                this.checkIUVExistence(paIdFiscalCode,iuv);
                break;
            } catch (TableServiceException e) {
                if (retryCount > iuvMaxRetryCount) {
                	throw new IuvGeneratorException("Unable to get a unique IUV in " + iuvMaxRetryCount + " retry");   
                }
                logger.warning(
                        "Not unique IUV ["+iuv+"] in table ["+iuvsTable+"]: a new one will be generated [retry = "+retryCount+"]");
                retryCount++;
            }
        }
        return iuv;
    }
	
	public void checkIUVExistence (String partitionKey, String rowKey) throws TableServiceException, IllegalArgumentException {

        TableClient tableClient = new TableClientBuilder()
        .connectionString(storageConnectionString)
        .tableName(iuvsTable)
        .buildClient();
        
        
        TableEntity iuvEntity = new TableEntity(partitionKey,rowKey);
        tableClient.createEntity(iuvEntity);

    }
	
	private String generateIUV(int segregationCode, int auxDigit) throws IuvGeneratorException {
        return IuvCodeBusiness.generateIUV(segregationCode, auxDigit);
    }
	
	
}
