package apimatic.core.utilities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import io.apimatic.core.utilities.CoreJsonValue;

public class CoreJsonValueTest {
    private static final long TEST_LONG_NUMBER = 21L;
    private static final double TEST_DOUBLE = 5.5;
    private static final int TEST_INTEGER = 21;

    @Test
    public void testFromString() {
        String value = "TestString";

        // stub
        String expected = "\"TestString\"";
        CoreJsonValue jsonValue = CoreJsonValue.fromString(value);
        String actual = jsonValue.toString();

        assertEquals(actual, expected);
    }

    @Test
    public void testFromStringNull() {
        String value = null;

        // stub
        CoreJsonValue jsonValue = CoreJsonValue.fromString(value);
        Object actual = jsonValue.getStoredObject();

        assertNull(actual);
    }

    @Test
    public void testFromBoolean() {
        Boolean value = true;

        // stub
        String expected = "true";
        CoreJsonValue jsonValue = CoreJsonValue.fromBoolean(value);
        String actual = jsonValue.toString();

        assertEquals(actual, expected);
    }

    @Test
    public void testFromBooleanNull() {
        Boolean value = null;

        // stub
        CoreJsonValue jsonValue = CoreJsonValue.fromBoolean(value);
        Object actual = jsonValue.getStoredObject();

        assertNull(actual);
    }

    @Test
    public void testFromInteger() {
        Integer value = TEST_INTEGER;

        // stub
        String expected = "21";
        CoreJsonValue jsonValue = CoreJsonValue.fromInteger(value);
        String actual = jsonValue.toString();

        assertEquals(actual, expected);
    }

    @Test
    public void testFromIntegerNull() {
        Integer value = null;

        // stub
        CoreJsonValue jsonValue = CoreJsonValue.fromInteger(value);
        Object actual = jsonValue.getStoredObject();

        assertNull(actual);
    }

    @Test
    public void testFromLong() {
        Long value = TEST_LONG_NUMBER;

        // stub
        String expected = String.format("%s", value);
        CoreJsonValue jsonValue = CoreJsonValue.fromLong(value);
        String actual = jsonValue.toString();

        assertEquals(actual, expected);
    }

    @Test
    public void testFromLongNull() {
        Long value = null;

        // stub
        CoreJsonValue jsonValue = CoreJsonValue.fromLong(value);
        Object actual = jsonValue.getStoredObject();

        assertNull(actual);
    }

    @Test
    public void testFromDouble() {
        Double value = TEST_DOUBLE;

        // stub
        String expected = "5.5";
        CoreJsonValue jsonValue = CoreJsonValue.fromDouble(value);
        String actual = jsonValue.toString();

        assertEquals(actual, expected);
    }

    @Test
    public void testFromDoubleNull() {
        Double value = null;

        // stub
        CoreJsonValue jsonValue = CoreJsonValue.fromDouble(value);
        Object actual = jsonValue.getStoredObject();

        assertNull(actual);
    }

    @Test
    public void testFromObject() {
        Object value = TEST_LONG_NUMBER;

        // stub
        String expected = "21";
        CoreJsonValue jsonValue = CoreJsonValue.fromObject(value);
        String actual = jsonValue.toString();

        assertEquals(actual, expected);
    }

    @Test
    public void testFromObjectNull() {
        Object value = null;

        // stub
        CoreJsonValue jsonValue = CoreJsonValue.fromObject(value);
        Object actual = jsonValue.getStoredObject();

        assertNull(actual);
    }

    @Test
    public void testFromArray() {
        List<Integer> value = new ArrayList<Integer>();
        value.add(1);
        value.add(2);

        // stub
        String expected = "[1,2]";
        CoreJsonValue jsonValue = CoreJsonValue.fromArray(value);
        String actual = jsonValue.toString();

        assertEquals(actual, expected);
    }

    @Test
    public void testFromArraytNull() {
        List<Integer> value = null;

        // stub
        CoreJsonValue jsonValue = CoreJsonValue.fromArray(value);
        Object actual = jsonValue.getStoredObject();

        assertNull(actual);
    }

    @Test
    public void testGetStoredObject() {
        Integer value = TEST_INTEGER;

        // stub
        CoreJsonValue jsonValue = CoreJsonValue.fromInteger(value);
        Integer actual = (Integer) jsonValue.getStoredObject();

        assertEquals(actual, value);
    }
}
