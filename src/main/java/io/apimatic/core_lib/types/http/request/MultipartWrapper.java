/*
 * TesterLib
 *
 * This file was automatically generated for Stamplay by APIMATIC v3.0 ( https://www.apimatic.io ).
 */

package io.apimatic.core_lib.types.http.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.apimatic.core_interfaces.http.HttpHeaders;

/**
 * Class to wrap byteArray and headers to be sent as part of a multipart request.
 */
public class MultipartWrapper implements io.apimatic.core_interfaces.http.request.MultipartWrapper {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String serializedObj;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private HttpHeaders headers;

    /**
     * Initialization constructor.
     * @param serializedObj Serialized string of object to be wrapped.
     * @param headers Headers for wrapping
     */
    public MultipartWrapper(String serializedObj, HttpHeaders headers) {
        this.serializedObj = serializedObj;
        this.headers = headers;
    }

    /**
     * Getter for bytes.
     * @return Array of bytes.
     */
    public byte[] getByteArray() {
        return serializedObj.getBytes();
    }

    /**
     * Getter for headers.
     * @return headers
     */
    public HttpHeaders getHeaders() {
        return headers;
    }
}
