package chess;

import chess.pieces.*;

import java.util.ArrayList;
import java.util.Collection;

public class ChessGameImpl implements ChessGame{

    private ChessBoardImpl board;
    private TeamColor teamTurn;

    public ChessGameImpl(){
        board = new ChessBoardImpl();
        //board.resetBoard();
    }
    @Override
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    @Override
    public void setTeamTurn(TeamColor team) {
        teamTurn = team;
    }

    @Override
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = board.getPiece(startPosition);
        if(piece == null){
            return null;
        }

        if(piece.getPieceType() != ChessPiece.PieceType.KING){
            Collection<ChessMove> moves = piece.pieceMoves(board,startPosition);
            moves.removeIf(move -> !safeMove(move));
            return moves;
        }
        else{
            return kingMoves(startPosition);
        }
    }

    public Boolean safeMove(ChessMove move){
        ChessBoardImpl newBoard = copyBoard();
        newBoard.movePiece(move);

        return !isInCheck(board.getPiece(move.getStartPosition()).getTeamColor(), newBoard);
    }



    @Override
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece piece = board.getPiece(move.getStartPosition());
        if(piece.getTeamColor() != teamTurn){
            throw new InvalidMoveException("Wrong Turn");
        }
        Collection<ChessMove> moves = validMoves(move.getStartPosition());

        if(!moves.contains(move)){
            throw new InvalidMoveException("Invalid Move");
        }
        else{
            board.movePiece(move);
            if(teamTurn == TeamColor.WHITE){
                teamTurn = TeamColor.BLACK;
            }
            else{
                teamTurn = TeamColor.WHITE;
            }
        }

    }

    @Override
    public boolean isInCheck(TeamColor teamColor) {
        ChessPiece kingPiece = null;
        ChessPosition kingPosition = null;
        for(int i = 1; i < 9; i++){
            for(int j = 1; j < 9; j++) {
                ChessPiece otherPiece = board.getPiece(new ChessPositionImpl(i, j));
                if(otherPiece != null && otherPiece.getPieceType() == ChessPiece.PieceType.KING && otherPiece.getTeamColor() == teamColor){
                    kingPiece = otherPiece;
                    kingPosition = new ChessPositionImpl(i,j);
                }
            }
        }
        for(int i = 1; i < 9; i++){
            for(int j = 1; j < 9; j++) {
                ChessPiece enemyPiece = board.getPiece(new ChessPositionImpl(i,j));
                if(enemyPiece != null && enemyPiece.getTeamColor() != teamColor){
                    Collection<ChessMove> enemyMoves = enemyPiece.pieceMoves(board,new ChessPositionImpl(i,j));
                    for(ChessMove move: enemyMoves){
                        if(move.getEndPosition().equals(kingPosition)){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean isInCheck(TeamColor teamColor, ChessBoard newBoard) {
        ChessPiece kingPiece;
        ChessPosition kingPosition = null;
        for(int i = 1; i < 9; i++){
            for(int j = 1; j < 9; j++) {
                ChessPiece otherPiece = newBoard.getPiece(new ChessPositionImpl(i, j));

                if(otherPiece != null && otherPiece.getPieceType() == ChessPiece.PieceType.KING && otherPiece.getTeamColor() == teamColor){
                    kingPiece = otherPiece;
                    kingPosition = new ChessPositionImpl(i,j);
                }
            }
        }
        if(kingPosition == null){
            return false;
        }
        for(int i = 1; i < 9; i++){
            for(int j = 1; j < 9; j++) {
                ChessPiece enemyPiece = newBoard.getPiece(new ChessPositionImpl(i,j));
                if(enemyPiece != null && enemyPiece.getTeamColor() != teamColor){
                    Collection<ChessMove> enemyMoves = enemyPiece.pieceMoves(newBoard,new ChessPositionImpl(i,j));
                    for(ChessMove move: enemyMoves){
                        if(move.getEndPosition().equals(kingPosition)){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean isInCheckmate(TeamColor teamColor) {
        if(!isInCheck(teamColor)){
            return false;
        }

        for(int i = 1; i < 9; i++){
            for(int j = 1; j < 9; j++) {
                ChessPiece piece = board.getPiece(new ChessPositionImpl(i, j));
                if(piece != null && piece.getTeamColor() == teamColor){

                    Collection<ChessMove> moves = validMoves(new ChessPositionImpl(i,j));

                    if (moves != null && moves.size() > 0){
                        return false;
                    }


                }
            }
        }

        if(isInCheck(teamColor)){
            return true;
        }else{
            return false;
        }
    }



    @Override
    public boolean isInStalemate(TeamColor teamColor) {

        for(int i = 1; i < 9; i++){
            for(int j = 1; j < 9; j++) {
                ChessPiece piece = board.getPiece(new ChessPositionImpl(i, j));
                if(piece != null && piece.getTeamColor() == teamColor){

                    Collection<ChessMove> moves = validMoves(new ChessPositionImpl(i,j));

                    if (moves != null && moves.size() > 0){
                        return false;
                    }

                }
            }
        }

        return true;
    }

    @Override
    public void setBoard(ChessBoard board) {
        this.board = (ChessBoardImpl) board;
    }

    @Override
    public ChessBoard getBoard() {
        return board;
    }

    public Collection<ChessMove> kingMoves(ChessPosition startPosition){
        ChessPiece king = board.getPiece(startPosition);
        Collection<ChessMove> moves = king.pieceMoves(board,startPosition);
        Collection<ChessMove> badMoves = new ArrayList<ChessMove>();

        for(ChessMove move: moves){
            ChessBoardImpl newBoard = copyBoard();
            newBoard.movePiece(move);
            if(isInCheck(king.getTeamColor(), newBoard)){
                badMoves.add(move);
            }
        }

        for(ChessMove move: badMoves){
            moves.remove(move);
        }

        if(moves.isEmpty()){
            return null;
        }
        return moves;
    }

    /*
    public Collection<ChessMove> kingMoves(ChessPosition startPosition){
        ChessPiece king = board.getPiece(startPosition);
        Collection<ChessMove> moves = king.pieceMoves(board,startPosition);
        Collection<ChessMove> badMoves = new ArrayList<ChessMove>();
        for(int i = 1; i < 9; i++){
            for(int j = 1; j < 9; j++){
                ChessPiece otherPiece = board.getPiece(new ChessPositionImpl(i,j));
                if(otherPiece != null && otherPiece.getTeamColor() != king.getTeamColor()){

                    if(otherPiece.getPieceType() != ChessPiece.PieceType.PAWN){
                        Collection<ChessMove> otherMoves = otherPiece.pieceMoves(board,new ChessPositionImpl(i,j));
                        Collection<ChessPosition> dangerSpaces = new ArrayList<ChessPosition>();
                        for(ChessMove move: otherMoves){
                            dangerSpaces.add(move.getEndPosition());
                        }
                        for(ChessMove move: moves){
                            if(dangerSpaces.contains(move.getEndPosition())){
                                if(!badMoves.contains(move)){
                                    badMoves.add(move);
                                }
                            }
                        }
                    }
                    else{
                        Collection<ChessPosition> dangerSpaces = new ArrayList<ChessPosition>();
                        if(otherPiece.getTeamColor() == TeamColor.WHITE){
                            dangerSpaces.add(new ChessPositionImpl(i+1,j+1));
                            dangerSpaces.add(new ChessPositionImpl(i+1,j-1));
                        }
                        else{
                            dangerSpaces.add(new ChessPositionImpl(i-1,j+1));
                            dangerSpaces.add(new ChessPositionImpl(i-1,j-1));
                        }

                        for(ChessMove move: moves){
                            if(dangerSpaces.contains(move.getEndPosition())){
                                if(!badMoves.contains(move)){
                                    badMoves.add(move);
                                }
                            }
                        }
                    }


                }
            }
        }

        for(ChessMove move: badMoves){
            moves.remove(move);
        }

        if(moves.size() == 0){
            return null;
        }
        return moves;
    }
     */

    public ChessBoardImpl copyBoard(){
        ChessBoardImpl newBoard = new ChessBoardImpl();
        for(int i = 1; i < 9; i++){
            for(int j = 1; j < 9; j++){
                ChessPiece piece = board.getPiece(new ChessPositionImpl(i,j));

                if(piece != null){
                    ChessPiece.PieceType type = piece.getPieceType();
                    TeamColor pieceColor = piece.getTeamColor();
                    if(type == ChessPiece.PieceType.KING){
                        newBoard.addPiece(new ChessPositionImpl(i, j), new King(pieceColor));
                    }
                    else if(type == ChessPiece.PieceType.QUEEN){
                        newBoard.addPiece(new ChessPositionImpl(i, j), new Queen(pieceColor));
                    }
                    else if(type == ChessPiece.PieceType.KNIGHT){
                        newBoard.addPiece(new ChessPositionImpl(i, j), new Knight(pieceColor));
                    }
                    else if(type == ChessPiece.PieceType.BISHOP){
                        newBoard.addPiece(new ChessPositionImpl(i, j), new Bishop(pieceColor));
                    }
                    else if(type == ChessPiece.PieceType.ROOK){
                        newBoard.addPiece(new ChessPositionImpl(i, j), new Rook(pieceColor));
                    }
                    else if(type == ChessPiece.PieceType.PAWN){
                        newBoard.addPiece(new ChessPositionImpl(i, j), new Pawn(pieceColor));
                    }

                }
            }
        }
        return newBoard;
    }
}
