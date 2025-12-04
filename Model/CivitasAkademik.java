package Model;

public class CivitasAkademik extends User {
    private String nim;
    private String email;
    private String roleType; // Mahasiswa/Dosen/Staf
    private String phoneNumber;
    private String status; // "aktif", "nonaktif"

    public CivitasAkademik(int id, String name, String nim, String email, String roleType, String phoneNumber, String status) {
        super(id, name);
        this.nim = nim;
        this.email = email;
        this.roleType = roleType;
        this.phoneNumber = phoneNumber;
        this.status = status;
    }

    public String getNim() {
        return nim;
    }

    public String getEmail() {
        return email;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String getRole() {
        return roleType;
    }
}