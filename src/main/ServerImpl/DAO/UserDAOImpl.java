package ServerImpl.DAO;

import ServerImpl.Objects.AuthToken;
import ServerImpl.Objects.AuthTokenImpl;
import ServerImpl.Objects.User;
import dataAccess.DataAccessException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserDAOImpl implements UserDAO {
    private static Map<String, User> users = new HashMap<>();


    @Override
    public void newUser(User user, String username) throws DataAccessException {

        if(user == null || username.isEmpty()){
            throw new DataAccessException("Error: bad request");
        }

        if(users.get(username) != null){
            throw new DataAccessException("Error: already taken");
        }

        users.put(username, user);
    }

    @Override
    public User getUser(String username) throws DataAccessException {
        return users.get(username);
    }

    @Override
    public AuthToken loginUser(String username, String password) throws DataAccessException {
        User user = users.get(username);

        if(user == null || !password.equals(user.getPassword())){
            throw new DataAccessException("Error: unauthorized");
        }

        AuthDAOImpl auths = new AuthDAOImpl();

        AuthToken auth = auths.login(username);


        return auth;
    }

    @Override
    public void clear() throws DataAccessException{
        users.clear();

        if(users.size() != 0){
            throw new DataAccessException("Error: UserDAO not cleared");
        }
    }
}
