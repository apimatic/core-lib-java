package apimatic.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.when;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.AbstractMap.SimpleEntry;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import apimatic.core.mocks.MockCoreConfig;
import apimatic.core.models.Employee;
import io.apimatic.core.ApiCall;
import io.apimatic.core.HttpRequest;
import io.apimatic.core.authentication.HeaderAuth;
import io.apimatic.core.authentication.QueryAuth;
import io.apimatic.core.utilities.CoreHelper;
import io.apimatic.core.utilities.LocalDateTimeHelper;
import io.apimatic.coreinterfaces.authentication.Authentication;
import io.apimatic.coreinterfaces.http.Callback;
import io.apimatic.coreinterfaces.http.HttpHeaders;
import io.apimatic.coreinterfaces.http.Method;
import io.apimatic.coreinterfaces.http.request.ArraySerializationFormat;
import io.apimatic.coreinterfaces.http.request.MutliPartRequestType;
import io.apimatic.coreinterfaces.http.request.Request;
import io.apimatic.coreinterfaces.type.CoreFileWrapper;

public class RequestBuilderTest extends MockCoreConfig {

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
     * Mock of List
     */
    @Mock
    private List<SimpleEntry<String, Object>> parameterList;

    /**
     * Mock of query parameters.
     */
    @Mock
    private Map<String, Object> queryParameters;

    /**
     * Mock of map of {@link Authentication}.
     */
    @Mock
    private Map<String, Authentication> authentications;

    /**
     * Mock of {@link HeaderAuth}.
     */
    @Mock
    private HeaderAuth authentication;

    /**
     * Mock of {@link Callback}.
     */
    @Mock
    private Callback httpCallback;

    /**
     * Mock of {@link CoreFileWrapper}.
     */
    @Mock
    private CoreFileWrapper fileWrapper;

    /**
     * Argument capture for void methods.
     */
    @Captor
    private ArgumentCaptor<Consumer<HttpRequest.Builder>> requestBuilder;

    /**
     * @throws IOException Signals that an I/O exception of some sort has occurred.
     */
    @Before
    public void setup() throws IOException {
        // MockitoAnnotations.openMocks(this);
        setExpectations();
    }

    @Test(expected = NullPointerException.class)
    public void testBodyParamValidation() throws IOException {
        // when
        new HttpRequest.Builder().httpMethod(Method.POST).bodyParam(param -> param.value(null))
                .build(getMockGlobalConfig());

    }

    @Test(expected = NullPointerException.class)
    public void testBodyParamValidation1() throws IOException {
        // when
        new HttpRequest.Builder().httpMethod(Method.POST).bodyParam(param -> param.value(null))
                .build(getMockGlobalConfig());

    }

    @Test
    public void testBodyParam() throws IOException {
        // when
        Request coreHttpRequest =
                new HttpRequest.Builder().httpMethod(Method.PATCH)
                        .bodyParam(param -> param.value("bodyValue")).build(getMockGlobalConfig());

        when(coreHttpRequest.getBody()).thenReturn("bodyValue");

        // verify
        assertEquals(coreHttpRequest.getBody(), "bodyValue");
    }

    @Test
    public void testBodyParamKey1() throws IOException {
        // when
        Request coreHttpRequest =
                new HttpRequest.Builder().httpMethod(Method.POST)
                        .bodyParam(param -> param.key("bodykey").value("bodyValue"))
                        .build(getMockGlobalConfig());

        // stub
        when(coreHttpRequest.getBody()).thenReturn("bodyValue");

        // verify
        assertEquals(coreHttpRequest.getBody(), "bodyValue");
    }

    @Test
    public void testBodyParamKey2() throws IOException {
        // when
        Request coreHttpRequest =
                new HttpRequest.Builder().httpMethod(Method.POST)
                        .bodyParam(param -> param.key("").value("bodyValue"))
                        .build(getMockGlobalConfig());

        // stub
        when(coreHttpRequest.getBody()).thenReturn("bodyValue");

        // verify
        assertEquals(coreHttpRequest.getBody(), "bodyValue");
    }

    @Test
    public void testBodyParamKey3() throws IOException {
        // when
        Request coreHttpRequest =
                new HttpRequest.Builder().httpMethod(Method.POST)
                        .bodyParam(param -> param.key(null).value("bodyValue"))
                        .build(getMockGlobalConfig());

        // stub
        when(coreHttpRequest.getBody()).thenReturn("bodyValue");

        // verify
        assertEquals(coreHttpRequest.getBody(), "bodyValue");
    }



    @Test(expected = NullPointerException.class)
    public void testHeaderParamValidation() throws IOException {
        // when
        new HttpRequest.Builder().headerParam(param -> param.key("accept"))
                .build(getMockGlobalConfig());
    }

    @Test
    public void testHeaderParamValidation1() throws IOException {
        Request coreHttpRequest =
                new HttpRequest.Builder().httpMethod(Method.GET)
                        .formParam(param -> param.key("formKey").value("value"))
                        .headerParam(param -> param.key("accept").value(null).isRequired(false))
                        .build(getMockGlobalConfig());

        when(coreHttpRequest.getHeaders()).thenReturn(getHttpHeaders());
        when(getHttpHeaders().value("accept")).thenReturn(null);

        assertFalse(coreHttpRequest.getHeaders().has("accept"));
    }

    @Test
    public void testHeaderParam() throws IOException {
        // when
        Request coreHttpRequest =
                new HttpRequest.Builder().httpMethod(Method.GET)
                        .formParam(param -> param.key("formKey").value("value"))
                        .headerParam(param -> param.key("accept").value("application/json"))
                        .build(getMockGlobalConfig());

        when(coreHttpRequest.getHeaders()).thenReturn(getHttpHeaders());
        when(getHttpHeaders().value("accept")).thenReturn("application/json");

        // verify
        assertEquals(coreHttpRequest.getHeaders().value("accept"), "application/json");
    }

    @Test
    public void testHeaderPrecedenceOverGlobal() throws IOException {
        Map<String, List<String>> headers = new HashMap<>();
        List<String> listOfheaders = Arrays.asList("image/png");
        headers.put("accept", listOfheaders);
        when(getMockGlobalConfig().getGlobalHeaders()).thenReturn(headers);
        // when
        Request coreHttpRequest =
                new HttpRequest.Builder().httpMethod(Method.GET)
                        .formParam(param -> param.key("formKey").value("value"))
                        .headerParam(param -> param.key("accept").value("application/json"))
                        .build(getMockGlobalConfig());

        when(coreHttpRequest.getHeaders()).thenReturn(getHttpHeaders());
        when(getHttpHeaders().value("accept")).thenReturn("application/json");

        String expected = "application/json";
        String actual = coreHttpRequest.getHeaders().value("accept");
        // verify
        assertEquals(actual, expected);
    }

    @Test
    public void testHeaderPrecedenceOverGlobal1() throws IOException {
        Map<String, List<String>> headers = new HashMap<>();
        List<String> listOfheaders = Arrays.asList("image/png");
        headers.put("content-type", listOfheaders);
        when(getMockGlobalConfig().getGlobalHeaders()).thenReturn(headers);
        // when
        Request coreHttpRequest =
                new HttpRequest.Builder().httpMethod(Method.GET)
                        .formParam(param -> param.key("formKey").value("value"))
                        .headerParam(param -> param.key("accept").value("application/json"))
                        .build(getMockGlobalConfig());

        when(coreHttpRequest.getHeaders()).thenReturn(getHttpHeaders());
        when(getHttpHeaders().value("accept")).thenReturn("application/json");
        when(getHttpHeaders().value("content-type")).thenReturn("image/png");

        String expectedContentType = "image/png";
        String actualContentType = coreHttpRequest.getHeaders().value("content-type");
        String expectedAcceptHeader = "application/json";
        String actualAcceptHeader = coreHttpRequest.getHeaders().value("accept");
        // verify
        assertEquals(actualContentType, expectedContentType);
        assertEquals(actualAcceptHeader, expectedAcceptHeader);
    }

    @Test
    public void testHeaderPrecedenceOverAdditional() throws IOException {
        Map<String, List<String>> headers = new HashMap<>();
        List<String> listOfheaders = Arrays.asList("image/png");
        headers.put("content-type", listOfheaders);
        when(getMockGlobalConfig().getAdditionalHeaders()).thenReturn(getHttpHeaders());
        Map<String, List<String>> headers2 = new HashMap<>();
        List<String> listOfheaders2 = Arrays.asList("application/json");
        headers2.put("content-type", listOfheaders2);
        when(getMockGlobalConfig().getGlobalHeaders()).thenReturn(headers2);

        // when
        Request coreHttpRequest =
                new HttpRequest.Builder().httpMethod(Method.GET)
                        .formParam(param -> param.key("formKey").value("value"))
                        .headerParam(param -> param.key("content-type").value("text/plain"))
                        .build(getMockGlobalConfig());

        when(coreHttpRequest.getHeaders()).thenReturn(getHttpHeaders());
        when(getHttpHeaders().value("content-type")).thenReturn("image/png");

        String expectedContentType = "image/png";
        String actualContentType = coreHttpRequest.getHeaders().value("content-type");

        // verify
        assertEquals(actualContentType, expectedContentType);
    }

    @Test
    public void testHeaderParamMultiple() throws IOException {
        // when
        Request coreHttpRequest =
                new HttpRequest.Builder().httpMethod(Method.GET)
                        .formParam(param -> param.key("formKey").value("value"))
                        .headerParam(param -> param.key("accept").value("application/json"))
                        .headerParam(param -> param.key("accept").value("text/plain"))
                        .build(getMockGlobalConfig());

        when(coreHttpRequest.getHeaders()).thenReturn(getHttpHeaders());
        when(getHttpHeaders().has("accept")).thenReturn(true);
        // verify
        assertTrue(coreHttpRequest.getHeaders().has("accept"));
    }

    @Test
    public void testHttpMethodParam() throws IOException {
        // when
        Request coreHttpRequest =
                new HttpRequest.Builder().httpMethod(Method.GET)
                        .formParam(param -> param.key("formKey").value("formValue"))
                        .build(getMockGlobalConfig());

        when(coreHttpRequest.getHttpMethod()).thenReturn(Method.POST);

        // verify
        assertEquals(coreHttpRequest.getHttpMethod(), Method.POST);
    }

    @Test(expected = NullPointerException.class)
    public void testFormParamValidation() throws IOException {
        // when
        new HttpRequest.Builder().formParam(param -> param.key("integers"))
                .build(getMockGlobalConfig());
    }

    @SuppressWarnings("unlikely-arg-type")
    @Test
    public void testFormParam() throws IOException {
        // when
        Request coreHttpRequest =
                new HttpRequest.Builder().httpMethod(Method.GET)
                        .formParam(param -> param.key("integers").value(1))
                        .arraySerializationFormat(ArraySerializationFormat.INDEXED)
                        .build(getMockGlobalConfig());
        // stub
        when(coreHttpRequest.getParameters()).thenReturn(parameterList);
        when(parameterList.contains("integers")).thenReturn(true);

        // verify
        assertTrue(coreHttpRequest.getParameters().contains("integers"));
    }

    @Test
    public void testFormParamEmptyFormParameter() throws IOException {
        // when
        Request coreHttpRequest =
                new HttpRequest.Builder().httpMethod(Method.GET).build(getMockGlobalConfig());
        // verify
        assertNull(coreHttpRequest);
    }

    @SuppressWarnings("unlikely-arg-type")
    @Test
    public void testFormParamModel() throws IOException {
        // Parameters for the API call
        Employee model = getEmployeeModel();

        Request coreHttpRequest =
                new HttpRequest.Builder().httpMethod(Method.GET)
                        .formParam(param -> param.key("employee").value(model))
                        .build(getMockGlobalConfig());

        // stub
        when(coreHttpRequest.getParameters()).thenReturn(parameterList);
        when(parameterList.contains("employee")).thenReturn(true);

        // verify
        assertTrue(coreHttpRequest.getParameters().contains("employee"));
    }

    @SuppressWarnings("unlikely-arg-type")
    @Test
    public void testFormParamOptional() throws IOException {
        // Parameters for the API call
        Employee model = getEmployeeModel();
        Map<String, Object> formParameters = new HashMap<String, Object>();
        formParameters.put("model", model);

        Request coreHttpRequest =
                new HttpRequest.Builder().httpMethod(Method.GET).formParam(formParameters)
                        .build(getMockGlobalConfig());

        // stub
        when(coreHttpRequest.getParameters()).thenReturn(parameterList);
        when(parameterList.contains("employee")).thenReturn(true);

        // verify
        assertTrue(coreHttpRequest.getParameters().contains("employee"));
    }


    @SuppressWarnings("unlikely-arg-type")
    @Test
    public void testFormParamMultiPartFile() throws IOException {
        // when
        Request coreHttpRequest =
                new HttpRequest.Builder().httpMethod(Method.GET)
                        .formParam(param -> param.key("file").value(fileWrapper)
                                .multipartHeaders("content-type", "application/octet-stream")
                                .multiPartRequestType(MutliPartRequestType.MULTI_PART_FILE))
                        .build(getMockGlobalConfig());
        // stub
        when(coreHttpRequest.getParameters()).thenReturn(parameterList);
        when(parameterList.contains("file")).thenReturn(true);

        // verify
        assertTrue(coreHttpRequest.getParameters().contains("file"));
    }


    @SuppressWarnings("unlikely-arg-type")
    @Test
    public void testMultiPartFileMultipleHeaders() throws IOException {
        // when
        Request coreHttpRequest =
                new HttpRequest.Builder().httpMethod(Method.GET)
                        .formParam(param -> param.key("file").value(fileWrapper)
                                .multipartHeaders("content-type", "application/octet-stream")
                                .multipartHeaders("content-type", "text/plain")
                                .multiPartRequestType(MutliPartRequestType.MULTI_PART_FILE))
                        .build(getMockGlobalConfig());

        // stub
        when(coreHttpRequest.getParameters()).thenReturn(parameterList);
        when(parameterList.contains("file")).thenReturn(true);

        // verify
        assertTrue(coreHttpRequest.getParameters().contains("file"));
    }

    @SuppressWarnings("unlikely-arg-type")
    @Test
    public void testFormParamMultiPart() throws IOException {
        List<Employee> models = getEmployeeModels();

        Request coreHttpRequest =
                new HttpRequest.Builder().httpMethod(Method.GET)
                        .formParam(param -> param.key("models").value(models)
                                .multipartSerializer(() -> CoreHelper.serialize(models))
                                .multipartHeaders("content-type", "application/octet-stream")
                                .multiPartRequestType(MutliPartRequestType.MULTI_PART))
                        .build(getMockGlobalConfig());

        // stub
        when(coreHttpRequest.getParameters()).thenReturn(parameterList);
        when(parameterList.contains("file")).thenReturn(true);

        // verify
        assertTrue(coreHttpRequest.getParameters().contains("file"));
    }

    @SuppressWarnings("unlikely-arg-type")
    @Test
    public void testTemplateParam() throws IOException {
        Request coreHttpRequest =
                new HttpRequest.Builder().httpMethod(Method.GET)
                        .formParam(param -> param.key("formKey").value("value"))
                        .templateParam(param -> param.key("integer").value(1))
                        .build(getMockGlobalConfig());
        // stub
        when(coreHttpRequest.getParameters()).thenReturn(parameterList);
        when(parameterList.contains("integer")).thenReturn(true);

        // verify
        assertTrue(coreHttpRequest.getParameters().contains("integer"));
    }

    @SuppressWarnings("unlikely-arg-type")
    @Test
    public void testTemplateParamWithoutEncoding() throws IOException {
        Request coreHttpRequest =
                new HttpRequest.Builder().httpMethod(Method.GET)
                        .formParam(param -> param.key("formKey").value("value"))
                        .templateParam(param -> param.key("integer").value(1).shouldEncode(false))
                        .build(getMockGlobalConfig());
        // stub
        when(coreHttpRequest.getParameters()).thenReturn(parameterList);
        when(parameterList.contains("integer")).thenReturn(true);


        // verify
        assertTrue(coreHttpRequest.getParameters().contains("integer"));
    }


    @SuppressWarnings("unlikely-arg-type")
    @Test
    public void testTemplateParamModel() throws IOException {
        // Parameters for the API call
        Employee model = getEmployeeModel();
        Request coreHttpRequest =
                new HttpRequest.Builder().httpMethod(Method.GET)
                        .formParam(param -> param.key("formKey").value("value").isRequired(false))
                        .templateParam(param -> param.key("model").value(model))
                        .build(getMockGlobalConfig());
        // stub
        when(coreHttpRequest.getParameters()).thenReturn(parameterList);
        when(parameterList.contains("model")).thenReturn(true);

        // verify
        assertTrue(coreHttpRequest.getParameters().contains("model"));
    }


    @Test(expected = NullPointerException.class)
    public void testTemplateParamValidation() throws IOException {
        new HttpRequest.Builder().templateParam(param -> param.key("model"))
                .build(getMockGlobalConfig());
    }



    @Test(expected = NullPointerException.class)
    public void testQueryParamValidation() throws IOException {
        new HttpRequest.Builder().queryParam(param -> param.key("query"))
                .build(getMockGlobalConfig());
    }

    @Test
    public void testQueryParam() throws IOException {
        when(getMockGlobalConfig().getHttpCallback()).thenReturn(null);
        Request coreHttpRequest =
                new HttpRequest.Builder().httpMethod(Method.GET)
                        .formParam(param -> param.key("form").value("formValue"))
                        .queryParam(param -> param.key("query").value("queryValue"))
                        .build(getMockGlobalConfig());

        when(coreHttpRequest.getQueryParameters()).thenReturn(queryParameters);
        when(queryParameters.get("query")).thenReturn("queryValue");

        // verify
        assertEquals(coreHttpRequest.getQueryParameters().get("query"), "queryValue");
    }


    @Test
    public void testQueryParamModelOptional() throws IOException {
        // Parameters for the API call
        Employee model = getEmployeeModel();
        Map<String, Object> optionalQueryParameters = new HashMap<String, Object>();
        optionalQueryParameters.put("model", model);

        Request coreHttpRequest =
                new HttpRequest.Builder().httpMethod(Method.GET)
                        .formParam(param -> param.key("form").value("formValue"))
                        .queryParam(optionalQueryParameters).build(getMockGlobalConfig());

        when(coreHttpRequest.getQueryParameters()).thenReturn(queryParameters);
        when(queryParameters.get("model")).thenReturn(model);

        // verify
        assertEquals(coreHttpRequest.getQueryParameters().get("model"), model);
    }

    @Test
    public void testQueryParamModel() throws IOException {
        // Parameters for the API call
        Employee model = getEmployeeModel();

        Request coreHttpRequest =
                new HttpRequest.Builder().httpMethod(Method.GET)
                        .formParam(param -> param.key("form").value("formValue"))
                        .queryParam(param -> param.key("model").value(model))
                        .build(getMockGlobalConfig());

        when(coreHttpRequest.getQueryParameters()).thenReturn(queryParameters);
        when(queryParameters.get("model")).thenReturn(model);

        // verify
        assertEquals(coreHttpRequest.getQueryParameters().get("model"), model);
    }


    @Test
    public void testbodySerializer() throws IOException {
        LocalDateTime dateTime = LocalDateTime.now();

        Request coreHttpRequest =
                new HttpRequest.Builder().httpMethod(Method.POST)
                        .bodyParam(param -> param.value(dateTime))
                        .bodySerializer(() -> CoreHelper.serialize(dateTime,
                                new LocalDateTimeHelper.UnixTimestampSerializer()))
                        .build(getMockGlobalConfig());

        when(coreHttpRequest.getBody()).thenReturn(
                CoreHelper.serialize(dateTime, new LocalDateTimeHelper.UnixTimestampSerializer()));

        // verify
        assertEquals(coreHttpRequest.getBody(),
                CoreHelper.serialize(dateTime, new LocalDateTimeHelper.UnixTimestampSerializer()));
    }

    @Test
    public void testBodyParamModel() throws IOException {
        // Parameters for the API call
        Employee model = getEmployeeModel();
        // when
        Request coreHttpRequest1 =
                new HttpRequest.Builder().httpMethod(Method.POST)
                        .bodyParam(param -> param.value(model)).build(getMockGlobalConfig());

        // stub
        when(coreHttpRequest1.getBody()).thenReturn(model);

        // verify
        assertEquals(coreHttpRequest1.getBody(), model);
    }

    @Test
    public void testBodyParamFileWrapper() throws IOException {
        Request coreHttpRequest =
                new HttpRequest.Builder().httpMethod(Method.GET)
                        .bodyParam(param -> param.value(fileWrapper)).build(getMockGlobalConfig());

        when(coreHttpRequest.getBody()).thenReturn(fileWrapper);

        // verify
        assertEquals(coreHttpRequest.getBody(), fileWrapper);
    }

    @Test
    public void testHeaderAuthentication() throws IOException {
        when(getCoreHttpRequest().getHeaders()).thenReturn(getHttpHeaders());
        when(authentications.get("global"))
                .thenReturn(new HeaderAuth(Collections.singletonMap("username", "password")));

        Request coreHttpRequest =
                new HttpRequest.Builder().server("https:\\localhost:3000").path("/auth/basic")
                        .formParam(param -> param.key("key").value("string"))
                        .authenticationKey("global").httpMethod(Method.GET)
                        .build(getMockGlobalConfig());


        when(getHttpHeaders().value("Authorization"))
                .thenReturn(CoreHelper.getBase64EncodedCredentials("username", "password"));

        // verify
        assertEquals(coreHttpRequest.getHeaders().value("Authorization"),
                CoreHelper.getBase64EncodedCredentials("username", "password"));
    }

    @Test(expected = IllegalStateException.class)
    public void testHeaderAuthenticationWithNull() throws IOException {
        when(getCoreHttpRequest().getHeaders()).thenReturn(getHttpHeaders());
        when(authentications.get("global"))
                .thenReturn(new HeaderAuth(Collections.singletonMap(null, null)));

        Request coreHttpRequest =
                new HttpRequest.Builder().server("https:\\localhost:3000").path("/auth/basic")
                        .formParam(param -> param.key("key").value("string"))
                        .authenticationKey("global").httpMethod(Method.GET)
                        .build(getMockGlobalConfig());
    }

    @Test(expected = IllegalStateException.class)
    public void testHeaderAuthenticationWithValueNull() throws IOException {
        when(getCoreHttpRequest().getHeaders()).thenReturn(getHttpHeaders());
        when(authentications.get("global"))
                .thenReturn(new HeaderAuth(Collections.singletonMap("authorization", null)));

        Request coreHttpRequest =
                new HttpRequest.Builder().server("https:\\localhost:3000").path("/auth/basic")
                        .formParam(param -> param.key("key").value("string"))
                        .authenticationKey("global").httpMethod(Method.GET)
                        .build(getMockGlobalConfig());
    }

    @Test
    public void testQueryAuthentication() throws IOException {
        Map<String, String> authParams = new HashMap<>();
        authParams.put("token", "api-token");
        authParams.put("api-key", "apikey");
        when(authentications.get("global")).thenReturn(new QueryAuth(authParams));
        Request coreHttpRequest =
                new HttpRequest.Builder().server("https:\\localhost:3000").path("/auth/basic")
                        .formParam(param -> param.key("key").value("string"))
                        .authenticationKey("global").httpMethod(Method.GET)
                        .build(getMockGlobalConfig());

        when(coreHttpRequest.getQueryParameters()).thenReturn(queryParameters);
        when(queryParameters.get("token")).thenReturn("api-token");
        when(queryParameters.get("api-key")).thenReturn("apikey");

        // verify
        assertEquals(coreHttpRequest.getQueryParameters().get("token"), "api-token");
        assertEquals(coreHttpRequest.getQueryParameters().get("api-key"), "apikey");
    }

    @Test(expected = IllegalStateException.class)
    public void testQueryAuthenticationWithNull() throws IOException {
        Map<String, String> authParams = new HashMap<>();
        authParams.put(null, null);
        authParams.put("api-key", "apikey");
        when(authentications.get("global")).thenReturn(new QueryAuth(authParams));
        Request coreHttpRequest =
                new HttpRequest.Builder().server("https:\\localhost:3000").path("/auth/basic")
                        .formParam(param -> param.key("key").value("string"))
                        .authenticationKey("global").httpMethod(Method.GET)
                        .build(getMockGlobalConfig());
    }

    @Test(expected = IllegalStateException.class)
    public void testQueryAuthenticationWithKeyNull() throws IOException {
        Map<String, String> authParams = new HashMap<>();
        authParams.put(null, "api-token");
        authParams.put("api-key", "apikey");
        when(authentications.get("global")).thenReturn(new QueryAuth(authParams));
        Request coreHttpRequest =
                new HttpRequest.Builder().server("https:\\localhost:3000").path("/auth/basic")
                        .formParam(param -> param.key("key").value("string"))
                        .authenticationKey("global").httpMethod(Method.GET)
                        .build(getMockGlobalConfig());
    }

    @Test(expected = IllegalStateException.class)
    public void testQueryAuthenticationWithValueNull() throws IOException {
        Map<String, String> authParams = new HashMap<>();
        authParams.put("token", null);
        authParams.put("api-key", "apikey");
        when(authentications.get("global")).thenReturn(new QueryAuth(authParams));
        Request coreHttpRequest =
                new HttpRequest.Builder().server("https:\\localhost:3000").path("/auth/basic")
                        .formParam(param -> param.key("key").value("string"))
                        .authenticationKey("global").httpMethod(Method.GET)
                        .build(getMockGlobalConfig());
    }

    @Test
    public void testEmptyAuthenticatioMap() throws IOException {
        when(getMockGlobalConfig().getAuthentications()).thenReturn(null);

        Request coreHttpRequest =
                new HttpRequest.Builder().server("https:\\localhost:3000").path("/auth/basic")
                        .formParam(param -> param.key("key").value("string"))
                        .authenticationKey("global").httpMethod(Method.GET)
                        .build(getMockGlobalConfig());

        when(coreHttpRequest.getQueryParameters()).thenReturn(queryParameters);
        when(queryParameters.get("token")).thenReturn(null);
        when(queryParameters.get("api-key")).thenReturn(null);

        // verify
        assertNull(coreHttpRequest.getQueryParameters().get("token"));
        assertNull(coreHttpRequest.getQueryParameters().get("api-key"));
    }

    private void prepareCoreConfigStub() {
        when(getMockGlobalConfig().getBaseUri()).thenReturn(test -> getBaseUri(test));
        when(getMockGlobalConfig().getCompatibilityFactory()).thenReturn(getCompatibilityFactory());
        when(getMockGlobalConfig().getAuthentications()).thenReturn(authentications);
        when(getMockGlobalConfig().getHttpCallback()).thenReturn(httpCallback);
        when(getMockGlobalConfig().getAdditionalHeaders()).thenReturn(getHttpHeaders());
    }

    private void setExpectations() throws IOException {
        prepareCoreConfigStub();
        prepareCompatibilityStub();
    }

    private void prepareCompatibilityStub() {
        when(getCompatibilityFactory().createHttpHeaders(anyMap())).thenReturn(getHttpHeaders());
        when(getCompatibilityFactory().createHttpRequest(any(Method.class),
                any(StringBuilder.class), any(HttpHeaders.class), anyMap(), any(Object.class)))
                        .thenReturn(getCoreHttpRequest());

        when(getCompatibilityFactory().createHttpRequest(any(Method.class),
                any(StringBuilder.class), any(HttpHeaders.class), anyMap(), anyList()))
                        .thenReturn(getCoreHttpRequest());
    }

    private Employee getEmployeeModel() throws IOException {
        return CoreHelper.deserialize(
                "{\"name\":\"Shahid Khaliq\",\"age\":5147483645,\"address\":\"H # 531, S # 20\","
                        + "\"uid\":\"123321\",\"birthday\":\"1994-02-13\",\"birthtime\":"
                        + "\"1994-02-13T14:01:54.9571247Z\",\"salary\":20000,\"department\":"
                        + "\"Software Development\",\"joiningDay\":\"Saturday\",\"workingDays\":"
                        + "[\"Monday\",\"Tuesday\",\"Friday\"],\"boss\":{\"personType\":\"Boss\","
                        + "\"name\":\"Zeeshan Ejaz\",\"age\":5147483645,\"address\":\"H # 531,"
                        + " S # 20\",\"uid\":\"123321\",\"birthday\":\"1994-02-13\",\"birthtime\""
                        + ":\"1994-02-13T14:01:54.9571247Z\",\"salary\":20000,\"department\":"
                        + "\"Software Development\",\"joiningDay\":\"Saturday\",\"workingDays\":"
                        + "[\"Monday\",\"Tuesday\",\"Friday\"],\"dependents\":[{\"name\":\"Future"
                        + " Wife\",\"age\":5147483649,\"address\":\"H # 531, S # 20\",\"uid\":"
                        + "\"123412\",\"birthday\":\"1994-02-13\",\"birthtime\":\"1994-02-13T14"
                        + ":01:54.9571247Z\"},{\"name\":\"Future Kid\",\"age\":5147483648,"
                        + "\"address\":\"H # 531, S # 20\",\"uid\":\"312341\",\"birthday\":"
                        + "\"1994-02-13\",\"birthtime\":\"1994-02-13T14:01:54.9571247Z\"}],"
                        + "\"hiredAt\":\"Sun, 06 Nov 1994 08:49:37 GMT\",\"promotedAt\":"
                        + "1484719381},\"dependents\":[{\"name\":\"Future Wife\",\"age\":"
                        + "5147483649,\"address\":\"H # 531, S # 20\",\"uid\":\"123412\","
                        + "\"birthday\":\"1994-02-13\",\"birthtime\":\"1994-02-13T14:01:"
                        + "54.9571247Z\"},{\"name\":\"Future Kid\",\"age\":5147483648,\"address\""
                        + ":\"H # 531, S # 20\",\"uid\":\"312341\",\"birthday\":\"1994-02-13\","
                        + "\"birthtime\":\"1994-02-13T14:01:54.9571247Z\"}],\"hiredAt\":"
                        + "\"Sun, 06 Nov 1994 08:49:37 GMT\"}",
                Employee.class);
    }

    private List<Employee> getEmployeeModels() throws IOException {
        return CoreHelper.deserializeArray(
                "[{\"name\":\"Shahid Khaliq\",\"age\":5147483645,\"address\":\"H # 531, S # 20\","
                        + "\"uid\":\"123321\",\"birthday\":\"1994-02-13\",\"birthtime\":"
                        + "\"1994-02-13T14:01:54.9571247Z\",\"salary\":20000,\"department\":"
                        + "\"Software Development\",\"joiningDay\":\"Saturday\",\"workingDays\":"
                        + "[\"Monday\",\"Tuesday\",\"Friday\"],\"boss\":{\"personType\":\"Boss\","
                        + "\"name\":\"Zeeshan Ejaz\",\"age\":5147483645,\"address\":\"H # 531, "
                        + "S # 20\",\"uid\":\"123321\",\"birthday\":\"1994-02-13\",\"birthtime\""
                        + ":\"1994-02-13T14:01:54.9571247Z\",\"salary\":20000,\"department\":"
                        + "\"Software Development\",\"joiningDay\":\"Saturday\",\"workingDays\":"
                        + "[\"Monday\",\"Tuesday\",\"Friday\"],\"dependents\":[{\"name\":\"Future"
                        + " Wife\",\"age\":5147483649,\"address\":\"H # 531, S # 20\",\"uid\":"
                        + "\"123412\",\"birthday\":\"1994-02-13\",\"birthtime\":\"1994-02-13T14:"
                        + "01:54.9571247Z\"},{\"name\":\"Future Kid\",\"age\":5147483648,"
                        + "\"address\":\"H # 531, S # 20\",\"uid\":\"312341\",\"birthday\":"
                        + "\"1994-02-13\",\"birthtime\":\"1994-02-13T14:01:54.9571247Z\"}],"
                        + "\"hiredAt\":\"Sun, 06 Nov 1994 08:49:37 GMT\",\"promotedAt\":"
                        + "1484719381},\"dependents\":[{\"name\":\"Future Wife\",\"age\":"
                        + "5147483649,\"address\":\"H # 531, S # 20\",\"uid\":\"123412\","
                        + "\"birthday\":\"1994-02-13\",\"birthtime\":\"1994-02-13T14:01:54"
                        + ".9571247Z\"},{\"name\":\"Future Kid\",\"age\":5147483648,\"address\""
                        + ":\"H # 531, S # 20\",\"uid\":\"312341\",\"birthday\":\"1994-02-13\","
                        + "\"birthtime\":\"1994-02-13T14:01:54.9571247Z\"}],\"hiredAt\":"
                        + "\"Sun, 06 Nov 1994 08:49:37 GMT\"},{\"name\":\"Shahid Khaliq"
                        + "\",\"age\":5147483645,\"address\":\"H # 531, S # 20\",\"uid\":"
                        + "\"123321\",\"birthday\":\"1994-02-13\",\"birthtime\":"
                        + "\"1994-02-13T14:01:54.9571247Z\",\"salary\":20000,\"department\":"
                        + "\"Software Development\",\"joiningDay\":\"Saturday\",\"workingDays\""
                        + ":[\"Monday\",\"Tuesday\",\"Friday\"],\"boss\":{\"personType\":\"Boss\","
                        + "\"name\":\"Zeeshan Ejaz\",\"age\":5147483645,\"address\":\"H # 531,"
                        + " S # 20\",\"uid\":\"123321\",\"birthday\":\"1994-02-13\",\"birthtime\""
                        + ":\"1994-02-13T14:01:54.9571247Z\",\"salary\":20000,\"department\":"
                        + "\"Software Development\",\"joiningDay\":\"Saturday\",\"workingDays\":"
                        + "[\"Monday\",\"Tuesday\",\"Friday\"],\"dependents\":[{\"name\":"
                        + "\"Future Wife\",\"age\":5147483649,\"address\":\"H # 531, S # 20"
                        + "\",\"uid\":\"123412\",\"birthday\":\"1994-02-13\",\"birthtime\""
                        + ":\"1994-02-13T14:01:54.9571247Z\"},{\"name\":\"Future Kid\","
                        + "\"age\":5147483648,\"address\":\"H # 531, S # 20\",\"uid\":"
                        + "\"312341\",\"birthday\":\"1994-02-13\",\"birthtime\":\"1994-02-13T14"
                        + ":01:54.9571247Z\"}],\"hiredAt\":\"Sun, 06 Nov 1994 08:49:37 GMT\","
                        + "\"promotedAt\":1484719381},\"dependents\":[{\"name\":\"Future Wife\""
                        + ",\"age\":5147483649,\"address\":\"H # 531, S # 20\",\"uid\":\"123412\""
                        + ",\"birthday\":\"1994-02-13\",\"birthtime\":\"1994-02-13T14:01:54."
                        + "9571247Z\"},{\"name\":\"Future Kid\",\"age\":5147483648,\"address\":"
                        + "\"H # 531, S # 20\",\"uid\":\"312341\",\"birthday\":\"1994-02-13\","
                        + "\"birthtime\":\"1994-02-13T14:01:54.9571247Z\"}],\"hiredAt\":\"Su"
                        + "n, 06 Nov 1994 08:49:37 GMT\"}]",
                Employee[].class);
    }
}
