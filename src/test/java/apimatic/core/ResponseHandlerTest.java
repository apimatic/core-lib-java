package apimatic.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.when;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
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
import apimatic.core.models.TestModel;
import io.apimatic.core.ApiCall;
import io.apimatic.core.ErrorCase;
import io.apimatic.core.ResponseHandler;
import io.apimatic.core.types.CoreApiException;
import io.apimatic.core.utilities.CoreHelper;
import io.apimatic.coreinterfaces.http.Callback;
import io.apimatic.coreinterfaces.http.Context;
import io.apimatic.coreinterfaces.http.HttpClient;
import io.apimatic.coreinterfaces.http.HttpHeaders;
import io.apimatic.coreinterfaces.http.Method;
import io.apimatic.coreinterfaces.http.request.ResponseClassType;
import io.apimatic.coreinterfaces.http.request.configuration.CoreEndpointConfiguration;
import io.apimatic.coreinterfaces.http.response.ApiResponseType;
import io.apimatic.coreinterfaces.http.response.DynamicType;
import io.apimatic.coreinterfaces.http.response.Response;

public class ResponseHandlerTest extends MockCoreConfig {

    /**
     * not found status code.
     */
    private static final int NOT_FOUND_STATUS_CODE = 404;

    /**
     * Internal server error.
     */
    private static final int INTERNAL_SERVER_ERROR = 500;

    /**
     * Information status code.
     */
    private static final int INFORMATION_CODE = 199;

    /**
     * Received status code
     */
    private static final int RECEIVED_STATUS_CODE = 209;

    /**
     * Bad request code.
     */
    private static final int BAD_REQUEST_CODE = 400;

    /**
     * Forbidden status code.
     */
    private static final int FORBIDDEN_STATUS_CODE = 403;

    /**
     * Created success code
     */
    private static final int CREATED_SUCCESS_CODE = 201;

    /**
     * Success code.
     */
    private static final int SUCCESS_CODE = 200;

    /**
     * Initializes mocks annotated with Mock.
     */
    @Rule
    public MockitoRule initRule = MockitoJUnit.rule().silent();

    /**
     * Mock of {@link ApiCall.Builder}.
     */
    @Mock
    private ApiCall.Builder<?, ?> mockApiCallBuilder;

    /**
     * Mock of {@link ApiCall}.
     */
    @Mock
    private ApiCall<?, ?> mockApiCall;

    /**
     * Mock of {@link HttpClient}.
     */
    @Mock
    private HttpClient client;

    /**
     * Mock of {@link CoreEndpointConfiguration}.
     */
    @Mock
    private CoreEndpointConfiguration endpointSetting;

    /**
     * Mock of {@link ResponseHandler}.
     */
    @Mock
    private ResponseHandler<?, ?> responseHandler;

    /**
     * Mock of {@link Response}.
     */
    @Mock
    private Response coreHttpResponse;

    /**
     * Mock of {@link Context}.
     */
    @Mock
    private Context context;

    /**
     * Mock of {@link Callback}.
     */
    @Mock
    private Callback httpCallback;

    /**
     * Mock of {@link DynamicType}.
     */
    @Mock
    private DynamicType dynamicType;

    /**
     * Mock of {@link ApiResponseType} of String type.
     */
    @Mock
    private ApiResponseType<String> stringApiResponseType;

    /**
     * Mock of {@link ApiResponseType} of {@link DynamicType}.
     */
    @Mock
    private ApiResponseType<DynamicType> dynamicApiResponseType;

    /**
     * @throws IOException Signals that an I/O exception of some sort has occurred.
     */
    @Before
    public void setup() throws IOException {
        setExpectations();
    }

    @Test
    public void testDeserializerMethod() throws IOException, CoreApiException {
        ResponseHandler<String, CoreApiException> coreResponseHandler =
                new ResponseHandler.Builder<String, CoreApiException>()
                        .deserializer(string -> new String(string)).build();
        // stub
        when(coreHttpResponse.getStatusCode()).thenReturn(SUCCESS_CODE);
        when(coreHttpResponse.getBody()).thenReturn("bodyValue");

        // verify
        assertEquals(coreResponseHandler.handle(getCoreHttpRequest(), coreHttpResponse,
                getMockGlobalConfig(), endpointSetting), "bodyValue");
    }

    @Test
    public void testDeserializerMethodUsingRawBody() throws IOException, CoreApiException {
        ResponseHandler<InputStream, CoreApiException> coreResponseHandler =
                new ResponseHandler.Builder<InputStream, CoreApiException>().build();

        String responseString = "bodyValue";
        InputStream inputStream = new ByteArrayInputStream(responseString.getBytes());
        // stub

        when(endpointSetting.hasBinaryResponse()).thenReturn(true);
        when(coreHttpResponse.getStatusCode()).thenReturn(SUCCESS_CODE);
        when(coreHttpResponse.getRawBody()).thenReturn(inputStream);

        // verify
        assertEquals(coreResponseHandler.handle(getCoreHttpRequest(), coreHttpResponse,
                getMockGlobalConfig(), endpointSetting), inputStream);
    }


    @Test
    public void testDeserializerMethodUsingContext() throws IOException, CoreApiException {
        ResponseHandler<TestModel, CoreApiException> coreResponseHandler =
                new ResponseHandler.Builder<TestModel, CoreApiException>()
                        .deserializer(string -> CoreHelper.deserialize(string, TestModel.class))
                        .contextInitializer((context, response) -> response.toBuilder()
                                .httpContext(context).build())
                        .build();
        TestModel model = new TestModel.Builder("ali", "DEV").httpContext(context).build();

        // stub
        when(coreHttpResponse.getStatusCode()).thenReturn(SUCCESS_CODE);
        when(coreHttpResponse.getBody()).thenReturn("{\"name\" : \"ali\", \"field\" : \"DEV\"}");

        // verify
        assertEquals(coreResponseHandler.handle(getCoreHttpRequest(), coreHttpResponse,
                getMockGlobalConfig(), endpointSetting).getContext(), model.getContext());
    }

    @Test
    public void testContextWithoutDeserializer() throws IOException, CoreApiException {
        ResponseHandler<TestModel, CoreApiException> coreResponseHandler =
                new ResponseHandler.Builder<TestModel, CoreApiException>().contextInitializer(
                        (context, response) -> response.toBuilder().httpContext(context).build())
                        .build();
        // stub
        when(coreHttpResponse.getStatusCode()).thenReturn(SUCCESS_CODE);
        when(coreHttpResponse.getBody()).thenReturn("{\"name\" : \"ali\", \"field\" : \"DEV\"}");

        // verify
        assertNull(coreResponseHandler.handle(getCoreHttpRequest(), coreHttpResponse,
                getMockGlobalConfig(), endpointSetting));
    }

    @Test
    public void testDynamicResponseTypeMethod() throws IOException, CoreApiException {
        ResponseHandler<DynamicType, CoreApiException> coreResponseHandler =
                new ResponseHandler.Builder<DynamicType, CoreApiException>()
                        .responseClassType(ResponseClassType.DYNAMIC_RESPONSE).build();

        // stub
        when(coreHttpResponse.getStatusCode()).thenReturn(SUCCESS_CODE);
        when(coreHttpResponse.getBody()).thenReturn("bodyValue");

        // verify
        assertEquals(coreResponseHandler.handle(getCoreHttpRequest(), coreHttpResponse,
                getMockGlobalConfig(), endpointSetting), dynamicType);
    }

    @Test
    public void testApiResponseTypeMethod() throws IOException, CoreApiException {
        ResponseHandler<ApiResponseType<String>, CoreApiException> coreResponseHandler =
                new ResponseHandler.Builder<ApiResponseType<String>, CoreApiException>()
                        .responseClassType(ResponseClassType.API_RESPONSE)
                        .apiResponseDeserializer(response -> new String(response)).build();
        // stub
        when(coreHttpResponse.getStatusCode()).thenReturn(CREATED_SUCCESS_CODE);
        when(coreHttpResponse.getHeaders()).thenReturn(getHttpHeaders());
        when(coreHttpResponse.getBody()).thenReturn("bodyValue");
        when(stringApiResponseType.getResult()).thenReturn("bodyValue");

        // verify
        assertEquals(
                coreResponseHandler.handle(getCoreHttpRequest(), coreHttpResponse,
                        getMockGlobalConfig(), endpointSetting).getResult(),
                stringApiResponseType.getResult());
    }

    @Test
    public void testDynamicApiResponseTypeMethod() throws IOException, CoreApiException {
        ResponseHandler<ApiResponseType<DynamicType>, CoreApiException> coreResponseHandler =
                new ResponseHandler.Builder<ApiResponseType<DynamicType>, CoreApiException>()
                        .responseClassType(ResponseClassType.DYNAMIC_API_RESPONSE).build();
        // stub
        when(coreHttpResponse.getStatusCode()).thenReturn(CREATED_SUCCESS_CODE);
        when(coreHttpResponse.getHeaders()).thenReturn(getHttpHeaders());
        when(dynamicApiResponseType.getResult()).thenReturn(dynamicType);

        // verify
        assertEquals(
                coreResponseHandler.handle(getCoreHttpRequest(), coreHttpResponse,
                        getMockGlobalConfig(), endpointSetting).getResult(),
                dynamicApiResponseType.getResult());
    }

    @Test
    public void testDefaultTypeMethod() throws IOException, CoreApiException {
        ResponseHandler<String, CoreApiException> coreResponseHandler =
                new ResponseHandler.Builder<String, CoreApiException>().build();
        // stub
        when(coreHttpResponse.getStatusCode()).thenReturn(CREATED_SUCCESS_CODE);
        // verify
        assertNull(coreResponseHandler.handle(getCoreHttpRequest(), coreHttpResponse,
                getMockGlobalConfig(), endpointSetting));
    }

    @Test
    public void testNullify404() throws IOException, CoreApiException {
        ResponseHandler<String, CoreApiException> coreResponseHandler =
                new ResponseHandler.Builder<String, CoreApiException>().nullify404(true).build();
        // stub
        when(coreHttpResponse.getStatusCode()).thenReturn(NOT_FOUND_STATUS_CODE);
        when(getCompatibilityFactory().createHttpContext(getCoreHttpRequest(), coreHttpResponse))
                .thenReturn(context);


        // verify
        assertNull(coreResponseHandler.handle(getCoreHttpRequest(), coreHttpResponse,
                getMockGlobalConfig(), endpointSetting));
    }

    @Test
    public void testNullify404False() throws IOException, CoreApiException {
        ResponseHandler<String, CoreApiException> coreResponseHandler =
                new ResponseHandler.Builder<String, CoreApiException>().nullify404(false)
                        .globalErrorCase(getGlobalErrorCases()).build();
        // stub
        when(coreHttpResponse.getStatusCode()).thenReturn(NOT_FOUND_STATUS_CODE);

        CoreApiException apiException = assertThrows(CoreApiException.class, () -> {
            coreResponseHandler.handle(getCoreHttpRequest(), coreHttpResponse,
                    getMockGlobalConfig(), endpointSetting);
        });

        String expectedMessage = "Not found";
        String actualMessage = apiException.getMessage();

        assertEquals(actualMessage, expectedMessage);
    }

    @Test
    public void testNullableResponseType() throws IOException, CoreApiException {
        ResponseHandler<String, CoreApiException> coreResponseHandler =
                new ResponseHandler.Builder<String, CoreApiException>()
                        .nullableResponseType(true).build();
        // stub
        when(coreHttpResponse.getStatusCode()).thenReturn(SUCCESS_CODE);
        when(coreHttpResponse.getBody()).thenReturn(null);
        when(getCompatibilityFactory().createHttpContext(getCoreHttpRequest(), coreHttpResponse))
                .thenReturn(context);

        // verify
        assertNull(coreResponseHandler.handle(getCoreHttpRequest(), coreHttpResponse,
                getMockGlobalConfig(), endpointSetting));

        when(coreHttpResponse.getBody()).thenReturn("");

        // verify
        assertNull(coreResponseHandler.handle(getCoreHttpRequest(), coreHttpResponse,
                getMockGlobalConfig(), endpointSetting));

        when(coreHttpResponse.getBody()).thenReturn("    ");

        // verify
        assertNull(coreResponseHandler.handle(getCoreHttpRequest(), coreHttpResponse,
                getMockGlobalConfig(), endpointSetting));
    }

    @Test
    public void testNullableResponseTypeFalse() throws IOException, CoreApiException {
        ResponseHandler<Integer, CoreApiException> coreResponseHandler =
                new ResponseHandler.Builder<Integer, CoreApiException>().nullableResponseType(false)
                        .deserializer(response -> Integer.parseInt(response))
                        .globalErrorCase(getGlobalErrorCases()).build();
        // stub
        when(coreHttpResponse.getStatusCode()).thenReturn(SUCCESS_CODE);
        String body = "50";
        when(coreHttpResponse.getBody()).thenReturn(body);

        // verify
        assertEquals(Integer.valueOf(body), coreResponseHandler.handle(getCoreHttpRequest(),
                coreHttpResponse, getMockGlobalConfig(), endpointSetting));

        when(coreHttpResponse.getBody()).thenReturn("");

        assertThrows(NumberFormatException.class, () -> {
            coreResponseHandler.handle(getCoreHttpRequest(), coreHttpResponse,
                    getMockGlobalConfig(), endpointSetting);
        });

        when(coreHttpResponse.getBody()).thenReturn("       ");

        assertThrows(NumberFormatException.class, () -> {
            coreResponseHandler.handle(getCoreHttpRequest(), coreHttpResponse,
                    getMockGlobalConfig(), endpointSetting);
        });
    }

    @Test
    public void testLocalException() throws IOException, CoreApiException {
        ResponseHandler<String, CoreApiException> coreResponseHandler =
                new ResponseHandler.Builder<String, CoreApiException>()
                        .localErrorCase("403",
                                ErrorCase.setReason("Forbidden",
                                        (reason, context) -> new CoreApiException(reason, context)))
                        .build();
        // stub
        when(coreHttpResponse.getStatusCode()).thenReturn(FORBIDDEN_STATUS_CODE);

        CoreApiException apiException = assertThrows(CoreApiException.class, () -> {
            coreResponseHandler.handle(getCoreHttpRequest(), coreHttpResponse,
                    getMockGlobalConfig(), endpointSetting);
        });

        String expectedMessage = "Forbidden";
        String actualMessage = apiException.getMessage();

        assertEquals(actualMessage, expectedMessage);
    }

    @Test
    public void testDefaultException() throws IOException, CoreApiException {
        ResponseHandler<String, CoreApiException> coreResponseHandler =
                new ResponseHandler.Builder<String, CoreApiException>()
                        .localErrorCase("403",
                                ErrorCase.setReason("Forbidden",
                                        (reason, context) -> new CoreApiException(reason, context)))
                        .globalErrorCase(getGlobalErrorCases()).build();

        // stub
        when(coreHttpResponse.getStatusCode()).thenReturn(INFORMATION_CODE);

        CoreApiException apiException = assertThrows(CoreApiException.class, () -> {
            coreResponseHandler.handle(getCoreHttpRequest(), coreHttpResponse,
                    getMockGlobalConfig(), endpointSetting);
        });

        String expectedMessage = "Invalid response.";
        String actualMessage = apiException.getMessage();

        assertEquals(actualMessage, expectedMessage);
    }

    @Test
    public void testDefaultException1() throws IOException, CoreApiException {
        // when
        ResponseHandler<String, CoreApiException> coreResponseHandler =
                new ResponseHandler.Builder<String, CoreApiException>()
                        .localErrorCase("403",
                                ErrorCase.setReason("Forbidden",
                                        (reason, context) -> new CoreApiException(reason, context)))
                        .globalErrorCase(getGlobalErrorCases()).build();


        // stub
        when(coreHttpResponse.getStatusCode()).thenReturn(RECEIVED_STATUS_CODE);


        CoreApiException apiException = assertThrows(CoreApiException.class, () -> {
            coreResponseHandler.handle(getCoreHttpRequest(), coreHttpResponse,
                    getMockGlobalConfig(), endpointSetting);
        });

        String expectedMessage = "Invalid response.";
        String actualMessage = apiException.getMessage();

        int expectedResponseCode = RECEIVED_STATUS_CODE;
        int actualResponseCode = apiException.getResponseCode();

        Context expectedContext = context;
        Context actualContext = apiException.getHttpContext();

        assertEquals(actualContext, expectedContext);
        assertEquals(actualResponseCode, expectedResponseCode);
        assertEquals(actualMessage, expectedMessage);
    }

    @Test
    public void testGlobalException() throws IOException, CoreApiException {
        ResponseHandler<String, CoreApiException> coreResponseHandler =
                new ResponseHandler.Builder<String, CoreApiException>()
                        .globalErrorCase(getGlobalErrorCases()).build();

        String exceptionResponse =
                "{\"ServerMessage\" : \"Internal server error\" , \"ServerCode\" : 500 }";
        InputStream exceptionResponseStream =
                new ByteArrayInputStream(exceptionResponse.getBytes());
        // stub
        when(coreHttpResponse.getStatusCode()).thenReturn(BAD_REQUEST_CODE);
        when(coreHttpResponse.getRawBody()).thenReturn(exceptionResponseStream);

        when(endpointSetting.hasBinaryResponse()).thenReturn(true);


        GlobalTestException apiException = assertThrows(GlobalTestException.class, () -> {
            coreResponseHandler.handle(getCoreHttpRequest(), coreHttpResponse,
                    getMockGlobalConfig(), endpointSetting);
        });

        String expectedMessage = "Bad Request";
        String actualMessage = apiException.getMessage();

        String expectedServerMessage = "Internal server error";
        String actualSeverMessage = apiException.getServerMessage();

        int expectedServerCode = INTERNAL_SERVER_ERROR;
        int actualSeverCode = apiException.getServerCode();


        assertEquals(actualMessage, expectedMessage);
        assertEquals(actualSeverMessage, expectedServerMessage);
        assertEquals(actualSeverCode, expectedServerCode);
    }

    private Map<String, ErrorCase<CoreApiException>> getGlobalErrorCases() {

        Map<String, ErrorCase<CoreApiException>> globalErrorCase = new HashMap<>();
        globalErrorCase.put("400", ErrorCase.setReason("Bad Request",
                (reason, context) -> new GlobalTestException(reason, context)));

        globalErrorCase.put("404", ErrorCase.setReason("Not found",
                (reason, context) -> new CoreApiException(reason, context)));

        globalErrorCase.put(ErrorCase.DEFAULT, ErrorCase.setReason("Invalid response.",
                (reason, context) -> new CoreApiException(reason, context)));

        return globalErrorCase;

    }


    private void prepareCoreConfigStub() throws IOException {
        when(getMockGlobalConfig().getBaseUri()).thenReturn(test -> getBaseUri(test));
        when(getMockGlobalConfig().getCompatibilityFactory()).thenReturn(getCompatibilityFactory());
        when(endpointSetting.hasBinaryResponse()).thenReturn(false);
    }

    private void setExpectations() throws IOException {
        prepareCoreConfigStub();
        prepareCompatibilityStub();

        when(context.getResponse()).thenReturn(coreHttpResponse);
    }

    private void prepareCompatibilityStub() {
        when(getCompatibilityFactory().createHttpHeaders(anyMap())).thenReturn(getHttpHeaders());
        when(getCompatibilityFactory().createHttpRequest(any(Method.class),
                any(StringBuilder.class), any(HttpHeaders.class), anyMap(), any(Object.class)))
                        .thenReturn(getCoreHttpRequest());
        when(getCompatibilityFactory().createHttpRequest(any(Method.class),
                any(StringBuilder.class), any(HttpHeaders.class), anyMap(), anyList()))
                        .thenReturn(getCoreHttpRequest());
        when(getCompatibilityFactory().createHttpContext(getCoreHttpRequest(), coreHttpResponse))
                .thenReturn(context);

        when(getCompatibilityFactory().createDynamicResponse(coreHttpResponse))
                .thenReturn(dynamicType);

        when(getCompatibilityFactory().createApiResponse(any(int.class), any(HttpHeaders.class),
                any(String.class))).thenReturn(stringApiResponseType);
        when(getCompatibilityFactory().createApiResponse(any(int.class), any(HttpHeaders.class),
                any(DynamicType.class))).thenReturn(dynamicApiResponseType);
    }


}

