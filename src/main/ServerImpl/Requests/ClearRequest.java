package ServerImpl.Requests;

import ServerImpl.Objects.AuthToken;

/**
 * Request class for clearing all data.
 */
public class ClearRequest implements Request{
    private AuthToken token;
    public ClearRequest(AuthToken t){
        token = t;
    }
}
