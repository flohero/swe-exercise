package swe4.server.repositories;

import swe4.domain.Team;
import swe4.server.ConnectionFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SqlTeamRepository implements TeamRepository {

    SqlTeamRepository() {
    }

    private Team teamFromResultSet(ResultSet resultSet) throws SQLException {
        return new Team(
                resultSet.getInt("id"),
                resultSet.getString("name")
        );
    }

    @Override
    public Collection<Team> findAllTeams() {
        List<Team> teams = new ArrayList<>();
        try (var stmt = ConnectionFactory.getConnection()
                .prepareStatement("SELECT id, name FROM teams")) {
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                teams.add(teamFromResultSet(resultSet));
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return teams;
    }

    @Override
    public void insertTeam(Team team) {
        try (var stmt =
                     ConnectionFactory.getConnection().prepareStatement(
                             "INSERT INTO teams (name) VALUE (?)",
                             PreparedStatement.RETURN_GENERATED_KEYS
                     )) {
            stmt.setString(1, team.getName());
            stmt.executeUpdate();
            ResultSet resultSet = stmt.getGeneratedKeys();
            resultSet.next();
            team.setId(resultSet.getInt(1));
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
}
