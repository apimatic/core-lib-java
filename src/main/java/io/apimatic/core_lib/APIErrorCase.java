package io.apimatic.core_lib;

import io.apimatic.core_interfaces.http.CoreHttpContext;
import io.apimatic.core_interfaces.type.functional.ExceptionCreator;
import io.apimatic.core_lib.types.ApiException;

public class APIErrorCase<ExceptionType extends ApiException> {
    public static final String DEFAULT = "DEFAULT";
    private String reason;
    private ExceptionCreator<ExceptionType> exceptionCreator;

    private APIErrorCase(String reason, ExceptionCreator<ExceptionType> exceptionCreator) {
        this.reason = reason;
        this.exceptionCreator = exceptionCreator;
    }

    /**
     * @return the reason
     */
    public String getReason() {
        return reason;
    }

    public APIErrorCase<ExceptionType> exceptionCreator(
            ExceptionCreator<ExceptionType> exceptionCreator) {
        this.exceptionCreator = exceptionCreator;
        return this;
    }

    public void throwException(CoreHttpContext httpContext) throws ExceptionType {
        throw exceptionCreator.apply(reason, httpContext);
    }

    public static <ExceptionType extends ApiException> APIErrorCase<ExceptionType> create(
            String reason, ExceptionCreator<ExceptionType> exceptionCreator) {
        APIErrorCase<ExceptionType> errorCase =
                new APIErrorCase<ExceptionType>(reason, exceptionCreator);
        return errorCase;
    }

}
