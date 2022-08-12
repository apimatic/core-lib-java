package io.apimatic.core_lib;

import io.apimatic.core_interfaces.http.HttpHeaders;
import io.apimatic.core_interfaces.type.FileWrapper;
import io.apimatic.core_lib.types.http.request.MultipartFileWrapper;
import io.apimatic.core_lib.types.http.request.MultipartWrapper;

public class Parameter {

	private String key;
	private Object value;
	private boolean isRequired = true;
	private boolean shouldEncode = false;

	/**
	 * @param key
	 * @param value
	 * @param isRequired
	 * @param shouldEncode
	 */
	private Parameter(String key, Object value, boolean isRequired, boolean shouldEncode) {
		this.key = key;
		this.value = value;
		this.isRequired = isRequired;
		this.shouldEncode = shouldEncode;
	}

	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * 
	 * @return the encodeFlag
	 */
	public boolean isEncodeAllow() {
		return shouldEncode;
	}

	/**
	 * @return the value
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * Validate the parameter fields
	 */
	public void validate() {
		if (isRequired) {
			// validating required parameters
			if (null == value) {
				throw new NullPointerException("The parameter value is a required parameter and cannot be null.");
			}
		}

	}

	public static class Builder {
		private String key;
		private Object value;
		private boolean isRequired = true;
		private boolean shouldEncode = false;
		private MultiPartRequestType multiPartRequest;
		private HttpHeaders fileHeaders;

		public Builder key(String key) {
			this.key = key;
			return this;
		}

		public Builder value(Object value) {
			this.value = value;
			return this;
		}

		public Builder multiPartRequestType(MultiPartRequestType multiPartRequestType) {
			this.multiPartRequest = multiPartRequestType;
			return this;
		}

		public Builder fileHeader(HttpHeaders fileHeaders) {
			this.fileHeaders = fileHeaders;
			return this;
		}

		public Builder isRequired(boolean isRequired) {
			this.isRequired = isRequired;
			return this;
		}

		public Builder shouldEncode(boolean shouldEncode) {
			this.shouldEncode = shouldEncode;
			return this;
		}

		public Parameter build() {
			handleMultiPartRequest();
			return new Parameter(key, value, isRequired, shouldEncode);

		}

		private void handleMultiPartRequest() {
			if (fileHeaders != null && multiPartRequest != null) {
				switch (multiPartRequest) {
				case MULTI_PART_FILE:
					MultipartFileWrapper multipartFileWrapper = new MultipartFileWrapper((FileWrapper) value,
						fileHeaders);
					value = multipartFileWrapper;
					break;
				case MULTI_PART:
					MultipartWrapper multipartWrapper = new MultipartWrapper(value.toString(), fileHeaders);
					value = multipartWrapper;
					break;
				default:
					break;
				}
			}
		}

	}

}
