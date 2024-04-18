package apimatic.core.configurations.http.client;

import static org.junit.Assert.assertEquals;
import java.util.TreeSet;
import org.junit.Test;
import org.slf4j.event.Level;

import io.apimatic.core.logger.configurations.ApiLoggingConfiguration;

public class ApiLoggingConfigurationTest {
    @Test
    public void testLogsExplicity() {
        TreeSet<String> headerFilters = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
        headerFilters.add("accept");
        ApiLoggingConfiguration apiLoggingConfiguration =
                new ApiLoggingConfiguration.Builder()
                        .level(Level.ERROR).build();
        assertEquals(Level.ERROR, apiLoggingConfiguration.getLevel());
    }
}
