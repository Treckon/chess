package ServerImpl.DAO;

import Objects.Game;
import Objects.GameImpl;
import chess.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dataAccess.DataAccessException;
import dataAccess.Database;
import deserializers.GameDeserializer;
import deserializers.PositionDeserializer;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class GameDAOImpl implements GameDAO {
    private static Integer nextCode = 1;
    @Override
    public ArrayList<GameImpl> listGames() throws DataAccessException {
        ArrayList<GameImpl> gameList = new ArrayList<GameImpl>();

        Database db = new Database();
        Connection conn = null;
        ChessGameImpl game = null;

        try {
            conn = db.getConnection();
            var preparedStatement = conn.prepareStatement("SELECT gameid, game, whiteplayer, blackplayer, gamename FROM GameDAO");

            try(var rs = preparedStatement.executeQuery()){
                while (rs.next()){
                    String json = rs.getString("game");
                    var builder = new GsonBuilder();
                    builder.registerTypeAdapter(ChessPiece.class, new GameDeserializer());
                    builder.registerTypeAdapter(ChessPosition.class, new PositionDeserializer());

                    game = builder.create().fromJson(json,ChessGameImpl.class);
                    GameImpl listGame = new GameImpl(rs.getInt("gameid"),rs.getString(5),rs.getString("whitePlayer"),rs.getString("blackplayer"),game);
                    gameList.add(listGame);
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(ex.toString());
        } finally{
            db.closeConnection(conn);
        }
        /*
        for(Map.Entry<Integer, Game> game: games.entrySet()){
            gameList.add(game.getValue());
        }*/
        return gameList;
    }

    @Override
    public Integer createGame(String name) throws DataAccessException {
        if(name == null || name.isEmpty()){
            throw new DataAccessException("Error: bad request");
        }
        var ID = 0;

        ChessGameImpl game = new ChessGameImpl();
        Database db = new Database();
        Connection conn = null;

        try {
            conn = db.getConnection();
            var preparedStatement = conn.prepareStatement("INSERT INTO GameDAO (game, gamename) VALUES(?, ?)", RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, new Gson().toJson(game));
            preparedStatement.setString(2, name);

            preparedStatement.execute();
            var resultSet = preparedStatement.getGeneratedKeys();

            if (resultSet.next()) {
                ID = resultSet.getInt(1);
            }
        } catch (SQLException ex) {
            throw new DataAccessException(ex.toString());
        } finally{
            db.closeConnection(conn);
        }


        return ID;
    }

    @Override
    public void joinGame(String color, String username, Integer gameId) throws DataAccessException {
        String black = "BLACK";
        String white = "WHITE";

        ChessGameImpl game = null;
        try{
            game = findGame(gameId);
        }catch(DataAccessException e){
            throw new DataAccessException("Error: bad request");
        }
        if(game == null){
            throw new DataAccessException("Error: bad request");
        }

        if(username == null || username.isEmpty()){
            throw new DataAccessException("Error: bad request");
        }

        if(color == null || color.equals("")){
            return;

        }else if(color != null && color.equals(black)){
            game = null;
            try{
                game = findGame(gameId);
            }catch(DataAccessException e){
                throw new DataAccessException("Error: bad request");
            }
            if(game == null){
                throw new DataAccessException("Error: bad request");
            }

            Database db = new Database();
            Connection conn = null;

            try {
                conn = db.getConnection();
                var preparedStatement = conn.prepareStatement("SELECT gameid, blackplayer FROM GameDAO WHERE gameid=?");
                preparedStatement.setInt(1, gameId);

                try(var rs = preparedStatement.executeQuery()){
                    while (rs.next()){
                        String blackPlayer = rs.getString("blackplayer");
                        if(blackPlayer != null){
                            throw new DataAccessException("Error: already taken");
                        }
                    }
                }
            } catch (SQLException ex) {
                throw new DataAccessException(ex.toString());
            }finally{
                db.closeConnection(conn);
            }

            try {
                conn = db.getConnection();
                var preparedStatement = conn.prepareStatement("UPDATE GameDAO SET blackplayer=? WHERE gameid=?");
                preparedStatement.setString(1, username);
                preparedStatement.setInt(2, gameId);

                preparedStatement.executeUpdate();
            } catch (SQLException ex) {
                throw new DataAccessException(ex.toString());
            }finally{
                db.closeConnection(conn);
            }
            return;
        }else if(color != null && color.equals(white)){
            game = null;
            try{
                game = findGame(gameId);
            }catch(DataAccessException e){
                throw new DataAccessException("Error: bad request");
            }
            if(game == null){
                throw new DataAccessException("Error: bad request");
            }

            Database db = new Database();
            Connection conn = null;

            try {
                conn = db.getConnection();
                var preparedStatement = conn.prepareStatement("SELECT gameid, whiteplayer FROM GameDAO WHERE gameid=?");
                preparedStatement.setInt(1, gameId);

                try(var rs = preparedStatement.executeQuery()){
                    while (rs.next()){
                        String whitePlayer = rs.getString("whiteplayer");
                        if(whitePlayer != null){
                            throw new DataAccessException("Error: already taken");
                        }
                    }
                }
            } catch (SQLException ex) {
                throw new DataAccessException(ex.toString());
            }finally{
                db.closeConnection(conn);
            }

            try {
                conn = db.getConnection();
                var preparedStatement = conn.prepareStatement("UPDATE GameDAO SET whiteplayer=? WHERE gameid=?");
                preparedStatement.setString(1, username);
                preparedStatement.setInt(2, gameId);

                preparedStatement.executeUpdate();
            } catch (SQLException ex) {
                throw new DataAccessException(ex.toString());
            }finally{
                db.closeConnection(conn);
            }
            return;
        }
        throw new DataAccessException("Error: bad request");


    }

    @Override
    public ChessGameImpl findGame(Integer gameId) throws DataAccessException {
        Database db = new Database();
        ChessGameImpl game = null;
        Connection conn = null;

        try {
            conn = db.getConnection();
            var preparedStatement = conn.prepareStatement("SELECT gameid, game FROM GameDAO WHERE gameid=?");
            preparedStatement.setInt(1, gameId);

            try(var rs = preparedStatement.executeQuery()){
                while (rs.next()){
                    String json = rs.getString("game");
                    var builder = new GsonBuilder();
                    builder.registerTypeAdapter(ChessPiece.class, new GameDeserializer());
                    builder.registerTypeAdapter(ChessPosition.class, new PositionDeserializer());

                    game = builder.create().fromJson(json,ChessGameImpl.class);
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Error: bad request");
        }finally{
            db.closeConnection(conn);
        }
        return game;
    }

    public void updateGame(Game newMove, Integer gameId) throws DataAccessException{
        ChessGameImpl game = findGame(gameId);
        if(game == null){
            throw new DataAccessException("Error: Game not found");
        }

        Database db = new Database();
        Connection conn = null;

        try {
            conn = db.getConnection();
            var preparedStatement = conn.prepareStatement("UPDATE GameDAO SET game=? WHERE gameid=?");
            preparedStatement.setString(1, new Gson().toJson(newMove));
            preparedStatement.setInt(2,gameId);

            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException(ex.toString());
        }finally{
            db.closeConnection(conn);
        }

    }

    public void removeGame(Integer gameId) throws DataAccessException{

        Database db = new Database();
        Connection conn = null;

        try {
            conn = db.getConnection();
            var preparedStatement = conn.prepareStatement("DELETE FROM GameDAO WHERE gameid=?");
            preparedStatement.setInt(1, gameId);

            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException(ex.toString());
        }finally{
            db.closeConnection(conn);
        }
    }

    @Override
    public void clear() throws DataAccessException{
        Database db = new Database();
        Connection conn = null;


        try {
            conn = db.getConnection();
            var preparedStatement = conn.prepareStatement("TRUNCATE GameDAO");
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException(ex.toString());
        }finally{
            db.closeConnection(conn);
        }


        Integer count = 0;
        try {
            conn = db.getConnection();
            var preparedStatement = conn.prepareStatement("SELECT gameid FROM GameDAO");
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
    }

    private Integer generateID(){
        Integer ID = nextCode;
        nextCode = ID + 1;
        return ID;
    }
}
