package apimatic.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import com.fasterxml.jackson.core.type.TypeReference;

import apimatic.core.exceptions.GlobalTestException;
import apimatic.core.mocks.MockCoreConfig;
import apimatic.core.type.pagination.RecordPage;
import io.apimatic.core.ApiCall;
import io.apimatic.core.ErrorCase;
import io.apimatic.core.GlobalConfiguration;
import io.apimatic.core.types.CoreApiException;
import io.apimatic.core.types.pagination.CursorPagination;
import io.apimatic.core.types.pagination.LinkPagination;
import io.apimatic.core.types.pagination.OffsetPagination;
import io.apimatic.core.types.pagination.PagePagination;
import io.apimatic.core.types.pagination.PaginatedData;
import io.apimatic.core.types.pagination.PaginationDataManager;
import io.apimatic.core.utilities.CoreHelper;
import io.apimatic.coreinterfaces.http.Callback;
import io.apimatic.coreinterfaces.http.Context;
import io.apimatic.coreinterfaces.http.HttpClient;
import io.apimatic.coreinterfaces.http.HttpHeaders;
import io.apimatic.coreinterfaces.http.Method;
import io.apimatic.coreinterfaces.http.request.ArraySerializationFormat;
import io.apimatic.coreinterfaces.http.request.Request;
import io.apimatic.coreinterfaces.http.request.configuration.CoreEndpointConfiguration;
import io.apimatic.coreinterfaces.http.request.configuration.RetryOption;
import io.apimatic.coreinterfaces.http.response.Response;

public class EndToEndTest extends MockCoreConfig {

    /**
     * Success code status code.
     */
    private static final int SUCCESS_CODE = 200;
    private static final int BAD_REQUEST = 400;
    private static final int TOO_MANY_REQUEST = 429;
    private static final int UNAUTHORIZED = 401;
    private static final int METHOD_NOT_ALLOWED = 405;
    private static final int UNSUPPORTED_MEDIA = 415;
    private static final int INTERNAL_SERVER_ERROR = 500;
    private static final int GONE_CLIENT = 410;

    /**
     * Map of Global Error Cases
     */
    private static final Map<String, ErrorCase<CoreApiException>> GLOBAL_ERROR_CASES =
            new HashMap<String, ErrorCase<CoreApiException>>() {
        private static final long serialVersionUID = 1L;
        {
            put("400", ErrorCase.setTemplate(
                    "Failed to make the request, {$statusCode} {$response.body#/errors/0/code} - "
                            + "{$response.body#/errors/0/detail}",
                    (reason, context) -> new GlobalTestException(reason, context)));
            put("404", ErrorCase.setReason("Not found",
                    (reason, context) -> new CoreApiException(reason, context)));
            put("401",
                    ErrorCase.setTemplate("Failed to make the request, "
                            + "{$response.header.content-type} {$response.body#/errors/0/code}"
                            + " - {$response.body#/errors/0/detail}",
                            (reason, context) -> new CoreApiException(reason, context)));
            put("410",
                    ErrorCase.setTemplate("Failed to make the request, {$response.body}",
                            (reason, context) -> new CoreApiException(reason, context)));
            put("405",
                    ErrorCase.setTemplate("Failed to make the request, {$response.header.accept} "
                            + "{$response.body#/errors/0/code} - {$response.body#/errors/0/detail}",
                            (reason, context) -> new CoreApiException(reason, context)));
            put("500",
                    ErrorCase.setTemplate("Failed to make the request, http status code:"
                            + " {$statusCode}",
                            (reason, context) -> new CoreApiException(reason, context)));
            put("4XX",
                    ErrorCase.setTemplate(
                            "Failed to make the request, {$response.body#/errors/0/code}"
                                    + " - {$response.body#/errors/0/detail}",
                            (reason, context) -> new CoreApiException(reason, context)));
            put(ErrorCase.DEFAULT,
                    ErrorCase.setReason(
                            "Failed to make the request, {$response.body#/errors/0/code}"
                                    + " - {$response.body#/errors/0/detail}",
                            (reason, context) -> new CoreApiException(reason, context)));
        }};

    /**
     * Initializes mocks annotated with Mock.
     */
    @Rule
    public MockitoRule initRule = MockitoJUnit.rule().silent();

    /**
     * Mock of {@link Callback}.
     */
    @Mock
    private Callback callback;

    /**
     * Mock of {@link Context}.
     */
    @Mock
    private Context context;

    /**
     * Mock of {@link HttpClient}.
     */
    @Mock
    private HttpClient httpClient;

    /**
     * Mock of {@link Response}.
     */
    @Mock
    private Response response;


    /**
     * Mock of {@link Request}.
     */
    @Mock
    private Request coreHttpRequest;

    /**
     * Mock of {@link CoreEndpointConfiguration}.
     */
    @Mock
    private CoreEndpointConfiguration endpointConfiguration;

    /**
     * @throws IOException Signals that an I/O exception of some sort has occurred.
     */
    @Before
    public void setup() throws IOException {
        prepareStub();
    }

    /**
     * End to End sync call.
     * @throws IOException Signals that an I/O exception of some sort has occurred.
     * @throws CoreApiException Exception in api call execution.
     */
    @Test
    public void testEndToEndSyncCall() throws IOException, CoreApiException {
        String expected = "Turtle";
        String actual = getApiCall().execute();
        assertEquals(actual, expected);
    }
    
    @Test
    public void testLinkPaginationData() throws IOException, CoreApiException {
        verifyData(getPaginatedApiCall(new LinkPagination("$response.body#/next_link")).execute());
    }
    
    @Test
    public void testCursorPaginationData() throws IOException, CoreApiException {
        verifyData(getPaginatedApiCall(new CursorPagination("$response.body#/page_info", "$request.path#/cursor")).execute());
    }
    
    @Test
    public void testOffsetPaginationData() throws IOException, CoreApiException {
        verifyData(getPaginatedApiCall(new OffsetPagination("$request.headers#/offset")).execute());
    }
    
    @Test
    public void testPagePaginationData() throws IOException, CoreApiException {
        verifyData(getPaginatedApiCall(new PagePagination("$request.query#/page")).execute());
        verifyOnlyPages(getPaginatedApiCall(new PagePagination("$request.query#/page")).execute());
    }

    private void verifyOnlyPages(PaginatedData<String, RecordPage> paginatedData) {
        RecordPage expectedPage1 = new RecordPage();
        expectedPage1.data = Arrays.asList("apple", "mango", "orange");
        expectedPage1.pageInfo = "fruits";
        expectedPage1.nextLink = "https://localhost:3000/path?page=2";
        
        RecordPage expectedPage2 = new RecordPage();
        expectedPage2.data = Arrays.asList("potato", "carrot", "tomato");
        expectedPage2.pageInfo = "vegitables";
        expectedPage2.nextLink = null;
        
        int pageNum = 0;
        List<RecordPage> expectedPages = Arrays.asList(expectedPage1, expectedPage2);
        for (RecordPage p : paginatedData.pages()) {
            assertEquals(expectedPages.get(pageNum).data, p.data);
            assertEquals(expectedPages.get(pageNum).pageInfo, p.pageInfo);
            assertEquals(expectedPages.get(pageNum).nextLink, p.nextLink);
            pageNum++;
        }
        Iterator<RecordPage> iterator = paginatedData.pages().iterator();
        Exception exception = assertThrows(NoSuchElementException.class, () -> {
            while (iterator.hasNext()) {
                iterator.next();
            }
            iterator.next();
        });
        assertEquals("No more data available.", exception.getMessage());
    }

    private void verifyData(PaginatedData<String, RecordPage> paginatedData) {
        int index = 0;
        List<String> expectedData = Arrays.asList("apple", "mango", "orange", "potato", "carrot", "tomato");
        Iterator<String> iterator = paginatedData.iterator();
        while (iterator.hasNext()) {
            String d = iterator.next();
            assertEquals(expectedData.get(index), d);
            index++;
        }
        Exception exception = assertThrows(NoSuchElementException.class, () -> {
            iterator.next();
        });
        assertEquals("No more data available.", exception.getMessage());

        RecordPage expectedPage1 = new RecordPage();
        expectedPage1.data = Arrays.asList("apple", "mango", "orange");
        expectedPage1.pageInfo = "fruits";
        expectedPage1.nextLink = "https://localhost:3000/path?page=2";
        
        RecordPage expectedPage2 = new RecordPage();
        expectedPage2.data = Arrays.asList("potato", "carrot", "tomato");
        expectedPage2.pageInfo = "vegitables";
        expectedPage2.nextLink = null;
        
        int pageNum = 0;
        List<RecordPage> expectedPages = Arrays.asList(expectedPage1, expectedPage2);
        for (RecordPage p : paginatedData.pages()) {
            assertEquals(expectedPages.get(pageNum).data, p.data);
            assertEquals(expectedPages.get(pageNum).pageInfo, p.pageInfo);
            assertEquals(expectedPages.get(pageNum).nextLink, p.nextLink);
            pageNum++;
        }
    }

    /**
     * Test the local error template.
     * @throws IOException Signals that an I/O exception of some sort has occurred.
     * @throws CoreApiException Exception in api call execution.
     */
    @Test
    public void testLocalErrorTemplateBody() throws IOException, CoreApiException {
        String responseString = "{\r\n" + "  \"errors\": [\r\n" + "    {\r\n"
                + "      \"category\": \"AUTHENTICATION_ERROR\",\r\n"
                + "      \"code\": \"UNAUTHORIZED\",\r\n"
                + "      \"detail\": \"This request could not be authorized.\"\r\n" + "    }\r\n"
                + "  ]\r\n" + "}\r\n" + "\r\n";
        Exception exception = assertThrows(CoreApiException.class, () -> {
            getApiCallLocalErrorTemplate(responseString, BAD_REQUEST).execute();
        });
        String expected = "Failed to make the request, 400 UNAUTHORIZED - "
                + "This request could not be authorized.";
        String actual = exception.getMessage();
        assertEquals(actual, expected);
    }

    /**
     * Test the local error template.
     * @throws IOException Signals that an I/O exception of some sort has occurred.
     * @throws CoreApiException Exception in api call execution.
     */
    @Test
    public void testLocalErrorTemplateNonScalerBody() throws IOException, CoreApiException {
        String responseString = "{\r\n" + "  \"errors\": [\r\n" + "    {\r\n"
                + "      \"category\": \"AUTHENTICATION_ERROR\",\r\n"
                + "      \"code\": {\"type\" : \"status\"},\r\n"
                + "      \"detail\": \"This request could not be authorized.\"\r\n" + "    }\r\n"
                + "  ]\r\n" + "}\r\n" + "\r\n";
        Exception exception = assertThrows(CoreApiException.class, () -> {
            getApiCallLocalErrorTemplate(responseString, BAD_REQUEST).execute();
        });
        String expected = "Failed to make the request, 400 {type:status}"
                + " - This request could not be authorized.";
        String actual = exception.getMessage();
        assertEquals(actual, expected);
    }


    /**
     * Test the Global Error template.
     * @throws IOException Signals that an I/O exception of some sort has occurred.
     * @throws CoreApiException Exception in api call execution.
     */
    @Test
    public void testGlobalErrorTemplateBody() throws IOException, CoreApiException {
        String responseString = "{\r\n" + "  \"errors\": [\r\n" + "    {\r\n"
                + "      \"category\": \"AUTHENTICATION_ERROR\",\r\n"
                + "      \"code\": \"UNAUTHORIZED\",\r\n"
                + "      \"detail\": \"This request could not be authorized.\"\r\n" + "    }\r\n"
                + "  ]\r\n" + "}\r\n" + "\r\n";
        Exception exception = assertThrows(GlobalTestException.class, () -> {
            getApiCallGlobalErrorTemplate(responseString, BAD_REQUEST).execute();
        });
        String expected = "Failed to make the request, 400 UNAUTHORIZED - "
                + "This request could not be authorized.";
        String actual = exception.getMessage();
        assertEquals(actual, expected);
    }

    /**
     * Test the Global Error template.
     * @throws IOException Signals that an I/O exception of some sort has occurred.
     * @throws CoreApiException Exception in api call execution.
     */
    @Test
    public void testGlobalErrorTemplateMissingBody() throws IOException, CoreApiException {
        String responseString = "{\r\n" + "  \"errors\": [\r\n" + "    {\r\n"
                + "      \"category\": \"AUTHENTICATION_ERROR\",\r\n"
                + "      \"code\": \"UNAUTHORIZED\"\r\n" + "    }\r\n" + "  ]\r\n" + "}\r\n"
                + "\r\n";
        Exception exception = assertThrows(GlobalTestException.class, () -> {
            getApiCallGlobalErrorTemplate(responseString, BAD_REQUEST).execute();
        });
        String expected = "Failed to make the request, 400 UNAUTHORIZED -";
        String actual = exception.getMessage().trim();
        assertEquals(actual, expected);
    }

    /**
     * Test the Global Error template.
     * @throws IOException Signals that an I/O exception of some sort has occurred.
     * @throws CoreApiException Exception in api call execution.
     */
    @Test
    public void testGlobalErrorTemplateUnknownPropertyBody() throws IOException, CoreApiException {
        String responseString = "{\r\n" + "  \"errors\": [\r\n" + "    {\r\n"
                + "      \"category\": \"AUTHENTICATION_ERROR\",\r\n"
                + "      \"code\": \"UNAUTHORIZED\"\r\n" + "    }\r\n" + "  ]\r\n" + "}\r\n"
                + "\r\n";
        Exception exception = assertThrows(GlobalTestException.class, () -> {
            getApiCallGlobalErrorTemplate(responseString, BAD_REQUEST).execute();
        });
        String expected = "Failed to make the request, 400 UNAUTHORIZED -";
        String actual = exception.getMessage().trim();
        assertEquals(actual, expected);
    }

    /**
     * Test the Global Error template.
     * @throws IOException Signals that an I/O exception of some sort has occurred.
     * @throws CoreApiException Exception in api call execution.
     */
    @Test
    public void testGlobalErrorTemplateScalarBody() throws IOException, CoreApiException {
        String responseString = "\"true\"";
        Exception exception = assertThrows(GlobalTestException.class, () -> {
            getApiCallGlobalErrorTemplate(responseString, BAD_REQUEST).execute();
        });
        String expected = "Failed to make the request, 400  -";
        String actual = exception.getMessage().trim();
        assertEquals(actual, expected);
    }

    /**
     * Test the Global Error template.
     * @throws IOException Signals that an I/O exception of some sort has occurred.
     * @throws CoreApiException Exception in api call execution.
     */
    @Test
    public void testGlobalErrorTemplateExistScalarBody() throws IOException, CoreApiException {
        String responseString = "\"true\"";
        Exception exception = assertThrows(CoreApiException.class, () -> {
            getApiCallGlobalErrorTemplate(responseString, GONE_CLIENT).execute();
        });
        String expected = "Failed to make the request, true";
        String actual = exception.getMessage().trim();
        assertEquals(actual, expected);
    }

    /**
     * Test the Global Error template missing status code.
     * @throws IOException Signals that an I/O exception of some sort has occurred.
     * @throws CoreApiException Exception in api call execution.
     */
    @Test
    public void testGlobalErrorTemplateMissingStatusCode() throws IOException, CoreApiException {
        String responseString = "{\r\n" + "  \"errors\": [\r\n" + "    {\r\n"
                + "      \"category\": \"AUTHENTICATION_ERROR\",\r\n"
                + "      \"code\": \"UNAUTHORIZED\",\r\n"
                + "      \"detail\": \"This request could not be authorized.\"\r\n" + "    }\r\n"
                + "  ]\r\n" + "}\r\n" + "\r\n";
        Exception exception = assertThrows(CoreApiException.class, () -> {
            getApiCallGlobalErrorTemplate(responseString, TOO_MANY_REQUEST).execute();
        });
        String expected =
                "Failed to make the request, UNAUTHORIZED - This request could not be authorized.";
        String actual = exception.getMessage();
        assertEquals(actual, expected);
    }

    /**
     * Test the Global Error template headers.
     * @throws IOException Signals that an I/O exception of some sort has occurred.
     * @throws CoreApiException Exception in api call execution.
     */
    @Test
    public void testGlobalErrorTemplateHeaders() throws IOException, CoreApiException {
        String responseString = "{\r\n" + "  \"errors\": [\r\n" + "    {\r\n"
                + "      \"category\": \"AUTHENTICATION_ERROR\",\r\n"
                + "      \"code\": \"UNAUTHORIZED\",\r\n"
                + "      \"detail\": \"This request could not be authorized.\"\r\n" + "    }\r\n"
                + "  ]\r\n" + "}\r\n" + "\r\n";
        Exception exception = assertThrows(CoreApiException.class, () -> {
            getApiCallGlobalErrorTemplateWithHeaders(responseString, UNAUTHORIZED).execute();
        });
        String expected = "Failed to make the request, application/json UNAUTHORIZED - "
                + "This request could not be authorized.";
        String actual = exception.getMessage();
        assertEquals(actual, expected);
    }

    /**
     * Test the Global Error template missing headers.
     * @throws IOException Signals that an I/O exception of some sort has occurred.
     * @throws CoreApiException Exception in api call execution.
     */
    @Test
    public void testGlobalErrorTemplateHeadersMissing() throws IOException, CoreApiException {
        String responseString = "{\r\n" + "  \"errors\": [\r\n" + "    {\r\n"
                + "      \"category\": \"AUTHENTICATION_ERROR\",\r\n"
                + "      \"code\": \"UNAUTHORIZED\",\r\n"
                + "      \"detail\": \"This request could not be authorized.\"\r\n" + "    }\r\n"
                + "  ]\r\n" + "}\r\n" + "\r\n";
        Exception exception = assertThrows(CoreApiException.class, () -> {
            getApiCallGlobalErrorTemplateWithHeaders(responseString, METHOD_NOT_ALLOWED).execute();
        });
        String expected = "Failed to make the request,  UNAUTHORIZED - "
                + "This request could not be authorized.";
        String actual = exception.getMessage();
        assertEquals(actual, expected);
    }

    /**
     * Test the Global Error template missing headers.
     * @throws IOException Signals that an I/O exception of some sort has occurred.
     * @throws CoreApiException Exception in api call execution.
     */
    @Test
    public void testGlobalErrorTemplate4XX() throws IOException, CoreApiException {
        String responseString = "{\r\n" + "  \"errors\": [\r\n" + "    {\r\n"
                + "      \"category\": \"AUTHENTICATION_ERROR\",\r\n"
                + "      \"code\": \"UNAUTHORIZED\",\r\n"
                + "      \"detail\": \"This request could not be authorized.\"\r\n" + "    }\r\n"
                + "  ]\r\n" + "}\r\n" + "\r\n";
        Exception exception = assertThrows(CoreApiException.class, () -> {
            getApiCallGlobalErrorTemplateWithHeaders(responseString, UNSUPPORTED_MEDIA).execute();
        });
        String expected = "Failed to make the request, UNAUTHORIZED - "
                + "This request could not be authorized.";
        String actual = exception.getMessage();
        assertEquals(actual, expected);
    }

    /**
     * Test the Global Error template missing headers.
     * @throws IOException Signals that an I/O exception of some sort has occurred.
     * @throws CoreApiException Exception in api call execution.
     */
    @Test
    public void testGlobalErrorTemplate5XX() throws IOException, CoreApiException {
        String responseString = "{\r\n" + "  \"errors\": [\r\n" + "    {\r\n"
                + "      \"category\": \"AUTHENTICATION_ERROR\",\r\n"
                + "      \"code\": \"UNAUTHORIZED\",\r\n"
                + "      \"detail\": \"This request could not be authorized.\"\r\n" + "    }\r\n"
                + "  ]\r\n" + "}\r\n" + "\r\n";
        Exception exception = assertThrows(CoreApiException.class, () -> {
            getApiCallGlobalErrorTemplateWithHeaders(responseString, INTERNAL_SERVER_ERROR)
                    .execute();
        });
        String expected = "Failed to make the request, http status code: 500";
        String actual = exception.getMessage();
        assertEquals(actual, expected);
    }

    private ApiCall<String, CoreApiException> getApiCall() throws IOException {
        when(response.getBody()).thenReturn("\"Turtle\"");
        return new ApiCall.Builder<String, CoreApiException>().globalConfig(getGlobalConfig(callback))
                .requestBuilder(requestBuilder -> requestBuilder.server("https://localhost:3000")
                        .path("/v2/bank-accounts")
                        .queryParam(param -> param.key("cursor").value("cursor").isRequired(false))
                        .formParam(param -> param.key("limit").value("limit").isRequired(false))
                        .templateParam(param -> param.key("location_id").value("locationId")
                                .shouldEncode(true).isRequired(false))
                        .headerParam(param -> param.key("accept").value("application/json"))
                        .withAuth(auth -> auth.add("global"))
                        .httpMethod(Method.GET))
                .responseHandler(responseHandler -> responseHandler
                        .deserializer(response -> CoreHelper.deserialize(response, String.class))
                        .nullify404(false).globalErrorCase(Collections.emptyMap()))
                .endpointConfiguration(
                        param -> param.arraySerializationFormat(ArraySerializationFormat.INDEXED)
                                .hasBinaryResponse(false).retryOption(RetryOption.DEFAULT))
                .build();
    }

    private ApiCall<PaginatedData<String, RecordPage>, CoreApiException> getPaginatedApiCall(PaginationDataManager... pagination) throws IOException {
        when(response.getBody()).thenReturn("{\"data\":[\"apple\",\"mango\",\"orange\"],\"page_info\":\"fruits\","
                + "\"next_link\":\"https://localhost:3000/path?page=2\"}");
        when(response.getHeaders()).thenReturn(getHttpHeaders());
        Callback callback = new Callback() {
            private int callNumber = 1; 
            @Override
            public void onBeforeRequest(Request request) {
                if (callNumber > 1) {
                    when(response.getBody()).thenReturn("{\"data\":[\"potato\",\"carrot\",\"tomato\"],\"page_info\":\"vegitables\"}");
                }
                if (callNumber > 2) {
                    throw new NoSuchElementException("No more data available.");
                }
                callNumber++;
            }
            @Override
            public void onAfterResponse(Context context) {
                // TODO Auto-generated method stub
            }
        };
        return new ApiCall.Builder<PaginatedData<String, RecordPage>, CoreApiException>().globalConfig(getGlobalConfig(callback))
                .requestBuilder(requestBuilder -> requestBuilder.server("https://localhost:3000")
                        .path("/path/{cursor}")
                        .templateParam(param -> param.key("cursor").value("cursor").isRequired(false))
                        .headerParam(param -> param.key("offset").value(1).isRequired(false))
                        .queryParam(param -> param.key("page").value(1).isRequired(false))
                        .formParam(param -> param.key("limit").value("limit").isRequired(false))
                        .headerParam(param -> param.key("accept").value("application/json"))
                        .httpMethod(Method.GET))
                .responseHandler(responseHandler -> responseHandler
                        .paginatedDeserializer(new TypeReference<RecordPage>() {}, t -> t.data, r -> r, pagination)
                        .nullify404(false)
                        .globalErrorCase(Collections.emptyMap()))
                .endpointConfiguration(
                        param -> param.arraySerializationFormat(ArraySerializationFormat.INDEXED)
                                .hasBinaryResponse(false).retryOption(RetryOption.DEFAULT))
                .build();
    }

    private ApiCall<String, CoreApiException> getApiCallLocalErrorTemplate(String responseString,
            int statusCode) throws IOException {
        when(response.getBody()).thenReturn(responseString);
        when(response.getStatusCode()).thenReturn(statusCode);
        return new ApiCall.Builder<String, CoreApiException>().globalConfig(getGlobalConfig(callback))
                .requestBuilder(requestBuilder -> requestBuilder.server("https://localhost:3000")
                        .path("/v2/bank-accounts")
                        .queryParam(param -> param.key("cursor").value("cursor").isRequired(false))
                        .formParam(param -> param.key("limit").value("limit").isRequired(false))
                        .templateParam(param -> param.key("location_id").value("locationId")
                                .shouldEncode(true).isRequired(false))
                        .headerParam(param -> param.key("accept").value("application/json"))
                        .withAuth(auth -> auth.add("global"))
                        .httpMethod(Method.GET))
                .responseHandler(responseHandler -> responseHandler
                        .deserializer(response -> CoreHelper.deserialize(response, String.class))
                        .localErrorCase("400",
                                ErrorCase.setTemplate(
                                        "Failed to make the request, {$statusCode} "
                                                + "{$response.body#/errors/0/code} - "
                                                + "{$response.body#/errors/0/detail}",
                                        (reason, context) -> new CoreApiException(reason, context)))
                        .nullify404(false).globalErrorCase(Collections.emptyMap()))
                .endpointConfiguration(
                        param -> param.arraySerializationFormat(ArraySerializationFormat.INDEXED)
                                .hasBinaryResponse(false).retryOption(RetryOption.DEFAULT))
                .build();

    }

    private ApiCall<String, CoreApiException> getApiCallGlobalErrorTemplate(String responseString,
            int statusCode) throws IOException {
        when(response.getBody()).thenReturn(responseString);
        when(response.getStatusCode()).thenReturn(statusCode);
        return new ApiCall.Builder<String, CoreApiException>().globalConfig(getGlobalConfig(callback))
                .requestBuilder(requestBuilder -> requestBuilder.server("https://localhost:3000")
                        .path("/v2/bank-accounts")
                        .queryParam(param -> param.key("cursor").value("cursor").isRequired(false))
                        .formParam(param -> param.key("limit").value("limit").isRequired(false))
                        .templateParam(param -> param.key("location_id").value("locationId")
                                .shouldEncode(true).isRequired(false))
                        .headerParam(param -> param.key("accept").value("application/json"))
                        .withAuth(auth -> auth.add("global"))
                        .httpMethod(Method.GET))
                .responseHandler(responseHandler -> responseHandler
                        .deserializer(response -> CoreHelper.deserialize(response, String.class))
                        .nullify404(false).globalErrorCase(GLOBAL_ERROR_CASES))
                .endpointConfiguration(
                        param -> param.arraySerializationFormat(ArraySerializationFormat.INDEXED)
                                .hasBinaryResponse(false).retryOption(RetryOption.DEFAULT))
                .build();
    }


    private ApiCall<String, CoreApiException> getApiCallGlobalErrorTemplateWithHeaders(
            String responseString, int statusCode) throws IOException {
        when(response.getBody()).thenReturn(responseString);
        when(response.getStatusCode()).thenReturn(statusCode);
        when(response.getHeaders()).thenReturn(getHttpHeaders());
        when(getHttpHeaders().has("content-type")).thenReturn(true);
        when(getHttpHeaders().value("content-type")).thenReturn("application/json");
        return new ApiCall.Builder<String, CoreApiException>().globalConfig(getGlobalConfig(callback))
                .requestBuilder(requestBuilder -> requestBuilder.server("https://localhost:3000")
                        .path("/v2/bank-accounts")
                        .queryParam(param -> param.key("cursor").value("cursor").isRequired(false))
                        .formParam(param -> param.key("limit").value("limit").isRequired(false))
                        .templateParam(param -> param.key("location_id").value("locationId")
                                .shouldEncode(true).isRequired(false))
                        .headerParam(param -> param.key("accept").value("application/json"))
                        .withAuth(auth -> auth.add("global"))
                        .httpMethod(Method.GET))
                .responseHandler(responseHandler -> responseHandler
                        .deserializer(response -> CoreHelper.deserialize(response, String.class))
                        .nullify404(false).globalErrorCase(GLOBAL_ERROR_CASES))
                .endpointConfiguration(
                        param -> param.arraySerializationFormat(ArraySerializationFormat.INDEXED)
                                .hasBinaryResponse(false).retryOption(RetryOption.DEFAULT))
                .build();
    }

    private GlobalConfiguration getGlobalConfig(Callback callback) {
        String userAgent = "APIMATIC 3.0";
        GlobalConfiguration globalConfig = new GlobalConfiguration.Builder()
                .authentication(Collections.emptyMap())
                .compatibilityFactory(getCompatibilityFactory()).httpClient(httpClient)
                .baseUri(server -> getBaseUri(server)).callback(callback).userAgent(userAgent)
                .userAgentConfig(Collections.emptyMap()).additionalHeaders(null)
                .globalHeader("version", "0.1").globalHeader("version", "1.2")
                .build();
        return globalConfig;
    }

    protected static String getBaseUri(String test) {
        return "http://localhost:3000";
    }

    private void prepareStub() throws IOException {
        when(httpClient.execute(any(Request.class), any(CoreEndpointConfiguration.class)))
                .thenReturn(response);
        when(getCompatibilityFactory().createHttpHeaders(anyMap())).thenReturn(getHttpHeaders());
        when(getCompatibilityFactory().createHttpRequest(any(Method.class),
                nullable(StringBuilder.class), nullable(HttpHeaders.class), anyMap(), anyList()))
                .thenReturn(coreHttpRequest);
        when(getCompatibilityFactory().createHttpContext(coreHttpRequest, response))
                .thenReturn(context);
        when(context.getResponse()).thenReturn(response);
        when(response.getStatusCode()).thenReturn(SUCCESS_CODE);
    }
}
