package Model;

public class Admin extends User {
    private String username;

    public Admin(int id, String name, String username) {
        super(id, name);
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getRole() {
        return "ADMIN";
    }

    @Override
    public String toString() {
        return getName() + " (@" + username + ")";
    }
}