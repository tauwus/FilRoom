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

    @Override
    public String getRole() {
        return "ADMIN";
    }
}