package swe4.server.repositories;

import swe4.domain.entities.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class FakeUserRepository implements UserRepository {

    private final List<User> users = new ArrayList<>();

    FakeUserRepository() {}

    @Override
    public Collection<User> findAllUsers() {
        return users;
    }

    @Override
    public Optional<User> findUserByUsername(String username) {
        return users
                .stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst();
    }

    @Override
    public void insertUser(final User user) {
        users.add(user);
    }

    @Override
    public void updateUser(final User user) {
        int index = users.indexOf(user);
        if(index < 0) {
            return;
        }
        users.set(index, user);
    }

    @Override
    public void deleteUser(User user) {
        users.remove(user);
    }
}
