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
import apimatic.core.models.DeleteBody;
import apimatic.core.models.TestModel;
import apimatic.core.utilities.MockCoreRequest;
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

public class ResponseHandlerTest extends MockCoreRequest {

    @Rule
    public MockitoRule initRule = MockitoJUnit.rule().silent();

    @Mock
    private ApiCall.Builder<?, ?> mockApiCallBuilder;

    @Mock
    private ApiCall<?, ?> mockApiCall;

    @Mock
    private HttpClient client;

    @Mock
    private CoreEndpointConfiguration endpointSetting;

    @Mock
    private ResponseHandler<?, ?> responseHandler;

    @Mock
    private Response coreHttpResponse;

    @Mock
    private Context context;

    @Mock
    private Callback httpCallback;

    @Mock
    private DynamicType dynamicType;

    @Mock
    private ApiResponseType<String> stringApiResponseType;

    @Mock
    private ApiResponseType<DynamicType> dynamicApiResponseType;

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
        when(coreHttpResponse.getStatusCode()).thenReturn(200);
        when(coreHttpResponse.getBody()).thenReturn("bodyValue");

        // verify
        assertEquals(coreResponseHandler.handle(coreHttpRequest, coreHttpResponse, mockGlobalConfig,
                endpointSetting), "bodyValue");
    }

    @Test
    public void testDeserializerMethodUsingRawBody() throws IOException, CoreApiException {
        ResponseHandler<InputStream, CoreApiException> coreResponseHandler =
                new ResponseHandler.Builder<InputStream, CoreApiException>().build();

        String responseString = "bodyValue";
        InputStream inputStream = new ByteArrayInputStream(responseString.getBytes());
        // stub

        when(endpointSetting.hasBinaryResponse()).thenReturn(true);
        when(coreHttpResponse.getStatusCode()).thenReturn(200);
        when(coreHttpResponse.getRawBody()).thenReturn(inputStream);

        // verify
        assertEquals(coreResponseHandler.handle(coreHttpRequest, coreHttpResponse, mockGlobalConfig,
                endpointSetting), inputStream);
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
        when(coreHttpResponse.getStatusCode()).thenReturn(200);
        when(coreHttpResponse.getBody()).thenReturn("{\"name\" : \"ali\", \"field\" : \"DEV\"}");

        // verify
        assertEquals(coreResponseHandler
                .handle(coreHttpRequest, coreHttpResponse, mockGlobalConfig, endpointSetting)
                .getContext(), model.getContext());
    }

    @Test
    public void testContextWithoutDeserializer() throws IOException, CoreApiException {
        ResponseHandler<TestModel, CoreApiException> coreResponseHandler =
                new ResponseHandler.Builder<TestModel, CoreApiException>().contextInitializer(
                        (context, response) -> response.toBuilder().httpContext(context).build())
                        .build();
        // stub
        when(coreHttpResponse.getStatusCode()).thenReturn(200);
        when(coreHttpResponse.getBody()).thenReturn("{\"name\" : \"ali\", \"field\" : \"DEV\"}");

        // verify
        assertNull(coreResponseHandler.handle(coreHttpRequest, coreHttpResponse, mockGlobalConfig,
                endpointSetting));
    }

    @Test
    public void testDynamicResponseTypeMethod() throws IOException, CoreApiException {
        ResponseHandler<DynamicType, CoreApiException> coreResponseHandler =
                new ResponseHandler.Builder<DynamicType, CoreApiException>()
                        .responseClassType(ResponseClassType.DYNAMIC_RESPONSE).build();

        // stub
        when(coreHttpResponse.getStatusCode()).thenReturn(200);
        when(coreHttpResponse.getBody()).thenReturn("bodyValue");

        // verify
        assertEquals(coreResponseHandler.handle(coreHttpRequest, coreHttpResponse, mockGlobalConfig,
                endpointSetting), dynamicType);
    }

    @Test
    public void testApiResponseTypeMethod() throws IOException, CoreApiException {
        ResponseHandler<ApiResponseType<String>, CoreApiException> coreResponseHandler =
                new ResponseHandler.Builder<ApiResponseType<String>, CoreApiException>()
                        .responseClassType(ResponseClassType.API_RESPONSE)
                        .apiResponseDeserializer(response -> new String(response)).build();
        // stub
        when(coreHttpResponse.getStatusCode()).thenReturn(201);
        when(coreHttpResponse.getHeaders()).thenReturn(httpHeaders);
        when(coreHttpResponse.getBody()).thenReturn("bodyValue");
        when(stringApiResponseType.getResult()).thenReturn("bodyValue");

        // verify
        assertEquals(coreResponseHandler
                .handle(coreHttpRequest, coreHttpResponse, mockGlobalConfig, endpointSetting)
                .getResult(), stringApiResponseType.getResult());
    }

    @Test
    public void testDynamicApiResponseTypeMethod() throws IOException, CoreApiException {
        ResponseHandler<ApiResponseType<DynamicType>, CoreApiException> coreResponseHandler =
                new ResponseHandler.Builder<ApiResponseType<DynamicType>, CoreApiException>()
                        .responseClassType(ResponseClassType.DYNAMIC_API_RESPONSE).build();
        // stub
        when(coreHttpResponse.getStatusCode()).thenReturn(201);
        when(coreHttpResponse.getHeaders()).thenReturn(httpHeaders);
        when(dynamicApiResponseType.getResult()).thenReturn(dynamicType);

        // verify
        assertEquals(coreResponseHandler
                .handle(coreHttpRequest, coreHttpResponse, mockGlobalConfig, endpointSetting)
                .getResult(), dynamicApiResponseType.getResult());
    }

    @Test
    public void testDefaultTypeMethod() throws IOException, CoreApiException {
        ResponseHandler<String, CoreApiException> coreResponseHandler =
                new ResponseHandler.Builder<String, CoreApiException>().build();
        // stub
        when(coreHttpResponse.getStatusCode()).thenReturn(201);
        // verify
        assertNull(coreResponseHandler.handle(coreHttpRequest, coreHttpResponse, mockGlobalConfig,
                endpointSetting));
    }

    @Test
    public void testNullify404() throws IOException, CoreApiException {
        ResponseHandler<String, CoreApiException> coreResponseHandler =
                new ResponseHandler.Builder<String, CoreApiException>().nullify404(true).build();
        // stub
        when(coreHttpResponse.getStatusCode()).thenReturn(404);
        when(compatibilityFactory.createHttpContext(coreHttpRequest, coreHttpResponse))
                .thenReturn(context);


        // verify
        assertNull(coreResponseHandler.handle(coreHttpRequest, coreHttpResponse, mockGlobalConfig,
                endpointSetting));
    }

    @Test
    public void testNullify404False() throws IOException, CoreApiException {
        ResponseHandler<String, CoreApiException> coreResponseHandler =
                new ResponseHandler.Builder<String, CoreApiException>().nullify404(false)
                        .globalErrorCase(getGlobalErrorCases()).build();
        // stub
        when(coreHttpResponse.getStatusCode()).thenReturn(404);

        CoreApiException apiException = assertThrows(CoreApiException.class, () -> {
            coreResponseHandler.handle(coreHttpRequest, coreHttpResponse, mockGlobalConfig,
                    endpointSetting);
        });

        String expectedMessage = "Not found";
        String actualMessage = apiException.getMessage();

        assertEquals(actualMessage, expectedMessage);
    }

    @Test
    public void testLocalException() throws IOException, CoreApiException {
        ResponseHandler<String, CoreApiException> coreResponseHandler =
                new ResponseHandler.Builder<String, CoreApiException>()
                        .localErrorCase("403",
                                ErrorCase.create("Forbidden",
                                        (reason, context) -> new CoreApiException(reason, context)))
                        .build();
        // stub
        when(coreHttpResponse.getStatusCode()).thenReturn(403);

        CoreApiException apiException = assertThrows(CoreApiException.class, () -> {
            coreResponseHandler.handle(coreHttpRequest, coreHttpResponse, mockGlobalConfig,
                    endpointSetting);
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
                                ErrorCase.create("Forbidden",
                                        (reason, context) -> new CoreApiException(reason, context)))
                        .globalErrorCase(getGlobalErrorCases()).build();

        // stub
        when(coreHttpResponse.getStatusCode()).thenReturn(199);

        CoreApiException apiException = assertThrows(CoreApiException.class, () -> {
            coreResponseHandler.handle(coreHttpRequest, coreHttpResponse, mockGlobalConfig,
                    endpointSetting);
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
                                ErrorCase.create("Forbidden",
                                        (reason, context) -> new CoreApiException(reason, context)))
                        .globalErrorCase(getGlobalErrorCases()).build();


        // stub
        when(coreHttpResponse.getStatusCode()).thenReturn(209);


        CoreApiException apiException = assertThrows(CoreApiException.class, () -> {
            coreResponseHandler.handle(coreHttpRequest, coreHttpResponse, mockGlobalConfig,
                    endpointSetting);
        });

        String expectedMessage = "Invalid response.";
        String actualMessage = apiException.getMessage();

        int expectedResponseCode = 209;
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
                "{\"ServerMessage\" : \"This is a message from server\" , \"ServerCode\" : 5000 }";
        InputStream exceptionResponseStream =
                new ByteArrayInputStream(exceptionResponse.getBytes());
        // stub
        when(coreHttpResponse.getStatusCode()).thenReturn(400);
        when(coreHttpResponse.getRawBody()).thenReturn(exceptionResponseStream);

        when(endpointSetting.hasBinaryResponse()).thenReturn(true);


        GlobalTestException apiException = assertThrows(GlobalTestException.class, () -> {
            coreResponseHandler.handle(coreHttpRequest, coreHttpResponse, mockGlobalConfig,
                    endpointSetting);
        });

        String expectedMessage = "Bad Request";
        String actualMessage = apiException.getMessage();

        String expectedServerMessage = "This is a message from server";
        String actualSeverMessage = apiException.getServerMessage();

        int expectedServerCode = 5000;
        int actualSeverCode = apiException.getServerCode();


        assertEquals(actualMessage, expectedMessage);
        assertEquals(actualSeverMessage, expectedServerMessage);
        assertEquals(actualSeverCode, expectedServerCode);
    }

    private Map<String, ErrorCase<CoreApiException>> getGlobalErrorCases() {

        Map<String, ErrorCase<CoreApiException>> globalErrorCase = new HashMap<>();
        globalErrorCase.put("400", ErrorCase.create("Bad Request",
                (reason, context) -> new GlobalTestException(reason, context)));

        globalErrorCase.put("404", ErrorCase.create("Not found",
                (reason, context) -> new CoreApiException(reason, context)));

        globalErrorCase.put(ErrorCase.DEFAULT, ErrorCase.create("Invalid response.",
                (reason, context) -> new CoreApiException(reason, context)));

        return globalErrorCase;

    }


    private void prepareCoreConfigStub() throws IOException {
        when(mockGlobalConfig.getBaseUri()).thenReturn(test -> getBaseUri(test));
        when(mockGlobalConfig.getCompatibilityFactory()).thenReturn(compatibilityFactory);
        when(endpointSetting.hasBinaryResponse()).thenReturn(false);
    }

    private void setExpectations() throws IOException {
        prepareCoreConfigStub();
        prepareCompatibilityStub();

        when(context.getResponse()).thenReturn(coreHttpResponse);
    }

    private void prepareCompatibilityStub() {
        when(compatibilityFactory.createHttpHeaders(anyMap())).thenReturn(httpHeaders);
        when(compatibilityFactory.createHttpRequest(any(Method.class), any(StringBuilder.class),
                any(HttpHeaders.class), anyMap(), any(Object.class))).thenReturn(coreHttpRequest);
        when(compatibilityFactory.createHttpRequest(any(Method.class), any(StringBuilder.class),
                any(HttpHeaders.class), anyMap(), anyList())).thenReturn(coreHttpRequest);
        when(compatibilityFactory.createHttpContext(coreHttpRequest, coreHttpResponse))
                .thenReturn(context);

        when(compatibilityFactory.createDynamicResponse(coreHttpResponse)).thenReturn(dynamicType);

        when(compatibilityFactory.createApiResponse(any(int.class), any(HttpHeaders.class),
                any(String.class))).thenReturn(stringApiResponseType);
        when(compatibilityFactory.createApiResponse(any(int.class), any(HttpHeaders.class),
                any(DynamicType.class))).thenReturn(dynamicApiResponseType);
    }


}

