package apimatic.core.utilities;

import static org.mockito.Mockito.when;
import java.io.IOException;
import org.junit.Before;
import org.junit.Rule;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import io.apimatic.core.GlobalConfiguration;

public class MockCoreConfig extends CompatibilityFactoryMock {

    /**
     * Initializes mocks annotated with Mock.
     */
    @Rule
    public MockitoRule initRule = MockitoJUnit.rule().silent();

    /**
     * Mock of {@link GlobalConfiguration.Builder}
     */
    @Mock
    private static GlobalConfiguration.Builder mockCoreConfigBuilder;

    /**
     * Mock of {@link GlobalConfiguration}
     */
    @Mock
    private static GlobalConfiguration mockGlobalConfig;

    /**
     * Setup the test setup.
     * @throws IOException Signals that an I/O exception of some sort has occurred.
     */
    @Before
    public void setup() throws IOException {
        prepareMockCoreConfigBuilder();
        prepareMockCoreConfig();
    }

    /**
     * @return {@link GlobalConfiguration}
     */
    public static GlobalConfiguration getMockGlobalConfig() {
        return mockGlobalConfig;
    }

    private void prepareMockCoreConfigBuilder() {
        when(mockCoreConfigBuilder.baseUri(test -> getBaseUri(test)))
                .thenReturn(mockCoreConfigBuilder);
        when(mockCoreConfigBuilder.compatibilityFactory(getCompatibilityFactory()))
                .thenReturn(mockCoreConfigBuilder);
        when(mockCoreConfigBuilder.build()).thenReturn(mockGlobalConfig);
    }

    private void prepareMockCoreConfig() {
        // stubs
        when(mockGlobalConfig.getBaseUri()).thenReturn(test -> getBaseUri(test));
        when(mockGlobalConfig.getCompatibilityFactory()).thenReturn(getCompatibilityFactory());
    }

    protected static String getBaseUri(String test) {
        return "http://localhost:3000";
    }
}
