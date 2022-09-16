package io.apimatic.core_lib;

import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import io.apimatic.core_interfaces.authentication.Authentication;
import io.apimatic.core_interfaces.compatibility.CompatibilityFactory;
import io.apimatic.core_interfaces.http.Method;
import io.apimatic.core_interfaces.http.HttpHeaders;
import io.apimatic.core_interfaces.http.request.ArraySerializationFormat;
import io.apimatic.core_interfaces.http.request.Request;
import io.apimatic.core_interfaces.http.request.MutliPartRequestType;
import io.apimatic.core_interfaces.type.FileWrapper;
import io.apimatic.core_interfaces.type.functional.Serializer;
import io.apimatic.core_lib.types.http.request.MultipartFileWrapper;
import io.apimatic.core_lib.types.http.request.MultipartWrapper;
import io.apimatic.core_lib.utilities.CoreHelper;

/**
 * An HttpRequest instance is built through an HttpRequest builder. A request's URI, headers, and
 * body can be set. Request bodies are provided through a body supplied to one of the POST, PUT or
 * method methods. Once all required parameters have been set in the builder, build will return the
 * HttpRequest. Builders can be copied and modified many times in order to build multiple related
 * requests that differ in some parameters.
 *
 */
public class HttpRequest {
    private final Request coreHttpRequest;
    private final GlobalConfiguration coreConfig;
    private final StringBuilder urlBuilder;
    private final CompatibilityFactory compatibilityFactory;

    /**
     * @param server
     * @param path
     * @param httpMethod
     * @param requiresAuth
     * @param queryParams
     * @param templateParams
     * @param headerParams
     * @param formParams
     * @param body
     * @param bodySerializer
     * @throws IOException
     */
    private HttpRequest(GlobalConfiguration coreConfig, String server, String path,
            Method httpMethod, String authenticationKey, Map<String, Object> queryParams,
            Map<String, SimpleEntry<Object, Boolean>> templateParams,
            Map<String, List<String>> headerParams, Set<Parameter> formParams, Object body,
            Serializer bodySerializer, Map<String, Object> bodyParameters,
            ArraySerializationFormat arraySerializationFormat) throws IOException {
        this.coreConfig = coreConfig;
        this.compatibilityFactory = coreConfig.getCompatibilityFactory();
        urlBuilder = getStringBuilder(server, path);

        processTemplateParams(templateParams);
        body = buildBody(body, bodySerializer, bodyParameters);
        coreHttpRequest = buildRequest(httpMethod, body, addHeaders(headerParams), queryParams,
                formParams, arraySerializationFormat);
        applyAuthentication(authenticationKey);
    }

    /**
     * 
     * @return the {@link Request} instance which is used for making {@link ApiCall}
     */
    public Request getCoreHttpRequest() {
        return coreHttpRequest;
    }

    private void applyAuthentication(String authenticationKey) {
        if (authenticationKey == null) {
            return;
        }

        Map<String, Authentication> authentications = coreConfig.getAuthentications();
        if (authentications != null) {
            Authentication authManager = authentications.get(authenticationKey);
            if (authManager != null) {
                authManager.apply(coreHttpRequest);
            }
        }
    }

    private Request buildRequest(Method httpMethod, Object body, HttpHeaders headerParams,
            Map<String, Object> queryParams, Set<Parameter> formParams,
            ArraySerializationFormat arraySerializationFormat) throws IOException {
        if (body != null) {
            return compatibilityFactory.createHttpRequest(httpMethod, urlBuilder, headerParams,
                    queryParams, body);
        }

        List<SimpleEntry<String, Object>> formFields =
                generateFormFields(formParams, arraySerializationFormat);
        return compatibilityFactory.createHttpRequest(httpMethod, urlBuilder, headerParams,
                queryParams, formFields);
    }

    /**
     * @param compatibilityFactory
     * @return
     */
    private List<SimpleEntry<String, Object>> generateFormFields(Set<Parameter> formParams,
            ArraySerializationFormat arraySerializationFormat) {
        if (formParams.isEmpty()) {
            return null;
        }

        Map<String, Object> formParamaters = new HashMap<>();
        for (Parameter formParameter : formParams) {
            String key = formParameter.getKey();
            Object value = formParameter.getValue();
            if (formParameter.getMultiPartRequest() != null) {
                value = handleMultiPartRequest(formParameter);
            }

            formParamaters.put(key, value);
        }

        return CoreHelper.prepareFormFields(formParamaters, arraySerializationFormat);
    }

    private StringBuilder getStringBuilder(String server, String path) {
        return new StringBuilder(coreConfig.getBaseUri().apply(server) + path);
    }

    private void processTemplateParams(Map<String, SimpleEntry<Object, Boolean>> templateParams) {
        if (!templateParams.isEmpty()) {
            CoreHelper.appendUrlWithTemplateParameters(urlBuilder, templateParams);
        }
    }

    private HttpHeaders addHeaders(Map<String, List<String>> headerParams) {
        addGlobalHeader(headerParams);
        addAdditionalHeaders(headerParams);
        return compatibilityFactory.createHttpHeaders(headerParams);
    }

    private void addGlobalHeader(Map<String, List<String>> headerParams) {
        coreConfig.getGlobalHeaders().forEach((key, value) -> {
            if (!headerParams.containsKey(key)) {
                headerParams.put(key, value);
            }
        });
    }

    private void addAdditionalHeaders(Map<String, List<String>> headerParams) {
        headerParams.putAll(coreConfig.getAdditionalHeaders());
    }

    private Object buildBody(Object body, Serializer bodySerializer,
            Map<String, Object> bodyParameters) throws IOException {
        if (body != null) {
            if (bodySerializer != null) {
                return bodySerializer.apply(body);
            }
            if (body instanceof FileWrapper) {
                return body;
            }

            if (body instanceof String) {
                return body.toString();
            }

            return CoreHelper.serialize(body);
        }

        if (bodyParameters != null) {
            CoreHelper.removeNullValues(bodyParameters);
            return CoreHelper.serialize(bodyParameters);
        }

        return body;
    }

    private Object handleMultiPartRequest(Parameter formParameter) {
        HttpHeaders multipartFileHeaders =
                compatibilityFactory.createHttpHeaders(formParameter.getMultipartHeaders());

        if (formParameter.getMultiPartRequest() == MutliPartRequestType.MULTI_PART_FILE) {
            return new MultipartFileWrapper((FileWrapper) formParameter.getValue(),
                    multipartFileHeaders);
        }
        return new MultipartWrapper(formParameter.getValue().toString(), multipartFileHeaders);

    }

    public static class Builder {
        private String server;
        private String path;
        private Method httpMethod;
        private String authenticationKey;
        private Map<String, Object> queryParams = new HashMap<>();
        private Map<String, SimpleEntry<Object, Boolean>> templateParams = new HashMap<>();
        private Map<String, List<String>> headerParams = new HashMap<>();
        private Set<Parameter> formParams = new HashSet<>();
        private Object body;
        private Serializer bodySerializer;
        private Map<String, Object> bodyParameters;
        private ArraySerializationFormat arraySerializationFormat =
                ArraySerializationFormat.INDEXED;
        private Parameter.Builder parameterBuilder = new Parameter.Builder();

        /**
         * 
         * @param server the base uri address
         * @return
         */
        public Builder server(String server) {
            this.server = server;
            return this;
        }

        /**
         * 
         * @param path the endpoint path
         * @return
         */
        public Builder path(String path) {
            this.path = path;
            return this;
        }

        /**
         *
         * @param httpMethod HttpMethod value for httpMethod
         * @return Builder
         */
        public Builder httpMethod(Method httpMethod) {
            this.httpMethod = httpMethod;
            return this;
        }

        /**
         * Setter for requiresAuth
         * 
         * @param requiresAuth boolean value for requiresAuth
         * @return Builder
         */
        public Builder authenticationKey(String authenticationKey) {
            this.authenticationKey = authenticationKey;
            return this;
        }

        /**
         * 
         * @param key String value for key
         * @param param Object value for param
         * @return Builder
         */
        public Builder queryParam(Consumer<Parameter.Builder> action) {
            action.accept(parameterBuilder);
            Parameter queryParameter = parameterBuilder.build();
            queryParameter.validate();
            this.queryParams.put(queryParameter.getKey(), queryParameter.getValue());
            return this;
        }

        /**
         * 
         * @param key String value for key
         * @param param Object value for param
         * @return Builder
         */
        public Builder queryParam(Map<String, Object> queryParameters) {
            this.queryParams.putAll(queryParameters);
            return this;
        }

        /**
         * 
         * @param key String value for key
         * @param param SimpleEntry<Object, Boolean> value for param
         * @return Builder
         */
        public Builder templateParam(Consumer<Parameter.Builder> action) {
            parameterBuilder = new Parameter.Builder();
            action.accept(parameterBuilder);
            Parameter templateParameter = parameterBuilder.build();
            templateParameter.validate();
            SimpleEntry<Object, Boolean> templateEntry = new SimpleEntry<Object, Boolean>(
                    templateParameter.getValue(), templateParameter.shouldEncode());
            this.templateParams.put(templateParameter.getKey(), templateEntry);
            return this;
        }

        /**
         * 
         * @param headerParams HttpHeaders for headerParams
         * @return Builder
         */
        public Builder headerParam(Consumer<Parameter.Builder> action) {
            parameterBuilder = new Parameter.Builder();
            action.accept(parameterBuilder);
            Parameter httpHeaderParameter = parameterBuilder.build();
            httpHeaderParameter.validate();
            String key = httpHeaderParameter.getKey();
            String value = httpHeaderParameter.getValue() == null ? null
                    : httpHeaderParameter.getValue().toString();

            if (headerParams.containsKey(key)) {
                headerParams.get(key).add(value);
            } else {
                List<String> headerValues = new ArrayList<String>();
                headerValues.add(value);
                headerParams.put(key, headerValues);
            }
            return this;
        }

        /**
         * @param key String value for key
         * @param param Object value for param
         * @return Builder
         */
        public Builder formParams(Consumer<Parameter.Builder> action) {
            parameterBuilder = new Parameter.Builder();
            action.accept(parameterBuilder);
            Parameter formParameter = parameterBuilder.build();
            formParameter.validate();
            this.formParams.add(formParameter);
            return this;
        }


        /**
         * 
         * @param body Object value for body
         * @return Builder
         */
        public Builder body(Consumer<Parameter.Builder> action) {
            parameterBuilder = new Parameter.Builder();
            action.accept(parameterBuilder);
            Parameter bodyParam = parameterBuilder.build();
            bodyParam.validate();
            if (bodyParam.getKey() != null && !bodyParam.getKey().isEmpty()) {
                if (bodyParameters == null) {
                    bodyParameters = new HashMap<String, Object>();
                }
                bodyParameters.put(bodyParam.getKey(), bodyParam.getValue());
            } else {
                this.body = bodyParam.getValue();
            }

            return this;
        }

        /**
         * @param bodySerializer Function value for bodySerializer
         * @return Builder
         */

        public Builder bodySerializer(Serializer bodySerializer) {
            this.bodySerializer = bodySerializer;
            return this;
        }

        /**
         * 
         * @param arraySerializationFormat the serialization format for the array
         * @return
         */
        public Builder arraySerializationFormat(ArraySerializationFormat arraySerializationFormat) {
            this.arraySerializationFormat = arraySerializationFormat;
            return this;
        }

        /**
         * Initialise the CoreHttpRequest
         * 
         * @param coreConfig the configuration for the Http request
         * @return
         * @throws IOException
         */
        public Request build(GlobalConfiguration coreConfig) throws IOException {
            HttpRequest coreRequest = new HttpRequest(coreConfig, server, path, httpMethod,
                    authenticationKey, queryParams, templateParams, headerParams, formParams, body,
                    bodySerializer, bodyParameters, arraySerializationFormat);
            Request coreHttpRequest = coreRequest.getCoreHttpRequest();

            if (coreConfig.getHttpCallback() != null) {
                coreConfig.getHttpCallback().onBeforeRequest(coreHttpRequest);
            }

            return coreHttpRequest;
        }
    }
}
