package swe4.server.repositories;

import swe4.domain.entities.User;

import java.util.Collection;
import java.util.Optional;

public interface UserRepository {
    Collection<User> findAllUsers();

    Optional<User> findUserByUsername(final String username);
    void insertUser(User user);

    void updateUser(User user);

    void deleteUser(User user);
}
