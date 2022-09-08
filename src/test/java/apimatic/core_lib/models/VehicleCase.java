package apimatic.core_lib.models;

import java.io.IOException;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.apimatic.core_lib.annotations.TypeCombinator.TypeCombinatorCase;
import io.apimatic.core_lib.utilities.CoreHelper;

/**
 * This is a implementation class for VehicleCase.
 */
@JsonDeserialize(using = JsonDeserializer.None.class)
@TypeCombinatorCase
public class VehicleCase {

    @JsonValue
    private List<Vehicle> vehicle;

    VehicleCase(List<Vehicle> vehicle) {
        this.vehicle = vehicle;
    }


    @JsonCreator
    private VehicleCase(JsonNode jsonNode) throws IOException {
        this.vehicle = CoreHelper.deserializeArray(jsonNode, Vehicle[].class);
    }

    @Override
    public String toString() {
        return vehicle.toString();
    }
}
