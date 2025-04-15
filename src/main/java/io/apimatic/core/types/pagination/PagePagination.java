package io.apimatic.core.types.pagination;

import java.util.Map;

import io.apimatic.core.HttpRequest.Builder;

public class PagePagination  implements PaginationDataManager {
    private String input;
    private Builder nextReqBuilder;

    public PagePagination(String input) {
        this.input = input;
    }

    @Override
    public boolean isValid(PaginatedData<?, ?> paginatedData) {
        if (input == null) {
            return false;
        }

        try {
            Builder lastRequest = paginatedData.getLastEndpointConfig().getRequestBuilder();
            Map<String, Object> reqQuery = lastRequest.build(paginatedData.getLastEndpointConfig().getGlobalConfiguration())
                    .getQueryParameters();

            if (input != null && reqQuery.containsKey(input)) {
                Integer newPageValue = Integer.parseInt((String) reqQuery.get(input)) + 1;
                nextReqBuilder = lastRequest.queryParam(q -> q.key(input).value(newPageValue));
                return true;
            }
            
        } catch (Exception e) {
        }
        
        return false;
    }

    @Override
    public Builder getNextRequestBuilder(PaginatedData<?, ?> paginatedData) {
        return nextReqBuilder;
    }
}
