package apimatic.core.type.pagination;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import io.apimatic.core.HttpRequest;
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
        PaginatedData<?, ?> paginatedData = mock(PaginatedData.class);

        when(paginatedData.getLastRequestBuilder())
            .thenReturn(new HttpRequest.Builder().headerParam(h -> h.key("offset").value(INITIAL_OFFSET)));
        when(paginatedData.getLastDataSize()).thenReturn(PAGE_SIZE);

        OffsetPagination offset = new OffsetPagination("$request.headers#/offset");

        assertTrue(offset.isValid(paginatedData));
        assertNotNull(offset.getNextRequestBuilder());

        offset.getNextRequestBuilder().updateByReference("$request.headers#/offset", v -> {
            assertEquals(OFFSET_PLUS_PAGE, v);
            return v;
        });
    }

    @Test
    public void testValidOffsetTemplateReturnsTrue() {
        PaginatedData<?, ?> paginatedData = mock(PaginatedData.class);

        when(paginatedData.getLastRequestBuilder())
            .thenReturn(new HttpRequest.Builder().templateParam(t -> t.key("offset").value(INITIAL_OFFSET)));
        when(paginatedData.getLastDataSize()).thenReturn(PAGE_SIZE);

        OffsetPagination offset = new OffsetPagination("$request.path#/offset");

        assertTrue(offset.isValid(paginatedData));
        assertNotNull(offset.getNextRequestBuilder());

        offset.getNextRequestBuilder().updateByReference("$request.path#/offset", v -> {
            assertEquals(OFFSET_PLUS_PAGE, v);
            return v;
        });
    }

    @Test
    public void testValidOffsetReturnsTrue() {
        PaginatedData<?, ?> paginatedData = mock(PaginatedData.class);

        when(paginatedData.getLastRequestBuilder())
            .thenReturn(new HttpRequest.Builder().queryParam(q -> q.key("offset").value(INITIAL_OFFSET)));
        when(paginatedData.getLastDataSize()).thenReturn(PAGE_SIZE);

        OffsetPagination offset = new OffsetPagination("$request.query#/offset");

        assertTrue(offset.isValid(paginatedData));
        assertNotNull(offset.getNextRequestBuilder());

        offset.getNextRequestBuilder().updateByReference("$request.query#/offset", v -> {
            assertEquals(OFFSET_PLUS_PAGE, v);
            return v;
        });
    }

    @Test
    public void testValidOffsetAsInnerFieldReturnsTrue() {
        PaginatedData<?, ?> paginatedData = mock(PaginatedData.class);

        when(paginatedData.getLastRequestBuilder()).thenReturn(
            new HttpRequest.Builder().queryParam(q -> q.key("offset").value(new HashMap<String, String>() {
                private static final long serialVersionUID = 1L;
                {
                    put("val", "1");
                }
            })));
        when(paginatedData.getLastDataSize()).thenReturn(PAGE_SIZE);

        OffsetPagination offset = new OffsetPagination("$request.query#/offset/val");

        assertTrue(offset.isValid(paginatedData));
        assertNotNull(offset.getNextRequestBuilder());

        offset.getNextRequestBuilder().updateByReference("$request.query#/offset/val", v -> {
            assertEquals(OFFSET_VAL_PLUS_ONE, v);
            return v;
        });
    }

    @Test
    public void testValidStringOffsetReturnsTrue() {
        PaginatedData<?, ?> paginatedData = mock(PaginatedData.class);

        when(paginatedData.getLastRequestBuilder())
            .thenReturn(new HttpRequest.Builder().queryParam(q -> q.key("offset").value("5")));
        when(paginatedData.getLastDataSize()).thenReturn(PAGE_SIZE);

        OffsetPagination offset = new OffsetPagination("$request.query#/offset");

        assertTrue(offset.isValid(paginatedData));
        assertNotNull(offset.getNextRequestBuilder());

        offset.getNextRequestBuilder().updateByReference("$request.query#/offset", v -> {
            assertEquals(OFFSET_STRING_PLUS_PAGE, v);
            return v;
        });
    }

    @Test
    public void testInvalidStringOffsetReturnsFalse() {
        PaginatedData<?, ?> paginatedData = mock(PaginatedData.class);

        when(paginatedData.getLastRequestBuilder())
            .thenReturn(new HttpRequest.Builder().queryParam(q -> q.key("offset").value("5a")));
        when(paginatedData.getLastDataSize()).thenReturn(PAGE_SIZE);

        OffsetPagination offset = new OffsetPagination("$request.query#/offset");

        assertFalse(offset.isValid(paginatedData));
        assertNotNull(offset.getNextRequestBuilder());

        offset.getNextRequestBuilder().updateByReference("$request.query#/offset", v -> {
            assertEquals("5a", v);
            return v;
        });
    }

    @Test
    public void testMissingOffsetReturnsFalse() {
        PaginatedData<?, ?> paginatedData = mock(PaginatedData.class);

        when(paginatedData.getLastRequestBuilder()).thenReturn(new HttpRequest.Builder());
        when(paginatedData.getLastDataSize()).thenReturn(PAGE_SIZE);

        OffsetPagination offset = new OffsetPagination("$request.query#/offset");

        assertFalse(offset.isValid(paginatedData));
        assertNotNull(offset.getNextRequestBuilder());

        offset.getNextRequestBuilder().updateByReference("$request.query#/offset", v -> {
            fail();
            return v;
        });
    }

    @Test
    public void testNullOffsetReturnsFalse() {
        PaginatedData<?, ?> paginatedData = mock(PaginatedData.class);

        when(paginatedData.getLastRequestBuilder())
            .thenReturn(new HttpRequest.Builder().queryParam(q -> q.key("offset").value(NUMERIC_OFFSET)));
        when(paginatedData.getLastDataSize()).thenReturn(PAGE_SIZE);

        OffsetPagination offset = new OffsetPagination(null);

        assertFalse(offset.isValid(paginatedData));
        assertNotNull(offset.getNextRequestBuilder());

        offset.getNextRequestBuilder().updateByReference("$request.query#/offset", v -> {
            assertEquals(NUMERIC_OFFSET, v);
            return v;
        });
    }
}
