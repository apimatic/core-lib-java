package apimatic.core.type.pagination;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import io.apimatic.core.HttpRequest;
import io.apimatic.core.HttpRequest.Builder;
import io.apimatic.core.types.pagination.CursorPagination;
import io.apimatic.core.types.pagination.PageWrapper;
import io.apimatic.core.types.pagination.PaginatedData;
import io.apimatic.coreinterfaces.http.response.Response;

public class CursorPaginationTest {

    /**
     * Initializes Mockito rule to enable Mockito annotations and
     * silently handle unnecessary stubbings during tests.
     */
    @Rule
    public MockitoRule initRule = MockitoJUnit.rule().silent();

    private static final String CURSOR_KEY = "cursor";
    private static final String RESPONSE_POINTER_VALID = "$response.body#/next_cursor";
    private static final String RESPONSE_POINTER_INVALID = "$response.body#/next";
    private static final String REQUEST_QUERY_POINTER = "$request.query#/cursor";
    private static final String REQUEST_HEADER_POINTER = "$request.headers#/cursor";
    private static final String REQUEST_POINTER_INVALID = "$request.headers#/next_cursor";
    private static final String NEXT_CURSOR_JSON_STRING = "{\"next_cursor\": \"xyz123\"}";
    private static final String NEXT_CURSOR_JSON_INT = "{\"next_cursor\": 123}";

    @Test
    public void testWithValidCursor() {
        PaginatedData<?, ?, ?, ?> paginatedData = mock(PaginatedData.class);
        Response response = mock(Response.class);

        when(paginatedData.getRequestBuilder()).thenReturn(
                new HttpRequest.Builder().queryParam(q -> q.key(CURSOR_KEY).value("abc")));
        when(paginatedData.getResponse()).thenReturn(response);
        when(response.getBody()).thenReturn(NEXT_CURSOR_JSON_STRING);

        CursorPagination cursor = new CursorPagination(RESPONSE_POINTER_VALID,
                REQUEST_QUERY_POINTER);

        Builder requestBuilder = cursor.apply(paginatedData);
        assertNotNull(requestBuilder);
        requestBuilder.updateParameterByJsonPointer(REQUEST_QUERY_POINTER, v -> {
            assertEquals("xyz123", v);
            return v;
        });
        PageWrapper<?, ?> pageWrapper = PageWrapper.create(response, null, null, cursor);
        assertTrue(pageWrapper.isCursorPagination());
        assertEquals("xyz123", pageWrapper.getCursorInput());
    }

    @Test
    public void testWithValidCursorAndDifferentTypeA() {
        PaginatedData<?, ?, ?, ?> paginatedData = mock(PaginatedData.class);
        Response response = mock(Response.class);

        when(paginatedData.getRequestBuilder()).thenReturn(
                new HttpRequest.Builder().queryParam(q -> q.key(CURSOR_KEY).value("abc")));
        when(paginatedData.getResponse()).thenReturn(response);
        when(response.getBody()).thenReturn(NEXT_CURSOR_JSON_INT);

        CursorPagination cursor = new CursorPagination(RESPONSE_POINTER_VALID,
                REQUEST_QUERY_POINTER);

        Builder requestBuilder = cursor.apply(paginatedData);
        assertNotNull(requestBuilder);
        requestBuilder.updateParameterByJsonPointer(REQUEST_QUERY_POINTER, v -> {
            assertEquals("123", v);
            return v;
        });
        PageWrapper<?, ?> pageWrapper = PageWrapper.create(response, null, null, cursor);
        assertTrue(pageWrapper.isCursorPagination());
        assertEquals("123", pageWrapper.getCursorInput());
    }

    @Test
    public void testWithValidCursorAndDifferentType() {
        PaginatedData<?, ?, ?, ?> paginatedData = mock(PaginatedData.class);
        final int current = 456;
        Response response = mock(Response.class);

        when(paginatedData.getRequestBuilder()).thenReturn(
                new HttpRequest.Builder().queryParam(q -> q.key(CURSOR_KEY).value(current)));
        when(paginatedData.getResponse()).thenReturn(response);
        when(response.getBody()).thenReturn(NEXT_CURSOR_JSON_INT);

        CursorPagination cursor = new CursorPagination(RESPONSE_POINTER_VALID,
                REQUEST_QUERY_POINTER);

        Builder requestBuilder = cursor.apply(paginatedData);
        assertNotNull(requestBuilder);
        requestBuilder.updateParameterByJsonPointer(REQUEST_QUERY_POINTER, v -> {
            assertEquals("123", v);
            return v;
        });
        PageWrapper<?, ?> pageWrapper = PageWrapper.create(response, null, null, cursor);
        assertTrue(pageWrapper.isCursorPagination());
        assertEquals("123", pageWrapper.getCursorInput());
    }

    @Test
    public void testWithValidCursorButMissingInFirstRequest() {
        PaginatedData<?, ?, ?, ?> paginatedData = mock(PaginatedData.class);
        Response response = mock(Response.class);

        when(paginatedData.getRequestBuilder()).thenReturn(new HttpRequest.Builder());
        when(paginatedData.getResponse()).thenReturn(response);
        when(response.getBody()).thenReturn(NEXT_CURSOR_JSON_STRING);

        CursorPagination cursor = new CursorPagination(RESPONSE_POINTER_VALID,
                REQUEST_QUERY_POINTER);

        Builder requestBuilder = cursor.apply(paginatedData);
        assertNotNull(requestBuilder);
        requestBuilder.updateParameterByJsonPointer(RESPONSE_POINTER_VALID, v -> {
            assertEquals(null, v);
            return v;
        });
        PageWrapper<?, ?> pageWrapper = PageWrapper.create(response, null, null, cursor);
        assertTrue(pageWrapper.isCursorPagination());
        assertEquals("xyz123", pageWrapper.getCursorInput());
    }

    @Test
    public void testWithMissingCursorFirstRequest() {
        PaginatedData<?, ?, ?, ?> paginatedData = mock(PaginatedData.class);
        Response response = mock(Response.class);

        when(paginatedData.getRequestBuilder()).thenReturn(new HttpRequest.Builder());
        when(paginatedData.getResponse()).thenReturn(null);

        CursorPagination cursor = new CursorPagination(RESPONSE_POINTER_VALID,
                REQUEST_QUERY_POINTER);

        Builder requestBuilder = cursor.apply(paginatedData);
        assertNotNull(requestBuilder);
        requestBuilder.updateParameterByJsonPointer(RESPONSE_POINTER_VALID, v -> {
            assertEquals(null, v);
            return v;
        });
        PageWrapper<?, ?> pageWrapper = PageWrapper.create(response, null, null, cursor);
        assertTrue(pageWrapper.isCursorPagination());
        assertEquals(null, pageWrapper.getCursorInput());
    }

    @Test
    public void testWithValidCursorFromResponseBody() {
        PaginatedData<?, ?, ?, ?> paginatedData = mock(PaginatedData.class);
        Response response = mock(Response.class);

        when(paginatedData.getRequestBuilder()).thenReturn(
                new HttpRequest.Builder().queryParam(q -> q.key(CURSOR_KEY).value("abc")));
        when(paginatedData.getResponse()).thenReturn(response);
        when(response.getBody()).thenReturn(NEXT_CURSOR_JSON_INT);

        CursorPagination cursor = new CursorPagination(RESPONSE_POINTER_VALID,
                REQUEST_QUERY_POINTER);

        Builder requestBuilder = cursor.apply(paginatedData);
        assertNotNull(requestBuilder);
        requestBuilder.updateParameterByJsonPointer(REQUEST_QUERY_POINTER, v -> {
            assertEquals("123", v);
            return v;
        });
        PageWrapper<?, ?> pageWrapper = PageWrapper.create(response, null, null, cursor);
        assertTrue(pageWrapper.isCursorPagination());
        assertEquals("123", pageWrapper.getCursorInput());
    }

    @Test
    public void testWithInvalidResponsePointer() {
        PaginatedData<?, ?, ?, ?> paginatedData = mock(PaginatedData.class);
        Response response = mock(Response.class);

        when(paginatedData.getRequestBuilder()).thenReturn(
                new HttpRequest.Builder().headerParam(h -> h.key(CURSOR_KEY).value("abc")));
        when(paginatedData.getResponse()).thenReturn(response);
        when(response.getBody()).thenReturn(NEXT_CURSOR_JSON_STRING);

        CursorPagination cursor = new CursorPagination(RESPONSE_POINTER_INVALID,
                REQUEST_HEADER_POINTER);

        Builder requestBuilder = cursor.apply(paginatedData);
        assertNull(requestBuilder);
    }

    @Test
    public void testWithMissingResponsePointer() {
        PaginatedData<?, ?, ?, ?> paginatedData = mock(PaginatedData.class);
        Response response = mock(Response.class);

        when(paginatedData.getRequestBuilder()).thenReturn(
                new HttpRequest.Builder().headerParam(h -> h.key(CURSOR_KEY).value("abc")));
        when(paginatedData.getResponse()).thenReturn(response);
        when(response.getBody()).thenReturn(NEXT_CURSOR_JSON_STRING);

        CursorPagination cursor = new CursorPagination(null, REQUEST_HEADER_POINTER);

        Builder requestBuilder = cursor.apply(paginatedData);
        assertNull(requestBuilder);
    }

    @Test
    public void testWithInvalidRequestPointer() {
        PaginatedData<?, ?, ?, ?> paginatedData = mock(PaginatedData.class);
        Response response = mock(Response.class);

        when(paginatedData.getRequestBuilder()).thenReturn(
                new HttpRequest.Builder().headerParam(h -> h.key(CURSOR_KEY).value("abc")));
        when(paginatedData.getResponse()).thenReturn(response);
        when(response.getBody()).thenReturn(NEXT_CURSOR_JSON_STRING);

        CursorPagination cursor = new CursorPagination(RESPONSE_POINTER_VALID,
                REQUEST_POINTER_INVALID);

        Builder requestBuilder = cursor.apply(paginatedData);
        assertNotNull(requestBuilder);
        requestBuilder.updateParameterByJsonPointer(REQUEST_POINTER_INVALID, v -> {
            assertEquals("xyz123", v);
            return v;
        });
        PageWrapper<?, ?> pageWrapper = PageWrapper.create(response, null, null, cursor);
        assertTrue(pageWrapper.isCursorPagination());
        assertEquals("xyz123", pageWrapper.getCursorInput());
    }

    @Test
    public void testWithMissingRequest() {
        PaginatedData<?, ?, ?, ?> paginatedData = mock(PaginatedData.class);
        Response response = mock(Response.class);

        when(paginatedData.getRequestBuilder()).thenReturn(
                new HttpRequest.Builder().headerParam(h -> h.key(CURSOR_KEY).value("abc")));
        when(paginatedData.getResponse()).thenReturn(response);
        when(response.getBody()).thenReturn(NEXT_CURSOR_JSON_STRING);

        CursorPagination cursor = new CursorPagination(RESPONSE_POINTER_VALID, null);
        assertNull(cursor.apply(paginatedData));
    }
}
