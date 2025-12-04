package Model;

public class Admin extends User {
    public Admin(int id, String name) {
        super(id, name);
    }

    @Override
    public String getRole() {
        return "ADMIN";
    }
}