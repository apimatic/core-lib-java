package apimatic.core.utilities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import apimatic.core.mocks.TestDateTimeHelper;
import apimatic.core.models.AtomCase;
import apimatic.core.models.AttributesAndElements;
import apimatic.core.models.CarCase;
import apimatic.core.models.ChildClass;
import apimatic.core.models.ComplexType;
import apimatic.core.models.DateTimeCases;
import apimatic.core.models.DeleteBody;
import apimatic.core.models.MorningCase;
import apimatic.core.models.NonScalarModel;
import apimatic.core.models.OrbitCase;
import apimatic.core.models.Person;
import apimatic.core.models.containers.SendParamsFormDateTime;
import apimatic.core.models.containers.SendScalarParamBody;
import io.apimatic.core.utilities.CoreHelper;
import io.apimatic.core.utilities.CoreJsonObject;
import io.apimatic.core.utilities.CoreJsonValue;
import io.apimatic.core.utilities.DateHelper;
import io.apimatic.core.utilities.LocalDateTimeHelper;
import io.apimatic.coreinterfaces.http.request.ArraySerializationFormat;

public class CoreHelperTest {

    private static final int YEAR2010 = 2010;
    private static final double PRECISION_NUMBER = 1.2;
    private static final int YEAR3 = 2020;
    private static final int SECONDS1 = 54;
    private static final int MINUTE = 01;
    private static final int HOUR2 = 14;
    private static final int XML_NO_OF_ELEMENT = 6;
    private static final int XML_NO_OF_ATTRIBUTE = 3;
    private static final double FLOAT_TEST_NUMBER2 = 2.3;
    private static final double FLOAT_TEST_NUMBER1 = 1.6;
    private static final int MONTH2 = 2;
    private static final int HOUR1 = 1;
    private static final int MINUTES = 10;
    private static final int YEAR2 = 1994;
    private static final int DAY1 = 13;
    private static final int MONTH1 = 7;
    private static final int YEAR1 = 1997;
    private static final long UNIQUE_UUID_NUMBER2 = 87866L;
    private static final long UNIQUE_UUID_NUMBER1 = 876547L;
    private static final List<Integer> LIST_OF_INTEGERS = Arrays.asList(1, 2, 3, 4, 5);
    private static final String XML_ARRAY =
            "<arrayOfModels>\r\n" + "  <item number=\"3\" string=\"XMLRootName\">\r\n"
                    + "    <number>6</number>\r\n" + "    <string>Data</string>\r\n" + "</item>\r\n"
                    + "</arrayOfModels>";
    private static final String INVALID_XML =
            "\r\n" + "  item number=\"3\" string=\"XMLRootName\"\r\n" + "    number>6</number>\r\n"
                    + "    <string>Data</string>\r\n" + "</item>\r\n" + "</arrayOfModels>";
    private static final String XML =
            "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n"
                    + "<arrayOfModels number=\"3\" string=\"XMLRootName\">\r\n"
                    + "    <number>6</number>\r\n" + "    <string>Data</string>\r\n"
                    + "</arrayOfModels>\r\n" + "";

    private static final String JSON_OBJECT =
            "https://localhost:3000/query?operations[$id]=https%3A%2F%2Fexample.com%2Fperson."
                    + "schema.json&operations[$schema]=https%3A%2F%2Fjson-schema"
                    + ".org%2Fdraft%2F2020-12%2Fschema&operations[title]=Person&operations"
                    + "[type]=object&operations[properties][firstName][type]=string&operations"
                    + "[properties][firstName][description]=The+person%27s+first+name.&"
                    + "operations[properties][lastName][type]=string&operations[properties]"
                    + "[lastName][description]=The+person%27s+last+name.&operations"
                    + "[properties][age][type]=integer&operations[properties][age][description]"
                    + "=Age+in+years&operations[properties][age][minimum]=0";
    private static final String JSON_VALUE =
            "https://localhost:3000/query?operations=test-JsonValue";

    @Test
    public void testSerializeNullObject() throws JsonProcessingException {
        Object obj = null;
        assertNull(CoreHelper.serialize(obj));
    }


    @Test
    public void testIsWhiteSpace() {
        String whiteSpaceString = " ";
        assertTrue(CoreHelper.isNullOrWhiteSpace(whiteSpaceString));
    }

    @Test
    public void testIsNotWhiteSpace() {
        String whiteSpaceString = "";
        assertFalse(CoreHelper.isNullOrWhiteSpace(whiteSpaceString));
    }

    @Test
    public void testIsNull() {
        String nullString = null;
        assertTrue(CoreHelper.isNullOrWhiteSpace(nullString));
    }

    @Test
    public void testIsNotNull() {
        String notNullString = "not null String";
        assertFalse(CoreHelper.isNullOrWhiteSpace(notNullString));
    }

    @Test
    public void testBase64Encoding() {
        String username = "username";
        String password = "password";


        String expectedEncodedString = "Basic dXNlcm5hbWU6cGFzc3dvcmQ=";
        String actualEncodedString = CoreHelper.getBase64EncodedCredentials(username, password);
        assertEquals(actualEncodedString, expectedEncodedString);
    }

    @Test
    public void testUrlEncoding() {
        String urlString = "https://localhost:8080%query=0";
        boolean spaceAsPecentage = false;

        String expected = "https%3A%2F%2Flocalhost%3A8080%25query%3D0";
        String actual = CoreHelper.tryUrlEncode(urlString, spaceAsPecentage);

        assertEquals(actual, expected);
    }

    @Test
    public void testInvalidUrlEncoding() {
        String urlString = "https://localhost:8080%query=0";
        boolean spaceAsPecentage = false;

        String expected = "https%3A%2F%2Flocalhost%3A8080%25query%3D0";
        String actual = CoreHelper.tryUrlEncode(urlString, spaceAsPecentage);

        assertEquals(actual, expected);
    }


    @Test
    public void testUrlEncodingPercentage() {
        String urlString = "https://localhost:8080/query=0";
        boolean spaceAsPecentage = true;

        String expected = "https%3A%2F%2Flocalhost%3A8080%2Fquery%3D0";
        String actual = CoreHelper.tryUrlEncode(urlString, spaceAsPecentage);

        assertEquals(actual, expected);
    }

    @Test
    public void testCleanUrl() {
        String server = "https://localhost:3000/query";
        String path = "/basic/body";
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(server);
        stringBuilder.append(path);


        String expected = "https://localhost:3000/query/basic/body";
        String actual = CoreHelper.cleanUrl(stringBuilder);

        assertEquals(actual, expected);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCleanUrlIllegalCharacter() {
        String server = "localhost:3000/query";
        String path = "/basic/body";
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(server);
        stringBuilder.append(path);

        CoreHelper.cleanUrl(stringBuilder);
    }

    @Test
    public void testRemoveNullValues() {
        Map<String, Object> actual = new HashMap<>();

        actual.put("key1", null);
        actual.put("key2", "value2");

        Map<String, Object> expected = new HashMap<>();
        expected.put("key2", "value2");
        CoreHelper.removeNullValues(actual);

        assertEquals(actual, expected);
    }

    @Test
    public void testRemoveNullValues1() {
        Map<String, Object> actual = null;
        CoreHelper.removeNullValues(actual);
        assertNull(actual);
    }

    @Test
    public void testUpdateUserAgent() {
        String userAgent = "Java|31.8.0|{engine}|{engine-version}|{os-info}";

        String expected =
                "Java|31.8.0|JRE|" + System.getProperty("java.runtime.version") + "|"
                        + System.getProperty("os.name") + "-" + System.getProperty("os.version");

        String actual = CoreHelper.updateUserAgent(userAgent, null);

        assertEquals(actual, expected);
    }

    @Test
    public void testUpdateUserAgent1() {
        String userAgent = "Java|31.8.0|{engine}|{engine-version}|{os-info}|{square-version}";
        Map<String, String> userAgentConfig = new HashMap<>();
        userAgentConfig.put("{square-version}", "17.2.6");

        String expected =
                "Java|31.8.0|JRE|" + System.getProperty("java.runtime.version") + "|"
                        + System.getProperty("os.name") + "-" + System.getProperty("os.version")
                        + "|17.2.6";

        String actual = CoreHelper.updateUserAgent(userAgent, userAgentConfig);

        assertEquals(actual, expected);
    }


    @Test
    public void testAppendTemplateParameters() {
        String baseUri = "https://localhost:3000";
        String templateStringValue = "strings";
        boolean shouldEncode = true;
        StringBuilder queryBuilder = new StringBuilder(baseUri + "/template/{strings}");

        Map<String, SimpleEntry<Object, Boolean>> templateParameters = new HashMap<>();
        templateParameters.put("strings",
                new SimpleEntry<Object, Boolean>(templateStringValue, shouldEncode));
        CoreHelper.appendUrlWithTemplateParameters(queryBuilder, templateParameters);

        String expected = "https://localhost:3000/template/strings";
        String actual = queryBuilder.toString();
        assertEquals(actual, expected);
    }

    @Test
    public void testAppendTemplateParameters1() {
        String baseUri = "https://localhost:3000";
        String templateStringValue = null;
        boolean shouldEncode = true;
        StringBuilder queryBuilder = new StringBuilder(baseUri + "/template/{strings}");

        Map<String, SimpleEntry<Object, Boolean>> templateParameters = new HashMap<>();
        templateParameters.put("strings",
                new SimpleEntry<Object, Boolean>(templateStringValue, shouldEncode));
        CoreHelper.appendUrlWithTemplateParameters(queryBuilder, templateParameters);

        String expected = "https://localhost:3000/template/";
        String actual = queryBuilder.toString();
        assertEquals(actual, expected);
    }


    @Test
    public void testAppendTemplateParameters2() {
        String baseUri = "https://localhost:3000";
        List<String> templateList = Arrays.asList("strings");
        boolean shouldEncode = true;
        StringBuilder queryBuilder = new StringBuilder(baseUri + "/template/{strings}");

        Map<String, SimpleEntry<Object, Boolean>> templateParameters = new HashMap<>();
        templateParameters.put("strings",
                new SimpleEntry<Object, Boolean>(templateList, shouldEncode));
        CoreHelper.appendUrlWithTemplateParameters(queryBuilder, templateParameters);

        String expected = "https://localhost:3000/template/strings";
        String actual = queryBuilder.toString();
        assertEquals(actual, expected);
    }

    @Test
    public void testAppendTemplateParameters3() {
        String baseUri = "https://localhost:3000";
        List<String> templateList = Arrays.asList("strings");
        boolean shouldEncode = false;
        StringBuilder queryBuilder = new StringBuilder(baseUri + "/template/{strings}");

        Map<String, SimpleEntry<Object, Boolean>> templateParameters = new HashMap<>();
        templateParameters.put("strings",
                new SimpleEntry<Object, Boolean>(templateList, shouldEncode));
        CoreHelper.appendUrlWithTemplateParameters(queryBuilder, templateParameters);

        String expected = "https://localhost:3000/template/strings";
        String actual = queryBuilder.toString();
        assertEquals(actual, expected);
    }


    @Test(expected = IllegalArgumentException.class)
    public void testAppendTemplateParameters4() {
        String templateStringValue = "strings";
        boolean shouldEncode = true;
        StringBuilder queryBuilder = null;

        Map<String, SimpleEntry<Object, Boolean>> templateParameters = new HashMap<>();
        templateParameters.put("strings",
                new SimpleEntry<Object, Boolean>(templateStringValue, shouldEncode));
        CoreHelper.appendUrlWithTemplateParameters(queryBuilder, templateParameters);
    }


    @Test
    public void testAppendTemplateParametersShouldNotEncode() {
        String baseUri = "https://localhost:3000";
        String templateStringValue = "strings";
        boolean shouldEncode = false;
        StringBuilder queryBuilder = new StringBuilder(baseUri + "/template/{strings}");

        Map<String, SimpleEntry<Object, Boolean>> templateParameters = new HashMap<>();
        templateParameters.put("strings",
                new SimpleEntry<Object, Boolean>(templateStringValue, shouldEncode));
        CoreHelper.appendUrlWithTemplateParameters(queryBuilder, templateParameters);

        String expected = "https://localhost:3000/template/strings";
        String actual = queryBuilder.toString();
        assertEquals(actual, expected);
    }


    @Test
    public void testAppendTemplateParameters5() {
        String baseUri = "https://localhost:3000";
        StringBuilder queryBuilder = new StringBuilder(baseUri + "/template/{strings}");

        Map<String, SimpleEntry<Object, Boolean>> templateParameters = null;
        CoreHelper.appendUrlWithTemplateParameters(queryBuilder, templateParameters);

        String expected = "https://localhost:3000/template/{strings}";
        String actual = queryBuilder.toString();
        assertEquals(actual, expected);
    }


    @Test
    public void testptionalNullable() throws IOException {
        ChildClass child =
                CoreHelper.deserialize(
                        "{\"Grand_Parent_Required_Nullable\":null,\"Grand_Parent_Required\":"
                                + "\"not nullable and required\",\"class\":23,\""
                                + "Parent_Optional_Nullable_With_Default_Value\":"
                                + "\"Has default value\",\"Parent_Required_Nullable\""
                                + ":null,\"Parent_Required\":\"not nullable and required\","
                                + "\"Optional_Nullable\":null,\"Optional_Nullable_"
                                + "With_Default_Value\":\"With default value\",\""
                                + "Required_Nullable\":null,\"Required\":\"not "
                                + "nullable and required\",\"Child_Class_Array\":null}",
                        ChildClass.class);

        String expected =
                "{\"Grand_Parent_Required_Nullable\":null,\"Grand_Parent_Required\":"
                        + "\"not nullable and required\",\"Parent_Optional_Nullable_With_"
                        + "Default_Value\":\"Has default value\",\"Parent_Required_Nullable"
                        + "\":null,\"Parent_Required\":\"not nullable and required\","
                        + "\"Optional_Nullable\":null,\"Optional_Nullable_With_Default_Value\":"
                        + "\"With default value\",\"Required_Nullable\":null,\"Required\":"
                        + "\"not nullable and required\",\"Child_Class_Array\":null,\"class\":23}";

        String actual = CoreHelper.serialize(child);
        assertEquals(actual, expected);
    }

    @Test
    public void testAppendQueryParameters() {
        String baseUri = "https://localhost:3000";
        String queryValue = "x+y";
        StringBuilder queryBuilder = new StringBuilder(baseUri + "/query");

        Map<String, Object> queryParameters = new HashMap<>();
        queryParameters.put("operations", queryValue);

        CoreHelper.appendUrlWithQueryParameters(queryBuilder, queryParameters,
                ArraySerializationFormat.INDEXED);
        String expected = "https://localhost:3000/query?operations=x%2By";
        String actual = queryBuilder.toString();
        assertEquals(actual, expected);
    }

    @Test
    public void testAppendQueryParametersArray() {
        String baseUri = "https://localhost:3000";
        String[] queryValue = new String[HOUR1];
        queryValue[0] = "x+y";
        StringBuilder queryBuilder = new StringBuilder(baseUri + "/query");

        Map<String, Object> queryParameters = new HashMap<>();
        queryParameters.put("operations", queryValue);

        CoreHelper.appendUrlWithQueryParameters(queryBuilder, queryParameters,
                ArraySerializationFormat.INDEXED);
        String expected = "https://localhost:3000/query?operations[0]=x%2By";
        String actual = queryBuilder.toString();
        assertEquals(actual, expected);
    }

    @Test
    public void testAppendQueryParametersIndexedSerialization() {
        String baseUri = "https://localhost:3000";
        List<String> queryValue = new ArrayList<String>();
        queryValue.add("x+y");
        StringBuilder queryBuilder = new StringBuilder(baseUri + "/query");

        Map<String, Object> queryParameters = new HashMap<>();
        queryParameters.put("operations", queryValue);

        CoreHelper.appendUrlWithQueryParameters(queryBuilder, queryParameters,
                ArraySerializationFormat.INDEXED);
        String expected = "https://localhost:3000/query?operations[0]=x%2By";
        String actual = queryBuilder.toString();
        assertEquals(actual, expected);
    }

    @Test
    public void testAppendQueryParametersPlainSerialization() {
        String baseUri = "https://localhost:3000";
        List<String> queryValue = new ArrayList<String>();
        queryValue.add("x+y");
        StringBuilder queryBuilder = new StringBuilder(baseUri + "/query");

        Map<String, Object> queryParameters = new HashMap<>();
        queryParameters.put("operations", queryValue);

        CoreHelper.appendUrlWithQueryParameters(queryBuilder, queryParameters,
                ArraySerializationFormat.PLAIN);
        String expected = "https://localhost:3000/query?operations=x%2By";
        String actual = queryBuilder.toString();
        assertEquals(actual, expected);
    }

    @Test
    public void testAppendQueryParameterCSVSerialization() {
        String baseUri = "https://localhost:3000";
        List<String> queryValue = new ArrayList<String>();
        queryValue.add("x+y");
        StringBuilder queryBuilder = new StringBuilder(baseUri + "/query");

        Map<String, Object> queryParameters = new HashMap<>();
        queryParameters.put("operations", queryValue);

        CoreHelper.appendUrlWithQueryParameters(queryBuilder, queryParameters,
                ArraySerializationFormat.CSV);
        String expected = "https://localhost:3000/query?operations=x%2By";
        String actual = queryBuilder.toString();
        assertEquals(actual, expected);
    }

    @Test
    public void testAppendQueryParameterTSVSerialization() {
        String baseUri = "https://localhost:3000";
        List<String> queryValue = new ArrayList<String>();
        queryValue.add("x+y");
        StringBuilder queryBuilder = new StringBuilder(baseUri + "/query");

        Map<String, Object> queryParameters = new HashMap<>();
        queryParameters.put("operations", queryValue);

        CoreHelper.appendUrlWithQueryParameters(queryBuilder, queryParameters,
                ArraySerializationFormat.TSV);
        String expected = "https://localhost:3000/query?operations=x%2By";
        String actual = queryBuilder.toString();
        assertEquals(actual, expected);
    }

    @Test
    public void testAppendQueryParameterPSVSerialization() {
        String baseUri = "https://localhost:3000";
        List<String> queryValue = new ArrayList<String>();
        queryValue.add("x+y");
        StringBuilder queryBuilder = new StringBuilder(baseUri + "/query");

        Map<String, Object> queryParameters = new HashMap<>();
        queryParameters.put("operations", queryValue);

        CoreHelper.appendUrlWithQueryParameters(queryBuilder, queryParameters,
                ArraySerializationFormat.PSV);
        String expected = "https://localhost:3000/query?operations=x%2By";
        String actual = queryBuilder.toString();
        assertEquals(actual, expected);
    }

    @Test
    public void testAppendQueryParameterUnindexedSerialization() {
        String baseUri = "https://localhost:3000";
        List<String> queryValue = new ArrayList<String>();
        queryValue.add("x+y");
        StringBuilder queryBuilder = new StringBuilder(baseUri + "/query");

        Map<String, Object> queryParameters = new HashMap<>();
        queryParameters.put("operations", queryValue);

        CoreHelper.appendUrlWithQueryParameters(queryBuilder, queryParameters,
                ArraySerializationFormat.UNINDEXED);
        String expected = "https://localhost:3000/query?operations[]=x%2By";
        String actual = queryBuilder.toString();
        assertEquals(actual, expected);
    }

    @Test
    public void testAppendQueryParameters1() {
        String baseUri = "https://localhost:3000";
        String queryValue = "x+y";
        StringBuilder queryBuilder = new StringBuilder(baseUri + "/query?type=calculator");

        Map<String, Object> queryParameters = new HashMap<>();
        queryParameters.put("operations", queryValue);

        CoreHelper.appendUrlWithQueryParameters(queryBuilder, queryParameters,
                ArraySerializationFormat.INDEXED);
        String expected = "https://localhost:3000/query?type=calculator&operations=x%2By";
        String actual = queryBuilder.toString();
        assertEquals(actual, expected);
    }

    @Test
    public void testAppendQueryParameters2() {
        String baseUri = "https://localhost:3000";
        StringBuilder queryBuilder = new StringBuilder(baseUri + "/query?type=calculator");

        Map<String, Object> queryParameters = null;
        CoreHelper.appendUrlWithQueryParameters(queryBuilder, queryParameters,
                ArraySerializationFormat.INDEXED);
        String expected = "https://localhost:3000/query?type=calculator";
        String actual = queryBuilder.toString();
        assertEquals(actual, expected);
    }

    @Test
    public void testAppendQueryParameters3() {
        String baseUri = "https://localhost:3000";
        StringBuilder queryBuilder = new StringBuilder(baseUri + "/query?type=calculator");

        Map<String, Object> queryParameters = new HashMap<>();
        CoreHelper.appendUrlWithQueryParameters(queryBuilder, queryParameters,
                ArraySerializationFormat.INDEXED);
        String expected = "https://localhost:3000/query?type=calculator";
        String actual = queryBuilder.toString();
        assertEquals(actual, expected);
    }

    @Test
    public void testTabSeperated() throws IOException {
        String baseUri = "https://localhost:3000";
        StringBuilder queryBuilder = new StringBuilder(baseUri + "/query?type=calculator");

        Map<String, ComplexType> complexType = getComplexType();
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("query", complexType);
        CoreHelper.appendUrlWithQueryParameters(queryBuilder, parameters,
                ArraySerializationFormat.TSV);
        String expected = "a5e48529-745b-4dfb-aac0-a7d844debd8b";
        String actual = queryBuilder.toString();
        assertTrue(actual.contains(expected));
        assertTrue(actual.contains("%09"));
    }

    @Test
    public void testPipeSeperated() throws IOException {
        String baseUri = "https://localhost:3000";
        StringBuilder queryBuilder = new StringBuilder(baseUri + "/query?type=calculator");

        Map<String, ComplexType> complexType = getComplexType();
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("query", complexType);
        CoreHelper.appendUrlWithQueryParameters(queryBuilder, parameters,
                ArraySerializationFormat.PSV);
        String expected = "a5e48529-745b-4dfb-aac0-a7d844debd8b";
        String actual = queryBuilder.toString();
        assertTrue(actual.contains(expected));
        assertTrue(actual.contains("%7C"));
    }

    @Test
    public void testCommaSeperated() throws IOException {
        String baseUri = "https://localhost:3000";
        StringBuilder queryBuilder = new StringBuilder(baseUri + "/query?type=calculator");

        Map<String, ComplexType> complexType = getComplexType();
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("query", complexType);
        CoreHelper.appendUrlWithQueryParameters(queryBuilder, parameters,
                ArraySerializationFormat.CSV);
        String expected = "a5e48529-745b-4dfb-aac0-a7d844debd8b";
        String actual = queryBuilder.toString();
        assertTrue(actual.contains(expected));
        assertTrue(actual.contains(","));
    }



    @Test(expected = IllegalArgumentException.class)
    public void testAppendQueryParameters4() {
        String queryValue = "x+y";
        StringBuilder queryBuilder = null;
        Map<String, Object> queryParameters = new HashMap<>();
        queryParameters.put("operations", queryValue);

        CoreHelper.appendUrlWithQueryParameters(queryBuilder, queryParameters,
                ArraySerializationFormat.INDEXED);
    }


    @Test
    public void testPrepareFormFields() {
        String body = "test for prepare form fields";
        Map<String, Object> formParameters = new HashMap<>();
        formParameters.put("body", body);

        List<SimpleEntry<String, Object>> expected = new ArrayList<SimpleEntry<String, Object>>();
        expected.add(new SimpleEntry<String, Object>("body", body));
        List<SimpleEntry<String, Object>> actual =
                CoreHelper.prepareFormFields(formParameters, ArraySerializationFormat.INDEXED);
        assertEquals(actual, expected);
    }

    @Test
    public void testPrepareFormFieldsUUID() {
        UUID body = new UUID(UNIQUE_UUID_NUMBER1, UNIQUE_UUID_NUMBER2);
        Map<String, Object> formParameters = new HashMap<>();
        formParameters.put("body", body);

        List<SimpleEntry<String, Object>> expected = new ArrayList<SimpleEntry<String, Object>>();
        expected.add(new SimpleEntry<String, Object>("body", body.toString()));
        List<SimpleEntry<String, Object>> actual =
                CoreHelper.prepareFormFields(formParameters, ArraySerializationFormat.INDEXED);
        assertEquals(actual, expected);
    }


    @Test
    public void testPrepareFormFieldsModel() {
        DeleteBody body = new DeleteBody.Builder().name("ali").field("QA").build();
        Map<String, Object> formParameters = new HashMap<>();
        formParameters.put("body", body);

        List<SimpleEntry<String, Object>> expected = new ArrayList<SimpleEntry<String, Object>>();
        expected.add(new SimpleEntry<String, Object>("body[name]", "ali"));
        expected.add(new SimpleEntry<String, Object>("body[field]", "QA"));
        List<SimpleEntry<String, Object>> actual =
                CoreHelper.prepareFormFields(formParameters, ArraySerializationFormat.INDEXED);
        assertEquals(actual, expected);
    }


    @Test
    public void testPrepareFormFieldsIndexedSerialization() {
        String bodyText = "test for prepare form fields";
        List<String> body = new ArrayList<String>();
        body.add(bodyText);
        Map<String, Object> formParameters = new HashMap<>();
        formParameters.put("body", body);

        List<SimpleEntry<String, Object>> expected = new ArrayList<SimpleEntry<String, Object>>();
        expected.add(new SimpleEntry<String, Object>("body[0]", bodyText));
        List<SimpleEntry<String, Object>> actual =
                CoreHelper.prepareFormFields(formParameters, ArraySerializationFormat.INDEXED);
        assertEquals(actual, expected);
    }

    @Test
    public void testPrepareFormFieldsIndexedSerialization1() {
        List<String> body = new ArrayList<String>();
        body.add(null);
        Map<String, Object> formParameters = new HashMap<>();
        formParameters.put("body", body);

        List<SimpleEntry<String, Object>> expected = new ArrayList<SimpleEntry<String, Object>>();
        List<SimpleEntry<String, Object>> actual =
                CoreHelper.prepareFormFields(formParameters, ArraySerializationFormat.INDEXED);
        assertEquals(actual, expected);
    }

    @Test
    public void testPrepareFormFieldsPlainSerialization() {
        String bodyText = "test for prepare form fields";
        List<String> body = Arrays.asList(bodyText);
        Map<String, Object> formParameters = new HashMap<>();
        formParameters.put("body", body);

        List<SimpleEntry<String, Object>> expected = new ArrayList<SimpleEntry<String, Object>>();
        expected.add(new SimpleEntry<String, Object>("body", bodyText));
        List<SimpleEntry<String, Object>> actual =
                CoreHelper.prepareFormFields(formParameters, ArraySerializationFormat.PLAIN);
        assertEquals(actual, expected);
    }

    @Test
    public void testPrepareFormFieldsUnindexedSerialization() {
        String bodyText = "test for prepare form fields";
        List<String> body = Arrays.asList(bodyText);
        Map<String, Object> formParameters = new HashMap<>();
        formParameters.put("body", body);

        List<SimpleEntry<String, Object>> expected = new ArrayList<SimpleEntry<String, Object>>();
        expected.add(new SimpleEntry<String, Object>("body[]", bodyText));
        List<SimpleEntry<String, Object>> actual =
                CoreHelper.prepareFormFields(formParameters, ArraySerializationFormat.UNINDEXED);
        assertEquals(actual, expected);
    }

    @Test
    public void testPrepareFormFieldsCSVSerialization() {
        String bodyText = "test for prepare form fields";
        ArrayList<String> body = new ArrayList<>();
        body.add(bodyText);
        Map<String, Object> formParameters = new HashMap<>();
        formParameters.put("body", body);

        List<SimpleEntry<String, Object>> expected = new ArrayList<SimpleEntry<String, Object>>();
        expected.add(new SimpleEntry<String, Object>("body[0]", bodyText));
        List<SimpleEntry<String, Object>> actual =
                CoreHelper.prepareFormFields(formParameters, ArraySerializationFormat.CSV);
        assertEquals(actual, expected);
    }

    @Test
    public void testPrepareFormFieldPSVSerialization() {
        String bodyText = "test for prepare form fields";
        List<String> body = Arrays.asList(bodyText);
        Map<String, Object> formParameters = new HashMap<>();
        formParameters.put("body", body);

        List<SimpleEntry<String, Object>> expected = new ArrayList<SimpleEntry<String, Object>>();
        expected.add(new SimpleEntry<String, Object>("body[0]", bodyText));
        List<SimpleEntry<String, Object>> actual =
                CoreHelper.prepareFormFields(formParameters, ArraySerializationFormat.PSV);
        assertEquals(actual, expected);
    }

    @Test
    public void testPrepareFormFieldTSVSerialization() {
        String bodyText = "test for prepare form fields";
        List<String> body = Arrays.asList(bodyText);
        Map<String, Object> formParameters = new HashMap<>();
        formParameters.put("body", body);

        List<SimpleEntry<String, Object>> expected = new ArrayList<SimpleEntry<String, Object>>();
        expected.add(new SimpleEntry<String, Object>("body[0]", bodyText));
        List<SimpleEntry<String, Object>> actual =
                CoreHelper.prepareFormFields(formParameters, ArraySerializationFormat.TSV);
        assertEquals(actual, expected);
    }


    @Test
    public void testPrepareFormFields1() {
        Map<String, Object> formParameters = null;

        List<SimpleEntry<String, Object>> expected = new ArrayList<SimpleEntry<String, Object>>();
        List<SimpleEntry<String, Object>> actual =
                CoreHelper.prepareFormFields(formParameters, ArraySerializationFormat.INDEXED);
        assertEquals(actual, expected);
    }

    @Test
    public void testSerializeListOfInteger() throws JsonProcessingException {
        String expected = "[1,2,3,4,5]";
        String actual = CoreHelper.serialize(LIST_OF_INTEGERS);
        assertEquals(actual, expected);
    }

    @Test
    public void testSerializeMapOfString() throws JsonProcessingException {
        Map<String, Object> mapOfStrings = new HashMap<>();
        mapOfStrings.put("Electonics", "laptop");

        String expected = "{\"Electonics\":\"laptop\"}";
        String actual = CoreHelper.serialize(mapOfStrings);
        assertEquals(actual, expected);
    }

    @Test
    public void testUnixTimeStampSerializer() throws JsonProcessingException {
        LocalDateTime localDateTime =
                TestDateTimeHelper.getLocalDateTimeFromGMT(ZonedDateTime.of(YEAR1, MONTH1, DAY1,
                        HOUR1, MINUTES, 0, 0, ZoneId.of("GMT")));
        JsonSerializer<?> serializer = new LocalDateTimeHelper.UnixTimestampSerializer();
        String expected = "868756200";
        String actual = CoreHelper.serialize(localDateTime, serializer);

        assertEquals(actual, expected);
    }

    @Test
    public void testUnixTimeStampSerializerArray() throws JsonProcessingException {
        List<LocalDateTime> localDateTimeArray = new ArrayList<LocalDateTime>();
        localDateTimeArray.add(TestDateTimeHelper.getLocalDateTimeFromGMT(
                ZonedDateTime.of(YEAR1, MONTH1, DAY1, HOUR1, MINUTES, 0, 0, ZoneId.of("GMT"))));
        JsonSerializer<?> serializer = new LocalDateTimeHelper.UnixTimestampSerializer();
        String expected = "[868756200]";
        String actual = CoreHelper.serialize(localDateTimeArray, serializer);

        assertEquals(actual, expected);
    }

    @Test
    public void testUnixTimeStampSerializerMap() throws JsonProcessingException {
        Map<String, LocalDateTime> mapOfLocalDateTime = new LinkedHashMap<>();
        mapOfLocalDateTime.put("date", TestDateTimeHelper.getLocalDateTimeFromGMT(
                ZonedDateTime.of(YEAR1, MONTH1, DAY1, HOUR1, MINUTES, 0, 0, ZoneId.of("GMT"))));
        JsonSerializer<?> serializer = new LocalDateTimeHelper.UnixTimestampSerializer();
        String expected = "{\"date\":868756200}";
        String actual = CoreHelper.serialize(mapOfLocalDateTime, serializer);

        assertEquals(actual, expected);
    }

    @Test
    public void testUnixTimeStampSerializerNullObject() throws JsonProcessingException {
        LocalDateTime localDateTime = null;
        JsonSerializer<?> serializer = new LocalDateTimeHelper.UnixTimestampSerializer();
        String actual = CoreHelper.serialize(localDateTime, serializer);

        assertNull(actual);
    }

    @Test
    public void testUnixTimeStampSerializerNull() throws JsonProcessingException {
        LocalDateTime localDateTime =
                LocalDateTime.of(YEAR1, MONTH1, DAY1, XML_NO_OF_ELEMENT, MINUTES);
        JsonSerializer<?> serializer = null;
        String actual = CoreHelper.serialize(localDateTime, serializer);

        assertNull(actual);
    }

    @Test
    public void testSimpleDateDeserializer() throws IOException {
        List<LocalDate> expectedDates = new ArrayList<>();
        expectedDates.add(LocalDate.of(YEAR2, MONTH2, DAY1));
        expectedDates.add(LocalDate.of(YEAR2, MONTH2, DAY1));
        List<LocalDate> actualDates =
                CoreHelper.deserialize("[\"1994-02-13\",\"1994-02-13\"]",
                        new TypeReference<List<LocalDate>>() {}, LocalDate.class,
                        new DateHelper.SimpleDateDeserializer());
        assertEquals(actualDates, expectedDates);
    }

    @Test
    public void testDeserializerArray() throws IOException {
        List<Integer> expected = LIST_OF_INTEGERS;
        List<Integer> actual = CoreHelper.deserializeArray("[1,2,3,4,5]", Integer[].class);
        assertEquals(actual, expected);
    }

    @Test
    public void testDeserializerArrayNull() throws IOException {
        String json = null;
        List<Integer> actual = CoreHelper.deserializeArray(json, Integer[].class);
        assertNull(actual);
    }

    @Test
    public void testSimpleDateDeserializerNull() throws IOException {
        String json = null;
        List<LocalDate> actualDates =
                CoreHelper.deserialize(json, new TypeReference<List<LocalDate>>() {},
                        LocalDate.class, new DateHelper.SimpleDateDeserializer());
        assertNull(actualDates);
    }

    @Test
    public void testDeserializerWithClass() throws IOException {
        DeleteBody expected = new DeleteBody.Builder("ali", "QA").build();

        DeleteBody actual =
                CoreHelper.deserialize("{\"name\": \"ali\", \"field\": \"QA\"}", DeleteBody.class);
        assertEquals(actual.getName(), expected.getName());
        assertEquals(actual.getField(), expected.getField());
    }

    @Test
    public void testDeserializerWithClass1() throws IOException {
        String json = null;
        DeleteBody actual = CoreHelper.deserialize(json, DeleteBody.class);
        assertNull(actual);
    }



    @Test
    public void testDeserializeAsObject() {
        String json = "{\"name\": \"ali\", \"field\": \"QA\"}";
        LinkedHashMap<String, String> expected = new LinkedHashMap<>();
        expected.put("name", "ali");
        expected.put("field", "QA");
        Object actual = CoreHelper.deserializeAsObject(json);
        assertEquals(actual, expected);
    }

    @Test
    public void testDeserializeAsObjectInvalidJson() {
        String json = "{\"name\":, \"field\": \"QA\"}";
        Object actual = CoreHelper.deserializeAsObject(json);
        assertEquals(actual, json);
    }

    @Test
    public void testDeserializeAsObject1() {
        String json = null;
        Object actual = CoreHelper.deserializeAsObject(json);
        assertNull(actual);
    }

    @Test
    public void testDeserialize() throws IOException {
        String json = "{\"name\": \"ali\", \"field\": \"QA\"}";
        LinkedHashMap<String, String> expected = new LinkedHashMap<>();
        expected.put("name", "ali");
        expected.put("field", "QA");
        Object actual = CoreHelper.deserialize(json);
        assertEquals(actual, expected);
    }

    @Test
    public void testDeserialize1() throws IOException {
        String json = "{\"name\": \"ali\", \"field\": \"QA\"}";
        LinkedHashMap<String, String> expected = new LinkedHashMap<>();
        expected.put("name", "ali");
        expected.put("field", "QA");
        Object actual = CoreHelper.deserialize(json, new TypeReference<Object>() {});
        assertEquals(actual, expected);
    }

    @Test
    public void testDeserialize2() throws IOException {
        String json = null;
        Object actual = CoreHelper.deserialize(json, new TypeReference<Object>() {});
        assertNull(actual);
    }

    @Test
    public void testDeserializeNullJson() throws IOException {
        String json = null;
        Object actual = CoreHelper.deserialize(json);
        assertNull(actual);
    }

    @Test
    public void testDeserializeOneOf() throws IOException {
        String json =
                "{\"key1\":{\"NumberOfElectrons\":2,\"NumberOfProtons\":2},"
                        + "\"key2\":{\"NumberOfElectrons\":2,\"NumberOfProtons\":2}}";
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(json);

        Object result =
                CoreHelper.deserialize(jsonNode, Arrays.asList(AtomCase.class, OrbitCase.class),
                        true);
        assertNotNull(result);
    }

    @Test
    public void testDeserializeOneOfNull() throws IOException {
        JsonNode jsonNode = null;
        Object result =
                CoreHelper.deserialize(jsonNode, Arrays.asList(AtomCase.class, OrbitCase.class),
                        true);
        assertNull(result);
    }


    @Test
    public void testDeserializeOneOf1() throws IOException {
        String json = "{\"NumberOfElectrons\":2}";
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(json);

        Object result =
                CoreHelper.deserialize(jsonNode, Arrays.asList(AtomCase.class, OrbitCase.class),
                        true);
        assertNotNull(result);
    }

    @Test(expected = IOException.class)
    public void testDeserializeOneOf2() throws IOException {
        String json = "{\"RandomKey\":2}";
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(json);

        CoreHelper.deserialize(jsonNode, Arrays.asList(AtomCase.class, OrbitCase.class), true);
    }

    @Test(expected = IOException.class)
    public void testDeserializeOneOf3() throws IOException {
        String json =
                "[{\"NumberOfElectrons\":2},{\"NumberOfElectrons\":2, \"NumberOfProtons\":2}]";
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(json);

        CoreHelper.deserialize(jsonNode, Arrays.asList(AtomCase.class, OrbitCase.class), true);
    }


    @Test
    public void testDeserializeAnyOf() throws IOException {
        String json =
                "{\"key1\":{\"NumberOfElectrons\":2,\"NumberOfProtons\":2},"
                        + "\"key2\":{\"NumberOfElectrons\":2,\"NumberOfProtons\":2}}";
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(json);

        Object result =
                CoreHelper.deserialize(jsonNode, Arrays.asList(AtomCase.class, OrbitCase.class),
                        false);
        assertNotNull(result);
    }

    @Test
    public void testDeserializeOneOfAnyOf1() throws IOException {
        String json = "{\"NumberOfElectrons\":2}";
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(json);

        Object result =
                CoreHelper.deserialize(jsonNode, Arrays.asList(AtomCase.class, OrbitCase.class),
                        false);
        assertNotNull(result);
    }

    @Test(expected = IOException.class)
    public void testDeserializeOneOfAnyOf2() throws IOException {
        String json = "{\"RandomKey\":2}";
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(json);

        CoreHelper.deserialize(jsonNode, Arrays.asList(AtomCase.class, OrbitCase.class), false);
    }

    @Test(expected = IOException.class)
    public void testDeserializeAnyOf3() throws IOException {
        String json =
                "[{\"NumberOfElectrons\":2},{\"NumberOfElectrons\":2, \"NumberOfProtons\":2}]";
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(json);

        CoreHelper.deserialize(jsonNode, Arrays.asList(AtomCase.class, OrbitCase.class), false);
    }


    @Test
    public void testTypeCombinatorSerializationString() throws JsonProcessingException {
        SendScalarParamBody body = SendScalarParamBody.fromMString("some string");
        String expected = "some string";
        String actual = CoreHelper.serializeTypeCombinator(body);
        assertEquals(actual, expected);
    }

    @Test
    public void testTypeCombinatorSerializationStringNull() throws JsonProcessingException {
        SendScalarParamBody body = null;
        String actual = CoreHelper.serializeTypeCombinator(body);
        assertNull(actual);
    }

    @Test
    public void testTypeCombinatorSerializationString1() throws JsonProcessingException {
        SendScalarParamBody body = SendScalarParamBody.fromMString("some string");
        List<SimpleEntry<String, Object>> expected = new ArrayList<>();
        expected.add(new SimpleEntry<String, Object>("key1", "some string"));
        Map<String, Object> mapOfTypes = new HashMap<>();
        mapOfTypes.put("key1", body);

        List<SimpleEntry<String, Object>> actual =
                CoreHelper.prepareFormFields(mapOfTypes, ArraySerializationFormat.INDEXED);
        assertEquals(actual, expected);
    }

    @Test
    public void testTypeCombinatorSerializationInteger() throws JsonProcessingException {
        SendScalarParamBody body =
                SendScalarParamBody.fromPrecision(Arrays.asList(PRECISION_NUMBER));
        String expected = "[1.2]";
        String actual = CoreHelper.serializeTypeCombinator(body);
        assertEquals(actual, expected);
    }

    @Test
    public void testDeserializeTypeReferenceJsonNode() throws IOException {
        String json = "{\"key1\":254,\"key2\":254}";
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(json);

        Object result = CoreHelper.deserialize(jsonNode, new TypeReference<Object>() {});
        assertNotNull(result);
    }

    @Test
    public void testDeserializeTypeReferenceJsonNode1() throws IOException {
        JsonNode jsonNode = null;

        Object result = CoreHelper.deserialize(jsonNode, new TypeReference<Object>() {});
        assertNull(result);
    }

    @Test
    public void testDeserializeThroughParser() throws IOException {
        String discriminator = "sessionType";
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext context = mapper.getDeserializationContext();
        JsonParser jsonParser = mapper.createParser("{\"NumberOfTyres\":\"4\",\"HaveTrunk\":true}");

        Object actual =
                CoreHelper.deserialize(jsonParser, context, discriminator,
                        Arrays.asList(Collections.singletonMap("Morning", MorningCase.class)),
                        Arrays.asList(CarCase.class, AtomCase.class), true);

        assertNotNull(actual);
    }

    @Test(expected = IOException.class)
    public void testDeserializeThroughParser1() throws IOException {
        String discriminator = "sessionType";
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext context = mapper.getDeserializationContext();
        JsonParser jsonParser = mapper.createParser("{\"NumberOfTyres\":\"4\",\"HaveTrunk\":true}");

        CoreHelper.deserialize(jsonParser, context, discriminator,
                Arrays.asList(Collections.singletonMap("Morning", MorningCase.class)),
                Arrays.asList(), true);
    }


    @Test
    public void testDeserializeThroughParser2() throws IOException {
        String discriminator = "";
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext context = mapper.getDeserializationContext();
        JsonParser jsonParser = mapper.createParser("{\"NumberOfTyres\":\"4\",\"HaveTrunk\":true}");

        CoreHelper.deserialize(jsonParser, context, discriminator,
                Arrays.asList(Collections.singletonMap("Morning", MorningCase.class)),
                Arrays.asList(CarCase.class, AtomCase.class), true);
    }

    @Test
    public void testDeserializeThroughParser3() throws IOException {
        String discriminator = "";
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext context = mapper.getDeserializationContext();
        JsonParser jsonParser =
                mapper.createParser("[{\"NumberOfTyres\":\"4\",\"HaveTrunk\":true}]");

        CoreHelper.deserialize(jsonParser, context, discriminator,
                Arrays.asList(Collections.singletonMap("Morning", MorningCase.class)),
                Arrays.asList(CarCase[].class, AtomCase[].class), true);
    }

    @Test(expected = IOException.class)
    public void testDeserializeThroughParser4() throws IOException {
        String discriminator = "";
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext context = mapper.getDeserializationContext();
        JsonParser jsonParser =
                mapper.createParser("[{\"NumberOfTyres\":\"4\",\"HaveTrunk\":true}]");

        CoreHelper.deserialize(jsonParser, context, discriminator,
                Arrays.asList(Collections.singletonMap("Morning", MorningCase.class)), null, true);
    }

    @Test
    public void testDeserializeThroughParser5() throws IOException {
        JsonNode node = null;
        Object actual = CoreHelper.deserialize(node, null, null, null);
        assertNull(actual);
    }

    @Test
    public void testPrepareFormFieldOneOfAnyOf() throws IOException {
        NonScalarModel formNonScalarModel =
                CoreHelper.deserialize(
                        "{\"outerMap" + "\":{\"key1\":{\"startsAt\":\"15:00\",\"endsAt\":"
                                + "\"21:00\",\"offerLunch\":true,\"sessionType\":\"Noon\"}"
                                + ",\"key2\":{\"startsAt\":\"6:00\",\"endsAt\":\"11:00\","
                                + "\"offerTeaBreak\":true,\"sessionType\":\"Morning\"}}}",
                        NonScalarModel.class);

        Map<String, Object> formParameters = new HashMap<>();
        formParameters.put("Key1", formNonScalarModel);
        List<SimpleEntry<String, Object>> actual =
                CoreHelper.prepareFormFields(formParameters, ArraySerializationFormat.INDEXED);
        assertNotNull(actual);
    }

    @Test
    public void testPrepareFormFieldOneOfAnyOfDateTime() throws IOException {
        DateTimeCases formDateTimeCases =
                CoreHelper.deserialize(
                        "{\"mapvsArray\":{\"key1\":" + "\"Sun, 06 Nov 1994 08:49:37 GMT\","
                                + "\"key2\":\"Sun, 06 Nov 1994 08:49:37 GMT\"}}",
                        DateTimeCases.class);

        Map<String, Object> formParameters = new HashMap<>();
        formParameters.put("DateTime", formDateTimeCases);
        List<SimpleEntry<String, Object>> actual =
                CoreHelper.prepareFormFields(formParameters, ArraySerializationFormat.INDEXED);
        assertNotNull(actual);
    }


    @Test
    public void testDeserializeArrayJsonNode() throws IOException {
        String json = "[1.6, 2.3]";
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(json);
        List<Double> expectedArray = Arrays.asList(FLOAT_TEST_NUMBER1, FLOAT_TEST_NUMBER2);
        List<Double> actualArray = CoreHelper.deserializeArray(jsonNode, Double[].class);
        assertEquals(actualArray, expectedArray);
    }


    @Test
    public void testDeserializeArrayJsonNode1() throws IOException {
        JsonNode jsonNode = null;
        List<Double> actualArray = CoreHelper.deserializeArray(jsonNode, Double[].class);
        assertNull(actualArray);
    }

    @Test
    public void testDeserializeJsonNode() throws IOException {
        String json = "1.6";
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(json);
        Double expected = FLOAT_TEST_NUMBER1;
        Double actual = CoreHelper.deserialize(jsonNode, Double.class);
        assertEquals(actual, expected);
    }

    @Test
    public void testDeserializeJsonNode1() throws IOException {
        JsonNode jsonNode = null;
        Double actual = CoreHelper.deserialize(jsonNode, Double.class);
        assertNull(actual);
    }

    @Test
    public void testFormSerializationAnnotation() throws IOException {
        List<SendParamsFormDateTime> formDateTime = new ArrayList<>();
        formDateTime.add(SendParamsFormDateTime
                .fromDateTime(LocalDateTime.of(YEAR2, MONTH2, DAY1, HOUR2, MINUTE, SECONDS1)));
        formDateTime.add(SendParamsFormDateTime.fromDate(LocalDate.of(YEAR3, MONTH2, DAY1)));
        formDateTime.add(SendParamsFormDateTime.fromDate(LocalDate.of(YEAR3, MONTH2, DAY1)));

        Map<String, Object> formParameters = new HashMap<>();
        formParameters.put("date", formDateTime);

        List<SimpleEntry<String, Object>> formFields =
                CoreHelper.prepareFormFields(formParameters, ArraySerializationFormat.PLAIN);
        assertNotNull(formFields);
    }

    @Test
    public void testFormSerializationAnnotation1() throws IOException {
        List<Person> formDateTime = new ArrayList<>();
        formDateTime.add(new Person.Builder()
                .birthtime(LocalDateTime.of(YEAR3, MONTH2, DAY1, HOUR2, MINUTE, SECONDS1)).build());
        formDateTime.add(new Person.Builder()
                .birthtime(LocalDateTime.of(YEAR2010, MONTH2, DAY1, HOUR2, MINUTE, SECONDS1))
                .build());

        Map<String, Object> formParameters = new HashMap<>();
        formParameters.put("date", formDateTime);

        List<SimpleEntry<String, Object>> formFields =
                CoreHelper.prepareFormFields(formParameters, ArraySerializationFormat.PLAIN);
        assertNotNull(formFields);
    }

    @Test
    public void testSerializeXMLArray() throws IOException {
        String expected = XML_ARRAY.replace("\r\n", "");
        AttributesAndElements elements =
                new AttributesAndElements.Builder("XMLRootName", XML_NO_OF_ATTRIBUTE, "Data",
                        XML_NO_OF_ELEMENT).build();
        List<AttributesAndElements> attributesAndElements = new ArrayList<>();
        attributesAndElements.add(elements);
        String actual =
                CoreHelper.serializeXmlArray(
                        attributesAndElements
                                .toArray(new AttributesAndElements[attributesAndElements.size()]),
                        "arrayOfModels", "item", AttributesAndElements.class);
        assertEquals(actual.replace("\n", ""), expected);
    }

    @Test
    public void testSerializeXML() throws IOException {
        String expected = XML.replace("\r\n", "");
        AttributesAndElements elements =
                new AttributesAndElements.Builder("XMLRootName", XML_NO_OF_ATTRIBUTE, "Data",
                        XML_NO_OF_ELEMENT).build();

        String actual =
                CoreHelper.serializeXml(elements, "arrayOfModels", AttributesAndElements.class);
        assertEquals(actual.replace("\n", ""), expected);
    }

    @Test
    public void testDeserializeXML() throws IOException {
        AttributesAndElements expected =
                new AttributesAndElements.Builder("XMLRootName", XML_NO_OF_ATTRIBUTE, "Data",
                        XML_NO_OF_ELEMENT).build();

        AttributesAndElements actual = CoreHelper.deserializeXml(XML, AttributesAndElements.class);
        assertEquals(actual.getNumberAttr(), expected.getNumberAttr());
        assertEquals(actual.getNumberElement(), expected.getNumberElement());
        assertEquals(actual.getStringAttr(), expected.getStringAttr());
        assertEquals(actual.getStringElement(), expected.getStringElement());
    }

    @Test
    public void testDeserializeXMLArray() throws IOException {
        AttributesAndElements elements =
                new AttributesAndElements.Builder("XMLRootName", XML_NO_OF_ATTRIBUTE, "Data",
                        XML_NO_OF_ELEMENT).build();
        List<AttributesAndElements> expected = new ArrayList<>();
        expected.add(elements);

        List<AttributesAndElements> actual =
                CoreHelper.deserializeXmlArray(XML_ARRAY, AttributesAndElements[].class);
        assertEquals(actual.get(0).getNumberAttr(), expected.get(0).getNumberAttr());
    }

    @Test(expected = IOException.class)
    public void testDeserializeInvalidXMLArray() throws IOException {
        CoreHelper.deserializeXmlArray(INVALID_XML, AttributesAndElements[].class);
    }

    @Test(expected = IOException.class)
    public void testDeserializeInvalidXML() throws IOException {
        CoreHelper.deserializeXml(INVALID_XML, AttributesAndElements[].class);
    }

    @Test
    public void testDeserializeXMLArrayTypes() throws IOException {
        List<String> expected = Arrays.asList("6", "Data");
        List<String> actual = CoreHelper.deserializeXmlSimpleTypesArray(XML_ARRAY, String.class);
        assertEquals(actual, expected);
    }

    @Test
    public void testJSonObjectDeserialization() throws IOException {
        CoreJsonObject body =
                CoreJsonObject.fromJsonString(
                        "{\"$id\":\"https://example.com/person.schema.json\",\"$schema\":"
                                + "\"https://json-schema.org/draft/2020-12/schema\",\"title\":"
                                + "\"Person\",\"type\":\"object\",\"properties\":"
                                + "{\"firstName\":{\"type\":\"string\",\"description\":"
                                + "\"The person's first name.\"},\"lastName\":"
                                + "{\"type\":\"string\",\"description\":\"The person's last n"
                                + "ame.\",\"test\":null},\"age\":{\"type\":\"integer\","
                                + "\"description\":\"Age in years\",\"minimum\":0}}}");
        String baseUri = "https://localhost:3000";
        StringBuilder queryBuilder = new StringBuilder(baseUri + "/query");

        Map<String, Object> queryParameters = new HashMap<>();
        queryParameters.put("operations", body);

        CoreHelper.appendUrlWithQueryParameters(queryBuilder, queryParameters,
                ArraySerializationFormat.INDEXED);
        String expected = JSON_OBJECT;
        String actual = queryBuilder.toString();
        assertEquals(actual, expected);
    }

    @Test
    public void testJSonValueDeserialization() throws IOException {
        CoreJsonValue body = CoreJsonValue.fromString("test-JsonValue");
        String baseUri = "https://localhost:3000";
        StringBuilder queryBuilder = new StringBuilder(baseUri + "/query");

        Map<String, Object> queryParameters = new HashMap<>();
        queryParameters.put("operations", body);

        CoreHelper.appendUrlWithQueryParameters(queryBuilder, queryParameters,
                ArraySerializationFormat.INDEXED);
        String expected = JSON_VALUE;
        String actual = queryBuilder.toString();
        assertEquals(actual, expected);
    }

    private Map<String, ComplexType> getComplexType() throws IOException {
        Map<String, ComplexType> complexType =
                CoreHelper.deserialize(
                        "{\"key1\": {\"numberListType\":[555,666,777],\"numberMapType\":"
                                + "{\"num1\":1,\"num3\":2,\"num2\":3},\"innerComplexType"
                                + "\":{\"stringType\":\"MyString1\",\"booleanTyp"
                                + "e\":true,\"dateTimeType\":\"1994-11-06T08:49:37Z\""
                                + ",\"dateType\":\"1994-02-13\",\"uuidType\":"
                                + "\"a5e48529-745b-4dfb-aac0-a7d844debd8b\","
                                + "\"longType\":500000000,\"precisionType\":5.43,"
                                + "\"objectType\":{\"long2\":1000000000,\"long1\":500000000},"
                                + "\"stringListType\":[\"Item1\",\"Item2\"]},"
                                + "\"innerComplexListType\":[{\"stringTyp"
                                + "e\":\"MyString1\",\"booleanType\":true,\"dateTimeType\":"
                                + "\"1994-11-06T08:49:37Z\",\"dateType\":\"1994-02-13\","
                                + "\"uuidType\":\"a5e48529-745b-4dfb-aac0-a7d844debd"
                                + "8b\",\"longType\":500000000,\"precisionType\":5.43,"
                                + "\"objectType\":{\"long2\":1000000000,\"long1\":500000000}"
                                + ",\"stringListType\":[\"Item1\",\"Item2\"]},{\"string"
                                + "Type\":\"MyString2\",\"booleanType\":false,"
                                + "\"dateTimeType\":\"1994-11-07T08:49:37Z\",\"dateType\":"
                                + "\"1994-02-12\",\"uuidType\":\"b46ba2d3-b4ac-4b40-ae62-6326e88c"
                                + "89a6\",\"longType\":1000000000,\"precisionType\":5.43,"
                                + "\"objectType\":{\"bool1\":true,\"bool2\":false},"
                                + "\"stringListType\":[\"Item1\",\"Item2\"]}]}, \"key2\": {"
                                + "\"numberListType\":[555,666,777],\"numberMapType\":"
                                + "{\"num1\":1,\"num3\":2,\"num2\":3},\"innerComplexType\":"
                                + "{\"stringType\":\"MyString1\",\"booleanType\":true,"
                                + "\"dateTimeType\":\"1994-11-06T08:49:37Z\",\"dateType\":"
                                + "\"1994-02-13\",\"uuidType\":\"a5e48529-745b-4dfb-aac0-"
                                + "a7d844debd8b\",\"longType\":500000000,\"precisionType\":"
                                + "5.43,\"objectType\":{\"long2\":1000000000,\"long1\":500000000}"
                                + ",\"stringListType\":[\"Item1\",\"Item2\"]},"
                                + "\"innerComplexListType\":[{\"stringType\":\"MyString1\","
                                + "\"booleanType\":true,\"dateTimeType\":\"1994-11-06T08"
                                + ":49:37Z\",\"dateType\":\"1994-02-13\",\"uuidType\":"
                                + "\"a5e48529-745b-4dfb-aac0-a7d844debd8b\",\"longTy"
                                + "pe\":500000000,\"precisionType\":5.43,\"objectType\""
                                + ":{\"long2\":1000000000,\"long1\":500000000},\"stringListType\""
                                + ":[\"Item1\",\"Item2\"]},{\"stringType\":\"MySt"
                                + "ring2\",\"booleanType\":false,\"dateTimeType\":"
                                + "\"1994-11-07T08:49:37Z\",\"dateType\":\"1994-02-12\","
                                + "\"uuidType\":\"b46ba2d3-b4ac-4b40-ae62-6326e88c89a6\",\"long"
                                + "Type\":1000000000,\"precisionType\":5.43,\"objectType\":"
                                + "{\"bool1\":true,\"bool2\":false},\"stringListType\":[\"Item1\","
                                + "\"Item2\"]}]}}",
                        new TypeReference<Map<String, ComplexType>>() {});
        return complexType;
    }
}
