package apimatic.core.type.pagination;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
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

/**
 * Unit tests for the {@link CursorPagination} class.
 * This class tests the behavior of cursor pagination logic.
 */
public class CursorPaginationTest {

    /**
     * Silent rule for Mockito initialization.
     */
    @Rule
    public MockitoRule initRule = MockitoJUnit.rule().silent();

    // Constants
    private static final String CURSOR_KEY = "cursor";
    private static final String RESPONSE_POINTER_VALID = "$response.body#/next_cursor";
    private static final String RESPONSE_POINTER_INVALID = "$response.body#/next";
    private static final String REQUEST_QUERY_POINTER = "$request.query#/cursor";
    private static final String REQUEST_HEADER_POINTER = "$request.headers#/cursor";
    private static final String REQUEST_POINTER_INVALID = "$request.headers#/next_cursor";
    private static final String NEXT_CURSOR_JSON_STRING = "{\"next_cursor\": \"xyz123\"}";
    private static final String NEXT_CURSOR_JSON_INT = "{\"next_cursor\": 123}";

    /**
     * Test with a valid cursor value.
     */
    @Test
    public void testWithValidCursor() {
        PaginatedData<?, ?, ?, ?> paginatedData = mock(PaginatedData.class);
        Response response = mock(Response.class);

        when(paginatedData.getRequestBuilder())
                .thenReturn(new HttpRequest.Builder().queryParam(
                        q -> q.key(CURSOR_KEY).value("abc")));
        when(paginatedData.getResponse()).thenReturn(response);
        when(response.getBody()).thenReturn(NEXT_CURSOR_JSON_STRING);

        CursorPagination cursor = new CursorPagination(RESPONSE_POINTER_VALID, REQUEST_QUERY_POINTER);

        Builder requestBuilder = cursor.apply(paginatedData);
        assertNotNull(requestBuilder);

        requestBuilder.updateParameterByJsonPointer(REQUEST_QUERY_POINTER, v -> {
            assertEquals("xyz123", v);
            return v;
        });
        PageWrapper<?, ?> pageWrapper = PageWrapper.Create(response, null, null);
        cursor.addMetaData(pageWrapper);
        assertEquals("xyz123", pageWrapper.getCursorInput());
    }

    /**
     * Test with a valid cursor but different response type (integer).
     */
    @Test
    public void testWithValidCursorAndDifferentTypeA() {
        PaginatedData<?, ?, ?, ?> paginatedData = mock(PaginatedData.class);
        Response response = mock(Response.class);

        when(paginatedData.getRequestBuilder())
                .thenReturn(new HttpRequest.Builder().queryParam(
                        q -> q.key(CURSOR_KEY).value("abc")));
        when(paginatedData.getResponse()).thenReturn(response);
        when(response.getBody()).thenReturn(NEXT_CURSOR_JSON_INT);

        CursorPagination cursor = new CursorPagination(RESPONSE_POINTER_VALID, REQUEST_QUERY_POINTER);

        Builder requestBuilder = cursor.apply(paginatedData);
        assertNotNull(requestBuilder);

        requestBuilder.updateParameterByJsonPointer(REQUEST_QUERY_POINTER, v -> {
            assertEquals("123", v);
            return v;
        });
        PageWrapper<?, ?> pageWrapper = PageWrapper.Create(response, null, null);
        cursor.addMetaData(pageWrapper);
        assertEquals("123", pageWrapper.getCursorInput());
    }

    /**
     * Test with a valid cursor but different response type (integer) and integer cursor input.
     */
    @Test
    public void testWithValidCursorAndDifferentType() {
        PaginatedData<?, ?, ?, ?> paginatedData = mock(PaginatedData.class);
        final int current = 456;
        Response response = mock(Response.class);

        when(paginatedData.getRequestBuilder())
                .thenReturn(new HttpRequest.Builder().queryParam(
                        q -> q.key(CURSOR_KEY).value(current)));
        when(paginatedData.getResponse()).thenReturn(response);
        when(response.getBody()).thenReturn(NEXT_CURSOR_JSON_INT);

        CursorPagination cursor = new CursorPagination(RESPONSE_POINTER_VALID, REQUEST_QUERY_POINTER);

        Builder requestBuilder = cursor.apply(paginatedData);
        assertNotNull(requestBuilder);

        requestBuilder.updateParameterByJsonPointer(REQUEST_QUERY_POINTER, v -> {
            assertEquals("123", v);
            return v;
        });
        PageWrapper<?, ?> pageWrapper = PageWrapper.Create(response, null, null);
        cursor.addMetaData(pageWrapper);
        assertEquals("123", pageWrapper.getCursorInput());
    }

    /**
     * Test with valid cursor but missing in the first request.
     */
    @Test
    public void testWithValidCursorButMissingInFirstRequest() {
        PaginatedData<?, ?, ?, ?> paginatedData = mock(PaginatedData.class);
        Response response = mock(Response.class);

        when(paginatedData.getRequestBuilder()).thenReturn(new HttpRequest.Builder());
        when(paginatedData.getResponse()).thenReturn(response);
        when(response.getBody()).thenReturn(NEXT_CURSOR_JSON_STRING);

        CursorPagination cursor = new CursorPagination(RESPONSE_POINTER_VALID, REQUEST_QUERY_POINTER);

        Builder requestBuilder = cursor.apply(paginatedData);
        assertNull(requestBuilder);
    }

    /**
     * Test with valid cursor from a response body and different type.
     */
    @Test
    public void testWithValidCursorFromResponseBody() {
        PaginatedData<?, ?, ?, ?> paginatedData = mock(PaginatedData.class);
        Response response = mock(Response.class);

        when(paginatedData.getRequestBuilder())
                .thenReturn(new HttpRequest.Builder().queryParam(
                        q -> q.key(CURSOR_KEY).value("abc")));
        when(paginatedData.getResponse()).thenReturn(response);
        when(response.getBody()).thenReturn(NEXT_CURSOR_JSON_INT);

        CursorPagination cursor = new CursorPagination(RESPONSE_POINTER_VALID, REQUEST_QUERY_POINTER);

        Builder requestBuilder = cursor.apply(paginatedData);
        assertNotNull(requestBuilder);

        requestBuilder.updateParameterByJsonPointer(REQUEST_QUERY_POINTER, v -> {
            assertEquals("123", v);
            return v;
        });
        PageWrapper<?, ?> pageWrapper = PageWrapper.Create(response, null, null);
        cursor.addMetaData(pageWrapper);
        assertEquals("123", pageWrapper.getCursorInput());
    }

    /**
     * Test case where the response pointer is invalid.
     */
    @Test
    public void testWithInvalidResponsePointer() {
        PaginatedData<?, ?, ?, ?> paginatedData = mock(PaginatedData.class);
        Response response = mock(Response.class);

        when(paginatedData.getRequestBuilder())
                .thenReturn(new HttpRequest.Builder().headerParam(
                        h -> h.key(CURSOR_KEY).value("abc")));
        when(paginatedData.getResponse()).thenReturn(response);
        when(response.getBody()).thenReturn(NEXT_CURSOR_JSON_STRING);

        CursorPagination cursor = new CursorPagination(RESPONSE_POINTER_INVALID, REQUEST_HEADER_POINTER);

        Builder requestBuilder = cursor.apply(paginatedData);
        assertNull(requestBuilder);
    }

    /**
     * Test with missing response pointer.
     */
    @Test
    public void testWithMissingResponsePointer() {
        PaginatedData<?, ?, ?, ?> paginatedData = mock(PaginatedData.class);
        Response response = mock(Response.class);

        when(paginatedData.getRequestBuilder())
                .thenReturn(new HttpRequest.Builder().headerParam(
                        h -> h.key(CURSOR_KEY).value("abc")));
        when(paginatedData.getResponse()).thenReturn(response);
        when(response.getBody()).thenReturn(NEXT_CURSOR_JSON_STRING);

        CursorPagination cursor = new CursorPagination(null, REQUEST_HEADER_POINTER);

        Builder requestBuilder = cursor.apply(paginatedData);
        assertNull(requestBuilder);
    }

    /**
     * Test case with invalid request pointer.
     */
    @Test
    public void testWithInvalidRequestPointer() {
        PaginatedData<?, ?, ?, ?> paginatedData = mock(PaginatedData.class);
        Response response = mock(Response.class);

        when(paginatedData.getRequestBuilder())
                .thenReturn(new HttpRequest.Builder().headerParam(
                        h -> h.key(CURSOR_KEY).value("abc")));
        when(paginatedData.getResponse()).thenReturn(response);
        when(response.getBody()).thenReturn(NEXT_CURSOR_JSON_STRING);

        CursorPagination cursor = new CursorPagination(RESPONSE_POINTER_VALID, REQUEST_POINTER_INVALID);

        assertNull(cursor.apply(paginatedData));
    }

    /**
     * Test with missing request pointer.
     */
    @Test
    public void testWithMissingRequest() {
        PaginatedData<?, ?, ?, ?> paginatedData = mock(PaginatedData.class);
        Response response = mock(Response.class);

        when(paginatedData.getRequestBuilder())
                .thenReturn(new HttpRequest.Builder().headerParam(
                        h -> h.key(CURSOR_KEY).value("abc")));
        when(paginatedData.getResponse()).thenReturn(response);
        when(response.getBody()).thenReturn(NEXT_CURSOR_JSON_STRING);

        CursorPagination cursor = new CursorPagination(RESPONSE_POINTER_VALID, null);
        assertNull(cursor.apply(paginatedData));
    }

}
