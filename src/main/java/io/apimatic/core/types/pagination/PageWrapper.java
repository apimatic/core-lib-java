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
     * @param strategy Pagination strategy to be applied.
     * @return An new instance of PageWrapper.
     */
    public static <I, P> PageWrapper<I, P> create(Response response, P page,
            List<I> items, PaginationStrategy strategy) {
        return new PageWrapper<I, P>(response.getStatusCode(), response.getHeaders(), page,
                items, strategy);
    }

    private final int statusCode;
    private final HttpHeaders headers;
    private final P page;
    private final List<I> items;
    private final PaginationStrategy strategy;

    private String nextLinkInput;
    private int offsetInput;
    private int pageInput;
    private String cursorInput;

    private PageWrapper(int statusCode, final HttpHeaders headers, final P page,
            final List<I> items, final PaginationStrategy strategy) {
        this.statusCode = statusCode;
        this.headers = headers;
        this.page = page;
        this.items = items;
        this.strategy = strategy;
        strategy.addMetaData(this);
    }

    /**
     * @return True if cursor paginated page is wrapped
     */
    public boolean isCursorPagination() {
        return strategy instanceof CursorPagination;
    }

    /**
     * @return True if link paginated page is wrapped
     */
    public boolean isLinkPagination() {
        return strategy instanceof LinkPagination;
    }

    /**
     * @return True if offset paginated page is wrapped
     */
    public boolean isOffsetPagination() {
        return strategy instanceof OffsetPagination;
    }

    /**
     * @return True if number paginated page is wrapped
      */
    public boolean isNumberPagination() {
        return strategy instanceof PagePagination;
    }

    /**
     * Sets the next link input
     * @param strategy value of next link input
     */
    public void setNextLinkInput(String nextLinkInput) {
        this.nextLinkInput = nextLinkInput;
    }

    /**
     * Gets the next link input used for link-based pagination.
     * @return The next page link
     */
    public String getNextLinkInput() {
        return nextLinkInput;
    }

    /**
     * Sets the cursor input
     * @param strategy value of cursor input
     */
    public void setCursorInput(String cursorInput) {
        this.cursorInput = cursorInput;
    }

    /**
     * Gets the cursor input used for cursor-based pagination.
     * @return The cursor token
     */
    public String getCursorInput() {
        return cursorInput;
    }

    /**
     * Sets the offset input
     * @param strategy value of offset input
     */
    public void setOffsetInput(int offsetInput) {
        this.offsetInput = offsetInput;
    }

    /**
     * Gets the offset input used for offset-based pagination.
     * @return The offset value
     */
    public int getOffsetInput() {
        return offsetInput;
    }

    /**
     * Sets the page input
     * @param strategy value of page input
     */
    public void setPageInput(int pageInput) {
        this.pageInput = pageInput;
    }

    /**
     * Gets the page number input used for page-based pagination.
     * @return The page number
     */
    public int getPageInput() {
        return pageInput;
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
