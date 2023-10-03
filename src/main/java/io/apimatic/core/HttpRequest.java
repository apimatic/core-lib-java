package io.apimatic.core;

import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import io.apimatic.core.authentication.AuthBuilder;
import io.apimatic.core.types.http.request.MultipartFileWrapper;
import io.apimatic.core.types.http.request.MultipartWrapper;
import io.apimatic.core.utilities.CoreHelper;
import io.apimatic.coreinterfaces.authentication.Authentication;
import io.apimatic.coreinterfaces.compatibility.CompatibilityFactory;
import io.apimatic.coreinterfaces.http.HttpHeaders;
import io.apimatic.coreinterfaces.http.Method;
import io.apimatic.coreinterfaces.http.request.ArraySerializationFormat;
import io.apimatic.coreinterfaces.http.request.MutliPartRequestType;
import io.apimatic.coreinterfaces.http.request.Request;
import io.apimatic.coreinterfaces.type.CoreFileWrapper;
import io.apimatic.coreinterfaces.type.functional.Serializer;

/**
 * An HttpRequest instance is built through an HttpRequest builder. A request's URI, headers, and
 * body can be set. Request bodies are provided through a body supplied to one of the POST, PUT or
 * method methods. Once all required parameters have been set in the builder, build will return the
 * HttpRequest. Builders can be copied and modified many times in order to build multiple related
 * requests that differ in some parameters.
 */
public final class HttpRequest {
    /**
     * An instance of {@link Request}.
     */
    private final Request coreHttpRequest;

    /**
     * An instance of {@link GlobalConfiguration}.
     */
    private final GlobalConfiguration coreConfig;

    /**
     * A StringBuilder.
     */
    private final StringBuilder urlBuilder;

    /**
     * An instance of {@link CompatibilityFactory}.
     */
    private final CompatibilityFactory compatibilityFactory;

    /**
     * @param coreConfig
     * @param server
     * @param path
     * @param httpMethod
     * @param authenticationKey
     * @param queryParams
     * @param templateParams
     * @param headerParams
     * @param formParams
     * @param body
     * @param formParameters
     * @param bodySerializer
     * @param bodyParameters
     * @param arraySerializationFormat
     * @throws IOException
     */
    private HttpRequest(final GlobalConfiguration coreConfig, final String server,
            final String path, final Method httpMethod, final Authentication authentication,
            final Map<String, Object> queryParams,
            final Map<String, SimpleEntry<Object, Boolean>> templateParams,
            final Map<String, List<String>> headerParams, final Set<Parameter> formParams,
            final Map<String, Object> formParameters, final Object body,
            final Serializer bodySerializer, final Map<String, Object> bodyParameters,
            final ArraySerializationFormat arraySerializationFormat) throws IOException {
        this.coreConfig = coreConfig;
        this.compatibilityFactory = coreConfig.getCompatibilityFactory();
        urlBuilder = getStringBuilder(server, path);

        processTemplateParams(templateParams);
        Object bodyValue = buildBody(body, bodySerializer, bodyParameters);
        List<SimpleEntry<String, Object>> formFields =
                generateFormFields(formParams, formParameters, arraySerializationFormat);
        coreHttpRequest =
                buildRequest(httpMethod, bodyValue, addHeaders(headerParams), queryParams,
                        formFields, arraySerializationFormat);
        applyAuthentication(authentication);
    }

	/**
     * @return the {@link Request} instance which is used for making {@link ApiCall}.
     */
    public Request getCoreHttpRequest() {
        return coreHttpRequest;
    }

    private Request buildRequest(
            Method httpMethod, Object body, HttpHeaders headerParams,
            Map<String, Object> queryParams, List<SimpleEntry<String, Object>> formFields,
            ArraySerializationFormat arraySerializationFormat) throws IOException {
        if (body != null) {
            return compatibilityFactory.createHttpRequest(httpMethod, urlBuilder, headerParams,
                    queryParams, body);
        }

        return compatibilityFactory.createHttpRequest(httpMethod, urlBuilder, headerParams,
                queryParams, formFields);
    }

    private void applyAuthentication(Authentication authentication) {
    	if(authentication != null) {
        	if (!authentication.isValid()) {
        		throw new IllegalStateException(authentication.getErrorMessage());
        	}
        	
            authentication.apply(coreHttpRequest); 
        }
	}
    
    /**
     * @param formParams
     * @param optionalFormParamaters
     * @param arraySerializationFormat
     * @return list of form parameters
     * @throws IOException
     */
    private List<SimpleEntry<String, Object>> generateFormFields(
            Set<Parameter> formParams, Map<String, Object> optionalFormParamaters,
            ArraySerializationFormat arraySerializationFormat) throws IOException {
        if (formParams.isEmpty() && optionalFormParamaters.isEmpty()) {
            return null;
        }
        Map<String, Object> formParameters = new HashMap<>();
        for (Parameter formParameter : formParams) {
            String key = formParameter.getKey();
            Object value = formParameter.getValue();
            if (formParameter.getMultiPartRequest() != null) {
                value = handleMultiPartRequest(formParameter);
            }

            formParameters.put(key, value);
        }

        formParameters.putAll(optionalFormParamaters);
        return CoreHelper.prepareFormFields(formParameters, arraySerializationFormat);
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
        if (coreConfig.getAdditionalHeaders() != null) {
            headerParams.putAll(coreConfig.getAdditionalHeaders().asMultimap());
        }
    }

    private Object buildBody(
            Object body, Serializer bodySerializer, Map<String, Object> bodyParameters)
            throws IOException {
        if (body != null) {
            if (bodySerializer != null) {
                return bodySerializer.supply();
            }

            if (body instanceof CoreFileWrapper) {
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

    private Object handleMultiPartRequest(Parameter formParameter) throws IOException {
        HttpHeaders multipartFileHeaders =
                compatibilityFactory.createHttpHeaders(formParameter.getMultipartHeaders());

        if (formParameter.getMultiPartRequest() == MutliPartRequestType.MULTI_PART_FILE) {
            return new MultipartFileWrapper((CoreFileWrapper) formParameter.getValue(),
                    multipartFileHeaders);
        }
        String value = formParameter.getMultipartSerializer().supply();
        return new MultipartWrapper(value, multipartFileHeaders);

    }

    public static class Builder {
        /**
         * A string of server.
         */
        private String server;

        /**
         * A string of path.
         */
        private String path;

        /**
         * A HttpMethod.
         */
        private Method httpMethod;
        
        /**
         * An auth builder for the request.
         */
        private AuthBuilder authBuilder = new AuthBuilder();

        /**
         * A map of query parameters.
         */
        private Map<String, Object> queryParams = new HashMap<>();

        /**
         * A map of template parameters.
         */
        private Map<String, SimpleEntry<Object, Boolean>> templateParams = new HashMap<>();

        /**
         * A map of header parameters.
         */
        private Map<String, List<String>> headerParams = new HashMap<>();

        /**
         * A set of {@link Parameter}.
         */
        private Set<Parameter> formParams = new HashSet<>();

        /**
         * A map of form parameters
         */
        private Map<String, Object> formParamaters = new HashMap<>();

        /**
         * A body's object.
         */
        private Object body;

        /**
         * A body {@link Serializer} function.
         */
        private Serializer bodySerializer;

        /**
         * A map of body parameters.
         */
        private Map<String, Object> bodyParameters;

        /**
         * A array serialization format.
         */
        private ArraySerializationFormat arraySerializationFormat =
                ArraySerializationFormat.INDEXED;

        /**
         * An instance of {@link Parameter.Builder}.
         */
        private Parameter.Builder parameterBuilder = new Parameter.Builder();

        /**
         * Base uri server address.
         * @param server the base uri address.
         * @return Builder.
         */
        public Builder server(String server) {
            this.server = server;
            return this;
        }

        /**
         * Endpoint route path.
         * @param path the endpoint path.
         * @return Builder.
         */
        public Builder path(String path) {
            this.path = path;
            return this;
        }

        /**
         * Http Request Method.
         * @param httpMethod HttpMethod value for httpMethod.
         * @return Builder.
         */
        public Builder httpMethod(Method httpMethod) {
            this.httpMethod = httpMethod;
            return this;
        }

        /**
         * Setter for authentication key.
         * @param authenticationKey string value for authenticationKey.
         * @return Builder.
         */
        public Builder authenticationKey(String authenticationKey) {
            authBuilder = authBuilder.add(authenticationKey);
            return this;
        }
        
        /**
         * Setter for Authentication Builder, used for authenticating the request.
         * @param authBuilder the builder for authentication.
         * @return Builder.
         */
        public Builder withAuth(Consumer<AuthBuilder> action) {
            action.accept(authBuilder);
            return this;
        }

        /**
         * Optional query parameters.
         * @param queryParameters the optional query parameter.
         * @return Builder.
         */
        public Builder queryParam(Map<String, Object> queryParameters) {
            this.queryParams.putAll(queryParameters);
            return this;
        }


        /**
         * To configure the query paramater.
         * @param action the query parameter {@link Consumer}.
         * @return Builder.
         */
        public Builder queryParam(Consumer<Parameter.Builder> action) {
            action.accept(parameterBuilder);
            Parameter queryParameter = parameterBuilder.build();
            queryParameter.validate();
            this.queryParams.put(queryParameter.getKey(), queryParameter.getValue());
            return this;
        }

        /**
         * To configure the template parameter.
         * @param action the template parameter {@link Consumer}.
         * @return Builder.
         */
        public Builder templateParam(Consumer<Parameter.Builder> action) {
            parameterBuilder = new Parameter.Builder();
            action.accept(parameterBuilder);
            Parameter templateParameter = parameterBuilder.build();
            templateParameter.validate();
            SimpleEntry<Object, Boolean> templateEntry =
                    new SimpleEntry<Object, Boolean>(templateParameter.getValue(),
                            templateParameter.shouldEncode());
            this.templateParams.put(templateParameter.getKey(), templateEntry);
            return this;
        }

        /**
         * To configure the header parameter.
         * @param action the header parameter {@link Consumer}.
         * @return Builder.
         */
        public Builder headerParam(Consumer<Parameter.Builder> action) {
            parameterBuilder = new Parameter.Builder();
            action.accept(parameterBuilder);
            Parameter httpHeaderParameter = parameterBuilder.build();
            httpHeaderParameter.validate();
            String key = httpHeaderParameter.getKey();
            String value =
                    httpHeaderParameter.getValue() == null ? null
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
         * To configure the form parameter.
         * @param action the form parameter {@link Consumer}.
         * @return Builder.
         */
        public Builder formParam(Consumer<Parameter.Builder> action) {
            parameterBuilder = new Parameter.Builder();
            action.accept(parameterBuilder);
            Parameter formParameter = parameterBuilder.build();
            formParameter.validate();
            this.formParams.add(formParameter);
            return this;
        }

        /**
         * To configure the optional form parameters.
         * @param formParameters the optional form parameter map.
         * @return Builder.
         */
        public Builder formParam(Map<String, Object> formParameters) {
            this.formParamaters.putAll(formParameters);
            return this;
        }


        /**
         * To configure the body parameter.
         * @param action the body parameter {@link Consumer}.
         * @return Builder.
         */
        public Builder bodyParam(Consumer<Parameter.Builder> action) {
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
         * @param bodySerializer Function value for bodySerializer.
         * @return Builder.
         */
        public Builder bodySerializer(Serializer bodySerializer) {
            this.bodySerializer = bodySerializer;
            return this;
        }

        /**
         * @param arraySerializationFormat the serialization format for the array.
         * @return Builder.
         */
        public Builder arraySerializationFormat(ArraySerializationFormat arraySerializationFormat) {
            this.arraySerializationFormat = arraySerializationFormat;
            return this;
        }

        /**
         * Initialise the CoreHttpRequest.
         * @param coreConfig the configuration for the Http request.
         * @return {@link Request}.
         * @throws IOException Signals that an I/O exception of some sort has occurred.
         */
        public Request build(GlobalConfiguration coreConfig) throws IOException {
            Authentication authentication = authBuilder.build(coreConfig.getAuthentications());
            HttpRequest coreRequest =
                    new HttpRequest(coreConfig, server, path, httpMethod, authentication,
                            queryParams, templateParams, headerParams, formParams, formParamaters,
                            body, bodySerializer, bodyParameters, arraySerializationFormat);
            Request coreHttpRequest = coreRequest.getCoreHttpRequest();

            if (coreConfig.getHttpCallback() != null) {
                coreConfig.getHttpCallback().onBeforeRequest(coreHttpRequest);
            }

            return coreHttpRequest;
        }
    }
}
