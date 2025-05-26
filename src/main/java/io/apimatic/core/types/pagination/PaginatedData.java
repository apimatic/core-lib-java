package io.apimatic.core.types.pagination;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.stream.Collectors;

import io.apimatic.core.ApiCall;
import io.apimatic.core.HttpRequest;
import io.apimatic.core.types.CoreApiException;
import io.apimatic.coreinterfaces.http.response.Response;

public class PaginatedData<I, P, Res, ExceptionType extends CoreApiException> implements Iterator<ItemWrapper<I, ExceptionType>> {

    private final ApiCall<Res, ExceptionType> firstApiCall;
    private final Function<PageWrapper<I, Res, ExceptionType>, P> responseToPage;
    private final Function<Res, List<I>> responseToItems;
    private final PaginationStrategy[] strategies;
    
    private int currentIndex = 0;
    private List<ItemWrapper<I, ExceptionType>> items = new ArrayList<>();
    private PageWrapper<I, Res, ExceptionType> page = null;
    private int lastPageSize = 0;
    private ApiCall<Res, ExceptionType> apiCall;
    private boolean caughtFailure = false;

    public PaginatedData(final ApiCall<Res, ExceptionType> apiCall,
            final Function<PageWrapper<I, Res, ExceptionType>, P> responseToPage,
            final Function<Res, List<I>> responseToItems,
            final PaginationStrategy... dataManagers) {
        this.firstApiCall = apiCall;
        this.responseToPage = responseToPage;
        this.responseToItems = responseToItems;
        this.strategies = dataManagers;
        this.apiCall = apiCall;
    }

    /**
     * @return RequestBuilder that provided the last page
     */
    public HttpRequest.Builder getRequestBuilder() {
        return apiCall.getRequestBuilder();
    }

    /**
     * @return Response corresponding to the last page
     */
    public Response getResponse() {
        return apiCall.getResponse();
    }

    /**
     * @return Size of the last page
     */
    public int getLastPageSize() {
        return lastPageSize;
    }

    @Override
    public boolean hasNext() {
        if (currentIndex < items.size()) {
            return true;
        }

        return fetchNextPage();
    }

    @Override
    public ItemWrapper<I, ExceptionType> next() {
        if (hasNext()) {
            return items.get(currentIndex++);
        }

        throw new NoSuchElementException("No more data available.");
    }

    /**
     * @return An iterable of items of type T
     */
    public <T> Iterator<T> iterator(Function<ItemWrapper<I,ExceptionType>, T> converter) {
        PaginatedData<I, P, Res, ExceptionType> paginatedData = new PaginatedData<>(
                firstApiCall, responseToPage, responseToItems, strategies);
        
        return new Iterator<T>() {

            @Override
            public boolean hasNext() {
                return paginatedData.hasNext();
            }

            @Override
            public T next() {
                return converter.apply(paginatedData.next());
            }
        };
    }

    /**
     * @return An iterable of pages of type P
     */
    public Iterable<P> pages() {
        PaginatedData<I, P, Res, ExceptionType> paginatedData = new PaginatedData<>(
                firstApiCall, responseToPage, responseToItems, strategies);
        return new Iterable<P>() {
            @Override
            public Iterator<P> iterator() {
                return new Iterator<P>() {
                    @Override
                    public boolean hasNext() {
                        return paginatedData.fetchNextPage();
                    }

                    @Override
                    public P next() {
                        return responseToPage.apply(paginatedData.page);
                    }
                };
            }
        };
    }

    private boolean fetchNextPage() {
        if (caughtFailure) {
            return false;
        }

        for (PaginationStrategy strategy : strategies) {

            HttpRequest.Builder requestBuilder = strategy.apply(this);
            if (requestBuilder == null) {
                continue;
            }
            
            boolean isUpdated = tryUpdatingPage(requestBuilder);
            if (isUpdated) {
                strategy.addMetaData(page);
            }

            return isUpdated;
        }
        return false;
    }

    private boolean tryUpdatingPage(HttpRequest.Builder requestBuilder) {
        ApiCall<Res, ExceptionType> apiCall = this.apiCall.toBuilder()
                .requestBuilder(requestBuilder).build();
        PageWrapper<I, Res, ExceptionType> page;
        List<ItemWrapper<I, ExceptionType>> items;

        try {
            Res pageUnWrapped = apiCall.execute();
            if (pageUnWrapped == null) {
                return false;
            }
            List<I> itemsUnWrapped = responseToItems.apply(pageUnWrapped);
            if (itemsUnWrapped == null) {
                return false;
            }
            page = new PageWrapper<>(apiCall.getResponse().getStatusCode(),
                    apiCall.getResponse().getHeaders(), pageUnWrapped, itemsUnWrapped);
            items = itemsUnWrapped.stream().map(i -> new ItemWrapper<I, ExceptionType>() {
                @Override
                public I get() throws ExceptionType, IOException {
                    return i;
                }
            }).collect(Collectors.toList());
        } catch (IOException | CoreApiException exp) {
            caughtFailure = true;
            page = PageWrapper.CreateError(exp);
            items = Arrays.asList(new ItemWrapper<I, ExceptionType>() {
                @Override
                public I get() throws ExceptionType, IOException {
                    throw exp;
                }
            });
        }
            
        return updateWith(apiCall, page, items);
    }

    private boolean updateWith(ApiCall<Res, ExceptionType> apiCall,
            PageWrapper<I, Res, ExceptionType> page, List<ItemWrapper<I, ExceptionType>> items) {
        if (items.size() == 0) {
            return false;
        }
        
        currentIndex = 0;
        this.apiCall = apiCall;
        this.page = page;
        this.items = items;
        lastPageSize = items.size();
        
        return true;
    }
}
