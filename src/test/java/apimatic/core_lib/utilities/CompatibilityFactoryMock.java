package apimatic.core_lib.utilities;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.when;
import java.io.IOException;
import org.junit.Before;
import org.junit.Rule;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import io.apimatic.core_interfaces.compatibility.CompatibilityFactory;
import io.apimatic.core_interfaces.http.CoreHttpMethod;
import io.apimatic.core_interfaces.http.HttpHeaders;

public class CompatibilityFactoryMock extends CoreHttpRequestMock {

    @Rule
    public MockitoRule initRule = MockitoJUnit.rule();

    @Mock
    public CompatibilityFactory compatibilityFactory;



    @Before
    public void setup() throws IOException {
        MockitoAnnotations.openMocks(this);
        prepareExpecations();
    }

    public void prepareExpecations() {
        when(compatibilityFactory.createHttpHeaders(anyMap())).thenReturn(httpHeaders);
        when(compatibilityFactory.createHttpRequest(
                any(CoreHttpMethod.class), any(StringBuilder.class), any(HttpHeaders.class),
                anyMap(), any(Object.class))).thenReturn(coreHttpRequest);
    }
}
