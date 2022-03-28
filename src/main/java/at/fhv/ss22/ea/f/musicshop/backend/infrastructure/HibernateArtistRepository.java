package at.fhv.ss22.ea.f.musicshop.backend.infrastructure;

import at.fhv.ss22.ea.f.musicshop.backend.domain.model.artist.Artist;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.artist.ArtistId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.repository.ArtistRepository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.Optional;

public class HibernateArtistRepository implements ArtistRepository {

    private EntityManager em;

    public HibernateArtistRepository() {
        this.em = EntityManagerUtil.getEntityManager();
    }

    @Override
    public void add(Artist artist) {
        em.getTransaction().begin(); //TODO automate transaction management or at least move it to application layer
        em.persist(artist);
        em.getTransaction().commit();
    }

    @Override
    public Optional<Artist> artistById(ArtistId artistId) {
        TypedQuery<Artist> query = em.createQuery(
                "select a from Artist a where a.artistId = :artist_id",
                Artist.class);
        query.setParameter("artist_id", artistId);
        return query.getResultStream().findFirst();
    }
}
