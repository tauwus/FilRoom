package Model;

public class CivitasAkademik extends User {
    private String nim;
    private String email;
    private String roleType; // Mahasiswa/Dosen/Staf

    public CivitasAkademik(int id, String name, String nim, String email, String roleType) {
        super(id, name);
        this.nim = nim;
        this.email = email;
        this.roleType = roleType;
    }

    public String getNim() {
        return nim;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String getRole() {
        return roleType;
    }
}