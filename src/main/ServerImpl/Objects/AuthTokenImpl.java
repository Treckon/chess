package ServerImpl.Objects;

import ServerImpl.Objects.AuthToken;

public class AuthTokenImpl implements AuthToken {
    private String authToken;
    private String username;
    public AuthTokenImpl(String user, String token){
        username = user;
        authToken = token;
    }

    @Override
    public String getToken() {
        return authToken;
    }

    @Override
    public String getUsername() {
        return username;
    }
}
