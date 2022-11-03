package apimatic.core.type;

import static org.junit.Assert.assertEquals;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import io.apimatic.core.utilities.CoreHelper;
import io.apimatic.core.utilities.LocalDateTimeHelper;

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
            "{\"dateTime\":\"Wed, 20 Jan 2021 12:12:41 GMT\",\"dateTime1\":null}";

    /**
     * RFC 1123 date array string.
     */
    private static final String RFC1123_DATE_ARRAY =
            "{\"dateTime\":[\"Wed, 20 Jan 2021 12:12:41 GMT\","
                    + "\"Wed, 20 Jan 2021 12:12:41 GMT\"],\"dateTime1\":null}";
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
            "{\"dateTime\":{\"key\":\"Wed, 20 Jan 2021 12:12:41 GMT\"},\"dateTime1\":null}";

    /**
     * RFC 8601 date string.
     */
    private static final String RFC8601_DATE =
            "{\"dateTime\":\"2021-01-20T12:12:41Z\",\"dateTime1\":null}";
    /**
     * RFC 8601 date array string.
     */
    private static final String RFC8601_DATE_ARRAY =
            "{\"dateTime\":[\"2021-01-20T12:12:41Z\",\"2021-01-20T12:12:41Z\"],"
                    + "\"dateTime1\":null}";
    /**
     * RFC 8601 date map string.
     */
    private static final String RFC8601_DATE_MAP =
            "{\"dateTime\":{\"key\":\"2021-01-20T12:12:41Z\"},\"dateTime1\":null}";
    /**
     * RFC 8601 map to string.
     */
    private static final String RFC8601_DATE_MAP_TO_STRING =
            "Rfc8601DateMap [dateTime1=null, dateTime={key=2021-01-20T12:12:41}]";
    /**
     * An instance of {@link LocalDateTime}.
     */
    private static final LocalDateTime LOCAL_DATE_TIME = LocalDateTime.of(2021, 1, 20, 12, 12, 41);

    /**
     * An instance of {@link LocalDate}.
     */
    private static final LocalDate LOCAL_DATE = LocalDate.of(2020, 1, 8);

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
        String expected = "{\"dateTime\":" + unixDateTime + ",\"dateTime1\":null}";
        UnixDate unixDate = new UnixDate.Builder().dateTime(LOCAL_DATE_TIME).build();
        String actual = CoreHelper.serialize(unixDate);
        assertEquals(actual, expected);
    }

    @Test
    public void testUnixTimeStampArray() throws IOException {
        List<LocalDateTime> localDateTimes = Arrays.asList(LOCAL_DATE_TIME, LOCAL_DATE_TIME);
        String unixDateTimeArray =
                LocalDateTimeHelper.toUnixTimestamp(localDateTimes).toString().replace(" ", "");
        String expected = "{\"dateTime\":" + unixDateTimeArray + ",\"dateTime1\":null}";
        UnixDateArray unixDateArray =
                new UnixDateArray.Builder()
                        .dateTime(Arrays.asList(LOCAL_DATE_TIME, LOCAL_DATE_TIME)).build();
        String actual = CoreHelper.serialize(unixDateArray);
        assertEquals(actual, expected);
    }


    @Test
    public void testUnixTimeStampMap() throws IOException {
        String unixDateTime = LocalDateTimeHelper.toUnixTimestamp(LOCAL_DATE_TIME);
        String expected =
                "{\"dateTime\":{\"key\":" + unixDateTime + "},\"dateTime1\":{\"key\":"
                        + unixDateTime + "}}";
        Map<String, LocalDateTime> mapOfLocalDateTime = new HashMap<>();
        mapOfLocalDateTime.put("key", LOCAL_DATE_TIME);
        Map<String, LocalDateTime> mapOfLocalDateTime1 = new HashMap<>();
        mapOfLocalDateTime1.put("key", LOCAL_DATE_TIME);
        UnixDateMap unixDateMap =
                new UnixDateMap.Builder().dateTime(mapOfLocalDateTime)
                        .dateTime1(mapOfLocalDateTime1).build();
        String actual = CoreHelper.serialize(unixDateMap);
        assertEquals(actual, expected);
    }

    @Test
    public void testRfc1123Date() throws IOException {
        String expected = RFC1123_DATE;
        Rfc1123Date rfc1123Date = new Rfc1123Date.Builder().dateTime(LOCAL_DATE_TIME).build();
        String actual = CoreHelper.serialize(rfc1123Date);
        assertEquals(actual, expected);
    }

    @Test
    public void testRfc1123DateArray() throws IOException {
        String expected = RFC1123_DATE_ARRAY;
        Rfc1123DateArray rfc1123DateArray =
                new Rfc1123DateArray.Builder()
                        .dateTime(Arrays.asList(LOCAL_DATE_TIME, LOCAL_DATE_TIME)).build();
        String actual = CoreHelper.serialize(rfc1123DateArray);
        assertEquals(actual, expected);
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
        Rfc1123DateMap rfc1123DateMap =
                new Rfc1123DateMap.Builder().dateTime(mapOfLocalDateTime).build();
        String actual = CoreHelper.serialize(rfc1123DateMap);
        assertEquals(actual, expected);
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
        Rfc8601Date rfc8601Date = new Rfc8601Date.Builder().dateTime(LOCAL_DATE_TIME).build();
        String actual = CoreHelper.serialize(rfc8601Date);
        assertEquals(actual, expected);
    }

    @Test
    public void testRfc8601DateArray() throws IOException {
        String expected = RFC8601_DATE_ARRAY;
        Rfc8601DateArray rfc8601DateArray =
                new Rfc8601DateArray.Builder()
                        .dateTime(Arrays.asList(LOCAL_DATE_TIME, LOCAL_DATE_TIME)).build();
        String actual = CoreHelper.serialize(rfc8601DateArray);
        assertEquals(actual, expected);
    }

    @Test
    public void testRfc8601DateMap() throws IOException {
        String expected = RFC8601_DATE_MAP;
        Map<String, LocalDateTime> mapOfLocalDateTime = new HashMap<>();
        mapOfLocalDateTime.put("key", LOCAL_DATE_TIME);
        Rfc8601DateMap rfc8601DateMap =
                new Rfc8601DateMap.Builder().dateTime(mapOfLocalDateTime).build();
        String actual = CoreHelper.serialize(rfc8601DateMap);
        assertEquals(actual, expected);
    }

    @Test
    public void testRfc8601DateMapToString() throws IOException {
        String expected = RFC8601_DATE_MAP_TO_STRING;
        Map<String, LocalDateTime> mapOfLocalDateTime = new HashMap<>();
        mapOfLocalDateTime.put("key", LOCAL_DATE_TIME);
        Rfc8601DateMap rfc8601DateMap =
                new Rfc8601DateMap.Builder().dateTime(mapOfLocalDateTime).build();

        String actual = rfc8601DateMap.toString();
        assertEquals(actual, expected);
    }
}
