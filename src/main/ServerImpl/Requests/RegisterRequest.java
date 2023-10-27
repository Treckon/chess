package ServerImpl.Requests;

/**
 * Request class for containing info to register users.
 *
 * String username,
 * String password,
 * String Email
 */
public class RegisterRequest implements Request {
    private String username;
    private String password;
    private String email;

    /**
     *
     * @param u - username of user to be created
     * @param p - password of user to be created
     * @param e - email of user to be created
     *
     */
    public RegisterRequest(String u, String p, String e){
        username = u;
        password = p;
        email = e;
    }

    public String getUsername(){
        return username;
    }
    public String getPassword(){
        return password;
    }
    public String getEmail(){
        return email;
    }



}
