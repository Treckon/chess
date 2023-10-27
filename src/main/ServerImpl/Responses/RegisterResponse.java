package ServerImpl.Responses;

/**
 * Contains response information from registering user.
 *
 * username - username of registered user
 * token - resulting token from logged-in user.
 *
 * message - return message if something went wrong.
 */
public class RegisterResponse implements Response {
    private String username;
    private String authToken;
    private String message;

    /**
     * Response if everything went correctly.
     * @param u - logged in username
     * @param a - logged in authToken
     */
    public RegisterResponse(String u, String a){
        username = u;
        authToken = a;
    }

    /**
     * Response if something went wrong.
     * @param m - returned Error message
     */
    public RegisterResponse(String m){
        message = m;
    }
    @Override
    public String getMessage() {
        return message;
    }

    public String getUsername(){return username;}

    public String getAuthToken(){return authToken;}
}
