package io.apimatic.core_lib;

import java.util.Map;
import java.util.function.Function;
import io.apimatic.core_interfaces.authentication.AuthManager;
import io.apimatic.core_interfaces.compatibility.*;
import io.apimatic.core_interfaces.http.HttpCallback;
import io.apimatic.core_interfaces.http.HttpClient;
import io.apimatic.core_interfaces.http.HttpHeaders;

public class CoreConfig {
    private CompatibilityFactory compatibilityFactory;
    private String userAgent;
    private Map<String, String> userAgentConfig;
    private Map<String, AuthManager> authManagers;
    private HttpCallback httpCallback;
    private HttpClient httpClient;
    private HttpHeaders globalHeaders;
    private Function<String, String> baseUri;

    /**
     * private Constructor.
     */
    private CoreConfig(CompatibilityFactory compatibilityFactory, String userAgent,
            Map<String, String> userAgentConfig, Map<String, AuthManager> authManagers,
            HttpCallback httpCallback, HttpClient httpClient, HttpHeaders globalhHeaders,
            Function<String, String> baseUri) {
        this.compatibilityFactory = compatibilityFactory;
        this.userAgent = userAgent;
        this.userAgentConfig = userAgentConfig;
        this.authManagers = authManagers;
        this.httpCallback = httpCallback;
        this.httpClient = httpClient;
        this.globalHeaders = globalhHeaders;
        this.baseUri = baseUri;
        if (this.userAgent != null) {
            updateUserAgent();
        }
        if (this.userAgent != null && this.globalHeaders != null) {
            this.globalHeaders.add("user-agent", userAgent);
        }
    }

    /**
     * Getter for CompatibilityFactory
     * 
     * @return the compatibilityFactory instance
     */
    public CompatibilityFactory getCompatibilityFactory() {
        return compatibilityFactory;
    }

    /**
     * Getter for UserAgent
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
     * Getter for AuthManagers
     * 
     * @return the map of AuthManager
     */
    public Map<String, AuthManager> getAuthManagers() {
        return authManagers;
    }

    /**
     * Getter for HttpCallback
     * 
     * @return the httpCallback instance
     */
    public HttpCallback getHttpCallback() {
        return httpCallback;
    }

    /**
     * Getter for HttpClient
     * 
     * @return the httpClient instance
     */
    public HttpClient getHttpClient() {
        return httpClient;
    }

    /**
     * Getter for HttpHeaders
     * 
     * @return the HttpHeaders instance
     */
    public HttpHeaders getGlobalHeaders() {
        return globalHeaders;
    }

    /**
     * Getter for BaseUri
     * 
     * @return the baseUri function
     */
    public Function<String, String> getBaseUri() {
        return baseUri;
    }

    /**
     * Builds a new {@link CoreConfig.Builder} object. Creates the instance with the state of the
     * current state.
     * 
     * @return a new {@link CoreConfig.Builder} object
     */
    public Builder toBuilder() {
        Builder builder = new Builder().compatibilityFactor(compatibilityFactory)
                .userAgent(userAgent).authManager(authManagers).httpCallback(httpCallback)
                .httpClient(httpClient).globalHeaders(globalHeaders).baseUri(baseUri);
        return builder;

    }

    /**
     * Updates the user agent header value.
     */
    private void updateUserAgent() {
        String engineVersion = System.getProperty("java.runtime.version");
        String osName = System.getProperty("os.name") + "-" + System.getProperty("os.version");
        userAgent = userAgent.replace("{engine}", "JRE");
        userAgent =
                userAgent.replace("{engine-version}", engineVersion != null ? engineVersion : "");
        userAgent = userAgent.replace("{os-info}", osName != null ? osName : "");

        if (userAgentConfig != null) {
            userAgentConfig.forEach((key, value) -> {
                userAgent = userAgent.replace(key, value);
            });
        }
    }

    public static class Builder {
        private CompatibilityFactory compatibilityFactory;
        private String userAgent;
        private Map<String, String> userAgentConfig;
        private Map<String, AuthManager> authManagers;
        private HttpCallback httpCallback;
        private HttpClient httpClient;
        private HttpHeaders globalHeaders;
        private Function<String, String> baseUri;

        /**
         * Setter for compatibilityFactor.
         * 
         * @param compatibilityFactory value for CompatibilityFactor
         * @return Builder
         */
        public Builder compatibilityFactor(CompatibilityFactory compatibilityFactory) {
            this.compatibilityFactory = compatibilityFactory;
            return this;
        }

        /**
         * Setter for userAgent
         * 
         * @param userAgent String value for UserAgent
         * @return Builder
         */
        public Builder userAgent(String userAgent) {
            this.userAgent = userAgent;
            return this;
        }

        /**
         * Setter for userAgentConfig
         * 
         * @param userAgentConfig Map values for userAgentConfig
         * @return Builder
         */
        public Builder userAgentConfig(Map<String, String> userAgentConfig) {
            this.userAgentConfig = userAgentConfig;
            return this;
        }

        /**
         * Setter for authManagers
         * 
         * @param authManagers Map values for authManagers
         * @return Builder
         */
        public Builder authManager(Map<String, AuthManager> authManagers) {
            this.authManagers = authManagers;
            return this;
        }

        /**
         * Setter for HttpCallback
         * 
         * @param httpCallback value for HttpCallback
         * @return
         */
        public Builder httpCallback(HttpCallback httpCallback) {
            this.httpCallback = httpCallback;
            return this;
        }

        /**
         * Setter for HttpClient
         * 
         * @param httpClient value for
         * @return Builder
         */
        public Builder httpClient(HttpClient httpClient) {
            this.httpClient = httpClient;
            return this;
        }

        /**
         * Setter for globalHeaders
         * 
         * @param headers value for HttpHeaders
         * @return Builder
         */
        public Builder globalHeaders(HttpHeaders headers) {
            this.globalHeaders = headers;
            return this;
        }

        /**
         * Setter for baseUri
         * 
         * @param baseUri value for BaseUri
         * @return Builder
         */
        public Builder baseUri(Function<String, String> baseUri) {
            this.baseUri = baseUri;
            return this;
        }

        /**
         * Builds a new {@link CoreConfig} object using the set fields.
         * 
         * @return {@link CoreConfig}
         */
        public CoreConfig build() {
            return new CoreConfig(compatibilityFactory, userAgent, userAgentConfig, authManagers,
                    httpCallback, httpClient, globalHeaders, baseUri);
        }
    }

}
