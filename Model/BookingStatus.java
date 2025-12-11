package Model;

public enum BookingStatus {
    MENUNGGU_PERSETUJUAN,
    DISETUJUI,
    DITOLAK,
    DIBATALKAN,
    SELESAI;

    @Override
    public String toString() {
        switch (this) {
            case MENUNGGU_PERSETUJUAN: return "Menunggu";
            case DISETUJUI: return "Disetujui";
            case DITOLAK: return "Ditolak";
            case DIBATALKAN: return "Dibatalkan";
            case SELESAI: return "Selesai";
            default: return super.toString();
        }
    }
    
    public static BookingStatus fromString(String status) {
        for (BookingStatus b : BookingStatus.values()) {
            if (b.toString().equalsIgnoreCase(status) || b.name().equalsIgnoreCase(status)) {
                return b;
            }
        }
        return MENUNGGU_PERSETUJUAN; // Default or throw exception
    }
}
