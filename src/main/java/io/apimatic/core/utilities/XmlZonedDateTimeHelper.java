package io.apimatic.core.utilities;

import java.time.ZonedDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This is a utility class for XML ZonedDateTime operations.
 */
public class XmlZonedDateTimeHelper extends XMLDateHelper {
    /**
     * Serialize the RFC 8601 zoned date time object
     * @param dateObj An instance of {@link ZonedDateTime}
     * @param rootName The XML root name
     * @return A XML string
     */
    public static String serializeRfc8601DateTime(ZonedDateTime dateObj, String rootName) {
        String xmlBlock = "<" + rootName + ">";
        xmlBlock += ZonedDateTimeHelper.toRfc8601DateTime(dateObj);
        xmlBlock += "</" + rootName + ">";
        return xmlBlock;
    }

    /**
     * Deserialize the RFC 8601 zoned date time string
     * @param xml A XML string
     * @return {@link ZonedDateTime}
     */
    public static ZonedDateTime deserializeRfc8601DateTime(String xml) {
        Pattern pattern = Pattern.compile("<.+?>(.+?)</.+?>");
        Matcher patternMatcher = pattern.matcher(xml);

        if (!patternMatcher.find()) {
            throw new IllegalArgumentException("Invalid XML");
        }
        return ZonedDateTimeHelper.fromRfc8601DateTime(patternMatcher.group(1));
    }

    /**
     * Serialize the RFC 1123 zoned date time object
     * @param dateObj An instance of {@link ZonedDateTime}
     * @param rootName The XML root name
     * @return A XML string
     */
    public static String serializeRfc1123DateTime(ZonedDateTime dateObj, String rootName) {
        String xmlBlock = "<" + rootName + ">";
        xmlBlock += ZonedDateTimeHelper.toRfc1123DateTime(dateObj);
        xmlBlock += "</" + rootName + ">";
        return xmlBlock;
    }

    /**
     * Deserialize the RFC 1123 zoned date time string
     * @param xml A XML string
     * @return {@link ZonedDateTime}
     */
    public static ZonedDateTime deserializeRfc1123DateTime(String xml) {
        Pattern pattern = Pattern.compile("<.+?>(.+?)</.+?>");
        Matcher patternMatcher = pattern.matcher(xml);

        if (!patternMatcher.find()) {
            throw new IllegalArgumentException("Invalid XML");
        }
        return ZonedDateTimeHelper.fromRfc1123DateTime(patternMatcher.group(1));
    }

    /**
     * Serialize the Unixtime stamp zoned date time object
     * @param dateObj An instance of {@link ZonedDateTime}
     * @param rootName The XML root name
     * @return A XML string
     */
    public static String serializeUnixTimeStamp(ZonedDateTime dateObj, String rootName) {
        String xmlBlock = "<" + rootName + ">";
        xmlBlock += ZonedDateTimeHelper.toUnixTimestamp(dateObj);
        xmlBlock += "</" + rootName + ">";
        return xmlBlock;
    }

    /**
     * Deserialize the unix time stamp zoned date time string
     * @param xml A XML string
     * @return {@link ZonedDateTime}
     */
    public static ZonedDateTime deserializeUnixTimeStamp(String xml) {
        Pattern pattern = Pattern.compile("<.+?>(.+?)</.+?>");
        Matcher patternMatcher = pattern.matcher(xml);

        if (!patternMatcher.find()) {
            throw new IllegalArgumentException("Invalid XML");
        }
        return ZonedDateTimeHelper.fromUnixTimestamp(patternMatcher.group(1));
    }
}
