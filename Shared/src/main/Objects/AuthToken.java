package Objects;
/**
 * Object that contains a username that has logged in, with its designated AuthToken string.
 */
public interface AuthToken {
    /**
     * Returns current AuthToken value
     */
    public String getToken();
    /**
     * Returns current AuthToken Username
     */
    public String getUsername();
}
