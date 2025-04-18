package apimatic.core.type.pagination;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
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
    public void testIsValid_withValidCursor_returnsTrue() {
        PaginatedData<?, ?> paginatedData = mock(PaginatedData.class);

        when(paginatedData.getLastRequestBuilder())
                .thenReturn(new HttpRequest.Builder().queryParam(q -> q.key("cursor").value("abc")));
        when(paginatedData.getLastResponseBody()).thenReturn("{\"next_cursor\": \"xyz123\"}");
        when(paginatedData.getLastResponseHeaders()).thenReturn("{}");

        CursorPagination cursor = new CursorPagination("$response.body#/next_cursor", "$request.query#/cursor");

        assertTrue(cursor.isValid(paginatedData));
        assertNotNull(cursor.getNextRequestBuilder());

        cursor.getNextRequestBuilder().updateByReference("$request.query#/cursor", v -> {
            assertEquals("xyz123", v);
            return v;
        });
    }

}
