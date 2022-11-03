package apimatic.core.types.http.request;

import static org.junit.Assert.assertEquals;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import io.apimatic.core.types.http.request.MultipartFileWrapper;
import io.apimatic.coreinterfaces.http.HttpHeaders;
import io.apimatic.coreinterfaces.type.CoreFileWrapper;

public class MultipartFileWrapperTest {

    /**
     * Initializes mocks annotated with Mock.
     */
    @Rule
    public MockitoRule initRule = MockitoJUnit.rule().silent();

    /**
     * Mock of {@link CoreFileWrapper}.
     */
    @Mock
    private CoreFileWrapper coreFileWrapper;

    /**
     * Mock of {@link HttpHeaders}.
     */
    @Mock
    private HttpHeaders httpHeaders;

    @Test
    public void testMultiPartFileWrapper() {
        MultipartFileWrapper fileWrapper = new MultipartFileWrapper(coreFileWrapper, httpHeaders);
        assertEquals(fileWrapper.getFileWrapper(), coreFileWrapper);
        assertEquals(fileWrapper.getHeaders(), httpHeaders);
    }
}
