package io.apimatic.core_lib.types.http.request;

import io.apimatic.core_interfaces.http.request.ArraySerializationFormat;

public class ArraySerializationFormatter {
    private static final String INDEXED_FORMAT = "%s[%d]";
    private static final String UNINDEXED_FORMAT = "\"%s[]\"";
    private static final String PLAIN_FORMAT = "%s[%d]";
    private static final String PSV_FORMAT = "%s[%d]";
    private static final String CSV_FORMAT = "%s[%d]";
    private static final String TSV_FORMAT = "%s[%d]";



    public static String getSerializationFormat(ArraySerializationFormat arraySerializationFormat) {

        switch (arraySerializationFormat) {
            case INDEXED:
                return INDEXED_FORMAT;
            case UNINDEXED:
                return UNINDEXED_FORMAT;

            case PLAIN:
                return PLAIN_FORMAT;

            case PSV:
            case CSV:
            case TSV:
                return TSV_FORMAT;

            default:
                return INDEXED_FORMAT;
        }
    }
}
