package apimatic.core.type.pagination;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import io.apimatic.core.HttpRequest;
import io.apimatic.core.HttpRequest.Builder;
import io.apimatic.core.types.pagination.PagePagination;
import io.apimatic.core.types.pagination.PaginatedData;

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
        // Setup mocks
        PaginatedData<?, ?, ?, ?> paginatedData = mock(PaginatedData.class);
        final int initialPage = 3;
        final int nextPage = 4;
        
        HttpRequest.Builder requestBuilder = new HttpRequest.Builder()
            .headerParam(h -> h.key("page").value(initialPage));

        // Mock behaviors
        when(paginatedData.getRequestBuilder()).thenReturn(requestBuilder);

        // Test the page pagination
        PagePagination page = new PagePagination("$request.headers#/page");
        Builder nextRequestBuilder = page.apply(paginatedData);
        
        // Verify results
        assertNotNull(nextRequestBuilder);
        
        // Verify page increment
        AtomicReference<Object> pageValue = new AtomicReference<>();
        nextRequestBuilder.updateByReference("$request.headers#/page", v -> {
            pageValue.set(v);
            return v;
        });
        
        assertEquals(nextPage, pageValue.get());
    }


    @Test
    public void testWithValidPageTemplateReturnsTrue() {
        PaginatedData<?, ?, ?, ?> paginatedData = mock(PaginatedData.class);
        final int initialPage = 3;
        final int nextPage = 4;

        when(paginatedData.getRequestBuilder())
                .thenReturn(new HttpRequest.Builder().templateParam(
                        t -> t.key("page").value(initialPage)));

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
        final int initialPage = 3;
        final int nextPage = 4;

        when(paginatedData.getRequestBuilder())
                .thenReturn(new HttpRequest.Builder().queryParam(
                        q -> q.key("page").value(initialPage)));

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

        Map<String, String> pageValue = new HashMap<>();
        pageValue.put("val", "1");

        when(paginatedData.getRequestBuilder()).thenReturn(
                new HttpRequest.Builder().queryParam(q -> q.key("page").value(pageValue)));

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
        final String current = "5";
        final int next = 6;

        when(paginatedData.getRequestBuilder())
                .thenReturn(new HttpRequest.Builder().queryParam(
                        q -> q.key("page").value(current)));

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

        when(paginatedData.getRequestBuilder())
                .thenReturn(new HttpRequest.Builder().queryParam(q -> q.key("page").value("5a")));

        PagePagination page = new PagePagination("$request.query#/page");

        Builder requestBuilder = page.apply(paginatedData);
        assertNotNull(requestBuilder);

        requestBuilder.updateByReference("$request.query#/page", v -> {
            assertEquals("5a", v);
            return v;
        });
    }


    @Test
    public void testWithMissingPageReturnsFalse() {
        PaginatedData<?, ?, ?, ?> paginatedData = mock(PaginatedData.class);

        when(paginatedData.getRequestBuilder()).thenReturn(new HttpRequest.Builder());

        PagePagination page = new PagePagination("$request.query#/page");

        Builder requestBuilder = page.apply(paginatedData);
        assertNotNull(requestBuilder);

        requestBuilder.updateByReference("$request.query#/page", v -> {
            fail("Should not be called because page is missing");
            return v;
        });
    }


    @Test
    public void testWithNullPageReturnsFalse() {
        PaginatedData<?, ?, ?, ?> paginatedData = mock(PaginatedData.class);
        final int current = 5;

        when(paginatedData.getRequestBuilder())
                .thenReturn(new HttpRequest.Builder().queryParam(
                        q -> q.key("page").value(current)));

        PagePagination page = new PagePagination(null);

        Builder requestBuilder = page.apply(paginatedData);
        assertNotNull(requestBuilder);

        requestBuilder.updateByReference("$request.query#/page", v -> {
            assertEquals(current, v); // No update expected; should return current value
            return v;
        });
    }

}
