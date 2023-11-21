package Requests;

import Objects.AuthToken;

/**
 * Request class for clearing all data.
 */
public class ClearRequest implements Request{
    private AuthToken token;
    public ClearRequest(AuthToken t){
        token = t;
    }
}
