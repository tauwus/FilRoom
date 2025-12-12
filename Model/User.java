package Model;

public abstract class User {
    protected int id;
    protected String name;

    public User(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public abstract String getRole();

    @Override
    public String toString() {
        return name + " (" + getRole() + ")";
    }
}