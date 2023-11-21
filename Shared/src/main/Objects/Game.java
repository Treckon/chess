package Objects;

import dataAccess.DataAccessException;

/**
 * Object that holds a game instances with relevant information such as game name and ID
 */
public interface Game {
    /**
     * Returns current Game's Id
     */
    public int getId();
    /**
     * Returns current Game's Name
     */
    public String getName();
    /**
     * Takes in a color and username, then adds the username to the desired color,
     * if the color is not occupied already.
     *
     * @param color team color of the username
     * @param username username of user to assign to game
     */
    public void joinGame(String color, String username) throws DataAccessException;

    public String getWhiteUsername();
    public String getBlackUsername();

}
