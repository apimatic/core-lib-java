package io.apimatic.core_lib.types.http.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.apimatic.core_interfaces.http.HttpHeaders;
import io.apimatic.core_interfaces.http.request.MultipartFile;
import io.apimatic.core_interfaces.type.FileWrapper;

/**
 * Class to wrap file and headers to be sent as part of a multipart request.
 */
public class MultipartFileWrapper implements MultipartFile {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private FileWrapper fileWrapper;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private HttpHeaders headers;

    /**
     * Initialization constructor.
     * 
     * @param fileWrapper FileWrapper instance
     * @param headers Headers for wrapping
     */
    public MultipartFileWrapper(FileWrapper fileWrapper, HttpHeaders headers) {
        this.fileWrapper = fileWrapper;
        this.headers = headers;
    }

    /**
     * Getter for file wrapper.
     * 
     * @return FileWrapper instance
     */
    public FileWrapper getFileWrapper() {
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
