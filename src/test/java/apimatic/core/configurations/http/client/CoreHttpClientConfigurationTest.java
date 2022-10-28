package apimatic.core.configurations.http.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.util.HashSet;
import java.util.Set;
import org.junit.Test;
import io.apimatic.core.configurations.http.client.CoreHttpClientConfiguration;
import io.apimatic.coreinterfaces.http.Method;
import okhttp3.OkHttpClient;

public class CoreHttpClientConfigurationTest {

    /**
     * Not found status code
     */
    private static final int NOT_FOUND_STATUS_CODE = 404;

    /**
     * Back off factor
     */
    private static final int BACK_OFF_FACTOR = 3;

    /**
     * Default back off factor
     */
    private static final int DEFAULT_BACK_OFF_FACTOR = 2;

    /**
     * Number of retries
     */
    private static final int NO_OF_RETRIES = 3;

    /**
     * Timeout
     */
    private static final int TIMEOUT = 3;

    /**
     * Maximum retry wait time
     */
    private static final int MAX_RETRY_WAIT_TIME = 30;

    /**
     * Default maximum retry wait time
     */
    private static final int DEFAULT_MAX_RETRY_WAIT_TIME = 120;

    /**
     * Retry Interval
     */
    private static final int RETRY_INTERVAL = 5;



    @Test
    public void testHttpClientRetriesConfiguration() {
        Set<Integer> statusCodeToRetry = new HashSet<Integer>();
        statusCodeToRetry.add(NOT_FOUND_STATUS_CODE);
        Set<Method> httpMethodsToRetry = new HashSet<Method>();
        httpMethodsToRetry.add(Method.GET);
        CoreHttpClientConfiguration clientConfiguration =
                new CoreHttpClientConfiguration.Builder().timeout(TIMEOUT)
                        .backOffFactor(BACK_OFF_FACTOR).maximumRetryWaitTime(MAX_RETRY_WAIT_TIME)
                        .numberOfRetries(NO_OF_RETRIES).shouldRetryOnTimeout(false)
                        .httpMethodsToRetry(httpMethodsToRetry).retryInterval(RETRY_INTERVAL)
                        .httpStatusCodesToRetry(statusCodeToRetry).build();
        assertEquals(clientConfiguration.getBackOffFactor(), BACK_OFF_FACTOR);
        assertEquals(clientConfiguration.getNumberOfRetries(), NO_OF_RETRIES);
        assertEquals(clientConfiguration.getTimeout(), TIMEOUT);
        assertEquals(clientConfiguration.getRetryInterval(), RETRY_INTERVAL);
        assertEquals(clientConfiguration.getHttpMethodsToRetry(), httpMethodsToRetry);
        assertEquals(clientConfiguration.getHttpStatusCodesToRetry(), statusCodeToRetry);
        assertEquals(clientConfiguration.getMaximumRetryWaitTime(), MAX_RETRY_WAIT_TIME);
        assertFalse(clientConfiguration.shouldRetryOnTimeout());
    }

    @Test
    public void testHttpClientNegativeConfiguration() {
        CoreHttpClientConfiguration clientConfiguration =
                new CoreHttpClientConfiguration.Builder().timeout(0).backOffFactor(0)
                        .httpMethodsToRetry(null).httpStatusCodesToRetry(null)
                        .maximumRetryWaitTime(0).numberOfRetries(-1).retryInterval(-1).build();
        assertEquals(clientConfiguration.getBackOffFactor(), DEFAULT_BACK_OFF_FACTOR);
        assertEquals(clientConfiguration.getNumberOfRetries(), 0);
        assertEquals(clientConfiguration.getTimeout(), 0);
        assertEquals(clientConfiguration.getRetryInterval(), 1);
        assertEquals(clientConfiguration.getMaximumRetryWaitTime(), DEFAULT_MAX_RETRY_WAIT_TIME);
    }


    @Test
    public void testHttpClientInstanceConfiguration() {
        OkHttpClient client = new OkHttpClient();
        CoreHttpClientConfiguration clientConfiguration =
                new CoreHttpClientConfiguration.Builder().httpClientInstance(client).build();

        assertEquals(clientConfiguration.getHttpClientInstance(), client);
    }

    @Test
    public void testHttpClientInstanceOverrideConfiguration() {
        OkHttpClient client = new OkHttpClient();
        CoreHttpClientConfiguration clientConfiguration =
                new CoreHttpClientConfiguration.Builder().httpClientInstance(client, true)
                        .skipSslCertVerification(true).build();

        assertEquals(clientConfiguration.getHttpClientInstance(), client);
        assertTrue(clientConfiguration.shouldOverrideHttpClientConfigurations());
        assertTrue(clientConfiguration.skipSslCertVerification());
    }
}
