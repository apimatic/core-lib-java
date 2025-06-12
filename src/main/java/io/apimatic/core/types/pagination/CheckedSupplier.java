package io.apimatic.core.types.pagination;

import java.io.IOException;

import io.apimatic.core.types.CoreApiException;

public interface CheckedSupplier<T, E extends CoreApiException> {

    /**
     * @param <T> Represents type of stored value.
     * @param <E> Represents type of stored exception.
     * @param value Create an instance with stored exception of type E.
     * @return created CheckedSupplier object.
     */
    @SuppressWarnings("unchecked")
    static <T, E extends CoreApiException> CheckedSupplier<T, E> createError(
            Throwable exception) {
        if (exception instanceof IOException) {
            return new CheckedSupplier<T, E>() {
                @Override
                public T get() throws E, IOException {
                    throw (IOException) exception;
                }
            };
        }

        if (exception instanceof CoreApiException) {
            return new CheckedSupplier<T, E>() {
                @Override
                public T get() throws E, IOException {
                    throw (E) exception;
                }
            };
        }

        return null;
    }

    /**
     * @param <T> Represents type of stored value.
     * @param <E> Represents type of stored exception.
     * @param value Create an instance with stored value of type T.
     * @return created CheckedSupplier object.
     */
    static <T, E extends CoreApiException> CheckedSupplier<T, E> create(T value) {
        return new CheckedSupplier<T, E>() {
            @Override
            public T get() throws E, IOException {
                return value;
            }
        };
    }

    /**
     * Get the stored instance or throw an exception.
     * @return The stored instance of type T.
     * @throws E
     * @throws IOException
     */
    T get() throws E, IOException;
}
