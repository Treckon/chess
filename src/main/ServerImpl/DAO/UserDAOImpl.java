package ServerImpl.DAO;

import Objects.AuthToken;
import Objects.User;
import Objects.UserImpl;
import dataAccess.DataAccessException;
import dataAccess.Database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class UserDAOImpl implements UserDAO {
    private static Map<String, User> users = new HashMap<>();


    @Override
    public void newUser(User user, String username) throws DataAccessException {

        if(user == null || username == null || username.isEmpty()){
            throw new DataAccessException("Error: bad request");
        }

        if(users.get(username) != null){
            throw new DataAccessException("Error: already taken");
        }

        Database db = new Database();
        Connection conn = null;

        try {
            conn = db.getConnection();
            var preparedStatement = conn.prepareStatement("INSERT INTO UserDAO (username, password, email) VALUES(?, ?, ?)");
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getEmail());

            preparedStatement.execute();
        } catch (SQLException ex) {
            throw new DataAccessException(ex.toString());
        }finally{
            db.closeConnection(conn);
        }

        //Remove
        users.put(username, user);
    }

    @Override
    public User getUser(String username) throws DataAccessException {
        if(username == null || username.isEmpty()){
            throw new DataAccessException("Error: bad request");
        }

        Database db = new Database();
        Connection conn = null;
        UserImpl user = null;

        try {
            conn = db.getConnection();
            var preparedStatement = conn.prepareStatement("SELECT username, password, email FROM UserDAO WHERE username=?");
            preparedStatement.setString(1, username);

            try(var rs = preparedStatement.executeQuery()){
                while (rs.next()){
                    var un = rs.getString("username");
                    var pw = rs.getString("password");
                    var em = rs.getString("email");

                    user = new UserImpl(un,pw,em);

                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(ex.toString());
        }finally{
            db.closeConnection(conn);
        }

        return user;


        //return users.get(username);
    }

    @Override
    public AuthToken loginUser(String username, String password) throws DataAccessException {
        User user = getUser(username);

        if(user == null || !password.equals(user.getPassword())){
            throw new DataAccessException("Error: unauthorized");
        }

        AuthDAOImpl auths = new AuthDAOImpl();

        AuthToken auth = auths.login(username);


        return auth;
    }

    @Override
    public void clear() throws DataAccessException{
        Database db = new Database();
        Connection conn = null;

        try {
            conn = db.getConnection();
            var preparedStatement = conn.prepareStatement("TRUNCATE UserDAO");

            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException(ex.toString());
        }finally{
            db.closeConnection(conn);
        }


        Integer count = 0;
        try {
            conn = db.getConnection();
            var preparedStatement = conn.prepareStatement("SELECT username FROM UserDAO");
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

        users.clear();

        if(users.size() != 0){
            throw new DataAccessException("Error: UserDAO not cleared");
        }
    }
}
