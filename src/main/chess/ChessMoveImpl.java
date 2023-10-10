package chess;

import java.util.Objects;

public class ChessMoveImpl implements ChessMove{
    private ChessPosition startPosition;
    private ChessPosition endPosition;
    private ChessPiece.PieceType promotionPiece;

    public ChessMoveImpl(ChessPosition start, ChessPosition end){
        startPosition = start;
        endPosition = end;
    }
    public ChessMoveImpl(ChessPosition start, ChessPosition end, ChessPiece.PieceType promotion){
        startPosition = start;
        endPosition = end;
        promotionPiece = promotion;
    }
    @Override
    public ChessPosition getStartPosition() {
        return startPosition;
    }

    @Override
    public ChessPosition getEndPosition() {
        return endPosition;
    }

    @Override
    public ChessPiece.PieceType getPromotionPiece() {
        return promotionPiece;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessMoveImpl chessMove = (ChessMoveImpl) o;
        return Objects.equals(getStartPosition(), chessMove.getStartPosition()) && Objects.equals(getEndPosition(), chessMove.getEndPosition()) && getPromotionPiece() == chessMove.getPromotionPiece();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStartPosition(), getEndPosition(), getPromotionPiece());
    }


}
