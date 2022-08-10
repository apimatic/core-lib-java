package io.apimatic.core_lib;

import io.apimatic.core_interfaces.http.HttpHeaders;

public class MultiPartRequest {

	private MultiPartRequestType requestType;
	private HttpHeaders headers;
	
	public MultiPartRequestType getMultiPartRequestType() {
		return requestType;
	}
	
	public HttpHeaders getHeaders() {
		return headers;
	}
	
	public MultiPartRequest requestType(MultiPartRequestType multiPartRequestType) {
		requestType = multiPartRequestType;
		return this;
	}
	 
	public MultiPartRequest headers(String key, String value) {
		validate(key, value);
		this.headers.add(key, value);
		return this;
	}
	
	public MultiPartRequest(HttpHeaders headers) {
		this.headers = headers;
	}
	
	private void validate(String key, String value) {
		if (null == value) {
			throw new NullPointerException("The header value is a required parameter and cannot be null.");
		}
		
		if (null == key) {
			throw new NullPointerException("The header key is a required parameter and cannot be null.");
		}
	}
}
