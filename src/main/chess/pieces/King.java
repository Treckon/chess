package chess.pieces;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

public class King extends ChessPieceImpl {
    private ChessGame.TeamColor color;

    public King(ChessGame.TeamColor c){
        super(c, PieceType.KING);
        color = c;
    }
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<ChessMove>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();

        checkPosition(row,col,1,1,board,moves,myPosition);
        checkPosition(row,col,1,0,board,moves,myPosition);
        checkPosition(row,col,1,-1,board,moves,myPosition);
        checkPosition(row,col,0,1,board,moves,myPosition);
        checkPosition(row,col,0,-1,board,moves,myPosition);
        checkPosition(row,col,-1,1,board,moves,myPosition);
        checkPosition(row,col,-1,0,board,moves,myPosition);
        checkPosition(row,col,-1,-1,board,moves,myPosition);
        return moves;
    }

    public void checkPosition(int row, int col, int rowDif, int colDif, ChessBoard board, Collection<ChessMove> moves, ChessPosition myPosition){
        if(row+rowDif > 0 && row+rowDif < 9 && col+colDif > 0 && col+colDif < 9) {
            ChessPiece otherPiece = board.getPiece(new ChessPositionImpl(row + rowDif, col + colDif));
            if (otherPiece == null) {
                moves.add(new ChessMoveImpl(myPosition, new ChessPositionImpl(row + rowDif, col + colDif)));
            } else if (otherPiece.getTeamColor() != color) {
                moves.add(new ChessMoveImpl(myPosition, new ChessPositionImpl(row + rowDif, col + colDif)));
            }
        }
    }
}
