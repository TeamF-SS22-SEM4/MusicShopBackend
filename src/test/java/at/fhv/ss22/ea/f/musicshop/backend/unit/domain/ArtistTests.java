package at.fhv.ss22.ea.f.musicshop.backend.unit.domain;

import at.fhv.ss22.ea.f.musicshop.backend.domain.model.artist.Artist;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.artist.ArtistId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.product.ProductId;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ArtistTests {

    @Test
    void given_artistdetails_when_creating_artist_then_details_equals() {
        // given
        String artistIdStr = "1";
        ArtistId artistIdExpected = new ArtistId(artistIdStr);
        String firstnameExpected = "John";
        String lastnameExpected = "Doe";
        String countryOfOriginExpected = "Austria";
        List<ProductId> productIdsExpected = new ArrayList<>(){
            {
                add(new ProductId("1"));
                add(new ProductId("2"));
                add(new ProductId("3"));
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
        assertEquals(artistIdStr, artist.getId().getUUID());
        assertEquals(firstnameExpected, artist.getFirstname());
        assertEquals(lastnameExpected, artist.getLastname());
        assertEquals(countryOfOriginExpected, artist.getCountryOfOrigin());
        assertEquals(productIdsExpected.size(), artist.getProductIds().size());
    }
}
