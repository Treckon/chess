package ServerImpl.DAO;

import Objects.AuthToken;
import Objects.User;
import dataAccess.DataAccessException;

/**
 * This class holds a list of registered users
 */
public interface UserDAO {
    /**
     * Takes in a username, password, and email, then adds a user with that information to the list.
     *
     * @param username username of user to add.
     * @param user object to place in map
     */
    public void newUser(User user, String username) throws DataAccessException;
    /**
     * Checks list to see if username already exists, if so returning the user. If not, returns null.
     * @param username username of user to search for.
     *
     * @return user found with current username
     */
    public User getUser(String username) throws DataAccessException;

    /**
     * Takes in a username, password and determines if the pair exists in the User list.
     *
     * @param username username of user attempting to log in.
     * @param password password of user attempting to log in.
     *
     * @return true if username/password pair found. False otherwise
     */
    public AuthToken loginUser(String username, String password) throws DataAccessException;

    /**
     * Clears the list of Users from the system.
     *
     */
    public void clear() throws DataAccessException;
}
