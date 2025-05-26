package io.apimatic.core.types.pagination;

import java.io.IOException;
import java.util.List;

import io.apimatic.core.types.CoreApiException;
import io.apimatic.coreinterfaces.http.HttpHeaders;
public class PageWrapper<I, P, E extends CoreApiException> {
    
    @SuppressWarnings("unchecked")
    public static <I, P, E extends CoreApiException> PageWrapper<I, P, E> CreateError(
            Exception exception) {
        if (exception instanceof CoreApiException) {
            return new PageWrapper<I, P, E>((E) exception);
        }

        if (exception instanceof IOException) {
            return new PageWrapper<I, P, E>((IOException) exception);
        }

        return null;
    }
    
    private final int statusCode;
    private final HttpHeaders headers;
    private P result;
    private List<I> items;
    private E apiException = null;
    private IOException ioException = null;
    
    private String nextLinkInput = null;
    private int offsetInput = -1;
    private int pageInput = -1;
    private String cursorInput = null;

    public PageWrapper(int statusCode, HttpHeaders headers, P result, List<I> items) {
        this.statusCode = statusCode;
        this.headers = headers;
        this.result = result;
        this.items = items;
    }

    private PageWrapper(E apiException) {
        this.statusCode = apiException.getResponseCode();
        this.headers = apiException.getHttpContext().getResponse().getHeaders();
        this.apiException = apiException;
    }

    private PageWrapper(IOException ioException) {
        this.statusCode = 0;
        this.headers = null;
        this.ioException = ioException;
    }

    public void setNextLinkInput(String nextLinkInput) {
        this.nextLinkInput = nextLinkInput;
    }

    public void setOffsetInput(int offsetInput) {
        this.offsetInput = offsetInput;
    }

    public void setPageInput(int pageInput) {
        this.pageInput = pageInput;
    }

    public void setCursorInput(String cursorInput) {
        this.cursorInput = cursorInput;
    }

    /**
     * Gets the next link input used for link-based pagination.
     * @return The next page link
     */
    public String getNextLinkInput() {
        return nextLinkInput;
    }

    /**
     * Gets the offset input used for offset-based pagination.
     * @return The offset value
     */
    public int getOffsetInput() {
        return offsetInput;
    }

    /**
     * Gets the page number input used for page-based pagination.
     * @return The page number
     */
    public int getPageInput() {
        return pageInput;
    }

    /**
     * Gets the cursor input used for cursor-based pagination.
     * @return The cursor token
     */
    public String getCursorInput() {
        return cursorInput;
    }

    /**
     * HTTP Status code of the api response.
     * @return Int status code
     */
    public int getStatusCode() {
        return statusCode;
    }

    /**
     * Headers of the http response.
     * @return Headers
     */
    public HttpHeaders getHeaders() {
        return headers;
    }

    /**
     * Content of the page.
     * @return List of items on this page.
     * @throws E
     * @throws IOException
     */
    public List<I> getItems() throws E, IOException {
        if (ioException != null) {
            throw ioException;
        }

        if (apiException != null) {
            throw apiException;
        }

        return items;
    }

    /**
     * Content of the page.
     * @return Content
     * @throws E
     * @throws IOException
     */
    public P getResult() throws E, IOException {
        if (ioException != null) {
            throw ioException;
        }

        if (apiException != null) {
            throw apiException;
        }

        return result;
    }
}
