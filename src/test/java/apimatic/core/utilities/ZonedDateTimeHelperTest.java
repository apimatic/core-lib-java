package apimatic.core.utilities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.apimatic.core.utilities.ZonedDateTimeHelper;

public class ZonedDateTimeHelperTest {


    private static final long UNIXTIMESTAMP4 = 963450600L;
    private static final long UNIXTIMESTAMP3 = 1595657400L;
    private static final long UNIXTIMESTAMP2 = 963468600L;
    private static final long UNIXTIMESTAMP1 = 868756200L;
    private static final int YEAR1997 = 1997;
    private static final int DAY25 = 25;
    private static final int YEAR2020 = 2020;
    private static final int MINUTES10 = 10;
    private static final int HOUR6 = 6;
    private static final int DAY13 = 13;
    private static final int JULY = 7;
    private static final int YEAR2000 = 2000;

    @Test
    public void testZonedDateTimeToRfc1123() {
        ZonedDateTime zonedDateTime =
                ZonedDateTime.of(YEAR1997, JULY, DAY13, HOUR6, MINUTES10, 0, 0, ZoneId.of("GMT"));
        // stub
        String expected = "Sun, 13 Jul 1997 06:10:00 GMT";
        String actual = ZonedDateTimeHelper.toRfc1123DateTime(zonedDateTime);
        assertEquals(actual, expected);
    }

    @Test
    public void testZonedDateTimeNullToRfc1123() {
        ZonedDateTime dateTime = null;
        assertNull(ZonedDateTimeHelper.toRfc1123DateTime(dateTime));
    }

    @Test
    public void testZonedDateTimeListToRfc1123() {
        ZonedDateTime zonedDateTime1 =
                ZonedDateTime.of(YEAR2000, JULY, DAY13, HOUR6, MINUTES10, 0, 0, ZoneId.of("GMT"));
        ZonedDateTime zonedDateTime2 =
                ZonedDateTime.of(YEAR2020, JULY, DAY25, HOUR6, MINUTES10, 0, 0, ZoneId.of("GMT"));
        List<ZonedDateTime> dateTimeArray = Arrays.asList(zonedDateTime1, zonedDateTime2);

        // stub
        List<String> expected =
                Arrays.asList("Thu, 13 Jul 2000 06:10:00 GMT", "Sat, 25 Jul 2020 06:10:00 GMT");

        List<String> actual = ZonedDateTimeHelper.toRfc1123DateTime(dateTimeArray);
        assertEquals(actual, expected);
    }

    @Test
    public void testZonedDateTimeNullListToRfc1123() {
        List<ZonedDateTime> dateTimeArray = null;
        assertNull(ZonedDateTimeHelper.toRfc1123DateTime(dateTimeArray));
    }


    @Test
    public void testZonedDateTimeMapToRfc1123() {
        ZonedDateTime zonedDateTime1 =
                ZonedDateTime.of(YEAR2000, JULY, DAY13, HOUR6, MINUTES10, 0, 0, ZoneId.of("GMT"));
        ZonedDateTime zonedDateTime2 =
                ZonedDateTime.of(YEAR2020, JULY, DAY25, HOUR6, MINUTES10, 0, 0, ZoneId.of("GMT"));
        Map<String, ZonedDateTime> dateTimeMap = new HashMap<>();
        dateTimeMap.put("dateTime1", zonedDateTime1);
        dateTimeMap.put("dateTime2", zonedDateTime2);

        // stub
        Map<String, String> expected = new HashMap<>();
        expected.put("dateTime1", "Thu, 13 Jul 2000 06:10:00 GMT");
        expected.put("dateTime2", "Sat, 25 Jul 2020 06:10:00 GMT");



        assertEquals(ZonedDateTimeHelper.toRfc1123DateTime(dateTimeMap), expected);
    }

    @Test
    public void testZonedDateTimeListOfMapToRfc1123() {
        ZonedDateTime zonedDateTime1 =
                ZonedDateTime.of(YEAR2000, JULY, DAY13, HOUR6, MINUTES10, 0, 0, ZoneId.of("GMT"));
        ZonedDateTime zonedDateTime2 =
                ZonedDateTime.of(YEAR2020, JULY, DAY25, HOUR6, MINUTES10, 0, 0, ZoneId.of("GMT"));
        Map<String, ZonedDateTime> dateTimeMap = new HashMap<>();
        dateTimeMap.put("dateTime1", zonedDateTime1);
        dateTimeMap.put("dateTime2", zonedDateTime2);

        List<Map<String, ZonedDateTime>> listOfMapOfLocalDateTime = Arrays.asList(dateTimeMap);

        // stub
        Map<String, String> mapOfString = new HashMap<>();
        mapOfString.put("dateTime1", "Thu, 13 Jul 2000 06:10:00 GMT");
        mapOfString.put("dateTime2", "Sat, 25 Jul 2020 06:10:00 GMT");

        List<Map<String, String>> expected = Arrays.asList(mapOfString);

        assertEquals(ZonedDateTimeHelper.toArrayOfMapOfRfc1123DateTime(listOfMapOfLocalDateTime),
                expected);

    }

    @Test
    public void testNullListOfMapToRfc1123() {
        List<Map<String, ZonedDateTime>> listOfMapOfLocalDateTime = null;
        assertNull(ZonedDateTimeHelper.toArrayOfMapOfRfc1123DateTime(listOfMapOfLocalDateTime));

    }

    @Test
    public void testZonedDateTimeMapValidateToRfc1123() {
        Map<String, ZonedDateTime> dateTimeMap = null;
        assertNull(ZonedDateTimeHelper.toRfc1123DateTime(dateTimeMap));
    }


    @Test
    public void testZonedDateTimeToRfc8601() {
        ZonedDateTime dateTime =
                ZonedDateTime.of(YEAR1997, JULY, DAY13, HOUR6, MINUTES10, 0, 0, ZoneId.of("GMT"));
        // stub
        String expected = "1997-07-13T06:10Z[GMT]";
        String actual = ZonedDateTimeHelper.toRfc8601DateTime(dateTime);
        assertEquals(actual, expected);
    }

    @Test
    public void testZonedDateTimeNullToRfc8601() {
        ZonedDateTime dateTime = null;
        assertEquals(ZonedDateTimeHelper.toRfc8601DateTime(dateTime), null);

    }

    @Test
    public void testZonedDateTimeListToRfc8601() {
        ZonedDateTime zonedDateTime1 =
                ZonedDateTime.of(YEAR2000, JULY, DAY13, HOUR6, MINUTES10, 0, 0, ZoneId.of("GMT"));
        ZonedDateTime zonedDateTime2 =
                ZonedDateTime.of(YEAR2020, JULY, DAY25, HOUR6, MINUTES10, 0, 0, ZoneId.of("GMT"));

        List<ZonedDateTime> dateTimeArray = Arrays.asList(zonedDateTime1, zonedDateTime2);

        // stub
        List<String> expected = Arrays.asList("2000-07-13T06:10Z[GMT]", "2020-07-25T06:10Z[GMT]");
        List<String> actual = ZonedDateTimeHelper.toRfc8601DateTime(dateTimeArray);

        assertEquals(actual, expected);
    }

    @Test
    public void testZonedDateTimeNullListToRfc8601() {
        List<ZonedDateTime> dateTimeArray = null;
        assertNull(ZonedDateTimeHelper.toRfc8601DateTime(dateTimeArray));
    }


    @Test
    public void testZonedDateTimeMapToRfc8601() {
        ZonedDateTime zonedDateTime1 =
                ZonedDateTime.of(YEAR2000, JULY, DAY13, HOUR6, MINUTES10, 0, 0, ZoneId.of("GMT"));
        ZonedDateTime zonedDateTime2 =
                ZonedDateTime.of(YEAR2020, JULY, DAY25, HOUR6, MINUTES10, 0, 0, ZoneId.of("GMT"));
        Map<String, ZonedDateTime> dateTimeMap = new HashMap<>();
        dateTimeMap.put("dateTime1", zonedDateTime1);
        dateTimeMap.put("dateTime2", zonedDateTime2);

        // stub
        Map<String, String> expected = new HashMap<>();
        expected.put("dateTime1", "2000-07-13T06:10Z[GMT]");
        expected.put("dateTime2", "2020-07-25T06:10Z[GMT]");

        assertEquals(ZonedDateTimeHelper.toRfc8601DateTime(dateTimeMap), expected);
    }

    @Test
    public void testZonedDateTimeListOfMapToRfc8601() {
        ZonedDateTime zonedDateTime1 =
                ZonedDateTime.of(YEAR2000, JULY, DAY13, HOUR6, MINUTES10, 0, 0, ZoneId.of("GMT"));
        ZonedDateTime zonedDateTime2 =
                ZonedDateTime.of(YEAR2020, JULY, DAY25, HOUR6, MINUTES10, 0, 0, ZoneId.of("GMT"));
        Map<String, ZonedDateTime> dateTimeMap = new HashMap<>();
        dateTimeMap.put("dateTime1", zonedDateTime1);
        dateTimeMap.put("dateTime2", zonedDateTime2);

        List<Map<String, ZonedDateTime>> listOfMapOfLocalDateTime = Arrays.asList(dateTimeMap);

        // stub
        Map<String, String> mapOfStirng = new HashMap<>();
        mapOfStirng.put("dateTime1", "2000-07-13T06:10Z[GMT]");
        mapOfStirng.put("dateTime2", "2020-07-25T06:10Z[GMT]");

        List<Map<String, String>> expected = Arrays.asList(mapOfStirng);

        List<Map<String, String>> actual =
                ZonedDateTimeHelper.toArrayOfMapOfRfc8601DateTime(listOfMapOfLocalDateTime);
        assertEquals(actual, expected);
    }

    @Test
    public void testNullListOfMapToRfc8601() {
        List<Map<String, ZonedDateTime>> listOfMapOfLocalDateTime = null;
        assertNull(ZonedDateTimeHelper.toArrayOfMapOfRfc8601DateTime(listOfMapOfLocalDateTime));

    }

    @Test
    public void testZonedDateTimeMapValidateToRfc8601() {
        Map<String, ZonedDateTime> dateTimeMap = null;
        assertNull(ZonedDateTimeHelper.toRfc8601DateTime(dateTimeMap));
    }


    @Test
    public void testZonedDateTimeToUnixTimeStamp() {
        LocalDateTime dateTime = LocalDateTime.of(YEAR1997, JULY, DAY13, 1, MINUTES10);
        ZonedDateTime zonedDateTime = dateTime.atZone(ZoneId.of("GMT"));

        // stub
        String expected = "868756200";
        assertEquals(ZonedDateTimeHelper.toUnixTimestamp(zonedDateTime), expected);
    }

    @Test
    public void testZonedDateTimeNullToUnixTimeStamp() {
        ZonedDateTime dateTime = null;
        assertNull(ZonedDateTimeHelper.toUnixTimestamp(dateTime));

    }

    @Test
    public void testZonedDateTimeListToUnixTimeStamp() {
        ZonedDateTime zonedDateTime1 =
                ZonedDateTime.of(YEAR2000, JULY, DAY13, HOUR6, MINUTES10, 0, 0, ZoneId.of("GMT"));
        ZonedDateTime zonedDateTime2 =
                ZonedDateTime.of(YEAR2020, JULY, DAY25, HOUR6, MINUTES10, 0, 0, ZoneId.of("GMT"));
        List<ZonedDateTime> dateTimeArray = Arrays.asList(zonedDateTime1, zonedDateTime2);

        // stub
        List<String> expected = Arrays.asList("963468600", "1595657400");


        assertEquals(ZonedDateTimeHelper.toUnixTimestamp(dateTimeArray), expected);
    }

    @Test
    public void testZonedDateTimeNullListToUnixTimeStamp() {
        List<ZonedDateTime> dateTimeArray = null;
        assertNull(ZonedDateTimeHelper.toUnixTimestamp(dateTimeArray));
    }

    @Test
    public void testZonedDateTimeMapToUnixTimeStamp() {
        ZonedDateTime zonedDateTime1 =
                ZonedDateTime.of(YEAR2000, JULY, DAY13, HOUR6, MINUTES10, 0, 0, ZoneId.of("GMT"));
        ZonedDateTime zonedDateTime2 =
                ZonedDateTime.of(YEAR2020, JULY, DAY25, HOUR6, MINUTES10, 0, 0, ZoneId.of("GMT"));
        Map<String, ZonedDateTime> dateTimeMap = new HashMap<>();
        dateTimeMap.put("dateTime1", zonedDateTime1);
        dateTimeMap.put("dateTime2", zonedDateTime2);

        // stub
        Map<String, String> expected = new HashMap<>();
        expected.put("dateTime1", "963468600");
        expected.put("dateTime2", "1595657400");

        assertEquals(ZonedDateTimeHelper.toUnixTimestamp(dateTimeMap), expected);

    }

    @Test
    public void testListOfMapToUnixTimeStamp() {
        ZonedDateTime zonedDateTime1 =
                ZonedDateTime.of(YEAR2000, JULY, DAY13, HOUR6, MINUTES10, 0, 0, ZoneId.of("GMT"));
        ZonedDateTime zonedDateTime2 =
                ZonedDateTime.of(YEAR2020, JULY, DAY25, HOUR6, MINUTES10, 0, 0, ZoneId.of("GMT"));
        Map<String, ZonedDateTime> dateTimeMap = new HashMap<>();
        dateTimeMap.put("dateTime1", zonedDateTime1);
        dateTimeMap.put("dateTime2", zonedDateTime2);

        List<Map<String, ZonedDateTime>> listOfMapOfLocalDateTime = Arrays.asList(dateTimeMap);

        // stub
        Map<String, String> mapOfStirng = new HashMap<>();
        mapOfStirng.put("dateTime1", "963468600");
        mapOfStirng.put("dateTime2", "1595657400");

        List<Map<String, String>> expected = Arrays.asList(mapOfStirng);

        assertEquals(ZonedDateTimeHelper.toArrayOfMapOfUnixTimestamp(listOfMapOfLocalDateTime),
                expected);

    }

    @Test
    public void testNullListOfMapToUnixTimeStamp() {
        List<Map<String, ZonedDateTime>> listOfMapOfLocalDateTime = null;
        assertNull(ZonedDateTimeHelper.toArrayOfMapOfUnixTimestamp(listOfMapOfLocalDateTime));

    }

    @Test
    public void testZonedDateTimeMapValidateToUnixTimeStamp() {
        Map<String, ZonedDateTime> dateTimeMap = null;
        assertNull(ZonedDateTimeHelper.toUnixTimestamp(dateTimeMap));
    }

    @Test
    public void testUnixDateTimeLong() {
        LocalDateTime dateTime = LocalDateTime.of(YEAR1997, JULY, DAY13, 1, MINUTES10);
        ZonedDateTime zonedDateTime = dateTime.atZone(ZoneId.of("GMT"));

        Long expectedValue = UNIXTIMESTAMP1;
        Long actualValue = ZonedDateTimeHelper.toUnixTimestampLong(zonedDateTime);

        assertEquals(actualValue, expectedValue);

    }

    @Test
    public void testNullUnixDateTimeLong() {
        ZonedDateTime dateTime = null;

        Long nullExpected = ZonedDateTimeHelper.toUnixTimestampLong(dateTime);

        assertNull(nullExpected);

    }

    @Test
    public void testListToUnixTimeLong() {
        ZonedDateTime zonedDateTime1 =
                ZonedDateTime.of(YEAR2000, JULY, DAY13, HOUR6, MINUTES10, 0, 0, ZoneId.of("GMT"));
        ZonedDateTime zonedDateTime2 =
                ZonedDateTime.of(YEAR2020, JULY, DAY25, HOUR6, MINUTES10, 0, 0, ZoneId.of("GMT"));
        List<ZonedDateTime> dateTimeArray = Arrays.asList(zonedDateTime1, zonedDateTime2);

        // stub
        List<Long> expected = Arrays.asList(UNIXTIMESTAMP2, UNIXTIMESTAMP3);


        assertEquals(ZonedDateTimeHelper.toUnixTimestampLong(dateTimeArray), expected);
    }

    @Test
    public void testNullListToUnixTimeLong() {
        List<ZonedDateTime> dateTimeArray = null;
        assertNull(ZonedDateTimeHelper.toUnixTimestampLong(dateTimeArray));
    }

    @Test
    public void testZonedDateTimeMapToUnixTimeLong() {
        ZonedDateTime zonedDateTime1 =
                ZonedDateTime.of(YEAR2000, JULY, DAY13, HOUR6, MINUTES10, 0, 0, ZoneId.of("GMT"));
        ZonedDateTime zonedDateTime2 =
                ZonedDateTime.of(YEAR2020, JULY, DAY25, HOUR6, MINUTES10, 0, 0, ZoneId.of("GMT"));
        Map<String, ZonedDateTime> dateTimeMap = new HashMap<>();
        dateTimeMap.put("dateTime1", zonedDateTime1);
        dateTimeMap.put("dateTime2", zonedDateTime2);

        // stub
        Map<String, Long> expected = new HashMap<>();
        expected.put("dateTime1", UNIXTIMESTAMP2);
        expected.put("dateTime2", UNIXTIMESTAMP3);

        assertEquals(ZonedDateTimeHelper.toUnixTimestampLong(dateTimeMap), expected);
    }

    @Test
    public void testListOfMapToUnixTimeLong() {
        ZonedDateTime zonedDateTime1 =
                ZonedDateTime.of(YEAR2000, JULY, DAY13, HOUR6, MINUTES10, 0, 0, ZoneId.of("GMT"));
        ZonedDateTime zonedDateTime2 =
                ZonedDateTime.of(YEAR2020, JULY, DAY25, HOUR6, MINUTES10, 0, 0, ZoneId.of("GMT"));
        Map<String, ZonedDateTime> dateTimeMap = new HashMap<>();
        dateTimeMap.put("dateTime1", zonedDateTime1);
        dateTimeMap.put("dateTime2", zonedDateTime2);

        List<Map<String, ZonedDateTime>> listOfMapOfLocalDateTime = Arrays.asList(dateTimeMap);
        // stub
        Map<String, Long> mapOfString = new HashMap<>();
        mapOfString.put("dateTime1", UNIXTIMESTAMP2);
        mapOfString.put("dateTime2", UNIXTIMESTAMP3);

        List<Map<String, Long>> expected = Arrays.asList(mapOfString);

        assertEquals(ZonedDateTimeHelper.toArrayOfMapOfUnixTimestampLong(listOfMapOfLocalDateTime),
                expected);
    }

    @Test
    public void testNullListOfMapToUnixTimeLong() {
        List<Map<String, ZonedDateTime>> listOfMapOfLocalDateTime = null;
        assertNull(ZonedDateTimeHelper.toArrayOfMapOfUnixTimestampLong(listOfMapOfLocalDateTime));

    }

    @Test
    public void testZonedDateTimeMapValidateToUnixTimeLong() {
        Map<String, ZonedDateTime> dateTimeMap = null;
        assertNull(ZonedDateTimeHelper.toUnixTimestampLong(dateTimeMap));
    }


    @Test
    public void testFromUnixTimeStampLong() {
        ZonedDateTime expected =
                ZonedDateTime.of(YEAR2000, JULY, DAY13, 1, MINUTES10, 0, 0, ZoneId.of("GMT"));

        Long date = UNIXTIMESTAMP4;
        ZonedDateTime actualValue = ZonedDateTimeHelper.fromUnixTimestamp(date);
        ZonedDateTime actual = ZonedDateTime.ofInstant(actualValue.toInstant(), ZoneId.of("GMT"));
        assertEquals(actual, expected);
    }

    @Test
    public void testFromUnixTimeStampString() {
        ZonedDateTime expected =
                ZonedDateTime.of(YEAR2000, JULY, DAY13, 1, MINUTES10, 0, 0, ZoneId.of("GMT"));

        String date = "963450600";
        ZonedDateTime actualValue = ZonedDateTimeHelper.fromUnixTimestamp(date);
        ZonedDateTime actual = ZonedDateTime.ofInstant(actualValue.toInstant(), ZoneId.of("GMT"));
        assertEquals(actual, expected);
    }

    @Test
    public void testFromRfc1123String() {
        String date = "Sun, 13 Jul 1997 06:10:00 GMT";

        ZonedDateTime expected =
                ZonedDateTime.of(YEAR1997, JULY, DAY13, HOUR6, MINUTES10, 0, 0, ZoneId.of("GMT"));
        ZonedDateTime actualValue = ZonedDateTimeHelper.fromRfc1123DateTime(date);
        assertEquals(actualValue, expected);
    }

    @Test
    public void testFromRfc8601String() {
        String date = "1997-07-13T01:10Z[GMT]";
        LocalDateTime dateTime = LocalDateTime.of(YEAR1997, JULY, DAY13, 1, MINUTES10);
        ZonedDateTime expected = dateTime.atZone(ZoneId.of("GMT"));
        ZonedDateTime actualValue = ZonedDateTimeHelper.fromRfc8601DateTime(date);
        assertEquals(actualValue, expected);
    }


    @SuppressWarnings("unchecked")
    @Test
    public void testRfc1123Serializer() throws JsonProcessingException {
        LocalDateTime localDateTime = LocalDateTime.of(YEAR1997, JULY, DAY13, 1, MINUTES10);
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.of("GMT"));
        @SuppressWarnings("rawtypes")
        JsonSerializer serializer = new ZonedDateTimeHelper.Rfc1123DateTimeSerializer();
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(zonedDateTime.getClass(), serializer);
        mapper.registerModule(module);

        String expected = "\"Sun, 13 Jul 1997 01:10:00 GMT\"";
        String actual = mapper.writeValueAsString(zonedDateTime);
        assertEquals(actual, expected);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testRfc1123Deserializer() throws JsonProcessingException {
        @SuppressWarnings("rawtypes")
        JsonDeserializer deserializer = new ZonedDateTimeHelper.Rfc1123DateTimeDeserializer();
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(ZonedDateTime.class, deserializer);
        mapper.registerModule(module);

        String datetime = "\"Sun, 13 Jul 1997 06:10:00 GMT\"";
        LocalDateTime localDateTime = LocalDateTime.of(YEAR1997, JULY, DAY13, HOUR6, MINUTES10);
        ZonedDateTime expected = localDateTime.atZone(ZoneId.of("GMT"));
        ZonedDateTime actual = mapper.readValue(datetime, ZonedDateTime.class);

        assertEquals(actual, expected);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testRfc8601Serializer() throws JsonProcessingException {
        LocalDateTime localDateTime = LocalDateTime.of(YEAR1997, JULY, DAY13, 1, MINUTES10);
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.of("GMT"));
        @SuppressWarnings("rawtypes")
        JsonSerializer serializer = new ZonedDateTimeHelper.Rfc8601DateTimeSerializer();
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(zonedDateTime.getClass(), serializer);
        mapper.registerModule(module);

        String expected = "\"1997-07-13T01:10Z[GMT]\"";

        String actual = mapper.writeValueAsString(zonedDateTime);

        assertEquals(actual, expected);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testRfc8601Deserializer() throws JsonProcessingException {
        @SuppressWarnings("rawtypes")
        JsonDeserializer deserializer = new ZonedDateTimeHelper.Rfc8601DateTimeDeserializer();
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(ZonedDateTime.class, deserializer);
        mapper.registerModule(module);

        String dateTime = "\"1997-07-13T06:10+05:00[Asia/Karachi]\"";

        LocalDateTime localDateTime = LocalDateTime.of(YEAR1997, JULY, DAY13, 1, MINUTES10);
        ZonedDateTime expected = localDateTime.atZone(ZoneId.of("GMT"));
        ZonedDateTime actual = mapper.readValue(dateTime, ZonedDateTime.class);
        ZonedDateTime actualGMT = ZonedDateTime.ofInstant(actual.toInstant(), ZoneId.of("GMT"));

        assertEquals(actualGMT, expected);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testUnixTimeStampSerializer() throws JsonProcessingException {
        LocalDateTime localDateTime = LocalDateTime.of(YEAR1997, JULY, DAY13, 1, MINUTES10);
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.of("GMT"));
        @SuppressWarnings("rawtypes")
        JsonSerializer serializer = new ZonedDateTimeHelper.UnixTimestampSerializer();
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(zonedDateTime.getClass(), serializer);
        mapper.registerModule(module);

        String expected = "868756200";
        String actual = mapper.writeValueAsString(zonedDateTime);
        assertEquals(actual, expected);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testUnixTimeStampDeserializer() throws JsonProcessingException {
        @SuppressWarnings("rawtypes")
        JsonDeserializer deserializer = new ZonedDateTimeHelper.UnixTimestampDeserializer();
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(ZonedDateTime.class, deserializer);
        mapper.registerModule(module);

        String datetime = "868756200";

        LocalDateTime localDateTime = LocalDateTime.of(YEAR1997, JULY, DAY13, 1, MINUTES10);
        ZonedDateTime expected = localDateTime.atZone(ZoneId.of("GMT"));
        ZonedDateTime actual = mapper.readValue(datetime, ZonedDateTime.class);
        ZonedDateTime actualGMT = ZonedDateTime.ofInstant(actual.toInstant(), ZoneId.of("GMT"));

        assertEquals(actualGMT, expected);
    }

}
