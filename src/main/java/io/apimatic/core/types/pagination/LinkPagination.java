package io.apimatic.core.types.pagination;

import io.apimatic.core.HttpRequest.Builder;
import io.apimatic.core.utilities.CoreHelper;

public class LinkPagination implements PaginationDataManager {
    private String next;
    private Builder nextReqBuilder;

    /**
     * @param next JsonPointer of a field in response, representing next request queryUrl.
     */
    public LinkPagination(final String next) {
        this.next = next;
    }

    @Override
    public boolean isValid(PaginatedData<?, ?> paginatedData) {
        nextReqBuilder = paginatedData.getLastRequestBuilder();

        String linkValue = CoreHelper.resolveResponsePointer(next,
                paginatedData.getLastResponseBody(), paginatedData.getLastResponseHeaders());

        if (linkValue == null) {
            return false;
        }

        nextReqBuilder.queryParam(CoreHelper.getQueryParameters(linkValue));

        return true;
    }

    @Override
    public Builder getNextRequestBuilder() {
        return nextReqBuilder;
    }
}
