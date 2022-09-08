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
import io.apimatic.core_interfaces.http.CoreHttpMethod;
import io.apimatic.core_interfaces.http.HttpHeaders;
import io.apimatic.core_interfaces.http.request.ArraySerializationFormat;
import io.apimatic.core_interfaces.http.request.CoreHttpRequest;
import io.apimatic.core_interfaces.http.request.MutliPartRequestType;
import io.apimatic.core_interfaces.type.FileWrapper;
import io.apimatic.core_interfaces.type.functional.Serializer;
import io.apimatic.core_lib.types.http.request.MultipartFileWrapper;
import io.apimatic.core_lib.types.http.request.MultipartWrapper;
import io.apimatic.core_lib.utilities.CoreHelper;

public class CoreRequest {
    private final CoreHttpRequest coreHttpRequest;
    private final CoreConfig coreConfig;
    private final StringBuilder urlBuilder;

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
    private CoreRequest(CoreConfig coreConfig, String server, String path,
            CoreHttpMethod httpMethod, String authenticationKey, Map<String, Object> queryParams,
            Map<String, SimpleEntry<Object, Boolean>> templateParams,
            Map<String, List<String>> headerParams, Set<Parameter> formParams, Object body,
            Serializer bodySerializer, Map<String, Object> bodyParameters,
            ArraySerializationFormat arraySerializationFormat) throws IOException {
        this.coreConfig = coreConfig;
        urlBuilder = getStringBuilder(server, path);

        processTemplateParams(templateParams);
        HttpHeaders headers = addHeaders(headerParams);
        body = buildBody(body, bodySerializer, bodyParameters);
        coreHttpRequest = buildRequest(httpMethod, body, headers, queryParams, formParams,
                arraySerializationFormat);
        applyAuthentication(authenticationKey);

    }


    public CoreHttpRequest getCoreHttpRequest() {
        return coreHttpRequest;
    }

    public static Builder builder(String server, String path) {
        return new CoreRequest.Builder();
    }

    private void applyAuthentication(String authenticationKey) {
        if (authenticationKey == null) {
            return;
        }

        Authentication authManager = coreConfig.getAuthentications().get(authenticationKey);
        if (authManager != null) {
            authManager.apply(coreHttpRequest);
        }
    }

    private CoreHttpRequest buildRequest(CoreHttpMethod httpMethod, Object body,
            HttpHeaders headerParams, Map<String, Object> queryParams, Set<Parameter> formParams,
            ArraySerializationFormat arraySerializationFormat) throws IOException {
        CompatibilityFactory compatibilityFactory = coreConfig.getCompatibilityFactory();

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
        CompatibilityFactory compatibilityFactory = coreConfig.getCompatibilityFactory();
        List<SimpleEntry<String, Object>> formFields = null;
        Map<String, Object> formParamaters = new HashMap();
        for (Parameter formParameter : formParams) {
            String key = formParameter.getKey();
            if (formParameter.getMultiPartRequest() != null) {
                formParamaters.put(key,
                        handleMultiPartRequest(formParameter, compatibilityFactory));
                continue;
            }

            formParamaters.put(key, formParameter.getValue());
        }
        if (!formParams.isEmpty()) {
            formFields = CoreHelper.prepareFormFields(formParamaters, arraySerializationFormat);

        }
        return formFields;
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
        return coreConfig.getCompatibilityFactory().createHttpHeaders(headerParams);

    }

    private void addGlobalHeader(Map<String, List<String>> headerParams) {
        if (coreConfig.getGlobalHeaders() != null) {
            headerParams.putAll(coreConfig.getGlobalHeaders().asMultimap());
        }
    }

    private Object buildBody(Object body, Serializer bodySerializer,
            Map<String, Object> bodyParameters) throws IOException {
        if (body != null) {
            if (bodySerializer != null) {
                return bodySerializer.apply(body);
            } else {
                if (body instanceof String) {
                    return body.toString();
                } else if (body instanceof FileWrapper) {
                    return body;
                } else {
                    return CoreHelper.serialize(body);
                }
            }
        }

        if (bodyParameters != null) {
            CoreHelper.removeNullValues(bodyParameters);
            return CoreHelper.serialize(bodyParameters);
        }

        return body;
    }

    private Object handleMultiPartRequest(Parameter formParameter,
            CompatibilityFactory compatibilityFactory) {
        HttpHeaders multipartFileHeaders =
                compatibilityFactory.createHttpHeaders(formParameter.getMultipartHeaders());


        if (formParameter.getMultiPartRequest() == MutliPartRequestType.MULTI_PART_FILE) {
            return new MultipartFileWrapper((FileWrapper) formParameter.getValue() ,
                    multipartFileHeaders);
        }
        return new MultipartWrapper(formParameter.getValue().toString(), multipartFileHeaders);

    }

    public static class Builder {
        private String server;
        private String path;
        private CoreHttpMethod httpMethod;
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


        public Builder server(String server) {
            this.server = server;
            return this;
        }

        public Builder path(String path) {
            this.path = path;
            return this;
        }

        /**
         * Setter for httpMethod
         * 
         * @param httpMethod HttpMethod value for httpMethod
         * @return Builder
         */
        public Builder httpMethod(CoreHttpMethod httpMethod) {
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
         * Key value pair for queryParams
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
         * Key value pair for queryParams
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
         * Key value pair for templateParams
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
         * Setter for httpHeaders
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
            String value = httpHeaderParameter.getValue().toString();

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
         * Key value pair for formParams
         * 
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
         * Setter for body
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
         * Setter for bodySerializer
         * 
         * @param bodySerializer Function value for bodySerializer
         * @return Builder
         */

        public Builder bodySerializer(Serializer bodySerializer) {
            this.bodySerializer = bodySerializer;
            return this;
        }

        public Builder arraySerializationFormat(ArraySerializationFormat arraySerializationFormat) {
            this.arraySerializationFormat = arraySerializationFormat;
            return this;
        }

        public CoreHttpRequest build(CoreConfig coreConfig) throws IOException {


            CoreRequest coreRequest = new CoreRequest(coreConfig, server, path, httpMethod,
                    authenticationKey, queryParams, templateParams, headerParams, formParams, body,
                    bodySerializer, bodyParameters, arraySerializationFormat);


            // Invoke the callback before request if its not null
            if (coreConfig.getHttpCallback() != null) {
                coreConfig.getHttpCallback().onBeforeRequest(coreRequest.getCoreHttpRequest());
            }

            return coreRequest.getCoreHttpRequest();

        }
    }

}
