package apimatic.core.type.pagination;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import io.apimatic.core.HttpRequest;
import io.apimatic.core.HttpRequest.Builder;
import io.apimatic.core.types.pagination.OffsetPagination;
import io.apimatic.core.types.pagination.PaginatedData;
import io.apimatic.coreinterfaces.http.response.Response;

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
        PaginatedData<?, ?, ?, ?> paginatedData = mock(PaginatedData.class);
        Response response = mock(Response.class);

        when(paginatedData.getRequestBuilder())
            .thenReturn(new HttpRequest.Builder().headerParam(
                    h -> h.key("offset").value(INITIAL_OFFSET)));
        when(paginatedData.getResponse()).thenReturn(response);
        when(response.getBody()).thenReturn("{\"offset\": " + INITIAL_OFFSET + "}");
        when(paginatedData.getPageSize()).thenReturn(PAGE_SIZE);

        OffsetPagination offset = new OffsetPagination("$request.headers#/offset");

        Builder requestBuilder = offset.apply(paginatedData);
        assertNotNull(requestBuilder);

        requestBuilder.updateByReference("$request.headers#/offset", v -> {
            assertEquals(OFFSET_PLUS_PAGE, v);
            return v;
        });
    }

    @Test
    public void testValidOffsetTemplateReturnsTrue() {
        PaginatedData<?, ?, ?, ?> paginatedData = mock(PaginatedData.class);
        Response response = mock(Response.class);

        when(paginatedData.getRequestBuilder())
            .thenReturn(new HttpRequest.Builder().templateParam(
                    t -> t.key("offset").value(INITIAL_OFFSET)));
        when(paginatedData.getResponse()).thenReturn(response);
        when(response.getBody()).thenReturn("{\"offset\": " + INITIAL_OFFSET + "}");
        when(paginatedData.getPageSize()).thenReturn(PAGE_SIZE);

        OffsetPagination offset = new OffsetPagination("$request.path#/offset");

        Builder requestBuilder = offset.apply(paginatedData);
        assertNotNull(requestBuilder);

        requestBuilder.updateByReference("$request.path#/offset", v -> {
            assertEquals(OFFSET_PLUS_PAGE, v);
            return v;
        });
    }

    @Test
    public void testValidOffsetReturnsTrue() {
        PaginatedData<?, ?, ?, ?> paginatedData = mock(PaginatedData.class);
        Response response = mock(Response.class);

        when(paginatedData.getRequestBuilder())
            .thenReturn(new HttpRequest.Builder().queryParam(
                    q -> q.key("offset").value(INITIAL_OFFSET)));
        when(paginatedData.getResponse()).thenReturn(response);
        when(response.getBody()).thenReturn("{\"offset\": " + INITIAL_OFFSET + "}");
        when(paginatedData.getPageSize()).thenReturn(PAGE_SIZE);

        OffsetPagination offset = new OffsetPagination("$request.query#/offset");

        Builder requestBuilder = offset.apply(paginatedData);
        assertNotNull(requestBuilder);

        requestBuilder.updateByReference("$request.query#/offset", v -> {
            assertEquals(OFFSET_PLUS_PAGE, v);
            return v;
        });
    }

    @Test
    public void testValidOffsetAsInnerFieldReturnsTrue() {
        PaginatedData<?, ?, ?, ?> paginatedData = mock(PaginatedData.class);
        Response response = mock(Response.class);

        when(paginatedData.getRequestBuilder()).thenReturn(
            new HttpRequest.Builder().queryParam(q -> q.key("offset").value(
                    new HashMap<String, String>() {
                        private static final long serialVersionUID = 1L;
                        {
                            put("val", "1");
                        }
                    })));
        when(paginatedData.getResponse()).thenReturn(response);
        when(response.getBody()).thenReturn("{\"offset\": {\"val\": 1}}");
        when(paginatedData.getPageSize()).thenReturn(PAGE_SIZE);

        OffsetPagination offset = new OffsetPagination("$request.query#/offset/val");

        Builder requestBuilder = offset.apply(paginatedData);
        assertNotNull(requestBuilder);

        requestBuilder.updateByReference("$request.query#/offset/val", v -> {
            assertEquals(OFFSET_VAL_PLUS_ONE, v);
            return v;
        });
    }

    @Test
    public void testValidStringOffsetReturnsTrue() {
        PaginatedData<?, ?, ?, ?> paginatedData = mock(PaginatedData.class);
        Response response = mock(Response.class);

        when(paginatedData.getRequestBuilder())
            .thenReturn(new HttpRequest.Builder().queryParam(
                    q -> q.key("offset").value("5")));
        when(paginatedData.getResponse()).thenReturn(response);
        when(response.getBody()).thenReturn("{\"offset\": \"5\"}");
        when(paginatedData.getPageSize()).thenReturn(PAGE_SIZE);

        OffsetPagination offset = new OffsetPagination("$request.query#/offset");

        Builder requestBuilder = offset.apply(paginatedData);
        assertNotNull(requestBuilder);

        requestBuilder.updateByReference("$request.query#/offset", v -> {
            assertEquals(OFFSET_STRING_PLUS_PAGE, v);
            return v;
        });
    }


    @Test
    public void testInvalidStringOffsetReturnsFalse() {
        PaginatedData<?, ?, ?, ?> paginatedData = mock(PaginatedData.class);

        when(paginatedData.getRequestBuilder())
            .thenReturn(new HttpRequest.Builder().queryParam(
                    q -> q.key("offset").value("5a")));
        when(paginatedData.getPageSize()).thenReturn(PAGE_SIZE);

        OffsetPagination offset = new OffsetPagination("$request.query#/offset");

        Builder requestBuilder = offset.apply(paginatedData);
        assertNotNull(requestBuilder);

        requestBuilder.updateByReference("$request.query#/offset", v -> {
            assertEquals("5a", v);
            return v;
        });
    }

    @Test
    public void testMissingOffsetReturnsFalse() {
        PaginatedData<?, ?, ?, ?> paginatedData = mock(PaginatedData.class);

        when(paginatedData.getRequestBuilder()).thenReturn(new HttpRequest.Builder());
        when(paginatedData.getPageSize()).thenReturn(PAGE_SIZE);

        OffsetPagination offset = new OffsetPagination("$request.query#/offset");

        Builder requestBuilder = offset.apply(paginatedData);
        assertNotNull(requestBuilder);
    }

    @Test
    public void testNullOffsetReturnsFalse() {
        PaginatedData<?, ?, ?, ?> paginatedData = mock(PaginatedData.class);

        when(paginatedData.getRequestBuilder())
            .thenReturn(new HttpRequest.Builder().queryParam(
                    q -> q.key("offset").value(NUMERIC_OFFSET)));
        when(paginatedData.getPageSize()).thenReturn(PAGE_SIZE);

        OffsetPagination offset = new OffsetPagination(null);

        Builder requestBuilder = offset.apply(paginatedData);
        assertNotNull(requestBuilder);

        requestBuilder.updateByReference("$request.query#/offset", v -> {
            assertEquals(NUMERIC_OFFSET, v);
            return v;
        });
    }
}