package apimatic.core.type.pagination;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import io.apimatic.core.HttpRequest;
import io.apimatic.core.HttpRequest.Builder;
import io.apimatic.core.types.pagination.OffsetPagination;
import io.apimatic.core.types.pagination.PaginatedData;

/**
 * Unit tests for {@link OffsetPagination}.
 */
public class OffsetPaginationTest {
    private static final int INITIAL_OFFSET = 3;
    private static final int PAGE_SIZE = 100;
    private static final int OFFSET_PLUS_PAGE = 103;
    private static final int OFFSET_VAL_PLUS_ONE = 101;
    private static final int OFFSET_STRING_PLUS_PAGE = 105;
    private static final int NUMERIC_OFFSET = 5;

    /**
     * JUnit rule to initialize Mockito annotations.
     */
    @Rule
    public MockitoRule initRule = MockitoJUnit.rule().silent();

    @Test
    public void testValidOffsetHeaderReturnsTrue() {
        // Setup mocks
        PaginatedData<?, ?, ?, ?> paginatedData = mock(PaginatedData.class);
        HttpRequest.Builder requestBuilder = new HttpRequest.Builder()
                .headerParam(h -> h.key("offset").value(INITIAL_OFFSET));

        // Mock behaviors
        when(paginatedData.getRequestBuilder()).thenReturn(requestBuilder);
        when(paginatedData.getPageSize()).thenReturn(PAGE_SIZE);

        // Test the offset pagination
        OffsetPagination offset = new OffsetPagination("$request.headers#/offset");
        Builder nextRequestBuilder = offset.apply(paginatedData);

        // Verify results
        assertNotNull(nextRequestBuilder);
        nextRequestBuilder.updateByReference("$request.headers#/offset", v -> {
            assertEquals(OFFSET_PLUS_PAGE, v);
            return v;
        });
    }

    @Test
    public void testValidOffsetTemplateReturnsTrue() {
        // Setup mocks
        PaginatedData<?, ?, ?, ?> paginatedData = mock(PaginatedData.class);
        HttpRequest.Builder requestBuilder = new HttpRequest.Builder()
                .templateParam(t -> t.key("offset").value(INITIAL_OFFSET));

        // Mock behaviors
        when(paginatedData.getRequestBuilder()).thenReturn(requestBuilder);
        when(paginatedData.getPageSize()).thenReturn(PAGE_SIZE);

        // Test the offset pagination
        OffsetPagination offset = new OffsetPagination("$request.path#/offset");
        Builder nextRequestBuilder = offset.apply(paginatedData);

        // Verify results
        assertNotNull(nextRequestBuilder);
        nextRequestBuilder.updateByReference("$request.path#/offset", v -> {
            assertEquals(OFFSET_PLUS_PAGE, v);
            return v;
        });
    }

    @Test
    public void testValidOffsetReturnsTrue() {
        // Setup mocks
        PaginatedData<?, ?, ?, ?> paginatedData = mock(PaginatedData.class);
        HttpRequest.Builder requestBuilder = new HttpRequest.Builder()
                .queryParam(q -> q.key("offset").value(INITIAL_OFFSET));

        // Mock behaviors
        when(paginatedData.getRequestBuilder()).thenReturn(requestBuilder);
        when(paginatedData.getPageSize()).thenReturn(PAGE_SIZE);

        // Test the offset pagination
        OffsetPagination offset = new OffsetPagination("$request.query#/offset");
        Builder nextRequestBuilder = offset.apply(paginatedData);

        // Verify results
        assertNotNull(nextRequestBuilder);
        nextRequestBuilder.updateByReference("$request.query#/offset", v -> {
            assertEquals(OFFSET_PLUS_PAGE, v);
            return v;
        });
    }

    @Test
    public void testValidOffsetAsInnerFieldReturnsTrue() {
        // Setup mocks
        PaginatedData<?, ?, ?, ?> paginatedData = mock(PaginatedData.class);
        Map<String, String> offsetMap = new HashMap<String, String>() {
            private static final long serialVersionUID = 1L;
            {
                put("val", "1");
            }
        };

        HttpRequest.Builder requestBuilder = new HttpRequest.Builder()
                .queryParam(q -> q.key("offset").value(offsetMap));

        // Mock behaviors
        when(paginatedData.getRequestBuilder()).thenReturn(requestBuilder);
        when(paginatedData.getPageSize()).thenReturn(PAGE_SIZE);

        // Test the offset pagination with nested field
        OffsetPagination offset = new OffsetPagination("$request.query#/offset/val");
        Builder nextRequestBuilder = offset.apply(paginatedData);

        // Verify results
        assertNotNull(nextRequestBuilder);
        nextRequestBuilder.updateByReference("$request.query#/offset/val", v -> {
            assertEquals(OFFSET_VAL_PLUS_ONE, v);
            return v;
        });
    }

    @Test
    public void testValidStringOffsetReturnsTrue() {
        // Setup mocks
        PaginatedData<?, ?, ?, ?> paginatedData = mock(PaginatedData.class);
        HttpRequest.Builder requestBuilder = new HttpRequest.Builder().queryParam(q -> q.key("offset").value("5"));

        // Mock behaviors
        when(paginatedData.getRequestBuilder()).thenReturn(requestBuilder);
        when(paginatedData.getPageSize()).thenReturn(PAGE_SIZE);

        // Test the offset pagination with string value
        OffsetPagination offset = new OffsetPagination("$request.query#/offset");
        Builder nextRequestBuilder = offset.apply(paginatedData);

        // Verify results
        assertNotNull(nextRequestBuilder);
        nextRequestBuilder.updateByReference("$request.query#/offset", v -> {
            assertEquals(OFFSET_STRING_PLUS_PAGE, v);
            return v;
        });
    }

    @Test
    public void testInvalidStringOffsetReturnsFalse() {
        // Setup mocks
        PaginatedData<?, ?, ?, ?> paginatedData = mock(PaginatedData.class);
        HttpRequest.Builder requestBuilder = new HttpRequest.Builder().queryParam(q -> q.key("offset").value("5a"));

        // Mock behaviors
        when(paginatedData.getRequestBuilder()).thenReturn(requestBuilder);
        when(paginatedData.getPageSize()).thenReturn(PAGE_SIZE);

        // Test the offset pagination with invalid string value
        OffsetPagination offset = new OffsetPagination("$request.query#/offset");

        // Verify the offset remains unchanged for invalid values
        Builder nextRequestBuilder = offset.apply(paginatedData);
        assertNotNull(nextRequestBuilder);

        nextRequestBuilder.updateByReference("$request.query#/offset", v -> {
            assertEquals("5a", v); // Value should remain unchanged
            return v;
        });
    }

    @Test
    public void testMissingOffsetReturnsFalse() {
        // Setup mocks
        PaginatedData<?, ?, ?, ?> paginatedData = mock(PaginatedData.class);
        HttpRequest.Builder requestBuilder = new HttpRequest.Builder(); // No offset param

        // Mock behaviors
        when(paginatedData.getRequestBuilder()).thenReturn(requestBuilder);
        when(paginatedData.getPageSize()).thenReturn(PAGE_SIZE);

        // Test the offset pagination with missing offset
        OffsetPagination offset = new OffsetPagination("$request.query#/offset");

        // Execute and verify
        Builder nextRequestBuilder = offset.apply(paginatedData);
        assertNotNull("Builder should still be returned even with missing offset", nextRequestBuilder);

        // Verify no update is attempted for missing offset
        AtomicBoolean callbackInvoked = new AtomicBoolean(false);
        nextRequestBuilder.updateByReference("$request.query#/offset", v -> {
            callbackInvoked.set(true);
            return v;
        });

        assertFalse("Callback should not be invoked for missing offset", callbackInvoked.get());
    }

    @Test
    public void testNullOffsetReturnsFalse() {
        // Setup mocks
        PaginatedData<?, ?, ?, ?> paginatedData = mock(PaginatedData.class);
        HttpRequest.Builder requestBuilder = new HttpRequest.Builder()
                .queryParam(q -> q.key("offset").value(NUMERIC_OFFSET));

        // Mock behaviors
        when(paginatedData.getRequestBuilder()).thenReturn(requestBuilder);
        when(paginatedData.getPageSize()).thenReturn(PAGE_SIZE);

        // Test with null offset configuration
        OffsetPagination offset = new OffsetPagination(null);

        // Execute and verify
        Builder nextRequestBuilder = offset.apply(paginatedData);
        assertNotNull("Builder should still be returned even with null offset config", nextRequestBuilder);

        // Verify original offset remains unchanged
        AtomicBoolean callbackInvoked = new AtomicBoolean(false);
        nextRequestBuilder.updateByReference("$request.query#/offset", v -> {
            callbackInvoked.set(true);
            assertEquals("Original offset should remain unchanged", NUMERIC_OFFSET, v);
            return v;
        });

        assertTrue("Should still process existing offset values", callbackInvoked.get());
    }
}
