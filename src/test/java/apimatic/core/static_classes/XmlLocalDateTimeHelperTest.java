package apimatic.core.static_classes;

import static org.junit.Assert.assertEquals;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import org.junit.Test;
import apimatic.core_lib.utilities.TestDateTimeHelper;
import io.apimatic.core.utilities.XmlLocalDateTimeHelper;

public class XmlLocalDateTimeHelperTest {
    @Test
    public void testSerializeRfc8601DateTime() {
        LocalDateTime localDateTime = LocalDateTime.of(2000, 7, 13, 6, 10);
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
        LocalDateTime expected = LocalDateTime.of(2000, 7, 13, 6, 10);
        LocalDateTime actual = XmlLocalDateTimeHelper.deserializeRfc8601DateTime(dateTime);

        assertEquals(actual, expected);
    }

    @Test
    public void testSerializeRfc1123DateTime() {
        LocalDateTime localDateTime = LocalDateTime.of(2000, 7, 13, 6, 10);
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
        LocalDateTime expected = LocalDateTime.of(2000, 7, 13, 6, 10);
        LocalDateTime actual = XmlLocalDateTimeHelper.deserializeRfc1123DateTime(dateTime);

        assertEquals(actual, expected);
    }

    @Test
    public void testSerializeUnixTimeStamp() {
        LocalDateTime localDateTime = TestDateTimeHelper.getLocalDateTimeFromGMT(
                ZonedDateTime.of(2000, 7, 13, 1, 10, 0, 0, ZoneId.of("GMT")));
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
        LocalDateTime expected = TestDateTimeHelper.getLocalDateTimeFromGMT(
                ZonedDateTime.of(2000, 7, 13, 1, 10, 0, 0, ZoneId.of("GMT")));
        LocalDateTime actual = XmlLocalDateTimeHelper.deserializeUnixTimeStamp(dateTime);

        assertEquals(actual, expected);
    }
}
