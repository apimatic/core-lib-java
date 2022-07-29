package io.apimatic.core_lib;

import io.apimatic.core_lib.types.ApiException;

public class ErrorCase {

	private int statusCode;
	private ApiException exception;
	
	/**
	 * @return the statusCode
	 */
	public int getStatusCode() {
		return statusCode;
	}

	/**
	 * @return the excpetion
	 */
	public ApiException getException() {
		return exception;
	}

	private ErrorCase(int statusCode, ApiException exception) {
		this.statusCode = statusCode;
		this.exception = exception;
	}
	
	public Builder toBuilder(int statusCode, ApiException exception) {
		
		Builder builder = new Builder()
				.statusCode(statusCode)
				.apiException(exception);
		return builder;
	}
	
	public static class Builder {
		private int statusCode;
		private ApiException  exception; 
		
		public Builder statusCode(int statusCode) {
			this.statusCode = statusCode;
			return this;
		}
		
		public Builder apiException(ApiException apiException) {
			this.exception = apiException;
			return this;
		}
		
		public ErrorCase build() {
			return  new ErrorCase(statusCode, exception);
		}
	}
}
