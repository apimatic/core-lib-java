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

    private static final String CURSOR_KEY = "cursor";

    /**
     * Test with a valid cursor value.
     */
    @Test
    public void testWithValidCursorReturnsTrue() {
        PaginatedData<?, ?> paginatedData = mock(PaginatedData.class);

        when(paginatedData.getLastRequestBuilder())
                .thenReturn(new HttpRequest.Builder().queryParam(
                        q -> q.key(CURSOR_KEY).value("abc")));
        when(paginatedData.getLastResponseBody()).thenReturn("{\"next_cursor\": \"xyz123\"}");

        CursorPagination cursor = new CursorPagination("$response.body#/next_cursor",
                "$request.query#/cursor");

        assertTrue(cursor.isValid(paginatedData));
        assertNotNull(cursor.getNextRequestBuilder());

        cursor.getNextRequestBuilder().updateByReference("$request.query#/cursor", v -> {
            assertEquals("xyz123", v);
            return v;
        });
    }

    /**
     * Test with a valid cursor but different response type (integer).
     */
    @Test
    public void testWithValidCursorAndDifferentTypeReturnsTrueA() {
        PaginatedData<?, ?> paginatedData = mock(PaginatedData.class);

        when(paginatedData.getLastRequestBuilder())
                .thenReturn(new HttpRequest.Builder().queryParam(
                        q -> q.key(CURSOR_KEY).value("abc")));
        when(paginatedData.getLastResponseBody()).thenReturn("{\"next_cursor\": 123}");

        CursorPagination cursor = new CursorPagination("$response.body#/next_cursor",
                "$request.query#/cursor");

        assertTrue(cursor.isValid(paginatedData));
        assertNotNull(cursor.getNextRequestBuilder());

        cursor.getNextRequestBuilder().updateByReference("$request.query#/cursor", v -> {
            assertEquals("123", v);
            return v;
        });
    }

    @Test
    public void testWithValidCursorAndDifferentTypeReturnsTrueB() {
        PaginatedData<?, ?> paginatedData = mock(PaginatedData.class);
        final int current = 456;

        when(paginatedData.getLastRequestBuilder())
                .thenReturn(new HttpRequest.Builder().queryParam(
                        q -> q.key("cursor").value(current)));
        when(paginatedData.getLastResponseBody()).thenReturn("{\"next_cursor\": 123}");

        CursorPagination cursor = new CursorPagination("$response.body#/next_cursor",
                "$request.query#/cursor");

        assertTrue(cursor.isValid(paginatedData));
        assertNotNull(cursor.getNextRequestBuilder());

        cursor.getNextRequestBuilder().updateByReference("$request.query#/cursor", v -> {
            assertEquals("123", v);
            return v;
        });
    }

    @Test
    public void testWithValidCursorButMissingInFirstRequestReturnsFalse() {
        PaginatedData<?, ?> paginatedData = mock(PaginatedData.class);

        when(paginatedData.getLastRequestBuilder()).thenReturn(new HttpRequest.Builder());
        when(paginatedData.getLastResponseBody()).thenReturn("{\"next_cursor\": \"xyz123\"}");

        CursorPagination cursor = new CursorPagination("$response.body#/next_cursor",
                "$request.query#/cursor");

        assertFalse(cursor.isValid(paginatedData));
        assertNotNull(cursor.getNextRequestBuilder());

        cursor.getNextRequestBuilder().updateByReference("$request.query#/cursor", v -> {
            fail();
            return v;
        });
    }

    /**
     * Test with valid cursor from a response body and different type.
     */
    @Test
    public void testWithValidCursorFromResponseBodyReturnsTrue() {
        PaginatedData<?, ?> paginatedData = mock(PaginatedData.class);

        when(paginatedData.getLastRequestBuilder())
                .thenReturn(new HttpRequest.Builder().queryParam(
                        q -> q.key(CURSOR_KEY).value("abc")));
        when(paginatedData.getLastResponseBody()).thenReturn("{\"next_cursor\": 123}");

        CursorPagination cursor = new CursorPagination("$response.body#/next_cursor",
                "$request.query#/cursor");

        assertTrue(cursor.isValid(paginatedData));
        assertNotNull(cursor.getNextRequestBuilder());

        cursor.getNextRequestBuilder().updateByReference("$request.query#/cursor", v -> {
            assertEquals("123", v);
            return v;
        });
    }

    /**
     * Test case where the response pointer is invalid.
     */
    @Test
    public void testWithInvalidResponsePointerReturnsFalse() {
        PaginatedData<?, ?> paginatedData = mock(PaginatedData.class);

        when(paginatedData.getLastRequestBuilder())
                .thenReturn(new HttpRequest.Builder().headerParam(
                        h -> h.key(CURSOR_KEY).value("abc")));
        when(paginatedData.getLastResponseBody()).thenReturn("{\"next_cursor\": \"xyz123\"}");

        CursorPagination cursor = new CursorPagination("$response.body#/next",
                "$request.headers#/cursor");

        assertFalse(cursor.isValid(paginatedData));
        assertNotNull(cursor.getNextRequestBuilder());

        cursor.getNextRequestBuilder().updateByReference("$request.headers#/cursor", v -> {
            assertEquals("abc", v);
            return v;
        });
    }

    /**
     * Test with missing response pointer.
     */
    @Test
    public void testWithMissingResponsePointerReturnsFalse() {
        PaginatedData<?, ?> paginatedData = mock(PaginatedData.class);

        when(paginatedData.getLastRequestBuilder())
                .thenReturn(new HttpRequest.Builder().headerParam(
                        h -> h.key(CURSOR_KEY).value("abc")));
        when(paginatedData.getLastResponseBody()).thenReturn("{\"next_cursor\": \"xyz123\"}");

        CursorPagination cursor = new CursorPagination(null, "$request.headers#/cursor");

        assertFalse(cursor.isValid(paginatedData));
        assertNotNull(cursor.getNextRequestBuilder());

        cursor.getNextRequestBuilder().updateByReference("$request.headers#/cursor", v -> {
            assertEquals("abc", v);
            return v;
        });
    }

    /**
     * Test case with invalid request pointer.
     */
    @Test
    public void testWithInvalidRequestPointerReturnsFalse() {
        PaginatedData<?, ?> paginatedData = mock(PaginatedData.class);

        when(paginatedData.getLastRequestBuilder())
                .thenReturn(new HttpRequest.Builder().headerParam(
                        h -> h.key(CURSOR_KEY).value("abc")));
        when(paginatedData.getLastResponseBody()).thenReturn("{\"next_cursor\": \"xyz123\"}");

        CursorPagination cursor = new CursorPagination("$response.body#/next_cursor",
                "$request.headers#/next_cursor");

        assertFalse(cursor.isValid(paginatedData));
        assertNotNull(cursor.getNextRequestBuilder());

        cursor.getNextRequestBuilder().updateByReference("$request.headers#/cursor", v -> {
            assertEquals("abc", v);
            return v;
        });
    }

    /**
     * Test with missing request pointer.
     */
    @Test
    public void testWithMissingRequestPointerReturnsFalse() {
        PaginatedData<?, ?> paginatedData = mock(PaginatedData.class);

        when(paginatedData.getLastRequestBuilder())
                .thenReturn(new HttpRequest.Builder().headerParam(
                        h -> h.key(CURSOR_KEY).value("abc")));
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
