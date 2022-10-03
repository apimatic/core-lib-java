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

    @Rule
    public MockitoRule initRule = MockitoJUnit.rule().silent();

    @Mock
    protected static GlobalConfiguration.Builder mockCoreConfigBuilder;

    @Mock
    protected static GlobalConfiguration mockGlobalConfig;

    @Before
    public void setup() throws IOException {
        prepareMockCoreConfigBuilder();
        prepareMockCoreConfig();
    }

    public void prepareMockCoreConfigBuilder() {
        when(mockCoreConfigBuilder.baseUri(test -> getBaseUri(test)))
                .thenReturn(mockCoreConfigBuilder);
        when(mockCoreConfigBuilder.compatibilityFactory(compatibilityFactory))
                .thenReturn(mockCoreConfigBuilder);
        when(mockCoreConfigBuilder.build()).thenReturn(mockGlobalConfig);
    }

    private void prepareMockCoreConfig() {
        //stubs
        when(mockGlobalConfig.getBaseUri()).thenReturn(test -> getBaseUri(test));
        when(mockGlobalConfig.getCompatibilityFactory()).thenReturn(compatibilityFactory);
        when(mockGlobalConfig.getUserAgent()).thenReturn("APIMATIC3.0");
    }

    protected static String getBaseUri(String test) {
        return "http://localhost:3000";
    }
}
