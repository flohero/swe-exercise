package swe4.server.repositories;

import swe4.domain.User;

import java.util.*;

public class FakeUserRepository implements UserRepository {

    private final List<User> users = Collections.synchronizedList(new ArrayList<>());

    FakeUserRepository() {}

    @Override
    public synchronized Collection<User> findAllUsers() {
        return users;
    }

    @Override
    public synchronized Optional<User> findUserByUsername(String username) {
        return users
                .stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst();
    }

    @Override
    public synchronized void insertUser(final User user) {
        users.add(user);
    }

    @Override
    public synchronized void updateUser(final User user) {
        int index = users.indexOf(user);
        if(index < 0) {
            return;
        }
        users.set(index, user);
    }

    @Override
    public synchronized void deleteUser(User user) {
        users.remove(user);
    }
}
