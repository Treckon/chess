package ServerImpl.Requests;
/**
 * Request class for containing info to log in users.
 *
 * String username,
 * String password
 */
public class LoginRequest implements Request{
    private String username;
    private String password;

    /**
     *
     * @param u - username of user to log in
     * @param p - password of user to log in
     *
     */
    public LoginRequest(String u, String p){
        username = u;
        password = p;

    }

    public String getUsername(){
        return username;
    }
    public String getPassword(){
        return password;
    }
}
