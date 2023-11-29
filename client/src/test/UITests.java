import com.google.gson.Gson;
import org.junit.jupiter.api.*;
import ui.EscapeSequences;
import ui.PreGame;
import ui.ServerFacade;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UITests {
    private static String authToken = null;
    private static Boolean loggedIn = false;

    private static PreGame ui = new PreGame();
    private static ServerFacade facade = new ServerFacade();

    @BeforeEach
    public void clearAll() throws IOException, URISyntaxException {
        EscapeSequences lib = new EscapeSequences();
        lib.editMessage("Hello World");

        URI uri = new URI("http://localhost:8080/db");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("DELETE");
        http.setDoOutput(true);

        http.connect();


        try (InputStream respBody = http.getInputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(respBody);

            var response = new Gson().fromJson(inputStreamReader, Map.class);
            System.out.println(response);
        }
    }

    @Test
    @Order(1)
    @DisplayName("Register Success")
    public void registerSuccess() throws URISyntaxException, IOException {
        String[] input = new String[4];
        input[0] = "register";
        input[1] = "a";
        input[2] = "b";
        input[3] = "c";

        Assertions.assertTrue(facade.register(input));
    }
    @Test
    @Order(2)
    @DisplayName("Register Failure")
    public void registerFailure() throws IOException, URISyntaxException {
        String[] input = new String[3];
        input[0] = "register";
        input[1] = "a";
        input[2] = "b";

        Assertions.assertFalse(facade.register(input));
    }
    @Test
    @Order(3)
    @DisplayName("Login Success")
    public void loginSuccess() throws IOException, URISyntaxException {
        String[] input1 = new String[4];
        input1[0] = "register";
        input1[1] = "a";
        input1[2] = "b";
        input1[3] = "c";

        facade.register(input1);

        String[] input2 = new String[3];
        input2[0] = "login";
        input2[1] = "a";
        input2[2] = "b";

        facade.logout();
        Assertions.assertTrue(facade.login(input2));
    }
    @Test
    @Order(4)
    @DisplayName("Login Failure")
    public void loginFailure() throws IOException, URISyntaxException {
        String[] input = new String[3];
        input[0] = "login";
        input[1] = "a";
        input[2] = "b";

        Assertions.assertFalse(facade.login(input));
    }

    @Test
    @Order(5)
    @DisplayName("Create Game Success")
    public void createGameSuccess() throws IOException, URISyntaxException {
        String[] input = new String[4];
        input[0] = "register";
        input[1] = "a";
        input[2] = "b";
        input[3] = "c";

        facade.register(input);

        String[] input2 = new String[2];
        input2[0] = "create";
        input2[1] = "WarGames";
        facade.createGame(input2);

        EscapeSequences lib = new EscapeSequences();
        Assertions.assertEquals("create success", lib.getMessage());

    }
    @Test
    @Order(6)
    @DisplayName("Create Game Failure")
    public void createGameFailure() throws IOException, URISyntaxException {
        String[] input = new String[2];
        input[0] = "create";
        input[1] = "WarGames";
        facade.createGame(input);

        EscapeSequences lib = new EscapeSequences();
        Assertions.assertNotEquals("create success", lib.getMessage());
    }

    @Test
    @Order(7)
    @DisplayName("Join Game Success")
    public void joinGameSuccess() throws IOException, URISyntaxException {
        String[] input = new String[4];
        input[0] = "register";
        input[1] = "a";
        input[2] = "b";
        input[3] = "c";
        facade.register(input);

        String[] input2 = new String[2];
        input2[0] = "create";
        input2[1] = "WarGames";
        facade.createGame(input2);
        facade.resetGames();

        String[] input3 = new String[3];
        input3[0] = "join";
        input3[1] = "1";
        input3[2] = "WHITE";
        facade.joinGame(input3);

        EscapeSequences lib = new EscapeSequences();
        Assertions.assertEquals("join success", lib.getMessage());

    }
    @Test
    @Order(8)
    @DisplayName("Join Game Failure")
    public void joinGameFailure() throws IOException, URISyntaxException {
        String[] input = new String[4];
        input[0] = "register";
        input[1] = "a";
        input[2] = "b";
        input[3] = "c";
        facade.register(input);

        String[] input2 = new String[2];
        input2[0] = "create";
        input2[1] = "WarGames";
        facade.createGame(input2);
        facade.resetGames();

        String[] input3 = new String[3];
        input3[0] = "join";
        input3[1] = "1";
        input3[2] = "WHITE";
        facade.joinGame(input3);
        EscapeSequences lib = new EscapeSequences();
        lib.editMessage("Hello World");

        facade.resetGames();
        facade.joinGame(input3);

        Assertions.assertNotEquals("join success", lib.getMessage());
    }

    @Test
    @Order(9)
    @DisplayName("Observe Game Success")
    public void observeGameSuccess() throws IOException, URISyntaxException {
        String[] input = new String[4];
        input[0] = "register";
        input[1] = "a";
        input[2] = "b";
        input[3] = "c";
        facade.register(input);

        String[] input2 = new String[2];
        input2[0] = "create";
        input2[1] = "WarGames";
        facade.createGame(input2);
        facade.resetGames();

        String[] input3 = new String[2];
        input3[0] = "observe";
        input3[1] = "1";
        facade.observeGame(input3);

        EscapeSequences lib = new EscapeSequences();
        Assertions.assertEquals("observe success", lib.getMessage());

    }
    @Test
    @Order(10)
    @DisplayName("Observe Game Failure")
    public void observeGameFailure() throws IOException, URISyntaxException {
        String[] input = new String[4];
        input[0] = "register";
        input[1] = "a";
        input[2] = "b";
        input[3] = "c";
        facade.register(input);

        String[] input2 = new String[2];
        input2[0] = "create";
        input2[1] = "WarGames";
        facade.createGame(input2);
        facade.resetGames();

        String[] input3 = new String[2];
        input3[0] = "observe";
        input3[1] = "2";
        facade.observeGame(input3);
        EscapeSequences lib = new EscapeSequences();
        lib.editMessage("Hello World");

        Assertions.assertNotEquals("observe success", lib.getMessage());

    }

    @Test
    @Order(11)
    @DisplayName("List Game Success")
    public void listGameSuccess() throws IOException, URISyntaxException {
        String[] input = new String[4];
        input[0] = "register";
        input[1] = "a";
        input[2] = "b";
        input[3] = "c";
        facade.register(input);

        String[] input2 = new String[2];
        input2[0] = "create";
        input2[1] = "WarGames";
        facade.createGame(input2);
        facade.resetGames();
        String[] input3 = new String[1];
        input3[0] = "list";
        facade.ListGames(input3);

        EscapeSequences lib = new EscapeSequences();
        Assertions.assertEquals(" 1", lib.getMessage());
    }
    @Test
    @Order(12)
    @DisplayName("List Game Failure")
    public void listGameFailure() throws IOException, URISyntaxException {
        String[] input = new String[4];
        input[0] = "register";
        input[1] = "a";
        input[2] = "b";
        input[3] = "c";
        facade.register(input);

        String[] input3 = new String[1];
        input3[0] = "list";
        facade.ListGames(input3);

        EscapeSequences lib = new EscapeSequences();
        Assertions.assertNotEquals(" 1", lib.getMessage());
    }
    @Test
    @Order(13)
    @DisplayName("Logout Success")
    public void logoutSuccess() throws IOException, URISyntaxException {
        String[] input1 = new String[4];
        input1[0] = "register";
        input1[1] = "a";
        input1[2] = "b";
        input1[3] = "c";
        facade.register(input1);

        facade.logout();

        EscapeSequences lib = new EscapeSequences();
        Assertions.assertEquals("logout success", lib.getMessage());

    }
    @Test
    @Order(14)
    @DisplayName("Logout Failure")
    public void logoutFailure() throws IOException, URISyntaxException {
        facade.logout();

        EscapeSequences lib = new EscapeSequences();
        Assertions.assertNotEquals("logout success", lib.getMessage());
    }
}
