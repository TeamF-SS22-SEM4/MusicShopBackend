package at.fhv.ss22.ea.f.musicshop.backend.domain.repository;

import at.fhv.ss22.ea.f.musicshop.backend.domain.model.user.User;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.user.UserId;

import java.util.Optional;

public interface UserRepository {

    void add(User user);

    Optional<User> userById(UserId userId);

    Optional<User> userByUserName(String username);
}
