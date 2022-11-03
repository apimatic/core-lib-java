package apimatic.core.utilities;

import org.junit.Rule;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import io.apimatic.coreinterfaces.http.request.Request;

public class CoreHttpRequestMock extends HttpHeadersMock {

    /**
     * Initializes mocks annotated with Mock.
     */
    @Rule
    public MockitoRule initRule = MockitoJUnit.rule().silent();

    /**
     * Mock of {@link Request}.
     */
    @Mock
    private Request coreHttpRequest;

    /**
     * @return {@link Request}.
     */
    public Request getCoreHttpRequest() {
        return coreHttpRequest;
    }
}
