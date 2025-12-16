package Model;

public class CivitasAkademik extends User {
    private String nim;
    private String email;
    private AccountStatus statusAkun;

    public CivitasAkademik(int id, String name, String nim, String email, AccountStatus statusAkun) {
        super(id, name);
        this.nim = nim;
        this.email = email;
        this.statusAkun = statusAkun;
    }

    public CivitasAkademik(int id, String name, String nim, String email, String statusAkun) {
        super(id, name);
        this.nim = nim;
        this.email = email;
        this.statusAkun = AccountStatus.fromString(statusAkun);
    }

    public String getNim() { return nim; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public AccountStatus getStatusAkun() { return statusAkun; }
    public void setStatusAkun(AccountStatus statusAkun) { this.statusAkun = statusAkun; }

    @Override
    public String getRole() {
        return "CIVITAS";
    }

    @Override
    public String toString() {
        return getName() + " (" + nim + ")";
    }
}