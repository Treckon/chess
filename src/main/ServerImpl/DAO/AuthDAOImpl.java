package ServerImpl.DAO;

import ServerImpl.Objects.AuthToken;
import ServerImpl.Objects.AuthTokenImpl;
import dataAccess.DataAccessException;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class AuthDAOImpl implements AuthDAO {
    private static Map<String, AuthToken> tokens = new HashMap();
    private static AuthTokenImpl currentToken;
    @Override
    public AuthToken login(String username) throws DataAccessException {

        currentToken = new AuthTokenImpl(username, generateRandomToken());
        tokens.put(currentToken.getToken(), currentToken);

        return currentToken;
    }

    @Override
    public void logout() throws DataAccessException{
        if(currentToken == null){
            throw new DataAccessException("Error: unauthorized");
        }

        tokens.remove(currentToken.getToken());
        currentToken = null;
    }

    public Boolean verifyAuthToken(String token) throws DataAccessException{
        if(token == null){
            throw new DataAccessException("Error: unauthorized");
        }

        if(tokens.get(token) != null){
            return true;
        }
        throw new DataAccessException("Error: unauthorized");
    }

    public AuthToken getCurrentToken() throws DataAccessException{
        return currentToken;
    }

    @Override
    public void clear() throws DataAccessException{

        tokens.clear();
        currentToken = null;

        if(tokens.size() != 0 || currentToken != null){
            throw new DataAccessException("Error: AuthDAO not cleared");
        }
    }

    public  String generateRandomToken(){
        String token = UUID.randomUUID().toString();
        if(tokens.get(token) != null){
            token = generateRandomToken();
        }

        return token;
    }

    public String getUser(){
        return currentToken.getUsername();
    }
    public String getUserofAuth(String auth){
        return tokens.get(auth).getUsername();
    }
}
