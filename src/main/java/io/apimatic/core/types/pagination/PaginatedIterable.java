package io.apimatic.core.types.pagination;

import java.util.Iterator;

public class PaginatedIterable<T> implements Iterable<T> {

    private PaginatedData<T> iterator;
    
    public PaginatedIterable(PaginatedData<T> iterator) {
        this.iterator = iterator;
    }

    @Override
    public Iterator<T> iterator() {
        return iterator.reset();
    }

}
