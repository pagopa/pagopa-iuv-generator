package it.gov.pagopa.iuvgenerator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.logging.Logger;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;

import it.gov.pagopa.iuvgenerator.exception.IuvGeneratorException;
import it.gov.pagopa.iuvgenerator.model.IuvGenerationModel;
import it.gov.pagopa.iuvgenerator.model.IuvGenerationModelResponse;
import it.gov.pagopa.iuvgenerator.service.IUVService;

@ExtendWith(MockitoExtension.class)
class GenerateIUVTest {

	@Mock
    ExecutionContext context;

    @Spy
    GenerateIUV iuvFunction;

    @Mock
    IUVService iuvService;
    
    @Test
    void runOK() throws IllegalArgumentException, IuvGeneratorException {
    	Logger logger = Logger.getLogger("testlogging");
    	when(context.getLogger()).thenReturn(logger);
    	when(iuvFunction.getIUVServiceInstance(any())).thenReturn(iuvService);
    	when(iuvService.generateValidIUV(anyString(), anyLong(), anyLong())).thenReturn("validIUVString");
    	
    	Optional<IuvGenerationModel> model = Optional.ofNullable(IuvGenerationModel.builder().auxDigit(3L).segregationCode(47L).build());
    	
    	HttpRequestMessage<Optional<IuvGenerationModel>> requestMock = mock(HttpRequestMessage.class);
    	doReturn(model).when(requestMock).getBody();
    	
    	final HttpResponseMessage.Builder builderMock = mock(HttpResponseMessage.Builder.class);
    	doReturn(builderMock).when(requestMock).createResponseBuilder(any(HttpStatus.class));
        doReturn(builderMock).when(builderMock).header(anyString(), anyString());
        doReturn(builderMock).when(builderMock).body(any());
        
        HttpResponseMessage responseMock = mock(HttpResponseMessage.class);
        doReturn(HttpStatus.CREATED).when(responseMock).getStatus();
        doReturn(IuvGenerationModelResponse.builder().iuv("validIUVString").build()).when(responseMock).getBody();
        doReturn(responseMock).when(builderMock).build();
    	
    	HttpResponseMessage response = iuvFunction.run(requestMock, "777", context);
    	
    	// Asserts
        assertEquals(HttpStatus.CREATED, response.getStatus());
        assertEquals("IuvGenerationModelResponse(iuv=validIUVString)", response.getBody().toString());
    }
    
    @Test
    void runOK_OneDigitSegregationCode() throws IllegalArgumentException, IuvGeneratorException {
    	Logger logger = Logger.getLogger("testlogging");
    	when(context.getLogger()).thenReturn(logger);
    	when(iuvFunction.getIUVServiceInstance(any())).thenReturn(iuvService);
    	when(iuvService.generateValidIUV(anyString(), anyLong(), anyLong())).thenReturn("validIUVString");
    	
    	Optional<IuvGenerationModel> model = Optional.ofNullable(IuvGenerationModel.builder().auxDigit(7L).segregationCode(5L).build());
    	
    	HttpRequestMessage<Optional<IuvGenerationModel>> requestMock = mock(HttpRequestMessage.class);
    	doReturn(model).when(requestMock).getBody();
    	
    	final HttpResponseMessage.Builder builderMock = mock(HttpResponseMessage.Builder.class);
    	doReturn(builderMock).when(requestMock).createResponseBuilder(any(HttpStatus.class));
        doReturn(builderMock).when(builderMock).header(anyString(), anyString());
        doReturn(builderMock).when(builderMock).body(any());
        
        HttpResponseMessage responseMock = mock(HttpResponseMessage.class);
        doReturn(HttpStatus.CREATED).when(responseMock).getStatus();
        doReturn(IuvGenerationModelResponse.builder().iuv("validIUVString").build()).when(responseMock).getBody();
        doReturn(responseMock).when(builderMock).build();
    	
    	HttpResponseMessage response = iuvFunction.run(requestMock, "777", context);
    	
    	// Asserts
        assertEquals(HttpStatus.CREATED, response.getStatus());
        assertEquals("IuvGenerationModelResponse(iuv=validIUVString)", response.getBody().toString());
    }
    
    @Test
    void runKO_400() throws IllegalArgumentException, IuvGeneratorException {
    	Logger logger = Logger.getLogger("testlogging");
    	when(context.getLogger()).thenReturn(logger);
    	
    	Optional<IuvGenerationModel> model = Optional.ofNullable(IuvGenerationModel.builder().build());
    	
    	HttpRequestMessage<Optional<IuvGenerationModel>> requestMock = mock(HttpRequestMessage.class);
    	doReturn(model).when(requestMock).getBody();
    	
    	final HttpResponseMessage.Builder builderMock = mock(HttpResponseMessage.Builder.class);
    	doReturn(builderMock).when(requestMock).createResponseBuilder(any(HttpStatus.class));
        doReturn(builderMock).when(builderMock).body(anyString());
        
        HttpResponseMessage responseMock = mock(HttpResponseMessage.class);
        doReturn(HttpStatus.BAD_REQUEST).when(responseMock).getStatus();
        doReturn("The body is not well formed or is blank").when(responseMock).getBody();
        doReturn(responseMock).when(builderMock).build();
    	
    	HttpResponseMessage response = iuvFunction.run(requestMock, "777", context);
    	
    	// Asserts
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatus());
        assertEquals("The body is not well formed or is blank", response.getBody());
    }
    
    @Test
    void runKO_500() throws IllegalArgumentException, IuvGeneratorException {
    	Logger logger = Logger.getLogger("testlogging");
    	when(context.getLogger()).thenReturn(logger);
    	when(iuvFunction.getIUVServiceInstance(any())).thenReturn(iuvService);
    	when(iuvService.generateValidIUV(anyString(), anyLong(), anyLong())).thenThrow(IuvGeneratorException.class);
    	
    	Optional<IuvGenerationModel> model = Optional.ofNullable(IuvGenerationModel.builder().auxDigit(3L).segregationCode(47L).build());
    	
    	HttpRequestMessage<Optional<IuvGenerationModel>> requestMock = mock(HttpRequestMessage.class);
    	doReturn(model).when(requestMock).getBody();
    	
    	final HttpResponseMessage.Builder builderMock = mock(HttpResponseMessage.Builder.class);
    	doReturn(builderMock).when(requestMock).createResponseBuilder(any(HttpStatus.class));
        doReturn(builderMock).when(builderMock).header(anyString(), anyString());
        
        HttpResponseMessage responseMock = mock(HttpResponseMessage.class);
        doReturn(HttpStatus.INTERNAL_SERVER_ERROR).when(responseMock).getStatus();
        doReturn("Unable to get a unique IUV").when(responseMock).getBody();
        doReturn(responseMock).when(builderMock).build();
    	
    	HttpResponseMessage response = iuvFunction.run(requestMock, "777", context);
    	
    	// Asserts
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatus());
        assertEquals("Unable to get a unique IUV", response.getBody());
    }

}
