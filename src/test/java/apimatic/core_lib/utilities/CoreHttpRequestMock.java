package apimatic.core_lib.utilities;

import java.io.IOException;
import java.util.function.Consumer;
import org.junit.Before;
import org.junit.Rule;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import io.apimatic.core_interfaces.http.request.CoreHttpRequest;
import io.apimatic.core_lib.CoreRequest;

public class CoreHttpRequestMock extends HttpHeadersMock {

    @Rule
    public MockitoRule initRule = MockitoJUnit.rule();

    @Mock
    public CoreHttpRequest coreHttpRequest;

    @Mock
    public Consumer<CoreRequest.Builder> coreRequest;

    @Before
    public void setup() throws IOException {
        MockitoAnnotations.openMocks(this);
    }

}
