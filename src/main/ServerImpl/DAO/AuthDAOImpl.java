package ServerImpl.DAO;

import Objects.AuthToken;
import Objects.AuthTokenImpl;
import dataAccess.DataAccessException;
import dataAccess.Database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AuthDAOImpl implements AuthDAO {
    private static Map<String, AuthToken> tokens = new HashMap();
    private static AuthTokenImpl currentToken;
    @Override
    public AuthToken login(String username) throws DataAccessException {
        if(username == null || username.isEmpty()){
            throw new DataAccessException("Error: Bad Request");
        }
        String token = generateRandomToken();
        currentToken = new AuthTokenImpl(username, token);

        Database db = new Database();
        Connection conn = null;

        try {
            conn = db.getConnection();
            var preparedStatement = conn.prepareStatement("INSERT INTO AuthDAO (authtoken, username) VALUES(?, ?)");
            preparedStatement.setString(1, token);
            preparedStatement.setString(2, username);

            preparedStatement.execute();
        } catch (SQLException ex) {
            throw new DataAccessException(ex.toString());
        }finally{
            db.closeConnection(conn);
        }


        tokens.put(currentToken.getToken(), currentToken);

        return currentToken;
    }

    @Override
    public void logout(String token) throws DataAccessException{
        if(currentToken == null){
            throw new DataAccessException("Error: unauthorized");
        }

        Database db = new Database();
        Connection conn = null;

        try {
            conn = db.getConnection();
            var preparedStatement = conn.prepareStatement("DELETE FROM AuthDAO WHERE authtoken=?");
            preparedStatement.setString(1, token);

            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException(ex.toString());
        }finally{
            db.closeConnection(conn);
        }


        tokens.remove(currentToken.getToken());
        currentToken = null;
    }

    public Boolean verifyAuthToken(String token) throws DataAccessException{
        if(token == null){
            throw new DataAccessException("Error: unauthorized");
        }

        Database db = new Database();
        Connection conn = null;
        Boolean verified = false;

        try {
            conn = db.getConnection();
            var preparedStatement = conn.prepareStatement("SELECT authtoken, username FROM AuthDAO WHERE authtoken=?");
            preparedStatement.setString(1, token);

            try(var rs = preparedStatement.executeQuery()){
                while (rs.next()){
                    var auth = rs.getString("authtoken");
                    if(auth.equals(token)){
                        verified = true;
                    }
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(ex.toString());
        }finally{
            db.closeConnection(conn);
        }

        if(verified){
            return true;
        }
        return false;
    }

    public AuthToken getCurrentToken() throws DataAccessException{
        return currentToken;
    }

    @Override
    public void clear() throws DataAccessException{
        Database db = new Database();
        Connection conn = null;

        try {
            conn = db.getConnection();
            var preparedStatement = conn.prepareStatement("TRUNCATE AuthDAO");
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException(ex.toString());
        }finally{
            db.closeConnection(conn);
        }


        Integer count = 0;
        try {
            conn = db.getConnection();
            var preparedStatement = conn.prepareStatement("SELECT authtoken FROM AuthDAO");
            try(var rs = preparedStatement.executeQuery()){
                while (rs.next()){
                    count++;
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(ex.toString());
        }finally{
            db.closeConnection(conn);
        }

        if(count != 0){
            throw new DataAccessException("Error: GameDAO not cleared");
        }

        tokens.clear();
        currentToken = null;

        if(tokens.size() != 0 || currentToken != null){
            throw new DataAccessException("Error: AuthDAO not cleared");
        }
    }

    public  String generateRandomToken() throws DataAccessException {
        String token = UUID.randomUUID().toString();

        Boolean exists = verifyAuthToken(token);

        if(exists){
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
