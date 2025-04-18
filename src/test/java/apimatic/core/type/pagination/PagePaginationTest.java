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

public class PagePaginationTest {
    @Rule
    public MockitoRule initRule = MockitoJUnit.rule().silent();

    @Test
    public void testWithValidPageHeader_returnsTrue() {
        PaginatedData<?, ?> paginatedData = mock(PaginatedData.class);

        when(paginatedData.getLastRequestBuilder())
                .thenReturn(new HttpRequest.Builder().headerParam(h -> h.key("page").value(3)));

        PagePagination page = new PagePagination("$request.headers#/page");

        assertTrue(page.isValid(paginatedData));
        assertNotNull(page.getNextRequestBuilder());

        page.getNextRequestBuilder().updateByReference("$request.headers#/page", v -> {
            assertEquals(4, v);
            return v;
        });
    }

    @Test
    public void testWithValidPageTemplate_returnsTrue() {
        PaginatedData<?, ?> paginatedData = mock(PaginatedData.class);

        when(paginatedData.getLastRequestBuilder())
                .thenReturn(new HttpRequest.Builder().templateParam(t -> t.key("page").value(3)));

        PagePagination page = new PagePagination("$request.path#/page");

        assertTrue(page.isValid(paginatedData));
        assertNotNull(page.getNextRequestBuilder());

        page.getNextRequestBuilder().updateByReference("$request.path#/page", v -> {
            assertEquals(4, v);
            return v;
        });
    }

    @Test
    public void testWithValidPage_returnsTrue() {
        PaginatedData<?, ?> paginatedData = mock(PaginatedData.class);

        when(paginatedData.getLastRequestBuilder())
                .thenReturn(new HttpRequest.Builder().queryParam(q -> q.key("page").value(3)));

        PagePagination page = new PagePagination("$request.query#/page");

        assertTrue(page.isValid(paginatedData));
        assertNotNull(page.getNextRequestBuilder());

        page.getNextRequestBuilder().updateByReference("$request.query#/page", v -> {
            assertEquals(4, v);
            return v;
        });
    }

    @Test
    public void testWithValidPageAsInnerField_returnsTrue() {
        PaginatedData<?, ?> paginatedData = mock(PaginatedData.class);

        when(paginatedData.getLastRequestBuilder()).thenReturn(
                new HttpRequest.Builder().queryParam(q -> q.key("page").value(new HashMap<String, String>() {
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
    public void testWithValidStringPage_returnsTrue() {
        PaginatedData<?, ?> paginatedData = mock(PaginatedData.class);

        when(paginatedData.getLastRequestBuilder())
                .thenReturn(new HttpRequest.Builder().queryParam(q -> q.key("page").value("5")));

        PagePagination page = new PagePagination("$request.query#/page");

        assertTrue(page.isValid(paginatedData));
        assertNotNull(page.getNextRequestBuilder());

        page.getNextRequestBuilder().updateByReference("$request.query#/page", v -> {
            assertEquals(6, v);
            return v;
        });
    }

    @Test
    public void testWithInValidStringPage_returnsFalse() {
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
    public void testWithMissingPage_returnsFalse() {
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
    public void testWithNullPage_returnsFalse() {
        PaginatedData<?, ?> paginatedData = mock(PaginatedData.class);

        when(paginatedData.getLastRequestBuilder())
                .thenReturn(new HttpRequest.Builder().queryParam(q -> q.key("page").value(5)));

        PagePagination page = new PagePagination(null);

        assertFalse(page.isValid(paginatedData));
        assertNotNull(page.getNextRequestBuilder());

        page.getNextRequestBuilder().updateByReference("$request.query#/page", v -> {
            assertEquals(5, v);
            return v;
        });
    }

}
