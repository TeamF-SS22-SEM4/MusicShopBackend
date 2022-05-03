package at.fhv.ss22.ea.f.musicshop.backend.unit.infrastructure;

import at.fhv.ss22.ea.f.musicshop.backend.domain.model.UserRole;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.user.User;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.user.UserId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.repository.UserRepository;
import at.fhv.ss22.ea.f.musicshop.backend.infrastructure.EntityManagerUtil;
import at.fhv.ss22.ea.f.musicshop.backend.infrastructure.HibernateUserRepository;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


class HibernateUserRepoTests {

    private UserRepository userRepository =  new HibernateUserRepository();

    @Test
    void given_product_when_searched_by_equal_but_not_same_id_then_product_found() {
        User user = User.create(new UserId(UUID.randomUUID()), "userA", "max", "mustermann", List.of(UserRole.EMPLOYEE),List.of());
        EntityManagerUtil.beginTransaction();
        userRepository.add(user);
        EntityManagerUtil.commit();
        UserId surrogateId = new UserId(user.getUserId().getUUID());

        //when
        Optional<User> employeeOpt = userRepository.userById(surrogateId);
        //then
        assertTrue(employeeOpt.isPresent());
        User e = employeeOpt.get();
        assertEquals(user.getFirstname(), e.getFirstname());
        assertEquals(user.getLastname(), e.getLastname());
        assertEquals(user.getUsername(), e.getUsername());
    }

    @Test
    void given_invalid_product_id_when_searched_then_empty_result() {
        //given
        UserId id = new UserId(UUID.randomUUID());

        //when
        Optional<User> employeeOpt = userRepository.userById(id);

        //then
        assertTrue(employeeOpt.isEmpty());
    }

    @Test
    void search_by_username() {
        EntityManagerUtil.beginTransaction();
        String username = "uniqueUsername";
        User user = User.create(new UserId(UUID.randomUUID()), username, "max", "mustermann", List.of(UserRole.EMPLOYEE),List.of());

        userRepository.add(user);

        User userAct = userRepository.userByUserName(username).get();

        assertEquals(user.getUserId().getUUID(), userAct.getUserId().getUUID());
        EntityManagerUtil.rollback();
    }
}