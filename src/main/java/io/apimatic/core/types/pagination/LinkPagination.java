package io.apimatic.core.types.pagination;

import io.apimatic.core.HttpRequest.Builder;
import io.apimatic.core.utilities.CoreHelper;

public class LinkPagination implements PaginationDataManager {
    private String next;
    private String linkValue;

    public LinkPagination(String next) {
        this.next = next;
    }

    @Override
    public boolean isValid(PaginatedData<?, ?> paginatedData) {
        String responseBody = paginatedData.getLastResponse();
        linkValue = CoreHelper.getValueFromJson(next, responseBody);

        if (linkValue == null) {
            return false;
        }

        return true;
    }

    @Override
    public Builder getNextRequestBuilder(PaginatedData<?, ?> paginatedData) {
        Builder lastRequestBuilder = paginatedData.getLastEndpointConfig().getRequestBuilder();
        return lastRequestBuilder.queryParam(CoreHelper.getQueryParameters(linkValue));
    }
}
