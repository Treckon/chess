package ServerImpl.Services;

import ServerImpl.DAO.UserDAOImpl;
import Objects.AuthToken;
import Requests.LoginRequest;
import Responses.LoginResponse;
import dataAccess.DataAccessException;


/**
 * Deals with requests to log in users.
 */
public class LoginService {
    /**
     * Takes in a request with a username and password, then calls UserDAO to see if the username and password
     * match, creating an authtoken for the user if so.
     * @param loginRequest
     *
     * @return Response with AuthToken for the logged in user.
     */

    public LoginResponse login(LoginRequest loginRequest){
        UserDAOImpl users = new UserDAOImpl();
        AuthToken auth;
        try{
            auth = users.loginUser(loginRequest.getUsername(), loginRequest.getPassword());
        }catch(DataAccessException e){
            return new LoginResponse(e.getMessage());
        }
        return new LoginResponse(loginRequest.getUsername(),auth.getToken());
    }
}
