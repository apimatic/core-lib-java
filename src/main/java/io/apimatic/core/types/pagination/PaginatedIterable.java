package io.apimatic.core.types.pagination;

import java.util.Iterator;

public class PaginatedIterable<T> implements Iterable<T> {

    private PaginatedData<T> paginatedData;
    
    public PaginatedIterable(PaginatedData<T> iterator) {
        this.paginatedData = iterator;
    }

    @Override
    public Iterator<T> iterator() {
        return paginatedData.reset();
    }

}
