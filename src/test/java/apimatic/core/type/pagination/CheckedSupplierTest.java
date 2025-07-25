package apimatic.core.type.pagination;

import org.junit.Test;
import static org.junit.Assert.assertNull;

import java.io.IOException;

import io.apimatic.core.types.CoreApiException;
import io.apimatic.core.types.pagination.CheckedSupplier;

public class CheckedSupplierTest {

    @Test(expected = IOException.class)
    public void testCreateErrorWithIOException() throws Exception {
        IOException ioException = new IOException("Test IO Exception");
        CheckedSupplier<String, CoreApiException> supplier =
                CheckedSupplier.createError(ioException);
        @SuppressWarnings("unused")
        String value = supplier.get();
    }

    @Test(expected = CoreApiException.class)
    public void testCreateErrorWithCoreApiException() throws Exception {
        CoreApiException apiException = new CoreApiException("Test API Exception");
        CheckedSupplier<String, CoreApiException> supplier =
                CheckedSupplier.createError(apiException);
        @SuppressWarnings("unused")
        String value = supplier.get();
    }

    @Test
    public void testCreateErrorWithUnsupportedException() {
        RuntimeException runtimeException = new RuntimeException("Test Exception");
        CheckedSupplier<String, CoreApiException> supplier =
                CheckedSupplier.createError(runtimeException);
        assertNull(supplier);
    }
}
