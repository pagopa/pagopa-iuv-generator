package it.gov.pagopa.iuvgenerator.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import com.azure.core.http.HttpResponse;
import com.azure.data.tables.models.TableServiceException;

import it.gov.pagopa.iuvgenerator.exception.IuvGeneratorException;

@ExtendWith(MockitoExtension.class)
class IUVServiceTest {
	
	@Spy
	IUVService iuvService;

	@Test
	void generateValidIUV() throws IllegalArgumentException, IuvGeneratorException {
		doNothing().when(iuvService).checkIUVExistence(anyString(),anyString());
		String iuv = iuvService.generateValidIUV("777", 47, 3);
		assertEquals(17, iuv.length());
	}
	
	@Test
	void generateValidIUV_IuvGeneratorException() throws IllegalArgumentException, IuvGeneratorException {
		HttpResponse responseMock = mock(HttpResponse.class);
		doThrow(new TableServiceException ("duplicated entity", responseMock)).when(iuvService).checkIUVExistence(anyString(),anyString());
		try {
			iuvService.generateValidIUV("777", 47, 3);
			fail();
		} catch (IuvGeneratorException e) {
			assertTrue(e.getMessage().contains("Unable to get a unique IUV"));
		} catch (Exception e) {
			fail();
		}
	}
}
