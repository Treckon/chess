package chess;

import java.util.Collection;
import java.util.Objects;

public abstract class ChessPieceImpl implements ChessPiece{
    private ChessGame.TeamColor color;
    private PieceType type;



    public ChessPieceImpl(ChessGame.TeamColor c, PieceType t){
        color = c;
        type = t;
    }

    @Override
    public ChessGame.TeamColor getTeamColor() {
        return color;
    }

    @Override
    public PieceType getPieceType() {
        return type;
    }

    @Override
    public abstract Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPieceImpl that = (ChessPieceImpl) o;
        return color == that.color && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, type);
    }
}
