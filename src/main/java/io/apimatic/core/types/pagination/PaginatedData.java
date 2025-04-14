package io.apimatic.core.types.pagination;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import io.apimatic.core.ApiCall;
import io.apimatic.core.ErrorCase;
import io.apimatic.core.configurations.http.request.EndpointConfiguration;
import io.apimatic.core.types.CoreApiException;
import io.apimatic.coreinterfaces.http.response.Response;
import io.apimatic.coreinterfaces.type.functional.Deserializer;

public class PaginatedData<T> implements Iterator<T> {

    private int currentIndex = 0;

    private List<T> data = new ArrayList<T>();
    private int lastDataSize;
    private Response lastResponse;
    private EndpointConfiguration lastEndpointConfig;
    Deserializer<List<T>> deserializer;

    private PaginationDataManager[] paginationDataManagers;

    private PaginatedData(final Deserializer<List<T>> deserializer, final Response response,
            final EndpointConfiguration config, final PaginationDataManager... dataManagers) throws IOException {
        data.addAll(deserializer.apply(response.getBody()));
        lastDataSize = data.size();
        lastResponse = response;
        lastEndpointConfig = config;
        this.deserializer = deserializer;
        paginationDataManagers = dataManagers;
    }

    public static <T> PaginatedIterable<T> Create(Deserializer<List<T>> deserializer,
            EndpointConfiguration endPointConfig, Response response, final PaginationDataManager... dataManagers)
            throws IOException {
        return new PaginatedIterable<T>(new PaginatedData<T>(deserializer, response, endPointConfig, dataManagers));
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

    public Iterator<T> reset() {
        currentIndex = 0;
        return this;
    }

    @Override
    public boolean hasNext() {
        if (currentIndex < data.size()) {
            return true;
        }

        PaginatedIterable<T> newData = fetchData();
        if (newData != null) {
            updateData((PaginatedData<T>) newData.iterator());
            return currentIndex < data.size();
        }

        return false;
    }

    @Override
    public T next() {
        if (hasNext()) {
            return data.get(currentIndex++);
        }

        throw new NoSuchElementException("No more data available.");
    }

    private void updateData(PaginatedData<T> newData) {
        this.data.addAll(newData.data);
        this.lastDataSize = newData.lastDataSize;
        this.lastResponse = newData.lastResponse;
        this.lastEndpointConfig = newData.lastEndpointConfig;
    }

    private PaginatedIterable<T> fetchData() {
        for (PaginationDataManager manager : paginationDataManagers) {
            
            if (!manager.isValid(this)) {
                continue;
            }

            EndpointConfiguration endpointConfig = getLastEndpointConfig();

            try {
                return new ApiCall.Builder<PaginatedIterable<T>, CoreApiException>()
                        .endpointConfiguration(endpointConfig)
                        .globalConfig(endpointConfig.getGlobalConfiguration())
                        .requestBuilder(manager.getNextRequestBuilder(this))
                        .responseHandler(res -> res
                                .globalErrorCase(Collections.singletonMap(ErrorCase.DEFAULT,
                                        ErrorCase.setReason(null,
                                                (reason, context) -> new CoreApiException(reason, context))))
                                .paginatedDeserializer(deserializer, paginationDataManagers))
                        .build().execute();
            } catch (Exception e) {
                continue;
            }
        }

        return null;
    }
}
