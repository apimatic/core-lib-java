package io.apimatic.core.configurations.http.client;

import io.apimatic.coreinterfaces.http.proxy.ProxyConfiguration;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import io.apimatic.coreinterfaces.http.ClientConfiguration;
import io.apimatic.coreinterfaces.http.Method;

/**
 * Class to hold HTTP Client Configuration.
 */
public final class CoreHttpClientConfiguration implements ClientConfiguration {

    /**
     * Request timeout status code.
     */
    private static final int REQUEST_TIMEOUT = 408;

    /**
     * Long data status code.
     */
    private static final int LONG_DATA = 413;

    /**
     * Too many request status code.
     */
    private static final int TOO_MANY_REQUEST = 429;

    /**
     * Internal server error status code.
     */
    private static final int INTERNAL_SERVER_ERROR = 500;

    /**
     * Bad Gateway status code.
     */
    private static final int BAD_GATEWAY = 502;

    /**
     * Service unavailable status code.
     */
    private static final int SERVICE_UNAVAILABLE = 503;

    /**
     * Gateway timeout status code.
     */
    private static final int GATEWAY_TIMEOUT = 504;

    /**
     * Web server is down code.
     */
    private static final int WEB_SERVER_IS_DOWN = 521;

    /**
     * Connection timeout.
     */
    private static final int CONNECTION_TIMEOUT = 522;

    /**
     * Server timeout.
     */
    private static final int SERVER_TIMEOUT = 524;

    /**
     * Maximum retry wait time.
     */
    private static final int MAX_WAIT_TIME = 120;

    /**
     * The timeout in seconds to use for making HTTP requests.
     */
    private final long timeout;

    /**
     * The number of retries to make.
     */
    private final int numberOfRetries;

    /**
     * To use in calculation of wait time for next request in case of failure.
     */
    private final int backOffFactor;

    /**
     * To use in calculation of wait time for next request in case of failure.
     */
    private final long retryInterval;

    /**
     * Http status codes to retry against.
     */
    private final Set<Integer> httpStatusCodesToRetry;

    /**
     * Http methods to retry against.
     */
    private final Set<Method> httpMethodsToRetry;

    /**
     * The maximum wait time for overall retrying requests.
     */
    private final long maximumRetryWaitTime;

    /**
     * Whether to retry on request timeout.
     */
    private final boolean shouldRetryOnTimeout;

    /**
     * The OkHttpClient instance used to make the HTTP calls.
     */
    private final okhttp3.OkHttpClient httpClientInstance;

    /**
     * Allow the SDK to override HTTP client instance's settings used for features like retries,
     * timeouts etc.
     */
    private final boolean overrideHttpClientConfigurations;

    /**
     * Allow or prevent skipping SSL certificate verification.
     */
    private final boolean skipSslCertVerification;

    /**
     * The proxy configuration used to route network requests through a proxy server.
     * Contains details such as address, port, and optional authentication credentials.
     */
    private final ProxyConfiguration proxyConfiguration;

    /**
     * @param timeout
     * @param numberOfRetries
     * @param backOffFactor
     * @param retryInterval
     * @param skipSslCertVerification
     * @param httpStatusCodesToRetry
     * @param httpMethodsToRetry
     * @param maximumRetryWaitTime
     * @param shouldRetryOnTimeout
     * @param httpClientInstance
     * @param overrideHttpClientConfigurations
     * @param proxyConfiguration
     */
    private CoreHttpClientConfiguration(final long timeout, final int numberOfRetries,
            final int backOffFactor, final long retryInterval,
            final boolean skipSslCertVerification, final Set<Integer> httpStatusCodesToRetry,
            final Set<Method> httpMethodsToRetry, final long maximumRetryWaitTime,
            final boolean shouldRetryOnTimeout, final okhttp3.OkHttpClient httpClientInstance,
            final boolean overrideHttpClientConfigurations,
            final ProxyConfiguration proxyConfiguration) {
        this.timeout = timeout;
        this.numberOfRetries = numberOfRetries;
        this.backOffFactor = backOffFactor;
        this.retryInterval = retryInterval;
        this.httpStatusCodesToRetry = httpStatusCodesToRetry;
        this.httpMethodsToRetry = httpMethodsToRetry;
        this.maximumRetryWaitTime = maximumRetryWaitTime;
        this.shouldRetryOnTimeout = shouldRetryOnTimeout;
        this.httpClientInstance = httpClientInstance;
        this.overrideHttpClientConfigurations = overrideHttpClientConfigurations;
        this.skipSslCertVerification = skipSslCertVerification;
        this.proxyConfiguration = proxyConfiguration;
    }

    /**
     * The timeout in seconds to use for making HTTP requests.
     * @return timeout.
     */
    public long getTimeout() {
        return timeout;
    }

    /**
     * The number of retries to make.
     * @return numberOfRetries.
     */
    public int getNumberOfRetries() {
        return numberOfRetries;
    }

    /**
     * To use in calculation of wait time for next request in case of failure.
     * @return backOffFactor.
     */
    public int getBackOffFactor() {
        return backOffFactor;
    }

    /**
     * To use in calculation of wait time for next request in case of failure.
     * @return retryInterval.
     */
    public long getRetryInterval() {
        return retryInterval;
    }

    /**
     * Http status codes to retry against.
     * @return httpStatusCodesToRetry.
     */
    public Set<Integer> getHttpStatusCodesToRetry() {
        return httpStatusCodesToRetry;
    }

    /**
     * Http methods to retry against.
     * @return httpMethodsToRetry.
     */
    public Set<Method> getHttpMethodsToRetry() {
        return httpMethodsToRetry;
    }

    /**
     * The maximum wait time for overall retrying requests.
     * @return maximumRetryWaitTime.
     */
    public long getMaximumRetryWaitTime() {
        return maximumRetryWaitTime;
    }

    /**
     * Whether to retry on request timeout.
     * @return shouldRetryOnTimeout.
     */
    public boolean shouldRetryOnTimeout() {
        return shouldRetryOnTimeout;
    }

    /**
     * The OkHttpClient instance used to make the HTTP calls.
     * @return httpClientInstance.
     */
    public okhttp3.OkHttpClient getHttpClientInstance() {
        return httpClientInstance;
    }

    /**
     * Allow the SDK to override HTTP client instance's settings used for features like retries,
     * timeouts etc.
     * @return overrideHttpClientConfigurations.
     */
    public boolean shouldOverrideHttpClientConfigurations() {
        return overrideHttpClientConfigurations;
    }

    /**
     * Allow or prevent skipping SSL certificate verification.
     * @return skipSslCertVerification.
     */
    public boolean skipSslCertVerification() {
        return skipSslCertVerification;
    }

    /**
     * Returns the proxy configuration used to route requests through a proxy server.
     * This includes the proxy address, port, and any authentication credentials.
     * @return the {@link ProxyConfiguration}
     */
    public ProxyConfiguration getProxyConfiguration() { return proxyConfiguration; }

    /**
     * Converts this HttpClientConfiguration into string format.
     * @return String representation of this class.
     */
    @Override
    public String toString() {
        return "HttpClientConfiguration [" + "timeout=" + timeout + ", numberOfRetries="
                + numberOfRetries + ", backOffFactor=" + backOffFactor + ", retryInterval="
                + retryInterval + ", httpStatusCodesToRetry=" + httpStatusCodesToRetry
                + ", httpMethodsToRetry=" + httpMethodsToRetry + ", maximumRetryWaitTime="
                + maximumRetryWaitTime + ", shouldRetryOnTimeout=" + shouldRetryOnTimeout
                + ", httpClientInstance=" + httpClientInstance
                + ", overrideHttpClientConfigurations=" + overrideHttpClientConfigurations
                + ", proxy=" + proxyConfiguration + "]";
    }

    /**
     * Builds a new {@link CoreHttpClientConfiguration.Builder} object. Creates the instance with
     * the current state.
     * @return a new {@link CoreHttpClientConfiguration.Builder} object.
     */
    public Builder newBuilder() {
        return new Builder().timeout(timeout).numberOfRetries(numberOfRetries)
                .backOffFactor(backOffFactor).retryInterval(retryInterval)
                .httpStatusCodesToRetry(httpStatusCodesToRetry)
                .httpMethodsToRetry(httpMethodsToRetry).maximumRetryWaitTime(maximumRetryWaitTime)
                .shouldRetryOnTimeout(shouldRetryOnTimeout)
                .httpClientInstance(httpClientInstance, overrideHttpClientConfigurations)
                .proxyConfiguration(proxyConfiguration);
    }

    /**
     * Class to build instances of {@link CoreHttpClientConfiguration}.
     */
    public static class Builder {

        /**
         * Timeout.
         */
        private long timeout = 0;

        /**
         * Number of retries.
         */
        private int numberOfRetries = 0;
        /**
         * Back off factor.
         */
        private int backOffFactor = 2;
        /**
         * Retry interval.
         */
        private long retryInterval = 1;
        /**
         * Set of Status codes to retry.
         */
        private Set<Integer> httpStatusCodesToRetry = new HashSet<>();
        /**
         * Set of http methods.
         */
        private Set<Method> httpMethodsToRetry = new HashSet<>();
        /**
         * Maximum retry wait time.
         */
        private long maximumRetryWaitTime = MAX_WAIT_TIME;
        /**
         * Should Retry on timeout.
         */
        private boolean shouldRetryOnTimeout = true;
        /**
         * An instance of OkHttpClient.
         */
        private okhttp3.OkHttpClient httpClientInstance;
        /**
         * Do need to Override Http client configuration.
         */
        private boolean overrideHttpClientConfigurations = true;
        /**
         * Skip Ssl certification.
         */
        private boolean skipSslCertVerification;
        /**
         * The proxy configuration used to route network requests through a proxy server.
         * Contains details such as address, port, and optional authentication credentials.
         */
        private ProxyConfiguration proxyConfiguration;

        /**
         * Default Constructor to initiate builder with default properties.
         */
        public Builder() {
            // setting default values
            httpStatusCodesToRetry.addAll(Arrays.asList(REQUEST_TIMEOUT, LONG_DATA,
                    TOO_MANY_REQUEST, INTERNAL_SERVER_ERROR, BAD_GATEWAY, SERVICE_UNAVAILABLE,
                    GATEWAY_TIMEOUT, WEB_SERVER_IS_DOWN, CONNECTION_TIMEOUT, SERVER_TIMEOUT));
            httpMethodsToRetry.addAll(Arrays.asList(Method.GET, Method.PUT));
        }

        /**
         * The timeout in seconds to use for making HTTP requests.
         * @param timeout The timeout to set.
         * @return Builder.
         */
        public Builder timeout(long timeout) {
            if (timeout > 0) {
                this.timeout = timeout;
            }
            return this;
        }

        /**
         * The number of retries to make.
         * @param numberOfRetries The numberOfRetries to set.
         * @return Builder.
         */
        public Builder numberOfRetries(int numberOfRetries) {
            if (numberOfRetries >= 0) {
                this.numberOfRetries = numberOfRetries;
            }
            return this;
        }

        /**
         * To use in calculation of wait time for next request in case of failure.
         * @param backOffFactor The backOffFactor to set.
         * @return Builder.
         */
        public Builder backOffFactor(int backOffFactor) {
            if (backOffFactor >= 1) {
                this.backOffFactor = backOffFactor;
            }
            return this;
        }

        /**
         * To use in calculation of wait time for next request in case of failure.
         * @param retryInterval The retryInterval to set.
         * @return Builder.
         */
        public Builder retryInterval(long retryInterval) {
            if (retryInterval >= 0) {
                this.retryInterval = retryInterval;
            }
            return this;
        }

        /**
         * Http status codes to retry against.
         * @param httpStatusCodesToRetry The httpStatusCodesToRetry to set.
         * @return Builder.
         */
        public Builder httpStatusCodesToRetry(Set<Integer> httpStatusCodesToRetry) {
            this.httpStatusCodesToRetry.clear();
            if (httpStatusCodesToRetry != null) {
                this.httpStatusCodesToRetry.addAll(httpStatusCodesToRetry);
            }
            return this;
        }

        /**
         * Http methods to retry against.
         * @param httpMethodsToRetry The httpMethodsToRetry to set.
         * @return Builder.
         */
        public Builder httpMethodsToRetry(Set<Method> httpMethodsToRetry) {
            this.httpMethodsToRetry.clear();
            if (httpMethodsToRetry != null) {
                this.httpMethodsToRetry.addAll(httpMethodsToRetry);
            }
            return this;
        }

        /**
         * The maximum wait time for overall retrying requests.
         * @param maximumRetryWaitTime The maximumRetryWaitTime to set.
         * @return Builder.
         */
        public Builder maximumRetryWaitTime(long maximumRetryWaitTime) {
            if (maximumRetryWaitTime > 0) {
                this.maximumRetryWaitTime = maximumRetryWaitTime;
            }
            return this;
        }

        /**
         * Whether to retry on request timeout.
         * @param shouldRetryOnTimeout The shouldRetryOnTimeout to set
         * @return Builder.
         */
        public Builder shouldRetryOnTimeout(boolean shouldRetryOnTimeout) {
            this.shouldRetryOnTimeout = shouldRetryOnTimeout;
            return this;
        }

        /**
         * The OkHttpClient instance used to make the HTTP calls.
         * @param httpClientInstance The httpClientInstance to set
         * @return Builder.
         */
        public Builder httpClientInstance(okhttp3.OkHttpClient httpClientInstance) {
            this.httpClientInstance = httpClientInstance;
            return this;
        }

        /**
         * The OkHttpClient instance used to make the HTTP calls.
         * @param httpClientInstance The httpClientInstance to set.
         * @param overrideHttpClientConfigurations The overrideHttpClientConfigurations to set.
         * @return Builder.
         */
        public Builder httpClientInstance(
                okhttp3.OkHttpClient httpClientInstance, boolean overrideHttpClientConfigurations) {
            this.httpClientInstance = httpClientInstance;
            this.overrideHttpClientConfigurations = overrideHttpClientConfigurations;
            return this;
        }

        /**
         * Whether to prevent SSL cert verification or not.
         * @param skipSslCertVerification The skipSslCertVerification to set.
         * @return Builder.
         */
        public Builder skipSslCertVerification(boolean skipSslCertVerification) {
            this.skipSslCertVerification = skipSslCertVerification;
            return this;
        }

        /**
         * Sets the proxy configuration to be used for routing requests through a proxy server.
         *
         * @param proxyConfiguration the {@link ProxyConfiguration} instance to use
         * @return the builder instance
         */
        public Builder proxyConfiguration(ProxyConfiguration proxyConfiguration) {
            this.proxyConfiguration = proxyConfiguration;
            return this;
        }

        /**
         * Builds a new HttpClientConfiguration object using the set fields.
         * @return {@link CoreHttpClientConfiguration}.
         */
        public CoreHttpClientConfiguration build() {
            return new CoreHttpClientConfiguration(timeout, numberOfRetries, backOffFactor,
                    retryInterval, skipSslCertVerification, httpStatusCodesToRetry,
                    httpMethodsToRetry, maximumRetryWaitTime, shouldRetryOnTimeout,
                    httpClientInstance, overrideHttpClientConfigurations, proxyConfiguration);
        }
    }
}
