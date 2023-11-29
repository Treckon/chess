package deserializers;

import chess.*;
import com.google.gson.*;

import java.lang.reflect.Type;

public class BoardDeserializer implements JsonDeserializer<ChessBoardImpl> {
    @Override
    public ChessBoardImpl deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        ChessBoardImpl board;


        var builder = new GsonBuilder();
        builder.registerTypeAdapter(ChessPieceImpl.class, new GameDeserializer());
        builder.registerTypeAdapter(ChessPositionImpl.class, new PositionDeserializer());

        board = builder.create().fromJson(jsonElement,ChessBoardImpl.class);

        return board;
    }
}
