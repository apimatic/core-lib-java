package io.apimatic.core.types.pagination;

import java.util.concurrent.atomic.AtomicBoolean;

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
        AtomicBoolean isUpdated = new AtomicBoolean(false);

        reqBuilder.updateParameterByJsonPointer(input, old -> {
            int oldValue = Integer.parseInt("" + old);

            if (response == null) {
                currentRequestPageNumber = oldValue;
                isUpdated.set(true);
                return old;
            }

            int newValue = oldValue + 1;
            currentRequestPageNumber = newValue;
            isUpdated.set(true);
            return newValue;
        });
        
        if (!isUpdated.get() && response == null) {
            return reqBuilder;
        }

        return isUpdated.get() ? reqBuilder : null;
    }

    @Override
    public void addMetaData(PageWrapper<?, ?> page) {
        page.setPageInput(currentRequestPageNumber);
        currentRequestPageNumber = -1;
    }
}
