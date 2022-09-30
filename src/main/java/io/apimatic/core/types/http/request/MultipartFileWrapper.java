package io.apimatic.core.types.http.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.apimatic.coreinterfaces.http.HttpHeaders;
import io.apimatic.coreinterfaces.http.request.MultipartFile;
import io.apimatic.coreinterfaces.type.CoreFileWrapper;

/**
 * To wrap file and headers to be sent as part of a multipart request.
 */
public class MultipartFileWrapper implements MultipartFile {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private CoreFileWrapper fileWrapper;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private HttpHeaders headers;

    /**
     * Initialization constructor.
     * 
     * @param fileWrapper FileWrapper instance
     * @param headers Headers for wrapping
     */
    public MultipartFileWrapper(CoreFileWrapper fileWrapper, HttpHeaders headers) {
        this.fileWrapper = fileWrapper;
        this.headers = headers;
    }

    /**
     * Getter for file wrapper.
     * 
     * @return FileWrapper instance
     */
    public CoreFileWrapper getFileWrapper() {
        return fileWrapper;
    }

    /**
     * Getter for headers.
     * 
     * @return Headers
     */
    public HttpHeaders getHeaders() {
        return headers;
    }
}
