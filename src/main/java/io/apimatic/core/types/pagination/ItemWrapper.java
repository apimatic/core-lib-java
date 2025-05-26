package io.apimatic.core.types.pagination;

import java.io.IOException;

import io.apimatic.core.types.CoreApiException;

public interface ItemWrapper<T, ExceptionType extends CoreApiException> {
    T get() throws ExceptionType, IOException;
}
