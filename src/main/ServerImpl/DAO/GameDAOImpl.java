package ServerImpl.DAO;

import ServerImpl.Objects.Game;
import ServerImpl.Objects.GameImpl;
import dataAccess.DataAccessException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GameDAOImpl implements GameDAO {
    private static Map<Integer, Game> games = new HashMap<>();
    private static Integer nextCode = 1;
    @Override
    public ArrayList<Game> listGames() throws DataAccessException {
        ArrayList<Game> gameList = new ArrayList<Game>();
        for(Map.Entry<Integer, Game> game: games.entrySet()){
            gameList.add(game.getValue());
        }
        return gameList;
    }

    @Override
    public Integer createGame(String name) throws DataAccessException {
        if(name == null || name.isEmpty()){
            throw new DataAccessException("Error: bad request");
        }
        Integer ID = generateID();

        GameImpl newGame = new GameImpl(ID, name);

        games.put(ID, newGame);

        return newGame.getId();
    }

    @Override
    public void joinGame(String color, String username, Integer gameId) throws DataAccessException {
        String black = "BLACK";
        String white = "WHITE";
        if(color == null || color.equals("")){
            Game currentGame = games.get(gameId);
            if(currentGame == null){
                throw new DataAccessException("Error: bad request");
            }

            return;
        }
        if(color != null && (color.equals(black) || color.equals(white))){
            Game currentGame = games.get(gameId);
            if(currentGame == null){
                throw new DataAccessException("Error: bad request");
            }
            currentGame.joinGame(color, username);

            return;
        }
        throw new DataAccessException("Error: bad request");

    }

    @Override
    public Game findGame(Integer gameId) throws DataAccessException {
        return games.get(gameId);
    }

    public void updateGame(Game newMove, Integer gameId) throws DataAccessException{
        if(games.get(gameId) == null){
            throw new DataAccessException("Error: Game not found");
        }

        games.replace(gameId,newMove);
    }

    public void removeGame(Integer gameId) throws DataAccessException{
        if(games.get(gameId) == null){
            throw new DataAccessException("Error: Game not found");
        }

        games.remove(gameId);
    }

    @Override
    public void clear() throws DataAccessException{
        games.clear();
        nextCode = 1;

        if(games.size() != 0|| nextCode != 1){
            throw new DataAccessException("Error: GameDAO not cleared");
        }
    }

    private Integer generateID(){
        Integer ID = nextCode;
        nextCode = ID + 1;
        return ID;
    }
}
