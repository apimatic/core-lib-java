/*
 * MdNotesCCGLib
 *
 * This file was automatically generated by APIMATIC v3.0 ( https://www.apimatic.io ).
 */

package apimatic.core.models;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * OAuthScopeEnum to be used.
 */
public enum OAuthScopeEnum {
    /**
     * Read scope
     */
    READ_SCOPE,

    /**
     * Write scope
     */
    WRITE_SCOPE;


    private static TreeMap<String, OAuthScopeEnum> valueMap = new TreeMap<>();
    private String value;

    static {
        READ_SCOPE.value = "read";
        WRITE_SCOPE.value = "write";

        valueMap.put("read", READ_SCOPE);
        valueMap.put("write", WRITE_SCOPE);
    }

    /**
     * Returns the enum member associated with the given string value.
     * @param toConvert String value to get enum member.
     * @return The enum member against the given string value.
     */
    @com.fasterxml.jackson.annotation.JsonCreator
    public static OAuthScopeEnum fromString(String toConvert) {
        return valueMap.get(toConvert);
    }

    /**
     * Returns the string value associated with the enum member.
     * @return The string value against enum member.
     */
    @com.fasterxml.jackson.annotation.JsonValue
    public String value() {
        return value;
    }
        
    /**
     * Get string representation of this enum.
     */
    @Override
    public String toString() {
        return value.toString();
    }

    /**
     * Convert list of OAuthScopeEnum values to list of string values.
     * @param toConvert The list of OAuthScopeEnum values to convert.
     * @return List of representative string values.
     */
    public static List<String> toValue(List<OAuthScopeEnum> toConvert) {
        if (toConvert == null) {
            return null;
        }
        List<String> convertedValues = new ArrayList<>();
        for (OAuthScopeEnum enumValue : toConvert) {
            convertedValues.add(enumValue.value);
        }
        return convertedValues;
    }
} 