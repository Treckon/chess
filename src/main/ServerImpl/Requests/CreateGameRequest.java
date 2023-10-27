package ServerImpl.Requests;

import ServerImpl.DAO.AuthDAOImpl;
import ServerImpl.Objects.AuthToken;
import ServerImpl.Objects.AuthTokenImpl;

/**
 * Request class for creating a new Game.
 *
 * currentToken - Current AuthToken that is logged in.
 * gameName - name for the new game
 */
public class CreateGameRequest implements Request{
    private AuthToken currentToken;
    private String gameName;

    /**
     * currentToken - assigned a value by the current AuthDAOImpl
     * @param name - name to be assigned to game.
     */
    public CreateGameRequest(String name, AuthToken token){
        currentToken = token;
        gameName = name;
    }

    public String getName(){return gameName;}
    public AuthToken getCurrentToken(){return currentToken;}

    public void setAuthToken(AuthToken t){
        currentToken = t;
    }
}
