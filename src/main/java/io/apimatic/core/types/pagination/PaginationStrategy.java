package io.apimatic.core.types.pagination;

import io.apimatic.core.HttpRequest;

public interface PaginationStrategy {

    /**
     * @param paginatedData Data to be processed for validity.
     * @return HttpRequest.Builder if paginated data is valid to make another request.
     */
    HttpRequest.Builder apply(PaginatedData<?, ?, ?, ?> paginatedData);

    /**
     * @param page A pageWrapper instance that will be updated with meta data.
     */
    void addMetaData(PageWrapper<?, ?> page);
}
