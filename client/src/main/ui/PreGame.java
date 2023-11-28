package ui;

import Objects.Game;
import Objects.GameImpl;
import Responses.ListGamesResponse;
import chess.ChessGameImpl;
import chess.ChessPieceImpl;
import chess.ChessPositionImpl;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dataAccess.DataAccessException;
import deserializers.GameDeserializer;
import deserializers.PositionDeserializer;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class PreGame {
    private static String authToken = null;
    private static Boolean loggedIn = false;

    public static void main(String[] args) throws URISyntaxException, IOException {
        System.out.printf("Welcome to Chess 240! Type help to get started.\n");
        Scanner scanner = new Scanner(System.in);

        String userName = null;
        URI uri = null;
        HttpURLConnection http = null;


        Boolean quit = false;


        while(!quit) {
            String line = scanner.nextLine();
            var input = line.split(" ");
            if (!loggedIn) {
                switch (input[0]) {
                    case "help":
                        System.out.printf("register <USERNAME> <PASSWORD> <EMAIL> - to create an account\n" +
                                "login <USERNAME> <PASSWORD> - to play chess\n" +
                                "quit - playing chess\n" +
                                "help - print out possible commands\n");
                        break;
                    case "quit":
                        quit = true;
                        System.out.printf("Thank you for playing.\n");
                        break;
                    case "register":
                        if(input.length >= 4){
                            uri = new URI("http://localhost:8080/user");
                            http = (HttpURLConnection) uri.toURL().openConnection();
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
                            }

                            try (InputStream respBody = http.getInputStream()) {
                                InputStreamReader inputStreamReader = new InputStreamReader(respBody);

                                var response = new Gson().fromJson(inputStreamReader, HashMap.class);
                                authToken = (String)response.get("authToken");
                                userName = (String)response.get("userName");
                                System.out.println(response);
                            }

                        }else{
                            System.out.printf("Invalid input. Please enter - register <USERNAME> <PASSWORD> <EMAIL>\n");
                        }
                        break;
                    case "login":
                        if(input.length >= 3){
                            uri = new URI("http://localhost:8080/session");
                            http = (HttpURLConnection) uri.toURL().openConnection();
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
                            }

                            try (InputStream respBody = http.getInputStream()) {
                                InputStreamReader inputStreamReader = new InputStreamReader(respBody);

                                var response = new Gson().fromJson(inputStreamReader, Map.class);
                                authToken = (String)response.get("authToken");
                                userName = (String)response.get("userName");
                                System.out.println(response);
                            }

                        }else{
                            System.out.printf("Invalid input. Please enter - login <USERNAME> <PASSWORD>\n");
                        }
                        break;
                    default:
                        System.out.printf("Invalid Input. Please try again, or press enter help for more options.\n");

                }

            } else {
                switch (input[0]) {
                    case "help":
                        System.out.printf("create <NAME> - a game\n" +
                                "list - games\n" +
                                "join <ID> [WHITE|BLACK|<empty>] - a game\n" +
                                "observe <ID> - a game\n" +
                                "logout - when you are done\n" +
                                "quit - playing chess\n" +
                                "help - print out possible commands\n");
                        break;
                    case "quit":
                        quit = true;
                        logout();
                        break;
                    case "create":
                        if(input.length < 2){
                            System.out.printf("Invalid input.\n");
                            break;
                        }
                        uri = new URI("http://localhost:8080/game");
                        http = (HttpURLConnection) uri.toURL().openConnection();
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
                            break;
                        }

                        try (InputStream respBody = http.getInputStream()) {
                            InputStreamReader inputStreamReader = new InputStreamReader(respBody);

                            var response = new Gson().fromJson(inputStreamReader, Map.class);
                            System.out.println(response);
                        }
                        break;
                    case "list":
                        uri = new URI("http://localhost:8080/game");
                        http = (HttpURLConnection) uri.toURL().openConnection();
                        http.setRequestMethod("GET");
                        http.setDoOutput(true);

                        http.addRequestProperty("authorization", authToken);

                        http.connect();

                        if(http.getResponseCode() == 401){
                            System.out.printf("Error, unauthorized.\n");
                            break;
                        }

                        var builder = new GsonBuilder();
                        builder.registerTypeAdapter(ChessPieceImpl.class, new GameDeserializer());
                        builder.registerTypeAdapter(ChessPositionImpl.class, new PositionDeserializer());

                        ArrayList<GameImpl> games = null;

                        try (InputStream respBody = http.getInputStream()) {
                            InputStreamReader inputStreamReader = new InputStreamReader(respBody);
                            //var response = builder.create().fromJson(inputStreamReader,ListGamesResponse.class);
                            var response = new Gson().fromJson(inputStreamReader, ListGamesResponse.class);
                            //games = response.getList();
                            System.out.println(response);
                            System.out.println(games.get(0));
                        }

                        if(games == null){
                            break;
                        }
                        //int i = 1;

                        for(int i = 1; i <= games.size(); i++){
                            //Game game = builder.create().fromJson(games.get(i), ChessGameImpl.class);
                            Game game = games.get(i - 1);
                            System.out.printf(i + ". "+ game.getName() + " (ID: " + game.getId() + ") "+"\n");
                            System.out.printf("    White Player: " + game.getWhiteUsername());
                            System.out.printf("    Black Player: " + game.getBlackUsername());
                        }
                        break;
                    case "join":
                        if(input.length < 2){
                            System.out.printf("Invalid input.\n");
                            break;
                        }
                        uri = new URI("http://localhost:8080/game");
                        http = (HttpURLConnection) uri.toURL().openConnection();
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
                            break;
                        }

                        try (InputStream respBody = http.getInputStream()) {
                            InputStreamReader inputStreamReader = new InputStreamReader(respBody);

                            var response = new Gson().fromJson(inputStreamReader, Map.class);
                            System.out.println(response);
                        }
                        break;
                    case "observe":
                        if(input.length < 2){
                            System.out.printf("Invalid input.\n");
                            break;
                        }
                        uri = new URI("http://localhost:8080/game");
                        http = (HttpURLConnection) uri.toURL().openConnection();
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
                            break;
                        }

                        try (InputStream respBody = http.getInputStream()) {
                            InputStreamReader inputStreamReader = new InputStreamReader(respBody);

                            var response = new Gson().fromJson(inputStreamReader, Map.class);
                            System.out.println(response);
                        }
                        break;
                    case "logout":
                        logout();
                        break;
                    default:
                        System.out.printf("Invalid Input. Please try again, or press enter help for more options.\n");
                        break;
                }

            }

        }

/*
        for (var i = 0; i < args.length; i++) {
            System.out.printf("%d. %s%n", i+1, args[i]);
        }*/
    }

    public static void logout() throws IOException, URISyntaxException {
        URI uri = new URI("http://localhost:8080/session");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("DELETE");
        http.setDoOutput(true);

        http.addRequestProperty("authorization", authToken);

        http.connect();

        if(http.getResponseCode() == 401){
            System.out.printf("Error, unauthorized.\n");
            return;
        }

        if(http.getResponseCode() == 200){
            loggedIn = false;
            authToken = null;
            System.out.printf("You are now logged out.\n");
        }

        try (InputStream respBody = http.getInputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(respBody);

            var response = new Gson().fromJson(inputStreamReader, Map.class);
            System.out.println(response);
        }
    }
}
