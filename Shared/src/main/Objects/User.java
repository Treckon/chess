package Objects;
/**
 * Object that contains user information, such as username, password, and email.
 */
public interface User {
    /**
     * Returns current User's username.
     */
    public String getUsername();
    /**
     * Returns current User's password.
     */
    public String getPassword();
    /**
     * Returns current User's email.
     */
    public String getEmail();
}
