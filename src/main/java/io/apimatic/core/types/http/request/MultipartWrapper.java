package io.apimatic.core.types.http.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.apimatic.coreinterfaces.http.HttpHeaders;
import io.apimatic.coreinterfaces.http.request.Multipart;

/**
 * To wrap byteArray and headers to be sent as part of a multipart request.
 */
public class MultipartWrapper implements Multipart {

    /**
     * A string of serialized Object
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String serializedObj;

    /**
     * An instance of {@link HttpHeaders}
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private HttpHeaders headers;

    /**
     * Initialization constructor.
     * @param serializedObj Serialized string of object to be wrapped.
     * @param headers Headers for wrapping
     */
    public MultipartWrapper(final String serializedObj, final HttpHeaders headers) {
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
