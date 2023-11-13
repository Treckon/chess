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
public class AuthDAOTests {

    @BeforeEach
    public void clearAll(){
        ClearService service = new ClearService();
        service.clear();
    }

    @Test
    @Order(1)
    @DisplayName("Verify Success")
    public void insertUserSuccess(){
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
            token = auths.login("darthconner2000");
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
    @Order(2)
    @DisplayName("Verify Failure")
    public void insertUserFailure() throws DataAccessException {
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
            token = auths.login("darthconner2000");
        }catch(DataAccessException e){
            e.printStackTrace();
        }

        try{
            auths.logout(token.getToken());
        }catch(DataAccessException e){
            e.printStackTrace();
        }

        Boolean verified = false;
        try{
            verified = auths.verifyAuthToken(token.getToken());
        }catch(DataAccessException s){
            s.printStackTrace();
        }
        Assertions.assertEquals(false, verified, "logged in somehow");
    }

    @Test
    @Order(3)
    @DisplayName("Logout Success")
    public void logoutSuccess(){
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
            token = auths.login("darthconner2000");
        }catch(DataAccessException e){
            e.printStackTrace();
        }

        try{
            auths.logout(token.getToken());
        }catch(DataAccessException e){
            e.printStackTrace();
        }


        Boolean verified = false;
        try{
            verified = auths.verifyAuthToken(token.getToken());
        }catch(DataAccessException s){
            s.printStackTrace();
        }
        Assertions.assertEquals(false, verified, "not logged in");
    }

    @Test
    @Order(4)
    @DisplayName("Logout Failure")
    public void logoutFailure() throws DataAccessException {
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
            token = auths.login("darthconner2000");
        }catch(DataAccessException e){
            e.printStackTrace();
        }

        try{
            auths.logout(token.getToken());
        }catch(DataAccessException e){
            e.printStackTrace();
        }
        String tokenValue = token.getToken();

        Assertions.assertThrows(DataAccessException.class,()->{auths.logout(tokenValue);});
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
            token = auths.login("darthconner2000");
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
            auths.login(null);
        }catch(DataAccessException e){
            e.printStackTrace();
        }

        Assertions.assertThrows(DataAccessException.class,()->{auths.login(null);});
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
        auths.login("darthconner2000");

        try{
            auths.clear();
        }catch(DataAccessException e){
            e.printStackTrace();
        }

        Database db = new Database();
        Connection conn = null;
        Integer count = 0;
        try {
            conn = db.getConnection();
            var preparedStatement = conn.prepareStatement("SELECT * FROM AuthDAO");
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
