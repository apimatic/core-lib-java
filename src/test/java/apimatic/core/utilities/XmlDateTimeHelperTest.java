package apimatic.core.utilities;

import static org.junit.Assert.assertEquals;
import java.time.LocalDate;
import org.junit.Test;
import io.apimatic.core.utilities.XmlLocalDateTimeHelper;

public class XmlDateTimeHelperTest {
    private static final int DAY13 = 13;
    private static final int JULY = 7;
    private static final int YEAR2000 = 2000;

    @Test
    public void testSerializeSimpleDate() {
        LocalDate localDate = LocalDate.of(YEAR2000, JULY, DAY13);
        String rootName = "XmlRootName";

        // stub
        String expected = "<XmlRootName>2000-07-13</XmlRootName>";
        String actual = XmlLocalDateTimeHelper.serializeSimpleDate(localDate, rootName);

        assertEquals(actual, expected);
    }

    @Test
    public void testDeserializeUnixSimpleDate() {
        String dateTime = "<XmlRootName>2000-07-13</XmlRootName>";

        // stub
        LocalDate expected = LocalDate.of(YEAR2000, JULY, DAY13);
        LocalDate actual = XmlLocalDateTimeHelper.deserializeSimpleDate(dateTime);

        assertEquals(actual, expected);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeUnixSimpleDateInvalidXML() {
        String dateTime = "<XmlRootName 2000-07-13/>";
        XmlLocalDateTimeHelper.deserializeSimpleDate(dateTime);
    }
}
