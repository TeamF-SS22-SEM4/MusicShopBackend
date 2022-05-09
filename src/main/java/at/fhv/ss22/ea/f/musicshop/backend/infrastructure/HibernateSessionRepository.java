package at.fhv.ss22.ea.f.musicshop.backend.infrastructure;

import at.fhv.ss22.ea.f.musicshop.backend.domain.model.session.Session;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.session.SessionId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.repository.SessionRepository;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.Instant;
import java.util.Optional;

@Local(SessionRepository.class)
@Stateless
public class HibernateSessionRepository implements SessionRepository {

    private EntityManager em;

    public HibernateSessionRepository() {
        this.em = EntityManagerUtil.getEntityManager();
    }

    @Override
    public void add(Session session) {
        em.persist(session);
    }

    @Override
    public Optional<Session> sessionById(SessionId sessionId) {
        TypedQuery<Session> query = this.em.createQuery(
                "select s from Session s where s.sessionId = :sessionId",
                Session.class
        );
        query.setParameter("sessionId", sessionId);
        return query.getResultStream().findFirst();
    }

    @Override
    public void removeExpiredSessions() {
        TypedQuery<Session> invalidSessionQuery = em.createQuery(
                "select s from Session s where s.validUntil < :now",
                Session.class
        );
        invalidSessionQuery.setParameter("now", Instant.now());

        invalidSessionQuery.getResultStream().forEach(session -> this.em.remove(session));
    }
}
