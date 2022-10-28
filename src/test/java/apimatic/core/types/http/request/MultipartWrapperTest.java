package apimatic.core.types.http.request;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import io.apimatic.core.types.http.request.MultipartWrapper;
import io.apimatic.coreinterfaces.http.HttpHeaders;

public class MultipartWrapperTest {

    /**
     * Initializes mocks annotated with Mock.
     */
    @Rule
    public MockitoRule initRule = MockitoJUnit.rule().silent();

    /**
     * Mock of {@link HttpHeaders}
     */
    @Mock
    private HttpHeaders httpHeaders;

    @Test
    public void testMultiPartFileWrapper() {
        String serializedObj = "file data";
        MultipartWrapper fileWrapper = new MultipartWrapper(serializedObj, httpHeaders);
        assertTrue(fileWrapper.getByteArray().length > 0);
        assertEquals(fileWrapper.getHeaders(), httpHeaders);
    }
}
