package swe4.managementtool.repositories;

import swe4.managementtool.domain.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FakeUserRepository implements UserRepository {

    private final List<User> users = new ArrayList<>();

    FakeUserRepository() {}

    @Override
    public Collection<User> findAllUsers() {
        return users;
    }

    @Override
    public User insertUser(final User user) {
        users.add(user);
        return user;
    }

    @Override
    public void updateUser(final User user) {
        int index = users.indexOf(user);
        if(index < 0) {
            return;
        }
        users.set(index, user);
    }
}
