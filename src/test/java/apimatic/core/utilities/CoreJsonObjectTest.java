package apimatic.core.utilities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import java.io.IOException;
import org.junit.Test;
import io.apimatic.core.utilities.CoreJsonObject;

public class CoreJsonObjectTest {
    @Test
    public void testFromJsonString() throws IOException {
        String value = "{\"name\":\"test name\",\"field\":\"QA\"}";

        // stub
        String expected = value;
        CoreJsonObject jsonObject = CoreJsonObject.fromJsonString(value);
        String actual = jsonObject.toString();

        assertEquals(actual, expected);
    }

    @Test
    public void testFromJsonStringNull() throws IOException {
        String value = null;

        // stub
        CoreJsonObject jsonObject = CoreJsonObject.fromJsonString(value);
        Object actual = jsonObject.getStoredObject();

        assertNull(actual);
    }

    @Test(expected = IOException.class)
    public void testFromJsonStringInvalidObject() throws IOException {
        String value = "\"name\":\"\"";

        CoreJsonObject.fromJsonString(value);
    }

    @Test
    public void testGetStoredObject() throws IOException {
        String value =
                "{\"company name\" : \"APIMatic\",\"address\" : \"nust\",\"cell number"
                        + "\" : \"090078601\",\"first name\" : \"Adeel\",\"last name\" : \"Ali\","
                        + "\"address_boss\" : \"nust\"}";

        // stub
        String expected =
                "{company name=APIMatic, address=nust, cell number=090078601, "
                        + "first name=Adeel, last name=Ali, address_boss=nust}";

        CoreJsonObject jsonObject = CoreJsonObject.fromJsonString(value);
        String actual = String.valueOf(jsonObject.getStoredObject());
        assertEquals(actual, expected);
    }
}
