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

    // Helper function for conversion
    private static final Function<Object, Integer> toInteger = value -> {
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        return null;
    };

    @Test
    public void testConvertToSimpleType_Success() {
        assertEquals(Integer.valueOf(5), ConversionHelper.convertToSimpleType(5, toInteger));
    }

    @Test
    public void testConvertToSimpleType_NullConversionFunction() {
        assertNull(ConversionHelper.convertToSimpleType("test", toInteger));
    }

    @Test
    public void testConvertToMap_Success() {
        Map<String, Object> input = new HashMap<>();
        input.put("key1", 1);
        input.put("key2", 2);
        Map<String, Integer> result = ConversionHelper.convertToMap(input, toInteger);
        assertEquals(2, result.size());
        assertEquals(Integer.valueOf(1), result.get("key1"));
        assertEquals(Integer.valueOf(2), result.get("key2"));
    }

    @Test
    public void testConvertToMap_SkipsUnconvertibleValues() {
        Map<String, Object> input = new HashMap<>();
        input.put("key1", 1);
        input.put("key2", "not an int");
        Map<String, Integer> result = ConversionHelper.convertToMap(input, toInteger);
        assertEquals(1, result.size());
        assertEquals(Integer.valueOf(1), result.get("key1"));
        assertNull(result.get("key2"));
    }

    @Test
    public void testConvertToMap_NullInput() {
        assertNull(ConversionHelper.convertToMap(null, toInteger));
    }

    @Test
    public void testConvertToArray_Success() {
        List<Object> input = Arrays.asList(1, 2, 3);
        List<Integer> result = ConversionHelper.convertToArray(input, toInteger);
        assertEquals(Arrays.asList(Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3)), result);
    }

    @Test
    public void testConvertToArray_SkipsUnconvertibleItems() {
        List<Object> input = Arrays.asList(1, "not an int", 3);
        List<Integer> result = ConversionHelper.convertToArray(input, toInteger);
        assertEquals(Arrays.asList(Integer.valueOf(1), Integer.valueOf(3)), result);
    }

    @Test
    public void testConvertToArray_NullInput() {
        assertNull(ConversionHelper.convertToArray(null, toInteger));
    }

    @Test
    public void testConvertToArrayOfMap_Success() {
        List<Object> input = new ArrayList<>();
        Map<String, Object> map1 = new HashMap<>();
        map1.put("key1", 1);
        input.add(map1);
        List<Map<String, Integer>> result = ConversionHelper.convertToArrayOfMap(input, toInteger);
        assertEquals(1, result.size());
        assertEquals(Integer.valueOf(1), result.get(0).get("key1"));
    }

    @Test
    public void testConvertToArrayOfMap_SkipsEmptyAndUnconvertibleMaps() {
        List<Object> input = new ArrayList<>();
        Map<String, Object> map1 = new HashMap<>();
        map1.put("key1", 1);
        Map<String, Object> map2 = new HashMap<>();
        map2.put("key2", "not an int");
        input.add(map1);
        input.add(map2);
        List<Map<String, Integer>> result = ConversionHelper.convertToArrayOfMap(input, toInteger);
        assertEquals(1, result.size());
        assertEquals(Integer.valueOf(1), result.get(0).get("key1"));
    }

    @Test
    public void testConvertToArrayOfMap_NullInput() {
        assertNull(ConversionHelper.convertToArrayOfMap(null, toInteger));
    }

    @Test
    public void testConvertToMapOfArray_Success() {
        Map<String, Object> input = new HashMap<>();
        input.put("key1", Arrays.asList(1, 2));
        Map<String, List<Integer>> result = ConversionHelper.convertToMapOfArray(input, toInteger);
        assertEquals(1, result.size());
        assertEquals(Arrays.asList(Integer.valueOf(1), Integer.valueOf(2)), result.get("key1"));
    }

    @Test
    public void testConvertToMapOfArray_SkipsEmptyAndUnconvertibleArrays() {
        Map<String, Object> input = new HashMap<>();
        input.put("key1", Arrays.asList(1, "not an int", 3));
        input.put("key2", Arrays.asList("not an int"));
        Map<String, List<Integer>> result = ConversionHelper.convertToMapOfArray(input, toInteger);
        assertEquals(1, result.size());
        assertEquals(Arrays.asList(Integer.valueOf(1), Integer.valueOf(3)), result.get("key1"));
    }

    @Test
    public void testConvertToMapOfArray_NullInput() {
        assertNull(ConversionHelper.convertToMapOfArray(null, toInteger));
    }

    @Test
    public void testConvertToNDimensionalArray_1DArray() {
        List<Object> input = Arrays.asList(1, 2, 3);
        List<Integer> result = ConversionHelper.convertToNDimensionalArray(input, toInteger, 1);
        assertEquals(Arrays.asList(Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3)), result);
    }

    @Test
    public void testConvertToNDimensionalArray_2DArray() {
        List<Object> input = Arrays.asList(Arrays.asList(1, 2), Arrays.asList(3, 4));
        List<List<Integer>> result = ConversionHelper.convertToNDimensionalArray(input, toInteger, 2);
        assertEquals(Arrays.asList(Arrays.asList(Integer.valueOf(1), Integer.valueOf(2)),
                Arrays.asList(Integer.valueOf(3), Integer.valueOf(4))), result);
    }

    @Test
    public void testConvertToNDimensionalArray_SkipsEmptyAndUnconvertibleItems() {
        List<Object> input = Arrays.asList(Arrays.asList(1, "not an int"), Arrays.asList(3, 4));
        List<List<Integer>> result = ConversionHelper.convertToNDimensionalArray(input, toInteger, 2);
        assertEquals(
                Arrays.asList(Arrays.asList(Integer.valueOf(1)), Arrays.asList(Integer.valueOf(3), Integer.valueOf(4))),
                result);
    }

    @Test
    public void testConvertToNDimensionalArray_NullInput() {
        assertNull(ConversionHelper.convertToNDimensionalArray(null, toInteger, 2));
    }
}
