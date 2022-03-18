package at.fhv.ss22.ea.f.musicshop.backend.unit.domain;

import at.fhv.ss22.ea.f.musicshop.backend.domain.model.artist.Artist;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.artist.ArtistId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.product.ProductId;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ArtistTests {

    @Test
    void given_artistdetails_when_creating_artist_then_details_equals() {
        // given
        UUID artistIdUUID = UUID.randomUUID();
        ArtistId artistIdExpected = new ArtistId(artistIdUUID);
        String firstnameExpected = "John";
        String lastnameExpected = "Doe";
        String countryOfOriginExpected = "Austria";
        List<ProductId> productIdsExpected = new ArrayList<>(){
            {
                add(new ProductId(UUID.randomUUID()));
                add(new ProductId(UUID.randomUUID()));
                add(new ProductId(UUID.randomUUID()));
            }
        };

        // when
        Artist artist = Artist.create(
                artistIdExpected,
                firstnameExpected,
                lastnameExpected,
                countryOfOriginExpected,
                productIdsExpected
        );

        // then
        assertEquals(artistIdExpected, artist.getId());
        assertEquals(artistIdUUID, artist.getId().getUUID());
        assertEquals(firstnameExpected, artist.getFirstname());
        assertEquals(lastnameExpected, artist.getLastname());
        assertEquals(countryOfOriginExpected, artist.getCountryOfOrigin());
        assertEquals(productIdsExpected.size(), artist.getProductIds().size());

        // Check content of lists
        for(int i = 0; i < productIdsExpected.size(); i++) {
            assertEquals(productIdsExpected.get(i), artist.getProductIds().get(i));
            assertEquals(productIdsExpected.get(i).getUUID(), artist.getProductIds().get(i).getUUID());
        }
    }
}
