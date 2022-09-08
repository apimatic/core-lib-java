package apimatic.core_lib.static_classes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.apimatic.core_lib.utilities.DateHelper;

public class DateHelperTest {

    @Test
    public void testDateToSimpleDate() {
        LocalDate date = LocalDate.of(2022, 9, 2);

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
        LocalDate date1 = LocalDate.of(2022, 9, 2);
        LocalDate date2 = LocalDate.of(2021, 8, 1);

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
        LocalDate date1 = LocalDate.of(2022, 9, 2);
        LocalDate date2 = LocalDate.of(2021, 8, 1);

        Map<String, LocalDate> dateMap = Map.of("date1", date1, "date2", date2);
        Map<String, String> expected = Map.of("date1", "2022-09-02", "date2", "2021-08-01");
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
        LocalDate date1 = LocalDate.of(2022, 9, 2);
        LocalDate date2 = LocalDate.of(2021, 8, 1);

        Map<String, LocalDate> dateMap = Map.of("date1", date1, "date2", date2);

        List<Map<String, LocalDate>> listOfMapOfDate = Arrays.asList(dateMap);
        List<Map<String, String>> expected =
                Arrays.asList(Map.of("date1", "2022-09-02", "date2", "2021-08-01"));
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
        LocalDate localDate = LocalDate.of(1997, 7, 13);
        JsonSerializer serializer = new DateHelper.SimpleDateSerializer();
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(LocalDate.class, serializer);
        mapper.registerModule(module);

        String expected = "\"1997-07-13\"";

        String actual = mapper.writeValueAsString(localDate);;

        assertEquals(actual, expected);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testSimpleDateDeserializer() throws JsonProcessingException {
        JsonDeserializer deserializer = new DateHelper.SimpleDateDeserializer();
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(LocalDate.class, deserializer);
        mapper.registerModule(module);

        String date = "\"1997-07-13\"";
        LocalDate expected = LocalDate.of(1997, 7, 13);
        LocalDate actual = mapper.readValue(date, LocalDate.class);

        assertEquals(actual, expected);
    }
}
