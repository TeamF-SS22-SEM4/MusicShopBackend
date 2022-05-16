package at.fhv.ss22.ea.f.musicshop.backend.domain.model.soundcarrier;

public enum SoundCarrierType {
    VINYL("Vinyl"),
    CD("CD"),
    CASSETTE("Cassette"),
    DIGITAL("Digital");

    // To display it with a friendly name in the gui
    private final String friendlyName;

    SoundCarrierType(String aFriendlyName) {
        this.friendlyName = aFriendlyName;
    }

    public String getFriendlyName() {
        return this.friendlyName;
    }
}