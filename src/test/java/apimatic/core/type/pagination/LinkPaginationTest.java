package apimatic.core.type.pagination;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
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
    public void testIsValid_withValidLink_returnsTrue() {
        PaginatedData<?, ?> paginatedData = mock(PaginatedData.class);

        when(paginatedData.getLastRequestBuilder()).thenReturn(new HttpRequest.Builder());
        when(paginatedData.getLastResponseBody()).thenReturn("{\"next\": \"https://api.example.com?page=2\"}");
        when(paginatedData.getLastResponseHeaders()).thenReturn("{\"accept\": \"application/text\"}");

        LinkPagination link = new LinkPagination("$response.body#/next");
        
        assertTrue(link.isValid(paginatedData));
        assertNotNull(link.getNextRequestBuilder());
        link.getNextRequestBuilder().updateByReference("$request.query#/page", v -> 
        {
            assertEquals("2", v);
            // Assert and return original value without updating
            return v;
        });
    }

}
