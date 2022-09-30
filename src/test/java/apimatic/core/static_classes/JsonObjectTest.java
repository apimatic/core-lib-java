package apimatic.core.static_classes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import java.io.IOException;
import org.junit.Test;
import io.apimatic.core.utilities.JsonObject;

public class JsonObjectTest {
    @Test
    public void testFromJsonString() throws IOException {
        String value = "{\"name\":\"test name\",\"field\":\"QA\"}";

        // stub
        String expected = value;
        JsonObject jsonObject = JsonObject.fromJsonString(value);
        String actual = jsonObject.toString();
        
        assertEquals(actual, expected);
    }

    @Test
    public void testFromJsonStringNull() throws IOException {
        String value = null;
        
        // stub
        JsonObject jsonObject = JsonObject.fromJsonString(value);
        Object actual = jsonObject.getStoredObject();
        
        assertNull(actual);
    }
    
    @Test
    public void testGetStoredObject() throws IOException {
        String value = "{\"company name\" : \"APIMatic\",\"address\" : \"nust\",\"cell number"
                + "\" : \"090078601\",\"first name\" : \"Adeel\",\"last name\" : \"Ali\","
                + "\"address_boss\" : \"nust\"}";
        
        // stub
        String expected = "{company name=APIMatic, address=nust, cell number=090078601, first name=Adeel, last name=Ali, address_boss=nust}";
        JsonObject jsonObject = JsonObject.fromJsonString(value);
        String actual = String.valueOf(jsonObject.getStoredObject());
        
        assertEquals(actual, expected);
    }
}
