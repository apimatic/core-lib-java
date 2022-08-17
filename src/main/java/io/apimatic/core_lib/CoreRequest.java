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
    private final String server;
    private final String Path;
    private final CoreHttpMethod httpMethod;
    private final boolean requiresAuth;
    private final Map<String, Object> queryParams;
    private final Map<String, SimpleEntry<Object, Boolean>> templateParams;
    private final HttpHeaders headerParams;
    private final Map<String, Object> formParams;
    private final Object body;
    private final Serializer bodySerializer;

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
     */
    private CoreRequest(String server, String path, CoreHttpMethod httpMethod, boolean requiresAuth,
            Map<String, Object> queryParams,
            Map<String, SimpleEntry<Object, Boolean>> templateParams, HttpHeaders headerParams,
            Map<String, Object> formParams, Object body, Serializer bodySerializer) {
        this.server = server;
        Path = path;
        this.httpMethod = httpMethod;
        this.requiresAuth = requiresAuth;
        this.queryParams = queryParams;
        this.templateParams = templateParams;
        this.headerParams = headerParams;
        this.formParams = formParams;
        this.body = body;
        this.bodySerializer = bodySerializer;
    }

    /**
     * @return the server
     */
    public String getServer() {
        return server;
    }

    /**
     * @return the path
     */
    public String getPath() {
        return Path;
    }

    /**
     * @return the httpMethod
     */
    public CoreHttpMethod getHttpMethod() {
        return httpMethod;
    }

    /**
     * @return the requiresAuth
     */
    public boolean isRequiresAuth() {
        return requiresAuth;
    }

    /**
     * @return the queryParams
     */
    public Map<String, Object> getQueryParams() {
        return queryParams;
    }

    /**
     * @return the templateParams
     */
    public Map<String, SimpleEntry<Object, Boolean>> getTemplateParams() {
        return templateParams;
    }

    /**
     * @return the headerParams
     */
    public HttpHeaders getHeaderParams() {
        return headerParams;
    }

    /**
     * @return the formParams
     */
    public Map<String, Object> getFormParams() {
        return formParams;
    }

    /**
     * @return the body
     */
    public Object getBody() {
        return body;
    }

    /**
     * @return the bodySerializer
     */
    public Serializer getBodySerializer() {
        return bodySerializer;
    }

    public static Builder builder(String server, String path) {
        return new CoreRequest.Builder(server, path);
    }

    public static class Builder {
        private String server;
        private String path;
        private CoreHttpMethod httpMethod;
        private boolean requiresAuth;
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

        public Builder(String server, String path) {
            this.server = server;
            this.path = path;
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
        public Builder requiresAuth(boolean requiresAuth) {
            this.requiresAuth = requiresAuth;
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
            CompatibilityFactory compatibilityFactory = coreConfig.getCompatibilityFactory();
            final StringBuilder urlBuilder = getStringBuilder(coreConfig);

            processTemplateParams(urlBuilder);
            addGlobalHeader(coreConfig);

            CoreHttpRequest request = buildRequest(compatibilityFactory, urlBuilder);

            // Invoke the callback before request if its not null
            if (coreConfig.getHttpCallback() != null) {
                coreConfig.getHttpCallback().onBeforeRequest(request);
            }

            return request;

        }

        private CoreHttpRequest buildRequest(CompatibilityFactory compatibilityFactory,
                StringBuilder urlBuilder) throws IOException {

            HttpHeaders headers = compatibilityFactory.createHttpHeaders(headerParams);
            if (body != null || bodyParameters != null) {

                return compatibilityFactory.createHttpRequest(httpMethod, urlBuilder, headers,
                        queryParams, buildBodyString(urlBuilder));

            } else {
                List<SimpleEntry<String, Object>> formFields =
                        generateFormFields(compatibilityFactory);
                return compatibilityFactory.createHttpRequest(httpMethod, urlBuilder, headers,
                        queryParams, formFields);

            }

        }

        /**
         * @param compatibilityFactory
         * @return
         */
        private List<SimpleEntry<String, Object>> generateFormFields(
                CompatibilityFactory compatibilityFactory) {
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

        private StringBuilder getStringBuilder(CoreConfig coreConfig) {
            return new StringBuilder(coreConfig.getBaseUri().apply(server) + path);
        }

        private void processTemplateParams(StringBuilder urlBuilder) {
            if (!templateParams.isEmpty()) {
                CoreHelper.appendUrlWithTemplateParameters(urlBuilder, templateParams);
            }
        }

        private void addGlobalHeader(CoreConfig coreConfig) {
            if (coreConfig.getGlobalHeaders() != null) {
                headerParams.putAll(coreConfig.getGlobalHeaders().asMultimap());
            }
        }

        private String buildBodyString(StringBuilder urlBuilder) throws IOException {
            String serializedBody = null;
            if (body != null) {
                if (bodySerializer != null) {
                    serializedBody = bodySerializer.apply(body);
                } else {
                    if (body instanceof String) {
                        serializedBody = body.toString();
                    } else {
                        serializedBody = CoreHelper.serialize(body);
                    }
                }
            }

            if (bodyParameters != null) {
                CoreHelper.removeNullValues(bodyParameters);
                serializedBody = CoreHelper.serialize(bodyParameters);
            }
            return serializedBody;

        }

        private Object handleMultiPartRequest(Parameter formParameter,
                CompatibilityFactory compatibilityFactory) {
            HttpHeaders multipartFileHeaders =
                    compatibilityFactory.createHttpHeaders(formParameter.getMultipartHeaders());


            if (formParameter.getMultiPartRequest() == MutliPartRequestType.MULTI_PART_FILE) {
                return new MultipartFileWrapper((FileWrapper) formParameter.getValue(),
                        multipartFileHeaders);
            }
            return new MultipartWrapper(formParameter.getValue().toString(), multipartFileHeaders);

        }
    }

}
