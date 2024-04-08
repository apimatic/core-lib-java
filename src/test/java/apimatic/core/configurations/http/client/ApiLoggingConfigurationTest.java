package apimatic.core.configurations.http.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.util.TreeSet;
import org.junit.Test;
import io.apimatic.core.configurations.http.client.ApiLoggingConfiguration;
import io.apimatic.coreinterfaces.http.LoggingLevel;
import io.apimatic.coreinterfaces.http.LoggingPolicy;

public class ApiLoggingConfigurationTest {
    @Test
    public void testLogsExplicity() {
        TreeSet<String> headerFilters = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
        headerFilters.add("accept");
        ApiLoggingConfiguration apiLoggingConfiguration =
                new ApiLoggingConfiguration.Builder()
                        .level(LoggingLevel.ERROR).build();
        assertEquals(apiLoggingConfiguration.getLevel(), LoggingLevel.ERROR);
    }
}
