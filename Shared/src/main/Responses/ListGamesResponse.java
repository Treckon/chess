package Responses;

import Objects.Game;
import Objects.GameImpl;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Contains response information from listing games.
 *
 * games - list of games saved in the system.
 *
 * message - return message if something went wrong.
 */
public class ListGamesResponse implements Response{
    private ArrayList<GameImpl> games;
    private String message;

    /**
     * Response if everything went correctly.
     * @param g - list of current games in system.
     */
    public ListGamesResponse(ArrayList<GameImpl> g){
        games = g;
    }
    /**
     * Response if something went wrong.
     * @param m - returned Error message
     */
    public ListGamesResponse(String m){
        message = m;
    }
    @Override
    public String getMessage() {
        return message;
    }

    public ArrayList<GameImpl> getList(){return games;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ListGamesResponse that = (ListGamesResponse) o;
        return Objects.equals(games, that.games) && Objects.equals(getMessage(), that.getMessage());
    }

    @Override
    public int hashCode() {
        return Objects.hash(games, getMessage());
    }
}
