package io.apimatic.core;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import io.apimatic.core.types.CoreApiException;
import io.apimatic.coreinterfaces.compatibility.CompatibilityFactory;
import io.apimatic.coreinterfaces.http.Context;
import io.apimatic.coreinterfaces.http.request.Request;
import io.apimatic.coreinterfaces.http.request.ResponseClassType;
import io.apimatic.coreinterfaces.http.request.configuration.CoreEndpointConfiguration;
import io.apimatic.coreinterfaces.http.response.Response;
import io.apimatic.coreinterfaces.type.functional.ContextInitializer;
import io.apimatic.coreinterfaces.type.functional.Deserializer;

/**
 * Handler that encapsulates the process of generating a response object from a Response
 *
 * @param <ResponseType> The response to process
 * @param <ExceptionType> in case of a problem
 */
public class ResponseHandler<ResponseType, ExceptionType extends CoreApiException> {

    private final Map<String, ErrorCase<ExceptionType>> localErrorCases;
    private final Map<String, ErrorCase<ExceptionType>> globalErrorCases;
    private final Deserializer<ResponseType> deserializer;
    private final Deserializer<?> intermediateDeserializer;
    private final ResponseClassType responseClassType;
    private final ContextInitializer<ResponseType> contextInitializer;
    private final boolean isNullify404Enabled;

    /**
     * 
     * @param localErrorCases the map of local errors
     * @param globalErrorCases the map of global errors
     * @param deserializer the deserializer of json response
     * @param intermediateDeserializer the api response deserializer
     * @param responseClassType the type of response class
     * @param contextInitializer the context initializer in response models
     * @param isNullify404Enabled on 404 error return null or not?
     */
    private ResponseHandler(Map<String, ErrorCase<ExceptionType>> localErrorCases,
            Map<String, ErrorCase<ExceptionType>> globalErrorCases,
            Deserializer<ResponseType> deserializer, Deserializer<?> intermediateDeserializer,
            ResponseClassType responseClassType,
            ContextInitializer<ResponseType> contextInitializer, boolean isNullify404Enabled) {
        this.localErrorCases = localErrorCases;
        this.globalErrorCases = globalErrorCases;
        this.deserializer = deserializer;
        this.intermediateDeserializer = intermediateDeserializer;
        this.responseClassType = responseClassType;
        this.contextInitializer = contextInitializer;
        this.isNullify404Enabled = isNullify404Enabled;
    }


    /**
     * Processes an HttpResponse and returns some value corresponding to that response.
     * 
     * @param httpRequest Request which is made for endpoint
     * @param httpResponse Response which is received after execution
     * @param globalConfiguration the global configuration to store the request global information
     * @param endpointConfiguration the endpoint level configuration
     * @return An object of type ResponseType
     * @throws IOException Signals that an I/O exception of some sort has occurred.
     * @throws ExceptionType Represents error response from the server.
     */
    @SuppressWarnings("unchecked")
    public ResponseType handle(Request httpRequest, Response httpResponse,
            GlobalConfiguration globalConfiguration,
            CoreEndpointConfiguration endpointConfiguration) throws IOException, ExceptionType {

        Context httpContext = globalConfiguration.getCompatibilityFactory()
                .createHttpContext(httpRequest, httpResponse);
        // invoke the callback after response if its not null
        if (globalConfiguration.getHttpCallback() != null) {
            globalConfiguration.getHttpCallback().onAfterResponse(httpContext);
        }

        if (isNullify404Enabled) {
            int responseCode = httpContext.getResponse().getStatusCode();
            // return null on 404
            if (responseCode == 404) {
                return null;
            }
        }

        // handle errors defined at the API level
        validateResponse(httpContext);

        ResponseType result = applyDeserializer(deserializer, httpResponse);

        result = applyContextInitializer(contextInitializer, httpContext, result);

        if (endpointConfiguration.hasBinaryResponse()) {
            result = (ResponseType) httpResponse.getRawBody();
        }

        if (responseClassType != null) {

            return createResponseClassType(httpResponse, globalConfiguration);
        }

        return result;
    }


    private ResponseType applyContextInitializer(
            ContextInitializer<ResponseType> contextInitializer, Context httpContext,
            ResponseType result) throws IOException {
        if (contextInitializer != null && deserializer != null) {
            result = contextInitializer.apply(httpContext, result);
        }
        return result;
    }

    private <T> T applyDeserializer(Deserializer<T> deserializer, Response httpResponse)
            throws IOException {
        T result = null;
        if (deserializer != null) {
            // extract result from the http response
            result = deserializer.apply(httpResponse.getBody());
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    private <T> ResponseType createResponseClassType(Response httpResponse,
            GlobalConfiguration coreConfig) throws IOException {
        CompatibilityFactory compatibilityFactory = coreConfig.getCompatibilityFactory();
        switch (responseClassType) {
            case API_RESPONSE:
                return (ResponseType) compatibilityFactory.createApiResponse(
                        httpResponse.getStatusCode(), httpResponse.getHeaders(),
                        applyDeserializer(intermediateDeserializer, httpResponse));
            case DYNAMIC_RESPONSE:
                return createDynamicResponse(httpResponse, compatibilityFactory);
            case DYNAMIC_API_RESPONSE:
                return (ResponseType) compatibilityFactory.createApiResponse(
                        httpResponse.getStatusCode(), httpResponse.getHeaders(),
                        createDynamicResponse(httpResponse, compatibilityFactory));
            default:
                return null;
        }
    }

    @SuppressWarnings("unchecked")
    private ResponseType createDynamicResponse(Response httpResponse,
            CompatibilityFactory compatibilityFactory) {
        return (ResponseType) compatibilityFactory.createDynamicResponse(httpResponse);
    }

    /**
     * Validate the response and check that response contains the error code and throw the
     * corresponding exception
     * 
     * @param httpContext
     * @throws ExceptionType
     */
    private void validateResponse(Context httpContext) throws ExceptionType {
        Response response = httpContext.getResponse();
        int statusCode = response.getStatusCode();
        String errorCode = String.valueOf(statusCode);

        if (localErrorCases != null && localErrorCases.containsKey(errorCode)) {
            localErrorCases.get(errorCode).throwException(httpContext);
        }

        if (globalErrorCases != null && globalErrorCases.containsKey(errorCode)) {
            globalErrorCases.get(errorCode).throwException(httpContext);
        }

        if ((statusCode < 200) || (statusCode > 208)) {
            globalErrorCases.get(ErrorCase.DEFAULT).throwException(httpContext);
        }
    }

    public static class Builder<ResponseType, ExceptionType extends CoreApiException> {
        private Map<String, ErrorCase<ExceptionType>> localErrorCases = null;
        private Map<String, ErrorCase<ExceptionType>> globalErrorCases = null;
        private Deserializer<ResponseType> deserializer;
        private Deserializer<?> intermediateDeserializer;
        private ResponseClassType responseClassType;
        private ContextInitializer<ResponseType> contextInitializer;
        private boolean isNullify404Enabled = true;

        /**
         * Setter for the localErrorCase
         * 
         * @param statusCode the response status code from the server
         * @param errorCase to generate the SDK Exception
         * @return {@link ResponseHandler.Builder}
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
         * Setter for the globalErrorCases
         * 
         * @param globalErrorCases the global error cases for endpoints
         * @return {@link ResponseHandler.Builder}
         */
        public Builder<ResponseType, ExceptionType> globalErrorCase(
                Map<String, ErrorCase<ExceptionType>> globalErrorCases) {
            this.globalErrorCases = globalErrorCases;
            return this;
        }

        /**
         * Setter for the deserializer
         * 
         * @param deserializer to deserialize the server response
         * @return {@link ResponseHandler.Builder}
         */
        public Builder<ResponseType, ExceptionType> deserializer(
                Deserializer<ResponseType> deserializer) {
            this.deserializer = deserializer;
            return this;
        }


        /**
         * Setter for the deserializer
         * 
         * @param intermediateDeserializer to deserialize the api response
         * @param <IntermediateResponseType> the intermediate type of api response
         * @return {@link ResponseHandler.Builder}
         */
        public <IntermediateResponseType> Builder<ResponseType, ExceptionType> apiResponseDeserializer(
                Deserializer<IntermediateResponseType> intermediateDeserializer) {
            this.intermediateDeserializer = intermediateDeserializer;
            return this;
        }

        /**
         * Setter for the responseClassType
         * 
         * @param responseClassType specify the response class type for result.
         * @return {@link ResponseHandler.Builder}
         */
        public Builder<ResponseType, ExceptionType> responseClassType(
                ResponseClassType responseClassType) {
            this.responseClassType = responseClassType;
            return this;
        }

        /**
         * Setter for the {@link ContextInitializer}
         * 
         * @param contextInitializer the context initializer in response models.
         * @return {@link ResponseHandler.Builder}
         */
        public Builder<ResponseType, ExceptionType> contextInitializer(
                ContextInitializer<ResponseType> contextInitializer) {
            this.contextInitializer = contextInitializer;
            return this;
        }

        /**
         * Setter for the nullify404
         * 
         * @param isNullify404Enabled in case of 404 error return null or not
         * @return {@link ResponseHandler.Builder}
         */
        public Builder<ResponseType, ExceptionType> nullify404(boolean isNullify404Enabled) {
            this.isNullify404Enabled = isNullify404Enabled;
            return this;
        }

        /**
         * build the ResponseHandler
         * 
         * @return the instance of {@link ResponseHandler}
         */
        public ResponseHandler<ResponseType, ExceptionType> build() {
            return new ResponseHandler<ResponseType, ExceptionType>(localErrorCases,
                    globalErrorCases, deserializer, intermediateDeserializer, responseClassType,
                    contextInitializer, isNullify404Enabled);
        }
    }
}
