package it.gov.pagopa.iuvgenerator;

import com.microsoft.azure.functions.*;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.BindingName;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;
import it.gov.pagopa.iuvgenerator.model.IuvGenerationModel;
import it.gov.pagopa.iuvgenerator.model.IuvGenerationModelResponse;
import it.gov.pagopa.iuvgenerator.service.IUVService;
import org.apache.commons.lang3.ObjectUtils;

import javax.validation.constraints.NotBlank;
import javax.ws.rs.core.MediaType;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Azure Functions with Azure Http trigger.
 */

public class GenerateIUV {

    private static final String HEADER_KEY_CONTENT_TYPE = "Content-Type";
    public static final String X_REQUEST_ID = "X-Request-Id";

    /**
     * This function will be invoked when a Http Trigger occurs
     *
     * @return
     */
    @FunctionName("GenerateIUV")
    public HttpResponseMessage run(
            @HttpTrigger(name = "GenerateIUVTrigger", authLevel = AuthorizationLevel.ANONYMOUS,
                    methods = {HttpMethod.POST},
                    route = "organizations/{organizationfiscalcode}/iuv"
            ) HttpRequestMessage<Optional<IuvGenerationModel>> request,
            @NotBlank @BindingName("organizationfiscalcode") String organizationFiscalCode,
            final ExecutionContext context) {

        Logger logger = context.getLogger();

        String requestId = getRequestId(request);

        IuvGenerationModel body = request.getBody().orElseGet(IuvGenerationModel::new);

        logger.log(Level.INFO,
                () -> "[requestId=" + requestId + "] GenerateIUV function executed at: " + LocalDateTime.now() + " for organizationfiscalcode " + organizationFiscalCode + " with body: " + body);

        if (ObjectUtils.isEmpty(body.getAuxDigit()) || ObjectUtils.isEmpty(body.getSegregationCode())) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
                    .body("The body is not well formed or is blank.")
                    .build();
        }


        IUVService iuvService = this.getIUVServiceInstance(logger, requestId);

        try {
            String validIUV = iuvService.generateValidIUV(organizationFiscalCode, body.getSegregationCode(), body.getAuxDigit());
            return request.createResponseBuilder(HttpStatus.CREATED)
                    .header(HEADER_KEY_CONTENT_TYPE, MediaType.APPLICATION_JSON)
                    .body(IuvGenerationModelResponse.builder().iuv(validIUV).build())
                    .build();
        } catch (Exception e) {
            logger.log(Level.SEVERE, () -> "[requestId=" + requestId + "] GenerateIUV error: " + e.getLocalizedMessage());

            return request.createResponseBuilder(HttpStatus.INTERNAL_SERVER_ERROR)
                    .header(HEADER_KEY_CONTENT_TYPE, MediaType.APPLICATION_JSON)
                    .header(X_REQUEST_ID, requestId)
                    .build();
        }
    }

    private String getRequestId(HttpRequestMessage<Optional<IuvGenerationModel>> request) {
        String requestId = request.getHeaders().get(X_REQUEST_ID);
        if (requestId == null) {
            requestId = UUID.randomUUID().toString();
        }
        return requestId;
    }

    public IUVService getIUVServiceInstance(Logger logger, String requestId) {
        return new IUVService(logger, requestId);
    }
}
