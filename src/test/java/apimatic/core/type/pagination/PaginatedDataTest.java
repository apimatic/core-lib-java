package apimatic.core.type.pagination;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;

import org.junit.Test;

import apimatic.core.EndToEndTest;
import io.apimatic.core.ApiCall;
import io.apimatic.core.types.CoreApiException;
import io.apimatic.core.types.pagination.CheckedSupplier;
import io.apimatic.core.types.pagination.CursorPagination;
import io.apimatic.core.types.pagination.LinkPagination;
import io.apimatic.core.types.pagination.OffsetPagination;
import io.apimatic.core.types.pagination.PagePagination;
import io.apimatic.core.types.pagination.PageWrapper;
import io.apimatic.core.types.pagination.PaginatedData;
import io.apimatic.core.types.pagination.PaginationStrategy;
import io.apimatic.core.utilities.CoreHelper;
import io.apimatic.coreinterfaces.http.Callback;
import io.apimatic.coreinterfaces.http.Context;
import io.apimatic.coreinterfaces.http.Method;
import io.apimatic.coreinterfaces.http.request.ArraySerializationFormat;
import io.apimatic.coreinterfaces.http.request.Request;
import io.apimatic.coreinterfaces.http.request.configuration.RetryOption;

public class PaginatedDataTest extends EndToEndTest {
    private static final int NOT_FOUND_STATUS_CODE = 404;
    private static final int SUCCESS_STATUS_CODE = 200;
    private static final int PAGE_SIZE = 3;

    /**
     * Tests pagination with failing response (404 status code)
     */
    @Test
    public void testInvalidPaginationWithFailingResponse() throws IOException {
        Runnable call1 = () -> {
            when(getResponse().getStatusCode()).thenReturn(NOT_FOUND_STATUS_CODE);
        };
        PaginatedData<String, PageWrapper<String, RecordPage>,
            RecordPage, CoreApiException> paginatedData = getPaginatedData(call1, null, null,
                new LinkPagination("$response.body#/next_link"));
        Iterator<CheckedSupplier<PageWrapper<String, RecordPage>, CoreApiException>> pages =
                paginatedData.pages(cs -> cs);
        assertTrue(pages.hasNext());
        assertTrue(pages.hasNext());
        Exception exception = assertThrows(CoreApiException.class, pages.next()::get);
        assertEquals("Not found", exception.getMessage());
        assertFalse(pages.hasNext());
    }

    /**
     * Tests async pagination with failing response (404 status code)
     */
    @Test
    public void testInvalidPaginationWithFailingResponseAsync()
            throws IOException, InterruptedException, ExecutionException {
        Runnable call1 = () -> when(getResponse()
                .getStatusCode()).thenReturn(NOT_FOUND_STATUS_CODE);
        PaginatedData<String, PageWrapper<String, RecordPage>,
            RecordPage, CoreApiException> paginatedData = getPaginatedData(call1, null, null,
                new LinkPagination("$response.body#/next_link"));

        boolean hasNext = paginatedData.fetchNextPageAsync().get();
        assertTrue(hasNext);
        Exception exception = assertThrows(CoreApiException.class,
                paginatedData.getPage(cs -> cs)::get);
        assertEquals("Not found", exception.getMessage());

        hasNext = paginatedData.fetchNextPageAsync().get();
        assertFalse(hasNext);
    }

    @Test
    public void testInvalidPaginationWithEmptyItems() throws IOException {
        assertInvalidPaginatedData(
                "{\"data\":[],\"next_link\":\"https://localhost:3000/path?page=2\"}");
    }

    @Test
    public void testInvalidPaginationWithNullItems() throws IOException {
        assertInvalidPaginatedData(
                "{\"data\":null,\"next_link\":\"https://localhost:3000/path?page=2\"}");
    }

    @Test
    public void testInvalidPaginationWithNullPage() throws IOException {
        assertInvalidPaginatedData(null);
    }

    private void assertInvalidPaginatedData(String responseBody) throws IOException {
        Runnable call1 = () -> when(getResponse().getBody()).thenReturn(responseBody);
        PaginatedData<String, PageWrapper<String, RecordPage>,
            RecordPage, CoreApiException> paginatedData = getPaginatedData(call1, null, null,
                new LinkPagination("$response.body#/next_link"));
        assertFalse(paginatedData.pages(cs -> cs).hasNext());
    }

    @Test
    public void testLinkPaginationData() throws IOException, CoreApiException {
        Runnable call1 = () -> when(getResponse().getBody()).thenReturn(
                "{\"data\":[\"apple\",\"mango\",\"orange\"],\""
                + "page_info\":\"fruits\","
                + "\"next_link\":\"https://localhost:3000/path?page=2\"}");
        Runnable call2 = () -> when(getResponse().getBody()).thenReturn(
                "{\"data\":[\"potato\",\"carrot\",\"tomato\"],"
                + "\"page_info\":\"vegitables\","
                + "\"next_link\":\"https://localhost:3000/path?page=3\"}");
        Runnable call3 = () -> when(getResponse().getBody()).thenReturn(
                "{\"data\":[]}");

        List<PageWrapper<String, RecordPage>> pages = verifyData(getPaginatedData(call1, call2,
                call3, new LinkPagination("$response.body#/next_link")));

        assertTrue(pages.get(0).isLinkPagination());
        assertEquals(null, pages.get(0).getNextLinkInput());
        assertTrue(pages.get(1).isLinkPagination());
        assertEquals("https://localhost:3000/path?page=2", pages.get(1).getNextLinkInput());
    }

    @Test
    public void testInvalidLinkPaginationData() throws IOException, CoreApiException {
        Runnable call1 = () -> when(getResponse().getBody()).thenReturn(
                "{\"data\":[\"apple\",\"mango\",\"orange\"]}");

        PaginatedData<String, PageWrapper<String, RecordPage>,
            RecordPage, CoreApiException> paginatedData = getPaginatedData(call1, null, null,
                new LinkPagination("$response.body#/INVALID"));
        Iterator<CheckedSupplier<PageWrapper<String, RecordPage>, CoreApiException>> pages =
                paginatedData.pages(cs -> cs);
        assertTrue(pages.hasNext());
        assertTrue(pages.hasNext());
        assertEquals(Arrays.asList("apple", "mango", "orange"), pages.next().get().getItems());
        assertFalse(pages.hasNext());
    }

    @Test
    public void testCursorPaginationData() throws IOException, CoreApiException {
        Runnable call1 = () -> when(getResponse().getBody()).thenReturn(
                "{\"data\":[\"apple\",\"mango\",\"orange\"],\""
                + "page_info\":\"fruits\","
                + "\"next_link\":\"https://localhost:3000/path?page=2\"}");
        Runnable call2 = () -> when(getResponse().getBody()).thenReturn(
                "{\"data\":[\"potato\",\"carrot\",\"tomato\"],"
                + "\"page_info\":\"vegitables\","
                + "\"next_link\":\"https://localhost:3000/path?page=3\"}");
        Runnable call3 = () -> when(getResponse().getBody()).thenReturn(
                "{\"data\":[]}");

        List<PageWrapper<String, RecordPage>> pages = verifyData(getPaginatedData(call1, call2,
                call3, new CursorPagination("$response.body#/page_info",
                "$request.path#/cursor")));

        assertTrue(pages.get(0).isCursorPagination());
        assertEquals("cursor", pages.get(0).getCursorInput());
        assertTrue(pages.get(1).isCursorPagination());
        assertEquals("fruits", pages.get(1).getCursorInput());
    }

    @Test
    public void testInvalidCursorPaginationData() throws IOException, CoreApiException {
        Runnable call1 = () -> when(getResponse().getBody()).thenReturn(
                "{\"data\":[\"apple\",\"mango\",\"orange\"]}");

        PaginatedData<String, PageWrapper<String, RecordPage>,
            RecordPage, CoreApiException> paginatedData = getPaginatedData(call1, null, null,
                    new CursorPagination("$response.body#/INVALID", "$request.path#/cursor"));
        Iterator<CheckedSupplier<PageWrapper<String, RecordPage>, CoreApiException>> pages =
                paginatedData.pages(cs -> cs);
        assertTrue(pages.hasNext());
        assertTrue(pages.hasNext());
        assertEquals(Arrays.asList("apple", "mango", "orange"), pages.next().get().getItems());
        assertFalse(pages.hasNext());

        paginatedData = getPaginatedData(call1, null, null,
                    new CursorPagination("$response.body#/page_info", ""));
        pages = paginatedData.pages(cs -> cs);
        assertTrue(pages.hasNext());
        assertTrue(pages.hasNext());
        assertEquals(Arrays.asList("apple", "mango", "orange"), pages.next().get().getItems());
        assertFalse(pages.hasNext());
    }

    @Test
    public void testOffsetPaginationData() throws IOException, CoreApiException {
        Runnable call1 = () -> when(getResponse().getBody()).thenReturn(
                "{\"data\":[\"apple\",\"mango\",\"orange\"],\""
                + "page_info\":\"fruits\","
                + "\"next_link\":\"https://localhost:3000/path?page=2\"}");
        Runnable call2 = () -> when(getResponse().getBody()).thenReturn(
                "{\"data\":[\"potato\",\"carrot\",\"tomato\"],"
                + "\"page_info\":\"vegitables\","
                + "\"next_link\":\"https://localhost:3000/path?page=3\"}");
        Runnable call3 = () -> when(getResponse().getBody()).thenReturn(
                "{\"data\":[]}");

        List<PageWrapper<String, RecordPage>> pages = verifyData(getPaginatedData(call1, call2,
                call3, new OffsetPagination("$request.headers#/offset")));

        assertTrue(pages.get(0).isOffsetPagination());
        assertEquals(0, pages.get(0).getOffsetInput());
        assertTrue(pages.get(1).isOffsetPagination());
        assertEquals(PAGE_SIZE, pages.get(1).getOffsetInput());
    }

    @Test
    public void testInvalidOffsetPaginationData() throws IOException, CoreApiException {
        Runnable call1 = () -> when(getResponse().getBody()).thenReturn(
                "{\"data\":[\"apple\",\"mango\",\"orange\"]}");

        PaginatedData<String, PageWrapper<String, RecordPage>,
            RecordPage, CoreApiException> paginatedData = getPaginatedData(call1, null, null,
                    new OffsetPagination(null));
        Iterator<CheckedSupplier<PageWrapper<String, RecordPage>, CoreApiException>> pages =
                paginatedData.pages(cs -> cs);
        assertTrue(pages.hasNext());
        assertTrue(pages.hasNext());
        assertEquals(Arrays.asList("apple", "mango", "orange"), pages.next().get().getItems());
        assertFalse(pages.hasNext());
    }

    @Test
    public void testPagePaginationData() throws IOException, CoreApiException {
        Runnable call1 = () -> when(getResponse().getBody()).thenReturn(
                "{\"data\":[\"apple\",\"mango\",\"orange\"],\""
                + "page_info\":\"fruits\","
                + "\"next_link\":\"https://localhost:3000/path?page=2\"}");
        Runnable call2 = () -> when(getResponse().getBody()).thenReturn(
                "{\"data\":[\"potato\",\"carrot\",\"tomato\"],"
                + "\"page_info\":\"vegitables\","
                + "\"next_link\":\"https://localhost:3000/path?page=3\"}");
        Runnable call3 = () -> when(getResponse().getBody()).thenReturn(
                "{\"data\":[]}");

        List<PageWrapper<String, RecordPage>> pages = verifyData(getPaginatedData(call1, call2,
                call3, new PagePagination("$request.query#/page")));

        assertTrue(pages.get(0).isNumberPagination());
        assertEquals(1, pages.get(0).getPageInput());
        assertTrue(pages.get(1).isNumberPagination());
        assertEquals(2, pages.get(1).getPageInput());
    }

    @Test
    public void testInvalidPagePaginationData() throws IOException, CoreApiException {
        Runnable call1 = () -> when(getResponse().getBody()).thenReturn(
                "{\"data\":[\"apple\",\"mango\",\"orange\"]}");

        PaginatedData<String, PageWrapper<String, RecordPage>,
            RecordPage, CoreApiException> paginatedData = getPaginatedData(call1, call1, null,
                    new PagePagination("$request.query#/INVALID"));
        Iterator<CheckedSupplier<PageWrapper<String, RecordPage>, CoreApiException>> pages =
                paginatedData.pages(cs -> cs);
        assertTrue(pages.hasNext());
        assertTrue(pages.hasNext());
        assertEquals(Arrays.asList("apple", "mango", "orange"), pages.next().get().getItems());
        assertTrue(pages.hasNext());
    }

    @Test
    public void testMultiPaginationData() throws IOException, CoreApiException {
        Runnable call1 = () -> when(getResponse().getBody()).thenReturn(
                "{\"data\":[\"apple\",\"mango\",\"orange\"],\""
                + "page_info\":\"fruits\","
                + "\"next_link\":\"https://localhost:3000/path?page=2\"}");
        Runnable call2 = () -> when(getResponse().getBody()).thenReturn(
                "{\"data\":[\"potato\",\"carrot\",\"tomato\"],"
                + "\"page_info\":\"vegitables\","
                + "\"next_link\":\"https://localhost:3000/path?page=3\"}");
        Runnable call3 = () -> when(getResponse().getBody()).thenReturn(
                "{\"data\":[]}");

        List<PageWrapper<String, RecordPage>> pages = verifyData(getPaginatedData(call1, call2,
                call3, new LinkPagination("$response.INVALID#/next_link"),
                new PagePagination("$request.body#/limit")));

        assertTrue(pages.get(0).isLinkPagination());
        assertEquals(null, pages.get(0).getNextLinkInput());
        assertTrue(pages.get(1).isNumberPagination());
        assertEquals(2, pages.get(1).getPageInput());
    }

    @Test
    public void testInvalidPaginationDataAsync()
            throws IOException, CoreApiException, InterruptedException, ExecutionException {
        Runnable call1 = () -> when(getResponse().getBody()).thenReturn(
                "{\"data\":[\"apple\",\"mango\",\"orange\"]}");

        PaginatedData<String, PageWrapper<String, RecordPage>,
            RecordPage, CoreApiException> paginatedData = getPaginatedData(call1, null, null,
                    new PagePagination("$request.INVALID#/page"));

        boolean hasNext = paginatedData.fetchNextPageAsync().get();
        assertTrue(hasNext);
        assertEquals(Arrays.asList("apple", "mango", "orange"),
                paginatedData.getPage(cs -> cs).get().getItems());

        hasNext = paginatedData.fetchNextPageAsync().get();
        assertFalse(hasNext);
    }

    @Test
    public void testMultiPaginationDataAsync()
            throws IOException, InterruptedException, ExecutionException {
        Runnable call1 = () -> when(getResponse().getBody()).thenReturn(
                "{\"data\":[\"apple\",\"mango\",\"orange\"],\""
                + "page_info\":\"fruits\"}");
        Runnable call2 = () -> when(getResponse().getBody()).thenReturn(
                "{\"data\":[\"potato\",\"carrot\",\"tomato\"],"
                + "\"page_info\":\"vegitables\","
                + "\"next_link\":\"https://localhost:3000/path?page=3\"}");
        Runnable call3 = () -> when(getResponse().getBody()).thenReturn(
                "{\"data\":[]}");

        PaginatedData<String, PageWrapper<String, RecordPage>,
            RecordPage, CoreApiException> paginatedData = getPaginatedData(call1, call2, call3,
                new LinkPagination("$response.body#/next_link"),
                new PagePagination("$request.body#/limit"));

        Function<CheckedSupplier<String, CoreApiException>, String> itemCreator = cs -> {
            try {
                return cs.get();
            } catch (CoreApiException | IOException e) {
                return null;
            }
        };
        Function<CheckedSupplier<PageWrapper<String, RecordPage>, CoreApiException>,
            PageWrapper<String, RecordPage>> pageCreator = cs -> {
                try {
                    return cs.get();
                } catch (CoreApiException | IOException e) {
                    return null;
                }
            };

        RecordPage expectedPage2 = new RecordPage();
        expectedPage2.setData(Arrays.asList("potato", "carrot", "tomato"));
        expectedPage2.setPageInfo("vegitables");
        expectedPage2.setNextLink("https://localhost:3000/path?page=3");

        PageWrapper<String, RecordPage> page = paginatedData.getPage(pageCreator);
        assertNull(page);
        assertEquals(0, paginatedData.getItems(itemCreator).size());

        boolean hasNext = paginatedData.fetchNextPageAsync().get();
        assertEquals(true, hasNext);
        page = paginatedData.getPage(pageCreator);
        assertEquals("fruits", page.getResult().getPageInfo());
        assertEquals(Arrays.asList("apple", "mango", "orange"), page.getItems());
        assertEquals(page.getItems(), page.getResult().getData());
        assertEquals(page.getItems(), paginatedData.getItems(itemCreator));
        assertTrue(page.isLinkPagination());
        assertEquals(null, page.getNextLinkInput());

        hasNext = paginatedData.fetchNextPageAsync().get();
        assertEquals(true, hasNext);
        page = paginatedData.getPage(pageCreator);
        assertEquals("vegitables", page.getResult().getPageInfo());
        assertEquals("https://localhost:3000/path?page=3", page.getResult().getNextLink());
        assertEquals(Arrays.asList("potato", "carrot", "tomato"), page.getItems());
        assertEquals(page.getItems(), page.getResult().getData());
        assertEquals(page.getItems(), paginatedData.getItems(itemCreator));
        assertTrue(page.isNumberPagination());
        assertEquals(2, page.getPageInput());

        hasNext = paginatedData.fetchNextPageAsync().get();
        assertEquals(false, hasNext);
        page = paginatedData.getPage(pageCreator);
        assertNull(page);
        assertEquals(0, paginatedData.getItems(itemCreator).size());
    }

    private List<PageWrapper<String, RecordPage>> verifyData(
            PaginatedData<String, PageWrapper<String, RecordPage>,
            RecordPage, CoreApiException> paginatedData) throws CoreApiException, IOException {
        Iterator<CheckedSupplier<String, CoreApiException>> itemIterator =
                paginatedData.items(cs -> cs);

        assertTrue(itemIterator.hasNext());
        assertEquals("apple", itemIterator.next().get());
        assertTrue(itemIterator.hasNext());
        assertEquals("mango", itemIterator.next().get());
        assertTrue(itemIterator.hasNext());
        assertEquals("orange", itemIterator.next().get());
        assertTrue(itemIterator.hasNext());
        assertEquals("potato", itemIterator.next().get());
        assertTrue(itemIterator.hasNext());
        assertEquals("carrot", itemIterator.next().get());
        assertTrue(itemIterator.hasNext());
        assertEquals("tomato", itemIterator.next().get());
        assertFalse(itemIterator.hasNext());

        Exception exception = assertThrows(NoSuchElementException.class, itemIterator::next);
        assertEquals("No more items available.", exception.getMessage());

        List<PageWrapper<String, RecordPage>> pages = new ArrayList<>();

        Iterator<CheckedSupplier<PageWrapper<String, RecordPage>, CoreApiException>> pagesIterator =
                paginatedData.pages(cs -> cs);

        assertTrue(pagesIterator.hasNext());
        PageWrapper<String, RecordPage> p = pagesIterator.next().get();
        assertNotNull(p);
        assertEquals("fruits", p.getResult().getPageInfo());
        assertEquals("https://localhost:3000/path?page=2", p.getResult().getNextLink());
        assertEquals(Arrays.asList("apple", "mango", "orange"), p.getResult().getData());
        assertEquals(p.getResult().getData(), p.getItems());
        assertEquals(SUCCESS_STATUS_CODE, p.getStatusCode());
        assertEquals(getHttpHeaders(), p.getHeaders());
        pages.add(p);

        assertTrue(pagesIterator.hasNext());
        p = pagesIterator.next().get();
        assertNotNull(p);
        assertEquals("vegitables", p.getResult().getPageInfo());
        assertEquals("https://localhost:3000/path?page=3", p.getResult().getNextLink());
        assertEquals(Arrays.asList("potato", "carrot", "tomato"), p.getResult().getData());
        assertEquals(p.getResult().getData(), p.getItems());
        assertEquals(SUCCESS_STATUS_CODE, p.getStatusCode());
        assertEquals(getHttpHeaders(), p.getHeaders());
        pages.add(p);

        assertFalse(pagesIterator.hasNext());

        exception = assertThrows(NoSuchElementException.class, pagesIterator::next);
        assertEquals("No more pages available.", exception.getMessage());

        return pages;
    }

    private PaginatedData<String, PageWrapper<String, RecordPage>,
    RecordPage, CoreApiException> getPaginatedData(Runnable call1, Runnable call2,
            Runnable call3,
            PaginationStrategy... pagination) throws IOException {
    when(getResponse().getHeaders()).thenReturn(getHttpHeaders());
    Callback pageCallback = new Callback() {
        private int callNumber = 1;
        @Override
        public void onBeforeRequest(Request request) {
            if (callNumber == 1) {
                call1.run();
            } else if (callNumber == 2) {
                call2.run();
            } else if (callNumber == PAGE_SIZE) {
                call3.run();
                callNumber = 0;
            }
            callNumber++;
        }
        @Override
        public void onAfterResponse(Context context) {
            // Does nothing
        }
    };

        return new ApiCall.Builder<RecordPage, CoreApiException>()
                .globalConfig(getGlobalConfig(pageCallback))
                .requestBuilder(requestBuilder -> requestBuilder.server("https://localhost:3000")
                        .path("/path/{cursor}")
                        .templateParam(param -> param.key("cursor").value("cursor")
                                .isRequired(false))
                        .headerParam(param -> param.key("offset").value(0).isRequired(false))
                        .formParam(param -> param.key("limit").value(1).isRequired(false))
                        .queryParam(param -> param.key("page").value(1).isRequired(false))
                        .headerParam(param -> param.key("size").value(2).isRequired(false))
                        .headerParam(param -> param.key("size").value(PAGE_SIZE).isRequired(false))
                        .headerParam(param -> param.key("accept").value("application/json"))
                        .httpMethod(Method.GET))
                .responseHandler(responseHandler -> responseHandler
                        .deserializer(res -> CoreHelper.deserialize(res, RecordPage.class))
                        .nullify404(false)
                        .globalErrorCase(GLOBAL_ERROR_CASES))
                .endpointConfiguration(
                        param -> param.arraySerializationFormat(ArraySerializationFormat.INDEXED)
                                .hasBinaryResponse(false).retryOption(RetryOption.DEFAULT))
                .build()
                .paginate(pd -> pd, pw -> pw, p -> p.getData(), pagination);
    }

}
