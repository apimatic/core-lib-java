package apimatic.core_lib.models;

import java.io.IOException;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.apimatic.core_lib.annotations.TypeCombinator.TypeCombinatorCase;
import io.apimatic.core_lib.utilities.CoreHelper;
/**
 * This is a implementation class for AtomCase.
 */
@JsonDeserialize(using = JsonDeserializer.None.class)
@TypeCombinatorCase
public class AtomCase {

    @JsonValue
    private Map<String, Atom> atom;

    AtomCase(Map<String, Atom> atom) {
        this.atom = atom;
    }
    
    @JsonCreator
    private AtomCase(JsonNode jsonNode) throws IOException {
        this.atom = CoreHelper.deserialize(jsonNode,
            new TypeReference<Map<String, Atom>>(){});
    }

    @Override
    public String toString() {
        return atom.toString();
    }
}