package apimatic.core.type.pagination;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;

import org.junit.Test;
import org.junit.runner.RunWith;

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

import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
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

    /**
     * Provides invalid pagination test cases to verify handling of edge cases and null responses.
     *
     * @return A list of invalid pagination test cases, each represented as an object array.
     */
    @Parameters
    public static List<Object[]> invalidPaginationTestCases() {
        return Arrays.asList(new Object[][] {
            {"{\"data\":[],\"next_link\":\"https://localhost:3000/path?page=2\"}"},
            {"{\"data\":null,\"next_link\":\"https://localhost:3000/path?page=2\"}"},
            {null}
        });
    }

    private final String responseBody;

    /**
     * Constructs a PaginatedDataTest with the given response body.
     *
     * @param responseBody the response body string to be used in the test
     */
    public PaginatedDataTest(final String responseBody) {
        this.responseBody = responseBody;
    }

    /**
     * Tests invalid pagination scenarios
     */
    @Test
    public void testInvalidPagination() throws IOException {
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

        verifyData(getPaginatedData(call1, call2, call3,
                new LinkPagination("$response.body#/next_link")));
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

        verifyData(getPaginatedData(call1, call2, call3,
                new CursorPagination("$response.body#/page_info",
                "$request.path#/cursor")));
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

        verifyData(getPaginatedData(call1, call2, call3,
                new OffsetPagination("$request.headers#/offset")));
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

        verifyData(getPaginatedData(call1, call2, call3,
                new PagePagination("$request.query#/page")));
    }

    @Test
    public void testInvalidPagePaginationData() throws IOException, CoreApiException {
        Runnable call1 = () -> when(getResponse().getBody()).thenReturn(
                "{\"data\":[\"apple\",\"mango\",\"orange\"]}");

        PaginatedData<String, PageWrapper<String, RecordPage>,
            RecordPage, CoreApiException> paginatedData = getPaginatedData(call1, null, null,
                    new PagePagination("$request.query#/INVALID"));
        Iterator<CheckedSupplier<PageWrapper<String, RecordPage>, CoreApiException>> pages =
                paginatedData.pages(cs -> cs);
        assertTrue(pages.hasNext());
        assertTrue(pages.hasNext());
        assertEquals(Arrays.asList("apple", "mango", "orange"), pages.next().get().getItems());
        assertFalse(pages.hasNext());
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

        verifyData(getPaginatedData(call1, call2, call3,
                new LinkPagination("$response.INVALID#/next_link"),
                new PagePagination("$request.body#/limit")));
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

        RecordPage expectedPage1 = new RecordPage();
        expectedPage1.setData(Arrays.asList("apple", "mango", "orange"));
        expectedPage1.setPageInfo("fruits");

        RecordPage expectedPage2 = new RecordPage();
        expectedPage2.setData(Arrays.asList("potato", "carrot", "tomato"));
        expectedPage2.setPageInfo("vegitables");
        expectedPage2.setNextLink("https://localhost:3000/path?page=3");

        PageWrapper<String, RecordPage> page = paginatedData.getPage(pageCreator);
        assertNull(page);
        assertEquals(0, paginatedData.getItems(itemCreator).size());

        boolean hasNext = paginatedData.fetchNextPageAsync().get();
        assertEquals(true, hasNext);
        assertEquals(expectedPage1.getData(), paginatedData.getItems(itemCreator));
        page = paginatedData.getPage(pageCreator);
        assertEquals(expectedPage1.getData(), page.getResult().getData());
        assertEquals(expectedPage1.getNextLink(), page.getResult().getNextLink());
        assertEquals(expectedPage1.getPageInfo(), page.getResult().getPageInfo());
        assertEquals("/path/cursor?page=1", page.getNextLinkInput());
        assertEquals(-1, page.getPageInput());

        hasNext = paginatedData.fetchNextPageAsync().get();
        assertEquals(true, hasNext);
        assertEquals(expectedPage2.getData(), paginatedData.getItems(itemCreator));
        page = paginatedData.getPage(pageCreator);
        assertEquals(expectedPage2.getData(), page.getResult().getData());
        assertEquals(expectedPage2.getNextLink(), page.getResult().getNextLink());
        assertEquals(expectedPage2.getPageInfo(), page.getResult().getPageInfo());
        assertNull(page.getNextLinkInput());
        assertEquals(2, page.getPageInput());

        hasNext = paginatedData.fetchNextPageAsync().get();
        assertEquals(false, hasNext);
        page = paginatedData.getPage(pageCreator);
        assertNull(page);
        assertEquals(0, paginatedData.getItems(itemCreator).size());
    }

    private void verifyData(PaginatedData<String, PageWrapper<String, RecordPage>,
            RecordPage, CoreApiException> paginatedData) throws CoreApiException, IOException {
        int index = 0;
        List<String> expectedItems = Arrays.asList(
                "apple", "mango", "orange", "potato", "carrot", "tomato");

        Iterator<CheckedSupplier<String, CoreApiException>> itemIterator =
                paginatedData.items(cs -> cs);
        while (itemIterator.hasNext()) {
            String d = itemIterator.next().get();
            assertNotNull(d);
            assertEquals(expectedItems.get(index), d);
            index++;
        }

        Exception exception = assertThrows(NoSuchElementException.class, itemIterator::next);
        assertEquals("No more items available.", exception.getMessage());

        RecordPage expectedPage1 = new RecordPage();
        expectedPage1.setData(Arrays.asList("apple", "mango", "orange"));
        expectedPage1.setPageInfo("fruits");
        expectedPage1.setNextLink("https://localhost:3000/path?page=2");

        RecordPage expectedPage2 = new RecordPage();
        expectedPage2.setData(Arrays.asList("potato", "carrot", "tomato"));
        expectedPage2.setPageInfo("vegitables");
        expectedPage2.setNextLink("https://localhost:3000/path?page=3");

        int pageOffset = 0;
        List<RecordPage> expectedPages = Arrays.asList(expectedPage1, expectedPage2);
        Iterator<CheckedSupplier<PageWrapper<String, RecordPage>, CoreApiException>> pagesIterator =
                paginatedData.pages(cs -> cs);

        while (pagesIterator.hasNext()) {
            PageWrapper<String, RecordPage> p = pagesIterator.next().get();
            assertNotNull(p);

            String expectedPageInfo = expectedPages.get(pageOffset).getPageInfo();
            String expectedNextLink = expectedPages.get(pageOffset).getNextLink();
            List<String> expectedData = expectedPages.get(pageOffset).getData();

            assertEquals(expectedPageInfo, p.getResult().getPageInfo());
            assertEquals(expectedNextLink, p.getResult().getNextLink());
            assertEquals(expectedData, p.getResult().getData());
            assertEquals(expectedData, p.getItems());
            assertEquals(SUCCESS_STATUS_CODE, p.getStatusCode());
            assertEquals(getHttpHeaders(), p.getHeaders());
            if (p.getCursorInput() != null && pageOffset > 0) {
                assertEquals(expectedPages.get(pageOffset - 1).getPageInfo(), p.getCursorInput());
            }

            if (p.getNextLinkInput() != null && pageOffset > 0) {
                assertEquals(expectedPages.get(pageOffset - 1).getNextLink(), p.getNextLinkInput());
            }

            if (p.getPageInput() != -1) {
                assertEquals(pageOffset + 1, p.getPageInput());
            }

            if (p.getOffsetInput() != -1) {
                assertEquals(pageOffset * PAGE_SIZE, p.getOffsetInput());
            }
            pageOffset++;
        }

        exception = assertThrows(NoSuchElementException.class, pagesIterator::next);
        assertEquals("No more pages available.", exception.getMessage());
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
