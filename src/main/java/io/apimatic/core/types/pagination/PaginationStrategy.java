package io.apimatic.core.types.pagination;

import io.apimatic.core.HttpRequest;

public interface PaginationStrategy {

    /**
     * @param paginatedData Data to be checked for validity
     * @return True if paginated data is valid to make another request
     */
    HttpRequest.Builder apply(PaginatedData<?, ?, ?, ?> paginatedData);

    void addMetaData(PageWrapper<?, ?> page);
}
