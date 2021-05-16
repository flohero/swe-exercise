package swe4.repositories;

import swe4.domain.User;

import java.util.Collection;

public interface UserRepository {
    Collection<User> findAllUsers();

    User insertUser(User user);

    void updateUser(User user);
}
