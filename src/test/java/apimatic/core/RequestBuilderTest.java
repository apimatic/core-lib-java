package apimatic.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.nullable;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.anyMap;
import static org.mockito.Mockito.anyList;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.AbstractMap.SimpleEntry;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.stubbing.Answer;

import apimatic.core.mocks.MockCoreConfig;
import apimatic.core.models.Atom;
import apimatic.core.models.Car;
import apimatic.core.models.Employee;
import apimatic.core.models.Rfc1123Date;
import apimatic.core.models.containers.SendScalarParamBody;
import io.apimatic.core.ApiCall;
import io.apimatic.core.HttpRequest;
import io.apimatic.core.authentication.HeaderAuth;
import io.apimatic.core.authentication.QueryAuth;
import io.apimatic.core.exceptions.AuthValidationException;
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

    private static final int ELECTRONS_DEFAULT = 12;
    private static final int ELECTRONS_UPDATED = 14;
    private static final long QUERY_DEFAULT = 234L;
    private static final long QUERY_UPDATED = 254L;
    private static final double HEADER_DEFAULT = 2.14;
    private static final double HEADER_UPDATED = 19.95;
    private static final int ATOM_NUMBER = 23;
    private static final int ATOM_MASS = 24;
    private static final int BODY_A_ELECTRONS = 2;
    private static final int BODY_A_MASS = 4;
    private static final int BODY_B_ELECTRONS = 4;
    private static final int BODY_B_MASS = 2;
    private static final int BODY_B_UPDATED = 8;


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
    private Authentication authentication;

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

    @Test
    public void testUpdatePathParam() throws IOException {
        HttpRequest.Builder localRequestBuilder = new HttpRequest.Builder().httpMethod(Method.GET)
                .templateParam(param -> param.key("temp").value(false));

        updateAndVerify(localRequestBuilder, "$request.path#/temp", false, true);
    }


    @Test
    public void testUpdatePathParamArray() throws IOException {
        HttpRequest.Builder localRequestBuilder = new HttpRequest.Builder().httpMethod(Method.GET)
                .templateParam(param -> param.key("temp")
                        .value(new String[] {"apple", "banana", "cherry" }));

        updateAndVerify(localRequestBuilder, "$request.path#/temp/1", "banana", "mango");
    }

    @Test
    public void testUpdateSimpleFormParam() throws IOException {
        HttpRequest.Builder localRequestBuilder = new HttpRequest.Builder().httpMethod(Method.GET)
                .formParam(param -> param.key("form").value(true));

        updateAndVerify(localRequestBuilder, "$request.body#/form", true, false);
    }

    @Test
    public void testUpdateComplexFormParam() throws IOException {
        HttpRequest.Builder localRequestBuilder = new HttpRequest.Builder().httpMethod(Method.GET)
                .formParam(param -> param.key("form")
                        .value(new Atom(ELECTRONS_DEFAULT, ELECTRONS_DEFAULT)));

        updateAndVerify(localRequestBuilder, "$request.body#/form/NumberOfElectrons",
                ELECTRONS_DEFAULT, ELECTRONS_UPDATED);
    }

    @Test
    public void testUpdateSimpleQueryParam() throws IOException {
        HttpRequest.Builder localRequestBuilder = new HttpRequest.Builder().httpMethod(Method.GET)
                .queryParam(param -> param.key("que").value(QUERY_DEFAULT));

        updateAndVerify(localRequestBuilder, "$request.query#/que", QUERY_DEFAULT, QUERY_UPDATED);
    }

    @Test
    public void testUpdateComplexQueryParam() throws IOException {
        HttpRequest.Builder localRequestBuilder = new HttpRequest.Builder().httpMethod(Method.GET)
                .queryParam(param -> param.key("que")
                        .value(new Atom(ELECTRONS_DEFAULT, ELECTRONS_DEFAULT)));

        updateAndVerify(localRequestBuilder, "$request.query#/que/NumberOfElectrons",
                ELECTRONS_DEFAULT, ELECTRONS_UPDATED);
    }

    @Test
    public void testUpdateSimpleHeaderParam() throws IOException {
        HttpRequest.Builder localRequestBuilder = new HttpRequest.Builder().httpMethod(Method.GET)
                .headerParam(param -> param.key("head").value(HEADER_DEFAULT));

        updateAndVerify(localRequestBuilder, "$request.headers#/head",
                HEADER_DEFAULT, HEADER_UPDATED);
    }

    @Test
    public void testUpdateComplexHeaderParam() throws IOException {
        HttpRequest.Builder localRequestBuilder = new HttpRequest.Builder().httpMethod(Method.GET)
                .headerParam(param -> param.key("head")
                        .value(new Atom(ELECTRONS_DEFAULT, ELECTRONS_DEFAULT)));

        updateAndVerify(localRequestBuilder, "$request.headers#/head/NumberOfElectrons",
                ELECTRONS_DEFAULT, ELECTRONS_UPDATED);
    }

    @Test
    public void testUpdateMultipleBodyParams() throws IOException {
        HttpRequest.Builder localRequestBuilder = new HttpRequest.Builder().httpMethod(Method.POST)
                .bodyParam(param -> param.key("bodyA").value("bodyValue"))
                .bodyParam(param -> param.key("bodyB").value("bodyValue"));

        updateAndVerify(localRequestBuilder, "$request.body#/bodyB", "bodyValue", "ValueB");
    }

    @Test
    public void testUpdateComplexBodyParam() throws IOException {
        HttpRequest.Builder localRequestBuilder = new HttpRequest.Builder().httpMethod(Method.POST)
                .bodyParam(param -> param.value(new Atom(ATOM_NUMBER, ATOM_MASS)));

        updateAndVerify(localRequestBuilder, "$request.body#/NumberOfElectrons",
                ATOM_NUMBER, ATOM_MASS);
    }

    @Test
    public void testUpdateMultipleComplexBodyParams() throws IOException {
        HttpRequest.Builder localRequestBuilder = new HttpRequest.Builder().httpMethod(Method.POST)
                .bodyParam(param -> param.key("bodyA")
                .value(new Atom(BODY_A_ELECTRONS, BODY_A_MASS)))
                .bodyParam(param -> param.key("bodyB")
                .value(new Atom(BODY_B_ELECTRONS, BODY_B_MASS)));

        updateAndVerify(localRequestBuilder, "$request.body#/bodyB/NumberOfElectrons",
                BODY_B_ELECTRONS, BODY_B_UPDATED);
    }

    @Test
    public void testUpdateSimpleBodyParam() throws IOException {
        HttpRequest.Builder localRequestBuilder = new HttpRequest.Builder().httpMethod(Method.POST)
                .bodyParam(param -> param.value("BodyValue"));

        updateAndVerify(localRequestBuilder, "$request.body", "BodyValue", "new Value");
    }

    private void updateAndVerify(HttpRequest.Builder requestBuilder,
            String pointer, Object oldValue, Object newValue) {
        requestBuilder.updateParameterByJsonPointer(pointer, old -> {
            assertEquals(oldValue.toString(), old.toString());
            return newValue;
        });
        AtomicBoolean asserted = new AtomicBoolean(false);
        requestBuilder.updateParameterByJsonPointer(pointer, newV -> {
            assertEquals(newValue.toString(), newV.toString());
            asserted.set(true);
            return newV;
        });
        assertTrue(asserted.get());
    }

    @Test(expected = NullPointerException.class)
    public void testBodyParamValidation() throws IOException {
        // when
        new HttpRequest.Builder()
                .httpMethod(Method.POST)
                .bodyParam(param -> param.value(null))
                .build(getMockGlobalConfig());
    }

    @Test(expected = NullPointerException.class)
    public void testBodyParamValidation1() throws IOException {
        // when
        new HttpRequest.Builder()
                .httpMethod(Method.POST)
                .bodyParam(param -> param.value(null))
                .build(getMockGlobalConfig());
    }

    @Test
    public void testBodyParam() throws IOException {
        // when
        Request coreHttpRequest =
                new HttpRequest.Builder().httpMethod(Method.PATCH)
                        .bodyParam(param -> param.value("bodyValue"))
                        .build(getMockGlobalConfig());

        when(coreHttpRequest.getBody()).thenReturn("bodyValue");

        // verify
        assertEquals(coreHttpRequest.getBody(), "bodyValue");
    }

    @Test
    public void testClonedBodyParam() throws IOException {
        // when
        Request coreHttpRequest =
                new HttpRequest.Builder().httpMethod(Method.PATCH)
                        .bodyParam(param -> param.key("body").value("bodyValue")).copy()
                        .build(getMockGlobalConfig());

        when(coreHttpRequest.getBody()).thenReturn("bodyValue");

        // verify
        assertEquals("bodyValue", coreHttpRequest.getBody());
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

    /**
     * An instance of {@link LocalDateTime}.
     */
    private static final LocalDateTime LOCAL_DATE_TIME = LocalDateTime.of(2021, 1, 20, 12, 12, 41);
    /**
     * An instance of {@link ZonedDateTime}.
     */
    private static final ZonedDateTime ZONED_DATE_TIME =
            ZonedDateTime.of(LOCAL_DATE_TIME, ZoneId.of("GMT"));
    private static final double PRECISION_NUMBER1 = 100.11;
    private static final double PRECISION_NUMBER2 = 133.0;

    @Test
    public void testComplexHeaderParameter() throws IOException {
        // when
        String jsonObject = "{\"NumberOfTyres\":\"4\",\"HaveTrunk\":true}";
        Car car = CoreHelper.tryDeserialize(jsonObject, Car.class);

        SendScalarParamBody bodyStringType = SendScalarParamBody.fromMString("some string");
        SendScalarParamBody precisionArray = SendScalarParamBody.fromPrecision(
                Arrays.asList(PRECISION_NUMBER1, PRECISION_NUMBER2));
        Rfc1123Date rfc1123Date = new Rfc1123Date.Builder()
                .dateTime(LOCAL_DATE_TIME)
                .zonedDateTime(ZONED_DATE_TIME)
                .build();

        Request coreHttpRequest =
                new HttpRequest.Builder().httpMethod(Method.GET)
                        .formParam(param -> param.key("formKey").value("value"))
                        .headerParam(param -> param.key("accept").value("application/json"))
                        .headerParam(param -> param.key("car-complex-header").value(car))
                        .headerParam(param -> param.key("any-of-string").value(bodyStringType))
                        .headerParam(param -> param.key("precision-array").value(precisionArray))
                        .headerParam(param -> param.key("date-time-header").value(rfc1123Date))
                        .build(getMockGlobalConfig());

        when(coreHttpRequest.getHeaders()).thenReturn(getHttpHeaders());
        when(getHttpHeaders().value("car-complex-header")).thenReturn(jsonObject);

        // verify
        assertEquals(coreHttpRequest.getHeaders().value("car-complex-header"), jsonObject);
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
                        .withAuth(auth -> auth.add("global"))
                        .httpMethod(Method.GET)
                        .build(getMockGlobalConfig());


        when(getHttpHeaders().value("Authorization"))
                .thenReturn(CoreHelper.getBase64EncodedCredentials("username", "password"));

        // verify
        assertEquals(coreHttpRequest.getHeaders().value("Authorization"),
                CoreHelper.getBase64EncodedCredentials("username", "password"));
    }

    @Test(expected = AuthValidationException.class)
    public void testHeaderAuthenticationWithNull() throws IOException {
        when(getCoreHttpRequest().getHeaders()).thenReturn(getHttpHeaders());
        when(authentications.get("global"))
                .thenReturn(new HeaderAuth(Collections.singletonMap(null, null)));

        @SuppressWarnings("unused")
        Request coreHttpRequest =
                new HttpRequest.Builder().server("https:\\localhost:3000").path("/auth/basic")
                        .formParam(param -> param.key("key").value("string"))
                        .withAuth(auth -> auth.add("global"))
                        .httpMethod(Method.GET)
                        .build(getMockGlobalConfig());
    }

    @Test(expected = AuthValidationException.class)
    public void testHeaderAuthenticationWithValueNull() throws IOException {
        when(getCoreHttpRequest().getHeaders()).thenReturn(getHttpHeaders());
        when(authentications.get("global"))
                .thenReturn(new HeaderAuth(Collections.singletonMap("authorization", null)));

        @SuppressWarnings("unused")
        Request coreHttpRequest =
                new HttpRequest.Builder().server("https:\\localhost:3000").path("/auth/basic")
                        .formParam(param -> param.key("key").value("string"))
                        .withAuth(auth -> auth.add("global"))
                        .httpMethod(Method.GET)
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
                        .withAuth(auth -> auth.add("global"))
                        .httpMethod(Method.GET)
                        .build(getMockGlobalConfig());

        when(coreHttpRequest.getQueryParameters()).thenReturn(queryParameters);
        when(queryParameters.get("token")).thenReturn("api-token");
        when(queryParameters.get("api-key")).thenReturn("apikey");

        // verify
        assertEquals(coreHttpRequest.getQueryParameters().get("token"), "api-token");
        assertEquals(coreHttpRequest.getQueryParameters().get("api-key"), "apikey");
    }

    @Test(expected = AuthValidationException.class)
    public void testQueryAuthenticationWithNull() throws IOException {
        Map<String, String> authParams = new HashMap<>();
        authParams.put(null, null);
        authParams.put("api-key", "apikey");
        when(authentications.get("global")).thenReturn(new QueryAuth(authParams));
        @SuppressWarnings("unused")
        Request coreHttpRequest =
                new HttpRequest.Builder().server("https:\\localhost:3000").path("/auth/basic")
                        .formParam(param -> param.key("key").value("string"))
                        .withAuth(auth -> auth.add("global"))
                        .httpMethod(Method.GET)
                        .build(getMockGlobalConfig());
    }

    @Test(expected = AuthValidationException.class)
    public void testQueryAuthenticationWithKeyNull() throws IOException {
        Map<String, String> authParams = new HashMap<>();
        authParams.put(null, "api-token");
        authParams.put("api-key", "apikey");
        when(authentications.get("global")).thenReturn(new QueryAuth(authParams));
        @SuppressWarnings("unused")
        Request coreHttpRequest =
                new HttpRequest.Builder().server("https:\\localhost:3000").path("/auth/basic")
                        .formParam(param -> param.key("key").value("string"))
                        .withAuth(auth -> auth.add("global"))
                        .httpMethod(Method.GET)
                        .build(getMockGlobalConfig());
    }

    @Test(expected = AuthValidationException.class)
    public void testQueryAuthenticationWithValueNull() throws IOException {
        Map<String, String> authParams = new HashMap<>();
        authParams.put("token", null);
        authParams.put("api-key", "apikey");
        when(authentications.get("global")).thenReturn(new QueryAuth(authParams));
        @SuppressWarnings("unused")
        Request coreHttpRequest =
                new HttpRequest.Builder().server("https:\\localhost:3000").path("/auth/basic")
                        .formParam(param -> param.key("key").value("string"))
                        .withAuth(auth -> auth.add("global"))
                        .httpMethod(Method.GET)
                        .build(getMockGlobalConfig());
    }

    @Test
    public void testEmptyAuthenticatioMap() throws IOException {
        when(getMockGlobalConfig().getAuthentications()).thenReturn(null);

        Request coreHttpRequest =
                new HttpRequest.Builder().server("https:\\localhost:3000").path("/auth/basic")
                        .formParam(param -> param.key("key").value("string"))
                        .withAuth(auth -> auth.add("global"))
                        .httpMethod(Method.GET)
                        .build(getMockGlobalConfig());

        when(coreHttpRequest.getQueryParameters()).thenReturn(queryParameters);
        when(queryParameters.get("token")).thenReturn(null);
        when(queryParameters.get("api-key")).thenReturn(null);

        // verify
        assertNull(coreHttpRequest.getQueryParameters().get("token"));
        assertNull(coreHttpRequest.getQueryParameters().get("api-key"));
    }

    @SuppressWarnings("serial")
    @Test
    public void testMultipleAuthRequest() throws IOException {
        Map<String, Authentication> authManagers = new HashMap<String, Authentication>() {
            {
                put("basic-auth",
                        new HeaderAuth(Collections.singletonMap("username", "password")));
                put("query-auth",
                        new QueryAuth(Collections.singletonMap("x-api-key-query", "A1B2C3")));
                put("header-auth",
                        new HeaderAuth(Collections.singletonMap("x-api-key-header", "ABCDEF")));
                put("custom-header-auth",
                        new HeaderAuth(Collections.singletonMap("x-custom-header", "123456")));
                put("custom-query-auth",
                        new QueryAuth(Collections.singletonMap("x-custom-query", "QWERTY")));
            }
        };

        Map<String, String> headers = new HashMap<String, String>();
        Map<String, String> queryParams = new HashMap<String, String>();

        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                // Retrieve the arguments passed to the method
                Object[] args = invocation.getArguments();
                String headerKey = (String) args[0];
                String headerValue = (String) args[1];
                headers.put(headerKey, headerValue);
                return null; // Return null for void method
            }
        }).when(getHttpHeaders()).add(anyString(), anyString());

        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                // Retrieve the arguments passed to the method
                Object[] args = invocation.getArguments();
                String queryKey = (String) args[0];
                String headerValue = (String) args[1];
                queryParams.put(queryKey, headerValue);
                return null; // Return null for void method
            }
        }).when(getCoreHttpRequest()).addQueryParameter(anyString(), anyString());

        when(getMockGlobalConfig().getAuthentications()).thenReturn(authManagers);
        when(getCoreHttpRequest().getHeaders()).thenReturn(getHttpHeaders());

        new HttpRequest.Builder().server("https:\\localhost:3000").path("/auth/basic")
                        .formParam(param -> param.key("key").value("string"))
                        .withAuth(auth -> auth
                                .and(andAuth -> andAuth
                                        .add("basic-auth")
                                        .and(andAuth1 -> andAuth1
                                                .add("query-auth")
                                                .add("header-auth"))
                                        .or(orAuth -> orAuth
                                                .add("custom-header-auth")
                                                .add("custom-query-auth"))))
                        .httpMethod(Method.GET)
                        .build(getMockGlobalConfig());

        assertEquals("ABCDEF", headers.get("x-api-key-header"));
        assertEquals("123456", headers.get("x-custom-header"));
        assertEquals("password", headers.get("username"));

        assertEquals("A1B2C3", queryParams.get("x-api-key-query"));
        assertNull(queryParams.get("x-custom-query"));
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

        when(getCompatibilityFactory().createHttpRequest(any(Method.class),
                nullable(StringBuilder.class), nullable(HttpHeaders.class), anyMap(), anyList()))
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
