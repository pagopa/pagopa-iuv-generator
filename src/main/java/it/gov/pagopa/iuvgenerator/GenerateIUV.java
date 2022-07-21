package it.gov.pagopa.iuvgenerator;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.validation.constraints.NotBlank;
import javax.ws.rs.core.MediaType;

import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import org.apache.commons.lang3.ObjectUtils;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.annotation.BindingName;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;

import it.gov.pagopa.iuvgenerator.model.IuvGenerationModel;
import it.gov.pagopa.iuvgenerator.model.IuvGenerationModelResponse;
import it.gov.pagopa.iuvgenerator.service.IUVService;

/**
 * Azure Functions with Azure Http trigger.
 */

public class GenerateIUV {

	private static final String HEADER_KEY_CONTENT_TYPE = "Content-Type";

	/**
	 * This function will be invoked when a Http Trigger occurs
	 * @return
	 */
	@FunctionName("GenerateIUV")
	public HttpResponseMessage run (
			@HttpTrigger(name = "GenerateIUVTrigger", authLevel = AuthorizationLevel.ANONYMOUS,
			methods = {HttpMethod.POST},
			route = "/organizations/{organizationfiscalcode}/iuv"
					) HttpRequestMessage<Optional<IuvGenerationModel>> request,
			@NotBlank @BindingName("organizationfiscalcode") String organizationFiscalCode,
			final ExecutionContext context) {

		Logger logger = context.getLogger();

		IuvGenerationModel body = request.getBody().orElseGet(IuvGenerationModel::new);

		logger.log(Level.INFO, 
				() -> "GenerateIUV function executed at: " + LocalDateTime.now() + " for organizationfiscalcode " + organizationFiscalCode + " with body: "+body);

		if (ObjectUtils.isEmpty(body.getAuxDigit()) || ObjectUtils.isEmpty(body.getSegregationCode())) {
			return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
					.body("The body is not well formed or is blank.")
					.build();
		}


		IUVService iuvService = this.getIUVServiceInstance(logger);

		try {
			String validIUV = iuvService.generateValidIUV(organizationFiscalCode, body.getSegregationCode(), body.getAuxDigit());
			return request.createResponseBuilder(HttpStatus.CREATED)
					.header(HEADER_KEY_CONTENT_TYPE, MediaType.APPLICATION_JSON)
					.body(IuvGenerationModelResponse.builder().iuv(validIUV).build())
					.build();
		} catch (Exception e) {
			logger.log(Level.SEVERE, () -> "GenerateIUV error: " + e.getLocalizedMessage());

			return request.createResponseBuilder(HttpStatus.INTERNAL_SERVER_ERROR)
					.header(HEADER_KEY_CONTENT_TYPE, MediaType.APPLICATION_JSON)
					.build();
		} 
	}

	public IUVService getIUVServiceInstance(Logger logger) {
		return new IUVService(logger);
	}
}
