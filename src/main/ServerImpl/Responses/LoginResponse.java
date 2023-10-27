package ServerImpl.Responses;

import ServerImpl.Objects.AuthToken;
/**
 * Contains response information from logging in user.
 *
 * username - username of user logged-in
 * token - resulting token from logged-in user.
 *
 * message - return message if something went wrong.
 */
public class LoginResponse implements Response{
    private String username;
    private String authToken;
    private String message;

    /**
     * Response if everything went correctly.
     * @param u - logged in username
     * @param a - logged in authToken
     */
    public LoginResponse(String u, String a){
        username = u;
        authToken = a;
    }
    /**
     * Response if something went wrong.
     * @param m - returned Error message
     */
    public LoginResponse(String m){
        message = m;
    }
    @Override
    public String getMessage() {
        return message;
    }

    public String getUsername(){return username;}
    public String getAuthToken(){return authToken;}
}
