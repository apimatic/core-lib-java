package io.apimatic.core_lib;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import io.apimatic.core_interfaces.authentication.Authentication;
import io.apimatic.core_interfaces.compatibility.CompatibilityFactory;
import io.apimatic.core_interfaces.http.HttpCallback;
import io.apimatic.core_interfaces.http.HttpClient;
import io.apimatic.core_lib.utilities.CoreHelper;

/**
 * A class which hold the global configuration properties to make a successful Api Call
 *
 */
public class GlobalConfiguration {
    private CompatibilityFactory compatibilityFactory;
    private String userAgent;
    private Map<String, String> userAgentConfig;
    private Map<String, Authentication> authentications;
    private HttpCallback httpCallback;
    private HttpClient httpClient;
    private Map<String, List<String>> globalHeaders;
    private Map<String, List<String>> additionalHeaders;
    private Function<String, String> baseUri;

    /**
     * A private constructor
     * 
     * @param compatibilityFactory
     * @param userAgent
     * @param userAgentConfig
     * @param authentications
     * @param httpCallback
     * @param httpClient
     * @param globalHeaders
     * @param baseUri
     */
    private GlobalConfiguration(CompatibilityFactory compatibilityFactory, String userAgent,
            Map<String, String> userAgentConfig, Map<String, Authentication> authentications,
            HttpCallback httpCallback, HttpClient httpClient,
            Map<String, List<String>> globalHeaders, Map<String, List<String>> additionalHeaders,
            Function<String, String> baseUri) {
        this.compatibilityFactory = compatibilityFactory;
        this.userAgent = userAgent;
        this.userAgentConfig = userAgentConfig;
        this.authentications = authentications;
        this.httpCallback = httpCallback;
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
     * @return the httpCallback instance
     */
    public HttpCallback getHttpCallback() {
        return httpCallback;
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
    public Map<String, List<String>> getAdditionalHeaders() {
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
                .userAgent(userAgent).authentication(authentications).httpCallback(httpCallback)
                .httpClient(httpClient).globalHeaders(globalHeaders).baseUri(baseUri);
        return builder;

    }

    public static class Builder {
        private CompatibilityFactory compatibilityFactory;
        private String userAgent;
        private Map<String, String> userAgentConfig;
        private Map<String, Authentication> authentications;
        private HttpCallback httpCallback;
        private HttpClient httpClient;
        private Map<String, List<String>> globalHeaders;
        private Map<String, List<String>> additionalheaders = new HashMap<>();
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
         * @param httpCallback value for HttpCallback
         * @return
         */
        public Builder httpCallback(HttpCallback httpCallback) {
            this.httpCallback = httpCallback;
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
         * @param headers value for HttpHeaders
         * @return Builder
         */
        public Builder globalHeaders(Map<String, List<String>> headers) {
            this.globalHeaders = headers;
            return this;
        }

        /**
         * 
         * @param additional headers value for HttpHeaders
         * @return Builder
         */
        public Builder additionalHeaders(String key, String value) {
            if (additionalheaders.containsKey(key)) {
                additionalheaders.get(key).add(value);
            } else {
                List<String> headerValues = new ArrayList<String>();
                headerValues.add(value);
                additionalheaders.put(key, headerValues);
            }
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
                    authentications, httpCallback, httpClient, globalHeaders, additionalheaders,
                    baseUri);
        }
    }

}