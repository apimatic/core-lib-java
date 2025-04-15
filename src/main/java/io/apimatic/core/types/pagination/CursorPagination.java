package io.apimatic.core.types.pagination;

import io.apimatic.core.HttpRequest.Builder;
import io.apimatic.core.utilities.CoreHelper;

public class CursorPagination implements PaginationDataManager {
    private String output;
    private String input;
    private String cursorValue;

    public CursorPagination(String output, String input) {
        this.output = output;
        this.input = input;
    }

    @Override
    public boolean isValid(PaginatedData<?, ?> paginatedData) {
        String responseBody = paginatedData.getLastResponse();
        cursorValue = CoreHelper.getValueFromJson(output, responseBody);

        if (cursorValue == null) {
            return false;
        }

        return true;
    }

    @Override
    public Builder getNextRequestBuilder(PaginatedData<?, ?> paginatedData) {
        Builder lastRequestBuilder = paginatedData.getLastEndpointConfig().getRequestBuilder();
        return lastRequestBuilder.queryParam(q -> q.key(input).value(cursorValue));
    }
}
