package io.apimatic.core.utilities;

import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XmlLocalDateTimeHelper extends XMLDateHelper {
    
    public static String serializeRfc8601DateTime(LocalDateTime dateObj, String rootName) {
        String xmlBlock = "<" + rootName + ">";
        xmlBlock += LocalDateTimeHelper.toRfc8601DateTime(dateObj);
        xmlBlock += "</" + rootName + ">";
        return xmlBlock;
    }
    
    public static LocalDateTime deserializeRfc8601DateTime(String xml) {
        Pattern pattern = Pattern.compile("<.+?>(.+?)</.+?>");
        Matcher patternMatcher = pattern.matcher(xml);
            
        if (!patternMatcher.find()) {
            throw new IllegalArgumentException("Invalid XML");
        }
        return LocalDateTimeHelper.fromRfc8601DateTime(patternMatcher.group(1));
    }

    public static String serializeRfc1123DateTime(LocalDateTime dateObj, String rootName) {
        String xmlBlock = "<" + rootName + ">";
        xmlBlock += LocalDateTimeHelper.toRfc1123DateTime(dateObj);
        xmlBlock += "</" + rootName + ">";
        return xmlBlock;
    }
    
    public static LocalDateTime deserializeRfc1123DateTime(String xml) {
        Pattern pattern = Pattern.compile("<.+?>(.+?)</.+?>");
        Matcher patternMatcher = pattern.matcher(xml);
        
        if (!patternMatcher.find()) {
            throw new IllegalArgumentException("Invalid XML");
        }
        return LocalDateTimeHelper.fromRfc1123DateTime(patternMatcher.group(1));
    }

    public static String serializeUnixTimeStamp(LocalDateTime dateObj, String rootName) {
        String xmlBlock = "<" + rootName + ">";
        xmlBlock += LocalDateTimeHelper.toUnixTimestamp(dateObj);
        xmlBlock += "</" + rootName + ">";
        return xmlBlock;
    }
    
    public static LocalDateTime deserializeUnixTimeStamp(String xml) {
        Pattern pattern = Pattern.compile("<.+?>(.+?)</.+?>");
        Matcher patternMatcher = pattern.matcher(xml);
        
        if (!patternMatcher.find()) {
            throw new IllegalArgumentException("Invalid XML");
        }
        return LocalDateTimeHelper.fromUnixTimestamp(patternMatcher.group(1));
    }
}