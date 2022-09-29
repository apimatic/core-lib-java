package apimatic.core.models;

import java.io.IOException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.apimatic.core.annotations.TypeCombinator.TypeCombinatorCase;
import io.apimatic.core.utilities.CoreHelper;

/**
 * This is a implementation class for MorningCase.
 */
@JsonDeserialize(using = JsonDeserializer.None.class)
@TypeCombinatorCase
public  class MorningCase {

    @JsonValue
    private Morning morning;

    MorningCase(Morning morning) {
        this.morning = morning;
    }


    @JsonCreator
    private MorningCase(JsonNode jsonNode) throws IOException {
        this.morning = CoreHelper.deserialize(jsonNode,
            Morning.class);
    }

    @Override
    public String toString() {
        return morning.toString();
    }
}
