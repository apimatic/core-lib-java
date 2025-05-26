package io.apimatic.core.types.pagination;

import io.apimatic.core.HttpRequest.Builder;
import io.apimatic.core.utilities.CoreHelper;
import io.apimatic.coreinterfaces.http.response.Response;

public class LinkPagination implements PaginationStrategy {
    private final String next;
    private String currentRequestLink;

    /**
     * @param next JsonPointer of a field in response, representing next request queryUrl.
     */
    public LinkPagination(final String next) {
        this.next = next;
    }

    @Override
    public Builder apply(PaginatedData<?, ?, ?, ?> paginatedData) {
        Response response = paginatedData.getResponse();

        if (response == null) {
            currentRequestLink = paginatedData.getRequestBuilder().getQueryUrl();
            return paginatedData.getRequestBuilder();
        }

        String linkValue = CoreHelper.resolveResponsePointer(next, response);

        if (linkValue == null) {
            return null;
        }
        currentRequestLink = linkValue;

        return paginatedData.getRequestBuilder()
                .queryParam(CoreHelper.getQueryParameters(linkValue));
    }

    @Override
    public void addMetaData(PageWrapper<?, ?, ?> page) {
        page.setNextLinkInput(currentRequestLink);
        currentRequestLink = null;
    }
}
