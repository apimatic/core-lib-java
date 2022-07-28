package io.apimatic.core_lib;

import java.io.IOException;

import io.apimatic.core_interfaces.http.HttpContext;
import io.apimatic.core_interfaces.http.request.HttpRequest;
import io.apimatic.core_interfaces.http.response.HttpResponse;

public class ApiCall {

	private CoreConfig coreConfig;
	private HttpRequest request;
	private ResponseHandler responsehandler;
	
	
	public Builder newApiCall(CoreConfig coreConfig) {
		Builder builder = new Builder()
				.coreConfig(coreConfig);
		return builder;

	}
	public CoreConfig getCoreConfig() {
		return this.coreConfig;
	}
	
	private ApiCall(CoreConfig coreConfig) {
		this.coreConfig = coreConfig;
	}
	
	
	public static class Builder<T> {
		private CoreConfig coreConfig;
		private Request.Builder requestBuilder = null;
		private ResponseHandler<?> responsehandler = null;
		
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
		
		public void execute() throws IOException {
			HttpRequest request =  requestBuilder.build(coreConfig);
			HttpResponse response = coreConfig.getHttpClient().execute(request, false, null);
			//return responsehandler.handle(request, response, coreConfig);
			
		}
		
		public void executeAsync() {
			
		}
		
		public ApiCall build() {
			return new ApiCall(coreConfig);
		}
	}
}
