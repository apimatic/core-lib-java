package io.apimatic.core.types.pagination;

import java.io.IOException;

import io.apimatic.core.types.CoreApiException;

public interface CheckedSupplier<T, E extends CoreApiException> {
    
    @SuppressWarnings("unchecked")
    public static <T, E extends CoreApiException> CheckedSupplier<T, E> createError(
            Throwable exception) {
        if (exception instanceof IOException) {
            return new CheckedSupplier<T, E>()
            {
                @Override
                public T get() throws E, IOException {
                    throw (IOException) exception;
                }
            };
        }

        if (exception instanceof CoreApiException) {
            return new CheckedSupplier<T, E>()
            {
                @Override
                public T get() throws E, IOException {
                    throw (E) exception;
                }
            };
        }

        return null;
        
    }

    public static <T, E extends CoreApiException> CheckedSupplier<T, E> create(T item) {
        return new CheckedSupplier<T, E>()
        {
            @Override
            public T get() throws E, IOException {
                return item;
            }
        };
    }

    T get() throws E, IOException;
}
