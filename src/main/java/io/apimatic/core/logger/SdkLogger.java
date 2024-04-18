package io.apimatic.core.logger;

import java.util.Map;

import org.slf4j.event.Level;

import io.apimatic.core.utilities.CoreHelper;
import io.apimatic.coreinterfaces.http.request.Request;
import io.apimatic.coreinterfaces.http.response.Response;
import io.apimatic.coreinterfaces.logger.ApiLogger;
import io.apimatic.coreinterfaces.logger.Logger;
import io.apimatic.coreinterfaces.logger.configuration.ReadonlyLoggingConfiguration;

/**
 * Class to log the Http api messages.
 */
public class SdkLogger implements ApiLogger {
	/**
	 * An instance of {@link Logger}.
	 */
	private Logger logger;

	/**
	 * An instance of {@link ReadonlyLoggingConfiguration}.
	 */
	private ReadonlyLoggingConfiguration config;

	/**
	 * Default Constructor.
	 * 
	 * @param config {@link ReadonlyLoggingConfiguration} as logging properties.
	 */
	public SdkLogger(final ReadonlyLoggingConfiguration config) {
		this.config = config;
		this.logger = config.getLogger();
	}

	/**
	 * Log requests.
	 * 
	 * @param request HttpRequest to be logged.
	 */
	public void logRequest(Request request) {
		Level level = config.getLevel() != null ? config.getLevel() : Level.INFO;

		String url = request.getUrl();
		String queryParameter = CoreHelper.getQueryParametersFromUrl(request.getQueryUrl());
		String contentType = request.getHeaders().value("content-type") != null
				? request.getHeaders().value("content-type")
				: "";

		if (config.getRequestLogOptions().shouldIncludeQueryInPath()) {
			logger.log(level, "Request {} {} {} queryParameters: {}", request.getHttpMethod(), url, contentType,
					queryParameter);
		} else {
			logger.log(level, "Request {} {} {}", request.getHttpMethod(), url, contentType);
		}

		if (config.getRequestLogOptions().shouldLogHeaders()) {
			Map<String, String> extractedHeaders = LoggerUtilities.extractHeadersToLog(
					request.getHeaders().asSimpleMap(), config.getRequestLogOptions().getHeadersToInclude(),
					config.getRequestLogOptions().getHeadersToExclude());

			Map<String, String> headersToLog = LoggerUtilities.filterSensitiveHeaders(extractedHeaders,
					config.getMaskSensitiveHeaders());

			logger.log(level, "Request Headers {}", headersToLog);
		}

		if (config.getRequestLogOptions().shouldLogBody()) {
			if (request.getBody() != null) {
				logger.log(level, "Request Body {}", request.getBody());
			} else if (request.getParameters() != null && !request.getParameters().isEmpty()) {
				logger.log(level, "Request Body {}", request.getParameters());
			}
		}
	}

	/**
	 * Log Responses.
	 * 
	 * @param response HttpResponse to be logged.
	 */
	public void logResponse(Response response) {
		Level level = config.getLevel() != null ? config.getLevel() : Level.INFO;

		String contentLength = response.getHeaders().value("content-length");
		String contentType = response.getHeaders().value("content-type") != null
				? response.getHeaders().value("content-type")
				: "";

		logger.log(level, "Response {} {} content-length: {}", response.getStatusCode(), contentType, contentLength);

		if (config.getResponseLogOptions().shouldLogHeaders()) {
			Map<String, String> extractedHeaders = LoggerUtilities.extractHeadersToLog(
					response.getHeaders().asSimpleMap(), config.getResponseLogOptions().getHeadersToInclude(),
					config.getResponseLogOptions().getHeadersToExclude());

			Map<String, String> headersToLog = LoggerUtilities.filterSensitiveHeaders(extractedHeaders,
					config.getMaskSensitiveHeaders());

			logger.log(level, "Response Headers {}", headersToLog);
		}

		if (config.getResponseLogOptions().shouldLogBody()) {
			logger.log(level, "Response Body {}", response.getBody());
		}
	}
}
