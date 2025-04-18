package io.apimatic.core.types.pagination;

import io.apimatic.core.HttpRequest.Builder;

public class OffsetPagination implements PaginationDataManager {
    private String input;
    private Builder nextReqBuilder;

    public OffsetPagination(String input) {
        this.input = input;
    }

    @Override
    public boolean isValid(PaginatedData<?, ?> paginatedData) {
        nextReqBuilder = paginatedData.getLastRequestBuilder();

        if (input == null) {
            return false;
        }

        final boolean[] isUpdated = { false };
        nextReqBuilder.updateByReference(input, old -> {
            int newValue = Integer.parseInt("" + old) + paginatedData.getLastDataSize();
            isUpdated[0] = true;
            return newValue;
        });

        return isUpdated[0];
    }

    @Override
    public Builder getNextRequestBuilder() {
        return nextReqBuilder;
    }
}
