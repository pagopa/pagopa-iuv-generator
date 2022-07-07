package it.gov.pagopa.iuvgenerator.service;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.logging.Logger;

import org.junit.ClassRule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import com.azure.data.tables.TableServiceClient;
import com.azure.data.tables.TableServiceClientBuilder;
import com.azure.data.tables.models.TableServiceException;

@Testcontainers
class IUVServiceContainerTest {

	@ClassRule @Container
	public static GenericContainer<?> azurite =
	new GenericContainer<>(
			DockerImageName.parse("mcr.microsoft.com/azure-storage/azurite:latest"))
	.withExposedPorts(10001, 10002, 10000);


	String storageConnectionString =
			String.format(
					"DefaultEndpointsProtocol=http;AccountName=devstoreaccount1;AccountKey=Eby8vdM02xNOcqFlqUwJPLlmEtlCDXJ1OUzFT50uSRZ6IFsuFq2UVErCz4I6tq/K1SZFPTOtr/KBHBeksoGMGw==;TableEndpoint=http://%s:%s/devstoreaccount1;QueueEndpoint=http://%s:%s/devstoreaccount1;BlobEndpoint=http://%s:%s/devstoreaccount1",
					azurite.getContainerIpAddress(),
					azurite.getMappedPort(10002),
					azurite.getContainerIpAddress(),
					azurite.getMappedPort(10001),
					azurite.getContainerIpAddress(),
					azurite.getMappedPort(10000));
	
	@BeforeEach
	public void setUp() {
		TableServiceClient tableServiceClient = new TableServiceClientBuilder()
			    .connectionString(storageConnectionString)
			    .buildClient();
		
		tableServiceClient.createTableIfNotExists("iuvsTable");
	}
	
	@AfterEach
	public void teardown() {
		TableServiceClient tableServiceClient = new TableServiceClientBuilder()
			    .connectionString(storageConnectionString)
			    .buildClient();
		
		tableServiceClient.deleteTable("iuvsTable");
	}


	@Test
	void checkTableIUVUniqueness()  {
		Logger logger = Logger.getLogger("testlogging");
		var iuvService = new IUVService(logger, storageConnectionString, "iuvsTable");
		
		iuvService.checkTableIUVUniqueness("partition", "row");
		assertTrue(true);  
	}
	
	@Test
	void checkTableIUVUniqueness_KO() {
		Logger logger = Logger.getLogger("testlogging");
		var iuvService = new IUVService(logger, storageConnectionString, "iuvsTable");
		try {
			// first insert must be ok
			iuvService.checkTableIUVUniqueness("partition", "row");
			assertTrue(true);
			// second insert with same data must be throw TableServiceException
			iuvService.checkTableIUVUniqueness("partition", "row");
			fail("No Exception throw but TableServiceException was expected");
		} catch (TableServiceException e) {
			assertTrue(true);
		} catch (Exception e) {
			fail("Obtained Exception but TableServiceException was expected");
		}
	}

}
