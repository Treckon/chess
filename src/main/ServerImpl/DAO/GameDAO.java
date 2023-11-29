package ServerImpl.DAO;

import Objects.Game;
import Objects.GameImpl;
import chess.ChessGameImpl;
import dataAccess.DataAccessException;

import java.util.ArrayList;
/**
 * This class holds a list of active games in the system.
 */

public interface GameDAO {
    /**
     * Returns a list of current gameIds.
     *
     * @return list of gameIds
     */
    public ArrayList<GameImpl> listGames() throws DataAccessException;
    /**
     * Takes in a String name and creates a game with that name, adding it to the list.
     * @param name name of game to be created.
     *
     * @return game object for current game
     */
    public Integer createGame(String name) throws DataAccessException;
    /**
     * Takes in a color and GameId, searches for the desired game, and calls joinGame on the Game object.
     *
     * @param color color of user to be added.
     * @param GameId game that the user is joining.
     */
    public void joinGame(String color, String username, Integer GameId) throws DataAccessException;

    /**
     * Takes in an int gameId and searches the list of games for a game with that Id.
     * @param gameId id of game to be searchec for.
     *
     * @return game object for game found, otherwise null.
     */

    public ChessGameImpl findGame(Integer gameId) throws DataAccessException;

    /**
     * Update current game state based on the game state
     * passed to the function.
     *
     * @param newMove - Game state to replace the current game.
     * @param gameId - Id of game to be updated.
     */
    public void updateGame(Game newMove, Integer gameId) throws DataAccessException;
    /**
     * Remove game based on Id passed into the function.
     *
     * @param gameId - Id of game to be removed.
     */
    public void removeGame(Integer gameId) throws DataAccessException;

    /**
     * Clears the list of Games from the system.
     *
     */
    public void clear() throws DataAccessException;
}
