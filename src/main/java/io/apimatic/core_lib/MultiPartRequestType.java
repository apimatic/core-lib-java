package io.apimatic.core_lib;


import java.util.TreeMap;


public enum MultiPartRequestType {

	MULTI_PART_FILE,

    MULTI_PART;


    private static TreeMap<String, MultiPartRequestType> valueMap = new TreeMap<>();
    private String value;

    static {
    	MULTI_PART_FILE.value = "multi part file";
    	MULTI_PART.value = "multi part";

        valueMap.put("multi part file", MULTI_PART_FILE);
        valueMap.put("multi part", MULTI_PART);
    }

    /**
     * Returns the enum member associated with the given string value.
     * @param toConvert String value to get enum member.
     * @return The enum member against the given string value.
     */
    @com.fasterxml.jackson.annotation.JsonCreator
    public static MultiPartRequestType fromString(String toConvert) {
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

}
