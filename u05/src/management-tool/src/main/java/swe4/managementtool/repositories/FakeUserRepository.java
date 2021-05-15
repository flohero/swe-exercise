package swe4.managementtool.repositories;

import swe4.managementtool.domain.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FakeUserRepository implements UserRepository {

    private static final List<User> users = new ArrayList<>();

    @Override
    public Collection<User> findAllUsers() {
        return users;
    }

    @Override
    public User insertUser(String username, byte[] hash) {
        User user = new User(username, hash);
        users.add(user);
        return user;
    }
}
