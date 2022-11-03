package apimatic.core.utilities;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public final class TestDateTimeHelper {

    private TestDateTimeHelper() {}

    /**
     * Utility method to convert the ZonedDateTime to LocalDataTime from GMT zone.
     * @param zonedDateTime.
     * @return The instance of {@link LocalDateTime}}.
     */
    public static LocalDateTime getLocalDateTimeFromGMT(ZonedDateTime zonedDateTime) {
        return ZonedDateTime.ofInstant(zonedDateTime.toInstant(), ZoneId.systemDefault())
                .toLocalDateTime();
    }
}
