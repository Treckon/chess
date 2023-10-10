package chess.pieces;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

public class Knight extends ChessPieceImpl {
    private ChessGame.TeamColor color;

    public Knight(ChessGame.TeamColor c){
        super(c, PieceType.KNIGHT);
        color = c;
    }
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<ChessMove>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();


        if(row+2 > 0 && row+2 < 9){
            if(col+1 > 0 && col+1 < 9){
                checkPosition(row, col, 2, 1, board,moves, myPosition);
            }
            if(col-1 > 0 && col-1 < 9){
                checkPosition(row, col, 2, -1, board,moves, myPosition);
                //moves.add(new ChessMoveImpl(myPosition, new ChessPositionImpl(row+2, col-1)));
            }
        }

        if(row-2 > 0 && row-2 < 9){
            if(col+1 > 0 && col+1 < 9){
                checkPosition(row, col, -2, 1, board,moves, myPosition);
                //moves.add(new ChessMoveImpl(myPosition, new ChessPositionImpl(row-2, col+1)));
            }
            if(col-1 > 0 && col-1 < 9){
                checkPosition(row, col, -2, -1, board,moves, myPosition);
                //moves.add(new ChessMoveImpl(myPosition, new ChessPositionImpl(row-2, col-1)));
            }
        }

        if(col+2 > 0 && col+2 < 9){
            if(row+1 > 0 && row+1 < 9){
                checkPosition(row, col, 1, 2, board,moves, myPosition);
                //moves.add(new ChessMoveImpl(myPosition, new ChessPositionImpl(row+1, col+2)));
            }
            if(row-1 > 0 && row-1 < 9){
                checkPosition(row, col, -1, 2, board,moves, myPosition);
                //moves.add(new ChessMoveImpl(myPosition, new ChessPositionImpl(row-1, col+2)));
            }
        }

        if(col-2 > 0 && col-2 < 9){
            if(row+1 > 0 && row+1 < 9){
                checkPosition(row, col, 1, -2, board,moves, myPosition);
                //moves.add(new ChessMoveImpl(myPosition, new ChessPositionImpl(row+1, col-2)));
            }
            if(row-1 > 0 && row-1 < 9){
                checkPosition(row, col, -1, -2, board,moves, myPosition);
                //moves.add(new ChessMoveImpl(myPosition, new ChessPositionImpl(row-1, col-2)));
            }
        }

        return moves;
    }

    public void checkPosition(int row, int col, int rowDif, int colDif, ChessBoard board, Collection<ChessMove> moves, ChessPosition myPosition){
        ChessPiece otherPiece = board.getPiece(new ChessPositionImpl(row+rowDif, col+colDif));
        if(otherPiece == null){
            moves.add(new ChessMoveImpl(myPosition, new ChessPositionImpl(row+rowDif, col+colDif)));
        }
        else if(otherPiece.getTeamColor() != color){
            moves.add(new ChessMoveImpl(myPosition, new ChessPositionImpl(row+rowDif, col+colDif)));
        }
    }

}
