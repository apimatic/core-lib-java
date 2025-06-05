package apimatic.core.type.pagination;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import io.apimatic.core.HttpRequest;
import io.apimatic.core.HttpRequest.Builder;
import io.apimatic.core.types.pagination.PagePagination;
import io.apimatic.core.types.pagination.PaginatedData;
import io.apimatic.coreinterfaces.http.response.Response;

/**
 * Unit tests for PagePagination.
 */
public class PagePaginationTest {

    /**
     * Mockito rule for initializing mocks.
     */
    @Rule
    public MockitoRule initRule = MockitoJUnit.rule().silent();

    @Test
    public void testWithValidPageHeaderReturnsTrue() {
        PaginatedData<?, ?, ?, ?> paginatedData = mock(PaginatedData.class);
        Response response = mock(Response.class);
        final int initialPage = 3;
        final int nextPage = 4;

        when(paginatedData.getRequestBuilder())
                .thenReturn(new HttpRequest.Builder().headerParam(
                        h -> h.key("page").value(initialPage)));
        when(paginatedData.getResponse()).thenReturn(response);
        when(response.getBody()).thenReturn("{\"page\": " + initialPage + "}");

        PagePagination page = new PagePagination("$request.headers#/page");

        Builder requestBuilder = page.apply(paginatedData);
        assertNotNull(requestBuilder);

        requestBuilder.updateByReference("$request.headers#/page", v -> {
            assertEquals(nextPage, v);
            return v;
        });
    }

    @Test
    public void testWithValidPageTemplateReturnsTrue() {
        PaginatedData<?, ?, ?, ?> paginatedData = mock(PaginatedData.class);
        Response response = mock(Response.class);
        final int initialPage = 3;
        final int nextPage = 4;

        when(paginatedData.getRequestBuilder())
                .thenReturn(new HttpRequest.Builder().templateParam(
                        t -> t.key("page").value(initialPage)));
        when(paginatedData.getResponse()).thenReturn(response);
        when(response.getBody()).thenReturn("{\"page\": " + initialPage + "}");

        PagePagination page = new PagePagination("$request.path#/page");

        Builder requestBuilder = page.apply(paginatedData);
        assertNotNull(requestBuilder);

        requestBuilder.updateByReference("$request.path#/page", v -> {
            assertEquals(nextPage, v);
            return v;
        });
    }

    @Test
    public void testWithValidPageReturnsTrue() {
        PaginatedData<?, ?, ?, ?> paginatedData = mock(PaginatedData.class);
        Response response = mock(Response.class);
        final int initialPage = 3;
        final int nextPage = 4;

        when(paginatedData.getRequestBuilder())
                .thenReturn(new HttpRequest.Builder().queryParam(
                        q -> q.key("page").value(initialPage)));
        when(paginatedData.getResponse()).thenReturn(response);
        when(response.getBody()).thenReturn("{\"page\": " + initialPage + "}");

        PagePagination page = new PagePagination("$request.query#/page");

        Builder requestBuilder = page.apply(paginatedData);
        assertNotNull(requestBuilder);

        requestBuilder.updateByReference("$request.query#/page", v -> {
            assertEquals(nextPage, v);
            return v;
        });
    }

    @Test
    public void testWithValidPageAsInnerFieldReturnsTrue() {
        PaginatedData<?, ?, ?, ?> paginatedData = mock(PaginatedData.class);
        Response response = mock(Response.class);

        when(paginatedData.getRequestBuilder()).thenReturn(
                new HttpRequest.Builder().queryParam(q -> q.key("page")
                        .value(new HashMap<String, String>() {
                    private static final long serialVersionUID = 1L;
                    {
                        put("val", "1");
                    }
                })));
        when(paginatedData.getResponse()).thenReturn(response);
        when(response.getBody()).thenReturn("{\"page\": {\"val\": 1}}");

        PagePagination page = new PagePagination("$request.query#/page/val");

        Builder requestBuilder = page.apply(paginatedData);
        assertNotNull(requestBuilder);

        requestBuilder.updateByReference("$request.query#/page/val", v -> {
            assertEquals(2, v);
            return v;
        });
    }

    @Test
    public void testWithValidStringPageReturnsTrue() {
        PaginatedData<?, ?, ?, ?> paginatedData = mock(PaginatedData.class);
        Response response = mock(Response.class);
        final String current = "5";
        final int next = 6;

        when(paginatedData.getRequestBuilder())
                .thenReturn(new HttpRequest.Builder().queryParam(
                        q -> q.key("page").value(current)));
        when(paginatedData.getResponse()).thenReturn(response);
        when(response.getBody()).thenReturn("{\"page\": \"" + current + "\"}");

        PagePagination page = new PagePagination("$request.query#/page");

        Builder requestBuilder = page.apply(paginatedData);
        assertNotNull(requestBuilder);

        requestBuilder.updateByReference("$request.query#/page", v -> {
            assertEquals(next, v);
            return v;
        });
    }

    @Test
    public void testWithInvalidStringPageReturnsFalse() {
        PaginatedData<?, ?, ?, ?> paginatedData = mock(PaginatedData.class);
        Response response = mock(Response.class);

        when(paginatedData.getRequestBuilder())
                .thenReturn(new HttpRequest.Builder().queryParam(q -> q.key("page").value("5a")));
        when(paginatedData.getResponse()).thenReturn(response);
        when(response.getBody()).thenReturn("{\"page\": \"5a\"}");

        PagePagination page = new PagePagination("$request.query#/page");

        Builder requestBuilder = page.apply(paginatedData);
        assertNull(requestBuilder);
    }

    @Test
    public void testWithMissingPageReturnsFalse() {
        PaginatedData<?, ?, ?, ?> paginatedData = mock(PaginatedData.class);
        Response response = mock(Response.class);

        when(paginatedData.getRequestBuilder()).thenReturn(new HttpRequest.Builder());
        when(paginatedData.getResponse()).thenReturn(response);
        when(response.getBody()).thenReturn("{}");

        PagePagination page = new PagePagination("$request.query#/page");

        Builder requestBuilder = page.apply(paginatedData);
        assertNull(requestBuilder);
    }

    @Test
    public void testWithNullPageReturnsFalse() {
        PaginatedData<?, ?, ?, ?> paginatedData = mock(PaginatedData.class);
        Response response = mock(Response.class);
        final int current = 5;

        when(paginatedData.getRequestBuilder())
                .thenReturn(new HttpRequest.Builder().queryParam(
                        q -> q.key("page").value(current)));
        when(paginatedData.getResponse()).thenReturn(response);
        when(response.getBody()).thenReturn("{\"page\": " + current + "}");

        PagePagination page = new PagePagination(null);

        Builder requestBuilder = page.apply(paginatedData);
        assertNull(requestBuilder);
    }
}