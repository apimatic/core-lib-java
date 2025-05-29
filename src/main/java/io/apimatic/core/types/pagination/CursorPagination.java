package io.apimatic.core.types.pagination;

import io.apimatic.core.HttpRequest.Builder;
import io.apimatic.core.utilities.CoreHelper;
import io.apimatic.coreinterfaces.http.response.Response;

public class CursorPagination implements PaginationStrategy {
    private final String output;
    private final String input;
    private String currentRequestCursor;

    /**
     * @param output JsonPointer of a field received in the response, representing next cursor.
     * @param input JsonPointer of a field in request, representing cursor.
     */
    public CursorPagination(final String output, final String input) {
        this.output = output;
        this.input = input;
    }

    @Override
    public Builder apply(PaginatedData<?, ?, ?, ?> paginatedData) {
        Response response = paginatedData.getResponse();
        Builder reqBuilder = paginatedData.getRequestBuilder();
        final boolean[] isUpdated = {false};

        reqBuilder.updateByReference(input, old -> {
            
            if (response == null) {
                currentRequestCursor = (String) old;
                isUpdated[0] = true;
                return old;
            }

            String cursorValue = CoreHelper.resolveResponsePointer(output, response);

            if (cursorValue == null) {
                return old;
            }

            currentRequestCursor = cursorValue;
            isUpdated[0] = true;
            return cursorValue;
        });

        return isUpdated[0] ? reqBuilder : null;
    }

    @Override
    public void addMetaData(PageWrapper<?, ?> page) {
        page.setCursorInput(currentRequestCursor);
        currentRequestCursor = null;
    }
}
