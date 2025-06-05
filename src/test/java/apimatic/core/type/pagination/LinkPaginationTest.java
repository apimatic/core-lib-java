package apimatic.core.type.pagination;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import io.apimatic.core.HttpRequest;
import io.apimatic.core.HttpRequest.Builder;
import io.apimatic.core.types.pagination.LinkPagination;
import io.apimatic.core.types.pagination.PaginatedData;
import io.apimatic.coreinterfaces.http.HttpHeaders;
import io.apimatic.coreinterfaces.http.response.Response;

/**
 * Unit tests for the LinkPagination class.
 */
public class LinkPaginationTest {

    /**
     * Silent rule for Mockito initialization.
     */
    @Rule
    public MockitoRule initRule = MockitoJUnit.rule().silent();

    @Test
    public void testValidLinkReturnsTrue() {
        PaginatedData<?, ?, ?, ?> paginatedData = mock(PaginatedData.class);
        Response response = mock(Response.class);

        when(paginatedData.getRequestBuilder()).thenReturn(new HttpRequest.Builder());
        when(paginatedData.getResponse()).thenReturn(response);
        when(response.getBody()).thenReturn("{\"next\": \"https://api.example.com?page=2\"}");

        LinkPagination link = new LinkPagination("$response.body#/next");

        Builder requestBuilder = link.apply(paginatedData);
        assertNotNull(requestBuilder);

        requestBuilder.updateByReference("$request.query#/page", v -> {
            assertEquals("2", v);
            return v;
        });
    }

    @Test
    public void testValidLinkWithAdditionalParamsReturnsTrue() {
        PaginatedData<?, ?, ?, ?> paginatedData = mock(PaginatedData.class);
        Response response = mock(Response.class);
        final int pageSize = 456;

        when(paginatedData.getRequestBuilder()).thenReturn(new HttpRequest.Builder()
                .queryParam(q -> q.key("size").value(pageSize))
                .queryParam(q -> q.key("page").value(1))
                .headerParam(h -> h.key("page").value(2)));

        when(paginatedData.getResponse()).thenReturn(response);
        when(response.getBody()).thenReturn("{\"next\": \"https://api.example.com?page=2\"}");

        LinkPagination link = new LinkPagination("$response.body#/next");

        Builder requestBuilder = link.apply(paginatedData);
        assertNotNull(requestBuilder);

        requestBuilder.updateByReference("$request.query#/page", v -> {
            assertEquals("2", v);
            return v;
        });

        requestBuilder.updateByReference("$request.query#/size", v -> {
            assertEquals(pageSize, v);
            return v;
        });

        requestBuilder.updateByReference("$request.headers#/page", v -> {
            assertEquals(2, v);
            return v;
        });
    }

    @Test
    public void testValidLinkFromHeaderReturnsTrue() {
        // Setup mocks
        PaginatedData<?, ?, ?, ?> paginatedData = mock(PaginatedData.class);
        Response response = mock(Response.class);
        Map<String, String> headers = new HashMap<>();
        headers.put("next", "https://api.example.com?page=2");

        when(paginatedData.getRequestBuilder()).thenReturn(new HttpRequest.Builder());
        when(paginatedData.getResponse()).thenReturn(response);
        when(response.getHeaders()).thenReturn(createHttpHeaders(headers));

        // Test the link pagination
        LinkPagination link = new LinkPagination("$response.headers#/next");
        Builder requestBuilder = link.apply(paginatedData);

        // Verify results
        assertNotNull(requestBuilder);
        requestBuilder.updateByReference("$request.query#/page", v -> {
            assertEquals("2", v); // Page extracted from URL query param
            return v;
        });
    }

	private HttpHeaders createHttpHeaders(Map<String, String> headers) {
		return new HttpHeaders() {
			
			@Override
			public List<String> values(String headerName) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public String value(String headerName) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public List<String> remove(String headerName) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Set<String> names() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public boolean has(String headerName) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public Map<String, String> asSimpleMap() {
				// TODO Auto-generated method stub
				return headers;
			}
			
			@Override
			public Map<String, List<String>> asMultimap() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public void addAllFromMultiMap(Map<String, List<String>> headers) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void addAllFromMap(Map<String, String> headers) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void addAll(HttpHeaders headers) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void add(String headerName, List<String> values) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void add(String headerName, String value) {
				// TODO Auto-generated method stub
				
			}
		};
	}

    
    @Test
    public void testInvalidPointerReturnsFalse() {
        PaginatedData<?, ?, ?, ?> paginatedData = mock(PaginatedData.class);
        Response response = mock(Response.class);

        when(paginatedData.getRequestBuilder()).thenReturn(new HttpRequest.Builder());
        when(paginatedData.getResponse()).thenReturn(response);
        when(response.getBody()).thenReturn("{\"next\": \"https://api.example.com?page=2\"}");

        LinkPagination link = new LinkPagination("$response.body#/next/href");

        // Since pointer is invalid, apply(...) should return null
        assertNull(link.apply(paginatedData));
    }


    @Test
    public void testMissingResponseReturnsFalse() {
        PaginatedData<?, ?, ?, ?> paginatedData = mock(PaginatedData.class);
        Response response = mock(Response.class);

        when(paginatedData.getRequestBuilder()).thenReturn(new HttpRequest.Builder());
        when(paginatedData.getResponse()).thenReturn(response);
        when(response.getBody()).thenReturn(null);

        LinkPagination link = new LinkPagination("$response.body#/next/href");

        // Since response body is null, apply should return null
        assertNull(link.apply(paginatedData));
    }


    @Test
    public void testMissingPointerReturnsFalse() {
        PaginatedData<?, ?, ?, ?> paginatedData = mock(PaginatedData.class);
        Response response = mock(Response.class);

        when(paginatedData.getRequestBuilder()).thenReturn(new HttpRequest.Builder());
        when(paginatedData.getResponse()).thenReturn(response);
        when(response.getBody()).thenReturn("{\"next\": \"https://api.example.com?page=2\"}");

        LinkPagination link = new LinkPagination(null);

        // Pointer is null, apply should return null
        assertNull(link.apply(paginatedData));
    }

    @Test
    public void testMultipleQueryParamsReturnsTrue() {
        PaginatedData<?, ?, ?, ?> paginatedData = mock(PaginatedData.class);
        Response response = mock(Response.class);

        when(paginatedData.getRequestBuilder()).thenReturn(new HttpRequest.Builder());
        when(paginatedData.getResponse()).thenReturn(response);
        when(response.getBody())
                .thenReturn("{\"next\": \"https://api.example.com?page=2&size=5\"}");

        LinkPagination link = new LinkPagination("$response.body#/next");

        Builder nextBuilder = link.apply(paginatedData);
        assertNotNull(nextBuilder);

        nextBuilder.updateByReference("$request.query#/page", v -> {
            assertEquals("2", v);
            return v;
        });

        nextBuilder.updateByReference("$request.query#/size", v -> {
            assertEquals("5", v);
            return v;
        });
    }


    @Test
    public void testEncodedQueryParamsReturnsTrue() {
        PaginatedData<?, ?, ?, ?> paginatedData = mock(PaginatedData.class);
        Response response = mock(Response.class);

        when(paginatedData.getRequestBuilder()).thenReturn(new HttpRequest.Builder());
        when(paginatedData.getResponse()).thenReturn(response);
        when(response.getBody())
                .thenReturn("{\"next\": \"https://api.example.com?page%20o=2%20a&size%20q=5^%214$#\"}");

        LinkPagination link = new LinkPagination("$response.body#/next");

        Builder builder = link.apply(paginatedData);
        assertNotNull(builder);

        builder.updateByReference("$request.query#/page o", v -> {
            assertEquals("2 a", v);
            return v;
        });

        builder.updateByReference("$request.query#/size q", v -> {
            assertEquals("5^!4$#", v);
            return v;
        });
    }

}
