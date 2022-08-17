package io.apimatic.core_lib;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import io.apimatic.core_interfaces.http.CoreHttpContext;
import io.apimatic.core_interfaces.http.request.CoreHttpRequest;
import io.apimatic.core_interfaces.http.request.ResponseClassType;
import io.apimatic.core_interfaces.http.response.CoreHttpResponse;
import io.apimatic.core_interfaces.type.functional.Deserializer;
import io.apimatic.core_lib.types.ApiException;

public class CoreResponseHandler<ResponseType, ExceptionType extends ApiException> {

    private final Map<String, APIErrorCase<ExceptionType>> localErrorCases;
    private final Map<String, APIErrorCase<ExceptionType>> globalErrorCases;
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
    private CoreResponseHandler(Map<String, APIErrorCase<ExceptionType>> localErrorCases,
            Map<String, APIErrorCase<ExceptionType>> globalErrorCases,
            Deserializer<ResponseType> deserializer, ResponseClassType responseClassType,
            boolean isNullify404Enabled) {
        this.localErrorCases = localErrorCases;
        this.globalErrorCases = globalErrorCases;
        this.deserializer = deserializer;
        this.responseClassType = responseClassType;
        this.isNullify404Enabled = isNullify404Enabled;
    }


    /**
     * Getter for the errorCases
     * 
     * @return the errorCase
     */
    public Map<String, APIErrorCase<ExceptionType>> getLocalErrorCases() {
        return this.localErrorCases;
    }

    /**
     * Getter for the deserializer
     * 
     * @return the deserializer function
     */
    public Deserializer<ResponseType> getDeserializer() {
        return this.deserializer;
    }

    /**
     * @return the globalErrorCases
     */
    public Map<String, APIErrorCase<ExceptionType>> getGlobalErrorCases() {
        return globalErrorCases;
    }


    public ResponseType handle(CoreHttpRequest httpRequest, CoreHttpResponse httpResponse,
            CoreConfig coreConfig) throws IOException, ExceptionType {
        CoreHttpContext httpContext =
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

        if (deserializer != null) {
            // extract result from the http response
            return deserializer.apply(httpResponse.getBody());
        }

        switch (responseClassType) {
            case API_RESPONSE:
                return (ResponseType) coreConfig.getCompatibilityFactory()
                        .createDynamicResponse(httpResponse);
            case DYNAMIC_RESPONSE:
                return (ResponseType) coreConfig.getCompatibilityFactory()
                        .createDynamicResponse(httpResponse);

            default:
                return null;
        }


    }

    private void validateResponse(CoreHttpContext httpContext) throws ExceptionType {
        // TODO Auto-generated method stub
        CoreHttpResponse response = httpContext.getResponse();
        int statusCode = response.getStatusCode();
        String errorCode = String.valueOf(statusCode);
        if (localErrorCases != null && localErrorCases.containsKey(errorCode)) {
            localErrorCases.get(errorCode).throwException(httpContext);
        }

        if (globalErrorCases != null && globalErrorCases.containsKey(errorCode)) {
            globalErrorCases.get(errorCode).throwException(httpContext);
        }

        if ((statusCode < 200) || (statusCode > 208)) { // [200,208] = HTTP OK
            globalErrorCases.get(APIErrorCase.DEFAULT).throwException(httpContext);
        }
    }

    public static class Builder<ResponseType, ExceptionType extends ApiException> {
        private Map<String, APIErrorCase<ExceptionType>> localErrorCases = null;
        private Map<String, APIErrorCase<ExceptionType>> globalErrorCases = null;
        private Deserializer<ResponseType> deserializer;
        private ResponseClassType responseClassType;
        private boolean isNullify404Enabled = false;

        public Builder<ResponseType, ExceptionType> localErrorCase(String statusCode,
                APIErrorCase<ExceptionType> errorCase) {
            if (this.localErrorCases == null) {
                this.localErrorCases = new HashMap<String, APIErrorCase<ExceptionType>>();
            }

            this.localErrorCases.put(statusCode, errorCase);
            return this;
        }

        public Builder<ResponseType, ExceptionType> globalErrorCase(
                Map<String, APIErrorCase<ExceptionType>> globalErrorCases) {
            this.globalErrorCases = globalErrorCases;
            return this;
        }


        public Builder<ResponseType, ExceptionType> deserializer(
                Deserializer<ResponseType> deserializer) {
            this.deserializer = deserializer;
            return this;
        }

        public Builder<ResponseType, ExceptionType> responseClassType(
                ResponseClassType responseClassType) {
            this.responseClassType = responseClassType;
            return this;
        }

        public Builder<ResponseType, ExceptionType> nullify404(boolean isNullify404Enabled) {
            this.isNullify404Enabled = isNullify404Enabled;
            return this;
        }

        public CoreResponseHandler<ResponseType, ExceptionType> build() {
            return new CoreResponseHandler<ResponseType, ExceptionType>(localErrorCases,
                    globalErrorCases, deserializer, responseClassType, isNullify404Enabled);
        }
    }

}
