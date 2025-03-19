package io.apimatic.core.types.pagination;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import io.apimatic.core.configurations.http.request.EndpointConfiguration;
import io.apimatic.coreinterfaces.http.response.Response;

public abstract class PaginatedData<T> implements Iterator<T> {

    private int currentIndex = 0;

    private List<T> data = new ArrayList<T>();

    private Response lastResponse;

    private EndpointConfiguration lastEndpointConfig;

    public PaginatedData(final List<T> values, final Response response, final EndpointConfiguration config) {
        data.addAll(values);
        lastResponse = response;
        lastEndpointConfig = config;
    }

    protected EndpointConfiguration getLastEndpointConfiguration() {
        return lastEndpointConfig;
    }

    protected Response getLastResponse() {
        return lastResponse;
    }

    protected int getDataSize() {
        return data.size();
    }

    @Override
    public String toString() {
        return "PaginatedData [currentIndex=" + currentIndex + ", data=" + data + "]";
    }

    @Override
    public boolean hasNext() {
        if (currentIndex < data.size()) {
            return true;
        }

        PaginatedData<T> newData = fetchData();
        if (newData != null) {
            updateData(newData);
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

    protected void updateData(PaginatedData<T> newData) {
        this.data.addAll(newData.data);
        this.lastResponse = newData.lastResponse;
        this.lastEndpointConfig = newData.lastEndpointConfig;
    }

    protected abstract PaginatedData<T> fetchData();
}
