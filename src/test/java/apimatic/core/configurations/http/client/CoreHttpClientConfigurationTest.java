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

    @Test
    public void testHttpClientRetriesConfiguration() {
        Set<Integer> statusCodeToRetry = new HashSet<Integer>();
        statusCodeToRetry.add(404);
        Set<Method> httpMethodsToRetry = new HashSet<Method>();
        httpMethodsToRetry.add(Method.GET);
        CoreHttpClientConfiguration clientConfiguration = new CoreHttpClientConfiguration.Builder()
                .timeout(5).backOffFactor(3).maximumRetryWaitTime(30).numberOfRetries(3)
                .shouldRetryOnTimeout(false).httpMethodsToRetry(httpMethodsToRetry).retryInterval(5)
                .httpStatusCodesToRetry(statusCodeToRetry).build();
        assertEquals(clientConfiguration.getBackOffFactor(), 3);
        assertEquals(clientConfiguration.getNumberOfRetries(), 3);
        assertEquals(clientConfiguration.getTimeout(), 5);
        assertEquals(clientConfiguration.getRetryInterval(), 5);
        assertEquals(clientConfiguration.getHttpMethodsToRetry(), httpMethodsToRetry);
        assertEquals(clientConfiguration.getHttpStatusCodesToRetry(), statusCodeToRetry);
        assertEquals(clientConfiguration.getMaximumRetryWaitTime(), 30);
        assertFalse(clientConfiguration.shouldRetryOnTimeout());
    }

    @Test
    public void testHttpClientNegativeConfiguration() {
        CoreHttpClientConfiguration clientConfiguration =
                new CoreHttpClientConfiguration.Builder().timeout(0).backOffFactor(0)
                        .httpMethodsToRetry(null).httpStatusCodesToRetry(null)
                        .maximumRetryWaitTime(0).numberOfRetries(-1).retryInterval(-1).build();
        assertEquals(clientConfiguration.getBackOffFactor(), 2);
        assertEquals(clientConfiguration.getNumberOfRetries(), 0);
        assertEquals(clientConfiguration.getTimeout(), 0);
        assertEquals(clientConfiguration.getRetryInterval(), 1);
        assertEquals(clientConfiguration.getMaximumRetryWaitTime(), 120);
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
        CoreHttpClientConfiguration clientConfiguration = new CoreHttpClientConfiguration.Builder()
                .httpClientInstance(client, true).skipSslCertVerification(true).build();

        assertEquals(clientConfiguration.getHttpClientInstance(), client);
        assertTrue(clientConfiguration.shouldOverrideHttpClientConfigurations());
        assertTrue(clientConfiguration.skipSslCertVerification());
    }
}
