package at.fhv.ss22.ea.f.musicshop.backend.domain.model.product;

import at.fhv.ss22.ea.f.musicshop.backend.domain.Generated;

import javax.persistence.*;
import java.util.Objects;

@Embeddable
public class Song {

    @Column
    private String title;
    @Column
    private String duration;

    protected Song() {}
    public static Song create(String aTitle, String aDuration) {
        return new Song(aTitle, aDuration);
    }

    private Song(String aTitle, String aDuration) {
        this.title = aTitle;
        this.duration = aDuration;
    }

    public String getTitle() {
        return title;
    }

    public String getDuration() {
        return duration;
    }

    @Generated
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Song song = (Song) o;
        return Objects.equals(title, song.title) && Objects.equals(duration, song.duration);
    }

    @Generated
    @Override
    public int hashCode() {
        return Objects.hash(title, duration);
    }
}
