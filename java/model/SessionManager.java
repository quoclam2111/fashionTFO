package model;

public class SessionManager {
    private static SessionManager instance;
    private User currentUser;
    
    private SessionManager() {
        // Mặc định là Guest khi khởi động
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
        this.currentUser = new User(); // Reset về Guest
    }
    
    public boolean isLoggedIn() {
        return !currentUser.isGuest();
    }
}
