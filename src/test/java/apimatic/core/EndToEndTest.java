package apimatic.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.when;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import apimatic.core.exceptions.GlobalTestException;
import apimatic.core.mocks.MockCoreConfig;
import io.apimatic.core.ApiCall;
import io.apimatic.core.ErrorCase;
import io.apimatic.core.GlobalConfiguration;
import io.apimatic.core.types.CoreApiException;
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
        String expected = "Failed to make the request, 400 UNAUTHORIZED - ";
        String actual = exception.getMessage();
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
        return new ApiCall.Builder<String, CoreApiException>().globalConfig(getGlobalConfig())
                .requestBuilder(requestBuilder -> requestBuilder.server("https://localhost:3000")
                        .path("/v2/bank-accounts")
                        .queryParam(param -> param.key("cursor").value("cursor").isRequired(false))
                        .formParam(param -> param.key("limit").value("limit").isRequired(false))
                        .templateParam(param -> param.key("location_id").value("locationId")
                                .shouldEncode(true).isRequired(false))
                        .headerParam(param -> param.key("accept").value("application/json"))
                        .authenticationKey("global").httpMethod(Method.GET))
                .responseHandler(responseHandler -> responseHandler
                        .deserializer(response -> CoreHelper.deserialize(response, String.class))
                        .nullify404(false).globalErrorCase(Collections.emptyMap()))
                .endpointConfiguration(
                        param -> param.arraySerializationFormat(ArraySerializationFormat.INDEXED)
                                .hasBinaryResponse(false).retryOption(RetryOption.DEFAULT))
                .build();
    }

    private ApiCall<String, CoreApiException> getApiCallLocalErrorTemplate(String responseString,
            int statusCode) throws IOException {
        when(response.getBody()).thenReturn(responseString);
        when(response.getStatusCode()).thenReturn(statusCode);
        return new ApiCall.Builder<String, CoreApiException>().globalConfig(getGlobalConfig())
                .requestBuilder(requestBuilder -> requestBuilder.server("https://localhost:3000")
                        .path("/v2/bank-accounts")
                        .queryParam(param -> param.key("cursor").value("cursor").isRequired(false))
                        .formParam(param -> param.key("limit").value("limit").isRequired(false))
                        .templateParam(param -> param.key("location_id").value("locationId")
                                .shouldEncode(true).isRequired(false))
                        .headerParam(param -> param.key("accept").value("application/json"))
                        .authenticationKey("global").httpMethod(Method.GET))
                .responseHandler(responseHandler -> responseHandler
                        .deserializer(response -> CoreHelper.deserialize(response, String.class))
                        .localErrorCase("400",
                                ErrorCase.createErrorTemplate(
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
        return new ApiCall.Builder<String, CoreApiException>().globalConfig(getGlobalConfig())
                .requestBuilder(requestBuilder -> requestBuilder.server("https://localhost:3000")
                        .path("/v2/bank-accounts")
                        .queryParam(param -> param.key("cursor").value("cursor").isRequired(false))
                        .formParam(param -> param.key("limit").value("limit").isRequired(false))
                        .templateParam(param -> param.key("location_id").value("locationId")
                                .shouldEncode(true).isRequired(false))
                        .headerParam(param -> param.key("accept").value("application/json"))
                        .authenticationKey("global").httpMethod(Method.GET))
                .responseHandler(responseHandler -> responseHandler
                        .deserializer(response -> CoreHelper.deserialize(response, String.class))
                        .nullify404(false).globalErrorCase(getGlobalErrorCases()))
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
        return new ApiCall.Builder<String, CoreApiException>().globalConfig(getGlobalConfig())
                .requestBuilder(requestBuilder -> requestBuilder.server("https://localhost:3000")
                        .path("/v2/bank-accounts")
                        .queryParam(param -> param.key("cursor").value("cursor").isRequired(false))
                        .formParam(param -> param.key("limit").value("limit").isRequired(false))
                        .templateParam(param -> param.key("location_id").value("locationId")
                                .shouldEncode(true).isRequired(false))
                        .headerParam(param -> param.key("accept").value("application/json"))
                        .authenticationKey("global").httpMethod(Method.GET))
                .responseHandler(responseHandler -> responseHandler
                        .deserializer(response -> CoreHelper.deserialize(response, String.class))
                        .nullify404(false).globalErrorCase(getGlobalErrorCases()))
                .endpointConfiguration(
                        param -> param.arraySerializationFormat(ArraySerializationFormat.INDEXED)
                                .hasBinaryResponse(false).retryOption(RetryOption.DEFAULT))
                .build();
    }

    private GlobalConfiguration getGlobalConfig() {
        String userAgent = "APIMATIC 3.0";
        GlobalConfiguration globalConfig = new GlobalConfiguration.Builder()
                .authentication(Collections.emptyMap())
                .compatibilityFactory(getCompatibilityFactory()).httpClient(httpClient)
                .baseUri(server -> getBaseUri(server)).callback(callback).userAgent(userAgent)
                .userAgentConfig(Collections.emptyMap()).additionalHeaders(null)
                .globalHeader("version", "0.1").globalHeader("version", "1.2").build();
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
                any(StringBuilder.class), any(HttpHeaders.class), anyMap(), anyList()))
                        .thenReturn(coreHttpRequest);
        when(getCompatibilityFactory().createHttpContext(coreHttpRequest, response))
                .thenReturn(context);
        when(context.getResponse()).thenReturn(response);
        when(response.getStatusCode()).thenReturn(SUCCESS_CODE);
    }

    private Map<String, ErrorCase<CoreApiException>> getGlobalErrorCases() {
        Map<String, ErrorCase<CoreApiException>> globalErrorCase = new HashMap<>();
        globalErrorCase.put("400", ErrorCase.createErrorTemplate(
                "Failed to make the request, {$statusCode} {$response.body#/errors/0/code} - "
                        + "{$response.body#/errors/0/detail}",
                (reason, context) -> new GlobalTestException(reason, context)));

        globalErrorCase.put("404", ErrorCase.create("Not found",
                (reason, context) -> new CoreApiException(reason, context)));

        globalErrorCase.put("401", ErrorCase.createErrorTemplate(
                "Failed to make the request, {$response.header.content-type} "
                        + "{$response.body#/errors/0/code} - {$response.body#/errors/0/detail}",
                (reason, context) -> new CoreApiException(reason, context)));

        globalErrorCase.put("405", ErrorCase.createErrorTemplate(
                "Failed to make the request, {$response.header.accept} "
                        + "{$response.body#/errors/0/code} - {$response.body#/errors/0/detail}",
                (reason, context) -> new CoreApiException(reason, context)));

        globalErrorCase.put("500",
                ErrorCase.createErrorTemplate(
                        "Failed to make the request, http status code: {$statusCode}",
                        (reason, context) -> new CoreApiException(reason, context)));

        globalErrorCase.put("4XX",
                ErrorCase.createErrorTemplate(
                        "Failed to make the request, {$response.body#/errors/0/code} -"
                                + " {$response.body#/errors/0/detail}",
                        (reason, context) -> new CoreApiException(reason, context)));

        globalErrorCase.put(ErrorCase.DEFAULT,
                ErrorCase.create(
                        "Failed to make the request, {$response.body#/errors/0/code} - "
                                + "{$response.body#/errors/0/detail}",
                        (reason, context) -> new CoreApiException(reason, context)));

        return globalErrorCase;

    }

}
