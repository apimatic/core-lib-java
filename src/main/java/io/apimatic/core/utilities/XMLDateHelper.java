package io.apimatic.core.utilities;

import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This is a utility class for XML LocalDate operations.
 */
public class XMLDateHelper {

    protected XMLDateHelper() {}

    /**
     * Serialize the simple date
     * @param dateObj An instance of {@link LocalDate}
     * @param rootName The root name of XML block
     * @return A XML string
     */
    public static String serializeSimpleDate(LocalDate dateObj, String rootName) {
        String xmlBlock = "<" + rootName + ">";
        xmlBlock += dateObj.toString();
        xmlBlock += "</" + rootName + ">";
        return xmlBlock;
    }

    /**
     * Deserialize the simple date string
     * @param xml The Xml string
     * @return An instance of {@link LocalDate}
     */
    public static LocalDate deserializeSimpleDate(String xml) {
        Pattern pattern = Pattern.compile("<.+?>(.+?)</.+?>");
        Matcher patternMatcher = pattern.matcher(xml);

        if (!patternMatcher.find()) {
            throw new IllegalArgumentException("Invalid XML");
        }
        return LocalDate.parse(patternMatcher.group(1));
    }
}
