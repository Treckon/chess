package ServerImpl.Objects;

import ServerImpl.Objects.AuthToken;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthTokenImpl authToken1 = (AuthTokenImpl) o;
        return Objects.equals(authToken, authToken1.authToken) && Objects.equals(getUsername(), authToken1.getUsername());
    }

    @Override
    public int hashCode() {
        return Objects.hash(authToken, getUsername());
    }
}
