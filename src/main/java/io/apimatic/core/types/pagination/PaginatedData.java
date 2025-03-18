package io.apimatic.core.types.pagination;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import com.fasterxml.jackson.core.type.TypeReference;

import io.apimatic.core.configurations.http.request.EndpointConfiguration;
import io.apimatic.core.utilities.CoreHelper;
import io.apimatic.coreinterfaces.http.response.Response;

public abstract class PaginatedData<T> implements Iterator<T> {

    private int currentIndex = 0;

    private List<T> data = new ArrayList<T>();

    private List<Response> responses = new ArrayList<Response>();

    private List<EndpointConfiguration> endPointConfigs = new ArrayList<EndpointConfiguration>();

    private String resultPointer;

    public PaginatedData(final T value, final Response response, final EndpointConfiguration config,
            final String resultPointer) {
        data.add(value);
        endPointConfigs.add(config);
        responses.add(response);
        this.resultPointer = resultPointer;
    }

    public static <InnerType> List<InnerType> extract(Iterator<?> iterator, Class<InnerType> clazz) throws IOException {
        List<InnerType> items = new ArrayList<InnerType>();

        if (!(iterator instanceof PaginatedData<?>)) {
            return items;
        }

        PaginatedData<?> paginatedData = (PaginatedData<?>) iterator;
        for (Response res : paginatedData.responses) {
            String resultArray = CoreHelper.getValueFromJson(paginatedData.resultPointer, res.getBody());
            List<InnerType> result = CoreHelper.deserialize(resultArray, new TypeReference<List<InnerType>>() {});
            for (InnerType inner : result) {
                items.add(CoreHelper.deserialize(CoreHelper.serialize(inner), clazz));
            }
        }
        
        return items;
    }

    protected EndpointConfiguration getLastEndpointConfiguration() {
        return endPointConfigs.get(currentIndex - 1);
    }

    protected Response getLastResponse() {
        return responses.get(currentIndex - 1);
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

        PaginatedData<T> data = fetchData();
        if (data != null) {
            updateUsing(data);
            return true;
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

    private void updateUsing(PaginatedData<T> existing) {
        this.data.add(existing.data.get(existing.currentIndex));
        this.responses.add(existing.responses.get(existing.currentIndex));
        this.endPointConfigs.add(existing.endPointConfigs.get(existing.currentIndex));
    }

    protected abstract PaginatedData<T> fetchData();
}
