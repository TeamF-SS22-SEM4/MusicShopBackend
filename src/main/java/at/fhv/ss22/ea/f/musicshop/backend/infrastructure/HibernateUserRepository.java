package at.fhv.ss22.ea.f.musicshop.backend.infrastructure;

import at.fhv.ss22.ea.f.musicshop.backend.domain.model.user.User;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.user.UserId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.repository.UserRepository;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.Optional;

@Local(UserRepository.class)
@Stateless
public class HibernateUserRepository implements UserRepository {

    private EntityManager em;

    public HibernateUserRepository() {
        this.em = EntityManagerUtil.getEntityManager();
    }

    @Override
    public void add(User user) {
        this.em.persist(user);
    }

    @Override
    public Optional<User> userById(UserId userId) {
        TypedQuery<User> query = this.em.createQuery(
                "select u from User u where u.userId = :userId",
                User.class
        );
        query.setParameter("userId", userId);
        return query.getResultStream().findFirst();
    }

    @Override
    public Optional<User> userByUserName(String username) {
        TypedQuery<User> query = this.em.createQuery(
                "select u from User u where u.username = :username",
                User.class
        );
        query.setParameter("username", username);
        return query.getResultStream().findFirst();
    }
}
