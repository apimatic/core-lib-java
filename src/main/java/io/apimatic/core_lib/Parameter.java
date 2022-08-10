package io.apimatic.core_lib;

public class Parameter {

	private String key;
	private Object value;
	private boolean isRequired = true;
	private boolean shouldEncode = false;
	private MultiPartRequest multiPartRequest;

	public Parameter() {

	}
	
	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	public boolean isEncodeAllow() {
		return shouldEncode;
	}

	/**
	 * @return the value
	 */
	public Object getValue() {
		return value;
	}

	public MultiPartRequest getMultiPartRequest() {
		return multiPartRequest;
	}

	public Parameter keyValue(String key, Object value) {
		this.key = key;
		this.value = value;
		return this;
	}

	public Parameter keyValue(String key, Object value, MultiPartRequest multiPartRequest) {
		this.key = key;
		this.value = value;
		this.multiPartRequest = multiPartRequest;
		return this;
	}

	public Parameter isRequired(boolean isRequired) {
		this.isRequired = isRequired;
		return this;
	}

	public Parameter shouldEncode(boolean shouldEncode) {
		this.shouldEncode = shouldEncode;
		return this;
	}

	public void validate() {
		if (isRequired) {
			// validating required parameters
			if (null == value) {
				throw new NullPointerException("The parameter value is a required parameter and cannot be null.");
			}
		}

	}
}
