package io.apimatic.core.types.pagination;

import io.apimatic.core.HttpRequest.Builder;

public class PagePagination implements PaginationDataManager {
    private String input;
    private Builder nextReqBuilder;

    /**
     * @param input JsonPointer of a field in request, representing page.
     */
    public PagePagination(final String input) {
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
            int newValue = Integer.parseInt("" + old) + 1;
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
