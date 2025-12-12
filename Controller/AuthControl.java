package Controller;

import Model.Admin;
import Model.User;

public class AuthControl {
    private static User currentUser = null;

    public static void createSession(User user) {
        currentUser = user;
    }

    public static void destroySession() {
        currentUser = null;
    }
    
    public static void logout() {
        destroySession();
    }

    public static boolean isLoggedIn() {
        return currentUser != null;
    }

    public static User getCurrentUser() {
        return currentUser;
    }
    
    public static Admin getCurrentAdmin() {
        if (currentUser instanceof Admin) {
            return (Admin) currentUser;
        }
        return null;
    }
}