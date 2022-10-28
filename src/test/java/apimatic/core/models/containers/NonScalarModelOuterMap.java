/*
 * TypeCombinatorModerateLib
 *
 * This file was automatically generated by APIMATIC v3.0 ( https://www.apimatic.io ).
 */

package apimatic.core.models.containers;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import apimatic.core.models.Evening;
import apimatic.core.models.Morning;
import apimatic.core.models.Noon;
import io.apimatic.core.annotations.TypeCombinator.TypeCombinatorCase;
import io.apimatic.core.utilities.CoreHelper;

/**
 * This is a container class for any-of types.
 */
@JsonDeserialize(using = NonScalarModelOuterMap.NonScalarModelOuterMapDeserializer.class)
public abstract class NonScalarModelOuterMap {

    /**
     * Any-of type initialization method.
     * @param morning Morning value for morning.
     * @return The MorningCase object.
     */
    public static NonScalarModelOuterMap fromMorning(Morning morning) {
        return morning == null ? null : new MorningCase(morning);
    }

    /**
     * Any-of type initialization method.
     * @param evening Evening value for evening.
     * @return The EveningCase object.
     */
    public static NonScalarModelOuterMap fromEvening(Evening evening) {
        return evening == null ? null : new EveningCase(evening);
    }

    /**
     * Any-of type initialization method.
     * @param noon Noon value for noon.
     * @return The NoonCase object.
     */
    public static NonScalarModelOuterMap fromNoon(Noon noon) {
        return noon == null ? null : new NoonCase(noon);
    }

    /**
     * Method to match from the provided any-of cases.
     * @param <R> The type to return after applying callback.
     * @param cases The any-of type cases callback.
     * @return The any-of matched case.
     */
    public abstract <R> R match(Cases<R> cases);

    /**
     * This is interface for any-of cases.
     * @param <R> The type to return after applying callback.
     */
    public interface Cases<R> {
        R morning(Morning morning);

        R evening(Evening evening);

        R noon(Noon noon);
    }

    /**
     * This is a implementation class for MorningCase.
     */
    @JsonDeserialize(using = JsonDeserializer.None.class)
    @TypeCombinatorCase
    private static class MorningCase extends NonScalarModelOuterMap {

        @JsonValue
        private Morning morning;

        MorningCase(Morning morning) {
            this.morning = morning;
        }

        @Override
        public <R> R match(Cases<R> cases) {
            return cases.morning(this.morning);
        }

        @JsonCreator
        private MorningCase(JsonNode jsonNode) throws IOException {
            this.morning = CoreHelper.deserialize(jsonNode, Morning.class);
        }

        @Override
        public String toString() {
            return morning.toString();
        }
    }

    /**
     * This is a implementation class for EveningCase.
     */
    @JsonDeserialize(using = JsonDeserializer.None.class)
    @TypeCombinatorCase
    private static class EveningCase extends NonScalarModelOuterMap {

        @JsonValue
        private Evening evening;

        EveningCase(Evening evening) {
            this.evening = evening;
        }

        @Override
        public <R> R match(Cases<R> cases) {
            return cases.evening(this.evening);
        }

        @JsonCreator
        private EveningCase(JsonNode jsonNode) throws IOException {
            this.evening = CoreHelper.deserialize(jsonNode, Evening.class);
        }

        @Override
        public String toString() {
            return evening.toString();
        }
    }

    /**
     * This is a implementation class for NoonCase.
     */
    @JsonDeserialize(using = JsonDeserializer.None.class)
    @TypeCombinatorCase
    private static class NoonCase extends NonScalarModelOuterMap {

        @JsonValue
        private Noon noon;

        NoonCase(Noon noon) {
            this.noon = noon;
        }

        @Override
        public <R> R match(Cases<R> cases) {
            return cases.noon(this.noon);
        }

        @JsonCreator
        private NoonCase(JsonNode jsonNode) throws IOException {
            this.noon = CoreHelper.deserialize(jsonNode, Noon.class);
        }

        @Override
        public String toString() {
            return noon.toString();
        }
    }

    /**
     * This is a custom deserializer class for NonScalarModelOuterMap.
     */
    protected static class NonScalarModelOuterMapDeserializer
            extends JsonDeserializer<NonScalarModelOuterMap> {

        private String discriminator = "sessionType";
        private List<Map<String, Class<? extends NonScalarModelOuterMap>>> registry =
                Arrays.asList(Collections.singletonMap("Morning", MorningCase.class),
                        Collections.singletonMap("Evening", EveningCase.class),
                        Collections.singletonMap("Noon", NoonCase.class));

        @Override
        public NonScalarModelOuterMap deserialize(JsonParser jp, DeserializationContext ctxt)
                throws IOException, JsonProcessingException {
            return CoreHelper.deserialize(jp, ctxt, discriminator, registry, null, false);
        }
    }

}
