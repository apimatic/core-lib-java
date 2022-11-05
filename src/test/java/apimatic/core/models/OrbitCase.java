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
 * This is a implementation class for OrbitCase.
 */
@JsonDeserialize(using = JsonDeserializer.None.class)
@TypeCombinatorCase
public class OrbitCase {

    @JsonValue
    private Orbit orbit;

    OrbitCase(final Orbit orbit) {
        this.orbit = orbit;
    }


    @JsonCreator
    private OrbitCase(final JsonNode jsonNode) throws IOException {
        this.orbit = CoreHelper.deserialize(jsonNode, Orbit.class);
    }

    @Override
    public String toString() {
        return orbit.toString();
    }
}
