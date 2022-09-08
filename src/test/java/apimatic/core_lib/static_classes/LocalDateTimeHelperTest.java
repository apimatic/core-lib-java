package apimatic.core_lib.static_classes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.apimatic.core_lib.utilities.LocalDateTimeHelper;

public class LocalDateTimeHelperTest {


    @Test
    public void testLocalDateTimeToRfc1123() {
        LocalDateTime dateTime = LocalDateTime.of(1997, 7, 13, 6, 10);

        // stub
        String expected = "Sun, 13 Jul 1997 06:10:00 GMT";
        String actual = LocalDateTimeHelper.toRfc1123DateTime(dateTime);
        assertEquals(actual, expected);
    }

    @Test
    public void testLocalDateTimeNullToRfc1123() {
        LocalDateTime dateTime = null;
        assertNull(LocalDateTimeHelper.toRfc1123DateTime(dateTime));
    }

    @Test
    public void testLocalDateTimeListToRfc1123() {
        LocalDateTime dateTime1 = LocalDateTime.of(2000, 7, 13, 6, 10);
        LocalDateTime dateTime2 = LocalDateTime.of(2020, 7, 25, 6, 10);
        List<LocalDateTime> dateTimeArray = Arrays.asList(dateTime1, dateTime2);


        // stub
        List<String> expected =
                Arrays.asList("Thu, 13 Jul 2000 06:10:00 GMT", "Sat, 25 Jul 2020 06:10:00 GMT");


        assertEquals(LocalDateTimeHelper.toRfc1123DateTime(dateTimeArray), expected);
    }

    @Test
    public void testLocalDateTimeNullListToRfc1123() {
        List<LocalDateTime> dateTimeArray = null;
        assertNull(LocalDateTimeHelper.toRfc1123DateTime(dateTimeArray));
    }


    @Test
    public void testLocalDateTimeMapToRfc1123() {
        LocalDateTime dateTime1 = LocalDateTime.of(2000, 7, 13, 6, 10);
        LocalDateTime dateTime2 = LocalDateTime.of(2020, 7, 25, 6, 10);
        Map<String, LocalDateTime> dateTimeMap =
                Map.of("dateTime1", dateTime1, "dateTime2", dateTime2);

        // stub
        Map<String, String> expected = Map.of("dateTime1", "Thu, 13 Jul 2000 06:10:00 GMT",
                "dateTime2", "Sat, 25 Jul 2020 06:10:00 GMT");

        assertEquals(LocalDateTimeHelper.toRfc1123DateTime(dateTimeMap), expected);

    }

    @Test
    public void testLocalDateTimeListOfMapToRfc1123() {
        LocalDateTime dateTime1 = LocalDateTime.of(2000, 7, 13, 6, 10);
        LocalDateTime dateTime2 = LocalDateTime.of(2020, 7, 25, 6, 10);
        Map<String, LocalDateTime> dateTimeMap =
                Map.of("dateTime1", dateTime1, "dateTime2", dateTime2);

        List<Map<String, LocalDateTime>> listOfMapOfLocalDateTime = Arrays.asList(dateTimeMap);

        // stub
        List<Map<String, String>> expected = Arrays.asList(Map.of("dateTime1",
                "Thu, 13 Jul 2000 06:10:00 GMT", "dateTime2", "Sat, 25 Jul 2020 06:10:00 GMT"));

        assertEquals(LocalDateTimeHelper.toArrayOfMapOfRfc1123DateTime(listOfMapOfLocalDateTime),
                expected);

    }

    @Test
    public void testNullListOfMapToRfc1123() {
        List<Map<String, LocalDateTime>> listOfMapOfLocalDateTime = null;
        assertNull(LocalDateTimeHelper.toArrayOfMapOfRfc1123DateTime(listOfMapOfLocalDateTime));

    }

    @Test
    public void testLocalDateTimeMapValidateToRfc1123() {
        Map<String, LocalDateTime> dateTimeMap = null;
        assertNull(LocalDateTimeHelper.toRfc1123DateTime(dateTimeMap));
    }


    @Test
    public void testLocalDateTimeToRfc8601() {
        LocalDateTime dateTime = LocalDateTime.of(1997, 7, 13, 6, 10);

        // stub
        String expected = "1997-07-13T06:10Z";
        assertEquals(LocalDateTimeHelper.toRfc8601DateTime(dateTime), expected);
    }

    @Test
    public void testLocalDateTimeNullToRfc8601() {
        LocalDateTime dateTime = null;
        assertEquals(LocalDateTimeHelper.toRfc8601DateTime(dateTime), null);

    }

    @Test
    public void testLocalDateTimeListToRfc8601() {
        LocalDateTime dateTime1 = LocalDateTime.of(2000, 7, 13, 6, 10);
        LocalDateTime dateTime2 = LocalDateTime.of(2020, 7, 25, 6, 10);
        List<LocalDateTime> dateTimeArray = Arrays.asList(dateTime1, dateTime2);
        // stub
        List<String> expected = Arrays.asList("2000-07-13T06:10Z", "2020-07-25T06:10Z");


        assertEquals(LocalDateTimeHelper.toRfc8601DateTime(dateTimeArray), expected);
    }

    @Test
    public void testLocalDateTimeNullListToRfc8601() {
        List<LocalDateTime> dateTimeArray = null;
        assertNull(LocalDateTimeHelper.toRfc8601DateTime(dateTimeArray));
    }


    @Test
    public void testLocalDateTimeMapToRfc8601() {
        LocalDateTime dateTime1 = LocalDateTime.of(2000, 7, 13, 6, 10);
        LocalDateTime dateTime2 = LocalDateTime.of(2020, 7, 25, 6, 10);
        Map<String, LocalDateTime> dateTimeMap =
                Map.of("dateTime1", dateTime1, "dateTime2", dateTime2);

        // stub
        Map<String, String> expected =
                Map.of("dateTime1", "2000-07-13T06:10Z", "dateTime2", "2020-07-25T06:10Z");

        assertEquals(LocalDateTimeHelper.toRfc8601DateTime(dateTimeMap), expected);

    }

    @Test
    public void testLocalDateTimeListOfMapToRfc8601() {
        LocalDateTime dateTime1 = LocalDateTime.of(2000, 7, 13, 6, 10);
        LocalDateTime dateTime2 = LocalDateTime.of(2020, 7, 25, 6, 10);
        Map<String, LocalDateTime> dateTimeMap =
                Map.of("dateTime1", dateTime1, "dateTime2", dateTime2);

        List<Map<String, LocalDateTime>> listOfMapOfLocalDateTime = Arrays.asList(dateTimeMap);

        // stub
        List<Map<String, String>> expected = Arrays
                .asList(Map.of("dateTime1", "2000-07-13T06:10Z", "dateTime2", "2020-07-25T06:10Z"));

        assertEquals(LocalDateTimeHelper.toArrayOfMapOfRfc8601DateTime(listOfMapOfLocalDateTime),
                expected);

    }

    @Test
    public void testNullListOfMapToRfc8601() {
        List<Map<String, LocalDateTime>> listOfMapOfLocalDateTime = null;
        assertNull(LocalDateTimeHelper.toArrayOfMapOfRfc8601DateTime(listOfMapOfLocalDateTime));

    }

    @Test
    public void testLocalDateTimeMapValidateToRfc8601() {
        Map<String, LocalDateTime> dateTimeMap = null;
        assertNull(LocalDateTimeHelper.toRfc8601DateTime(dateTimeMap));
    }


    @Test
    public void testLocalDateTimeToUnixTimeStamp() {
        LocalDateTime dateTime = LocalDateTime.of(1997, 7, 13, 6, 10);
        // stub
        String expected = "868756200";
        assertEquals(LocalDateTimeHelper.toUnixTimestamp(dateTime), expected);
    }

    @Test
    public void testLocalDateTimeNullToUnixTimeStamp() {
        LocalDateTime dateTime = null;
        assertEquals(LocalDateTimeHelper.toUnixTimestamp(dateTime), null);

    }

    @Test
    public void testLocalDateTimeListToUnixTimeStamp() {
        LocalDateTime dateTime1 = LocalDateTime.of(2000, 7, 13, 6, 10);
        LocalDateTime dateTime2 = LocalDateTime.of(2020, 7, 25, 6, 10);
        List<LocalDateTime> dateTimeArray = Arrays.asList(dateTime1, dateTime2);

        // stub
        List<String> expected = Arrays.asList("963450600", "1595639400");


        assertEquals(LocalDateTimeHelper.toUnixTimestamp(dateTimeArray), expected);
    }

    @Test
    public void testLocalDateTimeNullListToUnixTimeStamp() {
        List<LocalDateTime> dateTimeArray = null;
        assertNull(LocalDateTimeHelper.toUnixTimestamp(dateTimeArray));
    }


    @Test
    public void testLocalDateTimeMapToUnixTimeStamp() {
        LocalDateTime dateTime1 = LocalDateTime.of(2000, 7, 13, 6, 10);
        LocalDateTime dateTime2 = LocalDateTime.of(2020, 7, 25, 6, 10);
        Map<String, LocalDateTime> dateTimeMap =
                Map.of("dateTime1", dateTime1, "dateTime2", dateTime2);
        // stub
        Map<String, String> expected = Map.of("dateTime1", "963450600", "dateTime2", "1595639400");
        assertEquals(LocalDateTimeHelper.toUnixTimestamp(dateTimeMap), expected);

    }

    @Test
    public void testListOfMapToUnixTimeStamp() {
        LocalDateTime dateTime1 = LocalDateTime.of(2000, 7, 13, 6, 10);
        LocalDateTime dateTime2 = LocalDateTime.of(2020, 7, 25, 6, 10);
        Map<String, LocalDateTime> dateTimeMap =
                Map.of("dateTime1", dateTime1, "dateTime2", dateTime2);

        List<Map<String, LocalDateTime>> listOfMapOfLocalDateTime = Arrays.asList(dateTimeMap);

        // stub
        List<Map<String, String>> expected =
                Arrays.asList(Map.of("dateTime1", "963450600", "dateTime2", "1595639400"));

        assertEquals(LocalDateTimeHelper.toArrayOfMapOfUnixTimestamp(listOfMapOfLocalDateTime),
                expected);

    }

    @Test
    public void testNullListOfMapToUnixTimeStamp() {
        List<Map<String, LocalDateTime>> listOfMapOfLocalDateTime = null;
        assertNull(LocalDateTimeHelper.toArrayOfMapOfUnixTimestamp(listOfMapOfLocalDateTime));

    }

    @Test
    public void testLocalDateTimeMapValidateToUnixTimeStamp() {
        Map<String, LocalDateTime> dateTimeMap = null;
        assertNull(LocalDateTimeHelper.toUnixTimestamp(dateTimeMap));
    }

    @Test
    public void testUnixDateTimeLong() {
        LocalDateTime dateTime = LocalDateTime.of(1997, 7, 13, 6, 10);

        Long expectedValue = 868756200L;
        Long actualValue = LocalDateTimeHelper.toUnixTimestampLong(dateTime);

        assertEquals(actualValue, expectedValue);

    }

    @Test
    public void testNullUnixDateTimeLong() {
        LocalDateTime dateTime = null;

        Long nullExpected = LocalDateTimeHelper.toUnixTimestampLong(dateTime);

        assertNull(nullExpected);

    }

    @Test
    public void testListToUnixTimeLong() {
        LocalDateTime dateTime1 = LocalDateTime.of(2000, 7, 13, 6, 10);
        LocalDateTime dateTime2 = LocalDateTime.of(2020, 7, 25, 6, 10);
        List<LocalDateTime> dateTimeArray = Arrays.asList(dateTime1, dateTime2);

        // stub
        List<Long> expected = Arrays.asList(963450600L, 1595639400L);


        assertEquals(LocalDateTimeHelper.toUnixTimestampLong(dateTimeArray), expected);
    }

    @Test
    public void testNullListToUnixTimeLong() {
        List<LocalDateTime> dateTimeArray = null;
        assertNull(LocalDateTimeHelper.toUnixTimestampLong(dateTimeArray));
    }

    @Test
    public void testLocalDateTimeMapToUnixTimeLong() {
        LocalDateTime dateTime1 = LocalDateTime.of(2000, 7, 13, 6, 10);
        LocalDateTime dateTime2 = LocalDateTime.of(2020, 7, 25, 6, 10);
        Map<String, LocalDateTime> dateTimeMap =
                Map.of("dateTime1", dateTime1, "dateTime2", dateTime2);
        // stub
        Map<String, Long> expected = Map.of("dateTime1", 963450600L, "dateTime2", 1595639400L);
        assertEquals(LocalDateTimeHelper.toUnixTimestampLong(dateTimeMap), expected);

    }

    @Test
    public void testListOfMapToUnixTimeLong() {
        LocalDateTime dateTime1 = LocalDateTime.of(2000, 7, 13, 6, 10);
        LocalDateTime dateTime2 = LocalDateTime.of(2020, 7, 25, 6, 10);
        Map<String, LocalDateTime> dateTimeMap =
                Map.of("dateTime1", dateTime1, "dateTime2", dateTime2);

        List<Map<String, LocalDateTime>> listOfMapOfLocalDateTime = Arrays.asList(dateTimeMap);

        // stub
        List<Map<String, Long>> expected =
                Arrays.asList(Map.of("dateTime1", 963450600L, "dateTime2", 1595639400L));

        assertEquals(LocalDateTimeHelper.toArrayOfMapOfUnixTimestampLong(listOfMapOfLocalDateTime),
                expected);

    }

    @Test
    public void testNullListOfMapToUnixTimeLong() {
        List<Map<String, LocalDateTime>> listOfMapOfLocalDateTime = null;
        assertNull(LocalDateTimeHelper.toArrayOfMapOfUnixTimestampLong(listOfMapOfLocalDateTime));

    }

    @Test
    public void testLocalDateTimeMapValidateToUnixTimeLong() {
        Map<String, LocalDateTime> dateTimeMap = null;
        assertNull(LocalDateTimeHelper.toUnixTimestampLong(dateTimeMap));
    }


    @Test
    public void testFromUnixTimeStampLong() {
        LocalDateTime expected = LocalDateTime.of(2000, 7, 13, 6, 10);
        Long date = 963450600L;

        LocalDateTime actualValue = LocalDateTimeHelper.fromUnixTimestamp(date);
        assertEquals(actualValue, expected);
    }

    @Test
    public void testFromUnixTimeStampString() {
        LocalDateTime expected = LocalDateTime.of(2000, 7, 13, 6, 10);
        String date = "963450600";

        LocalDateTime actualValue = LocalDateTimeHelper.fromUnixTimestamp(date);
        assertEquals(actualValue, expected);
    }

    @Test
    public void testFromRfc1123String() {
        String date = "Sun, 13 Jul 1997 06:10:00 GMT";
        LocalDateTime expected = LocalDateTime.of(1997, 7, 13, 6, 10);
        LocalDateTime actualValue = LocalDateTimeHelper.fromRfc1123DateTime(date);
        assertEquals(actualValue, expected);
    }

    @Test
    public void testFromRfc8601String() {
        String date = "1997-07-13T06:10Z";
        LocalDateTime expected = LocalDateTime.of(1997, 7, 13, 6, 10);
        LocalDateTime actualValue = LocalDateTimeHelper.fromRfc8601DateTime(date);
        assertEquals(actualValue, expected);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testRfc1123Serializer() throws JsonProcessingException {
        LocalDateTime localDateTime = LocalDateTime.of(1997, 7, 13, 6, 10);
        JsonSerializer serializer = new LocalDateTimeHelper.Rfc1123DateTimeSerializer();
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(localDateTime.getClass(), serializer);
        mapper.registerModule(module);

        String expected = "\"Sun, 13 Jul 1997 06:10:00 GMT\"";

        String actual = mapper.writeValueAsString(localDateTime);;

        assertEquals(actual, expected);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testRfc1123Deserializer() throws JsonProcessingException {
        JsonDeserializer deserializer = new LocalDateTimeHelper.Rfc1123DateTimeDeserializer();
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(LocalDateTime.class, deserializer);
        mapper.registerModule(module);

        String datetime = "\"Sun, 13 Jul 1997 06:10:00 GMT\"";
        LocalDateTime expected = LocalDateTime.of(1997, 7, 13, 6, 10);
        LocalDateTime actual = mapper.readValue(datetime, LocalDateTime.class);;

        assertEquals(actual, expected);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testRfc8601Serializer() throws JsonProcessingException {
        LocalDateTime localDateTime = LocalDateTime.of(1997, 7, 13, 6, 10);
        JsonSerializer serializer = new LocalDateTimeHelper.Rfc8601DateTimeSerializer();
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(localDateTime.getClass(), serializer);
        mapper.registerModule(module);

        String expected = "\"1997-07-13T06:10Z\"";

        String actual = mapper.writeValueAsString(localDateTime);

        assertEquals(actual, expected);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testRfc8601Deserializer() throws JsonProcessingException {
        JsonDeserializer deserializer = new LocalDateTimeHelper.Rfc8601DateTimeDeserializer();
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(LocalDateTime.class, deserializer);
        mapper.registerModule(module);

        String dateTime = "\"1997-07-13T06:10Z\"";
        LocalDateTime expected = LocalDateTime.of(1997, 7, 13, 6, 10);

        LocalDateTime actual = mapper.readValue(dateTime, LocalDateTime.class);

        assertEquals(actual, expected);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testUnixTimeStampSerializer() throws JsonProcessingException {
        LocalDateTime localDateTime = LocalDateTime.of(1997, 7, 13, 6, 10);
        JsonSerializer serializer = new LocalDateTimeHelper.UnixTimestampSerializer();
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(localDateTime.getClass(), serializer);
        mapper.registerModule(module);

        String expected = "868756200";

        String actual = mapper.writeValueAsString(localDateTime);;

        assertEquals(actual, expected);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testUnixTimeStampDeSerializer() throws JsonProcessingException {
        JsonDeserializer deserializer = new LocalDateTimeHelper.UnixTimestampDeserializer();
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(LocalDateTime.class, deserializer);
        mapper.registerModule(module);

        String dateTime = "868756200";
        LocalDateTime expected = LocalDateTime.of(1997, 7, 13, 6, 10);
        LocalDateTime actual = mapper.readValue(dateTime, LocalDateTime.class);

        assertEquals(actual, expected);
    }
}
