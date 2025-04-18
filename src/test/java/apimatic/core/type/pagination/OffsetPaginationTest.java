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

public class OffsetPaginationTest {
    @Rule
    public MockitoRule initRule = MockitoJUnit.rule().silent();

    @Test
    public void testWithValidOffsetHeader_returnsTrue() {
        PaginatedData<?, ?> paginatedData = mock(PaginatedData.class);

        when(paginatedData.getLastRequestBuilder())
                .thenReturn(new HttpRequest.Builder().headerParam(h -> h.key("offset").value(3)));
        when(paginatedData.getLastDataSize()).thenReturn(100);

        OffsetPagination offset = new OffsetPagination("$request.headers#/offset");

        assertTrue(offset.isValid(paginatedData));
        assertNotNull(offset.getNextRequestBuilder());

        offset.getNextRequestBuilder().updateByReference("$request.headers#/offset", v -> {
            assertEquals(103, v);
            return v;
        });
    }

    @Test
    public void testWithValidOffsetTemplate_returnsTrue() {
        PaginatedData<?, ?> paginatedData = mock(PaginatedData.class);

        when(paginatedData.getLastRequestBuilder())
                .thenReturn(new HttpRequest.Builder().templateParam(t -> t.key("offset").value(3)));
        when(paginatedData.getLastDataSize()).thenReturn(100);

        OffsetPagination offset = new OffsetPagination("$request.path#/offset");

        assertTrue(offset.isValid(paginatedData));
        assertNotNull(offset.getNextRequestBuilder());

        offset.getNextRequestBuilder().updateByReference("$request.path#/offset", v -> {
            assertEquals(103, v);
            return v;
        });
    }

    @Test
    public void testWithValidOffset_returnsTrue() {
        PaginatedData<?, ?> paginatedData = mock(PaginatedData.class);

        when(paginatedData.getLastRequestBuilder())
                .thenReturn(new HttpRequest.Builder().queryParam(q -> q.key("offset").value(3)));
        when(paginatedData.getLastDataSize()).thenReturn(100);

        OffsetPagination offset = new OffsetPagination("$request.query#/offset");

        assertTrue(offset.isValid(paginatedData));
        assertNotNull(offset.getNextRequestBuilder());

        offset.getNextRequestBuilder().updateByReference("$request.query#/offset", v -> {
            assertEquals(103, v);
            return v;
        });
    }

    @Test
    public void testWithValidOffsetAsInnerField_returnsTrue() {
        PaginatedData<?, ?> paginatedData = mock(PaginatedData.class);

        when(paginatedData.getLastRequestBuilder()).thenReturn(
                new HttpRequest.Builder().queryParam(q -> q.key("offset").value(new HashMap<String, String>() {
                    private static final long serialVersionUID = 1L;
                    {
                        put("val", "1");
                    }
                })));
        when(paginatedData.getLastDataSize()).thenReturn(100);

        OffsetPagination offset = new OffsetPagination("$request.query#/offset/val");

        assertTrue(offset.isValid(paginatedData));
        assertNotNull(offset.getNextRequestBuilder());

        offset.getNextRequestBuilder().updateByReference("$request.query#/offset/val", v -> {
            assertEquals(101, v);
            return v;
        });
    }

    @Test
    public void testWithValidStringOffset_returnsTrue() {
        PaginatedData<?, ?> paginatedData = mock(PaginatedData.class);

        when(paginatedData.getLastRequestBuilder())
                .thenReturn(new HttpRequest.Builder().queryParam(q -> q.key("offset").value("5")));
        when(paginatedData.getLastDataSize()).thenReturn(100);

        OffsetPagination offset = new OffsetPagination("$request.query#/offset");

        assertTrue(offset.isValid(paginatedData));
        assertNotNull(offset.getNextRequestBuilder());

        offset.getNextRequestBuilder().updateByReference("$request.query#/offset", v -> {
            assertEquals(105, v);
            return v;
        });
    }

    @Test
    public void testWithInValidStringOffset_returnsFalse() {
        PaginatedData<?, ?> paginatedData = mock(PaginatedData.class);

        when(paginatedData.getLastRequestBuilder())
                .thenReturn(new HttpRequest.Builder().queryParam(q -> q.key("offset").value("5a")));
        when(paginatedData.getLastDataSize()).thenReturn(100);

        OffsetPagination offset = new OffsetPagination("$request.query#/offset");

        assertFalse(offset.isValid(paginatedData));
        assertNotNull(offset.getNextRequestBuilder());

        offset.getNextRequestBuilder().updateByReference("$request.query#/offset", v -> {
            assertEquals("5a", v);
            return v;
        });
    }

    @Test
    public void testWithMissingOffset_returnsFalse() {
        PaginatedData<?, ?> paginatedData = mock(PaginatedData.class);

        when(paginatedData.getLastRequestBuilder()).thenReturn(new HttpRequest.Builder());
        when(paginatedData.getLastDataSize()).thenReturn(100);

        OffsetPagination offset = new OffsetPagination("$request.query#/offset");

        assertFalse(offset.isValid(paginatedData));
        assertNotNull(offset.getNextRequestBuilder());

        offset.getNextRequestBuilder().updateByReference("$request.query#/offset", v -> {
            fail();
            return v;
        });
    }

    @Test
    public void testWithNullOffset_returnsFalse() {
        PaginatedData<?, ?> paginatedData = mock(PaginatedData.class);

        when(paginatedData.getLastRequestBuilder())
                .thenReturn(new HttpRequest.Builder().queryParam(q -> q.key("offset").value(5)));
        when(paginatedData.getLastDataSize()).thenReturn(100);

        OffsetPagination offset = new OffsetPagination(null);

        assertFalse(offset.isValid(paginatedData));
        assertNotNull(offset.getNextRequestBuilder());

        offset.getNextRequestBuilder().updateByReference("$request.query#/offset", v -> {
            assertEquals(5, v);
            return v;
        });
    }

}
