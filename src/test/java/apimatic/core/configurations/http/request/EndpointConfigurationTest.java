package apimatic.core.configurations.http.request;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import io.apimatic.core.configurations.http.request.EndpointConfiguration;
import io.apimatic.coreinterfaces.http.request.ArraySerializationFormat;
import io.apimatic.coreinterfaces.http.request.configuration.RetryOption;

public class EndpointConfigurationTest {

    @Test
    public void testEndpointConfiguration() {
        EndpointConfiguration configuration =
                new EndpointConfiguration(false, RetryOption.DEFAULT,
                        ArraySerializationFormat.INDEXED);
        assertEquals(configuration.getArraySerializationFormat(), ArraySerializationFormat.INDEXED);
        assertEquals(configuration.getRetryOption(), RetryOption.DEFAULT);
        assertEquals(configuration.hasBinaryResponse(), false);
    }
}
