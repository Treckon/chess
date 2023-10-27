package ServerImpl.Requests;

import ServerImpl.DAO.AuthDAOImpl;
import ServerImpl.Objects.AuthToken;
import dataAccess.DataAccessException;

/**
 * Request class for containing info to log in users.
 *
 * currentToken - Current AuthToken that is logged in.
 */
public class LogoutRequest implements Request{
    private AuthToken currentToken;

    /**
     * currentToken - assigned a value by the current AuthDAOImpl
     */
    public LogoutRequest(AuthToken token){
        currentToken = token;
    }

    public AuthToken getCurrentToken(){return currentToken;}
}
