package io.apimatic.core.types.pagination;

import java.io.IOException;

import io.apimatic.core.types.CoreApiException;

public interface CheckedSupplier<T, ExceptionType extends CoreApiException> {
    
    @SuppressWarnings("unchecked")
    public static <T, E extends CoreApiException> CheckedSupplier<T, E> CreateError(
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

    public static <T, E extends CoreApiException> CheckedSupplier<T, E> Create(T item) {
        return new CheckedSupplier<T, E>()
        {
            @Override
            public T get() throws E, IOException {
                return item;
            }
        };
    }

    T get() throws ExceptionType, IOException;
}
