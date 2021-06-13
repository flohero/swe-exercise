package swe4.server.repositories;

import swe4.domain.User;
import swe4.server.ConnectionFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;

public class SqlUserRepository implements UserRepository {
    @Override
    public Collection<User> findAllUsers() {
        Connection conn = ConnectionFactory.getConnection();
        try(var stmt = conn.prepareStatement("SELECT id, forname, surname, username, password FROM users")) {

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    @Override
    public Optional<User> findUserByUsername(String username) {
        return Optional.empty();
    }

    @Override
    public void insertUser(User user) {

    }

    @Override
    public void updateUser(User user) {

    }

    @Override
    public void deleteUser(User user) {

    }
}
