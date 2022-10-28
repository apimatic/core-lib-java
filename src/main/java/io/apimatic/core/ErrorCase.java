package io.apimatic.core;

import io.apimatic.core.types.CoreApiException;
import io.apimatic.coreinterfaces.http.Context;
import io.apimatic.coreinterfaces.type.functional.ExceptionCreator;

/**
 * A class is responsible to generate the SDK Exception.
 * @param <ExceptionType> Represents error response from the server.
 */
public final class ErrorCase<ExceptionType extends CoreApiException> {
    /**
     * A key for the default errors
     */
    public static final String DEFAULT = "DEFAULT";

    /**
     * A error's reason
     */
    private String reason;

    /**
     * An instance of {@link ExceptionCreator}
     */
    private ExceptionCreator<ExceptionType> exceptionCreator;

    /**
     * A private constructor
     * @param reason the exception reason
     * @param exceptionCreator the exceptionCreator
     */
    private ErrorCase(String reason, ExceptionCreator<ExceptionType> exceptionCreator) {
        this.reason = reason;
        this.exceptionCreator = exceptionCreator;
    }

    /**
     * this method throw the configured exception using functional interface
     * @param httpContext is wrapped the request sent to the server and the response received from
     *        the server.
     * @throws ExceptionType Represents error response from the server.
     */
    public void throwException(Context httpContext) throws ExceptionType {
        throw exceptionCreator.apply(reason, httpContext);
    }

    /**
     * Create the errorcase using the error reason and exception creator functional interface which
     * throws the respective exception while throwing
     * @param <ExceptionType> Represents error response from the server.
     * @param reason the exception message
     * @param exceptionCreator the functional interface which is responsible to create the server
     *        thrown exception
     * @return {@link ErrorCase}
     */
    public static <ExceptionType extends CoreApiException> ErrorCase<ExceptionType>
            create(String reason, ExceptionCreator<ExceptionType> exceptionCreator) {
        ErrorCase<ExceptionType> errorCase = new ErrorCase<ExceptionType>(reason, exceptionCreator);
        return errorCase;
    }

}
