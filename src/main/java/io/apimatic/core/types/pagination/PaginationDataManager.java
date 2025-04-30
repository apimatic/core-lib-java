package io.apimatic.core.types.pagination;

import io.apimatic.core.HttpRequest.Builder;

public interface PaginationDataManager {

    /**
     * @param paginatedData Data to be checked for validity
     * @return True if paginated data is valid to make another request
     */
    boolean isValid(PaginatedData<?, ?> paginatedData);

    /**
     * @return RequestBuilder for the next API Call
     */
    Builder getNextRequestBuilder();
}
