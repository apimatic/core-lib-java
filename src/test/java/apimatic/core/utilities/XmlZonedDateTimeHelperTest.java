package apimatic.core.utilities;

import static org.junit.Assert.assertEquals;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import org.junit.Test;
import io.apimatic.core.utilities.XmlZonedDateTimeHelper;

public class XmlZonedDateTimeHelperTest {
    private static final int MINUTES10 = 10;
    private static final int HOUR6 = 6;
    private static final int DAY13 = 13;
    private static final int JULY = 7;
    private static final int YEAR1997 = 1997;

    @Test
    public void testSerializeRfc8601DateTime() {
        ZonedDateTime zonedDateTime =
                ZonedDateTime.of(YEAR1997, JULY, DAY13, HOUR6, MINUTES10, 0, 0, ZoneId.of("GMT"));
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
        ZonedDateTime expected =
                ZonedDateTime.of(YEAR1997, JULY, DAY13, HOUR6, MINUTES10, 0, 0, ZoneId.of("GMT"));
        ZonedDateTime actual = XmlZonedDateTimeHelper.deserializeRfc8601DateTime(zonedDateTime);

        assertEquals(actual, expected);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeRfc8601DateTimeInvalidXml() {
        String zonedDateTime = "<XmlRootName1997-07-13T06:10Z[GMT]XmlRootName>";
        XmlZonedDateTimeHelper.deserializeRfc8601DateTime(zonedDateTime);
    }

    @Test
    public void testSerializeRfc1123DateTime() {
        ZonedDateTime zonedDateTime =
                ZonedDateTime.of(YEAR1997, JULY, DAY13, HOUR6, MINUTES10, 0, 0, ZoneId.of("GMT"));
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
        ZonedDateTime expected =
                ZonedDateTime.of(YEAR1997, JULY, DAY13, HOUR6, MINUTES10, 0, 0, ZoneId.of("GMT"));
        ZonedDateTime actual = XmlZonedDateTimeHelper.deserializeRfc1123DateTime(zonedDateTime);

        assertEquals(actual, expected);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeRfc1123DateTimeInvalidXml() {
        String zonedDateTime = "<XmlRootNameSun, 13 Jul 1997 06:10:00 GMTXmlRootName>";
        XmlZonedDateTimeHelper.deserializeRfc1123DateTime(zonedDateTime);
    }

    @Test
    public void testSerializeUnixTimeStamp() {
        ZonedDateTime zonedDateTime =
                ZonedDateTime.of(YEAR1997, JULY, DAY13, HOUR6, MINUTES10, 0, 0, ZoneId.of("GMT"));
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
        ZonedDateTime expected =
                ZonedDateTime.of(YEAR1997, JULY, DAY13, HOUR6, MINUTES10, 0, 0, ZoneId.of("GMT"));
        ZonedDateTime actual = XmlZonedDateTimeHelper.deserializeUnixTimeStamp(zonedDateTime);

        ZonedDateTime actualGMT = ZonedDateTime.ofInstant(actual.toInstant(), ZoneId.of("GMT"));
        assertEquals(actualGMT, expected);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeUnixTimeStampInvalidXml() {
        String zonedDateTime = "<868774200/XmlRootName>";
        XmlZonedDateTimeHelper.deserializeUnixTimeStamp(zonedDateTime);
    }
}
