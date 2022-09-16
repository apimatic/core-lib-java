package io.apimatic.core_lib.utilities;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XmlZonedDateTimeHelper {
    public static String serializeRfc8601DateTime(ZonedDateTime dateObj, String rootName) {
        String xmlBlock = "<" + rootName + ">";
        xmlBlock += ZonedDateTimeHelper.toRfc8601DateTime(dateObj);
        xmlBlock += "</" + rootName + ">";
        return xmlBlock;
    }

    public static ZonedDateTime deserializeRfc8601DateTime(String xml) {
        Pattern pattern = Pattern.compile("<.+?>(.+?)</.+?>");
        Matcher patternMatcher = pattern.matcher(xml);

        if (!patternMatcher.find()) {
            throw new IllegalArgumentException("Invalid XML");
        }
        return ZonedDateTimeHelper.fromRfc8601DateTime(patternMatcher.group(1));
    }

    public static String serializeRfc1123DateTime(ZonedDateTime dateObj, String rootName) {
        String xmlBlock = "<" + rootName + ">";
        xmlBlock += ZonedDateTimeHelper.toRfc1123DateTime(dateObj);
        xmlBlock += "</" + rootName + ">";
        return xmlBlock;
    }

    public static ZonedDateTime deserializeRfc1123DateTime(String xml) {
        Pattern pattern = Pattern.compile("<.+?>(.+?)</.+?>");
        Matcher patternMatcher = pattern.matcher(xml);

        if (!patternMatcher.find()) {
            throw new IllegalArgumentException("Invalid XML");
        }
        return ZonedDateTimeHelper.fromRfc1123DateTime(patternMatcher.group(1));
    }

    public static String serializeUnixTimeStamp(ZonedDateTime dateObj, String rootName) {
        String xmlBlock = "<" + rootName + ">";
        xmlBlock += ZonedDateTimeHelper.toUnixTimestamp(dateObj);
        xmlBlock += "</" + rootName + ">";
        return xmlBlock;
    }

    public static ZonedDateTime deserializeUnixTimeStamp(String xml) {
        Pattern pattern = Pattern.compile("<.+?>(.+?)</.+?>");
        Matcher patternMatcher = pattern.matcher(xml);

        if (!patternMatcher.find()) {
            throw new IllegalArgumentException("Invalid XML");
        }
        return ZonedDateTimeHelper.fromUnixTimestamp(patternMatcher.group(1));
    }

    public static String serializeSimpleDate(ZonedDateTime dateObj, String rootName) {
        String xmlBlock = "<" + rootName + ">";
        xmlBlock += dateObj.toString();
        xmlBlock += "</" + rootName + ">";
        return xmlBlock;
    }

    public static LocalDate deserializeSimpleDate(String xml) {
        Pattern pattern = Pattern.compile("<.+?>(.+?)</.+?>");
        Matcher patternMatcher = pattern.matcher(xml);

        if (!patternMatcher.find()) {
            throw new IllegalArgumentException("Invalid XML");
        }
        return LocalDate.parse(patternMatcher.group(1));
    }
}
