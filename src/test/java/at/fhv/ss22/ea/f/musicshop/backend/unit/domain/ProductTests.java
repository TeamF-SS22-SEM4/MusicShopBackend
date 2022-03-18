package at.fhv.ss22.ea.f.musicshop.backend.unit.domain;

import at.fhv.ss22.ea.f.musicshop.backend.domain.model.artist.ArtistId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.product.Product;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.product.ProductId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.product.Song;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProductTests {
    @Test
    void given_productdetails_when_creating_product_then_details_equals() {
        // given
        UUID productIdUUID = UUID.randomUUID();
        ProductId productIdExpected = new ProductId(productIdUUID);
        String nameExpected = "SomeProduct";
        String releaseYearExpected = "2020";
        List<String> genresExpected = new ArrayList<>() {
            {
                add("Rock");
                add("Pop");
            }
        };
        String labelExpected = "TeamF";
        String durationExpected = "5:00";
        List<ArtistId> artistIdsExpected = new ArrayList<>() {
            {
                add(new ArtistId(UUID.randomUUID()));
                add(new ArtistId(UUID.randomUUID()));
            }
        };
        List<Song> songsExpected = new ArrayList<>() {
            {
                add(Song.create("Song 1", "3:00"));
                add(Song.create("Song 2", "2:00"));
            }
        };

        // when
        Product product = Product.create(
                productIdExpected,
                nameExpected,
                releaseYearExpected,
                genresExpected,
                labelExpected,
                durationExpected,
                artistIdsExpected,
                songsExpected
        );

        // then
        assertEquals(productIdExpected, product.getId());
        assertEquals(productIdUUID, product.getId().getUUID());
        assertEquals(nameExpected, product.getName());
        assertEquals(releaseYearExpected, product.getReleaseYear());
        assertEquals(genresExpected.size(), product.getGenre().size());
        assertEquals(labelExpected, product.getLabel());
        assertEquals(durationExpected, product.getDuration());
        assertEquals(artistIdsExpected.size(), product.getArtistIds().size());
        assertEquals(songsExpected.size(), product.getSongs().size());

        // Check content of lists
        for(int i = 0; i < genresExpected.size(); i++) {
            assertEquals(genresExpected.get(i), product.getGenre().get(i));
        }

        for(int i = 0; i < artistIdsExpected.size(); i++) {
            assertEquals(artistIdsExpected.get(i), product.getArtistIds().get(i));
            assertEquals(artistIdsExpected.get(i).getUUID(), product.getArtistIds().get(i).getUUID());
        }

        for(int i = 0; i < songsExpected.size(); i++) {
            Song songExpected = songsExpected.get(i);
            Song songActual = product.getSongs().get(i);

            assertEquals(songExpected, songActual);
            assertEquals(songExpected.getTitle(), songActual.getTitle());
            assertEquals(songExpected.getDuration(), songActual.getDuration());
        }
    }
}
