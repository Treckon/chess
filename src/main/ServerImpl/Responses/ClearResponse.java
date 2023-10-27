package ServerImpl.Responses;
/**
 * Contains response information from clearing all
 * data.
 */
public class ClearResponse implements Response{
    String message;
    public ClearResponse(){}
    /**
     * Response if something went wrong.
     * @param m - returned Error message
     */
    public ClearResponse(String m){
        message = m;
    }
    @Override
    public String getMessage() {
        return message;
    }
}
