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

public class PaginatedData<I, P, Res, ExceptionType extends CoreApiException> {

    private final ApiCall<Res, ExceptionType> firstApiCall;
    private final Function<PageWrapper<I, Res>, P> pageCreator;
    private final Function<Res, List<I>> itemsCreator;
    private final PaginationStrategy[] strategies;
    
    private int itemIndex = 0;
    private final List<CheckedSupplier<I, ExceptionType>> items = new ArrayList<>();
    private CheckedSupplier<P, ExceptionType> page = null;
    private PaginationStrategy lockedStrategy;
    private boolean canLockStrategy = false;
    private ApiCall<Res, ExceptionType> apiCall;
    private boolean dataClosed = false;

    public PaginatedData(final ApiCall<Res, ExceptionType> apiCall,
            final Function<PageWrapper<I, Res>, P> pageCreator,
            final Function<Res, List<I>> itemsCreator,
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

    public <T> List<T> getItems(Function<CheckedSupplier<I, ExceptionType>, T> itemCreator) {
        List<T> items = new ArrayList<>();
        for (CheckedSupplier<I,ExceptionType> i : this.items) {
            items.add(itemCreator.apply(i));
        }
        
        return items;
    }

    public <T> T getPage(Function<CheckedSupplier<P, ExceptionType>, T> pageSupplier) {
        if (page == null) {
            return null;
        }

        return pageSupplier.apply(page);
    }

    /**
     * @return An Iterator of items of type T
     */
    public <T> Iterator<T> items(Function<CheckedSupplier<I, ExceptionType>, T> itemSupplier) {
        PaginatedData<I, P, Res, ExceptionType> paginatedData = copy();

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
     * @return An Iterator of pages of type T
     */
    public <T> Iterator<T> pages(Function<CheckedSupplier<P, ExceptionType>, T> pageSupplier) {
        PaginatedData<I, P, Res, ExceptionType> paginatedData = copy();

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

                T page = pageSupplier.apply(paginatedData.page);
                paginatedData.page = null;
                return page;
            }
        };
    }
    
    public PaginatedData<I, P, Res, ExceptionType> copy() {
        return new PaginatedData<>(firstApiCall, pageCreator, itemsCreator, strategies);
    }

    public CompletableFuture<Boolean> fetchNextPageAsync() {
        if (dataClosed) {
            return CompletableFuture.completedFuture(false);
        }

        for (PaginationStrategy strategy : getStrategies()) {
            HttpRequest.Builder requestBuilder = strategy.apply(this);
            if (requestBuilder == null) continue;

            ApiCall<Res, ExceptionType> newApiCall = this.apiCall.toBuilder()
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
                ApiCall<Res, ExceptionType> apiCall = this.apiCall.toBuilder()
                    .requestBuilder(requestBuilder).build();
                Res pageUnWrapped = apiCall.execute();

                return updateWith(apiCall, pageUnWrapped, strategy);
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

        return new PaginationStrategy[] { lockedStrategy };
    }

    private boolean updateWith(ApiCall<Res, ExceptionType> apiCall, Res pageUnWrapped, PaginationStrategy strategy) {
        itemIndex = 0;
        this.items.clear();
        this.page = null;
        
        if (pageUnWrapped == null) {
            return false;
        }

        List<I> itemsUnWrapped = itemsCreator.apply(pageUnWrapped);
        if (itemsUnWrapped == null || itemsUnWrapped.size() == 0) {
            return false;
        }

        this.apiCall = apiCall;
        PageWrapper<I, Res> pageWrapper = PageWrapper.Create(apiCall.getResponse(), pageUnWrapped, itemsUnWrapped);
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
