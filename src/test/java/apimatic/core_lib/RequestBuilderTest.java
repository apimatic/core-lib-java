package apimatic.core_lib;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.when;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.AbstractMap.SimpleEntry;
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
import com.fasterxml.jackson.core.JsonProcessingException;
import apimatic.core_lib.models.Employee;
import apimatic.core_lib.utilities.MockCoreRequest;
import io.apimatic.core_interfaces.authentication.Authentication;
import io.apimatic.core_interfaces.http.CoreHttpMethod;
import io.apimatic.core_interfaces.http.HttpCallback;
import io.apimatic.core_interfaces.http.HttpHeaders;
import io.apimatic.core_interfaces.http.request.CoreHttpRequest;
import io.apimatic.core_interfaces.http.request.MutliPartRequestType;
import io.apimatic.core_interfaces.type.FileWrapper;
import io.apimatic.core_lib.ApiCall;
import io.apimatic.core_lib.CoreRequest;
import io.apimatic.core_lib.authentication.HeaderAuth;
import io.apimatic.core_lib.authentication.QueryAuth;
import io.apimatic.core_lib.utilities.CoreHelper;
import io.apimatic.core_lib.utilities.LocalDateTimeHelper;

public class RequestBuilderTest extends MockCoreRequest {

    @Rule
    public MockitoRule initRule = MockitoJUnit.rule();

    @Mock
    private ApiCall.Builder<?, ?> mockApiCallBuilder;

    @Mock
    private ApiCall<?, ?> mockApiCall;

    @Mock
    private List<SimpleEntry<String, Object>> parameterList;

    @Mock
    private Map<String, Object> queryParameters;

    @Mock
    private Map<String, Authentication> authentications;

    @Mock
    private HeaderAuth authentication;

    @Mock
    private HttpCallback httpCallback;

    @Mock
    private FileWrapper fileWrapper;
    @Captor
    ArgumentCaptor<Consumer<CoreRequest.Builder>> requestBuilder;

    @Before
    public void setup() throws IOException {
        // MockitoAnnotations.openMocks(this);
        setExpectations();
    }

    @Test(expected = NullPointerException.class)
    public void testBodyParamValidation() throws IOException {

        // when
        CoreHttpRequest coreHttpRequest = new CoreRequest.Builder().httpMethod(CoreHttpMethod.POST)
                .body(param -> param.value(null)).build(mockCoreConfig);

    }

    @Test(expected = NullPointerException.class)
    public void testBodyParamValidation1() throws IOException {

        // when
        CoreHttpRequest coreHttpRequest = new CoreRequest.Builder().httpMethod(CoreHttpMethod.POST)
                .body(param -> param.value(null)).build(mockCoreConfig);

    }

    @Test
    public void testBodyParam() throws IOException {

        // when
        CoreHttpRequest coreHttpRequest = new CoreRequest.Builder().httpMethod(CoreHttpMethod.PATCH)
                .body(param -> param.value("bodyValue")).build(mockCoreConfig);
        when(coreHttpRequest.getBody()).thenReturn("bodyValue");

        // verify
        assertEquals(coreHttpRequest.getBody(), "bodyValue");
    }

    @Test
    public void testBodyParamKey1() throws IOException {

        // when
        CoreHttpRequest coreHttpRequest = new CoreRequest.Builder().httpMethod(CoreHttpMethod.POST)
                .body(param -> param.key("bodykey").value("bodyValue")).build(mockCoreConfig);

        // stub
        when(coreHttpRequest.getBody()).thenReturn("bodyValue");

        // verify
        assertEquals(coreHttpRequest.getBody(), "bodyValue");
    }

    @Test
    public void testBodyParamKey2() throws IOException {

        // when
        CoreHttpRequest coreHttpRequest = new CoreRequest.Builder().httpMethod(CoreHttpMethod.POST)
                .body(param -> param.key("").value("bodyValue")).build(mockCoreConfig);

        // stub
        when(coreHttpRequest.getBody()).thenReturn("bodyValue");

        // verify
        assertEquals(coreHttpRequest.getBody(), "bodyValue");
    }

    @Test
    public void testBodyParamKey3() throws IOException {

        // when
        CoreHttpRequest coreHttpRequest = new CoreRequest.Builder().httpMethod(CoreHttpMethod.POST)
                .body(param -> param.key(null).value("bodyValue")).build(mockCoreConfig);

        // stub
        when(coreHttpRequest.getBody()).thenReturn("bodyValue");

        // verify
        assertEquals(coreHttpRequest.getBody(), "bodyValue");
    }



    @Test(expected = NullPointerException.class)
    public void testHeaderParamValidation() throws IOException {

        // when
        CoreHttpRequest coreHttpRequest =
                new CoreRequest.Builder().body(param -> param.key("accept")).build(mockCoreConfig);
    }

    @Test
    public void testHeaderParam() throws IOException {
        // when
        CoreHttpRequest coreHttpRequest = new CoreRequest.Builder().httpMethod(CoreHttpMethod.GET)
                .formParams(param -> param.key("formKey").value("value"))
                .headerParam(param -> param.key("accept").value("application/json"))
                .build(mockCoreConfig);

        when(coreHttpRequest.getHeaders()).thenReturn(httpHeaders);
        when(httpHeaders.value("accept")).thenReturn("application/json");

        // verify
        assertEquals(coreHttpRequest.getHeaders().value("accept"), "application/json");
    }



    @Test
    public void testHeaderParamMultiple() throws IOException {
        // when
        CoreHttpRequest coreHttpRequest = new CoreRequest.Builder().httpMethod(CoreHttpMethod.GET)
                .formParams(param -> param.key("formKey").value("value"))
                .headerParam(param -> param.key("accept").value("application/json"))
                .headerParam(param -> param.key("accept").value("text/plain"))
                .build(mockCoreConfig);

        when(coreHttpRequest.getHeaders()).thenReturn(httpHeaders);
        when(httpHeaders.has("accept")).thenReturn(true);
        // verify
        assertTrue(coreHttpRequest.getHeaders().has("accept"));
    }


    @Test
    public void testHttpMethodParam() throws IOException {
        // when
        CoreHttpRequest coreHttpRequest = new CoreRequest.Builder().httpMethod(CoreHttpMethod.GET)
                .formParams(param -> param.key("formKey").value("formValue")).build(mockCoreConfig);

        when(coreHttpRequest.getHttpMethod()).thenReturn(CoreHttpMethod.POST);

        // verify
        assertEquals(coreHttpRequest.getHttpMethod(), CoreHttpMethod.POST);
    }


    @Test(expected = NullPointerException.class)
    public void testFormParamValidation() throws IOException {
        // when
        CoreHttpRequest coreHttpRequest = new CoreRequest.Builder()
                .formParams(param -> param.key("integers")).build(mockCoreConfig);
    }

    @SuppressWarnings("unlikely-arg-type")
    @Test
    public void testFormParam() throws IOException {
        // when
        CoreHttpRequest coreHttpRequest = new CoreRequest.Builder().httpMethod(CoreHttpMethod.GET)
                .formParams(param -> param.key("integers").value(1)).build(mockCoreConfig);
        // stub
        when(coreHttpRequest.getParameters()).thenReturn(parameterList);
        when(parameterList.contains("integers")).thenReturn(true);

        // verify
        assertTrue(coreHttpRequest.getParameters().contains("integers"));
    }

    @SuppressWarnings("unlikely-arg-type")
    @Test
    public void testFormParamModel() throws IOException {
        // Parameters for the API call
        Employee model = getEmployeeModel();

        CoreHttpRequest coreHttpRequest = new CoreRequest.Builder().httpMethod(CoreHttpMethod.GET)
                .formParams(param -> param.key("employee").value(model)).build(mockCoreConfig);

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
        CoreHttpRequest coreHttpRequest = new CoreRequest.Builder().httpMethod(CoreHttpMethod.GET)
                .formParams(param -> param.key("file").value(fileWrapper)
                        .multipartHeaders("content-type", "application/octet-stream")
                        .multiPartRequestType(MutliPartRequestType.MULTI_PART_FILE))
                .build(mockCoreConfig);
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
        CoreHttpRequest coreHttpRequest = new CoreRequest.Builder().httpMethod(CoreHttpMethod.GET)
                .formParams(param -> param.key("file").value(fileWrapper)
                        .multipartHeaders("content-type", "application/octet-stream")
                        .multipartHeaders("content-type", "text/plain")
                        .multiPartRequestType(MutliPartRequestType.MULTI_PART_FILE))
                .build(mockCoreConfig);

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

        CoreHttpRequest coreHttpRequest =
                new CoreRequest.Builder().httpMethod(CoreHttpMethod.GET).formParams(param -> {
                    try {
                        param.key("models").value(CoreHelper.serialize(models))
                                .multipartHeaders("content-type", "application/octet-stream")
                                .multiPartRequestType(MutliPartRequestType.MULTI_PART);
                    } catch (JsonProcessingException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                }).build(mockCoreConfig);

        // stub
        when(coreHttpRequest.getParameters()).thenReturn(parameterList);
        when(parameterList.contains("file")).thenReturn(true);

        // verify
        assertTrue(coreHttpRequest.getParameters().contains("file"));
    }

    @SuppressWarnings("unlikely-arg-type")
    @Test
    public void testTemplateParam() throws IOException {
        CoreHttpRequest coreHttpRequest = new CoreRequest.Builder().httpMethod(CoreHttpMethod.GET)
                .formParams(param -> param.key("formKey").value("value"))
                .templateParam(param -> param.key("integer").value(1)).build(mockCoreConfig);
        // stub
        when(coreHttpRequest.getParameters()).thenReturn(parameterList);
        when(parameterList.contains("integer")).thenReturn(true);

        // verify
        assertTrue(coreHttpRequest.getParameters().contains("integer"));
    }

    @SuppressWarnings("unlikely-arg-type")
    @Test
    public void testTemplateParamWithoutEncoding() throws IOException {
        CoreHttpRequest coreHttpRequest = new CoreRequest.Builder().httpMethod(CoreHttpMethod.GET)
                .formParams(param -> param.key("formKey").value("value"))
                .templateParam(param -> param.key("integer").value(1).shouldEncode(false))
                .build(mockCoreConfig);
        // stub
        when(coreHttpRequest.getParameters()).thenReturn(parameterList);
        when(parameterList.contains("integer")).thenReturn(true);


        // verify
        assertTrue(coreHttpRequest.getParameters().contains("integer"));
    }


    @SuppressWarnings("unlikely-arg-type")
    @Test
    public void testTemplateParamModel() throws IOException {
        when(mockCoreConfig.getGlobalHeaders()).thenReturn(null);
        // Parameters for the API call
        Employee model = getEmployeeModel();
        CoreHttpRequest coreHttpRequest = new CoreRequest.Builder().httpMethod(CoreHttpMethod.GET)
                .formParams(param -> param.key("formKey").value("value").isRequired(false))
                .templateParam(param -> param.key("model").value(model)).build(mockCoreConfig);
        // stub
        when(coreHttpRequest.getParameters()).thenReturn(parameterList);
        when(parameterList.contains("model")).thenReturn(true);

        // verify
        assertTrue(coreHttpRequest.getParameters().contains("model"));
    }


    @Test(expected = NullPointerException.class)
    public void testTemplateParamValidation() throws IOException {
        CoreHttpRequest coreHttpRequest = new CoreRequest.Builder()
                .templateParam(param -> param.key("model")).build(mockCoreConfig);
    }



    @Test(expected = NullPointerException.class)
    public void testQueryParamValidation() throws IOException {
        CoreHttpRequest coreHttpRequest = new CoreRequest.Builder()
                .queryParam(param -> param.key("query")).build(mockCoreConfig);
    }

    @Test
    public void testQueryParam() throws IOException {
        when(mockCoreConfig.getHttpCallback()).thenReturn(null);
        CoreHttpRequest coreHttpRequest = new CoreRequest.Builder().httpMethod(CoreHttpMethod.GET)
                .formParams(param -> param.key("form").value("formValue"))
                .queryParam(param -> param.key("query").value("queryValue")).build(mockCoreConfig);

        when(coreHttpRequest.getQueryParameters()).thenReturn(queryParameters);
        when(queryParameters.get("query")).thenReturn("queryValue");

        // verify
        assertEquals(coreHttpRequest.getQueryParameters().get("query"), "queryValue");
    }


    @Test
    public void testQueryParamModel() throws IOException {
        // Parameters for the API call
        Employee model = getEmployeeModel();

        CoreHttpRequest coreHttpRequest = new CoreRequest.Builder().httpMethod(CoreHttpMethod.GET)
                .formParams(param -> param.key("form").value("formValue"))
                .queryParam(param -> param.key("model").value(model)).build(mockCoreConfig);

        when(coreHttpRequest.getQueryParameters()).thenReturn(queryParameters);
        when(queryParameters.get("model")).thenReturn(model);

        // verify
        assertEquals(coreHttpRequest.getQueryParameters().get("model"), model);
    }

    @Test
    public void testbodySerializer() throws IOException {
        LocalDateTime dateTime = LocalDateTime.now();

        CoreHttpRequest coreHttpRequest = new CoreRequest.Builder().httpMethod(CoreHttpMethod.POST)
                .body(param -> param.value(dateTime)).bodySerializer(res -> CoreHelper
                        .serialize(res, new LocalDateTimeHelper.UnixTimestampSerializer()))
                .build(mockCoreConfig);

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
        CoreHttpRequest coreHttpRequest1 = new CoreRequest.Builder().httpMethod(CoreHttpMethod.POST)
                .body(param -> param.value(model)).build(mockCoreConfig);

        // stub
        when(coreHttpRequest1.getBody()).thenReturn(model);

        // verify
        assertEquals(coreHttpRequest1.getBody(), model);
    }

    @Test
    public void testBodyParamFileWrapper() throws IOException {
        CoreHttpRequest coreHttpRequest = new CoreRequest.Builder().httpMethod(CoreHttpMethod.GET)
                .body(param -> param.value(fileWrapper)).build(mockCoreConfig);

        when(coreHttpRequest.getBody()).thenReturn(fileWrapper);

        // verify
        assertEquals(coreHttpRequest.getBody(), fileWrapper);
    }

    @Test
    public void testHeaderAuthentication() throws IOException {
        when(coreHttpRequest.getHeaders()).thenReturn(httpHeaders);
        when(authentications.get("global"))
                .thenReturn(new HeaderAuth(Collections.singletonMap("username", "password")));

        CoreHttpRequest coreHttpRequest = new CoreRequest.Builder().server("https:\\localhost:3000")
                .path("/auth/basic").formParams(param -> param.key("key").value("string"))
                .authenticationKey("global").httpMethod(CoreHttpMethod.GET).build(mockCoreConfig);


        when(httpHeaders.value("Authorization"))
                .thenReturn(CoreHelper.getBase64EncodedCredentials("username", "password"));

        // verify
        assertEquals(coreHttpRequest.getHeaders().value("Authorization"),
                CoreHelper.getBase64EncodedCredentials("username", "password"));
    }

    @Test
    public void testQueryAuthentication() throws IOException {
        Map<String, String> authParams = new HashMap<>();
        authParams.put("token", "api-token");
        authParams.put("api-key", "apikey");
        when(authentications.get("global")).thenReturn(new QueryAuth(authParams));
        CoreHttpRequest coreHttpRequest = new CoreRequest.Builder().server("https:\\localhost:3000")
                .path("/auth/basic").formParams(param -> param.key("key").value("string"))
                .authenticationKey("global").httpMethod(CoreHttpMethod.GET).build(mockCoreConfig);

        when(coreHttpRequest.getQueryParameters()).thenReturn(queryParameters);
        when(queryParameters.get("token")).thenReturn("api-token");
        when(queryParameters.get("api-key")).thenReturn("apikey");

        // verify
        assertEquals(coreHttpRequest.getQueryParameters().get("token"), "api-token");
        assertEquals(coreHttpRequest.getQueryParameters().get("api-key"), "apikey");
    }

    private void prepareCoreConfigStub() {
        when(mockApiCall.getCoreConfig()).thenReturn(mockCoreConfig);
        when(mockCoreConfig.getBaseUri()).thenReturn(test -> getBaseUri(test));
        when(mockCoreConfig.getGlobalHeaders()).thenReturn(httpHeaders);
        when(mockCoreConfig.getCompatibilityFactory()).thenReturn(compatibilityFactory);
        when(mockCoreConfig.getAuthentications()).thenReturn(authentications);
        when(mockCoreConfig.getUserAgent()).thenReturn("APIMATIC3.0");
        when(mockCoreConfig.getHttpCallback()).thenReturn(httpCallback);
        when(mockCoreConfig.getGlobalHeaders()).thenReturn(httpHeaders);

        // doNothing().when(authentication.apply(coreHttpRequest));
    }

    private void setExpectations() throws IOException {
        prepareCoreConfigStub();
        prepareCompatibilityStub();
    }

    private void prepareCompatibilityStub() {
        when(compatibilityFactory.createHttpHeaders(anyMap())).thenReturn(httpHeaders);
        when(compatibilityFactory.createHttpRequest(any(CoreHttpMethod.class),
                any(StringBuilder.class), any(HttpHeaders.class), anyMap(), any(Object.class)))
                        .thenReturn(coreHttpRequest);

        when(compatibilityFactory.createHttpRequest(any(CoreHttpMethod.class),
                any(StringBuilder.class), any(HttpHeaders.class), anyMap(), anyList()))
                        .thenReturn(coreHttpRequest);

    }


    private Employee getEmployeeModel() throws IOException {
        return CoreHelper.deserialize(
                "{\"name\":\"Shahid Khaliq\",\"age\":5147483645,\"address\":\"H # 531, S # 20\",\"u"
                        + "id\":\"123321\",\"birthday\":\"1994-02-13\",\"birthtime\":\"1994-02-13T14:01:54."
                        + "9571247Z\",\"salary\":20000,\"department\":\"Software Development\",\"joiningDay"
                        + "\":\"Saturday\",\"workingDays\":[\"Monday\",\"Tuesday\",\"Friday\"],\"boss\":{"
                        + "\"personType\":\"Boss\",\"name\":\"Zeeshan Ejaz\",\"age\":5147483645,\"address"
                        + "\":\"H # 531, S # 20\",\"uid\":\"123321\",\"birthday\":\"1994-02-13\",\"birthtim"
                        + "e\":\"1994-02-13T14:01:54.9571247Z\",\"salary\":20000,\"department\":\"Software "
                        + "Development\",\"joiningDay\":\"Saturday\",\"workingDays\":[\"Monday\",\"Tuesday"
                        + "\",\"Friday\"],\"dependents\":[{\"name\":\"Future Wife\",\"age\":5147483649,\"ad"
                        + "dress\":\"H # 531, S # 20\",\"uid\":\"123412\",\"birthday\":\"1994-02-13\",\"bir"
                        + "thtime\":\"1994-02-13T14:01:54.9571247Z\"},{\"name\":\"Future Kid\",\"age\":5147"
                        + "483648,\"address\":\"H # 531, S # 20\",\"uid\":\"312341\",\"birthday\":\"1994-02"
                        + "-13\",\"birthtime\":\"1994-02-13T14:01:54.9571247Z\"}],\"hiredAt\":\"Sun, 06 Nov"
                        + " 1994 08:49:37 GMT\",\"promotedAt\":1484719381},\"dependents\":[{\"name\":\"Futu"
                        + "re Wife\",\"age\":5147483649,\"address\":\"H # 531, S # 20\",\"uid\":\"123412\","
                        + "\"birthday\":\"1994-02-13\",\"birthtime\":\"1994-02-13T14:01:54.9571247Z\"},{\"n"
                        + "ame\":\"Future Kid\",\"age\":5147483648,\"address\":\"H # 531, S # 20\",\"uid\":"
                        + "\"312341\",\"birthday\":\"1994-02-13\",\"birthtime\":\"1994-02-13T14:01:54.95712"
                        + "47Z\"}],\"hiredAt\":\"Sun, 06 Nov 1994 08:49:37 GMT\"}",
                Employee.class);
    }

    private List<Employee> getEmployeeModels() throws IOException {
        return CoreHelper.deserializeArray(
                "[{\"name\":\"Shahid Khaliq\",\"age\":5147483645,\"address\":\"H # 531, S # 20\","
                        + "\"uid\":\"123321\",\"birthday\":\"1994-02-13\",\"birthtime\":\"1994-02-13T14:01:"
                        + "54.9571247Z\",\"salary\":20000,\"department\":\"Software Development\",\"joining"
                        + "Day\":\"Saturday\",\"workingDays\":[\"Monday\",\"Tuesday\",\"Friday\"],\"boss\":"
                        + "{\"personType\":\"Boss\",\"name\":\"Zeeshan Ejaz\",\"age\":5147483645,\"address"
                        + "\":\"H # 531, S # 20\",\"uid\":\"123321\",\"birthday\":\"1994-02-13\",\"birthtim"
                        + "e\":\"1994-02-13T14:01:54.9571247Z\",\"salary\":20000,\"department\":\"Software "
                        + "Development\",\"joiningDay\":\"Saturday\",\"workingDays\":[\"Monday\",\"Tuesday"
                        + "\",\"Friday\"],\"dependents\":[{\"name\":\"Future Wife\",\"age\":5147483649,\"ad"
                        + "dress\":\"H # 531, S # 20\",\"uid\":\"123412\",\"birthday\":\"1994-02-13\",\"bir"
                        + "thtime\":\"1994-02-13T14:01:54.9571247Z\"},{\"name\":\"Future Kid\",\"age\":5147"
                        + "483648,\"address\":\"H # 531, S # 20\",\"uid\":\"312341\",\"birthday\":\"1994-02"
                        + "-13\",\"birthtime\":\"1994-02-13T14:01:54.9571247Z\"}],\"hiredAt\":\"Sun, 06 Nov"
                        + " 1994 08:49:37 GMT\",\"promotedAt\":1484719381},\"dependents\":[{\"name\":\"Futu"
                        + "re Wife\",\"age\":5147483649,\"address\":\"H # 531, S # 20\",\"uid\":\"123412\","
                        + "\"birthday\":\"1994-02-13\",\"birthtime\":\"1994-02-13T14:01:54.9571247Z\"},{\"n"
                        + "ame\":\"Future Kid\",\"age\":5147483648,\"address\":\"H # 531, S # 20\",\"uid\":"
                        + "\"312341\",\"birthday\":\"1994-02-13\",\"birthtime\":\"1994-02-13T14:01:54.95712"
                        + "47Z\"}],\"hiredAt\":\"Sun, 06 Nov 1994 08:49:37 GMT\"},{\"name\":\"Shahid Khaliq"
                        + "\",\"age\":5147483645,\"address\":\"H # 531, S # 20\",\"uid\":\"123321\",\"birth"
                        + "day\":\"1994-02-13\",\"birthtime\":\"1994-02-13T14:01:54.9571247Z\",\"salary\":2"
                        + "0000,\"department\":\"Software Development\",\"joiningDay\":\"Saturday\",\"worki"
                        + "ngDays\":[\"Monday\",\"Tuesday\",\"Friday\"],\"boss\":{\"personType\":\"Boss\","
                        + "\"name\":\"Zeeshan Ejaz\",\"age\":5147483645,\"address\":\"H # 531, S # 20\",\"u"
                        + "id\":\"123321\",\"birthday\":\"1994-02-13\",\"birthtime\":\"1994-02-13T14:01:54."
                        + "9571247Z\",\"salary\":20000,\"department\":\"Software Development\",\"joiningDay"
                        + "\":\"Saturday\",\"workingDays\":[\"Monday\",\"Tuesday\",\"Friday\"],\"dependents"
                        + "\":[{\"name\":\"Future Wife\",\"age\":5147483649,\"address\":\"H # 531, S # 20"
                        + "\",\"uid\":\"123412\",\"birthday\":\"1994-02-13\",\"birthtime\":\"1994-02-13T14:"
                        + "01:54.9571247Z\"},{\"name\":\"Future Kid\",\"age\":5147483648,\"address\":\"H # "
                        + "531, S # 20\",\"uid\":\"312341\",\"birthday\":\"1994-02-13\",\"birthtime\":\"199"
                        + "4-02-13T14:01:54.9571247Z\"}],\"hiredAt\":\"Sun, 06 Nov 1994 08:49:37 GMT\",\"pr"
                        + "omotedAt\":1484719381},\"dependents\":[{\"name\":\"Future Wife\",\"age\":5147483"
                        + "649,\"address\":\"H # 531, S # 20\",\"uid\":\"123412\",\"birthday\":\"1994-02-13"
                        + "\",\"birthtime\":\"1994-02-13T14:01:54.9571247Z\"},{\"name\":\"Future Kid\",\"ag"
                        + "e\":5147483648,\"address\":\"H # 531, S # 20\",\"uid\":\"312341\",\"birthday\":"
                        + "\"1994-02-13\",\"birthtime\":\"1994-02-13T14:01:54.9571247Z\"}],\"hiredAt\":\"Su"
                        + "n, 06 Nov 1994 08:49:37 GMT\"}]",
                Employee[].class);
    }

}
