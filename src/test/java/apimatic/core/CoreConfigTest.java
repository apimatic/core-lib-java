package apimatic.core;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import java.io.IOException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import apimatic.core_lib.utilities.MockCoreConfig;
import io.apimatic.core.ApiCall;

public class CoreConfigTest extends MockCoreConfig {

    @Rule
    public MockitoRule initRule = MockitoJUnit.rule();

    @Mock
    private ApiCall.Builder<?, ?> mockApiCallBuilder;

    @Mock
    private ApiCall<?, ?> mockApiCall;


    @Before
    public void setup() throws IOException {
        MockitoAnnotations.openMocks(this);
        setConditions();
    }


    @Test
    public void testCoreConfig() throws IOException {
        ApiCall<?, ?> apiCall = mockApiCallBuilder.globalConfig(mockGlobalConfig).build();
        assertEquals(apiCall.getGlobalConfig(), mockGlobalConfig);
    }



    // @Test
    // public void testCoreConfigClient() throws IOException {
    // when(mockApiCall.getCoreConfig().getHttpClient()).thenReturn(client);
    // ApiCall<?, ?> apiCall = mockApiCallBuilder.coreConfig(mockCoreConfig).build();
    // assertEquals(apiCall.getCoreConfig().getHttpClient(), client);
    // }

    @Test
    public void testCoreConfigCompatibility() throws IOException {
        when(mockApiCall.getGlobalConfig().getCompatibilityFactory())
                .thenReturn(compatibilityFactory);
        ApiCall<?, ?> apiCall = mockApiCallBuilder.globalConfig(mockGlobalConfig).build();
        assertEquals(apiCall.getGlobalConfig().getCompatibilityFactory(), compatibilityFactory);
    }

    // @Test
    // public void testCoreConfigCallBack() throws IOException {
    // when(mockApiCall.getCoreConfig().getHttpCallback()).thenReturn(httpCallback);
    // ApiCall<?, ?> apiCall = mockApiCallBuilder.coreConfig(mockCoreConfig).build();
    // assertEquals(apiCall.getCoreConfig().getHttpCallback(), httpCallback);
    // }

    @Test
    public void testCoreConfigUserAgent() throws IOException {
        when(mockApiCall.getGlobalConfig().getUserAgent()).thenReturn("APIMATIC3.0");
        ApiCall<?, ?> apiCall = mockApiCallBuilder.globalConfig(mockGlobalConfig).build();
        assertEquals(apiCall.getGlobalConfig().getUserAgent(), "APIMATIC3.0");
    }

    // @Test
    // public void testCoreConfigAuthManagers() throws IOException {
    // when(mockApiCall.getCoreConfig().getAuthManagers())
    // .thenReturn(Collections.singletonMap("global", authentication));
    // ApiCall<?, ?> apiCall = mockApiCallBuilder.coreConfig(mockCoreConfig).build();
    // assertEquals(apiCall.getCoreConfig().getAuthManagers(),
    // Collections.singletonMap("global", authentication));
    // }

    private void setConditions() throws IOException {
        doReturn(mockApiCallBuilder).when(mockApiCallBuilder).globalConfig(mockGlobalConfig);
        doReturn(mockApiCall).when(mockApiCallBuilder).build();
        doReturn(mockGlobalConfig).when(mockApiCall).getGlobalConfig();
    }

}
