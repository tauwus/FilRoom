package Model;

public class CivitasAkademik extends User {
    private String nim;
    private String email;
    private String noTelepon;
    private AccountStatus statusAkun;

    public CivitasAkademik(int id, String name, String nim, String email, String noTelepon, AccountStatus statusAkun) {
        super(id, name);
        this.nim = nim;
        this.email = email;
        this.noTelepon = noTelepon;
        this.statusAkun = statusAkun;
    }

    public CivitasAkademik(int id, String name, String nim, String email, String noTelepon, String statusAkun) {
        super(id, name);
        this.nim = nim;
        this.email = email;
        this.noTelepon = noTelepon;
        this.statusAkun = AccountStatus.fromString(statusAkun);
    }

    public String getNim() { return nim; }
    public String getEmail() { return email; }
    public String getNoTelepon() { return noTelepon; }
    public AccountStatus getStatusAkun() { return statusAkun; }

    @Override
    public String getRole() {
        return "CIVITAS";
    }
}