package dataAccess;

import ServerImpl.Objects.GameImpl;
import chess.ChessPiece;
import chess.ChessPieceImpl;
import chess.pieces.*;
import com.google.gson.*;

import java.lang.reflect.Type;

public class GameDeserializer implements JsonDeserializer<ChessPiece> {

    @Override
    public ChessPiece deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        ChessPieceImpl piece = new Gson().fromJson(jsonElement, ChessPieceImpl.class);

        String pieceType = piece.getPieceType().toString();

        switch (pieceType) {
            case "KING":
                return new Gson().fromJson(jsonElement, King.class);
            case "QUEEN":
                return new Gson().fromJson(jsonElement, Queen.class);
            case "PAWN":
                return new Gson().fromJson(jsonElement, Pawn.class);
            case "KNIGHT":
                return new Gson().fromJson(jsonElement, Knight.class);
            case "BISHOP":
                return new Gson().fromJson(jsonElement, Bishop.class);
            case "ROOK":
                return new Gson().fromJson(jsonElement, Rook.class);        }



        return null;
    }
}
