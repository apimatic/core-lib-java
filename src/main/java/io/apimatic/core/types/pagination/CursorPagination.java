package io.apimatic.core.types.pagination;

import java.util.concurrent.atomic.AtomicBoolean;

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
        AtomicBoolean isUpdated = new AtomicBoolean(false);
        currentRequestCursor = null;

        reqBuilder.updateParameterByJsonPointer(input, old -> {
            if (response == null) {
                currentRequestCursor = (String) old;
                isUpdated.set(true);
                return old;
            }

            String cursorValue = CoreHelper.resolveResponsePointer(output, response);

            if (cursorValue == null) {
                return old;
            }

            currentRequestCursor = cursorValue;
            isUpdated.set(true);
            return cursorValue;
        });

        if (!isUpdated.get() && response == null) {
            return reqBuilder;
        }

        return isUpdated.get() ? reqBuilder : null;
    }

    @Override
    public void addMetaData(PageWrapper<?, ?> page) {
        page.setCursorInput(currentRequestCursor);
    }
}
