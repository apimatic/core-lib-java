package io.apimatic.core_lib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import io.apimatic.core_interfaces.http.request.MutliPartRequestType;

public class Parameter {

    private final String key;
    private final Object value;
    private final boolean isRequired;
    private final boolean shouldEncode;
    private final MutliPartRequestType multiPartRequestType;
    private final Map<String, List<String>> multipartHeaders;


    /**
     * @param key
     * @param value
     * @param isRequired
     * @param shouldEncode
     * @param multiPartRequest
     * @param multipartHeaders
     */
    private Parameter(String key, Object value, boolean isRequired, boolean shouldEncode,
            MutliPartRequestType multiPartRequest, Map<String, List<String>> multipartHeaders) {
        super();
        this.key = key;
        this.value = value;
        this.isRequired = isRequired;
        this.shouldEncode = shouldEncode;
        this.multiPartRequestType = multiPartRequest;
        this.multipartHeaders = multipartHeaders;
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
    public boolean shouldEncode() {
        return shouldEncode;
    }

    /**
     * @return the value
     */
    public Object getValue() {
        return value;
    }

    /**
     * @return the multiPartRequest
     */
    public MutliPartRequestType getMultiPartRequest() {
        return multiPartRequestType;
    }

    /**
     * @return the multipartHeaders
     */
    public Map<String, List<String>> getMultipartHeaders() {
        return multipartHeaders;
    }

    /**
     * Validate the parameter fields
     */
    public void validate() {
        if (isRequired) {
            // validating required parameters
            if (null == value) {
                throw new NullPointerException(
                        "The parameter value is a required parameter and cannot be null.");
            }
        }

    }

    public static class Builder {
        private String key;
        private Object value;
        private boolean isRequired = true;
        private boolean shouldEncode;
        private MutliPartRequestType multiPartRequestType = null;
        private Map<String, List<String>> multipartHeaders = new HashMap<String, List<String>>();

        public Builder key(String key) {
            this.key = key;
            return this;
        }

        public Builder value(Object value) {
            this.value = value;
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

        public Builder multiPartRequestType(MutliPartRequestType multiPartRequestType) {
            this.multiPartRequestType = multiPartRequestType;
            return this;
        }

        public Builder multipartHeaders(String key, String value) {
            if (multipartHeaders.containsKey(key)) {
                multipartHeaders.get(key).add(value);
            } else {
                List<String> headerValues = new ArrayList<String>();
                headerValues.add(value);
                multipartHeaders.put(key, headerValues);
            }
            return this;
        }

        public Parameter build() {
            return new Parameter(key, value, isRequired, shouldEncode, multiPartRequestType,
                    multipartHeaders);

        }
    }

}
