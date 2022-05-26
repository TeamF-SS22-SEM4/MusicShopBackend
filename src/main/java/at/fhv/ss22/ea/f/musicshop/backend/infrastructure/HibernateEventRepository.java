package at.fhv.ss22.ea.f.musicshop.backend.infrastructure;

import at.fhv.ss22.ea.f.musicshop.backend.domain.event.purchase.DigitalProductPurchased;
import at.fhv.ss22.ea.f.musicshop.backend.domain.repository.EventRepository;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

@Local(EventRepository.class)
@Stateless
public class HibernateEventRepository implements EventRepository {

    private EntityManager em;

    public HibernateEventRepository() {
        this.em = EntityManagerUtil.getEntityManager();
    }

    @Override
    public void addOutgoingEvent(DigitalProductPurchased digitalProductPurchased) {
        this.em.persist(digitalProductPurchased);
    }

    @Override
    public List<DigitalProductPurchased> getNextOutgoing() {
        TypedQuery<DigitalProductPurchased> query = this.em.createQuery("select d from DigitalProductPurchased d"
                ,DigitalProductPurchased.class);
        return query.getResultList();
    }

    @Override
    public void remove(DigitalProductPurchased event) {
        EntityManagerUtil.beginTransaction();
        this.em.remove(event);
        this.em.flush();
        this.em.clear();

        EntityManagerUtil.commit();
    }
}
