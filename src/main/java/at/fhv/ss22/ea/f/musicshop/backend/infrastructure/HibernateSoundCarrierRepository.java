package at.fhv.ss22.ea.f.musicshop.backend.infrastructure;

import at.fhv.ss22.ea.f.musicshop.backend.domain.model.soundcarrier.SoundCarrier;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.soundcarrier.SoundCarrierId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.repository.SoundCarrierRepository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.Optional;

public class HibernateSoundCarrierRepository implements SoundCarrierRepository {
    private EntityManager em;

    public HibernateSoundCarrierRepository() {
        this.em = EntityManagerSupplier.getEntityManager();
    }

    @Override
    public void add(SoundCarrier soundCarrier) {
        this.em.getTransaction().begin();
        this.em.persist(soundCarrier);
        this.em.getTransaction().commit();
    }

    @Override
    public Optional<SoundCarrier> soundCarrierById(SoundCarrierId soundCarrierId) {
        TypedQuery<SoundCarrier> query = em.createQuery(
                "select s from SoundCarrier s where s.carrierId = :carrier_id",
                SoundCarrier.class
        );
        query.setParameter("carrier_id", soundCarrierId);
        return query.getResultStream().findFirst();
    }
}
