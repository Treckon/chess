package ServerImpl.DAO;

import Objects.AuthToken;
import dataAccess.DataAccessException;

/**
 * This class holds a list of AuthTokens with their designated Users
 */
public interface AuthDAO {

    /**
     * Takes in a username and creates an Authtoken, adding the pair of values to the list.
     * @param username username of user to be logged in.
     *
     * @return AuthToken for logged in User.
     */
    public AuthToken login(String username) throws DataAccessException;
    /**
     * Removes current Authtoken/username pair from the AuthDAO
     *
     */
    public void logout(String token) throws DataAccessException;

    /**
     * checks authtoken and if it exists, makes it the current authtoken
     *
     * @return - true or false depending on if authToken exists.
     */
    public Boolean verifyAuthToken(String token) throws DataAccessException;

    /**
     * return current AuthToken that is logged in.
     */
    public AuthToken getCurrentToken() throws DataAccessException;

    /**
     * Clears the list of AuthTokens from the system.
     *
     */
    public void clear() throws DataAccessException;
}
