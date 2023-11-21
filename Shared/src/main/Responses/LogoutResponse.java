package Responses;

/**
 * Contains response information from logging-out user.
 *
 * message - return message if something went wrong.
 */
public class LogoutResponse implements Response{
    private String message;
    public LogoutResponse(){}
    /**
     * Response if something went wrong.
     * @param m - returned Error message
     */
    public LogoutResponse(String m){
        message = m;
    }
    @Override
    public String getMessage() {
        return message;
    }
}
