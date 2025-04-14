package io.apimatic.core.types.pagination;

import java.util.Map;

import io.apimatic.core.HttpRequest.Builder;

public class OffsetPagination implements PaginationDataManager {
    private String input;
    private Builder nextReqBuilder;

    public OffsetPagination input(String input) {
        this.input = input;
        return this;
    }

    @Override
    public boolean isValid(PaginatedData<?> paginatedData) {
        if (input == null) {
            return false;
        }

        try {
            Builder lastRequest = paginatedData.getLastEndpointConfiguration().getRequestBuilder();
            Map<String, Object> reqQuery = lastRequest
                    .build(paginatedData.getLastEndpointConfiguration().getGlobalConfiguration()).getQueryParameters();

            if (input != null && reqQuery.containsKey(input)) {
                Integer nextOffsetValue = Integer.parseInt("" + reqQuery.get(input)) + paginatedData.getLastDataSize();
                nextReqBuilder = lastRequest.queryParam(q -> q.key(input).value(nextOffsetValue));
                return true;
            }

        } catch (Exception e) {
        }

        return false;
    }

    @Override
    public Builder getNextRequestBuilder(PaginatedData<?> paginatedData) {
        return nextReqBuilder;
    }
}
