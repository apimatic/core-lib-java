package io.apimatic.core;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.core.type.TypeReference;

import io.apimatic.core.configurations.http.request.EndpointConfiguration;
import io.apimatic.core.types.CoreApiException;
import io.apimatic.core.types.pagination.PaginatedData;
import io.apimatic.core.types.pagination.PaginationDeserializer;
import io.apimatic.core.types.pagination.PaginationDataManager;
import io.apimatic.core.utilities.CoreHelper;
import io.apimatic.coreinterfaces.compatibility.CompatibilityFactory;
import io.apimatic.coreinterfaces.http.Context;
import io.apimatic.coreinterfaces.http.request.ResponseClassType;
import io.apimatic.coreinterfaces.http.response.Response;
import io.apimatic.coreinterfaces.type.functional.ContextInitializer;
import io.apimatic.coreinterfaces.type.functional.Deserializer;

/**
 * Handler that encapsulates the process of generating a response object from a
 * Response.
 * 
 * @param <ResponseType>  The response to process.
 * @param <ExceptionType> in case of a problem.
 */
public final class ResponseHandler<ResponseType, ExceptionType extends CoreApiException> {
    /**
     * Not found status code.
     */
    private static final int NOT_FOUND_STATUS_CODE = 404;

    /**
     * Minimum Success code.
     */
    private static final int MIN_SUCCESS_CODE = 200;

    /**
     * Maximum Success code.
     */
    private static final int MAX_SUCCESS_CODE = 208;

    /**
     * A map for end point level errors.
     */
    private final Map<String, ErrorCase<ExceptionType>> localErrorCases;

    /**
     * A map for global level errors.
     */
    private final Map<String, ErrorCase<ExceptionType>> globalErrorCases;

    /**
     * An instance of {@link Deserializer}.
     */
    private final Deserializer<?> deserializer;

    /**
     * A functional interface to wrap in pagination types
     * {@link PaginationDeserializer}.
     */
    private final PaginationDeserializer paginationDeserializer;

    /**
     * An instance of {@link ResponseClassType}.
     */
    private final ResponseClassType responseClassType;

    /**
     * An instance of {@link ContextInitializer}.
     */
    private final ContextInitializer<ResponseType> contextInitializer;

    /**
     * Flag to determine to return null on 404 status code?.
     */
    private final boolean isNullify404Enabled;

    /**
     * Flag to determine if the provided response type a nullable type.
     */
    private final boolean isNullableResponseType;

    /**
     * @param localErrorCases          the map of local errors.
     * @param globalErrorCases         the map of global errors.
     * @param deserializer             the deserializer of json response.
     * @param intermediateDeserializer the api response deserializer.
     * @param responseClassType        the type of response class.
     * @param contextInitializer       the context initializer in response models.
     * @param isNullify404Enabled      on 404 error return null or not?.
     * @param isNullableResponseType   in case of nullable response type.
     */
    private ResponseHandler(final Map<String, ErrorCase<ExceptionType>> localErrorCases,
            final Map<String, ErrorCase<ExceptionType>> globalErrorCases, final Deserializer<ResponseType> deserializer,
            final Deserializer<?> intermediateDeserializer, final PaginationDeserializer paginationDeserializer,
            final ResponseClassType responseClassType, final ContextInitializer<ResponseType> contextInitializer,
            final boolean isNullify404Enabled, final boolean isNullableResponseType) {
        this.localErrorCases = localErrorCases;
        this.globalErrorCases = globalErrorCases;
        this.deserializer = deserializer == null ? intermediateDeserializer : deserializer;
        this.paginationDeserializer = paginationDeserializer;
        this.responseClassType = responseClassType;
        this.contextInitializer = contextInitializer;
        this.isNullify404Enabled = isNullify404Enabled;
        this.isNullableResponseType = isNullableResponseType;
    }

    /**
     * Processes an HttpResponse and returns some value corresponding to that
     * response.
     *
     * @param context Context which is made for endpoint.
     * @param config All endPoint level configuration.
     * @param globalConfig All apiCall level configuration.
     * @param requestBuilder The requestBuilder that can re create current API Call.
     *
     * @return An object of type ResponseType.
     * @throws IOException   Signals that an I/O exception of some sort has
     *                       occurred.
     * @throws ExceptionType Represents error response from the server.
     */
    public ResponseType handle(Context context, EndpointConfiguration config,
            GlobalConfiguration globalConfig, HttpRequest.Builder requestBuilder)
            throws IOException, ExceptionType {
        if (globalConfig.getHttpCallback() != null) {
            // invoke the callback after response if its not null
            globalConfig.getHttpCallback().onAfterResponse(context);
        }

        if (isNullify404Enabled) {
            int responseCode = context.getResponse().getStatusCode();
            // return null on 404
            if (responseCode == NOT_FOUND_STATUS_CODE) {
                return null;
            }
        }

        // handle errors defined at the API level
        validateResponse(context);

        Object result = convertResponse(context.getResponse(), requestBuilder, config,
                globalConfig);

        if (responseClassType == ResponseClassType.API_RESPONSE
                || responseClassType == ResponseClassType.DYNAMIC_API_RESPONSE) {
            return createApiResponse(context.getResponse(), globalConfig.getCompatibilityFactory(),
                    result);
        }

        return applyContextInitializer(context, result);
    }

    @SuppressWarnings("unchecked")
    private ResponseType applyContextInitializer(Context httpContext, Object result) throws IOException {
        if (contextInitializer != null && deserializer != null) {
            result = contextInitializer.apply(httpContext, (ResponseType) result);
        }
        return (ResponseType) result;
    }

    private Object convertResponse(Response response, HttpRequest.Builder requestBuilder,
            EndpointConfiguration config, GlobalConfiguration globalConfig) throws IOException {
        if (responseClassType == ResponseClassType.DYNAMIC_RESPONSE
                || responseClassType == ResponseClassType.DYNAMIC_API_RESPONSE) {
            return createDynamicResponse(response, globalConfig.getCompatibilityFactory());
        }

        if (config.hasBinaryResponse()) {
            return response.getRawBody();
        }

        if (isNullableResponseType && CoreHelper.isNullOrWhiteSpace(response.getBody())) {
            return null;
        }

        if (deserializer != null) {
            return deserializer.apply(response.getBody());
        }

        if (paginationDeserializer != null) {
            return paginationDeserializer.apply(config, globalConfig, requestBuilder, response);
        }

        return null;
    }

    private Object createDynamicResponse(Response httpResponse,
            CompatibilityFactory compatibilityFactory) {
        return compatibilityFactory.createDynamicResponse(httpResponse);
    }

    @SuppressWarnings("unchecked")
    private ResponseType createApiResponse(Response httpResponse,
            CompatibilityFactory compatibilityFactory, Object innerValue) {
        return (ResponseType) compatibilityFactory.createApiResponse(httpResponse.getStatusCode(),
                httpResponse.getHeaders(), innerValue);
    }

    /**
     * Validate the response and check that response contains the error code and
     * throw the corresponding exception
     *
     * @param httpContext
     * @throws ExceptionType
     */
    private void validateResponse(Context httpContext) throws ExceptionType {
        Response response = httpContext.getResponse();
        int statusCode = response.getStatusCode();
        String errorCode = String.valueOf(statusCode);

        throwConfiguredException(localErrorCases, errorCode, httpContext);
        throwConfiguredException(globalErrorCases, errorCode, httpContext);

        if ((statusCode < MIN_SUCCESS_CODE) || (statusCode > MAX_SUCCESS_CODE)) {
            globalErrorCases.get(ErrorCase.DEFAULT).throwException(httpContext);
        }
    }

    private void throwConfiguredException(Map<String, ErrorCase<ExceptionType>> errorCases,
            String errorCode, Context httpContext) throws ExceptionType {
        String defaultErrorCode = "";
        Matcher match = Pattern.compile("^[(4|5)[0-9]]{3}").matcher(errorCode);
        if (match.find()) {
            defaultErrorCode = errorCode.charAt(0) + "XX";
        }
        if (errorCases != null) {
            if (errorCases.containsKey(errorCode)) {
                errorCases.get(errorCode).throwException(httpContext);
            }
            if (errorCases.containsKey(defaultErrorCode)) {
                errorCases.get(defaultErrorCode).throwException(httpContext);
            }
        }
    }

    public static class Builder<ResponseType, ExceptionType extends CoreApiException> {
        /**
         * A map of end point level errors.
         */
        private Map<String, ErrorCase<ExceptionType>> localErrorCases = null;

        /**
         * A map of global level errors.
         */
        private Map<String, ErrorCase<ExceptionType>> globalErrorCases = null;

        /**
         * An instance of {@link Deserializer}.
         */
        private Deserializer<ResponseType> deserializer;

        /**
         * An instance of intermediate {@link Deserializer}.
         */
        private Deserializer<?> intermediateDeserializer;

        /**
         * A functional interface to wrap in pagination types
         * {@link PaginationDeserializer}.
         */
        private PaginationDeserializer paginationDeserializer;

        /**
         * An instance of {@link ResponseClassType}.
         */
        private ResponseClassType responseClassType;

        /**
         * An instance of {@link ContextInitializer}.
         */
        private ContextInitializer<ResponseType> contextInitializer;

        /**
         * A boolean variable to determine return null response on 404.
         */
        private boolean isNullify404Enabled = true;

        /**
         * A boolean variable to determine if the provided response type is a nullable
         * type.
         */
        private boolean isNullableResponseType = false;

        /**
         * Setter for the localErrorCase.
         *
         * @param statusCode the response status code from the server.
         * @param errorCase  to generate the SDK Exception.
         * @return {@link ResponseHandler.Builder}.
         */
        public Builder<ResponseType, ExceptionType> localErrorCase(String statusCode,
                ErrorCase<ExceptionType> errorCase) {
            if (this.localErrorCases == null) {
                this.localErrorCases = new HashMap<String, ErrorCase<ExceptionType>>();
            }

            this.localErrorCases.put(statusCode, errorCase);
            return this;
        }

        /**
         * Setter for the globalErrorCases.
         *
         * @param globalErrorCases the global error cases for endpoints.
         * @return {@link ResponseHandler.Builder}.
         */
        public Builder<ResponseType, ExceptionType> globalErrorCase(
                Map<String, ErrorCase<ExceptionType>> globalErrorCases) {
            this.globalErrorCases = globalErrorCases;
            return this;
        }

        /**
         * Setter for the deserializer.
         *
         * @param deserializer to deserialize the server response.
         * @return {@link ResponseHandler.Builder}.
         */
        public Builder<ResponseType, ExceptionType> deserializer(
                Deserializer<ResponseType> deserializer) {
            this.deserializer = deserializer;
            return this;
        }

        /**
         * Setter for the deserializer.
         *
         * @param intermediateDeserializer to deserialize the api response.
         * @param <IntermediateResponseType> the intermediate type of api response.
         * @return {@link ResponseHandler.Builder}.
         */
        public <IntermediateResponseType> Builder<ResponseType, ExceptionType>
                apiResponseDeserializer(
                        Deserializer<IntermediateResponseType> intermediateDeserializer) {
            this.intermediateDeserializer = intermediateDeserializer;
            return this;
        }

        /**
         * Setter for the deserializer to be used in pagination wrapper.
         *
         * @param pageType TypeReference representing the type.
         * @param converter Converter to convert Page into the list of InnerType.
         * @param returnTypeGetter Converter to convert PaginatedData into the return type.
         * @param dataManagers List of pagination data managers.
         * @param <Page>      the type of the outer page.
         * @param <InnerType> the type wrapped by PaginatedIterable.
         *
         * @return {@link ResponseHandler.Builder}.
         */
        public <InnerType, Page> Builder<ResponseType, ExceptionType> paginatedDeserializer(
                TypeReference<Page> pageType, Function<Page, List<InnerType>> converter,
                Function<PaginatedData<InnerType, Page>, ?> returnTypeGetter,
                PaginationDataManager... dataManagers) {
            this.paginationDeserializer = (config, globalConfig, reqBuilder, res) ->
                    new PaginatedData<InnerType, Page>(config, globalConfig, reqBuilder, res,
                            pageType, converter, dataManagers).convert(returnTypeGetter);
            return this;
        }

        /**
         * Setter for the responseClassType.
         *
         * @param responseClassType specify the response class type for result.
         * @return {@link ResponseHandler.Builder}.
         */
        public Builder<ResponseType, ExceptionType> responseClassType(ResponseClassType responseClassType) {
            this.responseClassType = responseClassType;
            return this;
        }

        /**
         * Setter for the {@link ContextInitializer}.
         *
         * @param contextInitializer the context initializer in response models.
         * @return {@link ResponseHandler.Builder}.
         */
        public Builder<ResponseType, ExceptionType> contextInitializer(
                ContextInitializer<ResponseType> contextInitializer) {
            this.contextInitializer = contextInitializer;
            return this;
        }

        /**
         * Setter for the nullify404.
         *
         * @param isNullify404Enabled in case of 404 error return null or not.
         * @return {@link ResponseHandler.Builder}.
         */
        public Builder<ResponseType, ExceptionType> nullify404(boolean isNullify404Enabled) {
            this.isNullify404Enabled = isNullify404Enabled;
            return this;
        }

        /**
         * Setter for the isNullableResponseType.
         *
         * @param isNullableResponseType in case of nullable response type.
         * @return {@link ResponseHandler.Builder}.
         */
        public Builder<ResponseType, ExceptionType> nullableResponseType(boolean isNullableResponseType) {
            this.isNullableResponseType = isNullableResponseType;
            return this;
        }

        /**
         * Build the ResponseHandler.
         *
         * @return the instance of {@link ResponseHandler}.
         */
        public ResponseHandler<ResponseType, ExceptionType> build() {
            return new ResponseHandler<ResponseType, ExceptionType>(localErrorCases, globalErrorCases, deserializer,
                    intermediateDeserializer, paginationDeserializer, responseClassType, contextInitializer,
                    isNullify404Enabled, isNullableResponseType);
        }
    }
}
