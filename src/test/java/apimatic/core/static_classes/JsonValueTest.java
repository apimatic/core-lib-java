package apimatic.core.static_classes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import io.apimatic.core.utilities.JsonValue;

public class JsonValueTest {
    @Test
    public void testFromString() {
        String value = "TestString";
        
        // stub
        String expected = "\"TestString\"";
        JsonValue jsonValue = JsonValue.fromString(value);
        String actual = jsonValue.toString();
        
        assertEquals(actual, expected);
    }
    
    @Test
    public void testFromStringNull() {
        String value = null;
        
        // stub
        JsonValue jsonValue = JsonValue.fromString(value);
        Object actual = jsonValue.getStoredObject();
        
        assertNull(actual);
    }
    
    @Test
    public void testFromBoolean() {
        Boolean value = true;
        
        // stub
        String expected = "true";
        JsonValue jsonValue = JsonValue.fromBoolean(value);
        String actual = jsonValue.toString();
        
        assertEquals(actual, expected);
    }
    
    @Test
    public void testFromBooleanNull() {
        Boolean value = null;
        
        // stub
        JsonValue jsonValue = JsonValue.fromBoolean(value);
        Object actual = jsonValue.getStoredObject();
        
        assertNull(actual);
    }
    
    @Test
    public void testFromInteger() {
        Integer value = 21;
        
        // stub
        String expected = "21";
        JsonValue jsonValue = JsonValue.fromInteger(value);
        String actual = jsonValue.toString();
        
        assertEquals(actual, expected);
    }
    
    @Test
    public void testFromIntegerNull() {
        Integer value = null;
        
        // stub
        JsonValue jsonValue = JsonValue.fromInteger(value);
        Object actual = jsonValue.getStoredObject();
        
        assertNull(actual);
    }
    
    @Test
    public void testFromLong() {
        Long value = 21L;
        
        // stub
        String expected = String.format("%s", value);
        JsonValue jsonValue = JsonValue.fromLong(value);
        String actual = jsonValue.toString();
        
        assertEquals(actual, expected);
    }
    
    @Test
    public void testFromLongNull() {
        Long value = null;
        
        // stub
        JsonValue jsonValue = JsonValue.fromLong(value);
        Object actual = jsonValue.getStoredObject();
        
        assertNull(actual);
    }
    
    @Test
    public void testFromDouble() {
        Double value = 5.5;
        
        // stub
        String expected = "5.5";
        JsonValue jsonValue = JsonValue.fromDouble(value);
        String actual = jsonValue.toString();
        
        assertEquals(actual, expected);
    }
    
    @Test
    public void testFromDoubleNull() {
        Double value = null;
        
        // stub
        JsonValue jsonValue = JsonValue.fromDouble(value);
        Object actual = jsonValue.getStoredObject();
        
        assertNull(actual);
    }
    
    @Test
    public void testFromObject() {
        Object value = (Object) 21L;
        
        // stub
        String expected = "21";
        JsonValue jsonValue = JsonValue.fromObject(value);
        String actual = jsonValue.toString();
        
        assertEquals(actual, expected);
    }
    
    @Test
    public void testFromObjectNull() {
        Object value = null;
        
        // stub
        JsonValue jsonValue = JsonValue.fromObject(value);
        Object actual = jsonValue.getStoredObject();
        
        assertNull(actual);
    }
    
    @Test
    public void testFromArray() {
        List<Integer> value = new ArrayList<Integer>();
        value.add(1);
        value.add(2);
        value.add(3);
        
        // stub
        String expected = "[1,2,3]";
        JsonValue jsonValue = JsonValue.fromArray(value);
        String actual = jsonValue.toString();
        
        assertEquals(actual, expected);
    }
    
    @Test
    public void testFromArraytNull() {
        List<Integer> value = null;
        
        // stub
        JsonValue jsonValue = JsonValue.fromArray(value);
        Object actual = jsonValue.getStoredObject();
        
        assertNull(actual);
    }
    
    @Test
    public void testGetStoredObject() {
        Integer value = 21;
        
        // stub
        JsonValue jsonValue = JsonValue.fromInteger(value);
        Integer actual = (Integer) jsonValue.getStoredObject();
        
        assertEquals(actual, value);
    }
}
