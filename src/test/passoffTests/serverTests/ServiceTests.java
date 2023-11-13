package passoffTests.serverTests;


import ServerImpl.DAO.AuthDAOImpl;
import ServerImpl.DAO.GameDAOImpl;
import ServerImpl.DAO.UserDAOImpl;
import ServerImpl.Objects.*;
import ServerImpl.Requests.*;
import ServerImpl.Responses.*;
import ServerImpl.Services.*;
import chess.ChessGameImpl;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import dataAccess.Database;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ServiceTests {

    @BeforeEach
    public void clearAll(){
        ClearService service = new ClearService();
        service.clear();
    }


    @Test
    @Order(1)
    @DisplayName("Register Failure")
    public void failedRegister() {
        UserDAOImpl users = new UserDAOImpl();
        AuthDAOImpl auths = new AuthDAOImpl();
        try{
            users.newUser(new UserImpl("darthconner2000", "password", "darthconner2000@gmail.com"), "darthconner2000");
        }catch(DataAccessException e){
            return;
        }

        RegisterService register = new RegisterService();
        RegisterResponse response = register.newUser(new RegisterRequest(null, "password", "darthconner2000@gmail.com"));

        Assertions.assertEquals("Error: bad request", response.getMessage(), "Allowed a Null Value");

        register = new RegisterService();
        response = register.newUser(new RegisterRequest("darthconner2000", "password", "darthconner2000@gmail.com"));

        Assertions.assertEquals("Error: already taken", response.getMessage(), "Allowed a duplicate username");

    }
    @Test
    @Order(2)
    @DisplayName("Register Success")
    public void successfulRegister() {
        UserDAOImpl users = new UserDAOImpl();
        AuthDAOImpl auths = new AuthDAOImpl();

        RegisterService register = new RegisterService();
        RegisterResponse response = register.newUser(new RegisterRequest("darthconner2000", "password", "darthconner2000@gmail.com"));

        User user;
        AuthToken auth;
        AuthToken currentAuth;

        try{
            user = users.getUser("darthconner2000");
        }catch (DataAccessException e){
            return;
        }

        try{
            auth = auths.getCurrentToken();
        }catch (DataAccessException e){
            return;
        }

        Assertions.assertEquals(response.getUsername(), user.getUsername(), "Username not found");
        Assertions.assertEquals(response.getAuthToken(), auth.getToken(), "User not logged in.");

    }

    @Test
    @Order(3)
    @DisplayName("Login Failure")
    public void failedLogin() {
        UserDAOImpl users = new UserDAOImpl();
        AuthDAOImpl auths = new AuthDAOImpl();

        try{
            users.newUser(new UserImpl("darthconner2000", "password", "darthconner2000@gmail.com"), "darthconner2000");
        }catch(DataAccessException e){
            return;
        }

        LoginService login = new LoginService();
        LoginResponse response = login.login(new LoginRequest("darthconner2000", "passw0rd"));

        User user;
        AuthToken auth;
        AuthToken currentAuth;


        Assertions.assertEquals("Error: unauthorized", response.getMessage(), "Correct error not returned");


    }

    @Test
    @Order(4)
    @DisplayName("Login Success")
    public void successfulLogin() {
        UserDAOImpl users = new UserDAOImpl();
        AuthDAOImpl auths = new AuthDAOImpl();

        try{
            users.newUser(new UserImpl("darthconner2000", "password", "darthconner2000@gmail.com"), "darthconner2000");
        }catch(DataAccessException e){
            return;
        }

        LoginService login = new LoginService();
        LoginResponse response = login.login(new LoginRequest("darthconner2000", "password"));

        User user;
        AuthToken auth;
        AuthToken currentAuth;

        try{
            user = users.getUser("darthconner2000");
        }catch (DataAccessException e){
            return;
        }

        try{
            auth = auths.getCurrentToken();
        }catch (DataAccessException e){
            return;
        }

        Assertions.assertEquals(response.getUsername(), user.getUsername(), "Username not found");
        Assertions.assertEquals(response.getAuthToken(), auth.getToken(), "User not logged in.");
    }

    @Test
    @Order(5)
    @DisplayName("Logout Success")
    public void successfulLogout() {
        UserDAOImpl users = new UserDAOImpl();
        AuthDAOImpl auths = new AuthDAOImpl();

        try{
            users.newUser(new UserImpl("darthconner2000", "password", "darthconner2000@gmail.com"), "darthconner2000");
        }catch(DataAccessException e){
            return;
        }

        LoginService login = new LoginService();
        LoginResponse loginResponse = login.login(new LoginRequest("darthconner2000", "password"));

        AuthToken auth;
        try{
            auth = auths.getCurrentToken();
        }catch(DataAccessException e){
            return;
        }

        LogoutService logout = new LogoutService();
        logout.logout(new LogoutRequest(auth));

        try{
            auth = auths.getCurrentToken();
        }catch(DataAccessException e){
            return;
        }

        Assertions.assertEquals(null, auth, "CurrentToken not null");

    }

    @Test
    @Order(6)
    @DisplayName("Logout Failure")
    public void failedLogout() {
        UserDAOImpl users = new UserDAOImpl();
        AuthDAOImpl auths = new AuthDAOImpl();

        try{
            users.newUser(new UserImpl("darthconner2000", "password", "darthconner2000@gmail.com"), "darthconner2000");
        }catch(DataAccessException e){
            return;
        }

        LoginService login = new LoginService();
        LoginResponse loginResponse = login.login(new LoginRequest("darthconner2000", "password"));

        AuthToken auth;
        try{
            auth = auths.getCurrentToken();
        }catch(DataAccessException e){
            return;
        }

        LogoutService logout = new LogoutService();
        logout.logout(new LogoutRequest(new AuthTokenImpl("darthconner2000", loginResponse.getAuthToken())));
        /*
        try{
            auth = auths.getCurrentToken();
        }catch(DataAccessException e){
            return;
        }

         */
        Boolean verified = true;
        try{
            verified = auths.verifyAuthToken(loginResponse.getAuthToken());
        }catch(DataAccessException e){

        }

        Assertions.assertNotEquals(true, verified, "Token still in there");

    }

    @Test
    @Order(7)
    @DisplayName("CreateGame Success")
    public void successfullCreateGame() {
        UserDAOImpl users = new UserDAOImpl();
        AuthDAOImpl auths = new AuthDAOImpl();

        RegisterService register = new RegisterService();
        RegisterResponse registerResponse = register.newUser(new RegisterRequest("darthconner2000", "password", "darthconner2000@gmail.com"));

        User user;
        AuthToken auth;
        AuthToken currentAuth;



        auth = new AuthTokenImpl(registerResponse.getUsername(), registerResponse.getAuthToken());



        CreateGameService gameService = new CreateGameService();
        CreateGameResponse response = gameService.createGame(new CreateGameRequest("War Games", auth));

        Assertions.assertEquals(1, response.getGameID());

        response = gameService.createGame(new CreateGameRequest("Checkmates", auth));

        Assertions.assertEquals(2, response.getGameID());
    }

    @Test
    @Order(8)
    @DisplayName("CreateGame Failure")
    public void failedCreateGame() {
        CreateGameService gameService = new CreateGameService();
        CreateGameResponse response = gameService.createGame(new CreateGameRequest("War Games", null));

        Assertions.assertEquals("Error: unauthorized", response.getMessage(), "Created a game without logging in.");


        AuthDAOImpl auths = new AuthDAOImpl();
        RegisterService register = new RegisterService();
        register.newUser(new RegisterRequest("darthconner2000", "password", "darthconner2000@gmail.com"));

        AuthToken auth;

        try{
            auth = auths.getCurrentToken();
        }catch (DataAccessException e){
            return;
        }

        response = gameService.createGame(new CreateGameRequest(null, auth));

        Assertions.assertEquals("Error: bad request", response.getMessage(), "No Error with null name.");

    }

    @Test
    @Order(9)
    @DisplayName("ListGames Success")
    public void successfulListGames() {
        UserDAOImpl users = new UserDAOImpl();
        AuthDAOImpl auths = new AuthDAOImpl();

        RegisterService register = new RegisterService();
        RegisterResponse registerResponse = register.newUser(new RegisterRequest("darthconner2000", "password", "darthconner2000@gmail.com"));

        User user;
        AuthToken auth;
        AuthToken currentAuth;

        try{
            auth = auths.getCurrentToken();
        }catch (DataAccessException e){
            return;
        }

        CreateGameService gameService = new CreateGameService();
        CreateGameResponse createGameResponse = gameService.createGame(new CreateGameRequest("War Games", auth));


        ListGameService listService = new ListGameService();
        ListGamesResponse response = listService.listGames(new ListGamesRequest(auth));

        Assertions.assertEquals(1, response.getList().size(), "Incorrect number of games.");

        createGameResponse = gameService.createGame(new CreateGameRequest("Checkmate", auth));

        response = listService.listGames(new ListGamesRequest(auth));

        Assertions.assertEquals(2, response.getList().size(), "Incorrect number of games.");
    }

    @Test
    @Order(10)
    @DisplayName("ListGames Failure")
    public void failedListGames() {
        UserDAOImpl users = new UserDAOImpl();
        AuthDAOImpl auths = new AuthDAOImpl();

        RegisterService register = new RegisterService();
        RegisterResponse registerResponse = register.newUser(new RegisterRequest("darthconner2000", "password", "darthconner2000@gmail.com"));

        User user;
        AuthToken auth;
        AuthToken currentAuth;

        try{
            auth = auths.getCurrentToken();
        }catch (DataAccessException e){
            return;
        }

        CreateGameService gameService = new CreateGameService();
        CreateGameResponse createGameResponse = gameService.createGame(new CreateGameRequest("War Games", auth));


        ListGameService listService = new ListGameService();
        ListGamesResponse response = listService.listGames(new ListGamesRequest(null));

        Assertions.assertEquals("Error: unauthorized", response.getMessage(), "Was somehow authorized.");
    }

    @Test
    @Order(11)
    @DisplayName("JoinGame Success")
    public void successfullJoinGame() throws DataAccessException {
        UserDAOImpl users = new UserDAOImpl();
        AuthDAOImpl auths = new AuthDAOImpl();

        RegisterService register = new RegisterService();
        RegisterResponse registerResponse = register.newUser(new RegisterRequest("darthconner2000", "password", "darthconner2000@gmail.com"));

        User user;
        AuthToken auth;
        AuthToken currentAuth;

        try{
            auth = auths.getCurrentToken();
        }catch (DataAccessException e){
            return;
        }

        CreateGameService gameService = new CreateGameService();
        CreateGameResponse createGameResponse = gameService.createGame(new CreateGameRequest("War Games", auth));

        JoinGameService service = new JoinGameService();
        JoinGameResponse response = service.joinGame(new JoinGameRequest("WHITE",createGameResponse.getGameID(),auth));

        GameDAOImpl games = new GameDAOImpl();
        ChessGameImpl game;
        try {
            game = games.findGame(createGameResponse.getGameID());
        }catch (DataAccessException e){
            return;
        }

        Database db = new Database();
        Connection conn = null;
        String whiteUser = null;
        try {
            conn = db.getConnection();
            var preparedStatement = conn.prepareStatement("SELECT gameid, whiteplayer FROM GameDAO WHERE gameid=?");
            preparedStatement.setInt(1, createGameResponse.getGameID());

            try(var rs = preparedStatement.executeQuery()){
                rs.next();
                whiteUser = rs.getString("whiteplayer");
            }
        } catch (SQLException | DataAccessException ex) {
            ex.printStackTrace();
        }finally{
            db.closeConnection(conn);
        }

        Assertions.assertEquals("darthconner2000", whiteUser, "White Username not Established");


    }

    @Test
    @Order(12)
    @DisplayName("JoinGame Failure")
    public void failedJoinGame() {
        UserDAOImpl users = new UserDAOImpl();
        AuthDAOImpl auths = new AuthDAOImpl();

        RegisterService register = new RegisterService();
        RegisterResponse registerResponse = register.newUser(new RegisterRequest("darthconner2000", "password", "darthconner2000@gmail.com"));

        User user;
        AuthToken auth;
        AuthToken currentAuth;

        try{
            auth = auths.getCurrentToken();
        }catch (DataAccessException e){
            return;
        }

        CreateGameService gameService = new CreateGameService();
        CreateGameResponse createGameResponse = gameService.createGame(new CreateGameRequest("War Games", auth));

        JoinGameService service = new JoinGameService();
        JoinGameResponse response = service.joinGame(new JoinGameRequest("WHITE",createGameResponse.getGameID(),auth));

        GameDAOImpl games = new GameDAOImpl();
        ChessGameImpl game;
        try {
            game = games.findGame(createGameResponse.getGameID());
        }catch (DataAccessException e){
            return;
        }

        response = service.joinGame(new JoinGameRequest("BLACK",createGameResponse.getGameID(),null));
        Assertions.assertEquals("Error: unauthorized", response.getMessage(), "Unauthorized Access");

        response = service.joinGame(new JoinGameRequest("BLACK",5,auth));
        Assertions.assertEquals("Error: bad request", response.getMessage(), "Add to bad ID");
        try{
            games.joinGame("BLACK","darthconner2000", createGameResponse.getGameID());
        } catch (DataAccessException e){
            return;
        }


        response = service.joinGame(new JoinGameRequest("BLACK", createGameResponse.getGameID(), auth));
        Assertions.assertEquals("Error: already taken", response.getMessage(), "Added to game where user already logged on.");

    }


    @Test
    @Order(13)
    @DisplayName("Clear Success")
    public void successfulClear() {
        UserDAOImpl users = new UserDAOImpl();
        AuthDAOImpl auths = new AuthDAOImpl();

        RegisterService register = new RegisterService();
        RegisterResponse registerResponse = register.newUser(new RegisterRequest("darthconner2000", "password", "darthconner2000@gmail.com"));

        User user;
        AuthToken auth;
        AuthToken currentAuth;

        try{
            auth = auths.getCurrentToken();
        }catch (DataAccessException e){
            return;
        }

        CreateGameService gameService = new CreateGameService();
        CreateGameResponse createGameResponse = gameService.createGame(new CreateGameRequest("War Games", auth));

        GameDAOImpl games = new GameDAOImpl();

        ClearService service = new ClearService();
        ClearResponse response = service.clear();

        Integer numOfGames;
        try{
            numOfGames = games.listGames().size();
        } catch (DataAccessException e){
            return;
        }

        Assertions.assertEquals(null, response.getMessage(), "no errors");
        Assertions.assertEquals(0, numOfGames, "no games");
    }

}
