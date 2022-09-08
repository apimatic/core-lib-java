package apimatic.core_lib.static_classes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
import apimatic.core_lib.models.AtomCase;
import apimatic.core_lib.models.CarCase;
import apimatic.core_lib.models.DateTimeCases;
import apimatic.core_lib.models.DeleteBody;
import apimatic.core_lib.models.MorningCase;
import apimatic.core_lib.models.NonScalarModel;
import apimatic.core_lib.models.OrbitCase;
import apimatic.core_lib.models.Person;
import apimatic.core_lib.models.containers.SendParamsFormDateTime;
import apimatic.core_lib.models.containers.SendScalarParamBody;
import io.apimatic.core_interfaces.http.request.ArraySerializationFormat;
import io.apimatic.core_lib.utilities.CoreHelper;
import io.apimatic.core_lib.utilities.DateHelper;
import io.apimatic.core_lib.utilities.LocalDateTimeHelper;

public class CoreHelperTest {


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

        Map<String, Object> expected = Map.of("key2", "value2");
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

        String expected = "Java|31.8.0|JRE|" + System.getProperty("java.runtime.version") + "|"
                + System.getProperty("os.name") + "-" + System.getProperty("os.version");

        String actual = CoreHelper.updateUserAgent(userAgent, null);

        assertEquals(actual, expected);
    }

    @Test
    public void testUpdateUserAgent1() {
        String userAgent = "Java|31.8.0|{engine}|{engine-version}|{os-info}|{square-version}";
        Map<String, String> userAgentConfig = Map.of("{square-version}", "17.2.6");

        String expected = "Java|31.8.0|JRE|" + System.getProperty("java.runtime.version") + "|"
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
        String[] queryValue = new String[1];
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
        String expected = "https://localhost:3000/query?operations==x%2By";
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
        String expected = "https://localhost:3000/query?operations==x%2By";
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
        String expected = "https://localhost:3000/query?operations==x%2By";
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
        UUID body = new UUID(876547L, 87866L);
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
        List<Integer> listOfInteger = Arrays.asList(1, 2, 5, 8);

        String expected = "[1,2,5,8]";
        String actual = CoreHelper.serialize(listOfInteger);
        assertEquals(actual, expected);
    }

    @Test
    public void testSerializeMapOfString() throws JsonProcessingException {
        Map<String, String> mapOfStrings = Map.of("Electonics", "laptop");

        String expected = "{\"Electonics\":\"laptop\"}";
        String actual = CoreHelper.serialize(mapOfStrings);
        assertEquals(actual, expected);
    }

    @Test
    public void testUnixTimeStampSerializer() throws JsonProcessingException {
        LocalDateTime localDateTime = LocalDateTime.of(1997, 7, 13, 6, 10);
        JsonSerializer<?> serializer = new LocalDateTimeHelper.UnixTimestampSerializer();
        String expected = "868756200";
        String actual = CoreHelper.serialize(localDateTime, serializer);

        assertEquals(actual, expected);
    }

    @Test
    public void testUnixTimeStampSerializerArray() throws JsonProcessingException {
        List<LocalDateTime> localDateTimeArray = new ArrayList<LocalDateTime>();
        localDateTimeArray.add(LocalDateTime.of(1997, 7, 13, 6, 10));
        JsonSerializer<?> serializer = new LocalDateTimeHelper.UnixTimestampSerializer();
        String expected = "[868756200]";
        String actual = CoreHelper.serialize(localDateTimeArray, serializer);

        assertEquals(actual, expected);
    }

    @Test
    public void testUnixTimeStampSerializerMap() throws JsonProcessingException {
        Map<String, LocalDateTime> mapOfLocalDateTime = new LinkedHashMap<>();
        mapOfLocalDateTime.put("date", LocalDateTime.of(1997, 7, 13, 6, 10));
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
        LocalDateTime localDateTime = LocalDateTime.of(1997, 7, 13, 6, 10);
        JsonSerializer<?> serializer = null;
        String actual = CoreHelper.serialize(localDateTime, serializer);

        assertNull(actual);
    }

    @Test
    public void testSimpleDateDeserializer() throws IOException {
        List<LocalDate> expectedDates = new ArrayList<>();
        expectedDates.add(LocalDate.of(1994, 2, 13));
        expectedDates.add(LocalDate.of(1994, 2, 13));
        List<LocalDate> actualDates = CoreHelper.deserialize("[\"1994-02-13\",\"1994-02-13\"]",
                new TypeReference<List<LocalDate>>() {}, LocalDate.class,
                new DateHelper.SimpleDateDeserializer());
        assertEquals(actualDates, expectedDates);
    }

    @Test
    public void testDeserializerArray() throws IOException {
        List<Integer> expected = Arrays.asList(1, 2, 3, 4, 5);
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
                "{\"key1\":{\"NumberOfElectrons\":2,\"NumberOfProtons\":2},\"key2\":{\"NumberOfElectrons\":2,\"NumberOfProtons\":2}}";
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(json);

        Object result = CoreHelper.deserialize(jsonNode,
                Arrays.asList(AtomCase.class, OrbitCase.class), true);
        assertNotNull(result);
    }

    @Test
    public void testDeserializeOneOfNull() throws IOException {
        JsonNode jsonNode = null;
        Object result = CoreHelper.deserialize(jsonNode,
                Arrays.asList(AtomCase.class, OrbitCase.class), true);
        assertNull(result);
    }


    @Test
    public void testDeserializeOneOf1() throws IOException {
        String json = "{\"NumberOfElectrons\":2}";
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(json);

        Object result = CoreHelper.deserialize(jsonNode,
                Arrays.asList(AtomCase.class, OrbitCase.class), true);
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
                "{\"key1\":{\"NumberOfElectrons\":2,\"NumberOfProtons\":2},\"key2\":{\"NumberOfElectrons\":2,\"NumberOfProtons\":2}}";
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(json);

        Object result = CoreHelper.deserialize(jsonNode,
                Arrays.asList(AtomCase.class, OrbitCase.class), false);
        assertNotNull(result);
    }

    @Test
    public void testDeserializeOneOfAnyOf1() throws IOException {
        String json = "{\"NumberOfElectrons\":2}";
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(json);

        Object result = CoreHelper.deserialize(jsonNode,
                Arrays.asList(AtomCase.class, OrbitCase.class), false);
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
        SendScalarParamBody body = SendScalarParamBody.fromPrecision(Arrays.asList(1.2));
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

        Object actual = CoreHelper.deserialize(jsonParser, context, discriminator,
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
    public void testPrepareFormFieldOneOfAnyOf() throws IOException {
        NonScalarModel formNonScalarModel = CoreHelper.deserialize("{\"outerMap"
                + "\":{\"key1\":{\"startsAt\":\"15:00\",\"endsAt\":\"21:00\",\"offerLunch\":true,"
                + "\"sessionType\":\"Noon\"},\"key2\":{\"startsAt\":\"6:00\",\"endsAt\":\"11:00\","
                + "\"offerTeaBreak\":true,\"sessionType\":\"Morning\"}}}", NonScalarModel.class);

        List<SimpleEntry<String, Object>> actual = CoreHelper.prepareFormFields(
                Map.of("Key1", formNonScalarModel), ArraySerializationFormat.INDEXED);
        assertNotNull(actual);
    }

    @Test
    public void testPrepareFormFieldOneOfAnyOfDateTime() throws IOException {
        DateTimeCases formDateTimeCases = CoreHelper.deserialize(
                "{\"mapvsArray\":{\"key1\":\"Sun, 06 Nov 1994 08"
                        + ":49:37 GMT\",\"key2\":\"Sun, 06 Nov 1994 08:49:37 GMT\"}}",
                DateTimeCases.class);


        List<SimpleEntry<String, Object>> actual = CoreHelper.prepareFormFields(
                Map.of("DateTime", formDateTimeCases), ArraySerializationFormat.INDEXED);
        assertNotNull(actual);
    }


    @Test
    public void testDeserializeArrayJsonNode() throws IOException {
        String json = "[1.6, 2.3]";
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(json);
        List<Double> expectedArray = Arrays.asList(1.6, 2.3);
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
        Double expected = 1.6;
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
        formDateTime.add(
                SendParamsFormDateTime.fromDateTime(LocalDateTime.of(1994, 2, 13, 14, 01, 54)));
        formDateTime.add(SendParamsFormDateTime.fromDate(LocalDate.of(2020, 2, 13)));
        formDateTime.add(SendParamsFormDateTime.fromDate(LocalDate.of(2020, 2, 13)));

        Map<String, Object> formParameters = new HashMap<>();
        formParameters.put("date", formDateTime);

        List<SimpleEntry<String, Object>> formFields =
                CoreHelper.prepareFormFields(formParameters, ArraySerializationFormat.PLAIN);
        assertNotNull(formFields);
    }

    @Test
    public void testFormSerializationAnnotation1() throws IOException {
        List<Person> formDateTime = new ArrayList<>();
        formDateTime.add(
                new Person.Builder().birthtime(LocalDateTime.of(2020, 2, 13, 14, 01, 54)).build());
        formDateTime.add(
                new Person.Builder().birthtime(LocalDateTime.of(2010, 2, 13, 14, 01, 54)).build());

        Map<String, Object> formParameters = new HashMap<>();
        formParameters.put("date", formDateTime);

        List<SimpleEntry<String, Object>> formFields =
                CoreHelper.prepareFormFields(formParameters, ArraySerializationFormat.PLAIN);
        assertNotNull(formFields);
    }
}
