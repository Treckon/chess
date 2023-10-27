package ServerImpl.Services;

import ServerImpl.DAO.AuthDAOImpl;
import ServerImpl.DAO.GameDAOImpl;
import ServerImpl.DAO.UserDAO;
import ServerImpl.DAO.UserDAOImpl;
import ServerImpl.Objects.AuthToken;
import ServerImpl.Objects.AuthTokenImpl;
import ServerImpl.Objects.UserImpl;
import ServerImpl.Requests.RegisterRequest;
import ServerImpl.Requests.Request;
import ServerImpl.Responses.RegisterResponse;
import dataAccess.DataAccessException;

/**
 * Deals with requests to register users.
 */

public class RegisterService {
    /**
     * Receives username, password, and email from RegisterHandler. Checks if the username already
     * exists in the UserDAO, then adds the information to UserDAO and returns an AuthToken for it.
     *
     * @param registerRequest request containing username, email, and password of the user to be added.
     *
     * @return Response with AuthToken for the new user that has also been logged in.
     */

    public RegisterResponse newUser(RegisterRequest registerRequest){
        if(registerRequest.getUsername() == null || registerRequest.getEmail() == null || registerRequest.getPassword() == null){
            return new RegisterResponse("Error: bad request");
        }

        UserImpl user = new UserImpl(registerRequest.getUsername(), registerRequest.getPassword(), registerRequest.getEmail());

        UserDAOImpl users = new UserDAOImpl();
        AuthDAOImpl auths = new AuthDAOImpl();

        try{
            users.newUser(user, registerRequest.getUsername());
        } catch(DataAccessException e){
            return new RegisterResponse(e.getMessage());
        }

        AuthToken a;
        try{
            a = auths.login(registerRequest.getUsername());
        }catch(DataAccessException c){
            return new RegisterResponse(c.getMessage());
        }

        return new RegisterResponse(registerRequest.getUsername(), a.getToken());
    }
}
