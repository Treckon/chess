package ServerImpl.Services;

import ServerImpl.DAO.AuthDAOImpl;
import ServerImpl.DAO.GameDAOImpl;
import ServerImpl.Requests.JoinGameRequest;
import ServerImpl.Responses.CreateGameResponse;
import ServerImpl.Responses.JoinGameResponse;
import dataAccess.DataAccessException;

/**
 * Deals with requests to add users to games.
 */
public class JoinGameService {
    /**
     * Takes in a color and gameId, then calls GameDAO with this information.
     *
     * @param joinGameRequest contains the color and gameId for the requesting user.
     */
    public JoinGameResponse joinGame(JoinGameRequest joinGameRequest){
        AuthDAOImpl auths = new AuthDAOImpl();

        if(joinGameRequest.getToken() == null){
            return new JoinGameResponse("Error: unauthorized");
        }

        try{
            auths.verifyAuthToken(joinGameRequest.getToken().getToken());
        } catch(DataAccessException e){
            return new JoinGameResponse(e.getMessage());
        }

        GameDAOImpl games = new GameDAOImpl();
        try{
            games.joinGame(joinGameRequest.getPlayerColor(),joinGameRequest.getToken().getUsername(),joinGameRequest.getGameID());
        } catch(DataAccessException e){
            return new JoinGameResponse(e.getMessage());
        }


        return new JoinGameResponse();
    }
}
