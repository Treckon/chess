package Responses;
/**
 * Contains response information from game joined.
 *
 * message - return message if something went wrong.
 */
public class JoinGameResponse implements Response{
    private String message;
    public JoinGameResponse(){}
    /**
     * Response if something went wrong.
     * @param m - returned Error message
     */
    public JoinGameResponse(String m){
        message = m;
    }
    @Override
    public String getMessage() {
        return message;
    }
}
