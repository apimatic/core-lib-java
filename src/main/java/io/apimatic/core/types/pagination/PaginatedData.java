package io.apimatic.core.types.pagination;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import io.apimatic.core.ApiCall;
import io.apimatic.core.HttpRequest;
import io.apimatic.core.types.CoreApiException;
import io.apimatic.coreinterfaces.http.response.Response;

public class PaginatedData<I, P, R, E extends CoreApiException> {
    private final ApiCall<R, E> firstApiCall;
    private final Function<PageWrapper<I, R>, P> pageCreator;
    private final Function<R, List<I>> itemsCreator;
    private final PaginationStrategy[] strategies;

    private int itemIndex = 0;
    private final List<CheckedSupplier<I, E>> items = new ArrayList<>();
    private CheckedSupplier<P, E> page = null;
    private ApiCall<R, E> apiCall;
    private PaginationStrategy lockedStrategy;
    private boolean canLockStrategy = false;
    private boolean dataClosed = false;

    /**
     * @param apiCall ApiCall instance to be paginated.
     * @param pageCreator Converts the PageWrapper into the instance of type P.
     * @param itemsCreator Extract list of items of type I from response.
     * @param strategies List of applicable pagination strategies.
     */
    public PaginatedData(final ApiCall<R, E> apiCall,
            final Function<PageWrapper<I, R>, P> pageCreator,
            final Function<R, List<I>> itemsCreator,
            final PaginationStrategy... strategies) {
        this.firstApiCall = apiCall;
        this.pageCreator = pageCreator;
        this.itemsCreator = itemsCreator;
        this.strategies = strategies;
        this.apiCall = apiCall;
    }

    /**
     * @return RequestBuilder that provided this page
     */
    public HttpRequest.Builder getRequestBuilder() {
        return apiCall.getRequestBuilder();
    }

    /**
     * @return Response corresponding to this page
     */
    public Response getResponse() {
        return apiCall.getResponse();
    }

    /**
     * @return Size of this page
     */
    public int getPageSize() {
        return items.size();
    }

    /**
     * Get the items in current page converted to type T.
     * @param <T> Represents the type of items in list.
     * @param itemSupplier A converter to convert the CheckedSupplier of items to type T.
     * @return All items in current page converted via itemSupplier.
     */
    public <T> List<T> getItems(Function<CheckedSupplier<I, E>, T> itemSupplier) {
        List<T> convertedItems = new ArrayList<>();
        for (CheckedSupplier<I, E> i : this.items) {
            convertedItems.add(itemSupplier.apply(i));
        }

        return convertedItems;
    }

    /**
     * Get the current page converted to type T.
     * @param <T> Represents the return type.
     * @param pageSupplier A converter to convert the CheckedSupplier of page to type T.
     * @return Current page converted via pageSupplier.
     */
    public <T> T getPage(Function<CheckedSupplier<P, E>, T> pageSupplier) {
        if (page == null) {
            return null;
        }

        return pageSupplier.apply(page);
    }

    /**
     * @param <T> Represents the type of items in iterator.
     * @param itemSupplier A converter to convert the CheckedSupplier of items to type T.
     * @return An Iterator of all items of type T.
     */
    public <T> Iterator<T> items(Function<CheckedSupplier<I, E>, T> itemSupplier) {
        PaginatedData<I, P, R, E> paginatedData = copy();

        return new Iterator<T>() {
            @Override
            public boolean hasNext() {
                if (paginatedData.itemIndex < paginatedData.items.size()) {
                    return true;
                }

                return paginatedData.fetchNextPage();
            }

            @Override
            public T next() {
                if (paginatedData.itemIndex == paginatedData.items.size()) {
                    throw new NoSuchElementException("No more items available.");
                }

                return itemSupplier.apply(paginatedData.items.get(paginatedData.itemIndex++));
            }
        };
    }

    /**
     * @param <T> Represents the type of pages in iterator.
     * @param pageSupplier A converter to convert the CheckedSupplier of page to type T.
     * @return An Iterator of all pages of type T.
     */
    public <T> Iterator<T> pages(Function<CheckedSupplier<P, E>, T> pageSupplier) {
        PaginatedData<I, P, R, E> paginatedData = copy();

        return new Iterator<T>() {
            @Override
            public boolean hasNext() {
                if (paginatedData.page != null) {
                    return true;
                }

                return paginatedData.fetchNextPage();
            }

            @Override
            public T next() {
                if (paginatedData.page == null) {
                    throw new NoSuchElementException("No more pages available.");
                }

                T convertedPage = pageSupplier.apply(paginatedData.page);
                paginatedData.page = null;
                return convertedPage;
            }

        };
    }

    /**
     * @return A copy of this instance of PaginatedData.
     */
    public PaginatedData<I, P, R, E> copy() {
        return new PaginatedData<>(firstApiCall, pageCreator, itemsCreator, strategies);
    }

    /**
     * Start fetching the next page asynchronously.
     * @return A CompletableFuture of boolean instance suggesting if there is a next page or not.
     */
    public CompletableFuture<Boolean> fetchNextPageAsync() {
        if (dataClosed) {
            return CompletableFuture.completedFuture(false);
        }

        for (PaginationStrategy strategy : getStrategies()) {
            HttpRequest.Builder requestBuilder = strategy.apply(this);
            if (requestBuilder == null) {
                continue;
            }

            ApiCall<R, E> newApiCall = this.apiCall.toBuilder()
                    .requestBuilder(requestBuilder)
                    .build();

            return newApiCall.executeAsync()
                    .thenApply(result -> updateWith(newApiCall, result, strategy))
                    .exceptionally(ex -> updateAsFailed(ex.getCause()));
        }

        return CompletableFuture.completedFuture(false);
    }

    private boolean fetchNextPage() {
        if (dataClosed) {
            return false;
        }

        for (PaginationStrategy strategy : getStrategies()) {

            HttpRequest.Builder requestBuilder = strategy.apply(this);
            if (requestBuilder == null) {
                continue;
            }

            try {
                ApiCall<R, E> newApiCall = apiCall.toBuilder()
                    .requestBuilder(requestBuilder).build();
                R pageUnWrapped = newApiCall.execute();

                return updateWith(newApiCall, pageUnWrapped, strategy);
            } catch (IOException | CoreApiException exp) {
                return updateAsFailed(exp);
            }
        }

        return false;
    }

    private PaginationStrategy[] getStrategies() {
        if (lockedStrategy == null) {
            return strategies;
        }

        return new PaginationStrategy[] {lockedStrategy};
    }

    private boolean updateWith(ApiCall<R, E> apiCall, R pageUnWrapped,
            PaginationStrategy strategy) {
        itemIndex = 0;
        this.items.clear();
        this.page = null;

        if (pageUnWrapped == null) {
            return false;
        }

        List<I> itemsUnWrapped = itemsCreator.apply(pageUnWrapped);
        if (itemsUnWrapped == null || itemsUnWrapped.isEmpty()) {
            return false;
        }

        this.apiCall = apiCall;
        PageWrapper<I, R> pageWrapper = PageWrapper.create(apiCall.getResponse(), pageUnWrapped,
                itemsUnWrapped);
        strategy.addMetaData(pageWrapper);
        this.page = CheckedSupplier.create(pageCreator.apply(pageWrapper));
        itemsUnWrapped.forEach(i -> items.add(CheckedSupplier.create(i)));

        if (canLockStrategy) {
            lockedStrategy = strategy;
        } else {
            canLockStrategy = true;
        }

        return true;
    }

    private boolean updateAsFailed(Throwable exp) {
        page = CheckedSupplier.createError(exp);
        itemIndex = 0;
        items.clear();
        items.add(CheckedSupplier.createError(exp));
        dataClosed = true;

        return true;
    }
}
