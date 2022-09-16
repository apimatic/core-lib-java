package io.apimatic.core_lib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import io.apimatic.core_interfaces.http.request.MutliPartRequestType;

/**
 * HTTP parameters consist of a type, a name, and a value. These parameters appear in the header and
 * body of an HTTP request.
 *
 */
public class Parameter {

    private final String key;
    private final Object value;
    private final boolean isRequired;
    private final boolean shouldEncode;
    private final MutliPartRequestType multiPartRequestType;
    private final Map<String, List<String>> multipartHeaders;


    /**
     * A private constructor
     * 
     * @param key the parameter key
     * @param value the parameter value
     * @param isRequired is used for validation
     * @param shouldEncode is used for encoding template parameter
     * @param multiPartRequest enum used to determine the multiRequestType
     * @param multipartHeaders the multipart headers
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
     * 
     * @return the String key
     */
    public String getKey() {
        return key;
    }

    /**
     * 
     * @return the boolean shouldEncode which determines that should encode the template parameter
     *         or not
     */
    public boolean shouldEncode() {
        return shouldEncode;
    }

    /**
     * 
     * @return the parameter value
     */
    public Object getValue() {
        return value;
    }

    /**
     * 
     * @return the MultiPartRequestType
     */
    public MutliPartRequestType getMultiPartRequest() {
        return multiPartRequestType;
    }

    /**
     * 
     * @return the Map of headers
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

        /**
         * 
         * @param value
         * @return {@link Parameter.Builder}
         */
        public Builder value(Object value) {
            this.value = value;
            return this;
        }

        /**
         * 
         * @param isRequired
         * @return {@link Parameter.Builder}
         */
        public Builder isRequired(boolean isRequired) {
            this.isRequired = isRequired;
            return this;
        }

        /**
         * 
         * @param shouldEncode
         * @return {@link Parameter.Builder}
         */
        public Builder shouldEncode(boolean shouldEncode) {
            this.shouldEncode = shouldEncode;
            return this;
        }

        /**
         * 
         * @param multiPartRequestType
         * @return {@link Parameter.Builder}
         */
        public Builder multiPartRequestType(MutliPartRequestType multiPartRequestType) {
            this.multiPartRequestType = multiPartRequestType;
            return this;
        }

        /**
         * 
         * @param key
         * @param value
         * @return {@link Parameter.Builder}
         */
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

        /**
         * this method initiates the {@link Parameter}
         * 
         * @return the instance of {@link Parameter}
         */
        public Parameter build() {
            return new Parameter(key, value, isRequired, shouldEncode, multiPartRequestType,
                    multipartHeaders);
        }
    }

}
