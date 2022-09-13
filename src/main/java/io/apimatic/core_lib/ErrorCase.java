package io.apimatic.core_lib;

import io.apimatic.core_interfaces.http.CoreHttpContext;
import io.apimatic.core_interfaces.type.functional.ExceptionCreator;
import io.apimatic.core_lib.types.ApiException;

public class ErrorCase<ExceptionType extends ApiException> {
    public static final String DEFAULT = "DEFAULT";
    private String reason;
    private ExceptionCreator<ExceptionType> exceptionCreator;

    private ErrorCase(String reason, ExceptionCreator<ExceptionType> exceptionCreator) {
        this.reason = reason;
        this.exceptionCreator = exceptionCreator;
    }

    public void throwException(CoreHttpContext httpContext) throws ExceptionType {
        throw exceptionCreator.apply(reason, httpContext);
    }

    public static <ExceptionType extends ApiException> ErrorCase<ExceptionType> create(
            String reason, ExceptionCreator<ExceptionType> exceptionCreator) {
        ErrorCase<ExceptionType> errorCase = new ErrorCase<ExceptionType>(reason, exceptionCreator);
        return errorCase;
    }

}
