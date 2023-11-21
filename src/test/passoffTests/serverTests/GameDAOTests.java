package passoffTests.serverTests;

import ServerImpl.DAO.GameDAOImpl;
import ServerImpl.Objects.Game;
import ServerImpl.Services.ClearService;
import chess.ChessGameImpl;
import dataAccess.DataAccessException;
import dataAccess.Database;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GameDAOTests {
    @BeforeEach
    public void clearAll(){
        ClearService service = new ClearService();
        service.clear();
    }

    @Test
    @Order(1)
    @DisplayName("Insert Game Success")
    public void insertGameSuccess(){
        GameDAOImpl games = new GameDAOImpl();

        try{
            games.createGame("war games");
        }catch(DataAccessException s){
            s.printStackTrace();
        }

        ChessGameImpl game = null;

        try{
            game = games.findGame(1);
        }catch(DataAccessException s){
            s.printStackTrace();
        }
        Assertions.assertNotEquals(null, game, "game not found");
    }

    @Test
    @Order(2)
    @DisplayName("Insert User Failure")
    public void insertUserFailure() throws DataAccessException {
        GameDAOImpl games = new GameDAOImpl();

        try{
            games.createGame("war games");
        }catch(DataAccessException s){
            s.printStackTrace();
        }

        Assertions.assertThrows(DataAccessException.class,()->{games.createGame(null);});
    }

    @Test
    @Order(3)
    @DisplayName("Join Game Success")
    public void joinGameSuccess() throws DataAccessException {
        GameDAOImpl games = new GameDAOImpl();

        try{
            games.createGame("war games");
        }catch(DataAccessException s){
            s.printStackTrace();
        }
        games.joinGame("BLACK","darthconner2000",1);

        Database db = new Database();
        Connection conn = null;
        String blackPlayer = null;

        try {
            conn = db.getConnection();
            var preparedStatement = conn.prepareStatement("SELECT gameid, blackplayer FROM GameDAO WHERE gameid=?");
            preparedStatement.setInt(1, 1);

            try(var rs = preparedStatement.executeQuery()){
                while (rs.next()){
                    blackPlayer = rs.getString("blackplayer");
                }
            }
        } catch (SQLException | DataAccessException ex) {
            ex.printStackTrace();
        }finally{
            db.closeConnection(conn);
        }
        Assertions.assertEquals(true, blackPlayer.equals("darthconner2000"), "game not found");
    }

    @Test
    @Order(4)
    @DisplayName("Join Game Failure")
    public void joinGameFailure() throws DataAccessException {
        GameDAOImpl games = new GameDAOImpl();

        try{
            games.createGame("war games");
        }catch(DataAccessException s){
            s.printStackTrace();
        }
        games.joinGame("BLACK","darthconner2000",1);
        Assertions.assertThrows(DataAccessException.class,()->{games.joinGame("BLACK", "theSenate",1);});

    }

    @Test
    @Order(5)
    @DisplayName("Find Game Success")
    public void findGameSuccess(){
        GameDAOImpl games = new GameDAOImpl();

        try{
            games.createGame("war games");
        }catch(DataAccessException s){
            s.printStackTrace();
        }
        ChessGameImpl game = null;

        try{
            game = games.findGame(1);
        }catch(DataAccessException s){
            s.printStackTrace();
        }
        Assertions.assertNotEquals(null, game, "game not found");
    }

    @Test
    @Order(6)
    @DisplayName("Find Game Failure")
    public void findGameFailure(){
        GameDAOImpl games = new GameDAOImpl();
        ChessGameImpl game = null;

        try{
            game = games.findGame(1);
        }catch(DataAccessException s){
            s.printStackTrace();
        }
        Assertions.assertEquals(null, game, "Game somehow found?");
    }

    @Test
    @Order(7)
    @DisplayName("List Game Success")
    public void listGameSuccess(){
        GameDAOImpl games = new GameDAOImpl();

        try{
            games.createGame("war games");
        }catch(DataAccessException s){
            s.printStackTrace();
        }

        ArrayList<Game> game = null;

        try{
            game = games.listGames();
        }catch(DataAccessException s){
            s.printStackTrace();
        }
        Assertions.assertEquals(1, game.size(), "games not found");
    }

    @Test
    @Order(8)
    @DisplayName("List Game Failure")
    public void listGameFailure(){
        GameDAOImpl games = new GameDAOImpl();

        ArrayList<Game> game = null;

        try{
            game = games.listGames();
        }catch(DataAccessException s){
            s.printStackTrace();
        }
        Assertions.assertEquals(0, game.size(), "games found");
    }

    @Test
    @Order(9)
    @DisplayName("Clear Success")
    public void ClearSuccess(){
        GameDAOImpl games = new GameDAOImpl();

        try{
            games.createGame("war games");
        }catch(DataAccessException s){
            s.printStackTrace();
        }

        try{
            games.clear();
        }catch(DataAccessException s){
            s.printStackTrace();
        }

        ArrayList<Game> game = null;

        try{
            game = games.listGames();
        }catch(DataAccessException s){
            s.printStackTrace();
        }
        Assertions.assertEquals(0, game.size(), "games found");
    }
}
