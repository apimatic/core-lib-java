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
import io.apimatic.core.types.pagination.LinkPagination;
import io.apimatic.core.types.pagination.PaginatedData;

public class LinkPaginationTest {
    @Rule
    public MockitoRule initRule = MockitoJUnit.rule().silent();

    @Test
    public void testWithValidLink_returnsTrue() {
        PaginatedData<?, ?> paginatedData = mock(PaginatedData.class);

        when(paginatedData.getLastRequestBuilder()).thenReturn(new HttpRequest.Builder());
        when(paginatedData.getLastResponseBody()).thenReturn("{\"next\": \"https://api.example.com?page=2\"}");

        LinkPagination link = new LinkPagination("$response.body#/next");
        
        assertTrue(link.isValid(paginatedData));
        assertNotNull(link.getNextRequestBuilder());

        link.getNextRequestBuilder().updateByReference("$request.query#/page", v -> 
        {
            assertEquals("2", v);
            return v;
        });
    }

    @Test
    public void testWithValidLinkImpactingOtherParams_returnsTrue() {
        PaginatedData<?, ?> paginatedData = mock(PaginatedData.class);

        when(paginatedData.getLastRequestBuilder()).thenReturn(new HttpRequest.Builder()
                .queryParam(q -> q.key("size").value(23))
                .queryParam(q -> q.key("page").value(1))
                .headerParam(h -> h.key("page").value(2)));
        when(paginatedData.getLastResponseBody()).thenReturn("{\"next\": \"https://api.example.com?page=2\"}");

        LinkPagination link = new LinkPagination("$response.body#/next");
        
        assertTrue(link.isValid(paginatedData));
        assertNotNull(link.getNextRequestBuilder());

        link.getNextRequestBuilder().updateByReference("$request.query#/page", v -> 
        {
            assertEquals("2", v);
            return v;
        });

        link.getNextRequestBuilder().updateByReference("$request.query#/size", v -> 
        {
            assertEquals(23, v);
            return v;
        });

        link.getNextRequestBuilder().updateByReference("$request.headers#/page", v -> 
        {
            assertEquals(2, v);
            return v;
        });
    }

    @Test
    public void testWithValidLinkFromResponseHeader_returnsTrue() {
        PaginatedData<?, ?> paginatedData = mock(PaginatedData.class);

        when(paginatedData.getLastRequestBuilder()).thenReturn(new HttpRequest.Builder());
        when(paginatedData.getLastResponseHeaders()).thenReturn("{\"next\": \"https://api.example.com?page=2\"}");

        LinkPagination link = new LinkPagination("$response.headers#/next");
        
        assertTrue(link.isValid(paginatedData));
        assertNotNull(link.getNextRequestBuilder());

        link.getNextRequestBuilder().updateByReference("$request.query#/page", v -> 
        {
            assertEquals("2", v);
            return v;
        });
    }

    @Test
    public void testWithInValidLinkPointer_returnsFalse() {
        PaginatedData<?, ?> paginatedData = mock(PaginatedData.class);

        when(paginatedData.getLastRequestBuilder()).thenReturn(new HttpRequest.Builder());
        when(paginatedData.getLastResponseBody()).thenReturn("{\"next\": \"https://api.example.com?page=2\"}");

        LinkPagination link = new LinkPagination("$response.body#/next/href");
        
        assertFalse(link.isValid(paginatedData));
        assertNotNull(link.getNextRequestBuilder());

        link.getNextRequestBuilder().updateByReference("$request.query#/page", v -> 
        {
            fail();
            return v;
        });
    }

    @Test
    public void testWithMissingResponse_returnsFalse() {
        PaginatedData<?, ?> paginatedData = mock(PaginatedData.class);

        when(paginatedData.getLastRequestBuilder()).thenReturn(new HttpRequest.Builder());
        when(paginatedData.getLastResponseBody()).thenReturn(null);

        LinkPagination link = new LinkPagination("$response.body#/next/href");
        
        assertFalse(link.isValid(paginatedData));
        assertNotNull(link.getNextRequestBuilder());

        link.getNextRequestBuilder().updateByReference("$request.query#/page", v -> 
        {
            fail();
            return v;
        });
    }

    @Test
    public void testWithMissingLinkPointer_returnsFalse() {
        PaginatedData<?, ?> paginatedData = mock(PaginatedData.class);

        when(paginatedData.getLastRequestBuilder()).thenReturn(new HttpRequest.Builder());
        when(paginatedData.getLastResponseBody()).thenReturn("{\"next\": \"https://api.example.com?page=2\"}");

        LinkPagination link = new LinkPagination(null);
        
        assertFalse(link.isValid(paginatedData));
        assertNotNull(link.getNextRequestBuilder());

        link.getNextRequestBuilder().updateByReference("$request.query#/page", v -> 
        {
            fail();
            return v;
        });
    }

    @Test
    public void testWithMultipleQueryParams_returnsTrue() {
        PaginatedData<?, ?> paginatedData = mock(PaginatedData.class);

        when(paginatedData.getLastRequestBuilder()).thenReturn(new HttpRequest.Builder());
        when(paginatedData.getLastResponseBody()).thenReturn("{\"next\": \"https://api.example.com?page=2&size=5\"}");

        LinkPagination link = new LinkPagination("$response.body#/next");
        
        assertTrue(link.isValid(paginatedData));
        assertNotNull(link.getNextRequestBuilder());

        link.getNextRequestBuilder().updateByReference("$request.query#/page", v -> 
        {
            assertEquals("2", v);
            return v;
        });

        link.getNextRequestBuilder().updateByReference("$request.query#/size", v -> 
        {
            assertEquals("5", v);
            return v;
        });
    }

    @Test
    public void testWithMultipleEncodedQueryParams_returnsTrue() {
        PaginatedData<?, ?> paginatedData = mock(PaginatedData.class);

        when(paginatedData.getLastRequestBuilder()).thenReturn(new HttpRequest.Builder());
        when(paginatedData.getLastResponseBody()).thenReturn("{\"next\": \"https://api.example.com?page%20o=2%20a&size%20q=5^%214$#\"}");

        LinkPagination link = new LinkPagination("$response.body#/next");
        
        assertTrue(link.isValid(paginatedData));
        assertNotNull(link.getNextRequestBuilder());

        link.getNextRequestBuilder().updateByReference("$request.query#/page o", v -> 
        {
            assertEquals("2 a", v);
            return v;
        });

        link.getNextRequestBuilder().updateByReference("$request.query#/size q", v -> 
        {
            assertEquals("5^!4$#", v);
            return v;
        });
    }

}
