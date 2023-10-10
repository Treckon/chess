package chess.pieces;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

public class Pawn extends ChessPieceImpl {
    private ChessGame.TeamColor color;
    private Boolean firstMove = true;

    public Pawn(ChessGame.TeamColor c){
        super(c, PieceType.PAWN);
        color = c;
    }
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<ChessMove>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        /*
        if(firstMove){
            checkPosition(row,col,2,0,board,moves,myPosition);
        }
        */
        if(color == ChessGame.TeamColor.BLACK && row != 2){
            checkPosition(row,col,-1,0,board,moves,myPosition);
            checkDiagonal(row,col,-1,-1,board,moves,myPosition);
            checkDiagonal(row,col,-1,1,board,moves,myPosition);
            if(row == 7){
                checkFirstMove(row,col,-1,board,moves,myPosition);
            }
        }else if(color == ChessGame.TeamColor.WHITE && row != 7){
            checkPosition(row,col,1,0,board,moves,myPosition);
            checkDiagonal(row,col,1,-1,board,moves,myPosition);
            checkDiagonal(row,col,1,1,board,moves,myPosition);
            if(row == 2){
                checkFirstMove(row,col,1,board,moves,myPosition);
            }
        }

        if(color == ChessGame.TeamColor.BLACK && row == 2){
            checkPromotion(row,col,-1,board,moves,myPosition);
        }else if(color == ChessGame.TeamColor.WHITE && row == 7) {
            checkPromotion(row,col,1,board,moves,myPosition);
        }


        return moves;
    }

    public void checkPosition(int row, int col, int rowDif, int colDif, ChessBoard board, Collection<ChessMove> moves, ChessPosition myPosition){
        ChessPiece otherPiece = board.getPiece(new ChessPositionImpl(row+rowDif, col+colDif));
        if(otherPiece == null){
            moves.add(new ChessMoveImpl(myPosition, new ChessPositionImpl(row+rowDif, col+colDif)));
        }
    }
    public void checkDiagonal(int row, int col, int rowDif, int colDif, ChessBoard board, Collection<ChessMove> moves, ChessPosition myPosition){
        ChessPiece otherPiece = board.getPiece(new ChessPositionImpl(row+rowDif, col+colDif));
        if(otherPiece != null && otherPiece.getTeamColor() != color){
            moves.add(new ChessMoveImpl(myPosition, new ChessPositionImpl(row+rowDif, col+colDif)));
        }
    }
    public void checkPromotion(int row, int col, int rowDif, ChessBoard board, Collection<ChessMove> moves, ChessPosition myPosition) {
        ChessPiece otherPiece = board.getPiece(new ChessPositionImpl(row + rowDif, col));
        if (otherPiece == null) {
            moves.add(new ChessMoveImpl(myPosition, new ChessPositionImpl(row + rowDif, col), PieceType.QUEEN));
            moves.add(new ChessMoveImpl(myPosition, new ChessPositionImpl(row + rowDif, col), PieceType.ROOK));
            moves.add(new ChessMoveImpl(myPosition, new ChessPositionImpl(row + rowDif, col), PieceType.BISHOP));
            moves.add(new ChessMoveImpl(myPosition, new ChessPositionImpl(row + rowDif, col), PieceType.KNIGHT));
        }
        ChessPiece leftDiagonal = board.getPiece(new ChessPositionImpl(row + rowDif, col-1));
        if(leftDiagonal != null){
            moves.add(new ChessMoveImpl(myPosition, new ChessPositionImpl(row + rowDif, col-1), PieceType.QUEEN));
            moves.add(new ChessMoveImpl(myPosition, new ChessPositionImpl(row + rowDif, col-1), PieceType.ROOK));
            moves.add(new ChessMoveImpl(myPosition, new ChessPositionImpl(row + rowDif, col-1), PieceType.BISHOP));
            moves.add(new ChessMoveImpl(myPosition, new ChessPositionImpl(row + rowDif, col-1), PieceType.KNIGHT));
        }
        ChessPiece rightDiagonal = board.getPiece(new ChessPositionImpl(row + rowDif, col+1));
        if(rightDiagonal != null){
            moves.add(new ChessMoveImpl(myPosition, new ChessPositionImpl(row + rowDif, col+1), PieceType.QUEEN));
            moves.add(new ChessMoveImpl(myPosition, new ChessPositionImpl(row + rowDif, col+1), PieceType.ROOK));
            moves.add(new ChessMoveImpl(myPosition, new ChessPositionImpl(row + rowDif, col+1), PieceType.BISHOP));
            moves.add(new ChessMoveImpl(myPosition, new ChessPositionImpl(row + rowDif, col+1), PieceType.KNIGHT));
        }
    }
    public void checkFirstMove(int row, int col, int rowDif, ChessBoard board, Collection<ChessMove> moves, ChessPosition myPosition){
        ChessPiece firstPiece = board.getPiece(new ChessPositionImpl(row+(rowDif), col));
        ChessPiece secondPiece = board.getPiece(new ChessPositionImpl(row+(rowDif*2), col));
        if(firstPiece == null && secondPiece == null){
            moves.add(new ChessMoveImpl(myPosition, new ChessPositionImpl(row+(rowDif*2), col)));
        }
    }
}
