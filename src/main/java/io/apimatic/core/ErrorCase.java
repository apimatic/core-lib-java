package io.apimatic.core;

import io.apimatic.core.types.ApiException;
import io.apimatic.coreinterfaces.http.Context;
import io.apimatic.coreinterfaces.type.functional.ExceptionCreator;

/**
 * A class is responsible to generate the SDK Exception.
 *
 * @param <ExceptionType>
 */
public class ErrorCase<ExceptionType extends ApiException> {
    public static final String DEFAULT = "DEFAULT";
    private String reason;
    private ExceptionCreator<ExceptionType> exceptionCreator;

    /**
     * A private constructor
     * 
     * @param reason
     * @param exceptionCreator
     */
    private ErrorCase(String reason, ExceptionCreator<ExceptionType> exceptionCreator) {
        this.reason = reason;
        this.exceptionCreator = exceptionCreator;
    }

    /**
     * this method throw the configured exception using functional interface
     * 
     * @param httpContext
     * @throws ExceptionType
     */
    public void throwException(Context httpContext) throws ExceptionType {
        throw exceptionCreator.apply(reason, httpContext);
    }

    /**
     * Create the errorcase using the error reason and exception creator functional interface which
     * throws the respective exception while throwing
     * 
     * @param <ExceptionType>
     * @param reason
     * @param exceptionCreator
     * @return
     */
    public static <ExceptionType extends ApiException> ErrorCase<ExceptionType> create(
            String reason, ExceptionCreator<ExceptionType> exceptionCreator) {
        ErrorCase<ExceptionType> errorCase = new ErrorCase<ExceptionType>(reason, exceptionCreator);
        return errorCase;
    }

}
