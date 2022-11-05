package apimatic.core.utilities;

import static org.junit.Assert.assertEquals;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import org.junit.Test;
import apimatic.core.mocks.TestDateTimeHelper;
import io.apimatic.core.utilities.XmlLocalDateTimeHelper;

public class XmlLocalDateTimeHelperTest {

    private static final int MINUTES10 = 10;
    private static final int HOUR6 = 6;
    private static final int DAY13 = 13;
    private static final int JULY = 7;
    private static final int YEAR2000 = 2000;

    @Test
    public void testSerializeRfc8601DateTime() {
        LocalDateTime localDateTime = LocalDateTime.of(YEAR2000, JULY, DAY13, HOUR6, MINUTES10);
        String rootName = "XmlRootName";

        // stub
        String expected = "<XmlRootName>2000-07-13T06:10Z</XmlRootName>";
        String actual = XmlLocalDateTimeHelper.serializeRfc8601DateTime(localDateTime, rootName);

        assertEquals(actual, expected);
    }

    @Test
    public void testDeserializeRfc8601DateTime() {
        String dateTime = "<XmlRootName>2000-07-13T06:10Z</XmlRootName>";

        // stub
        LocalDateTime expected = LocalDateTime.of(YEAR2000, JULY, DAY13, HOUR6, MINUTES10);
        LocalDateTime actual = XmlLocalDateTimeHelper.deserializeRfc8601DateTime(dateTime);

        assertEquals(actual, expected);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeRfc8601DateTimeInvalidXml() {
        String dateTime = "<XmlRootName2000-07-13T06:10ZXmlRootName>";
        XmlLocalDateTimeHelper.deserializeRfc8601DateTime(dateTime);
    }

    @Test
    public void testSerializeRfc1123DateTime() {
        LocalDateTime localDateTime = LocalDateTime.of(YEAR2000, JULY, DAY13, HOUR6, MINUTES10);
        String rootName = "XmlRootName";

        // stub
        String expected = "<XmlRootName>Thu, 13 Jul 2000 06:10:00 GMT</XmlRootName>";
        String actual = XmlLocalDateTimeHelper.serializeRfc1123DateTime(localDateTime, rootName);

        assertEquals(actual, expected);
    }

    @Test
    public void testDeserializeRfc1123DateTime() {
        String dateTime = "<XmlRootName>Thu, 13 Jul 2000 06:10:00 GMT</XmlRootName>";

        // stub
        LocalDateTime expected = LocalDateTime.of(YEAR2000, JULY, DAY13, HOUR6, MINUTES10);
        LocalDateTime actual = XmlLocalDateTimeHelper.deserializeRfc1123DateTime(dateTime);

        assertEquals(actual, expected);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeRfc1123DateTimeInvalidXml() {
        String dateTime = "<Thu, 13 Jul 2000 06:10:00 GMTXmlRootName>";
        XmlLocalDateTimeHelper.deserializeRfc1123DateTime(dateTime);
    }

    @Test
    public void testSerializeUnixTimeStamp() {
        LocalDateTime localDateTime =
                TestDateTimeHelper.getLocalDateTimeFromGMT(ZonedDateTime.of(YEAR2000, JULY, DAY13,
                        1, MINUTES10, 0, 0, ZoneId.of("GMT")));
        String rootName = "XmlRootName";

        // stub
        String expected = "<XmlRootName>963450600</XmlRootName>";
        String actual = XmlLocalDateTimeHelper.serializeUnixTimeStamp(localDateTime, rootName);

        assertEquals(actual, expected);
    }

    @Test
    public void testDeserializeUnixTimeStampe() {
        String dateTime = "<XmlRootName>963450600</XmlRootName>";

        // stub
        LocalDateTime expected =
                TestDateTimeHelper.getLocalDateTimeFromGMT(ZonedDateTime.of(YEAR2000, JULY, DAY13,
                        1, MINUTES10, 0, 0, ZoneId.of("GMT")));
        LocalDateTime actual = XmlLocalDateTimeHelper.deserializeUnixTimeStamp(dateTime);

        assertEquals(actual, expected);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeUnixTimeStampeInvalidXml() {
        String dateTime = "</XmlRootName>";
        XmlLocalDateTimeHelper.deserializeUnixTimeStamp(dateTime);
    }
}
