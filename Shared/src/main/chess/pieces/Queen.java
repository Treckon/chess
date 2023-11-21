package chess.pieces;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

public class Queen extends ChessPieceImpl {
//    private ChessGame.TeamColor color;

    public Queen(ChessGame.TeamColor c){
        super(c, PieceType.QUEEN);
        color = c;
    }
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<ChessMove>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        int rowDif = 0;
        int colDif = 0;

        while(col+colDif < 8){
            colDif++;
            Boolean isBarrier = checkPosition(row,col,rowDif,colDif,board,moves,myPosition);
            if(isBarrier){
                colDif = 9;
            }
        }
        colDif = 0;
        while(col+colDif > 1){
            colDif--;
            Boolean isBarrier = checkPosition(row,col,rowDif,colDif,board,moves,myPosition);
            if(isBarrier){
                colDif = -9;
            }
        }
        colDif = 0;
        while(row+rowDif < 8){
            rowDif++;
            Boolean isBarrier = checkPosition(row,col,rowDif,colDif,board,moves,myPosition);
            if(isBarrier){
                rowDif = 9;
            }
        }
        rowDif = 0;
        while(row+rowDif > 1){
            rowDif--;
            Boolean isBarrier = checkPosition(row,col,rowDif,colDif,board,moves,myPosition);
            if(isBarrier){
                rowDif = -9;
            }
        }
        rowDif = 0;


        while(col+colDif < 8 && row+rowDif < 8){
            colDif++;
            rowDif++;
            Boolean isBarrier = checkPosition(row,col,rowDif,colDif,board,moves,myPosition);
            if(isBarrier){
                colDif = 9;
            }
        }
        colDif = 0;
        rowDif = 0;
        while(col+colDif > 1 && row+rowDif < 8){
            colDif--;
            rowDif++;
            Boolean isBarrier = checkPosition(row,col,rowDif,colDif,board,moves,myPosition);
            if(isBarrier){
                rowDif = 9;
            }
        }
        colDif = 0;
        rowDif = 0;
        while(col+colDif < 8 && row+rowDif > 1){
            colDif++;
            rowDif--;
            Boolean isBarrier = checkPosition(row,col,rowDif,colDif,board,moves,myPosition);
            if(isBarrier){
                colDif = 9;
            }
        }
        colDif = 0;
        rowDif = 0;
        while(col+colDif > 1 && row+rowDif > 1){
            colDif--;
            rowDif--;
            Boolean isBarrier = checkPosition(row,col,rowDif,colDif,board,moves,myPosition);
            if(isBarrier){
                rowDif = -9;
            }
        }

        return moves;
    }

    public Boolean checkPosition(int row, int col, int rowDif, int colDif, ChessBoard board, Collection<ChessMove> moves, ChessPosition myPosition){
        ChessPiece otherPiece = board.getPiece(new ChessPositionImpl(row+rowDif, col+colDif));
        if(otherPiece == null){
            moves.add(new ChessMoveImpl(myPosition, new ChessPositionImpl(row+rowDif, col+colDif)));
            return false;
        }
        else if(otherPiece.getTeamColor() != color){
            moves.add(new ChessMoveImpl(myPosition, new ChessPositionImpl(row+rowDif, col+colDif)));
        }
        return true;
    }
}
