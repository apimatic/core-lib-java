package apimatic.core.mocks;

import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Rule;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import io.apimatic.coreinterfaces.http.HttpHeaders;

public class HttpHeadersMock {

    /**
     * Initializes mocks annotated with Mock.
     */
    @Rule
    public MockitoRule initRule = MockitoJUnit.rule().silent();

    /**
     * Mock of {@link HttpHeaders}.
     */
    @Mock
    private HttpHeaders httpHeaders;

    /**
     * Setup the httpHeaders mock instance.
     *
     * @throws IOException
     */
    @Before
    public void setup() throws IOException {
        when(httpHeaders.asSimpleMap()).thenReturn(new HashMap<String, String>());
    }

    /**
     * @return {@link HttpHeaders}.
     */
    public HttpHeaders getHttpHeaders() {
        return httpHeaders;
    }
}
