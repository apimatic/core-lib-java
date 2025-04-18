package io.apimatic.core.types.pagination;

import io.apimatic.core.HttpRequest.Builder;
import io.apimatic.core.utilities.CoreHelper;

public class CursorPagination implements PaginationDataManager {
    private String output;
    private String input;
    private Builder nextReqBuilder;

    public CursorPagination(String output, String input) {
        this.output = output;
        this.input = input;
    }

    @Override
    public boolean isValid(PaginatedData<?, ?> paginatedData) {
        nextReqBuilder = paginatedData.getLastRequestBuilder();

        String cursorValue = CoreHelper.resolveResponsePointer(output, paginatedData.getLastResponseBody(),
                paginatedData.getLastResponseHeaders());

        if (cursorValue == null) {
            return false;
        }

        final boolean[] isUpdated = { false };
        nextReqBuilder.updateByReference(input, old -> {
            isUpdated[0] = true;
            return cursorValue;
        });

        return isUpdated[0];
    }

    @Override
    public Builder getNextRequestBuilder() {
        return nextReqBuilder;
    }
}
