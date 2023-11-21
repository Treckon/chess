package chess;

import chess.pieces.King;
import chess.pieces.Pawn;
import chess.pieces.Queen;
import chess.pieces.Knight;
import chess.pieces.Bishop;
import chess.pieces.Rook;

import java.util.HashMap;
import java.util.Map;

public class ChessBoardImpl implements ChessBoard{

    private Map<ChessPosition,ChessPiece> board;

    public ChessBoardImpl(){
        board = new HashMap<ChessPosition, ChessPiece>();
    }
    @Override
    public void addPiece(ChessPosition position, ChessPiece piece) {
        board.put(position,piece);
    }

    @Override
    public ChessPiece getPiece(ChessPosition position) {
        return board.get(position);
    }

    @Override
    public void resetBoard() {
        if(board != null){
            board.clear();
        }
        else{
            board = new HashMap<ChessPosition, ChessPiece>();
        }

        setPawns();
        setKings();
        setQueens();
        setKnights();
        setBishops();
        setRooks();

    }

    public void movePiece(ChessMove move){
        ChessPiece piece = board.get(move.getStartPosition());
        if(piece == null){
            return;
        }
        board.remove(move.getStartPosition());

        ChessPiece enemyPiece = board.get(move.getEndPosition());
        if(enemyPiece != null){
            board.remove(move.getEndPosition());
        }

        if(piece.getPieceType() == ChessPiece.PieceType.PAWN && move.getPromotionPiece() != null){
            ChessGame.TeamColor color = piece.getTeamColor();
            if((color == ChessGame.TeamColor.WHITE && move.getEndPosition().getRow() == 8) || (color == ChessGame.TeamColor.BLACK && move.getEndPosition().getRow() == 1)){
                ChessPiece promotionPiece = null;
                if(move.getPromotionPiece() == ChessPiece.PieceType.QUEEN){
                    promotionPiece = new Queen(color);
                }
                else if(move.getPromotionPiece() == ChessPiece.PieceType.KNIGHT){
                    promotionPiece = new Knight(color);
                }
                else if(move.getPromotionPiece() == ChessPiece.PieceType.BISHOP){
                    promotionPiece = new Bishop(color);
                }
                else if(move.getPromotionPiece() == ChessPiece.PieceType.ROOK){
                    promotionPiece = new Rook(color);
                }
                board.put(move.getEndPosition(),promotionPiece);
            }
        }
        else{
            board.put(move.getEndPosition(),piece);
        }

    }




    public void setPawns(){
        for(int i = 1; i < 9; i++){
            board.put(new ChessPositionImpl(2,i), new Pawn(ChessGame.TeamColor.WHITE));
            //board[1][i] = new ChessPieceImpl(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        }
        for(int i = 1; i < 9; i++){
            board.put(new ChessPositionImpl(7,i), new Pawn(ChessGame.TeamColor.BLACK));
            //board[6][i] = new ChessPieceImpl(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        }
    }
    public void setKings(){
        board.put(new ChessPositionImpl(1,5), new King(ChessGame.TeamColor.WHITE));
        board.put(new ChessPositionImpl(8,5), new King(ChessGame.TeamColor.BLACK));

        //board[7][4] = new ChessPieceImpl(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);
        //board[0][3] = new ChessPieceImpl(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
    }

    public void setQueens(){
        board.put(new ChessPositionImpl(1,4), new Queen(ChessGame.TeamColor.WHITE));
        board.put(new ChessPositionImpl(8,4), new Queen(ChessGame.TeamColor.BLACK));

        //board[7][3] = new ChessPieceImpl(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
        //board[0][4] = new ChessPieceImpl(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
    }

    public void setBishops(){
        board.put(new ChessPositionImpl(1,3), new Bishop(ChessGame.TeamColor.WHITE));
        board.put(new ChessPositionImpl(1,6), new Bishop(ChessGame.TeamColor.WHITE));
        board.put(new ChessPositionImpl(8,3), new Bishop(ChessGame.TeamColor.BLACK));
        board.put(new ChessPositionImpl(8,6), new Bishop(ChessGame.TeamColor.BLACK));

        //board[7][1] = new ChessPieceImpl(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        //board[7][6] = new ChessPieceImpl(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        //board[0][1] = new ChessPieceImpl(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        //board[0][6] = new ChessPieceImpl(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
    }

    public void setKnights(){
        board.put(new ChessPositionImpl(1,2), new Knight(ChessGame.TeamColor.WHITE));
        board.put(new ChessPositionImpl(1,7), new Knight(ChessGame.TeamColor.WHITE));
        board.put(new ChessPositionImpl(8,2), new Knight(ChessGame.TeamColor.BLACK));
        board.put(new ChessPositionImpl(8,7), new Knight(ChessGame.TeamColor.BLACK));

        //board[7][2] = new ChessPieceImpl(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        //board[7][5] = new ChessPieceImpl(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        //board[0][2] = new ChessPieceImpl(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        //board[0][5] = new ChessPieceImpl(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
    }


    public void setRooks(){
        board.put(new ChessPositionImpl(1,1), new Rook(ChessGame.TeamColor.WHITE));
        board.put(new ChessPositionImpl(1,8), new Rook(ChessGame.TeamColor.WHITE));
        board.put(new ChessPositionImpl(8,1), new Rook(ChessGame.TeamColor.BLACK));
        board.put(new ChessPositionImpl(8,8), new Rook(ChessGame.TeamColor.BLACK));

        //board[7][0] = new ChessPieceImpl(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        //board[7][7] = new ChessPieceImpl(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        //board[0][0] = new ChessPieceImpl(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        //board[0][7] = new ChessPieceImpl(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
    }
}
