package it.gov.pagopa.iuvgenerator.service;

import java.util.logging.Logger;

import com.azure.data.tables.TableClient;
import com.azure.data.tables.TableClientBuilder;
import com.azure.data.tables.models.TableEntity;
import com.azure.data.tables.models.TableServiceException;

import it.gov.pagopa.iuvgenerator.exception.IuvGeneratorException;
import it.gov.pagopa.iuvgenerator.producer.IuvCodeBusiness;
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
    
    public IUVService(Logger logger, String storageConnectionString, String iuvsTable) {
        this.logger = logger;
        this.storageConnectionString = storageConnectionString;
        this.iuvsTable = iuvsTable;
    }
	
	public String generateValidIUV(String organizationFiscalCode, long segregationCode, long auxDigit) throws IuvGeneratorException, IllegalArgumentException  {
        int retryCount = 1;
        String iuv = null;
        while (true) {
            try {
            	iuv = IuvCodeBusiness.generateIUV(segregationCode, auxDigit);
                this.checkTableIUVUniqueness(organizationFiscalCode,iuv);
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
	
	
	/**
	 * @param partitionKey: The organization fiscal code
	 * @param rowKey: The generated iuv
	 * @throws TableServiceException:  If an entity with the same partition key and row key alreadyexists within the table
	 * @throws IllegalArgumentException:  If the provided entity is null
	 */
	public void checkTableIUVUniqueness  (String partitionKey, String rowKey) throws TableServiceException, IllegalArgumentException {

        TableClient tableClient = new TableClientBuilder()
        .connectionString(storageConnectionString)
        .tableName(iuvsTable)
        .buildClient();
        
        
        TableEntity iuvEntity = new TableEntity(partitionKey,rowKey);
        tableClient.createEntity(iuvEntity);

    }	
}
