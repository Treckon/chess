package ServerImpl.Services;

import ServerImpl.DAO.AuthDAOImpl;
import ServerImpl.Requests.LogoutRequest;
import ServerImpl.Responses.LogoutResponse;
import dataAccess.DataAccessException;

/**
 * Deals with requests to log out users.
 */
public class LogoutService {
    /**
     * Calls the AuthDAO to log out the current user/AuthToken
     * @param logoutRequest
     *
     * @return Response with success of Logout Request.
     */
    public LogoutResponse logout(LogoutRequest logoutRequest){
        AuthDAOImpl auths = new AuthDAOImpl();
        try{
            auths.verifyAuthToken(logoutRequest.getCurrentToken().getToken());
        }catch(DataAccessException e){
            return new LogoutResponse(e.getMessage());
        }
        try{
            auths.logout();
        }catch (DataAccessException e){
            return new LogoutResponse(e.getMessage());
        }


        return new LogoutResponse();
    }
}
