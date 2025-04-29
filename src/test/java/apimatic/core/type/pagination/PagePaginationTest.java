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
        PaginatedData<?, ?> paginatedData = mock(PaginatedData.class);
        final int initialPage = 3;
        final int nextPage = 4;

        when(paginatedData.getLastRequestBuilder())
                .thenReturn(new HttpRequest.Builder().headerParam(
                        h -> h.key("page").value(initialPage)));

        PagePagination page = new PagePagination("$request.headers#/page");

        assertTrue(page.isValid(paginatedData));
        assertNotNull(page.getNextRequestBuilder());

        page.getNextRequestBuilder().updateByReference("$request.headers#/page", v -> {
            assertEquals(nextPage, v);
            return v;
        });
    }

    @Test
    public void testWithValidPageTemplateReturnsTrue() {
        PaginatedData<?, ?> paginatedData = mock(PaginatedData.class);
        final int initialPage = 3;
        final int nextPage = 4;

        when(paginatedData.getLastRequestBuilder())
                .thenReturn(new HttpRequest.Builder().templateParam(
                        t -> t.key("page").value(initialPage)));

        PagePagination page = new PagePagination("$request.path#/page");

        assertTrue(page.isValid(paginatedData));
        assertNotNull(page.getNextRequestBuilder());

        page.getNextRequestBuilder().updateByReference("$request.path#/page", v -> {
            assertEquals(nextPage, v);
            return v;
        });
    }

    @Test
    public void testWithValidPageReturnsTrue() {
        PaginatedData<?, ?> paginatedData = mock(PaginatedData.class);
        final int initialPage = 3;
        final int nextPage = 4;

        when(paginatedData.getLastRequestBuilder())
                .thenReturn(new HttpRequest.Builder().queryParam(
                        q -> q.key("page").value(initialPage)));

        PagePagination page = new PagePagination("$request.query#/page");

        assertTrue(page.isValid(paginatedData));
        assertNotNull(page.getNextRequestBuilder());

        page.getNextRequestBuilder().updateByReference("$request.query#/page", v -> {
            assertEquals(nextPage, v);
            return v;
        });
    }

    @Test
    public void testWithValidPageAsInnerFieldReturnsTrue() {
        PaginatedData<?, ?> paginatedData = mock(PaginatedData.class);

        when(paginatedData.getLastRequestBuilder()).thenReturn(
                new HttpRequest.Builder().queryParam(q -> q.key("page")
                        .value(new HashMap<String, String>() {
                    private static final long serialVersionUID = 1L;
                    {
                        put("val", "1");
                    }
                })));

        PagePagination page = new PagePagination("$request.query#/page/val");

        assertTrue(page.isValid(paginatedData));
        assertNotNull(page.getNextRequestBuilder());

        page.getNextRequestBuilder().updateByReference("$request.query#/page/val", v -> {
            assertEquals(2, v);
            return v;
        });
    }

    @Test
    public void testWithValidStringPageReturnsTrue() {
        PaginatedData<?, ?> paginatedData = mock(PaginatedData.class);
        final String current = "5";
        final int next = 6;

        when(paginatedData.getLastRequestBuilder())
                .thenReturn(new HttpRequest.Builder().queryParam(
                        q -> q.key("page").value(current)));

        PagePagination page = new PagePagination("$request.query#/page");

        assertTrue(page.isValid(paginatedData));
        assertNotNull(page.getNextRequestBuilder());

        page.getNextRequestBuilder().updateByReference("$request.query#/page", v -> {
            assertEquals(next, v);
            return v;
        });
    }

    @Test
    public void testWithInvalidStringPageReturnsFalse() {
        PaginatedData<?, ?> paginatedData = mock(PaginatedData.class);

        when(paginatedData.getLastRequestBuilder())
                .thenReturn(new HttpRequest.Builder().queryParam(q -> q.key("page").value("5a")));

        PagePagination page = new PagePagination("$request.query#/page");

        assertFalse(page.isValid(paginatedData));
        assertNotNull(page.getNextRequestBuilder());

        page.getNextRequestBuilder().updateByReference("$request.query#/page", v -> {
            assertEquals("5a", v);
            return v;
        });
    }

    @Test
    public void testWithMissingPageReturnsFalse() {
        PaginatedData<?, ?> paginatedData = mock(PaginatedData.class);

        when(paginatedData.getLastRequestBuilder()).thenReturn(new HttpRequest.Builder());

        PagePagination page = new PagePagination("$request.query#/page");

        assertFalse(page.isValid(paginatedData));
        assertNotNull(page.getNextRequestBuilder());

        page.getNextRequestBuilder().updateByReference("$request.query#/page", v -> {
            fail();
            return v;
        });
    }

    @Test
    public void testWithNullPageReturnsFalse() {
        PaginatedData<?, ?> paginatedData = mock(PaginatedData.class);
        final int current = 5;

        when(paginatedData.getLastRequestBuilder())
                .thenReturn(new HttpRequest.Builder().queryParam(
                        q -> q.key("page").value(current)));

        PagePagination page = new PagePagination(null);

        assertFalse(page.isValid(paginatedData));
        assertNotNull(page.getNextRequestBuilder());

        page.getNextRequestBuilder().updateByReference("$request.query#/page", v -> {
            assertEquals(current, v);
            return v;
        });
    }
}
