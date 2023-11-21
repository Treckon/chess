package ServerImpl.Services;

import ServerImpl.DAO.AuthDAOImpl;
import ServerImpl.DAO.GameDAOImpl;
import Objects.Game;
import Requests.ListGamesRequest;
import Responses.ListGamesResponse;
import dataAccess.DataAccessException;

import java.util.ArrayList;


/**
 * Deals with requests to list all games.
 */
public class ListGameService {
    /**
     * Calls the GameDAO to return a list of GameIds, then returns the list.
     * @param listRequest only requires the current user's authToken.
     *
     * @return List of current Games.
     */
    public ListGamesResponse listGames(ListGamesRequest listRequest){
        AuthDAOImpl dao = new AuthDAOImpl();
        if(listRequest.getCurrentToken() == null){
            return new ListGamesResponse("Error: unauthorized");
        }
        try{
            dao.verifyAuthToken(listRequest.getCurrentToken().getToken());
        }catch (DataAccessException e){
            return new ListGamesResponse(e.getMessage());
        }

        GameDAOImpl games = new GameDAOImpl();
        ArrayList<Game> listGames;
        try{
            listGames = games.listGames();
        }catch(DataAccessException e){
            return new ListGamesResponse(e.getMessage());
        }
        return new ListGamesResponse(listGames);
    }
}
