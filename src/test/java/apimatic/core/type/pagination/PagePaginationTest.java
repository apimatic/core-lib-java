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
import io.apimatic.core.types.pagination.PageWrapper;
import io.apimatic.core.types.pagination.PaginatedData;
import io.apimatic.coreinterfaces.http.response.Response;

/**
 * Unit tests for PagePagination.
 */
public class PagePaginationTest {

    private static final int INITIAL_PAGE = 3;
    private static final int NEXT_PAGE = 4;
    private static final int INNER_FIELD_PAGE = 1;
    private static final int INNER_FIELD_NEXT_PAGE = 2;
    private static final String CURRENT_PAGE_STRING = "5";
    private static final int NEXT_PAGE_FROM_STRING = 6;
    private static final String INVALID_PAGE_STRING = "5a";
    private static final int CURRENT_PAGE_INT = 5;

    /**
     * Mockito rule for initializing mocks.
     */
    @Rule
    public MockitoRule initRule = MockitoJUnit.rule().silent();

    @Test
    public void testWithValidPageHeader() {
        PaginatedData<?, ?, ?, ?> paginatedData = mock(PaginatedData.class);
        Response response = mock(Response.class);

        when(paginatedData.getRequestBuilder())
                .thenReturn(new HttpRequest.Builder().headerParam(
                        h -> h.key("page").value(INITIAL_PAGE)));
        when(paginatedData.getResponse()).thenReturn(response);
        when(response.getBody()).thenReturn("{\"page\": " + INITIAL_PAGE + "}");

        PagePagination page = new PagePagination("$request.headers#/page");

        Builder requestBuilder = page.apply(paginatedData);
        assertNotNull(requestBuilder);
        requestBuilder.updateParameterByJsonPointer("$request.headers#/page", v -> {
            assertEquals(NEXT_PAGE, v);
            return v;
        });
        PageWrapper<?, ?> pageWrapper = PageWrapper.create(response, null, null);
        page.addMetaData(pageWrapper);
        assertEquals(NEXT_PAGE, pageWrapper.getPageInput());
    }

    @Test
    public void testWithValidPageTemplate() {
        PaginatedData<?, ?, ?, ?> paginatedData = mock(PaginatedData.class);
        Response response = mock(Response.class);

        when(paginatedData.getRequestBuilder())
                .thenReturn(new HttpRequest.Builder().templateParam(
                        t -> t.key("page").value(INITIAL_PAGE)));
        when(paginatedData.getResponse()).thenReturn(response);
        when(response.getBody()).thenReturn("{\"page\": " + INITIAL_PAGE + "}");

        PagePagination page = new PagePagination("$request.path#/page");

        Builder requestBuilder = page.apply(paginatedData);
        assertNotNull(requestBuilder);
        requestBuilder.updateParameterByJsonPointer("$request.path#/page", v -> {
            assertEquals(NEXT_PAGE, v);
            return v;
        });
        PageWrapper<?, ?> pageWrapper = PageWrapper.create(response, null, null);
        page.addMetaData(pageWrapper);
        assertEquals(NEXT_PAGE, pageWrapper.getPageInput());
    }

    @Test
    public void testWithValidPage() {
        PaginatedData<?, ?, ?, ?> paginatedData = mock(PaginatedData.class);
        Response response = mock(Response.class);

        when(paginatedData.getRequestBuilder())
                .thenReturn(new HttpRequest.Builder().queryParam(
                        q -> q.key("page").value(INITIAL_PAGE)));
        when(paginatedData.getResponse()).thenReturn(response);
        when(response.getBody()).thenReturn("{\"page\": " + INITIAL_PAGE + "}");

        PagePagination page = new PagePagination("$request.query#/page");

        Builder requestBuilder = page.apply(paginatedData);
        assertNotNull(requestBuilder);
        requestBuilder.updateParameterByJsonPointer("$request.query#/page", v -> {
            assertEquals(NEXT_PAGE, v);
            return v;
        });
        PageWrapper<?, ?> pageWrapper = PageWrapper.create(response, null, null);
        page.addMetaData(pageWrapper);
        assertEquals(NEXT_PAGE, pageWrapper.getPageInput());
    }

    @Test
    public void testWithValidPageAsInnerField() {
        PaginatedData<?, ?, ?, ?> paginatedData = mock(PaginatedData.class);
        Response response = mock(Response.class);

        when(paginatedData.getRequestBuilder()).thenReturn(
                new HttpRequest.Builder().queryParam(q -> q.key("page")
                        .value(new HashMap<String, String>() {
                    private static final long serialVersionUID = 1L;
                    {
                        put("val", String.valueOf(INNER_FIELD_PAGE));
                    }
                })));
        when(paginatedData.getResponse()).thenReturn(response);
        when(response.getBody()).thenReturn("{\"page\": {\"val\": " + INNER_FIELD_PAGE + "}}");

        PagePagination page = new PagePagination("$request.query#/page/val");

        Builder requestBuilder = page.apply(paginatedData);
        assertNotNull(requestBuilder);
        requestBuilder.updateParameterByJsonPointer("$request.query#/page/val", v -> {
            assertEquals(INNER_FIELD_NEXT_PAGE, v);
            return v;
        });
        PageWrapper<?, ?> pageWrapper = PageWrapper.create(response, null, null);
        page.addMetaData(pageWrapper);

        assertEquals(INNER_FIELD_NEXT_PAGE, pageWrapper.getPageInput());
    }

    @Test
    public void testWithValidStringPage() {
        PaginatedData<?, ?, ?, ?> paginatedData = mock(PaginatedData.class);
        Response response = mock(Response.class);

        when(paginatedData.getRequestBuilder())
                .thenReturn(new HttpRequest.Builder().queryParam(
                        q -> q.key("page").value(CURRENT_PAGE_STRING)));
        when(paginatedData.getResponse()).thenReturn(response);
        when(response.getBody()).thenReturn("{\"page\": \"" + CURRENT_PAGE_STRING + "\"}");

        PagePagination page = new PagePagination("$request.query#/page");

        Builder requestBuilder = page.apply(paginatedData);
        assertNotNull(requestBuilder);
        requestBuilder.updateParameterByJsonPointer("$request.query#/page", v -> {
            assertEquals(NEXT_PAGE_FROM_STRING, v);
            return v;
        });
        PageWrapper<?, ?> pageWrapper = PageWrapper.create(response, null, null);
        page.addMetaData(pageWrapper);

        assertEquals(NEXT_PAGE_FROM_STRING, pageWrapper.getPageInput());
    }

    @Test
    public void testWithInvalidStringPage() {
        PaginatedData<?, ?, ?, ?> paginatedData = mock(PaginatedData.class);
        Response response = mock(Response.class);

        when(paginatedData.getRequestBuilder())
                .thenReturn(new HttpRequest.Builder()
                		.queryParam(q -> q.key("page")
                		.value(INVALID_PAGE_STRING)));
        when(paginatedData.getResponse()).thenReturn(response);
        when(response.getBody()).thenReturn("{\"page\": \"" + INVALID_PAGE_STRING + "\"}");

        PagePagination page = new PagePagination("$request.query#/page");

        Builder requestBuilder = page.apply(paginatedData);
        assertNull(requestBuilder);
    }

    @Test
    public void testWithMissingPage() {
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
    public void testWithNullPage() {
        PaginatedData<?, ?, ?, ?> paginatedData = mock(PaginatedData.class);
        Response response = mock(Response.class);

        when(paginatedData.getRequestBuilder())
                .thenReturn(new HttpRequest.Builder().queryParam(
                        q -> q.key("page").value(CURRENT_PAGE_INT)));
        when(paginatedData.getResponse()).thenReturn(response);
        when(response.getBody()).thenReturn("{\"page\": " + CURRENT_PAGE_INT + "}");

        PagePagination page = new PagePagination(null);

        Builder requestBuilder = page.apply(paginatedData);
        assertNull(requestBuilder);
    }
}
