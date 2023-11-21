package ServerImpl.Services;


import ServerImpl.DAO.AuthDAOImpl;
import ServerImpl.DAO.GameDAOImpl;
import ServerImpl.DAO.UserDAOImpl;
import Responses.ClearResponse;
import dataAccess.DataAccessException;

/**
 * Deals with requests to clear all data.
 */
public class ClearService {
    /**
     * Calls the clear functions on UserDAO, AuthDAO, and GameDAO.
     *
     */
    public ClearResponse clear(){
        AuthDAOImpl auths = new AuthDAOImpl();

        try{
            auths.clear();
        } catch(DataAccessException e){
            return new ClearResponse(e.getMessage());
        }

        GameDAOImpl games = new GameDAOImpl();
        try{
            games.clear();
        } catch(DataAccessException e){
            return new ClearResponse(e.getMessage());
        }

        UserDAOImpl users = new UserDAOImpl();
        try{
            users.clear();
        } catch(DataAccessException e){
            return new ClearResponse(e.getMessage());
        }


        return new ClearResponse();
    }
}
