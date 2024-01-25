package apimatic.core.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.junit.Test;
import apimatic.core.models.Rfc1123Date;
import apimatic.core.models.Rfc1123DateArray;
import apimatic.core.models.Rfc1123DateMap;
import apimatic.core.models.Rfc8601Date;
import apimatic.core.models.Rfc8601DateArray;
import apimatic.core.models.Rfc8601DateMap;
import apimatic.core.models.SimpleDate;
import apimatic.core.models.SimpleDateArray;
import apimatic.core.models.SimpleDateMap;
import apimatic.core.models.UnixDate;
import apimatic.core.models.UnixDateArray;
import apimatic.core.models.UnixDateMap;
import io.apimatic.core.types.OptionalNullable;
import io.apimatic.core.utilities.CoreHelper;
import io.apimatic.core.utilities.LocalDateTimeHelper;
import io.apimatic.core.utilities.ZonedDateTimeHelper;

public class OptionalNullableTest {

    /**
     * Simple date string.
     */
    private static final String SIMPLE_DATE = "{\"dateNullable\":\"2020-01-08\"}";

    /**
     * Simple date array string.
     */
    private static final String SIMPLE_DATE_ARRAY = "{\"date\":[\"2020-01-08\",\"2020-01-08\"]}";

    /**
     * Simple date map string.
     */
    private static final String SIMPLE_DATE_MAP = "{\"date\":{\"key\":\"2020-01-08\"}}";

    /**
     * RFC 1123 date string.
     */
    private static final String RFC1123_DATE =
            "{\"dateTime1\":null,\"dateTime\":\"Wed, 20 Jan 2021 12:12:41 GMT\""
            + ",\"zonedDateTime\":\"Wed, 20 Jan 2021 12:12:41 GMT\"}";

    /**
     * RFC 1123 date array string.
     */
    private static final String RFC1123_DATE_ARRAY =
            "{\"dateTime1\":null,\"dateTime\":[\"Wed, 20 Jan 2021 12:12:41 GMT\","
            + "\"Wed, 20 Jan 2021 12:12:41 GMT\"],\"zonedDateTime\":["
            + "\"Wed, 20 Jan 2021 12:12:41 GMT\",\"Wed, 20 Jan 2021 12:12:41 GMT\"]}";
    /**
     * RFC 1123 Date array string.
     */
    private static final String RFC1123_DATE_ARRAY1 =
            "{\"dateTime1\":[\"Wed, 20 Jan 2021 12:12:41 GMT\","
                    + "\"Wed, 20 Jan 2021 12:12:41 GMT\"],\"dateTime\":null}";
    /**
     * RFC 1123 map array.
     */
    private static final String RFC1123_DATE_MAP =
            "{\"dateTime1\":null,\"dateTime\":{\"key\":\"Wed, 20 Jan 2021"
            + " 12:12:41 GMT\"},\"zonedDateTime\":{\"key\":\"Wed, 20 Jan 2021"
            + " 12:12:41 GMT\"}}";

    /**
     * RFC 8601 date string.
     */
    private static final String RFC8601_DATE =
            "{\"dateTime1\":null,\"dateTime\":\"2021-01-20T12:12:41Z\""
            + ",\"zonedDateTime\":\"2021-01-20T12:12:41Z\"}";
    /**
     * RFC 8601 date array string.
     */
    private static final String RFC8601_DATE_ARRAY =
            "{\"dateTime1\":null,\"dateTime\":[\"2021-01-20T12:12:41Z\""
            + ",\"2021-01-20T12:12:41Z\"],\"zonedDateTime\":["
            + "\"2021-01-20T12:12:41Z\",\"2021-01-20T12:12:41Z\"]}";
    /**
     * RFC 8601 date map string.
     */
    private static final String RFC8601_DATE_MAP =
            "{\"dateTime1\":null,\"dateTime\":{\"key\":\"2021-01-20T12:12:41Z\"}"
            + ",\"zonedDateTime\":{\"key\":\"2021-01-20T12:12:41Z\"}}";
    /**
     * RFC 8601 map to string.
     */
    private static final String RFC8601_DATE_MAP_TO_STRING =
            "Rfc8601DateMap [dateTime1=null, dateTime={key=2021-01-20T12:12:41},"
            + " zonedDateTime={key=2021-01-20T12:12:41Z[GMT]}]";
    /**
     * An instance of {@link LocalDateTime}.
     */
    private static final LocalDateTime LOCAL_DATE_TIME = LocalDateTime.of(2021, 1, 20, 12, 12, 41);
    /**
     * An instance of {@link ZonedDateTime}.
     */
    private static final ZonedDateTime ZONED_DATE_TIME =
            ZonedDateTime.of(LOCAL_DATE_TIME, ZoneId.of("GMT"));
    /**
     * An instance of {@link LocalDate}.
     */
    private static final LocalDate LOCAL_DATE = LocalDate.of(2020, 1, 8);
    /**
     * An instance of a number.
     */
    private static final int NUMBER_124 = 124;
    /**
     * An instance of a number.
     */
    private static final int NUMBER_125 = 125;

    @Test
    public void testSimpleDate() throws IOException {
        String expected = SIMPLE_DATE;
        SimpleDate simpleDate = new SimpleDate.Builder().dateNullable(LOCAL_DATE).build();
        String actual = CoreHelper.serialize(simpleDate);
        assertEquals(actual, expected);
    }

    @Test
    public void testSimpleDateArray() throws IOException {
        String expected = SIMPLE_DATE_ARRAY;
        SimpleDateArray simpleDateArray =
                new SimpleDateArray.Builder().date(Arrays.asList(LOCAL_DATE, LOCAL_DATE)).build();
        String actual = CoreHelper.serialize(simpleDateArray);
        assertEquals(actual, expected);
    }

    @Test
    public void testSimpleDateMap() throws IOException {
        String expected = SIMPLE_DATE_MAP;
        Map<String, LocalDate> mapOfLocalDate = new HashMap<>();
        mapOfLocalDate.put("key", LOCAL_DATE);
        SimpleDateMap simpleDateMap = new SimpleDateMap.Builder().date(mapOfLocalDate).build();
        String actual = CoreHelper.serialize(simpleDateMap);
        assertEquals(actual, expected);
    }

    @Test
    public void testUnixTimeStamp() throws IOException {
        String unixDateTime = LocalDateTimeHelper.toUnixTimestamp(LOCAL_DATE_TIME);
        String unixZonedDateTime = ZonedDateTimeHelper.toUnixTimestamp(ZONED_DATE_TIME);
        String expected = "{\"dateTime1\":null,\"dateTime\":" + unixDateTime
                + ",\"zonedDateTime\":" + unixZonedDateTime + "}";
        UnixDate unixDate = new UnixDate.Builder()
                .dateTime(LOCAL_DATE_TIME)
                .zonedDateTime(ZONED_DATE_TIME)
                .build();
        String actual = CoreHelper.serialize(unixDate);
        assertEquals(expected, actual);
    }

    @Test
    public void testUnixTimeStampArray() throws IOException {
        List<LocalDateTime> localDateTimes = Arrays.asList(LOCAL_DATE_TIME, LOCAL_DATE_TIME);
        String unixDateTimeArray = LocalDateTimeHelper
                .toUnixTimestamp(localDateTimes).toString().replace(" ", "");
        List<ZonedDateTime> zonedDateTimes = Arrays.asList(ZONED_DATE_TIME, ZONED_DATE_TIME);
        String unixZonedDateTimeArray = ZonedDateTimeHelper
                .toUnixTimestamp(zonedDateTimes).toString().replace(" ", "");
        String expected = "{\"dateTime1\":null,\"dateTime\":" + unixDateTimeArray
                + ",\"zonedDateTime\":" + unixZonedDateTimeArray + "}";
        UnixDateArray unixDateArray = new UnixDateArray.Builder()
                .dateTime(Arrays.asList(LOCAL_DATE_TIME, LOCAL_DATE_TIME))
                .zonedDateTime(Arrays.asList(ZONED_DATE_TIME, ZONED_DATE_TIME))
                .build();
        String actual = CoreHelper.serialize(unixDateArray);
        assertEquals(expected, actual);
    }


    @Test
    public void testUnixTimeStampMap() throws IOException {
        String unixDateTime = LocalDateTimeHelper.toUnixTimestamp(LOCAL_DATE_TIME);
        String unixZonedDateTime = ZonedDateTimeHelper.toUnixTimestamp(ZONED_DATE_TIME);
        String expected = "{\"dateTime1\":null,\"dateTime\":{\"key\":" + unixDateTime
                + "},\"zonedDateTime\":{\"key\":" + unixZonedDateTime + "}}";
        Map<String, LocalDateTime> mapOfLocalDateTime = new HashMap<>();
        mapOfLocalDateTime.put("key", LOCAL_DATE_TIME);
        Map<String, ZonedDateTime> mapOfZonedDateTime = new HashMap<>();
        mapOfZonedDateTime.put("key", ZONED_DATE_TIME);
        UnixDateMap unixDateMap =
                new UnixDateMap.Builder()
                .dateTime(mapOfLocalDateTime)
                .zonedDateTime(mapOfZonedDateTime)
                .build();
        String actual = CoreHelper.serialize(unixDateMap);
        assertEquals(expected, actual);
    }

    @Test
    public void testRfc1123Date() throws IOException {
        String expected = RFC1123_DATE;
        Rfc1123Date rfc1123Date = new Rfc1123Date.Builder()
                .dateTime(LOCAL_DATE_TIME)
                .zonedDateTime(ZONED_DATE_TIME)
                .build();
        String actual = CoreHelper.serialize(rfc1123Date);
        assertEquals(expected, actual);
    }

    @Test
    public void testRfc1123DateArray() throws IOException {
        String expected = RFC1123_DATE_ARRAY;
        Rfc1123DateArray rfc1123DateArray = new Rfc1123DateArray.Builder()
                .dateTime(Arrays.asList(LOCAL_DATE_TIME, LOCAL_DATE_TIME))
                .zonedDateTime(Arrays.asList(ZONED_DATE_TIME, ZONED_DATE_TIME))
                .build();
        String actual = CoreHelper.serialize(rfc1123DateArray);
        assertEquals(expected, actual);
    }

    @Test
    public void testRfc1123DateArrayGetDateTime1() throws IOException {
        Rfc1123DateArray expected =
                CoreHelper.deserialize(RFC1123_DATE_ARRAY, Rfc1123DateArray.class);
        Rfc1123DateArray actual =
                new Rfc1123DateArray.Builder()
                        .dateTime(Arrays.asList(LOCAL_DATE_TIME, LOCAL_DATE_TIME)).build();

        assertEquals(actual.getDateTime(), expected.getDateTime());
    }


    @Test
    public void testRfc1123DateMap() throws IOException {
        String expected = RFC1123_DATE_MAP;
        Map<String, LocalDateTime> mapOfLocalDateTime = new HashMap<>();
        mapOfLocalDateTime.put("key", LOCAL_DATE_TIME);
        Map<String, ZonedDateTime> mapOfZonedDateTime = new HashMap<>();
        mapOfZonedDateTime.put("key", ZONED_DATE_TIME);
        Rfc1123DateMap rfc1123DateMap = new Rfc1123DateMap.Builder()
                .dateTime(mapOfLocalDateTime)
                .zonedDateTime(mapOfZonedDateTime)
                .build();
        String actual = CoreHelper.serialize(rfc1123DateMap);
        assertEquals(expected, actual);
    }

    @Test
    public void testRfc1123DateArrayGetDateTimeNull() throws IOException {
        Rfc1123DateArray expected =
                CoreHelper.deserialize(RFC1123_DATE_ARRAY1, Rfc1123DateArray.class);
        Rfc1123DateArray actual =
                new Rfc1123DateArray.Builder()
                        .dateTime1(Arrays.asList(LOCAL_DATE_TIME, LOCAL_DATE_TIME)).build();

        assertEquals(actual.getDateTime(), expected.getDateTime());
    }


    @Test
    public void testRfc8601Date() throws IOException {
        String expected = RFC8601_DATE;
        Rfc8601Date rfc8601Date = new Rfc8601Date.Builder()
                .dateTime(LOCAL_DATE_TIME)
                .zonedDateTime(ZONED_DATE_TIME)
                .build();
        String actual = CoreHelper.serialize(rfc8601Date);
        assertEquals(expected, actual);
    }

    @Test
    public void testRfc8601DateArray() throws IOException {
        String expected = RFC8601_DATE_ARRAY;
        Rfc8601DateArray rfc8601DateArray = new Rfc8601DateArray.Builder()
                .dateTime(Arrays.asList(LOCAL_DATE_TIME, LOCAL_DATE_TIME))
                .zonedDateTime(Arrays.asList(ZONED_DATE_TIME, ZONED_DATE_TIME))
                .build();
        String actual = CoreHelper.serialize(rfc8601DateArray);
        assertEquals(expected, actual);
    }

    @Test
    public void testRfc8601DateMap() throws IOException {
        String expected = RFC8601_DATE_MAP;
        Map<String, LocalDateTime> mapOfLocalDateTime = new HashMap<>();
        mapOfLocalDateTime.put("key", LOCAL_DATE_TIME);
        Map<String, ZonedDateTime> mapOfZonedDateTime = new HashMap<>();
        mapOfZonedDateTime.put("key", ZONED_DATE_TIME);
        Rfc8601DateMap rfc8601DateMap = new Rfc8601DateMap.Builder()
                .dateTime(mapOfLocalDateTime)
                .zonedDateTime(mapOfZonedDateTime)
                .build();
        String actual = CoreHelper.serialize(rfc8601DateMap);
        assertEquals(expected, actual);
    }

    @Test
    public void testRfc8601DateMapToString() throws IOException {
        String expected = RFC8601_DATE_MAP_TO_STRING;
        Map<String, LocalDateTime> mapOfLocalDateTime = new HashMap<>();
        mapOfLocalDateTime.put("key", LOCAL_DATE_TIME);
        Map<String, ZonedDateTime> mapOfZonedDateTime = new HashMap<>();
        mapOfZonedDateTime.put("key", ZONED_DATE_TIME);
        Rfc8601DateMap rfc8601DateMap = new Rfc8601DateMap.Builder()
                .dateTime(mapOfLocalDateTime)
                .zonedDateTime(mapOfZonedDateTime)
                .build();

        String actual = rfc8601DateMap.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testEquals() throws IOException {
        OptionalNullable<String> object1 = OptionalNullable.of("some string");
        OptionalNullable<String> object2 = OptionalNullable.of("some string");
        OptionalNullable<String> object3 = OptionalNullable.of("some other string");
        OptionalNullable<Integer> object4 = OptionalNullable.of(NUMBER_124);
        OptionalNullable<Integer> object5 = OptionalNullable.of(null);

        assertTrue(object1.equals(object1));
        assertTrue(object1.equals(object2));
        assertTrue(object1.equals("some string"));
        assertTrue(object4.equals(NUMBER_124));
        assertTrue(object5.equals(null));

        assertFalse(object1.equals(object3));
        assertFalse(object1.equals(object4));
        assertFalse(object4.equals(NUMBER_125));
        assertFalse(object1.equals(null));
    }

    @Test
    public void testHashCode() throws IOException {
        OptionalNullable<String> object1 = OptionalNullable.of("some string");
        OptionalNullable<String> object2 = OptionalNullable.of("some string");
        OptionalNullable<Integer> object3 = OptionalNullable.of(NUMBER_124);

        assertEquals(object1.hashCode(), object2.hashCode());
        assertEquals(object1.hashCode(), Objects.hash("some string"));
        assertEquals(object3.hashCode(), Objects.hash(NUMBER_124));
    }
}
