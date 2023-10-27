package ServerImpl;

import ServerImpl.DAO.AuthDAOImpl;
import ServerImpl.DAO.GameDAOImpl;
import ServerImpl.Objects.AuthTokenImpl;
import ServerImpl.Requests.*;
import ServerImpl.Responses.*;
import ServerImpl.Services.*;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import spark.*;
import spark.Request;
import spark.Response;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.*;
import java.util.logging.FileHandler;

public class MainServer {
    private ArrayList<String> names = new ArrayList<>();

    public static void main(String[] args) {
        new MainServer().run();
    }

    private void run() {
        // Specify the port you want the server to listen on
        Spark.port(8080);

        // Register a directory for hosting static files
        Spark.externalStaticFileLocation("web");

        // Register handlers for each endpoint using the method reference syntax
        Spark.delete("/db", this::clear);
        Spark.post("/user", this::register);
        Spark.post("/session", this::login);
        Spark.delete("/session", this::logout);
        Spark.get("/game", this::listGames);
        Spark.post("/game", this::createGame);
        Spark.put("/game", this::joinGame);
    }

    private Object clear(Request req, Response res) {
        res.type("application/json");

        ClearService service = new ClearService();
        ClearResponse response = service.clear();

        if(response.getMessage() != null){
            res.status(500);
        }
        else{
            res.status(200);
        }
        return new Gson().toJson(response);
    }

    private Object register(Request req, Response res) {
        res.type("application/json");

        var body = new Gson().fromJson(req.body(), RegisterRequest.class);

        RegisterService service = new RegisterService();
        RegisterResponse response = service.newUser(body);

        if(response.getMessage() == "Error: bad request"){
            res.status(400);
        } else if(response.getMessage() == "Error: already taken"){
            res.status(403);
        } else if(response.getMessage() != null){
            res.status(500);
        } else{
            res.status(200);
        }
        return new Gson().toJson(response);
    }

    private Object login(Request req, Response res) {
        res.type("application/json");

        var body = new Gson().fromJson(req.body(), LoginRequest.class);

        LoginService service = new LoginService();
        LoginResponse response = service.login(body);

        if(response.getMessage() == "Error: unauthorized"){
            res.status(401);
        } else if(response.getMessage() != null){
            res.status(500);
        } else{
            res.status(200);
        }

        return new Gson().toJson(response);
    }

    private Object logout(Request req, Response res) {
        res.type("application/json");
        if(!validate(req)){
            res.status(401);
            return new Gson().toJson(new ListGamesResponse("Error: unauthorized"));
        }


        LogoutService service = new LogoutService();
        LogoutResponse response = service.logout(new LogoutRequest( new AuthTokenImpl("hi", req.headers("Authorization"))));

        if(response.getMessage() == "Error: unauthorized"){
            res.status(401);
        } else if(response.getMessage() != null){
            res.status(500);
        } else{
            res.status(200);
        }

        return new Gson().toJson(response);
    }

    private Object listGames(Request req, Response res) {
        res.type("application/json");
        var body = new Gson().fromJson(req.body(), ListGamesRequest.class);
        if(!validate(req)){
            res.status(401);
            return new Gson().toJson(new ListGamesResponse("Error: unauthorized"));
        }

        ListGameService service = new ListGameService();
        ListGamesResponse response = service.listGames(new ListGamesRequest(new AuthTokenImpl("hi", req.headers("Authorization"))));

        if(response.getMessage() == "Error: unauthorized"){
            res.status(401);
        } else if(response.getMessage() != null){
            res.status(500);
        }

        res.status(200);
        return new Gson().toJson(response);
    }

    private Object createGame(Request req, Response res) {
        GameDAOImpl games = new GameDAOImpl();
        try{
            games.listGames();
        }catch (DataAccessException e){

        }

        res.type("application/json");

        if(!validate(req)){
            res.status(401);
            return new Gson().toJson(new ListGamesResponse("Error: unauthorized"));
        }

        var body = new Gson().fromJson(req.body(), CreateGameRequest.class);

        //String name = body.getName();

        CreateGameRequest request = new CreateGameRequest(body.getName(), new AuthTokenImpl("hi", req.headers("Authorization")));

        CreateGameService service = new CreateGameService();
        CreateGameResponse response = service.createGame(request);

        if(response.getMessage() == "Error: bad request"){
            res.status(400);
        } else if(response.getMessage() == "Error: unauthorized"){
            res.status(401);
        } else if(response.getMessage() != null){
            res.status(500);
        } else{
            res.status(200);
        }

        return new Gson().toJson(response);
    }

    private Object joinGame(Request req, Response res) {
        res.type("application/json");

        if(!validate(req)){
            res.status(401);
            return new Gson().toJson(new ListGamesResponse("Error: unauthorized"));
        }

        var body = new Gson().fromJson(req.body(), JoinGameRequest.class);


        AuthDAOImpl auths = new AuthDAOImpl();
        String token = req.headers("Authorization");
        JoinGameRequest request = new JoinGameRequest(body.getPlayerColor(), body.getGameID(), new AuthTokenImpl(auths.getUserofAuth(token), req.headers("Authorization")));

        JoinGameService service = new JoinGameService();
        JoinGameResponse response = service.joinGame(request);

        if(response.getMessage() == "Error: bad request"){
            res.status(400);
        } else if(response.getMessage() == "Error: unauthorized"){
            res.status(401);
        } else if(response.getMessage() == "Error: already taken"){
            res.status(403);
        }else if(response.getMessage() != null){
            res.status(500);
        } else{
            res.status(200);
        }

        return new Gson().toJson(response);
    }

    private Boolean validate(Request req){
        var authToken = req.headers("Authorization");
        AuthDAOImpl auths = new AuthDAOImpl();

        try{
            auths.verifyAuthToken(authToken);
        }catch (DataAccessException e){

            return false;
        }
        return true;
    }


}
