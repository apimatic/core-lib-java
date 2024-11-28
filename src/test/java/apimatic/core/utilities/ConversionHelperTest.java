package apimatic.core.utilities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.junit.Test;

import io.apimatic.core.utilities.ConversionHelper;

public class ConversionHelperTest {

    // Constants
    private static final int EXPECTED_SIZE_ONE = 1;
    private static final int EXPECTED_SIZE_TWO = 2;
    private static final Integer EXPECTED_INTEGER_ONE = Integer.valueOf(1);
    private static final Integer EXPECTED_INTEGER_TWO = Integer.valueOf(2);
    private static final Integer EXPECTED_INTEGER_THREE = Integer.valueOf(3);
    private static final Integer EXPECTED_INTEGER_FOUR = Integer.valueOf(4);
    private static final Integer TEST_INTEGER_FIVE = Integer.valueOf(5);
    private static final String KEY_ONE = "key1";
    private static final String KEY_TWO = "key2";
    private static final String NON_INTEGER_VALUE = "not an int";
    private static final int DIMENSION_ONE = 1;
    private static final int DIMENSION_TWO = 2;

    // Helper function for conversion
    private static final Function<Object, Integer> TO_INTEGER = value -> {
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        return null;
    };

    @Test
    public void testConvertToSimpleTypeSuccess() {
        assertEquals(TEST_INTEGER_FIVE, ConversionHelper.convertToSimpleType(
                TEST_INTEGER_FIVE, TO_INTEGER));
    }

    @Test
    public void testConvertToSimpleTypeNullConversionFunction() {
        assertNull(ConversionHelper.convertToSimpleType("test", TO_INTEGER));
    }

    @Test
    public void testConvertToMapSuccess() {
        Map<String, Object> input = new HashMap<>();
        input.put(KEY_ONE, EXPECTED_INTEGER_ONE);
        input.put(KEY_TWO, EXPECTED_INTEGER_TWO);
        Map<String, Integer> result = ConversionHelper.convertToMap(input, TO_INTEGER);
        assertEquals(EXPECTED_SIZE_TWO, result.size());
        assertEquals(EXPECTED_INTEGER_ONE, result.get(KEY_ONE));
        assertEquals(EXPECTED_INTEGER_TWO, result.get(KEY_TWO));
    }

    @Test
    public void testConvertToMapSkipsUnconvertibleValues() {
        Map<String, Object> input = new HashMap<>();
        input.put(KEY_ONE, EXPECTED_INTEGER_ONE);
        input.put(KEY_TWO, NON_INTEGER_VALUE);
        Map<String, Integer> result = ConversionHelper.convertToMap(input, TO_INTEGER);
        assertEquals(EXPECTED_SIZE_ONE, result.size());
        assertEquals(EXPECTED_INTEGER_ONE, result.get(KEY_ONE));
        assertNull(result.get(KEY_TWO));
    }

    @Test
    public void testConvertToMapNullInput() {
        assertNull(ConversionHelper.convertToMap(null, TO_INTEGER));
    }

    @Test
    public void testConvertToArraySuccess() {
        List<Object> input = Arrays.asList(EXPECTED_INTEGER_ONE,
                EXPECTED_INTEGER_TWO, EXPECTED_INTEGER_THREE);
        List<Integer> result = ConversionHelper.convertToArray(input, TO_INTEGER);
        assertEquals(Arrays.asList(EXPECTED_INTEGER_ONE,
                EXPECTED_INTEGER_TWO, EXPECTED_INTEGER_THREE), result);
    }

    @Test
    public void testConvertToArraySkipsUnconvertibleItems() {
        List<Object> input = Arrays.asList(EXPECTED_INTEGER_ONE,
                NON_INTEGER_VALUE, EXPECTED_INTEGER_THREE);
        List<Integer> result = ConversionHelper.convertToArray(input, TO_INTEGER);
        assertEquals(Arrays.asList(EXPECTED_INTEGER_ONE, EXPECTED_INTEGER_THREE), result);
    }

    @Test
    public void testConvertToArrayNullInput() {
        assertNull(ConversionHelper.convertToArray(null, TO_INTEGER));
    }

    @Test
    public void testConvertToArrayOfMapSuccess() {
        List<Object> input = new ArrayList<>();
        Map<String, Object> map1 = new HashMap<>();
        map1.put(KEY_ONE, EXPECTED_INTEGER_ONE);
        input.add(map1);
        List<Map<String, Integer>> result = ConversionHelper.convertToArrayOfMap(input, TO_INTEGER);
        assertEquals(EXPECTED_SIZE_ONE, result.size());
        assertEquals(EXPECTED_INTEGER_ONE, result.get(0).get(KEY_ONE));
    }

    @Test
    public void testConvertToArrayOfMapSkipsEmptyAndUnconvertibleMaps() {
        List<Object> input = new ArrayList<>();
        Map<String, Object> map1 = new HashMap<>();
        map1.put(KEY_ONE, EXPECTED_INTEGER_ONE);
        Map<String, Object> map2 = new HashMap<>();
        map2.put(KEY_TWO, NON_INTEGER_VALUE);
        input.add(map1);
        input.add(map2);
        List<Map<String, Integer>> result = ConversionHelper.convertToArrayOfMap(input, TO_INTEGER);
        assertEquals(EXPECTED_SIZE_ONE, result.size());
        assertEquals(EXPECTED_INTEGER_ONE, result.get(0).get(KEY_ONE));
    }

    @Test
    public void testConvertToArrayOfMapNullInput() {
        assertNull(ConversionHelper.convertToArrayOfMap(null, TO_INTEGER));
    }

    @Test
    public void testConvertToMapOfArraySuccess() {
        Map<String, Object> input = new HashMap<>();
        input.put(KEY_ONE, Arrays.asList(EXPECTED_INTEGER_ONE, EXPECTED_INTEGER_TWO));
        Map<String, List<Integer>> result = ConversionHelper.convertToMapOfArray(input, TO_INTEGER);
        assertEquals(EXPECTED_SIZE_ONE, result.size());
        assertEquals(Arrays.asList(EXPECTED_INTEGER_ONE, EXPECTED_INTEGER_TWO),
                result.get(KEY_ONE));
    }

    @Test
    public void testConvertToMapOfArraySkipsEmptyAndUnconvertibleArrays() {
        Map<String, Object> input = new HashMap<>();
        input.put(KEY_ONE, Arrays.asList(EXPECTED_INTEGER_ONE,
                NON_INTEGER_VALUE, EXPECTED_INTEGER_THREE));
        input.put(KEY_TWO, Arrays.asList(NON_INTEGER_VALUE));
        Map<String, List<Integer>> result = ConversionHelper.convertToMapOfArray(input, TO_INTEGER);
        assertEquals(EXPECTED_SIZE_ONE, result.size());
        assertEquals(Arrays.asList(EXPECTED_INTEGER_ONE, EXPECTED_INTEGER_THREE),
                result.get(KEY_ONE));
    }

    @Test
    public void testConvertToMapOfArrayNullInput() {
        assertNull(ConversionHelper.convertToMapOfArray(null, TO_INTEGER));
    }

    @Test
    public void testConvertToNDimensionalArray1DArray() {
        List<Object> input = Arrays.asList(EXPECTED_INTEGER_ONE,
                EXPECTED_INTEGER_TWO, EXPECTED_INTEGER_THREE);
        List<Integer> result = ConversionHelper.convertToNDimensionalArray(input,
                TO_INTEGER, DIMENSION_ONE);
        assertEquals(Arrays.asList(EXPECTED_INTEGER_ONE,
                EXPECTED_INTEGER_TWO, EXPECTED_INTEGER_THREE), result);
    }

    @Test
    public void testConvertToNDimensionalArray2DArray() {
        List<Object> input = Arrays.asList(
                Arrays.asList(EXPECTED_INTEGER_ONE, EXPECTED_INTEGER_TWO),
                Arrays.asList(EXPECTED_INTEGER_THREE, EXPECTED_INTEGER_FOUR));
        List<List<Integer>> result = ConversionHelper.convertToNDimensionalArray(input,
                TO_INTEGER, DIMENSION_TWO);
        assertEquals(Arrays.asList(Arrays.asList(EXPECTED_INTEGER_ONE, EXPECTED_INTEGER_TWO),
                Arrays.asList(EXPECTED_INTEGER_THREE, EXPECTED_INTEGER_FOUR)), result);
    }

    @Test
    public void testConvertToNDimensionalArraySkipsEmptyAndUnconvertibleItems() {
        List<Object> input = Arrays.asList(Arrays.asList(EXPECTED_INTEGER_ONE, NON_INTEGER_VALUE),
                Arrays.asList(EXPECTED_INTEGER_THREE, EXPECTED_INTEGER_FOUR));
        List<List<Integer>> result = ConversionHelper.convertToNDimensionalArray(input,
                TO_INTEGER, DIMENSION_TWO);
        assertEquals(Arrays.asList(Arrays.asList(EXPECTED_INTEGER_ONE),
                Arrays.asList(EXPECTED_INTEGER_THREE, EXPECTED_INTEGER_FOUR)), result);
    }

    @Test
    public void testConvertToNDimensionalArrayNullInput() {
        assertNull(ConversionHelper.convertToNDimensionalArray(null, TO_INTEGER, DIMENSION_TWO));
    }
}
