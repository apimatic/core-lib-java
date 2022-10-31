package io.apimatic.core.utilities;

import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This is a utility class for XML LocalDateTime operations.
 */
public class XmlLocalDateTimeHelper extends XMLDateHelper {

    /**
     * Serialize the RFC 8601 date time object
     * @param dateObj An instance of {@link LocalDateTime}
     * @param rootName The XML root name
     * @return A xml string
     */
    public static String serializeRfc8601DateTime(LocalDateTime dateObj, String rootName) {
        String xmlBlock = "<" + rootName + ">";
        xmlBlock += LocalDateTimeHelper.toRfc8601DateTime(dateObj);
        xmlBlock += "</" + rootName + ">";
        return xmlBlock;
    }

    /**
     * Deserialize the RFC 8601 datetime xml string
     * @param xml A xml string
     * @return {@link LocalDateTime}
     */
    public static LocalDateTime deserializeRfc8601DateTime(String xml) {
        Pattern pattern = Pattern.compile("<.+?>(.+?)</.+?>");
        Matcher patternMatcher = pattern.matcher(xml);

        if (!patternMatcher.find()) {
            throw new IllegalArgumentException("Invalid XML");
        }
        return LocalDateTimeHelper.fromRfc8601DateTime(patternMatcher.group(1));
    }

    /**
     * Serialize the RFC 1123 date time object
     * @param dateObj An instance of {@link LocalDateTime}
     * @param rootName The XML root name
     * @return A xml string
     */
    public static String serializeRfc1123DateTime(LocalDateTime dateObj, String rootName) {
        String xmlBlock = "<" + rootName + ">";
        xmlBlock += LocalDateTimeHelper.toRfc1123DateTime(dateObj);
        xmlBlock += "</" + rootName + ">";
        return xmlBlock;
    }

    /**
     * Deserialize the RFC 1123 datetime xml string
     * @param xml A xml string
     * @return {@link LocalDateTime}
     */
    public static LocalDateTime deserializeRfc1123DateTime(String xml) {
        Pattern pattern = Pattern.compile("<.+?>(.+?)</.+?>");
        Matcher patternMatcher = pattern.matcher(xml);

        if (!patternMatcher.find()) {
            throw new IllegalArgumentException("Invalid XML");
        }
        return LocalDateTimeHelper.fromRfc1123DateTime(patternMatcher.group(1));
    }

    /**
     * Serialize the Unix time stamp object
     * @param dateObj An instance of {@link LocalDateTime}
     * @param rootName The XML root name
     * @return A xml string
     */
    public static String serializeUnixTimeStamp(LocalDateTime dateObj, String rootName) {
        String xmlBlock = "<" + rootName + ">";
        xmlBlock += LocalDateTimeHelper.toUnixTimestamp(dateObj);
        xmlBlock += "</" + rootName + ">";
        return xmlBlock;
    }

    /**
     * Deserialize the unix time stamp xml string
     * @param xml A xml string
     * @return {@link LocalDateTime}
     */
    public static LocalDateTime deserializeUnixTimeStamp(String xml) {
        Pattern pattern = Pattern.compile("<.+?>(.+?)</.+?>");
        Matcher patternMatcher = pattern.matcher(xml);

        if (!patternMatcher.find()) {
            throw new IllegalArgumentException("Invalid XML");
        }
        return LocalDateTimeHelper.fromUnixTimestamp(patternMatcher.group(1));
    }
}
