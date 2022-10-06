package io.apimatic.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import io.apimatic.core.utilities.CoreHelper;
import io.apimatic.coreinterfaces.authentication.Authentication;
import io.apimatic.coreinterfaces.compatibility.CompatibilityFactory;
import io.apimatic.coreinterfaces.http.Callback;
import io.apimatic.coreinterfaces.http.HttpClient;
import io.apimatic.coreinterfaces.http.HttpHeaders;

/**
 * A class which hold the global configuration properties to make a successful Api Call
 *
 */
public class GlobalConfiguration {
    private CompatibilityFactory compatibilityFactory;
    private String userAgent;
    private Map<String, String> userAgentConfig;
    private Map<String, Authentication> authentications;
    private Callback callback;
    private HttpClient httpClient;
    private Map<String, List<String>> globalHeaders;
    private HttpHeaders additionalHeaders;
    private Function<String, String> baseUri;

    /**
     * A private constructor
     * 
     * @param compatibilityFactory
     * @param userAgent
     * @param userAgentConfig
     * @param authentications
     * @param callback
     * @param httpClient
     * @param globalHeaders
     * @param baseUri
     */
    private GlobalConfiguration(CompatibilityFactory compatibilityFactory, String userAgent,
            Map<String, String> userAgentConfig, Map<String, Authentication> authentications,
            Callback callback, HttpClient httpClient, Map<String, List<String>> globalHeaders,
            HttpHeaders additionalHeaders, Function<String, String> baseUri) {
        this.compatibilityFactory = compatibilityFactory;
        this.userAgent = userAgent;
        this.userAgentConfig = userAgentConfig;
        this.authentications = authentications;
        this.callback = callback;
        this.httpClient = httpClient;
        this.globalHeaders = globalHeaders != null ? globalHeaders : new HashMap<>();
        this.additionalHeaders = additionalHeaders;
        this.baseUri = baseUri;

        if (this.userAgent != null) {
            this.userAgent = CoreHelper.updateUserAgent(userAgent, userAgentConfig);
            this.globalHeaders.put("user-agent", Arrays.asList(this.userAgent));
        }
    }

    /**
     * 
     * @return the compatibilityFactory instance
     */
    public CompatibilityFactory getCompatibilityFactory() {
        return compatibilityFactory;
    }

    /**
     * 
     * @return the UserAgent string
     */
    public String getUserAgent() {
        return userAgent;
    }

    /**
     * @return the userAgentConfig
     */
    public Map<String, String> getUserAgentConfig() {
        return userAgentConfig;
    }

    /**
     * 
     * @return the map of AuthManager
     */
    public Map<String, Authentication> getAuthentications() {
        return authentications;
    }

    /**
     * 
     * @return the callback instance
     */
    public Callback getHttpCallback() {
        return callback;
    }

    /**
     * 
     * @return the httpClient instance
     */
    public HttpClient getHttpClient() {
        return httpClient;
    }

    /**
     * 
     * @return the Map of global headers
     */
    public Map<String, List<String>> getGlobalHeaders() {
        return globalHeaders;
    }

    /**
     * 
     * @return the Map of additional headers
     */
    public HttpHeaders getAdditionalHeaders() {
        return additionalHeaders;
    }

    /**
     * 
     * @return the baseUri function
     */
    public Function<String, String> getBaseUri() {
        return baseUri;
    }

    /**
     * Builds a new {@link GlobalConfiguration.Builder} object. Creates the instance with the state
     * of the current state.
     * 
     * @return a new {@link GlobalConfiguration.Builder} object
     */
    public Builder toBuilder() {
        Builder builder = new Builder().compatibilityFactory(compatibilityFactory)
                .userAgent(userAgent).authentication(authentications).callback(callback)
                .httpClient(httpClient).globalHeader(globalHeaders).baseUri(baseUri);
        return builder;

    }
    
    public static class Builder {
        private CompatibilityFactory compatibilityFactory;
        private String userAgent;
        private Map<String, String> userAgentConfig;
        private Map<String, Authentication> authentications;
        private Callback callback;
        private HttpClient httpClient;
        private Map<String, List<String>> globalHeaders = new HashMap<>();
        private HttpHeaders additionalheaders;
        private Function<String, String> baseUri;

        /**
         * 
         * @param compatibilityFactory value for CompatibilityFactor
         * @return Builder
         */
        public Builder compatibilityFactory(CompatibilityFactory compatibilityFactory) {
            this.compatibilityFactory = compatibilityFactory;
            return this;
        }

        /**
         * 
         * @param userAgent String value for UserAgent
         * @return Builder
         */
        public Builder userAgent(String userAgent) {
            this.userAgent = userAgent;
            return this;
        }

        /**
         * 
         * @param userAgentConfig Map values for userAgentConfig
         * @return Builder
         */
        public Builder userAgentConfig(Map<String, String> userAgentConfig) {
            this.userAgentConfig = userAgentConfig;
            return this;
        }

        /**
         * 
         * @param authentications Map values for authManagers
         * @return Builder
         */
        public Builder authentication(Map<String, Authentication> authentications) {
            this.authentications = authentications;
            return this;
        }

        /**
         * 
         * @param callback value for callback
         * @return Builder
         */
        public Builder callback(Callback callback) {
            this.callback = callback;
            return this;
        }

        /**
         * 
         * @param httpClient value for
         * @return Builder
         */
        public Builder httpClient(HttpClient httpClient) {
            this.httpClient = httpClient;
            return this;
        }

        /**
         * 
         * @param key the global header key
         * @param value the global header value
         * @return Builder
         */
        public Builder globalHeader(String key, String value) {
            if (globalHeaders.containsKey(key)) {
                globalHeaders.get(key).add(value);
            } else {
                List<String> headerValues = new ArrayList<String>();
                headerValues.add(value);
                globalHeaders.put(key, headerValues);
            }
            return this;
        }

        /**
         * 
         * @param headers value for HttpHeaders
         * @return Builder
         */
        private Builder globalHeader(Map<String, List<String>> headers) {
            globalHeaders = headers;
            return this;
        }

        /**
         * Additional headers which you can configure other than endpoints headers
         * 
         * @param additionalHeaders headers which is configured other than endpoints
         * @return Builder
         */
        public Builder additionalHeaders(HttpHeaders additionalHeaders) {
            this.additionalheaders = additionalHeaders;
            return this;
        }

        /**
         * 
         * @param baseUri value for BaseUri
         * @return Builder
         */
        public Builder baseUri(Function<String, String> baseUri) {
            this.baseUri = baseUri;
            return this;
        }

        /**
         * Builds a new {@link GlobalConfiguration} object using the set fields.
         * 
         * @return {@link GlobalConfiguration}
         */
        public GlobalConfiguration build() {
            return new GlobalConfiguration(compatibilityFactory, userAgent, userAgentConfig,
                    authentications, callback, httpClient, globalHeaders, additionalheaders,
                    baseUri);
        }
    }

}
