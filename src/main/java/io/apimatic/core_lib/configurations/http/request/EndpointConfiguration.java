package io.apimatic.core_lib.configurations.http.request;

import io.apimatic.core_interfaces.http.request.configuration.RequestRetryConfiguration;

public class EndpointConfiguration implements io.apimatic.core_interfaces.http.request.configuration.EndpointConfiguration {

	private boolean hasBinary;
	private RequestRetryConfiguration requestRetryConfiguration;
	
	public EndpointConfiguration(boolean hasBinaryResponse) {
		this.hasBinary = hasBinaryResponse;
	}
	
	public void requestRetryConfiguration(RequestRetryConfiguration requestRetryConfiguration) {
		this.requestRetryConfiguration = requestRetryConfiguration;
	}
	
	public RequestRetryConfiguration getRequestRetryConfiguration() {
		return requestRetryConfiguration;
	}
	
	public void hasBinary(boolean hasBinary) {
		this.hasBinary = hasBinary;
	}
	
	public boolean hasBinary() {
		return hasBinary;
	}
	
	
}
