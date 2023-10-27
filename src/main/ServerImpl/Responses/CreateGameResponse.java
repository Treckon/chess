package ServerImpl.Responses;

import ServerImpl.Objects.Game;

import java.util.ArrayList;
/**
 * Contains response information from creating game.
 *
 * gameID - Id of game that was created
 *
 * message - return message if something went wrong.
 */
public class CreateGameResponse implements Response{
    private Integer gameID;
    private String message;

    /**
     * Response if everything went correctly.
     * @param Id - Id of game created.
     */
    public CreateGameResponse(Integer Id){
        gameID = Id;
    }
    /**
     * Response if something went wrong.
     * @param m - returned Error message
     */
    public CreateGameResponse(String m){
        message = m;
    }
    @Override
    public String getMessage() {
        return message;
    }

    public Integer getGameID(){return gameID;}
}
