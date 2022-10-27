package io.apimatic.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import io.apimatic.coreinterfaces.http.request.MultipartFile;
import io.apimatic.coreinterfaces.http.request.MutliPartRequestType;
import io.apimatic.coreinterfaces.type.functional.Serializer;

/**
 * HTTP parameters consist of a type, a name, and a value. These parameters appear in the header and
 * body of an HTTP request.
 */
public final class Parameter {

    /**
     * A parameter key
     */
    private final String key;

    /**
     * A parameter value
     */
    private final Object value;

    /**
     * A {@link MultipartFile} serializer
     */
    private final Serializer multipartSerializer;

    /**
     * boolean to determine it is required or not
     */
    private final boolean isRequired;

    /**
     * A boolean variable to encode the template parameter
     */
    private final boolean shouldEncode;
    
    /**
     * An instance of {@link MutliPartRequestType}
     */
    private final MutliPartRequestType multiPartRequestType;
    
    /**
     * A map of multipartHeaders
     */
    private final Map<String, List<String>> multipartHeaders;


    /**
     * A private constructor
     * @param key the parameter key
     * @param value the parameter value
     * @param multipartSerializer the serializer for multipart request
     * @param isRequired is used for validation
     * @param shouldEncode is used for encoding template parameter
     * @param multiPartRequest enum used to determine the multiRequestType
     * @param multipartHeaders the multipart headers
     */
    private Parameter(String key, Object value, Serializer multipartSerializer, boolean isRequired,
            boolean shouldEncode, MutliPartRequestType multiPartRequest,
            Map<String, List<String>> multipartHeaders) {
        this.key = key;
        this.value = value;
        this.multipartSerializer = multipartSerializer;
        this.isRequired = isRequired;
        this.shouldEncode = shouldEncode;
        this.multiPartRequestType = multiPartRequest;
        this.multipartHeaders = multipartHeaders;
    }

    /**
     * Getter for the key
     * @return the String key
     */
    public String getKey() {
        return key;
    }

    /**
     * Getter for the shouldEncode
     * @return the boolean shouldEncode which determines that should encode the template parameter
     *         or not
     */
    public boolean shouldEncode() {
        return shouldEncode;
    }

    /**
     * Getter for the parameter value
     * @return the parameter value
     */
    public Object getValue() {
        return value;
    }

    /**
     * Getter for the multipartSerializer
     * @return the {@link Serializer} multipartSerializer
     */
    public Serializer getMultipartSerializer() {
        return multipartSerializer;
    }

    /**
     * Getter for the MultipartRequestType
     * @return the MultiPartRequestType
     */
    public MutliPartRequestType getMultiPartRequest() {
        return multiPartRequestType;
    }

    /**
     * Getter for the multipartHeaders
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
        private Serializer multipartSerializer;
        private boolean isRequired = true;
        private boolean shouldEncode;
        private MutliPartRequestType multiPartRequestType = null;
        private Map<String, List<String>> multipartHeaders = new HashMap<String, List<String>>();

        /**
         * Setter for the key
         * @param key the parameter key
         * @return {@link Parameter.Builder}
         */
        public Builder key(String key) {
            this.key = key;
            return this;
        }

        /**
         * Setter for the value
         * @param value the parameter value
         * @return {@link Parameter.Builder}
         */
        public Builder value(Object value) {
            this.value = value;
            return this;
        }

        /**
         * Setter for the multipartSerializer
         * @param multipartSerializer if the request is {@link MutliPartRequestType} MULTI_PART then
         *        configure the multipartSerializer
         * @return {@link Parameter.Builder}
         */
        public Builder multipartSerializer(Serializer multipartSerializer) {
            this.multipartSerializer = multipartSerializer;
            return this;
        }

        /**
         * Variable determines either we need to validate the parameter
         * @param isRequired the parameter is required or not
         * @return {@link Parameter.Builder}
         */
        public Builder isRequired(boolean isRequired) {
            this.isRequired = isRequired;
            return this;
        }

        /**
         * Setter for the shouldEncode
         * @param shouldEncode the template parameter value shouldEncode or not
         * @return {@link Parameter.Builder}
         */
        public Builder shouldEncode(boolean shouldEncode) {
            this.shouldEncode = shouldEncode;
            return this;
        }

        /**
         * Setter for the multipPart request type
         * @param multiPartRequestType the type of multipart request
         * @return {@link Parameter.Builder}
         */
        public Builder multiPartRequestType(MutliPartRequestType multiPartRequestType) {
            this.multiPartRequestType = multiPartRequestType;
            return this;
        }

        /**
         * Add the multi part headers for the multi part request
         * @param key the header key
         * @param value the header value
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
         * @return the instance of {@link Parameter}
         */
        public Parameter build() {
            return new Parameter(key, value, multipartSerializer, isRequired, shouldEncode,
                    multiPartRequestType, multipartHeaders);
        }
    }

}
