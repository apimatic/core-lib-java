package apimatic.core.utilities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import java.time.LocalDate;
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
import io.apimatic.core.utilities.DateHelper;

public class DateHelperTest {

    private static final int DAY1 = 1;
    private static final int DAY13 = 13;
    private static final int JULY = 7;
    private static final int YEAR1997 = 1997;
    private static final int AUGUST = 8;
    private static final int YEAR2021 = 2021;
    private static final int DAY2 = 2;
    private static final int SEPTEMBER = 9;
    private static final int YEAR2022 = 2022;

    @Test
    public void testDateToSimpleDate() {
        LocalDate date = LocalDate.of(YEAR2022, SEPTEMBER, DAY2);

        // stub
        String expected = "2022-09-02";
        String actual = DateHelper.toSimpleDate(date);
        assertEquals(actual, expected);
    }

    @Test
    public void testDateNullToSimpleDate() {
        LocalDate date = null;
        assertNull(DateHelper.toSimpleDate(date));
    }

    @Test
    public void testListDateToSimpleDate() {
        LocalDate date1 = LocalDate.of(YEAR2022, SEPTEMBER, DAY2);
        LocalDate date2 = LocalDate.of(YEAR2021, AUGUST, DAY1);

        List<LocalDate> dateArray = Arrays.asList(date1, date2);
        List<String> expected = Arrays.asList("2022-09-02", "2021-08-01");
        List<String> actual = DateHelper.toSimpleDate(dateArray);
        assertEquals(actual, expected);
    }


    @Test
    public void testListDateNullToSimpleDate() {
        List<LocalDate> dateArray = null;
        assertNull(DateHelper.toSimpleDate(dateArray));
    }

    @Test
    public void testMapDateToSimpleDate() {
        LocalDate date1 = LocalDate.of(YEAR2022, SEPTEMBER, DAY2);
        LocalDate date2 = LocalDate.of(YEAR2021, AUGUST, DAY1);
        Map<String, LocalDate> dateMap = new HashMap<>();
        dateMap.put("date1", date1);
        dateMap.put("date2", date2);

        Map<String, String> expected = new HashMap<>();
        expected.put("date1", "2022-09-02");
        expected.put("date2", "2021-08-01");

        Map<String, String> actual = DateHelper.toSimpleDate(dateMap);
        assertEquals(actual, expected);
    }

    @Test
    public void testMapDateNullToSimpleDate() {
        Map<String, LocalDate> dateMap = null;
        assertNull(DateHelper.toSimpleDate(dateMap));
    }

    @Test
    public void testListOfMapDateToSimpleDate() {
        LocalDate date1 = LocalDate.of(YEAR2022, SEPTEMBER, DAY2);
        LocalDate date2 = LocalDate.of(YEAR2021, AUGUST, DAY1);

        Map<String, LocalDate> dateMap = new HashMap<>();
        dateMap.put("date1", date1);
        dateMap.put("date2", date2);

        List<Map<String, LocalDate>> listOfMapOfDate = Arrays.asList(dateMap);
        Map<String, String> mapOfDateString = new HashMap<>();
        mapOfDateString.put("date1", "2022-09-02");
        mapOfDateString.put("date2", "2021-08-01");

        List<Map<String, String>> expected = Arrays.asList(mapOfDateString);
        List<Map<String, String>> actual = DateHelper.toArrayOfMapOfSimpleDate(listOfMapOfDate);
        assertEquals(actual, expected);
    }

    @Test
    public void testListOfMapDateNullToSimpleDate() {
        List<Map<String, LocalDate>> listOfMapOfDate = null;
        assertNull(DateHelper.toArrayOfMapOfSimpleDate(listOfMapOfDate));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testUnixTimeStampSerializer() throws JsonProcessingException {
        LocalDate localDate = LocalDate.of(YEAR1997, JULY, DAY13);
        @SuppressWarnings("rawtypes")
        JsonSerializer serializer = new DateHelper.SimpleDateSerializer();
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(LocalDate.class, serializer);
        mapper.registerModule(module);

        String expected = "\"1997-07-13\"";
        String actual = mapper.writeValueAsString(localDate);
        assertEquals(actual, expected);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testSimpleDateDeserializer() throws JsonProcessingException {
        @SuppressWarnings("rawtypes")
        JsonDeserializer deserializer = new DateHelper.SimpleDateDeserializer();
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(LocalDate.class, deserializer);
        mapper.registerModule(module);

        String date = "\"1997-07-13\"";
        LocalDate expected = LocalDate.of(YEAR1997, JULY, DAY13);
        LocalDate actual = mapper.readValue(date, LocalDate.class);

        assertEquals(actual, expected);
    }
}
