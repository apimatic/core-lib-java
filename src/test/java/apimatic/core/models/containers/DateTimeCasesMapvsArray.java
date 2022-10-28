/*
 * TypeCombinatorSpecialLib
 *
 * This file was automatically generated by APIMATIC v3.0 ( https://www.apimatic.io ).
 */

package apimatic.core.models.containers;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.apimatic.core.annotations.TypeCombinator.FormSerialize;
import io.apimatic.core.annotations.TypeCombinator.TypeCombinatorCase;
import io.apimatic.core.utilities.CoreHelper;
import io.apimatic.core.utilities.LocalDateTimeHelper;

/**
 * This is a container class for any-of types.
 */
@JsonDeserialize(using = DateTimeCasesMapvsArray.DateTimeCasesMapvsArrayDeserializer.class)
public abstract class DateTimeCasesMapvsArray {

    /**
     * Any-of type initialization method.
     * @param dateTime List of LocalDateTime value for dateTime.
     * @return The DateTimeCase object.
     */
    public static DateTimeCasesMapvsArray fromDateTime(List<LocalDateTime> dateTime) {
        return dateTime == null ? null : new DateTimeCase(dateTime);
    }

    /**
     * Any-of type initialization method.
     * @param dateTime2 Map of String, value for dateTime2.
     * @return The DateTime2Case object.
     */
    public static DateTimeCasesMapvsArray fromDateTime2(Map<String, LocalDateTime> dateTime2) {
        return dateTime2 == null ? null : new DateTime2Case(dateTime2);
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
        R dateTime(List<LocalDateTime> dateTime);

        R dateTime2(Map<String, LocalDateTime> dateTime2);
    }

    /**
     * This is a implementation class for DateTimeCase.
     */
    @JsonDeserialize(using = JsonDeserializer.None.class)
    @JsonSerialize(using = DateTimeCase.DateTimeCaseSerializer.class)
    @TypeCombinatorCase
    private static class DateTimeCase extends DateTimeCasesMapvsArray {

        @FormSerialize(contentUsing = LocalDateTimeHelper.Rfc1123DateTimeSerializer.class)
        private List<LocalDateTime> dateTime;

        DateTimeCase(List<LocalDateTime> dateTime) {
            this.dateTime = dateTime;
        }

        @Override
        public <R> R match(Cases<R> cases) {
            return cases.dateTime(this.dateTime);
        }

        @JsonCreator
        private DateTimeCase(JsonNode jsonNode) throws IOException {
            this.dateTime =
                    CoreHelper.deserialize(jsonNode, new TypeReference<List<LocalDateTime>>() {},
                            LocalDateTime.class,
                            new LocalDateTimeHelper.Rfc1123DateTimeDeserializer());
        }

        @Override
        public String toString() {
            return LocalDateTimeHelper.toRfc1123DateTime(dateTime).toString();
        }

        /**
         * This is a custom serializer class for DateTimeCase.
         */
        private static class DateTimeCaseSerializer extends JsonSerializer<DateTimeCase> {
            @Override
            public void serialize(DateTimeCase dateTimeCase, JsonGenerator jgen,
                    SerializerProvider provider) throws IOException {
                jgen.writeObject(LocalDateTimeHelper.toRfc1123DateTime(dateTimeCase.dateTime));
            }
        }
    }

    /**
     * This is a implementation class for DateTime2Case.
     */
    @JsonDeserialize(using = JsonDeserializer.None.class)
    @JsonSerialize(using = DateTime2Case.DateTime2CaseSerializer.class)
    @TypeCombinatorCase
    private static class DateTime2Case extends DateTimeCasesMapvsArray {

        @FormSerialize(contentUsing = LocalDateTimeHelper.Rfc1123DateTimeSerializer.class)
        private Map<String, LocalDateTime> dateTime2;

        DateTime2Case(Map<String, LocalDateTime> dateTime2) {
            this.dateTime2 = dateTime2;
        }

        @Override
        public <R> R match(Cases<R> cases) {
            return cases.dateTime2(this.dateTime2);
        }

        @JsonCreator
        private DateTime2Case(JsonNode jsonNode) throws IOException {
            this.dateTime2 =
                    CoreHelper.deserialize(jsonNode,
                            new TypeReference<Map<String, LocalDateTime>>() {}, LocalDateTime.class,
                            new LocalDateTimeHelper.Rfc1123DateTimeDeserializer());
        }

        @Override
        public String toString() {
            return LocalDateTimeHelper.toRfc1123DateTime(dateTime2).toString();
        }

        /**
         * This is a custom serializer class for DateTime2Case.
         */
        private static class DateTime2CaseSerializer extends JsonSerializer<DateTime2Case> {
            @Override
            public void serialize(DateTime2Case dateTime2Case, JsonGenerator jgen,
                    SerializerProvider provider) throws IOException {
                jgen.writeObject(LocalDateTimeHelper.toRfc1123DateTime(dateTime2Case.dateTime2));
            }
        }
    }

    /**
     * This is a custom deserializer class for DateTimeCasesMapvsArray.
     */
    protected static class DateTimeCasesMapvsArrayDeserializer
            extends JsonDeserializer<DateTimeCasesMapvsArray> {

        @Override
        public DateTimeCasesMapvsArray deserialize(JsonParser jp, DeserializationContext ctxt)
                throws IOException, JsonProcessingException {
            ObjectCodec oc = jp.getCodec();
            JsonNode node = oc.readTree(jp);
            return CoreHelper.deserialize(node,
                    Arrays.asList(DateTimeCase.class, DateTime2Case.class), false);
        }
    }

}
