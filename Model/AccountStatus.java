package Model;

public enum AccountStatus {
    AKTIF,
    NONAKTIF,
    DIBEKUKAN;

    @Override
    public String toString() {
        switch (this) {
            case AKTIF: return "Aktif";
            case NONAKTIF: return "Non-Aktif";
            case DIBEKUKAN: return "Dibekukan";
            default: return super.toString();
        }
    }

    public static AccountStatus fromString(String status) {
        for (AccountStatus a : AccountStatus.values()) {
            if (a.toString().equalsIgnoreCase(status) || a.name().equalsIgnoreCase(status)) {
                return a;
            }
        }
        return NONAKTIF; // Default
    }
}
