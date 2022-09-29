package apimatic.core_lib.utilities;

import org.junit.Rule;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import io.apimatic.coreinterfaces.http.HttpHeaders;

public class HttpHeadersMock {

    @Rule
    public MockitoRule initRule = MockitoJUnit.rule();

    @Mock
    protected HttpHeaders httpHeaders;
}
