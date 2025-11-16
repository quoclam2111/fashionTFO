package adapters.Home;

public class SessionManager {
    private static SessionManager instance;
    private User currentUser;

    private SessionManager() {

        this.currentUser = new User();
    }

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void login(User user) {
        this.currentUser = user;
    }

    public void logout() {
        this.currentUser = new User();
    }

    public boolean isLoggedIn() {
        return !currentUser.isGuest();
    }
}
