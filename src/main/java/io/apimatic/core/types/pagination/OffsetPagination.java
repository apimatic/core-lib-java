package io.apimatic.core.types.pagination;

import io.apimatic.core.HttpRequest.Builder;
import io.apimatic.coreinterfaces.http.response.Response;

public class OffsetPagination implements PaginationStrategy {
    private final String input;
    private int currentRequestOffset = -1;

    /**
     * @param input JsonPointer of a field in request, representing offset.
     */
    public OffsetPagination(final String input) {
        this.input = input;
    }

    @Override
    public Builder apply(PaginatedData<?, ?, ?, ?> paginatedData) {
        Response response = paginatedData.getResponse();
        Builder reqBuilder = paginatedData.getRequestBuilder();
        final boolean[] isUpdated = {false};
        
        reqBuilder.updateByReference(input, old -> {
            int oldValue = Integer.parseInt("" + old);

            if (response == null) {
                currentRequestOffset = oldValue;
                isUpdated[0] = true;
                return old;
            }

            int newValue = oldValue + paginatedData.getLastPageSize();
            currentRequestOffset = newValue;
            isUpdated[0] = true;
            return newValue;
        });

        return isUpdated[0] ? reqBuilder : null;
    }

    @Override
    public void addMetaData(PageWrapper<?, ?, ?> page) {
        page.setOffsetInput(currentRequestOffset);
        currentRequestOffset = -1;
    }
}
