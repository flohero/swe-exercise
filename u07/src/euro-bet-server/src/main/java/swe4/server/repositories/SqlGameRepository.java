package swe4.server.repositories;

import swe4.domain.Game;
import swe4.domain.Team;
import swe4.server.ConnectionFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SqlGameRepository implements GameRepository {

    SqlGameRepository() {
    }

    private Game gameFromResultSet(ResultSet resultSet) throws SQLException {
        return new Game(
                resultSet.getInt("id"),
                new Team(
                        resultSet.getInt("team1"),
                        resultSet.getString("team1_name")
                ),
                new Team(
                        resultSet.getInt("team2"),
                        resultSet.getString("team2_name")
                ),
                resultSet.getInt("score_team1"),
                resultSet.getInt("score_team2"),
                resultSet.getObject("start_time", LocalDateTime.class),
                resultSet.getString("venue")
        );
    }

    @Override
    public void insertGame(Game game) {
        try (var stmt =
                     ConnectionFactory.getConnection().prepareStatement(
                             "INSERT INTO games (team1, team2, score_team1, score_team2, start_time, venue) " +
                                     "VALUE (?, ?, ?, ?, ?, ?)",
                             Statement.RETURN_GENERATED_KEYS
                     )) {
            stmt.setInt(1, game.getTeam1().getId());
            stmt.setInt(2, game.getTeam2().getId());
            stmt.setInt(3, game.getScoreTeam1());
            stmt.setInt(4, game.getScoreTeam2());
            stmt.setObject(5, game.getStartTime());
            stmt.setString(6, game.getVenue());
            stmt.executeUpdate();
            ResultSet resultSet = stmt.getGeneratedKeys();
            resultSet.next();
            game.setId(resultSet.getInt(1));
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void updateGame(Game game) {
        try (var stmt =
                     ConnectionFactory.getConnection().prepareStatement(
                             "UPDATE games SET score_team1 = ?, score_team2  = ? " +
                                     "WHERE id = ?"
                     )) {
            stmt.setInt(1, game.getScoreTeam1());
            stmt.setInt(2, game.getScoreTeam2());
            stmt.setInt(3, game.getId());
            stmt.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void deleteGame(Game game) {
        try (var stmt =
                     ConnectionFactory.getConnection().prepareStatement(
                             "DELETE FROM games WHERE id = ?"
                     )) {
            stmt.setInt(1, game.getId());
            stmt.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public Collection<Game> findAllGames() {
        List<Game> games = new ArrayList<>();
        final String queryString = "SELECT games.id, team1, t1.name as team1_name, team2, t2.name as team2_name, score_team1, score_team2, start_time, venue " +
                "FROM games " +
                "join teams t1 on t1.id = games.team1 " +
                "join teams t2 on t2.id = games.team2";
        try (var stmt =
                     ConnectionFactory.getConnection().prepareStatement(
                             queryString
                     )) {
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                games.add(gameFromResultSet(resultSet));
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return games;
    }

    @Override
    public Collection<Game> findByTeam(Team team) {
        List<Game> games = new ArrayList<>();
        final String queryString = "SELECT games.id, team1, t1.name as team1_name, team2, t2.name as team2_name, score_team1, score_team2, start_time, venue " +
                "FROM games " +
                "join teams t1 on t1.id = games.team1 " +
                "join teams t2 on t2.id = games.team2 " +
                "WHERE t1.id = ? OR t2.id = ?";
        try (var stmt =
                     ConnectionFactory.getConnection().prepareStatement(
                             queryString
                     )) {
            stmt.setInt(1, team.getId());
            stmt.setInt(2, team.getId());
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                games.add(gameFromResultSet(resultSet));
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return games;
    }

    @Override
    public Collection<Game> findByTeamAndGameIsDuringTime(Team team, LocalDateTime dateTime) {
        List<Game> games = new ArrayList<>();
        final String queryString = "SELECT games.id, team1, t1.name as team1_name, team2, t2.name as team2_name, score_team1, score_team2, start_time, venue " +
                "FROM games " +
                "join teams t1 on t1.id = games.team1 " +
                "join teams t2 on t2.id = games.team2 " +
                "WHERE (t1.id = ? OR t2.id = ?) AND start_time <= ? AND DATE_ADD(games.start_time, INTERVAL 105 MINUTE) >= ?";
        return findGamesWithTimeComponent(team, dateTime, dateTime, games, queryString);
    }

    @Override
    public Collection<Game> findByTeamAndGameOverlapsTimeFrame(Team team, LocalDateTime startTime, LocalDateTime endTime) {
        List<Game> games = new ArrayList<>();
        final String queryString =
                "SELECT games.id, team1, t1.name as team1_name, team2, t2.name as team2_name, score_team1, score_team2, start_time, venue " +
                        "FROM games " +
                        "join teams t1 on t1.id = games.team1 " +
                        "join teams t2 on t2.id = games.team2 " +
                        "WHERE (t1.id = ? OR t2.id = ?) AND ((start_time <= ? AND DATE_ADD(games.start_time, INTERVAL 105 MINUTE) >= ?))";
        return findGamesWithTimeComponent(team, startTime, endTime, games, queryString);
    }

    private Collection<Game> findGamesWithTimeComponent(Team team, LocalDateTime startTime, LocalDateTime endTime, List<Game> games, String queryString) {
        try (var stmt =
                     ConnectionFactory.getConnection().prepareStatement(
                             queryString
                     )) {
            stmt.setInt(1, team.getId());
            stmt.setInt(2, team.getId());
            stmt.setObject(3, endTime);
            stmt.setObject(4, startTime);
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                games.add(gameFromResultSet(resultSet));
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return games;
    }
}
