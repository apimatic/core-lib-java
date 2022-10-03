package apimatic.core_lib.utilities;

import org.junit.Rule;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import io.apimatic.coreinterfaces.http.request.Request;

public class CoreHttpRequestMock extends HttpHeadersMock {

    @Rule
    public MockitoRule initRule = MockitoJUnit.rule().silent();
    @Mock
    public Request coreHttpRequest;

}
