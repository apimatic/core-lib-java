package apimatic.core.utilities;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class TestDateTimeHelper {
    public static LocalDateTime getLocalDateTimeFromGMT(ZonedDateTime zonedDateTime) {
        return ZonedDateTime.ofInstant(zonedDateTime.toInstant(), ZoneId.systemDefault())
                .toLocalDateTime();
    }
}
