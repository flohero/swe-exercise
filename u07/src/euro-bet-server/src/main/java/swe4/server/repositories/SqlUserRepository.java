package swe4.server.repositories;

import swe4.domain.User;
import swe4.server.ConnectionFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class SqlUserRepository implements UserRepository {

    SqlUserRepository() {}

    private User userFromResultSet(ResultSet resultSet) throws SQLException {
        return new User(
                resultSet.getInt("id"),
                resultSet.getString("firstname"),
                resultSet.getString("lastname"),
                resultSet.getString("username"),
                resultSet.getString("password")
        );
    }

    @Override
    public Collection<User> findAllUsers() {
        List<User> users = new ArrayList<>();
        try (var stmt = ConnectionFactory.getConnection()
                .prepareStatement("SELECT id, firstname, lastname, username, password FROM users")) {
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                users.add(userFromResultSet(resultSet));
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return users;
    }

    @Override
    public Optional<User> findUserByUsername(String username) {
        Optional<User> user = Optional.empty();
        try (var stmt =
                     ConnectionFactory.getConnection().prepareStatement(
                             "SELECT id, firstname, lastname, username, password " +
                                     "FROM users " +
                                     "WHERE username LIKE ?"
                     )) {
            stmt.setString(1, username);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                System.out.println("HERE");
                user = Optional.of(userFromResultSet(resultSet));
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return user;
    }

    @Override
    public void insertUser(User user) {
        try (var stmt =
                     ConnectionFactory.getConnection().prepareStatement(
                             "INSERT INTO users (firstname, lastname, username, password) " +
                                     "VALUE (?, ?, ?, ?)"
                     )) {
            stmt.setString(1, user.getFirstname());
            stmt.setString(2, user.getLastname());
            stmt.setString(3, user.getUsername());
            stmt.setString(4, user.getPassword());
            stmt.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void updateUser(User user) {
        try (var stmt =
                     ConnectionFactory.getConnection().prepareStatement(
                             "UPDATE users SET firstname = ?,  lastname = ?, username = ?, password = ? " +
                                     "WHERE ID = ?"
                     )) {
            stmt.setString(1, user.getFirstname());
            stmt.setString(2, user.getLastname());
            stmt.setString(3, user.getUsername());
            stmt.setString(4, user.getPassword());
            stmt.setInt(5, user.getId());
            stmt.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void deleteUser(User user) {
        try (var stmt =
                     ConnectionFactory.getConnection().prepareStatement(
                             "DELETE FROM users WHERE id = ?"
                     )) {
            stmt.setInt(1, user.getId());
            stmt.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
}
