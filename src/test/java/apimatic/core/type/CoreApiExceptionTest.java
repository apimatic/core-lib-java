package apimatic.core.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import io.apimatic.core.types.CoreApiException;
import io.apimatic.coreinterfaces.http.Context;
import io.apimatic.coreinterfaces.http.response.Response;

public class CoreApiExceptionTest {


    @Rule
    public MockitoRule initRule = MockitoJUnit.rule();

    @Mock
    private Context context;

    @Mock
    private Response response;

    @Test
    public void testCoreApiException() {
        CoreApiException apiException = new CoreApiException("Http Response Not OK");
        assertEquals(apiException.getMessage(), "Http Response Not OK");
    }

    @Test
    public void testCoreApiExceptionContext() {
        String excpetionMessage = "Http Response Not OK";

        InputStream inputStream = new ByteArrayInputStream(
                new CoreApiException(excpetionMessage).toString().getBytes());

        when(context.getResponse()).thenReturn(response);
        when(response.getRawBody()).thenReturn(inputStream);

        CoreApiException apiException = new CoreApiException(excpetionMessage, context);
        assertEquals(apiException.getHttpContext().getResponse().getRawBody(), inputStream);
    }

    @Test
    public void testCoreApiExceptionWithNullContext() {
        String excpetionMessage = "Http Response Not OK";

        CoreApiException apiException = new CoreApiException(excpetionMessage, null);
        assertNull(apiException.getHttpContext());
        assertEquals(apiException.getResponseCode(), -1);
    }

    @Test
    public void testCoreApiExceptionWithNullResponse() {
        String excpetionMessage = "Http Response Not OK";

        CoreApiException apiException = new CoreApiException(excpetionMessage, context);
        assertEquals(apiException.getResponseCode(), -1);
    }
}
