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
import io.apimatic.core.types.pagination.PageWrapper;
import io.apimatic.core.types.pagination.PaginatedData;
import io.apimatic.coreinterfaces.http.HttpHeaders;
import io.apimatic.coreinterfaces.http.response.Response;

/**
 * Unit tests for the LinkPagination class.
 */
public class LinkPaginationTest {

    private static final String RESPONSE_BODY_POINTER = "$response.body#/next";
    private static final String RESPONSE_HEADERS_POINTER = "$response.headers#/next";
    private static final String REQUEST_QUERY_PAGE = "$request.query#/page";
    private static final String REQUEST_QUERY_SIZE = "$request.query#/size";
    private static final String REQUEST_HEADERS_PAGE = "$request.headers#/page";
    private static final String NEXT_URL_SINGLE = "https://api.example.com?page=2";
    private static final String NEXT_URL_MULTIPLE = "https://api.example.com?page=2&size=5";
    private static final String NEXT_URL_ENCODED = "https://api.example.com?page%20o=2%20a&size%20q=5^%214$#";
    private static final String PAGE = "page";
    private static final String SIZE = "size";
    private static final String NEXT = "next";

    /**
     * Silent rule for Mockito initialization.
     */
    @Rule
    public MockitoRule initRule = MockitoJUnit.rule().silent();

    @Test
    public void testValidLink() {
        PaginatedData<?, ?, ?, ?> paginatedData = mock(PaginatedData.class);
        Response response = mock(Response.class);

        when(paginatedData.getRequestBuilder()).thenReturn(new HttpRequest.Builder());
        when(paginatedData.getResponse()).thenReturn(response);
        when(response.getBody()).thenReturn("{\"next\": \"" + NEXT_URL_SINGLE + "\"}");

        LinkPagination link = new LinkPagination(RESPONSE_BODY_POINTER);

        Builder requestBuilder = link.apply(paginatedData);
        assertNotNull(requestBuilder);

        PageWrapper<?, ?> pageWrapper = PageWrapper.Create(response, null, null);
        link.addMetaData(pageWrapper);
        assertEquals(NEXT_URL_SINGLE, pageWrapper.getNextLinkInput());
    }

    @Test
    public void testValidLinkWithAdditionalParams() {
        PaginatedData<?, ?, ?, ?> paginatedData = mock(PaginatedData.class);
        Response response = mock(Response.class);
        final int pageSize = 456;

        when(paginatedData.getRequestBuilder()).thenReturn(new HttpRequest.Builder()
                .queryParam(q -> q.key(SIZE).value(pageSize))
                .queryParam(q -> q.key(PAGE).value(1))
                .headerParam(h -> h.key(PAGE).value(2)));

        when(paginatedData.getResponse()).thenReturn(response);
        when(response.getBody()).thenReturn("{\"next\": \"" + NEXT_URL_SINGLE + "\"}");

        LinkPagination link = new LinkPagination(RESPONSE_BODY_POINTER);

        Builder requestBuilder = link.apply(paginatedData);
        assertNotNull(requestBuilder);

        PageWrapper<?, ?> pageWrapper = PageWrapper.Create(response, null, null);
        link.addMetaData(pageWrapper);
        assertEquals(NEXT_URL_SINGLE, pageWrapper.getNextLinkInput());
    }

    @Test
    public void testValidLinkFromHeader() {
        PaginatedData<?, ?, ?, ?> paginatedData = mock(PaginatedData.class);
        Response response = mock(Response.class);
        Map<String, String> headers = new HashMap<>();
        headers.put(NEXT, NEXT_URL_SINGLE);

        when(paginatedData.getRequestBuilder()).thenReturn(new HttpRequest.Builder());
        when(paginatedData.getResponse()).thenReturn(response);
        when(response.getHeaders()).thenReturn(createHttpHeaders(headers));

        LinkPagination link = new LinkPagination(RESPONSE_HEADERS_POINTER);
        Builder requestBuilder = link.apply(paginatedData);

        assertNotNull(requestBuilder);

        PageWrapper<?, ?> pageWrapper = PageWrapper.Create(response, null, null);
        link.addMetaData(pageWrapper);
        assertEquals(NEXT_URL_SINGLE, pageWrapper.getNextLinkInput());
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
    public void testInvalidPointer() {
        PaginatedData<?, ?, ?, ?> paginatedData = mock(PaginatedData.class);
        Response response = mock(Response.class);

        when(paginatedData.getRequestBuilder()).thenReturn(new HttpRequest.Builder());
        when(paginatedData.getResponse()).thenReturn(response);
        LinkPagination link = new LinkPagination("$response.body#/next/href");

        assertNull(link.apply(paginatedData));
    }

    @Test
    public void testMissingResponse() {
        PaginatedData<?, ?, ?, ?> paginatedData = mock(PaginatedData.class);
        Response response = mock(Response.class);

        when(paginatedData.getRequestBuilder()).thenReturn(new HttpRequest.Builder());
        when(paginatedData.getResponse()).thenReturn(response);
        LinkPagination link = new LinkPagination("$response.body#/next/href");

        assertNull(link.apply(paginatedData));
    }

    @Test
    public void testMissingPointer() {
        PaginatedData<?, ?, ?, ?> paginatedData = mock(PaginatedData.class);
        Response response = mock(Response.class);

        when(paginatedData.getRequestBuilder()).thenReturn(new HttpRequest.Builder());
        when(paginatedData.getResponse()).thenReturn(response);
        LinkPagination link = new LinkPagination(null);

        assertNull(link.apply(paginatedData));
    }

    @Test
    public void testMultipleQueryParams() {
        PaginatedData<?, ?, ?, ?> paginatedData = mock(PaginatedData.class);
        Response response = mock(Response.class);

        when(paginatedData.getRequestBuilder()).thenReturn(new HttpRequest.Builder());
        when(paginatedData.getResponse()).thenReturn(response);
        when(response.getBody())
                .thenReturn("{\"next\": \"" + NEXT_URL_MULTIPLE + "\"}");

        LinkPagination link = new LinkPagination(RESPONSE_BODY_POINTER);

        Builder nextBuilder = link.apply(paginatedData);
        assertNotNull(nextBuilder);

        PageWrapper<?, ?> pageWrapper = PageWrapper.Create(response, null, null);
        link.addMetaData(pageWrapper);
        assertEquals(NEXT_URL_MULTIPLE, pageWrapper.getNextLinkInput());
    }

    @Test
    public void testEncodedQueryParams() {
        PaginatedData<?, ?, ?, ?> paginatedData = mock(PaginatedData.class);
        Response response = mock(Response.class);

        when(paginatedData.getRequestBuilder()).thenReturn(new HttpRequest.Builder());
        when(paginatedData.getResponse()).thenReturn(response);
        when(response.getBody())
                .thenReturn("{\"next\": \"" + NEXT_URL_ENCODED + "\"}");

        LinkPagination link = new LinkPagination(RESPONSE_BODY_POINTER);

        Builder builder = link.apply(paginatedData);
        assertNotNull(builder);

        PageWrapper<?, ?> pageWrapper = PageWrapper.Create(response, null, null);
        link.addMetaData(pageWrapper);
        assertEquals(NEXT_URL_ENCODED, pageWrapper.getNextLinkInput());
    }
}
