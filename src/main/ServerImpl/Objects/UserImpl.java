package ServerImpl.Objects;

public class UserImpl implements User {
    private String username;
    private String password;
    private String email;
    public UserImpl(String u, String p, String e){
        username = u;
        password = p;
        email = e;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getEmail() {
        return email;
    }
}
