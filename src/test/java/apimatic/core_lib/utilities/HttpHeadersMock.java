package apimatic.core_lib.utilities;

import java.io.IOException;
import org.junit.Before;
import org.junit.Rule;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import io.apimatic.core_interfaces.http.HttpHeaders;

public class HttpHeadersMock {

    @Rule
    public MockitoRule initRule = MockitoJUnit.rule();
    
    @Mock
    protected HttpHeaders httpHeaders;

    @Before
    public void setup() throws IOException {
        MockitoAnnotations.openMocks(this);
        prepareExpecations();
    }

    public void prepareExpecations() {
      //doReturn("application/json").when(httpHeaders).value("accept");
    }
}
