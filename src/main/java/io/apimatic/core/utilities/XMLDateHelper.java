package io.apimatic.core.utilities;

import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XMLDateHelper {

    public static String serializeSimpleDate(LocalDate dateObj, String rootName) {
        String xmlBlock = "<" + rootName + ">";
        xmlBlock +=  dateObj.toString();
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
