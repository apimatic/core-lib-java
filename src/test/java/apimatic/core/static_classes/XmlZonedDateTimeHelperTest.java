package apimatic.core.static_classes;

import static org.junit.Assert.assertEquals;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import org.junit.Test;
import io.apimatic.core.utilities.XmlZonedDateTimeHelper;

public class XmlZonedDateTimeHelperTest {
    @Test
    public void testSerializeRfc8601DateTime() {
        ZonedDateTime zonedDateTime = ZonedDateTime.of(1997, 7, 13, 6, 10, 0, 0, ZoneId.of("GMT"));
        String rootName = "XmlRootName";

        // stub
        String expected = "<XmlRootName>1997-07-13T06:10Z[GMT]</XmlRootName>";
        String actual = XmlZonedDateTimeHelper.serializeRfc8601DateTime(zonedDateTime, rootName);

        assertEquals(actual, expected);
    }

    @Test
    public void testDeserializeRfc8601DateTime() {
        String zonedDateTime = "<XmlRootName>1997-07-13T06:10Z[GMT]</XmlRootName>";

        // stub
        ZonedDateTime expected = ZonedDateTime.of(1997, 7, 13, 6, 10, 0, 0, ZoneId.of("GMT"));
        ZonedDateTime actual = XmlZonedDateTimeHelper.deserializeRfc8601DateTime(zonedDateTime);

        assertEquals(actual, expected);
    }

    @Test
    public void testSerializeRfc1123DateTime() {
        ZonedDateTime zonedDateTime = ZonedDateTime.of(1997, 7, 13, 6, 10, 0, 0, ZoneId.of("GMT"));
        String rootName = "XmlRootName";

        // stub
        String expected = "<XmlRootName>Sun, 13 Jul 1997 06:10:00 GMT</XmlRootName>";
        String actual = XmlZonedDateTimeHelper.serializeRfc1123DateTime(zonedDateTime, rootName);

        assertEquals(actual, expected);
    }

    @Test
    public void testDeserializeRfc1123DateTime() {
        String zonedDateTime = "<XmlRootName>Sun, 13 Jul 1997 06:10:00 GMT</XmlRootName>";

        // stub
        ZonedDateTime expected = ZonedDateTime.of(1997, 7, 13, 6, 10, 0, 0, ZoneId.of("GMT"));
        ZonedDateTime actual = XmlZonedDateTimeHelper.deserializeRfc1123DateTime(zonedDateTime);

        assertEquals(actual, expected);
    }

    @Test
    public void testSerializeUnixTimeStamp() {
        ZonedDateTime zonedDateTime = ZonedDateTime.of(1997, 7, 13, 6, 10, 0, 0, ZoneId.of("GMT"));
        String rootName = "XmlRootName";

        // stub
        String expected = "<XmlRootName>868774200</XmlRootName>";
        String actual = XmlZonedDateTimeHelper.serializeUnixTimeStamp(zonedDateTime, rootName);

        assertEquals(actual, expected);
    }

    @Test
    public void testDeserializeUnixTimeStamp() {
        String zonedDateTime = "<XmlRootName>868774200</XmlRootName>";

        // stub
        ZonedDateTime expected = ZonedDateTime.of(1997, 7, 13, 6, 10, 0, 0, ZoneId.of("GMT"));
        ZonedDateTime actual = XmlZonedDateTimeHelper.deserializeUnixTimeStamp(zonedDateTime);

        ZonedDateTime actualGMT = ZonedDateTime.ofInstant(actual.toInstant(), ZoneId.of("GMT"));

        assertEquals(actualGMT, expected);
    }
}
