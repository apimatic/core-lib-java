package io.apimatic.core;

import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

import io.apimatic.core.authentication.AuthBuilder;
import io.apimatic.core.exceptions.AuthValidationException;
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
    private final StringBuilder queryUrlBuilder;

    /**
     * An instance of {@link CompatibilityFactory}.
     */
    private final CompatibilityFactory compatibilityFactory;

    /**
     * @param coreConfig
     * @param server
     * @param path
     * @param httpMethod
     * @param authentication
     * @param queryParams
     * @param templateParams
     * @param headerParams
     * @param multipartFormParams
     * @param formParameters
     * @param body
     * @param bodySerializer
     * @param bodyParameters
     * @param arraySerializationFormat
     * @throws IOException
     */
    private HttpRequest(final GlobalConfiguration coreConfig, final String server,
            final String path, final Method httpMethod, final Authentication authentication,
            final Map<String, Object> queryParams,
            final Map<String, SimpleEntry<Object, Boolean>> templateParams,
            final Map<String, List<String>> headerParams, final Set<Parameter> multipartFormParams,
            final Map<String, Object> formParameters, final Object body,
            final Serializer bodySerializer, final Map<String, Object> bodyParameters,
            final ArraySerializationFormat arraySerializationFormat) throws IOException {
        this.coreConfig = coreConfig;
        this.compatibilityFactory = coreConfig.getCompatibilityFactory();
        HttpHeaders requestHeaders = addHeaders(headerParams);
        // Creating a basic request to provide it to auth instances
        Request request = buildBasicRequest(httpMethod, requestHeaders);

        if (request != null) {
            applyAuthentication(request, authentication);
            // include auth query parameters in request query params to have them in the query url
            if (request.getQueryParameters() != null) {
                queryParams.putAll(request.getQueryParameters());
            }
        }

        queryUrlBuilder = getStringBuilder(server, path, queryParams, arraySerializationFormat);
        processTemplateParams(templateParams);
        Object bodyValue = buildBody(body, bodySerializer, bodyParameters);
        List<SimpleEntry<String, Object>> formFields =
                generateFormFields(multipartFormParams, formParameters, arraySerializationFormat);
        coreHttpRequest =
                buildRequest(httpMethod, bodyValue, requestHeaders, queryParams,
                        formFields, arraySerializationFormat);
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
            return compatibilityFactory.createHttpRequest(httpMethod, queryUrlBuilder, headerParams,
                    queryParams, body);
        }

        return compatibilityFactory.createHttpRequest(httpMethod, queryUrlBuilder, headerParams,
                queryParams, formFields);
    }

    /**
     * Builds a request with minimal query parameters.
     * @param httpMethod The request HTTP verb.
     * @param headerParams The request header parameters.
     * @return the {@link Request} instance.
     */
    private Request buildBasicRequest(Method httpMethod, HttpHeaders headerParams)
            throws IOException {
        return compatibilityFactory.createHttpRequest(httpMethod, null, headerParams,
                new HashMap<String, Object>(), Collections.emptyList());
    }

    private void applyAuthentication(Request request, Authentication authentication) {
        if (authentication != null) {
            authentication.validate();
            if (!authentication.isValid()) {
                throw new AuthValidationException(authentication.getErrorMessage());
            }

            authentication.apply(request);
        }
    }

    /**
     * @param multipartFormParams
     * @param formParameters
     * @param arraySerializationFormat
     * @return list of form parameters
     * @throws IOException
     */
    private List<SimpleEntry<String, Object>> generateFormFields(
            Set<Parameter> multipartFormParams, Map<String, Object> formParameters,
            ArraySerializationFormat arraySerializationFormat) throws IOException {
        if (multipartFormParams.isEmpty() && formParameters.isEmpty()) {
            return null;
        }
        Map<String, Object> formParamsMap = new HashMap<>();

        for (Parameter formParameter : multipartFormParams) {
            Object value = handleMultiPartRequest(formParameter);
            formParamsMap.put(formParameter.getKey(), value);
        }

        formParamsMap.putAll(formParameters);
        return CoreHelper.prepareFormFields(formParamsMap, arraySerializationFormat);
    }

    private StringBuilder getStringBuilder(String server, String path,
            Map<String, Object> queryParams,
            ArraySerializationFormat arraySerializationFormat) {

        StringBuilder urlBuilder = new StringBuilder(coreConfig.getBaseUri().apply(server) + path);

        // set query parameters
        CoreHelper.appendUrlWithQueryParameters(urlBuilder, queryParams,
                arraySerializationFormat);

        return new StringBuilder(CoreHelper.cleanUrl(urlBuilder));
    }

    private void processTemplateParams(Map<String, SimpleEntry<Object, Boolean>> templateParams) {
        if (!templateParams.isEmpty()) {
            CoreHelper.appendUrlWithTemplateParameters(queryUrlBuilder, templateParams);
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
        private Map<String, List<Object>> headerParams = new HashMap<>();

        /**
         * A set of {@link Parameter}.
         */
        private Set<Parameter> multipartFormParams = new HashSet<>();

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
         * Update the request parameters using a setter thats called via a JSON pointer.
         *
         * @param pointer A JSON pointer pointing to any request field.
         * @param setter A function that takes in an old value and returns a new value.
         * @return The updated instance of current request builder.
         */
        public Builder updateParameterByJsonPointer(String pointer, UnaryOperator<Object> setter) {
            if (pointer == null) {
                return this;
            }

            String[] pointerParts = pointer.split("#");
            String prefix = pointerParts[0];
            String point = pointerParts.length > 1 ? pointerParts[1] : "";

            switch (prefix) {
            case "$request.path":
                updateTemplateParams(setter, point);
                return this;
            case "$request.query":
                queryParams = CoreHelper.updateValueByPointer(queryParams, point, setter);
                return this;
            case "$request.headers":
                updateHeaderParams(setter, point);
                return this;
            case "$request.body":
                updateBodyParams(setter, point);
                return this;
            default:
                return this;
            }
        }

        private void updateBodyParams(UnaryOperator<Object> setter, String point) {
            if (body != null) {
                if (body instanceof CoreFileWrapper) {
                    return;
                }

                if (bodySerializer != null && "".equals(point)) {
                    try {
                        String serializedBody = bodySerializer.supply();
                        String newSerializedBody = setter.apply(serializedBody).toString();
                        bodySerializer = () -> newSerializedBody;
                    } catch (Exception e) {
                        // Empty block
                    }
                    return;
                }

                if ((body instanceof String && "".equals(point)) || "".equals(point)) {
                    body = setter.apply(body);
                    return;
                }

                body = CoreHelper.updateValueByPointer(body, point, setter);
                return;
            }

            if (bodyParameters != null) {
                bodyParameters = CoreHelper.updateValueByPointer(bodyParameters, point, setter);
                return;
            }

            formParamaters = CoreHelper.updateValueByPointer(formParamaters, point, setter);
        }

        @SuppressWarnings("unchecked")
        private void updateHeaderParams(UnaryOperator<Object> setter, String point) {
            Map<String, Object> simplifiedHeaders = new HashMap<>();
            for (Entry<String, List<Object>> entry : headerParams.entrySet()) {
                if (entry.getValue().size() == 1) {
                    simplifiedHeaders.put(entry.getKey(), entry.getValue().get(0));
                } else {
                    simplifiedHeaders.put(entry.getKey(), entry.getValue());
                }
            }

            simplifiedHeaders = CoreHelper.updateValueByPointer(simplifiedHeaders, point, setter);

            for (Map.Entry<String, Object> entry : simplifiedHeaders.entrySet()) {
                if (entry.getValue() instanceof List<?>) {
                    headerParams.put(entry.getKey(), (List<Object>) entry.getValue());
                } else {
                    headerParams.put(entry.getKey(), Arrays.asList(entry.getValue()));
                }
            }
        }

        private void updateTemplateParams(UnaryOperator<Object> setter, String point) {
            Map<String, Object> simplifiedPath = new HashMap<>();
            for (Map.Entry<String, SimpleEntry<Object, Boolean>> entry
                    : templateParams.entrySet()) {
                simplifiedPath.put(entry.getKey(), entry.getValue().getKey());
            }

            simplifiedPath = CoreHelper.updateValueByPointer(simplifiedPath, point, setter);

            for (Map.Entry<String, Object> entry : simplifiedPath.entrySet()) {
                // Preserve the original boolean encoding flag
                Boolean originalFlag = templateParams.get(entry.getKey()).getValue();
                templateParams.put(entry.getKey(),
                        new SimpleEntry<>(entry.getValue(), originalFlag));
            }
        }

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
         * Setter for Authentication Builder, used for authenticating the request.
         * @param consumer the builder consumer for authentication.
         * @return Builder.
         */
        public Builder withAuth(Consumer<AuthBuilder> consumer) {
            consumer.accept(authBuilder);
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

            if (headerParams.containsKey(key)) {
                headerParams.get(key).add(httpHeaderParameter.getValue());
            } else {
                List<Object> headerValues = new ArrayList<Object>();
                headerValues.add(httpHeaderParameter.getValue());
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
            if (formParameter.getMultiPartRequest() != null) {
                this.multipartFormParams.add(formParameter);
                return this;
            }
            this.formParamaters.put(formParameter.getKey(), formParameter.getValue());

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

        private Map<String, List<String>> getHeaderParams() {
            Map<String, List<String>> converted = new HashMap<>();

            for (Map.Entry<String, List<Object>> entry : headerParams.entrySet()) {
                String key = entry.getKey();
                List<Object> originalList = entry.getValue();

                List<String> serializedList = new ArrayList<>();
                for (Object obj : originalList) {
                    serializedList.add(getSerializedHeaderValue(obj));
                }

                converted.put(key, serializedList);
            }

            return converted;
        }

        private static String getSerializedHeaderValue(Object obj) {
            if (obj == null) {
                return null;
            }

            if (CoreHelper.isTypeCombinatorStringCase(obj)
                    || CoreHelper.isTypeCombinatorDateTimeCase(obj)
                    || obj instanceof String) {
                return obj.toString();
            }

            return CoreHelper.trySerialize(obj);
        }

        /**
         * @return A copy of this request builder instance.
         */
        public Builder copy() {
            Builder copy = new Builder();
            copy.server = server;
            copy.path = path;
            copy.httpMethod = httpMethod;
            copy.authBuilder = authBuilder.copy();
            copy.queryParams = new HashMap<>(queryParams);
            for (Entry<String, SimpleEntry<Object, Boolean>> entry : templateParams.entrySet()) {
                copy.templateParams.put(entry.getKey(),
                    new SimpleEntry<>(entry.getValue().getKey(), entry.getValue().getValue()));
            }
            for (Entry<String, List<Object>> entry : headerParams.entrySet()) {
                copy.headerParams.put(entry.getKey(), new ArrayList<>(entry.getValue()));
            }
            copy.multipartFormParams = new HashSet<>(multipartFormParams);
            copy.formParamaters = new HashMap<>(formParamaters);
            copy.body = body;
            copy.bodySerializer = bodySerializer;
            if (bodyParameters != null) {
                copy.bodyParameters = new HashMap<>(bodyParameters);
            }
            copy.arraySerializationFormat = arraySerializationFormat;
            copy.parameterBuilder = new Parameter.Builder();

            return copy;
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
                            queryParams, templateParams, getHeaderParams(), multipartFormParams,
                            formParamaters, body, bodySerializer, bodyParameters,
                            arraySerializationFormat);
            Request coreHttpRequest = coreRequest.getCoreHttpRequest();

            if (coreConfig.getHttpCallback() != null) {
                coreConfig.getHttpCallback().onBeforeRequest(coreHttpRequest);
            }

            return coreHttpRequest;
        }
    }
}
