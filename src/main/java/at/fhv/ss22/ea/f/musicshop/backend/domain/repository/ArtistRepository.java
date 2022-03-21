package at.fhv.ss22.ea.f.musicshop.backend.domain.repository;

import at.fhv.ss22.ea.f.musicshop.backend.domain.model.artist.Artist;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.artist.ArtistId;

import java.util.Optional;

public interface ArtistRepository {

    void add(Artist artist);

    Optional<Artist> artistById(ArtistId artistId);
}
