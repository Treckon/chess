package Requests;


import Objects.AuthToken;
import dataAccess.DataAccessException;

/**
 * Request class for querying current Games.
 *
 * currentToken - Current AuthToken that is logged in.
 */
public class ListGamesRequest implements Request{

    private AuthToken currentToken;

    /**
     * currentToken - assigned a value by the current AuthDAOImpl
     */
    public ListGamesRequest(AuthToken token){
        currentToken = token;
    }

    public AuthToken getCurrentToken(){return currentToken;}

}
