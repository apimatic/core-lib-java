package io.apimatic.core_lib;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import io.apimatic.core_interfaces.http.Context;
import io.apimatic.core_interfaces.http.request.Request;
import io.apimatic.core_interfaces.http.request.ResponseClassType;
import io.apimatic.core_interfaces.http.response.Response;
import io.apimatic.core_interfaces.type.functional.Deserializer;
import io.apimatic.core_lib.types.ApiException;

/**
 * Handler that encapsulates the process of generating a response object from a Response.
 *
 * @param <ResponseType> The response to process
 * @param <ExceptionType> in case of a problem
 */
public class ResponseHandler<ResponseType, ExceptionType extends ApiException> {

    private final Map<String, ErrorCase<ExceptionType>> localErrorCases;
    private final Map<String, ErrorCase<ExceptionType>> globalErrorCases;
    private final Deserializer<ResponseType> deserializer;
    private final ResponseClassType responseClassType;
    private final boolean isNullify404Enabled;

    /**
     * @param localErrorCases
     * @param globalErrorCases
     * @param deserializer
     * @param objectCreator
     * @param isNullify404Enabled
     */
    private ResponseHandler(Map<String, ErrorCase<ExceptionType>> localErrorCases,
            Map<String, ErrorCase<ExceptionType>> globalErrorCases,
            Deserializer<ResponseType> deserializer, ResponseClassType responseClassType,
            boolean isNullify404Enabled) {
        this.localErrorCases = localErrorCases;
        this.globalErrorCases = globalErrorCases;
        this.deserializer = deserializer;
        this.responseClassType = responseClassType;
        this.isNullify404Enabled = isNullify404Enabled;
    }


    /**
     * Processes an HttpResponse and returns some value corresponding to that response.
     * 
     * @param httpRequest
     * @param httpResponse
     * @param coreConfig
     * @return the ResponseType
     * @throws IOException
     * @throws ExceptionType
     */
    public ResponseType handle(Request httpRequest, Response httpResponse,
            GlobalConfiguration coreConfig) throws IOException, ExceptionType {

        Context httpContext =
                coreConfig.getCompatibilityFactory().createHttpContext(httpRequest, httpResponse);
        // invoke the callback after response if its not null
        if (coreConfig.getHttpCallback() != null) {
            coreConfig.getHttpCallback().onAfterResponse(httpContext);
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
        ResponseType result = null;

        if (deserializer != null) {
            // extract result from the http response
            return deserializer.apply(httpResponse.getBody());
        }
        if (responseClassType != null) {
            return createResponseClassType(httpResponse, coreConfig);
        }

        return result;
    }

    @SuppressWarnings("unchecked")
    private ResponseType createResponseClassType(Response httpResponse,
            GlobalConfiguration coreConfig) {
        switch (responseClassType) {
            case API_RESPONSE:
                return (ResponseType) coreConfig.getCompatibilityFactory().createAPiResponse(
                        httpResponse.getStatusCode(), httpResponse.getHeaders(),
                        httpResponse.getBody());
            case DYNAMIC_RESPONSE:
                return (ResponseType) coreConfig.getCompatibilityFactory()
                        .createDynamicResponse(httpResponse);
            default:
                return null;
        }
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

    public static class Builder<ResponseType, ExceptionType extends ApiException> {
        private Map<String, ErrorCase<ExceptionType>> localErrorCases = null;
        private Map<String, ErrorCase<ExceptionType>> globalErrorCases = null;
        private Deserializer<ResponseType> deserializer;
        private ResponseClassType responseClassType;
        private boolean isNullify404Enabled = true;

        /**
         * Setter for the localErrorCase
         * 
         * @param statusCode
         * @param errorCase
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
         * @param globalErrorCases
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
         * @param deserializer
         * @return {@link ResponseHandler.Builder}
         */
        public Builder<ResponseType, ExceptionType> deserializer(
                Deserializer<ResponseType> deserializer) {
            this.deserializer = deserializer;
            return this;
        }

        /**
         * Setter for the responseClassType
         * 
         * @param responseClassType
         * @return {@link ResponseHandler.Builder}
         */
        public Builder<ResponseType, ExceptionType> responseClassType(
                ResponseClassType responseClassType) {
            this.responseClassType = responseClassType;
            return this;
        }

        /**
         * Setter for the nullify404
         * 
         * @param isNullify404Enabled
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
                    globalErrorCases, deserializer, responseClassType, isNullify404Enabled);
        }
    }
}
