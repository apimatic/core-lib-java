package apimatic.core.type.pagination;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import io.apimatic.core.HttpRequest;
import io.apimatic.core.types.pagination.CursorPagination;
import io.apimatic.core.types.pagination.PaginatedData;

public class CursorPaginationTest {
    @Rule
    public MockitoRule initRule = MockitoJUnit.rule().silent();

    @Test
    public void testWithValidCursor_returnsTrue() {
        PaginatedData<?, ?> paginatedData = mock(PaginatedData.class);

        when(paginatedData.getLastRequestBuilder())
                .thenReturn(new HttpRequest.Builder().queryParam(q -> q.key("cursor").value("abc")));
        when(paginatedData.getLastResponseBody()).thenReturn("{\"next_cursor\": \"xyz123\"}");

        CursorPagination cursor = new CursorPagination("$response.body#/next_cursor", "$request.query#/cursor");

        assertTrue(cursor.isValid(paginatedData));
        assertNotNull(cursor.getNextRequestBuilder());

        cursor.getNextRequestBuilder().updateByReference("$request.query#/cursor", v -> {
            assertEquals("xyz123", v);
            return v;
        });
    }

    @Test
    public void testWithValidCursorAndDifferentType1_returnsTrue() {
        PaginatedData<?, ?> paginatedData = mock(PaginatedData.class);

        when(paginatedData.getLastRequestBuilder())
                .thenReturn(new HttpRequest.Builder().queryParam(q -> q.key("cursor").value("abc")));
        when(paginatedData.getLastResponseBody()).thenReturn("{\"next_cursor\": 123}");

        CursorPagination cursor = new CursorPagination("$response.body#/next_cursor", "$request.query#/cursor");

        assertTrue(cursor.isValid(paginatedData));
        assertNotNull(cursor.getNextRequestBuilder());

        cursor.getNextRequestBuilder().updateByReference("$request.query#/cursor", v -> {
            assertEquals("123", v);
            return v;
        });
    }

    @Test
    public void testWithValidCursorAndDifferentType2_returnsTrue() {
        PaginatedData<?, ?> paginatedData = mock(PaginatedData.class);

        when(paginatedData.getLastRequestBuilder())
                .thenReturn(new HttpRequest.Builder().queryParam(q -> q.key("cursor").value(456)));
        when(paginatedData.getLastResponseBody()).thenReturn("{\"next_cursor\": 123}");

        CursorPagination cursor = new CursorPagination("$response.body#/next_cursor", "$request.query#/cursor");

        assertTrue(cursor.isValid(paginatedData));
        assertNotNull(cursor.getNextRequestBuilder());

        cursor.getNextRequestBuilder().updateByReference("$request.query#/cursor", v -> {
            assertEquals("123", v);
            return v;
        });
    }

    @Test
    public void testWithValidCursorButMissingInFirstRequest_returnsFalse() {
        PaginatedData<?, ?> paginatedData = mock(PaginatedData.class);

        when(paginatedData.getLastRequestBuilder()).thenReturn(new HttpRequest.Builder());
        when(paginatedData.getLastResponseBody()).thenReturn("{\"next_cursor\": \"xyz123\"}");

        CursorPagination cursor = new CursorPagination("$response.body#/next_cursor", "$request.query#/cursor");

        assertFalse(cursor.isValid(paginatedData));
        assertNotNull(cursor.getNextRequestBuilder());

        cursor.getNextRequestBuilder().updateByReference("$request.query#/cursor", v -> {
            fail();
            return v;
        });
    }

    @Test
    public void testWithValidCursorFromResponseHeader_returnsTrue() {
        PaginatedData<?, ?> paginatedData = mock(PaginatedData.class);

        when(paginatedData.getLastRequestBuilder())
                .thenReturn(new HttpRequest.Builder().queryParam(q -> q.key("cursor").value("abc")));
        when(paginatedData.getLastResponseHeaders()).thenReturn("{\"next_cursor\": \"xyz123\"}");

        CursorPagination cursor = new CursorPagination("$response.headers#/next_cursor", "$request.query#/cursor");

        assertTrue(cursor.isValid(paginatedData));
        assertNotNull(cursor.getNextRequestBuilder());

        cursor.getNextRequestBuilder().updateByReference("$request.query#/cursor", v -> {
            assertEquals("xyz123", v);
            return v;
        });
    }

    @Test
    public void testWithValidCursorInTemplateParam_returnsTrue() {
        PaginatedData<?, ?> paginatedData = mock(PaginatedData.class);

        when(paginatedData.getLastRequestBuilder())
                .thenReturn(new HttpRequest.Builder().templateParam(t -> t.key("cursor").value("abc")));
        when(paginatedData.getLastResponseBody()).thenReturn("{\"next_cursor\": \"xyz123\"}");

        CursorPagination cursor = new CursorPagination("$response.body#/next_cursor", "$request.path#/cursor");

        assertTrue(cursor.isValid(paginatedData));
        assertNotNull(cursor.getNextRequestBuilder());

        cursor.getNextRequestBuilder().updateByReference("$request.path#/cursor", v -> {
            assertEquals("xyz123", v);
            return v;
        });
    }

    @Test
    public void testWithValidCursorInHeaderParam_returnsTrue() {
        PaginatedData<?, ?> paginatedData = mock(PaginatedData.class);

        when(paginatedData.getLastRequestBuilder())
                .thenReturn(new HttpRequest.Builder().headerParam(h -> h.key("cursor").value("abc")));
        when(paginatedData.getLastResponseBody()).thenReturn("{\"next_cursor\": \"xyz123\"}");

        CursorPagination cursor = new CursorPagination("$response.body#/next_cursor", "$request.headers#/cursor");

        assertTrue(cursor.isValid(paginatedData));
        assertNotNull(cursor.getNextRequestBuilder());

        cursor.getNextRequestBuilder().updateByReference("$request.headers#/cursor", v -> {
            assertEquals("xyz123", v);
            return v;
        });
    }

    @Test
    public void testWithInValidResponsePointer_returnsFalse() {
        PaginatedData<?, ?> paginatedData = mock(PaginatedData.class);

        when(paginatedData.getLastRequestBuilder())
                .thenReturn(new HttpRequest.Builder().headerParam(h -> h.key("cursor").value("abc")));
        when(paginatedData.getLastResponseBody()).thenReturn("{\"next_cursor\": \"xyz123\"}");

        CursorPagination cursor = new CursorPagination("$response.body#/next", "$request.headers#/cursor");

        assertFalse(cursor.isValid(paginatedData));
        assertNotNull(cursor.getNextRequestBuilder());

        cursor.getNextRequestBuilder().updateByReference("$request.headers#/cursor", v -> {
            assertEquals("abc", v);
            return v;
        });
    }

    @Test
    public void testWithMissingResponsePointer_returnsFalse() {
        PaginatedData<?, ?> paginatedData = mock(PaginatedData.class);

        when(paginatedData.getLastRequestBuilder())
                .thenReturn(new HttpRequest.Builder().headerParam(h -> h.key("cursor").value("abc")));
        when(paginatedData.getLastResponseBody()).thenReturn("{\"next_cursor\": \"xyz123\"}");

        CursorPagination cursor = new CursorPagination(null, "$request.headers#/cursor");

        assertFalse(cursor.isValid(paginatedData));
        assertNotNull(cursor.getNextRequestBuilder());

        cursor.getNextRequestBuilder().updateByReference("$request.headers#/cursor", v -> {
            assertEquals("abc", v);
            return v;
        });
    }

    @Test
    public void testWithInValidRequestPointer_returnsFalse() {
        PaginatedData<?, ?> paginatedData = mock(PaginatedData.class);

        when(paginatedData.getLastRequestBuilder())
                .thenReturn(new HttpRequest.Builder().headerParam(h -> h.key("cursor").value("abc")));
        when(paginatedData.getLastResponseBody()).thenReturn("{\"next_cursor\": \"xyz123\"}");

        CursorPagination cursor = new CursorPagination("$response.body#/next_cursor", "$request.headers#/next_cursor");

        assertFalse(cursor.isValid(paginatedData));
        assertNotNull(cursor.getNextRequestBuilder());

        cursor.getNextRequestBuilder().updateByReference("$request.headers#/cursor", v -> {
            assertEquals("abc", v);
            return v;
        });
    }

    @Test
    public void testWithMissingRequestPointer_returnsFalse() {
        PaginatedData<?, ?> paginatedData = mock(PaginatedData.class);

        when(paginatedData.getLastRequestBuilder())
                .thenReturn(new HttpRequest.Builder().headerParam(h -> h.key("cursor").value("abc")));
        when(paginatedData.getLastResponseBody()).thenReturn("{\"next_cursor\": \"xyz123\"}");

        CursorPagination cursor = new CursorPagination("$response.body#/next_cursor", null);

        assertFalse(cursor.isValid(paginatedData));
        assertNotNull(cursor.getNextRequestBuilder());

        cursor.getNextRequestBuilder().updateByReference("$request.headers#/cursor", v -> {
            assertEquals("abc", v);
            return v;
        });
    }

}
