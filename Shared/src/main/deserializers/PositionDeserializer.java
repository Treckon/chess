package deserializers;

import chess.ChessPosition;
import chess.ChessPositionImpl;
import com.google.gson.*;

import java.lang.reflect.Type;

public class PositionDeserializer implements JsonDeserializer<ChessPosition> {
    @Override
    public ChessPosition deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return new Gson().fromJson(jsonElement, ChessPositionImpl.class);
    }
}
