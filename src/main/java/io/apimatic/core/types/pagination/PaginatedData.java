package io.apimatic.core.types.pagination;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Function;

import io.apimatic.core.ApiCall;
import io.apimatic.core.ErrorCase;
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
    private EndpointConfiguration lastEndpointConfig;
    
    private Class<P> pageClass;
    private Function<P, List<T>> converter;
    private PaginationDataManager[] dataManagers;

    public PaginatedData(PaginatedData<T, P> paginatedData) {
        this.pageClass = paginatedData.pageClass;
        this.converter = paginatedData.converter;
        this.dataManagers = paginatedData.dataManagers;

        this.lastDataSize = paginatedData.lastDataSize;
        this.lastResponse = paginatedData.lastResponse;
        this.lastEndpointConfig = paginatedData.lastEndpointConfig;
        
        this.data.addAll(paginatedData.data);
        this.pages.addAll(paginatedData.pages);
    }

    public PaginatedData(Class<P> pageClass, Function<P, List<T>> converter, Response response,
            EndpointConfiguration config, PaginationDataManager... dataManagers) throws IOException {
        this.pageClass = pageClass;
        this.converter = converter;
        this.dataManagers = dataManagers;

        updateUsing(response, config);
    }

    private void updateUsing(Response response, EndpointConfiguration endpointConfig) throws IOException {
        String responseBody = response.getBody();
        P page = CoreHelper.deserialize(responseBody, pageClass);
        List<T> newData = converter.apply(page);
        
        this.lastDataSize = newData.size();
        this.lastResponse = response;
        this.lastEndpointConfig = endpointConfig;
        
        this.data.addAll(newData);
        this.pages.add(page);
    }

    public EndpointConfiguration getLastEndpointConfig() {
        return lastEndpointConfig;
    }

    public String getLastResponse() {
        return lastResponse.getBody();
    }

    public int getLastDataSize() {
        return lastDataSize;
    }

    public PaginatedData<T, P> reset() {
        if (currentIndex == 0)
            return this;
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

    public Iterator<T> iterator() {
        return reset();
    }

    public Iterable<P> pages() {
        PaginatedData<T, P> data = reset();
        return new Iterable<P>() {
            @Override
            public Iterator<P> iterator() {
                return new Iterator<P>() {
                    private int currentIndex = 0;
                    
                    @Override
                    public boolean hasNext() {
                        if (currentIndex < data.pages.size()) {
                            return true;
                        }
                        
                        while (data.hasNext()) {
                            if (currentIndex < data.pages.size()) {
                                return true;
                            }
                            data.next();
                        }

                        return false;
                    }

                    @Override
                    public P next() {
                        if (data.hasNext()) {
                            return data.pages.get(currentIndex++);
                        }

                        throw new NoSuchElementException("No more data available.");
                    }
                };
            }
        };
    }

    public Object convert(Function<PaginatedData<T, P>, ?> returnTypeGetter) {
        return returnTypeGetter.apply(this);
    }

    private void fetchMoreData() {
        for (PaginationDataManager manager : dataManagers) {

            if (!manager.isValid(this)) {
                continue;
            }

            EndpointConfiguration endpointConfig = getLastEndpointConfig();

            try {
                PaginatedData<T, P> result = new ApiCall.Builder<PaginatedData<T, P>, CoreApiException>()
                        .endpointConfiguration(endpointConfig).globalConfig(
                                endpointConfig.getGlobalConfiguration())
                        .requestBuilder(
                                manager.getNextRequestBuilder(this))
                        .responseHandler(res -> res
                                .globalErrorCase(Collections.singletonMap(ErrorCase.DEFAULT,
                                        ErrorCase.setReason(null,
                                                (reason, context) -> new CoreApiException(reason, context))))
                                .nullify404(false)
                                .paginatedDeserializer(pageClass, converter, r -> r, dataManagers))
                        .build().execute();

                updateUsing(result.lastResponse, result.lastEndpointConfig);
                return;
            } catch (Exception e) {
                continue;
            }
        }
    }
}
