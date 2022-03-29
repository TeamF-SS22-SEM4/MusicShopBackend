package at.fhv.ss22.ea.f.musicshop.backend.unit.infrastructure;

import at.fhv.ss22.ea.f.musicshop.backend.InstanceProvider;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.artist.Artist;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.artist.ArtistId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.repository.ArtistRepository;
import at.fhv.ss22.ea.f.musicshop.backend.infrastructure.EntityManagerUtil;
import at.fhv.ss22.ea.f.musicshop.backend.infrastructure.HibernateArtistRepository;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HibernateArtistRepoTests {

    private ArtistRepository artistRepository = InstanceProvider.getArtistRepository();

    @Test
    void given_invalid_id_when_search_then_empty() {
        //given
        ArtistId artistId = new ArtistId(UUID.randomUUID());

        //when - then
        assertTrue(artistRepository.artistById(artistId).isEmpty());
    }

    @Test
    void given_valid_artist_when_search_with_equal_id_then_artist_found() {
        //given
        Artist artist = Artist.create(new ArtistId(UUID.randomUUID()), "rammstein", "deutschland", List.of());
        ArtistId surrogateId = new ArtistId(artist.getArtistId().getUUID());
        EntityManagerUtil.beginTransaction();
        artistRepository.add(artist);
        EntityManagerUtil.commit();

        //when
        Optional<Artist> artistOptional = artistRepository.artistById(surrogateId);

        //then
        assertTrue(artistOptional.isPresent());
        Artist artistAct = artistOptional.get();
        assertEquals(artist.getArtistName(), artistAct.getArtistName());
        assertEquals(artist.getArtistId(), artistAct.getArtistId());
    }
}
