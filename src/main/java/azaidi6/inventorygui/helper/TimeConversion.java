package azaidi6.inventorygui.helper;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

/**
 * This class handles converting to/from UTC and to ET for timestamp data.
 * The database stores timestamp data in UTC, which is converted to system
 * default timestamp for the user.
 */
public abstract class TimeConversion {

    private static final ZoneId zoneId = ZoneId.systemDefault();

    /**
     *
     * @return gets the zone ID of the person using the application for timestamp conversion.
     */
    public static ZoneId getZoneId() {
        return zoneId;
    }

    /**
     *
     * @param time the LocalDateTime data to convert into UTC time.
     * @return returns UTC LocalDateTime of time inputted.
     */
    public static LocalDateTime convertToUTC(LocalDateTime time) {
        return ZonedDateTime.of(time, zoneId).withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
    }

    /**
     *
     * @param time the LocalDateTime data to convert from UTC time.
     * @return returns time in the user's system default timezone.
     */
    public static LocalDateTime convertFromUTC(LocalDateTime time) {
        return ZonedDateTime.of(time, ZoneOffset.UTC).toOffsetDateTime().atZoneSameInstant(zoneId).toLocalDateTime();
    }

    /**
     *
     * @param time the LocalDateTime data to convert to ET time.
     * @return returns ET LocalDateTime data of time inputted.
     */
    public static LocalDateTime convertToET(LocalDateTime time) {
        return ZonedDateTime.of(time, zoneId).withZoneSameInstant(ZoneId.of("America/New_York")).toLocalDateTime();
    }

}
