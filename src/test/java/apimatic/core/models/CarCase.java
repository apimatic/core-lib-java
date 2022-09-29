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
 * This is a implementation class for CarCase.
 */
@JsonDeserialize(using = JsonDeserializer.None.class)
@TypeCombinatorCase
public class CarCase {

    @JsonValue
    private Car car;

    CarCase(Car car) {
        this.car = car;
    }



    @JsonCreator
    private CarCase(JsonNode jsonNode) throws IOException {
        this.car = CoreHelper.deserialize(jsonNode, Car.class);
    }

    @Override
    public String toString() {
        return car.toString();
    }
}
