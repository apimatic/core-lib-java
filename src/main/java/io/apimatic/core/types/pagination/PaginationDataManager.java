package io.apimatic.core.types.pagination;

import io.apimatic.core.HttpRequest.Builder;

public interface PaginationDataManager {

    public abstract boolean isValid(PaginatedData<?> paginatedData);
    public abstract Builder getNextRequestBuilder(PaginatedData<?> paginatedData);
}
