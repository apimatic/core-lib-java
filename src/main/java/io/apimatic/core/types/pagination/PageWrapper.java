package io.apimatic.core.types.pagination;

import java.util.List;

import io.apimatic.coreinterfaces.http.HttpHeaders;
import io.apimatic.coreinterfaces.http.response.ApiResponseType;
import io.apimatic.coreinterfaces.http.response.Response;

public final class PageWrapper<I, P> implements ApiResponseType<P> {

    /**
     * Create an instance of PageWrapper with provided page and meta data.
     * @param <I> Represent type of items in the page.
     * @param <P> Represent type of page.
     * @param response Response from API call.
     * @param page Page to be wrapped.
     * @param items Extracted items from the page.
     * @return An new instance of PageWrapper.
     */
    public static <I, P> PageWrapper<I, P> create(Response response, P page, List<I> items) {
        return new PageWrapper<I, P>(response.getStatusCode(), response.getHeaders(), page, items);
    }

    private final int statusCode;
    private final HttpHeaders headers;
    private P page;
    private List<I> items;

    private String nextLinkInput = null;
    private int offsetInput = -1;
    private int pageInput = -1;
    private String cursorInput = null;

    private PageWrapper(int statusCode, final HttpHeaders headers, final P page,
            final List<I> items) {
        this.statusCode = statusCode;
        this.headers = headers;
        this.page = page;
        this.items = items;
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
     * @return Content
     */
    public P getResult() {
        return page;
    }

    /**
     * Content of the page.
     * @return List of items on this page.
     */
    public List<I> getItems() {
        return items;
    }
}
