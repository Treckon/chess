package ServerImpl.Objects;

import chess.ChessGame;
import chess.ChessGameImpl;
import dataAccess.DataAccessException;

import java.util.Objects;

public class GameImpl implements Game {
    private int gameID;
    private String whiteUsername;
    private String blackUsername;
    private String gameName;
    private ChessGame implementation;

    public GameImpl(Integer ID, String name){
        gameID = ID;
        gameName = name;
        implementation = new ChessGameImpl();
    }

    @Override
    public int getId() {
        return gameID;
    }

    @Override
    public String getName() {
        return gameName;
    }

    @Override
    public void joinGame(String color, String username) throws DataAccessException {
        if(color.equals("WHITE")){
            if(whiteUsername == null){
                whiteUsername = username;
            }
            else {
                throw new DataAccessException("Error: already taken");
            }
        } else if(color.equals("BLACK")){
            if(blackUsername == null){
                blackUsername = username;
            }
            else {
                throw new DataAccessException("Error: already taken");
            }
        }

    }

    public String getWhiteUsername(){return whiteUsername;}
    public String getBlackUsername(){return blackUsername;}


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameImpl game = (GameImpl) o;
        return gameID == game.gameID && Objects.equals(getWhiteUsername(), game.getWhiteUsername()) && Objects.equals(getBlackUsername(), game.getBlackUsername()) && Objects.equals(gameName, game.gameName) && Objects.equals(implementation, game.implementation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameID, getWhiteUsername(), getBlackUsername(), gameName, implementation);
    }
}
