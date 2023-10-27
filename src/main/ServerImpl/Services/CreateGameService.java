package ServerImpl.Services;

import ServerImpl.DAO.AuthDAOImpl;
import ServerImpl.DAO.GameDAO;
import ServerImpl.DAO.GameDAOImpl;
import ServerImpl.Requests.CreateGameRequest;
import ServerImpl.Requests.Request;
import ServerImpl.Responses.CreateGameResponse;
import ServerImpl.Responses.Response;
import dataAccess.DataAccessException;

/**
 * Deals with requests to create games.
 */
public class CreateGameService {
    /**
     * Takes in a name for the game and calls GameDAO to create a new game with the name.
     * @param createGameRequest contains name for the game to be created.
     *
     * @return Response with Id of created game.
     */

    public CreateGameResponse createGame(CreateGameRequest createGameRequest){
        AuthDAOImpl auths = new AuthDAOImpl();
        if(createGameRequest.getCurrentToken() == null){
            return new CreateGameResponse("Error: unauthorized");
        }

        try{
            auths.verifyAuthToken(createGameRequest.getCurrentToken().getToken());
        } catch(DataAccessException e){
            return new CreateGameResponse(e.getMessage());
        }

        GameDAOImpl games = new GameDAOImpl();
        Integer Id;
        try{
            Id = games.createGame(createGameRequest.getName());
        }catch(DataAccessException e){
            return new CreateGameResponse(e.getMessage());
        }
        return new CreateGameResponse(Id);
    }
}
