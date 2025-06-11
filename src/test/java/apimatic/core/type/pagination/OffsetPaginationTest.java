package apimatic.core.type.pagination;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import io.apimatic.core.HttpRequest;
import io.apimatic.core.HttpRequest.Builder;
import io.apimatic.core.types.pagination.OffsetPagination;
import io.apimatic.core.types.pagination.PageWrapper;
import io.apimatic.core.types.pagination.PaginatedData;
import io.apimatic.coreinterfaces.http.response.Response;
import io.apimatic.coreinterfaces.type.CoreFileWrapper;

/**
 * Unit tests for {@link OffsetPagination}.
 */
public class OffsetPaginationTest {
    private static final int INITIAL_OFFSET = 3;
    private static final String INITIAL_OFFSET_STRING = "3";
    private static final int PAGE_SIZE = 100;
    private static final int OFFSET_PLUS_PAGE = INITIAL_OFFSET + PAGE_SIZE; 
    private static final int OFFSET_VAL = 1;
    private static final int OFFSET_VAL_PLUS_ONE = OFFSET_VAL + PAGE_SIZE;   
    private static final int OFFSET_STRING = 5;
    private static final int OFFSET_STRING_PLUS_PAGE = OFFSET_STRING + PAGE_SIZE;
    private static final int NUMERIC_OFFSET = 5;
    private static final int INVALID_OFFSET_RESULT = -1;
    private static final String INVALID_OFFSET_STRING = "5a";

    /**
     * JUnit rule to initialize Mockito annotations.
     */
    @Rule
    public MockitoRule initRule = MockitoJUnit.rule().silent();

    @Test
    public void testValidOffsetHeader() {
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
        PageWrapper<?, ?> pageWrapper = PageWrapper.Create(response, null, null);
        offset.addMetaData(pageWrapper);
        assertEquals(OFFSET_PLUS_PAGE, pageWrapper.getOffsetInput());
    }   
    
    @Test
    public void testBodyWithTypeCoreFileWrapper() {
        PaginatedData<?, ?, ?, ?> paginatedData = mock(PaginatedData.class);
        Response response = mock(Response.class);
        // Initial body to be serialized
        CoreFileWrapper initialBody = new CoreFileWrapper(){

			@Override
			public File getFile() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String getContentType() {
				// TODO Auto-generated method stub
				return null;
			}};
        // Build request with bodySerializer instead of bodyParam
        HttpRequest.Builder builder = new HttpRequest.Builder()
            .bodyParam(b -> b.value(initialBody));
        when(paginatedData.getRequestBuilder()).thenReturn(builder);
        when(paginatedData.getResponse()).thenReturn(response);
        when(paginatedData.getPageSize()).thenReturn(PAGE_SIZE);
        // OffsetPagination that will hit the bodySerializer branch (point == "")
        OffsetPagination offset = new OffsetPagination("$request.body");
        HttpRequest.Builder requestBuilder = offset.apply(paginatedData);
        assertNull(requestBuilder);

    }


    @Test
    public void testValidOffsetWithBodySerializerIOException() {
        PaginatedData<?, ?, ?, ?> paginatedData = mock(PaginatedData.class);
        Response response = mock(Response.class);

        // Create a body that is not CoreFileWrapper
        Object body = new Object();

        HttpRequest.Builder builder = new HttpRequest.Builder()
            .bodyParam(b -> b.value(body))
            .bodySerializer(() -> { 
                throw new IOException("Simulated IOException");
            });

        when(paginatedData.getRequestBuilder()).thenReturn(builder);
        when(paginatedData.getResponse()).thenReturn(response);
        when(paginatedData.getPageSize()).thenReturn(PAGE_SIZE);

        // Use the pointer that will hit the bodySerializer branch
        OffsetPagination offset = new OffsetPagination("$request.body");

        // The test passes if no exception is thrown and method completes
        HttpRequest.Builder requestBuilder = offset.apply(paginatedData);

        assertNull(requestBuilder);
    }

    
    @Test
    public void testValidOffsetWithBodySerializer() {
        PaginatedData<?, ?, ?, ?> paginatedData = mock(PaginatedData.class);
        Response response = mock(Response.class);
        // Initial body to be serialized
        String initialBody = INITIAL_OFFSET_STRING;
        // Build request with bodySerializer instead of bodyParam
        HttpRequest.Builder builder = new HttpRequest.Builder()
            .bodyParam(b -> b.value(INITIAL_OFFSET))
            .bodySerializer(() -> initialBody);
        when(paginatedData.getRequestBuilder()).thenReturn(builder);
        when(paginatedData.getResponse()).thenReturn(response);
        when(paginatedData.getPageSize()).thenReturn(PAGE_SIZE);
        // OffsetPagination that will hit the bodySerializer branch (point == "")
        OffsetPagination offset = new OffsetPagination("$request.body");
        HttpRequest.Builder requestBuilder = offset.apply(paginatedData);
        assertNotNull(requestBuilder);
        // Verify metadata update correctly
        PageWrapper<?, ?> pageWrapper = PageWrapper.Create(response, null, null);
        offset.addMetaData(pageWrapper);
        assertEquals(OFFSET_PLUS_PAGE, pageWrapper.getOffsetInput());
    }
   
    @Test
    public void testValidOffsetSingleBodyParamIntType() {
        PaginatedData<?, ?, ?, ?> paginatedData = mock(PaginatedData.class);
        Response response = mock(Response.class);

        HttpRequest.Builder builder = new HttpRequest.Builder()
            .bodyParam(b -> b.value(INITIAL_OFFSET)); 

        when(paginatedData.getRequestBuilder()).thenReturn(builder);
        when(paginatedData.getResponse()).thenReturn(response);
        when(paginatedData.getPageSize()).thenReturn(PAGE_SIZE);

        OffsetPagination offset = new OffsetPagination("$request.body");
        HttpRequest.Builder requestBuilder = offset.apply(paginatedData);
        assertNotNull(requestBuilder);
        
        // Verify metadata update correctly
        PageWrapper<?, ?> pageWrapper = PageWrapper.Create(response, null, null);
        offset.addMetaData(pageWrapper);
        assertEquals(OFFSET_PLUS_PAGE, pageWrapper.getOffsetInput());
    }
    
    @Test
    public void testValidOffsetSingleBodyParamStringType() {
        PaginatedData<?, ?, ?, ?> paginatedData = mock(PaginatedData.class);
        Response response = mock(Response.class);

        HttpRequest.Builder builder = new HttpRequest.Builder()
            .bodyParam(b -> b.value(INITIAL_OFFSET_STRING)); 

        when(paginatedData.getRequestBuilder()).thenReturn(builder);
        when(paginatedData.getResponse()).thenReturn(response);
        when(paginatedData.getPageSize()).thenReturn(PAGE_SIZE);

        OffsetPagination offset = new OffsetPagination("$request.body");
        HttpRequest.Builder requestBuilder = offset.apply(paginatedData);
        assertNotNull(requestBuilder);
        
        // Verify metadata update correctly
        PageWrapper<?, ?> pageWrapper = PageWrapper.Create(response, null, null);
        offset.addMetaData(pageWrapper);
        assertEquals(OFFSET_PLUS_PAGE, pageWrapper.getOffsetInput());
    }
    
    @Test
    public void testValidOffsetSingleBodyParam() {
        PaginatedData<?, ?, ?, ?> paginatedData = mock(PaginatedData.class);
        Response response = mock(Response.class);

        Map<String, Integer> body = new HashMap<>();
        body.put("offset", INITIAL_OFFSET);
        HttpRequest.Builder builder = new HttpRequest.Builder()
            .bodyParam(b -> b.value(body)); 

        when(paginatedData.getRequestBuilder()).thenReturn(builder);
        when(paginatedData.getResponse()).thenReturn(response);
        when(paginatedData.getPageSize()).thenReturn(PAGE_SIZE);

        OffsetPagination offset = new OffsetPagination("$request.body#/offset");
        HttpRequest.Builder requestBuilder = offset.apply(paginatedData);
        assertNotNull(requestBuilder);
        
        // Verify metadata update correctly
        PageWrapper<?, ?> pageWrapper = PageWrapper.Create(response, null, null);
        offset.addMetaData(pageWrapper);
        assertEquals(OFFSET_PLUS_PAGE, pageWrapper.getOffsetInput());
    }


    @Test
    public void testValidOffsetMultipleBodyParams() {
        PaginatedData<?, ?, ?, ?> paginatedData = mock(PaginatedData.class);
        Response response = mock(Response.class);

        // Test body parameters update
        HttpRequest.Builder builder = new HttpRequest.Builder()
            .bodyParam(b -> b.key("offset").value(INITIAL_OFFSET));

        when(paginatedData.getRequestBuilder()).thenReturn(builder);
        when(paginatedData.getResponse()).thenReturn(response);
        when(paginatedData.getPageSize()).thenReturn(PAGE_SIZE);

        OffsetPagination offset = new OffsetPagination("$request.body#/offset");
        HttpRequest.Builder requestBuilder = offset.apply(paginatedData);
        assertNotNull(requestBuilder);
        
        // Verify metadata is added correctly
        PageWrapper<?, ?> pageWrapper = PageWrapper.Create(response, null, null);
        offset.addMetaData(pageWrapper);
        assertEquals(OFFSET_PLUS_PAGE, pageWrapper.getOffsetInput());
    }

    @Test
    public void testValidOffsetTemplate() {
        PaginatedData<?, ?, ?, ?> paginatedData = mock(PaginatedData.class);
        Response response = mock(Response.class);

        when(paginatedData.getRequestBuilder())
            .thenReturn(new HttpRequest.Builder().templateParam(
                    t -> t.key("offset").value(INITIAL_OFFSET)));
        when(paginatedData.getResponse()).thenReturn(response);
        when(paginatedData.getPageSize()).thenReturn(PAGE_SIZE);

        OffsetPagination offset = new OffsetPagination("$request.path#/offset");

        Builder requestBuilder = offset.apply(paginatedData);
        assertNotNull(requestBuilder);

        requestBuilder.updateByReference("$request.path#/offset", v -> {
            assertEquals(OFFSET_PLUS_PAGE, v);
            return v;
        });
        PageWrapper<?, ?> pageWrapper = PageWrapper.Create(response, null, null);
        offset.addMetaData(pageWrapper);
        assertEquals(OFFSET_PLUS_PAGE, pageWrapper.getOffsetInput());
    }

    @Test
    public void testValidOffset() {
        PaginatedData<?, ?, ?, ?> paginatedData = mock(PaginatedData.class);
        Response response = mock(Response.class);

        when(paginatedData.getRequestBuilder())
            .thenReturn(new HttpRequest.Builder().queryParam(
                    q -> q.key("offset").value(INITIAL_OFFSET)));
        when(paginatedData.getResponse()).thenReturn(response);
        when(paginatedData.getPageSize()).thenReturn(PAGE_SIZE);

        OffsetPagination offset = new OffsetPagination("$request.query#/offset");

        Builder requestBuilder = offset.apply(paginatedData);
        assertNotNull(requestBuilder);

        requestBuilder.updateByReference("$request.query#/offset", v -> {
            assertEquals(OFFSET_PLUS_PAGE, v);
            return v;
        });
        PageWrapper<?, ?> pageWrapper = PageWrapper.Create(response, null, null);
        offset.addMetaData(pageWrapper);
        assertEquals(OFFSET_PLUS_PAGE, pageWrapper.getOffsetInput());
    }

    @Test
    public void testValidOffsetAsInnerField() {
        PaginatedData<?, ?, ?, ?> paginatedData = mock(PaginatedData.class);
        Response response = mock(Response.class);

        when(paginatedData.getRequestBuilder()).thenReturn(
            new HttpRequest.Builder().queryParam(q -> q.key("offset").value(
                    new HashMap<String, String>() {
                        private static final long serialVersionUID = 1L;
                        {
                            put("val", String.valueOf(OFFSET_VAL));
                        }
                    })));
        when(paginatedData.getResponse()).thenReturn(response);
        when(paginatedData.getPageSize()).thenReturn(PAGE_SIZE);

        OffsetPagination offset = new OffsetPagination("$request.query#/offset/val");

        Builder requestBuilder = offset.apply(paginatedData);
        assertNotNull(requestBuilder);

        requestBuilder.updateByReference("$request.query#/offset/val", v -> {
            assertEquals(OFFSET_VAL_PLUS_ONE, v);
            return v;
        });
        PageWrapper<?, ?> pageWrapper = PageWrapper.Create(response, null, null);
        offset.addMetaData(pageWrapper);
        assertEquals(OFFSET_VAL_PLUS_ONE, pageWrapper.getOffsetInput());
    }

    @Test
    public void testValidStringOffset() {
        PaginatedData<?, ?, ?, ?> paginatedData = mock(PaginatedData.class);
        Response response = mock(Response.class);

        when(paginatedData.getRequestBuilder())
            .thenReturn(new HttpRequest.Builder().queryParam(
                    q -> q.key("offset").value(String.valueOf(OFFSET_STRING))));
        when(paginatedData.getResponse()).thenReturn(response);
        when(paginatedData.getPageSize()).thenReturn(PAGE_SIZE);

        OffsetPagination offset = new OffsetPagination("$request.query#/offset");

        Builder requestBuilder = offset.apply(paginatedData);
        assertNotNull(requestBuilder);

        requestBuilder.updateByReference("$request.query#/offset", v -> {
            assertEquals(OFFSET_STRING_PLUS_PAGE, v);
            return v;
        });
        PageWrapper<?, ?> pageWrapper = PageWrapper.Create(response, null, null);
        offset.addMetaData(pageWrapper);
        assertEquals(OFFSET_STRING_PLUS_PAGE, pageWrapper.getOffsetInput());
    }


    @Test
    public void testInvalidStringOffset() {
        PaginatedData<?, ?, ?, ?> paginatedData = mock(PaginatedData.class);

        when(paginatedData.getRequestBuilder())
            .thenReturn(new HttpRequest.Builder().queryParam(
                    q -> q.key("offset").value(INVALID_OFFSET_STRING)));
        when(paginatedData.getPageSize()).thenReturn(PAGE_SIZE);

        OffsetPagination offset = new OffsetPagination("$request.query#/offset");

        Builder requestBuilder = offset.apply(paginatedData);
        assertNotNull(requestBuilder);

        requestBuilder.updateByReference("$request.query#/offset", v -> {
            assertEquals(INVALID_OFFSET_STRING, v);
            return v;
        });
        PageWrapper<?, ?> pageWrapper = PageWrapper.Create(mock(Response.class), null, null);
        offset.addMetaData(pageWrapper);
        assertEquals(INVALID_OFFSET_RESULT, pageWrapper.getOffsetInput());  // Assuming invalid offset sets to default -1
    }

    @Test
    public void testMissingOffset() {
        PaginatedData<?, ?, ?, ?> paginatedData = mock(PaginatedData.class);

        when(paginatedData.getRequestBuilder()).thenReturn(new HttpRequest.Builder());
        when(paginatedData.getPageSize()).thenReturn(PAGE_SIZE);

        OffsetPagination offset = new OffsetPagination("$request.query#/offset");

        Builder requestBuilder = offset.apply(paginatedData);
        assertNotNull(requestBuilder);
        PageWrapper<?, ?> pageWrapper = PageWrapper.Create(mock(Response.class), null, null);
        offset.addMetaData(pageWrapper);
        assertEquals(INVALID_OFFSET_RESULT, pageWrapper.getOffsetInput());  
    }

    @Test
    public void testNullOffset() {
        PaginatedData<?, ?, ?, ?> paginatedData = mock(PaginatedData.class);
        Response response = mock(Response.class);

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
        PageWrapper<?, ?> pageWrapper = PageWrapper.Create(response, null, null);
        offset.addMetaData(pageWrapper);
        assertEquals(INVALID_OFFSET_RESULT, pageWrapper.getOffsetInput());
    }
}
