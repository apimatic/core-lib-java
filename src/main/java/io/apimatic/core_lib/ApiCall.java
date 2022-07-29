package io.apimatic.core_lib;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import io.apimatic.core_interfaces.http.request.HttpRequest;
import io.apimatic.core_interfaces.http.response.HttpResponse;
import io.apimatic.core_lib.request.async.AsyncExecutor;

public class ApiCall {

	private CoreConfig coreConfig;
	private HttpRequest request;
	private ResponseHandler<?> responsehandler;

	private ApiCall(CoreConfig coreConfig) {
		this.coreConfig = coreConfig;
	}

	public CoreConfig getCoreConfig() {
		return this.coreConfig;
	}

	public HttpRequest getRequest() {
		return request;
	}

	public ResponseHandler<?> getResponsehandler() {
		return responsehandler;
	}

	public static class Builder<T> {
		private CoreConfig coreConfig;
		private Request.Builder requestBuilder = null;
		private ResponseHandler<T> responsehandler = null;

		public Builder<T> coreConfig(CoreConfig coreConfig) {
			this.coreConfig = coreConfig;
			return this;
		}

		public Builder<T> requestBuilder(Request.Builder requestBuilder) {
			this.requestBuilder = requestBuilder;
			return this;
		}

		public Builder<T> responseHandler(ResponseHandler<T> responseHanlder) {
			this.responsehandler = responseHanlder;
			return this;
		}

		public T execute() throws IOException {
			HttpRequest httpRequest = requestBuilder.build(coreConfig);
			HttpResponse httpResponse = coreConfig.getHttpClient().execute(httpRequest, false, null);
			return responsehandler.handle(httpRequest, httpResponse, coreConfig);
		}

		public CompletableFuture<T> executeAsync() {
//           return new AsyncExecutor(coreConfig).makeHttpCallAsync(() -> requestBuilder.build(coreConfig),
//        		   request -> coreConfig.getHttpClient().executeAsync(request, false, null),
//        		   context -> responseHandler.handle(context));

			return null;
		}
	}
}
