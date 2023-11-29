package ui;

import Objects.Game;
import Objects.GameImpl;
import Responses.ListGamesResponse;
import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPositionImpl;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import deserializers.GameEntryDeserializer;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ServerFacade {
    private static String authToken = null;
    private static Boolean loggedIn = false;
    EscapeSequences lib = new EscapeSequences();

    private static ArrayList<GameImpl> currentGames;

    public boolean register(String[] input) throws IOException, URISyntaxException {
        if(input.length >= 4){
            URI uri = new URI("http://localhost:8080/user");
            HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
            http.setRequestMethod("POST");
            http.setDoOutput(true);

            var body = Map.of("username", input[1], "password", input[2],"email", input[3]);
            try (var outputStream = http.getOutputStream()) {
                var jsonBody = new Gson().toJson(body);
                outputStream.write(jsonBody.getBytes());
            }

            http.connect();

            if(http.getResponseCode() == 200){
                loggedIn = true;
                System.out.printf("Successfully registered. You are now logged in.\n");
            }else if(http.getResponseCode() == 403){
                System.out.printf("Error, already taken.\n");
                return false;
            }else{
                System.out.printf("General Error:\n" + http.getResponseMessage() + "\n");
                return false;
            }

            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader inputStreamReader = new InputStreamReader(respBody);

                var response = new Gson().fromJson(inputStreamReader, HashMap.class);
                authToken = (String)response.get("authToken");
                System.out.println(response);
            }

        }else{
            System.out.printf("Invalid input. Please enter - register <USERNAME> <PASSWORD> <EMAIL>\n");
            return false;
        }
        return true;
    }

    public Boolean login(String[] input) throws IOException, URISyntaxException{
        if(input.length >= 3){
            URI uri = new URI("http://localhost:8080/session");
            HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
            http.setRequestMethod("POST");
            http.setDoOutput(true);

            var body = Map.of("username", input[1], "password", input[2]);
            try (var outputStream = http.getOutputStream()) {
                var jsonBody = new Gson().toJson(body);
                outputStream.write(jsonBody.getBytes());
            }

            http.connect();

            if(http.getResponseCode() == 200){
                loggedIn = true;
                System.out.printf("You are now logged in.\n");
            }else if(http.getResponseCode() == 401){
                System.out.printf("Error, unauthorized.\n");
                return false;
            } else{
                System.out.printf("General Error:\n" + http.getResponseMessage() + "\n");
                return false;
            }

            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader inputStreamReader = new InputStreamReader(respBody);

                var response = new Gson().fromJson(inputStreamReader, Map.class);
                authToken = (String)response.get("authToken");
                System.out.println(response);
            }

        }else{
            System.out.printf("Invalid input. Please enter - login <USERNAME> <PASSWORD>\n");
        }
        return true;
    }
    public void logout() throws IOException, URISyntaxException {
        URI uri = new URI("http://localhost:8080/session");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("DELETE");
        http.setDoOutput(true);

        http.addRequestProperty("authorization", authToken);

        http.connect();

        if(http.getResponseCode() == 401){
            System.out.printf("Error, unauthorized.\n");
            return;
        }else if(http.getResponseCode() == 500){
            System.out.printf("General Error:\n" + http.getResponseMessage() + "\n");
            return;
        }

        if(http.getResponseCode() == 200){
            loggedIn = false;
            authToken = null;
            System.out.printf("You are now logged out.\n");
            lib.editMessage("logout success");
        }

        try (InputStream respBody = http.getInputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(respBody);

            var response = new Gson().fromJson(inputStreamReader, Map.class);
            System.out.println(response);
        }
    }
    public void createGame(String[] input) throws IOException, URISyntaxException{
        if(input.length < 2){
            System.out.printf("Invalid input.\n");
            return;
        }
        URI uri = new URI("http://localhost:8080/game");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("POST");
        http.setDoOutput(true);

        http.addRequestProperty("authorization", authToken);

        var body = Map.of("gameName", input[1]);
        try (var outputStream = http.getOutputStream()) {
            var jsonBody = new Gson().toJson(body);
            outputStream.write(jsonBody.getBytes());
        }

        http.connect();

        if(http.getResponseCode() == 401){
            System.out.printf("Error, unauthorized.\n");
            return;
        } else if(http.getResponseCode() == 400){
            System.out.printf("Error, bad request.\n");
            return;
        }else if(http.getResponseCode() == 500){
            System.out.printf("General Error:\n" + http.getResponseMessage() + "\n");
            return;
        }

        try (InputStream respBody = http.getInputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(respBody);

            var response = new Gson().fromJson(inputStreamReader, Map.class);
            System.out.println(response);
        }
        lib.editMessage("create success");
    }
    public void joinGame(String[] input) throws IOException, URISyntaxException{
        if(input.length < 2){
            System.out.printf("Invalid input.\n");
            return;
        }

        Boolean exists = false;
        ChessGame currentGame = null;
        for(GameImpl game: currentGames){
            if(game.getId() == Integer.parseInt(input[1])){
                exists = true;
                currentGame = game.getGame();
            }
        }
        if(!exists){
            System.out.println("Game Id not recognized.\n");
            return;
        }


        URI uri = new URI("http://localhost:8080/game");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("PUT");
        http.setDoOutput(true);

        http.addRequestProperty("authorization", authToken);
        http.addRequestProperty("Content-Type", "application/json");

        if(input.length == 2){
            try {
                var joinBody = Map.of("gameID", Integer.parseInt(input[1]));
                try (var outputStream = http.getOutputStream()) {
                    var jsonBody = new Gson().toJson(joinBody);
                    outputStream.write(jsonBody.getBytes());
                }
            }catch (Exception e){
                System.out.printf("Invalid input.\n");
            }
        }else{
            try {
                var joinBody = Map.of("gameID", Integer.parseInt(input[1]), "playerColor", input[2]);
                try (var outputStream = http.getOutputStream()) {
                    var jsonBody = new Gson().toJson(joinBody);
                    outputStream.write(jsonBody.getBytes());
                }
            }catch(Exception e){
                System.out.printf("Invalid input.\n");
            }
        }


        http.connect();

        if(http.getResponseCode() == 401){
            System.out.printf("Error, unauthorized.\n");
            return;
        }else if(http.getResponseCode() == 403){
            System.out.printf("Sorry, already taken.\n");
            return;
        }else if(http.getResponseCode() == 500){
            System.out.printf("General Error:\n" + http.getResponseMessage() + "\n");
            return;
        }else if(http.getResponseCode() == 400){
            System.out.printf("Bad Request\n");
            return;
        }

        try (InputStream respBody = http.getInputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(respBody);

            var response = new Gson().fromJson(inputStreamReader, Map.class);
            System.out.println(response);
        }
        lib.editMessage("join success");
        printBoards(currentGame);
    }
    public void observeGame(String[] input) throws IOException, URISyntaxException{
        Boolean existsObserve = false;
        ChessGame currentGameObserve = null;
        for(GameImpl game: currentGames){
            if(game.getId() == Integer.parseInt(input[1])){
                existsObserve = true;
                currentGameObserve = game.getGame();
            }
        }
        if(!existsObserve){
            System.out.println("Game Id not recognized.\n");
            return;
        }


        if(input.length < 2){
            System.out.printf("Invalid input.\n");
            return;
        }
        URI uri = new URI("http://localhost:8080/game");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("PUT");
        http.setDoOutput(true);

        http.addRequestProperty("authorization", authToken);

        var observeBody = Map.of("gameID", input[1]);
        try (var outputStream = http.getOutputStream()) {
            var jsonBody = new Gson().toJson(observeBody);
            outputStream.write(jsonBody.getBytes());
        }

        http.connect();

        if(http.getResponseCode() == 401){
            System.out.printf("Error, unauthorized.\n");
            return;
        }

        try (InputStream respBody = http.getInputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(respBody);

            var response = new Gson().fromJson(inputStreamReader, Map.class);
            System.out.println(response);
        }
        lib.editMessage("observe success");
        printBoards(currentGameObserve);
    }
    public void ListGames(String[] input) throws IOException, URISyntaxException{
        URI uri = new URI("http://localhost:8080/game");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("GET");
        http.setDoOutput(true);

        http.addRequestProperty("authorization", authToken);

        http.connect();

        if(http.getResponseCode() == 401){
            System.out.printf("Error, unauthorized.\n");
            return;
        }else if(http.getResponseCode() == 500){
            System.out.printf("General Error:\n" + http.getResponseMessage() + "\n");
            return;
        }

        var builder = new GsonBuilder();
        //builder.registerTypeAdapter(ChessPieceImpl.class, new GameDeserializer());
        //builder.registerTypeAdapter(ChessPositionImpl.class, new PositionDeserializer());
        builder.registerTypeAdapter(ChessGame.class, new GameEntryDeserializer());
        ArrayList<GameImpl> games = null;

        try (InputStream respBody = http.getInputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(respBody);
            var response = builder.create().fromJson(inputStreamReader, ListGamesResponse.class);
            //var response = new Gson().fromJson(inputStreamReader, ListGamesResponse.class);
            games = response.getList();
            lib.editMessage(" " + games.size());
            System.out.println(response);
            if(games.isEmpty() || games == null){
                return;
            }else{
                System.out.println(games.get(0));
            }
        }


        //int i = 1;

        for(int i = 1; i <= games.size(); i++){
            //Game game = builder.create().fromJson(games.get(i), ChessGameImpl.class);
            Game game = games.get(i - 1);
            System.out.printf(i + ". "+ game.getName() + " (ID: " + game.getId() + ") "+"\n");
            System.out.printf("    White Player: " + game.getWhiteUsername() + "\n");
            System.out.printf("    Black Player: " + game.getBlackUsername() + "\n");
        }
    }
    public void resetGames() throws IOException, URISyntaxException {
        URI uri = new URI("http://localhost:8080/game");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("GET");
        http.setDoOutput(true);

        http.addRequestProperty("authorization", authToken);

        http.connect();

        var builder = new GsonBuilder();
        builder.registerTypeAdapter(ChessGame.class, new GameEntryDeserializer());
        ArrayList<GameImpl> games = null;

        try (InputStream respBody = http.getInputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(respBody);
            var response = builder.create().fromJson(inputStreamReader,ListGamesResponse.class);
            //var response = new Gson().fromJson(inputStreamReader, ListGamesResponse.class);
            games = response.getList();
            System.out.println(response);
            System.out.println(games.get(0));
        }

        if(games == null){
            return;
        }else{
            currentGames = games;
        }
    }

    public void printBoards(ChessGame currentGame){
        ChessBoard board = currentGame.getBoard();
        board.resetBoard();
        HashMap<Integer,Character> boardValues;
        //Black's perspective first
        System.out.print("\u001b[30:47m");
        System.out.printf("    h  g  f  e  d  c  b  a    ");
        System.out.print("\u001b[49m");
        System.out.println();
        for(int i = 1; i < 9; i++){
            System.out.print("\u001b[47m");
            System.out.printf(" " + i + " ");
            for(int j = 8; j > 0; j--){
                ChessPiece piece = board.getPiece(new ChessPositionImpl(i, j));
                if((i + j) % 2 == 0){
                    System.out.print("\u001b[107m");
                }else{
                    System.out.print("\u001b[40m");
                }
                if(piece != null){
                    ChessPiece.PieceType type = piece.getPieceType();
                    ChessGame.TeamColor color = piece.getTeamColor();
                    switch(color){
                        case BLACK -> System.out.print("\u001b[31m");
                        case WHITE -> System.out.print("\u001b[34m");
                    }
                    switch(type) {
                        case KING:
                            System.out.printf(" K ");
                            break;
                        case QUEEN:
                            System.out.printf(" Q ");
                            break;
                        case ROOK:
                            System.out.printf(" R ");
                            break;
                        case KNIGHT:
                            System.out.printf(" N ");
                            break;
                        case BISHOP:
                            System.out.printf(" B ");
                            break;
                        case PAWN:
                            System.out.printf(" P ");
                            break;
                    }
                }else{
                    System.out.printf("   ");
                }
                System.out.print("\u001b[30:40m");
            }

            System.out.print("\u001b[47m");
            System.out.printf(" " + i + " ");
            System.out.print("\u001b[49m");
            System.out.println();
        }
        System.out.print("\u001b[47m");
        System.out.printf("    h  g  f  e  d  c  b  a    ");
        System.out.print("\u001b[49m");
        System.out.println();

        System.out.println();

        System.out.print("\u001b[30:47m");
        System.out.printf("    a  b  c  d  e  f  g  h    ");
        System.out.print("\u001b[49m");
        System.out.println();
        for(int i = 8; i > 0; i--){
            System.out.print("\u001b[47m");
            System.out.printf(" " + i + " ");
            for(int j = 1; j < 9; j++){
                if((i + j) % 2 == 0){
                    System.out.print("\u001b[107m");
                }else{
                    System.out.print("\u001b[40m");
                }
                ChessPiece piece = board.getPiece(new ChessPositionImpl(i, j));
                if(piece != null){
                    ChessPiece.PieceType type = piece.getPieceType();
                    ChessGame.TeamColor color = piece.getTeamColor();
                    switch(color){
                        case BLACK -> System.out.print("\u001b[31m");
                        case WHITE -> System.out.print("\u001b[34m");
                    }
                    switch(type) {
                        case KING:
                            System.out.printf(" K ");
                            break;
                        case QUEEN:
                            System.out.printf(" Q ");
                            break;
                        case ROOK:
                            System.out.printf(" R ");
                            break;
                        case KNIGHT:
                            System.out.printf(" N ");
                            break;
                        case BISHOP:
                            System.out.printf(" B ");
                            break;
                        case PAWN:
                            System.out.printf(" P ");
                            break;
                    }
                }else{
                    System.out.printf("   ");
                }
                System.out.print("\u001b[30:40m");
            }
            System.out.print("\u001b[47m");
            System.out.printf(" " + i + " ");
            System.out.print("\u001b[49m");
            System.out.println();
        }
        System.out.print("\u001b[47m");
        System.out.printf("    a  b  c  d  e  f  g  h    ");
        System.out.print("\u001b[49m");
        System.out.println();
        System.out.print("\u001b[39:49m");
    }
}
