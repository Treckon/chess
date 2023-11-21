package Requests;

import Objects.AuthToken;

/**
 * Request class for joining a Game.
 *
 * currentToken - AuthToken of current User
 * color - Color the user will be joining as
 * gameID - Id of game the user will be joining
 */
public class JoinGameRequest implements Request{
    private AuthToken token;
    private String playerColor;
    private Integer gameID;

    /**
     * currentToken - assigned a value by the current AuthDAOImpl
     * @param c - color team the user is joining
     * @param Id - id of game the user is joining.
     */
    public JoinGameRequest(String c, Integer Id, AuthToken t){
        token = t;
        playerColor = c;
        gameID = Id;
    }

    public String getPlayerColor(){ return playerColor; }

    public Integer getGameID(){return gameID;}

    public AuthToken getToken(){return token;}

    public void setToken(AuthToken t){
        token = t;
    }
}
