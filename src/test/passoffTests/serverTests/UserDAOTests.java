package passoffTests.serverTests;

import ServerImpl.DAO.AuthDAOImpl;
import ServerImpl.DAO.UserDAOImpl;
import ServerImpl.Objects.AuthToken;
import ServerImpl.Objects.User;
import ServerImpl.Objects.UserImpl;
import ServerImpl.Services.ClearService;
import dataAccess.DataAccessException;
import dataAccess.Database;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserDAOTests {

    @BeforeEach
    public void clearAll(){
        ClearService service = new ClearService();
        service.clear();
    }

    @Test
    @Order(1)
    @DisplayName("Insert User Success")
    public void insertUserSuccess(){
        UserDAOImpl users = new UserDAOImpl();

        User user = new UserImpl("darthconner2000", "password", "darthconner2000@yahoo.com");
        try{
            users.newUser(user, "darthconner2000");
        }catch(DataAccessException s){
            s.printStackTrace();
        }
        User user2 = null;
        try{
            user2 = users.getUser("darthconner2000");
        }catch(DataAccessException s){
            s.printStackTrace();
        }
        Assertions.assertEquals(true, user2.equals(user), "Users not equal");
    }

    @Test
    @Order(2)
    @DisplayName("Insert User Failure")
    public void insertUserFailure() throws DataAccessException {
        UserDAOImpl users = new UserDAOImpl();
        String error;

        User user = new UserImpl(null, "password", "darthconner2000@yahoo.com");
        try{
            users.newUser(user, null);
        }catch(DataAccessException s){
            error = s.getMessage();
        }
        Assertions.assertThrows(DataAccessException.class,()->{users.newUser(user, null);});
    }

    @Test
    @Order(3)
    @DisplayName("Get User Success")
    public void getUserSuccess(){
        UserDAOImpl users = new UserDAOImpl();

        User user = new UserImpl("darthconner2000", "password", "darthconner2000@yahoo.com");
        try{
            users.newUser(user, "darthconner2000");
        }catch(DataAccessException s){
            s.printStackTrace();
        }
        User user2 = null;
        try{
            user2 = users.getUser("darthconner2000");
        }catch(DataAccessException s){
            s.printStackTrace();
        }
        Assertions.assertEquals(true, user2.equals(user), "Users not equal");
    }

    @Test
    @Order(4)
    @DisplayName("Get User Failure")
    public void getUserFailure() throws DataAccessException {
        UserDAOImpl users = new UserDAOImpl();
        String error;

        User user = new UserImpl("darthconner2000", "password", "darthconner2000@yahoo.com");
        try{
            users.newUser(user, "darthconner2000");
        }catch(DataAccessException s){
            s.printStackTrace();
        }
        User user2 = null;
        try{
            user2 = users.getUser(null);
        }catch(DataAccessException s){
            error = s.getMessage();
        }

        Assertions.assertThrows(DataAccessException.class,()->{users.getUser(null);});
    }

    @Test
    @Order(5)
    @DisplayName("Login User Success")
    public void loginUserSuccess(){
        UserDAOImpl users = new UserDAOImpl();
        AuthDAOImpl auths = new AuthDAOImpl();

        User user = new UserImpl("darthconner2000", "password", "darthconner2000@yahoo.com");
        try{
            users.newUser(user, "darthconner2000");
        }catch(DataAccessException s){
            s.printStackTrace();
        }
        AuthToken token = null;
        try{
            token = users.loginUser("darthconner2000","password");
        }catch(DataAccessException e){
            e.printStackTrace();
        }


        Boolean verified = false;
        try{
            verified = auths.verifyAuthToken(token.getToken());
        }catch(DataAccessException s){
            s.printStackTrace();
        }
        Assertions.assertEquals(true, verified, "not logged in");
    }

    @Test
    @Order(6)
    @DisplayName("Login User Failure")
    public void loginUserFailure() throws DataAccessException {
        UserDAOImpl users = new UserDAOImpl();
        AuthDAOImpl auths = new AuthDAOImpl();

        User user = new UserImpl("darthconner2000", "password", "darthconner2000@yahoo.com");
        try{
            users.newUser(user, "darthconner2000");
        }catch(DataAccessException s){
            s.printStackTrace();
        }

        try{
            users.loginUser("darthconner2000","passw0rd");
        }catch(DataAccessException e){
            e.printStackTrace();
        }

        Assertions.assertThrows(DataAccessException.class,()->{users.loginUser("darthconner2000","passw0rd");});
    }

    @Test
    @Order(7)
    @DisplayName("Clear User Success")
    public void clearUserSuccess() throws DataAccessException {
        UserDAOImpl users = new UserDAOImpl();
        AuthDAOImpl auths = new AuthDAOImpl();

        User user = new UserImpl("darthconner2000", "password", "darthconner2000@yahoo.com");
        try{
            users.newUser(user, "darthconner2000");
        }catch(DataAccessException s){
            s.printStackTrace();
        }

        try{
            users.clear();
        }catch(DataAccessException e){
            e.printStackTrace();
        }

        Database db = new Database();
        Connection conn = null;
        Integer count = 0;
        try {
            conn = db.getConnection();
            var preparedStatement = conn.prepareStatement("SELECT username FROM UserDAO");
            try(var rs = preparedStatement.executeQuery()){
                while (rs.next()){
                    count++;
                }
            }
        } catch (SQLException | DataAccessException ex) {
            ex.printStackTrace();
        }finally{
            db.closeConnection(conn);
        }
        Assertions.assertEquals(0, count, "Not empty");
    }

}
