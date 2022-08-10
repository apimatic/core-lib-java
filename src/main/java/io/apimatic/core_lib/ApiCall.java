package io.apimatic.core_lib;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import io.apimatic.core_interfaces.http.request.HttpRequest;
import io.apimatic.core_interfaces.http.request.configuration.EndpointConfiguration;
import io.apimatic.core_interfaces.http.response.HttpResponse;
import io.apimatic.core_lib.request.async.AsyncExecutor;
import io.apimatic.core_lib.types.ApiException;

public class ApiCall {

	private CoreConfig coreConfig;
	private HttpRequest request;
	private CoreResponseHandler<?, ?> responseHandler;
	private EndpointConfiguration endpointConfiguration;

	private ApiCall(CoreConfig coreConfig) {
		this.coreConfig = coreConfig;
	}

	/**
	 * Getter for the CoreConfig
	 * 
	 * @return the CoreConfig instance
	 */
	public CoreConfig getCoreConfig() {
		return this.coreConfig;
	}

	/**
	 * Getter for the HttpRequest
	 * 
	 * @return the HttpRequest instance
	 */
	public HttpRequest getRequest() {
		return request;
	}

	/**
	 * Getter for the ResponseHandler
	 * 
	 * @return the ResponseHandler
	 */
	public CoreResponseHandler<?, ?> getResponseHandler() {
		return responseHandler;
	}

	/**
	 * @return the endpointConfiguration
	 */
	public EndpointConfiguration getEndpointConfiguration() {
		return endpointConfiguration;
	}

	public static class Builder<ResponseType, ExceptionType extends ApiException> {
		private CoreConfig coreConfig;
		private CoreRequest.Builder requestBuilder = null;
		private CoreResponseHandler<ResponseType, ExceptionType> responseHandler = null;
		private EndpointConfiguration endpointConfiguration = null;

		public Builder<ResponseType, ExceptionType> coreConfig(CoreConfig coreConfig) {
			this.coreConfig = coreConfig;
			return this;
		}

		public Builder<ResponseType, ExceptionType> requestBuilder(CoreRequest.Builder requestBuilder) {
			this.requestBuilder = requestBuilder;
			return this;
		}

		public Builder<ResponseType, ExceptionType> responseHandler(
				CoreResponseHandler<ResponseType, ExceptionType> responseHandler) {
			this.responseHandler = responseHandler;
			return this;
		}

		public Builder<ResponseType, ExceptionType> endpointConfiguration(EndpointConfiguration configuration) {
			this.endpointConfiguration = configuration;
			return this;
		}

		public ResponseType execute() throws IOException, ExceptionType {
			HttpRequest httpRequest = requestBuilder.build(coreConfig);
			HttpResponse httpResponse = coreConfig.getHttpClient().execute(httpRequest, endpointConfiguration);
			return responseHandler.handle(httpRequest, httpResponse, coreConfig);
		}

		public CompletableFuture<ResponseType> executeAsync() throws IOException, ExceptionType {
			return new AsyncExecutor(coreConfig).makeHttpCallAsync(() -> requestBuilder.build(coreConfig),
					request -> coreConfig.getHttpClient().executeAsync(request, endpointConfiguration), (httpRequest,
							httpResponse, coreconfig) -> responseHandler.handle(httpRequest, httpResponse, coreConfig));
		}
	}
}
