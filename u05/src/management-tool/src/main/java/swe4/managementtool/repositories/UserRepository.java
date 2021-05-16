package swe4.managementtool.repositories;

import swe4.managementtool.domain.User;

import java.util.Collection;

public interface UserRepository {
    Collection<User> findAllUsers();

    User insertUser(User user);

    void updateUser(User user);
}