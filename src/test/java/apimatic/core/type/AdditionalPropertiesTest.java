package apimatic.core.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import apimatic.core.constants.DateTimeConstants;
import apimatic.core.models.ModelWith3dArrayOfNonPrimitiveAdditionalProperties;
import apimatic.core.models.ModelWith3dArrayOfPrimitiveAdditionalProperties;
import apimatic.core.models.ModelWithArrayOfMapOfNonPrimitiveAdditionalProperties;
import apimatic.core.models.ModelWithArrayOfMapOfPrimitiveAdditionalProperties;
import apimatic.core.models.ModelWithArrayOfNonPrimitiveAdditionalProperties;
import apimatic.core.models.ModelWithArrayOfPrimitiveAdditionalProperties;
import apimatic.core.models.ModelWithDateTimeAdditionalProperties;
import apimatic.core.models.ModelWithMapOfArrayOfNonPrimitiveAdditionalProperties;
import apimatic.core.models.ModelWithMapOfArrayOfPrimitiveAdditionalProperties;
import apimatic.core.models.ModelWithMapOfNonPrimitiveAdditionalProperties;
import apimatic.core.models.ModelWithMapOfPrimitiveAdditionalProperties;
import apimatic.core.models.ModelWithNonPrimitiveAdditionalProperties;
import apimatic.core.models.ModelWithPrimitiveAdditionalProperties;
import apimatic.core.models.ModelWithTypeCombinatorAdditionalProperties;
import apimatic.core.models.Vehicle;
import apimatic.core.models.containers.SendScalarParamBody;
import io.apimatic.core.utilities.CoreHelper;
import io.apimatic.core.utilities.LocalDateTimeHelper;

public class AdditionalPropertiesTest {
    @Test
    public void testSimpleAdditionalProperties() throws IOException {
        ModelWithPrimitiveAdditionalProperties simpleModel =
                new ModelWithPrimitiveAdditionalProperties.Builder(
                        "APIMatic").additionalProperty("name", "value").build();

        ModelWithPrimitiveAdditionalProperties actualSimpleModel = CoreHelper
                .deserialize(CoreHelper.serialize(simpleModel),
                        ModelWithPrimitiveAdditionalProperties.class);
        assertEquals("value", actualSimpleModel.getAdditionalProperty("name"));

        // DateTime Case
        ModelWithDateTimeAdditionalProperties dateTimeModel =
                new ModelWithDateTimeAdditionalProperties.Builder("APIMatic")
                .additionalProperty("name",
                        LocalDateTime.of(DateTimeConstants.YEAR2000, DateTimeConstants.JULY,
                                DateTimeConstants.DAY13, DateTimeConstants.HOUR6,
                                DateTimeConstants.MINUTES10))
                        .build();

        ModelWithDateTimeAdditionalProperties actualDateTimeModel = CoreHelper
                .deserialize(CoreHelper.serialize(dateTimeModel),
                        ModelWithDateTimeAdditionalProperties.class);
        assertEquals(LocalDateTimeHelper.fromRfc1123DateTime("Thu, 13 Jul 2000 06:10:00 GMT"),
                actualDateTimeModel.getAdditionalProperty("name"));

        // Non-Primitive Case
        ModelWithNonPrimitiveAdditionalProperties nonPrimitiveModel =
                new ModelWithNonPrimitiveAdditionalProperties.Builder("APIMatic")
                .additionalProperty("name", new Vehicle.Builder("4").build()).build();

        Vehicle expectedNonPrimitiveModel = new Vehicle.Builder("4").build();

        ModelWithNonPrimitiveAdditionalProperties actualNonPrimitiveModel = CoreHelper
                .deserialize(CoreHelper.serialize(nonPrimitiveModel),
                        ModelWithNonPrimitiveAdditionalProperties.class);
        assertEquals(expectedNonPrimitiveModel.toString(),
                actualNonPrimitiveModel.getAdditionalProperty("name").toString());
    }

    @Test
    public void testArrayAdditionalProperties() throws IOException {
        ModelWithArrayOfPrimitiveAdditionalProperties model =
                new ModelWithArrayOfPrimitiveAdditionalProperties.Builder("APIMatic")
                .additionalProperty("name", Arrays.asList("value1", "value2")).build();

        List<String> expectedValue = Arrays.asList("value1", "value2");

        ModelWithArrayOfPrimitiveAdditionalProperties actualModel =
                CoreHelper.deserialize(CoreHelper.serialize(model),
                        ModelWithArrayOfPrimitiveAdditionalProperties.class);
        assertEquals(expectedValue, actualModel.getAdditionalProperty("name"));

        // Non-Primitive Case
        ModelWithArrayOfNonPrimitiveAdditionalProperties nonPrimitiveModel =
                new ModelWithArrayOfNonPrimitiveAdditionalProperties.Builder("APIMatic")
                .additionalProperty("name",
                        Arrays.asList(new Vehicle.Builder("4").build(),
                                new Vehicle.Builder("5").build()))
                .build();

        List<Vehicle> expectedNonPrimitiveModel = Arrays.asList(new Vehicle.Builder("4").build(),
                new Vehicle.Builder("5").build());

        ModelWithArrayOfNonPrimitiveAdditionalProperties actualNonPrimitiveModel =
                CoreHelper.deserialize(
                        CoreHelper.serialize(nonPrimitiveModel),
                        ModelWithArrayOfNonPrimitiveAdditionalProperties.class);
        assertEquals(expectedNonPrimitiveModel.get(0).toString(),
                actualNonPrimitiveModel.getAdditionalProperty("name").get(0).toString());
        assertEquals(expectedNonPrimitiveModel.get(1).toString(),
                actualNonPrimitiveModel.getAdditionalProperty("name").get(1).toString());
    }

    @SuppressWarnings("serial")
    @Test
    public void testMapAdditionalProperties() throws IOException {
        Map<String, String> primitiveAdditionalProperties = new HashMap<String, String>() {
            {
                put("key1", "value1");
                put("key2", "value2");
            }
        };
        ModelWithMapOfPrimitiveAdditionalProperties model =
                new ModelWithMapOfPrimitiveAdditionalProperties.Builder("APIMatic")
                .additionalProperty("name", primitiveAdditionalProperties).build();

        Map<String, String> expectedValue = new HashMap<String, String>() {
            {
                put("key1", "value1");
                put("key2", "value2");
            }
        };

        ModelWithMapOfPrimitiveAdditionalProperties actualModel = CoreHelper.deserialize(
                CoreHelper.serialize(model),
                ModelWithMapOfPrimitiveAdditionalProperties.class);
        assertEquals(expectedValue, actualModel.getAdditionalProperty("name"));

        // Non-Primitive Case
        Map<String, Vehicle> nonPrimitiveAdditionalProperties = new HashMap<String, Vehicle>() {
            {
                put("key1", new Vehicle.Builder("4").build());
                put("key2", new Vehicle.Builder("5").build());
            }
        };
        ModelWithMapOfNonPrimitiveAdditionalProperties nonPrimitiveModel =
                new ModelWithMapOfNonPrimitiveAdditionalProperties.Builder("APIMatic")
                .additionalProperty("name", nonPrimitiveAdditionalProperties).build();

        Map<String, Vehicle> expectedNonPrimitiveModel = new HashMap<String, Vehicle>() {
            {
                put("key1", new Vehicle.Builder("4").build());
                put("key2", new Vehicle.Builder("5").build());
            }
        };

        ModelWithMapOfNonPrimitiveAdditionalProperties actualNonPrimitiveModel =
                CoreHelper.deserialize(CoreHelper.serialize(nonPrimitiveModel),
                        ModelWithMapOfNonPrimitiveAdditionalProperties.class);
        assertEquals(expectedNonPrimitiveModel.get("key1").toString(),
                actualNonPrimitiveModel.getAdditionalProperty("name").get("key1").toString());
    }

    @SuppressWarnings("serial")
    @Test
    public void testMapOfArrayAdditionalProperties() throws IOException {
        Map<String, List<String>> primitiveAdditionalProperties =
                new HashMap<String, List<String>>() {
            {
                put("key1", Arrays.asList("value1", "value2"));
                put("key2", Arrays.asList("value1", "value2"));
            }
        };
        ModelWithMapOfArrayOfPrimitiveAdditionalProperties model =
                new ModelWithMapOfArrayOfPrimitiveAdditionalProperties.Builder("APIMatic")
                .additionalProperty("name", primitiveAdditionalProperties).build();

        Map<String, List<String>> expectedValue = new HashMap<String, List<String>>() {
            {
                put("key1", Arrays.asList("value1", "value2"));
                put("key2", Arrays.asList("value1", "value2"));
            }
        };

        ModelWithMapOfArrayOfPrimitiveAdditionalProperties actualModel =
                CoreHelper.deserialize(CoreHelper.serialize(model),
                        ModelWithMapOfArrayOfPrimitiveAdditionalProperties.class);
        assertEquals(expectedValue, actualModel.getAdditionalProperty("name"));

        // Non-Primitive Case
        Map<String, List<Vehicle>> nonPrimitiveAdditionalProperties =
                new HashMap<String, List<Vehicle>>() {
            {
                put("key1", Arrays.asList(new Vehicle.Builder("4").build()));
                put("key2", Arrays.asList(new Vehicle.Builder("5").build()));
            }
        };
        ModelWithMapOfArrayOfNonPrimitiveAdditionalProperties nonPrimitiveModel =
                new ModelWithMapOfArrayOfNonPrimitiveAdditionalProperties.Builder("APIMatic")
                .additionalProperty("name", nonPrimitiveAdditionalProperties).build();

        Map<String, List<Vehicle>> expectedNonPrimitiveModel =
                new HashMap<String, List<Vehicle>>() {
            {
                put("key1", Arrays.asList(new Vehicle.Builder("4").build()));
                put("key2", Arrays.asList(new Vehicle.Builder("5").build()));
            }
        };

        ModelWithMapOfArrayOfNonPrimitiveAdditionalProperties actualNonPrimitiveModel =
                CoreHelper.deserialize(CoreHelper.serialize(nonPrimitiveModel),
                        ModelWithMapOfArrayOfNonPrimitiveAdditionalProperties.class);
        assertEquals(expectedNonPrimitiveModel.get("key1").get(0).toString(),
                actualNonPrimitiveModel.getAdditionalProperty("name")
                .get("key1").get(0).toString());
        assertEquals(expectedNonPrimitiveModel.get("key2").get(0).toString(),
                actualNonPrimitiveModel.getAdditionalProperty("name")
                .get("key2").get(0).toString());
    }

    @SuppressWarnings("serial")
    @Test
    public void testArrayOfMapAdditionalProperties() throws IOException {
        Map<String, String> primitiveAdditionalProperties = new HashMap<String, String>() {
            {
                put("key1", "value1");
                put("key2", "value2");
            }
        };
        ModelWithArrayOfMapOfPrimitiveAdditionalProperties model =
                new ModelWithArrayOfMapOfPrimitiveAdditionalProperties.Builder("APIMatic")
                .additionalProperty("name", Arrays.asList(primitiveAdditionalProperties)).build();

        List<Map<String, String>> expectedValue = Arrays.asList(primitiveAdditionalProperties);

        ModelWithArrayOfMapOfPrimitiveAdditionalProperties actualModel = CoreHelper.deserialize(
                CoreHelper.serialize(model),
                ModelWithArrayOfMapOfPrimitiveAdditionalProperties.class);
        assertEquals(expectedValue, actualModel.getAdditionalProperty("name"));

        // Non-Primitive Case
        Map<String, Vehicle> nonPrimitiveAdditionalProperties = new HashMap<String, Vehicle>() {
            {
                put("key1", new Vehicle.Builder("4").build());
                put("key2", new Vehicle.Builder("5").build());
            }
        };
        ModelWithArrayOfMapOfNonPrimitiveAdditionalProperties nonPrimitiveModel =
                new ModelWithArrayOfMapOfNonPrimitiveAdditionalProperties.Builder("APIMatic")
                .additionalProperty("name", Arrays.asList(nonPrimitiveAdditionalProperties))
                .build();

        List<Map<String, Vehicle>> expectedNonPrimitiveModel = Arrays.asList(
                nonPrimitiveAdditionalProperties);

        ModelWithArrayOfMapOfNonPrimitiveAdditionalProperties actualNonPrimitiveModel =
                CoreHelper.deserialize(CoreHelper.serialize(nonPrimitiveModel),
                        ModelWithArrayOfMapOfNonPrimitiveAdditionalProperties.class);
        assertEquals(expectedNonPrimitiveModel.get(0).get("key1").toString(),
                actualNonPrimitiveModel.getAdditionalProperty("name")
                .get(0).get("key1").toString());
        assertEquals(expectedNonPrimitiveModel.get(0).get("key2").toString(),
                actualNonPrimitiveModel.getAdditionalProperty("name")
                .get(0).get("key2").toString());
    }

    @Test
    public void test3DArrayAdditionalProperties() throws IOException {
        ModelWith3dArrayOfPrimitiveAdditionalProperties model =
                new ModelWith3dArrayOfPrimitiveAdditionalProperties.Builder("APIMatic")
                .additionalProperty("name", Arrays.asList(
                        Arrays.asList(Arrays.asList("value1", "value2"))))
                .build();

        List<List<List<String>>> expectedValue = Arrays.asList(Arrays.asList(
                Arrays.asList("value1", "value2")));

        ModelWith3dArrayOfPrimitiveAdditionalProperties actualModel = CoreHelper.deserialize(
                CoreHelper.serialize(model),
                ModelWith3dArrayOfPrimitiveAdditionalProperties.class);
        assertEquals(expectedValue, actualModel.getAdditionalProperty("name"));

        // Non-Primitive Case
        ModelWith3dArrayOfNonPrimitiveAdditionalProperties nonPrimitiveModel =
                new ModelWith3dArrayOfNonPrimitiveAdditionalProperties.Builder("APIMatic")
                .additionalProperty("name", Arrays.asList(Arrays.asList(Arrays.asList(
                        new Vehicle.Builder("4").build(), new Vehicle.Builder("4").build()))))
                .build();

        List<List<List<Vehicle>>> expectedNonPrimitiveModel = Arrays.asList(
                Arrays.asList(Arrays.asList(new Vehicle.Builder("4").build(),
                        new Vehicle.Builder("4").build())));

        ModelWith3dArrayOfNonPrimitiveAdditionalProperties actualNonPrimitiveModel =
                CoreHelper.deserialize(CoreHelper.serialize(nonPrimitiveModel),
                        ModelWith3dArrayOfNonPrimitiveAdditionalProperties.class);
        assertEquals(expectedNonPrimitiveModel.get(0).get(0).get(0).toString(),
                actualNonPrimitiveModel.getAdditionalProperty("name")
                .get(0).get(0).get(0).toString());
    }

    @Test
    public void testTypeCombinatorAdditionalProperties() throws IOException {
        final Double item1 = 100.11;
        final Double item2 = 133.00;
        // Case A
        ModelWithTypeCombinatorAdditionalProperties model =
                new ModelWithTypeCombinatorAdditionalProperties.Builder("APIMatic")
                .additionalProperty("name", SendScalarParamBody.fromPrecision(
                        Arrays.asList(item1, item2)))
                .build();

        ModelWithTypeCombinatorAdditionalProperties actualModel = CoreHelper.deserialize(
                CoreHelper.serialize(model), ModelWithTypeCombinatorAdditionalProperties.class);
        List<Double> expectedListValue = Arrays.asList(item1, item2);
        actualModel.getAdditionalProperty("name").match(new SendScalarParamBody.Cases<Void>() {

            @Override
            public Void precision(List<Double> actualValue) {
                assertEquals(expectedListValue, actualValue);
                return null;
            }

            @Override
            public Void mString(String mString) {
                fail("Got unexpected type combinator case.");
                return null;
            }
        });

        // Case B
        model = new ModelWithTypeCombinatorAdditionalProperties.Builder("APIMatic")
                .additionalProperty("name", SendScalarParamBody.fromMString("value")).build();

        actualModel = CoreHelper.deserialize(CoreHelper.serialize(model),
                ModelWithTypeCombinatorAdditionalProperties.class);
        String expectedStringValue = "value";
        actualModel.getAdditionalProperty("name").match(new SendScalarParamBody.Cases<Void>() {

            @Override
            public Void precision(List<Double> actualValue) {
                fail("Got unexpected type combinator case.");
                return null;
            }

            @Override
            public Void mString(String actualValue) {
                assertEquals(expectedStringValue, actualValue);
                return null;
            }
        });
    }
}
