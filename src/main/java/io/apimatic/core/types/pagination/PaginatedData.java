package io.apimatic.core.types.pagination;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Function;

import com.fasterxml.jackson.core.type.TypeReference;

import io.apimatic.core.ApiCall;
import io.apimatic.core.ErrorCase;
import io.apimatic.core.GlobalConfiguration;
import io.apimatic.core.HttpRequest;
import io.apimatic.core.configurations.http.request.EndpointConfiguration;
import io.apimatic.core.types.CoreApiException;
import io.apimatic.core.utilities.CoreHelper;
import io.apimatic.coreinterfaces.http.response.Response;

public class PaginatedData<T, P> implements Iterator<T> {

    private int currentIndex = 0;

    private List<T> data = new ArrayList<T>();
    private List<P> pages = new ArrayList<P>();
    private int lastDataSize;
    private Response lastResponse;
    private HttpRequest.Builder lastRequestBuilder;

    private TypeReference<P> pageType;
    private Function<P, List<T>> converter;
    private PaginationDataManager[] dataManagers;
    private EndpointConfiguration endpointConfig;
    private GlobalConfiguration globalConfig;

    /**
     * @param paginatedData Existing instance to be cloned.
     */
    public PaginatedData(final PaginatedData<T, P> paginatedData) {
        this.pageType = paginatedData.pageType;
        this.converter = paginatedData.converter;
        this.dataManagers = paginatedData.dataManagers;
        this.endpointConfig = paginatedData.endpointConfig;
        this.globalConfig = paginatedData.globalConfig;

        this.lastDataSize = paginatedData.lastDataSize;
        this.lastResponse = paginatedData.lastResponse;
        this.lastRequestBuilder = paginatedData.lastRequestBuilder;

        this.data.addAll(paginatedData.data);
        this.pages.addAll(paginatedData.pages);
    }

    /**
     * @param config ApiCall configuration that provided this paginated data.
     * @param globalConfig Global configuration that provided this paginated data.
     * @param requestBuilder RequestBuilder that provided this paginated data.
     * @param response Response corresponding to this paginated data instance.
     * @param pageType TypeReference of page type P.
     * @param converter PageType P to list of ItemType T converter
     * @param dataManagers A list of data managers that provided this paginated data.
     *
     * @throws IOException
     */
    public PaginatedData(final EndpointConfiguration config, final GlobalConfiguration globalConfig,
            final HttpRequest.Builder requestBuilder, final Response response,
            final TypeReference<P> pageType, final Function<P, List<T>> converter,
            final PaginationDataManager... dataManagers) throws IOException {
        this.pageType = pageType;
        this.converter = converter;
        this.dataManagers = dataManagers;
        this.endpointConfig = config;
        this.globalConfig = globalConfig;

        updateUsing(response, requestBuilder);
    }

    private void updateUsing(Response response, HttpRequest.Builder requestBuilder)
            throws IOException {
        String responseBody = response.getBody();
        P page = CoreHelper.deserialize(responseBody, pageType);
        List<T> newData = converter.apply(page);

        this.lastDataSize = newData.size();
        this.lastResponse = response;
        this.lastRequestBuilder = requestBuilder;

        this.data.addAll(newData);
        this.pages.add(page);
    }

    /**
     * @return RequestBuilder that provided the last page
     */
    public HttpRequest.Builder getLastRequestBuilder() {
        return lastRequestBuilder;
    }

    /**
     * @return Response body corresponding to the last page
     */
    public String getLastResponseBody() {
        return lastResponse.getBody();
    }

    /**
     * @return Response headers corresponding to the last page
     */
    public String getLastResponseHeaders() {
        return CoreHelper.trySerialize(lastResponse.getHeaders().asSimpleMap());
    }

    /**
     * @return Size of the last page
     */
    public int getLastDataSize() {
        return lastDataSize;
    }

    /**
     * @return Reset this instance and return a clone if its traversed before
     */
    public PaginatedData<T, P> reset() {
        if (currentIndex == 0) {
            return this;
        }

        return new PaginatedData<T, P>(this);
    }

    @Override
    public boolean hasNext() {
        if (currentIndex < data.size()) {
            return true;
        }

        fetchMoreData();

        return currentIndex < data.size();
    }

    @Override
    public T next() {
        if (hasNext()) {
            return data.get(currentIndex++);
        }

        throw new NoSuchElementException("No more data available.");
    }

    /**
     * @return An iterable of items of type T
     */
    public Iterator<T> iterator() {
        return reset();
    }

    /**
     * @return An iterable of pages of type P
     */
    public Iterable<P> pages() {
        PaginatedData<T, P> dataCopy = reset();
        return new Iterable<P>() {
            @Override
            public Iterator<P> iterator() {
                return new Iterator<P>() {
                    private int currentIndex = 0;

                    @Override
                    public boolean hasNext() {
                        if (currentIndex < dataCopy.pages.size()) {
                            return true;
                        }

                        while (dataCopy.hasNext()) {
                            if (currentIndex < dataCopy.pages.size()) {
                                return true;
                            }
                            dataCopy.next();
                        }

                        return false;
                    }

                    @Override
                    public P next() {
                        if (dataCopy.hasNext()) {
                            return dataCopy.pages.get(currentIndex++);
                        }

                        throw new NoSuchElementException("No more data available.");
                    }
                };
            }
        };
    }

    private void fetchMoreData() {
        for (PaginationDataManager manager : dataManagers) {

            if (!manager.isValid(this)) {
                continue;
            }

            try {
                PaginatedData<T, P> result =
                        new ApiCall.Builder<PaginatedData<T, P>, CoreApiException>()
                        .endpointConfiguration(endpointConfig.toBuilder())
                        .globalConfig(globalConfig)
                        .requestBuilder(manager.getNextRequestBuilder())
                        .responseHandler(res -> res
                                .globalErrorCase(Collections.singletonMap(ErrorCase.DEFAULT,
                                        ErrorCase.setReason(null, (reason, context) ->
                                        new CoreApiException(reason, context))))
                                .nullify404(false)
                                .paginatedDeserializer(pageType, converter, r -> r, dataManagers))
                        .build().execute();

                updateUsing(result.lastResponse, result.lastRequestBuilder);
                return;
            } catch (Exception e) {
            }
        }
    }
}
