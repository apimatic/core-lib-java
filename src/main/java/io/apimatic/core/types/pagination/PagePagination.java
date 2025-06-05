package io.apimatic.core.types.pagination;

import io.apimatic.core.HttpRequest.Builder;
import io.apimatic.coreinterfaces.http.response.Response;

public class PagePagination implements PaginationStrategy {
    private final String input;
    private int currentRequestPageNumber = -1;

    /**
     * @param input JsonPointer of a field in request, representing page.
     */
    public PagePagination(final String input) {
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
                currentRequestPageNumber = oldValue;
                isUpdated[0] = true;
                return old;
            }

            int newValue = oldValue + 1;
            currentRequestPageNumber = newValue;
            isUpdated[0] = true;
            return newValue;
        });
        
        if (!isUpdated[0] && response == null) {
            return reqBuilder;
        }

        return isUpdated[0] ? reqBuilder : null;
    }

    @Override
    public void addMetaData(PageWrapper<?, ?> page) {
        page.setPageInput(currentRequestPageNumber);
        currentRequestPageNumber = -1;
    }
}
