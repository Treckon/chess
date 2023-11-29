package deserializers;

import Objects.GameImpl;
import chess.*;
import com.google.gson.*;

import java.lang.reflect.Type;

public class GameEntryDeserializer implements JsonDeserializer<ChessGame> {
    @Override
    public ChessGame deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        ChessGameImpl game;


        var builder = new GsonBuilder();
        builder.registerTypeAdapter(ChessPieceImpl.class, new GameDeserializer());
        builder.registerTypeAdapter(ChessPositionImpl.class, new PositionDeserializer());
        //builder.registerTypeAdapter(ChessPositionImpl.class, new BoardDeserializer());

        game = builder.create().fromJson(jsonElement,ChessGameImpl.class);
        return game;
    }
}
