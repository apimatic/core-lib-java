package io.apimatic.core.types.pagination;

import io.apimatic.core.HttpRequest.Builder;
import io.apimatic.core.utilities.CoreHelper;

public class CursorPagination implements PaginationDataManager {
    private final String output;
    private final String input;
    private Builder nextReqBuilder;

    /**
     * @param output JsonPointer of a field received in the response, representing next cursor.
     * @param input JsonPointer of a field in request, representing cursor.
     */
    public CursorPagination(final String output, final String input) {
        this.output = output;
        this.input = input;
    }

    @Override
    public boolean isValid(PaginatedData<?, ?> paginatedData) {
        nextReqBuilder = paginatedData.getLastRequestBuilder();

        String cursorValue = CoreHelper.resolveResponsePointer(output,
                paginatedData.getLastResponseBody(), paginatedData.getLastResponseHeaders());

        if (cursorValue == null) {
            return false;
        }

        final boolean[] isUpdated = {false};
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
