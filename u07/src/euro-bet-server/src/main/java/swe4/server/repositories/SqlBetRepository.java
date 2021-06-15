package swe4.server.repositories;

import swe4.domain.*;
import swe4.server.ConnectionFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class SqlBetRepository implements BetRepository {

    SqlBetRepository() {}

    private Bet betFromResultSet(ResultSet resultSet) throws SQLException {
        final Team team1 = new Team(
                resultSet.getInt("team1_id"),
                resultSet.getString("team1_name")
        );
        final Team team2 = new Team(
                resultSet.getInt("team2_id"),
                resultSet.getString("team2_name")
        );
        final int winner = resultSet.getInt("winner_team");
        final Team winnerTeam = winner == team1.getId() ? team1 : team2;
        return new Bet(
                resultSet.getInt("bet_id"),
                new User(
                        resultSet.getInt("user_id"),
                        resultSet.getString("firstname"),
                        resultSet.getString("lastname"),
                        resultSet.getString("username"),
                        resultSet.getString("password")
                ),
                new Game(
                        resultSet.getInt("game_id"),
                        team1,
                        team2,
                        resultSet.getInt("score_team1"),
                        resultSet.getInt("score_team2"),
                        resultSet.getObject("start_time", LocalDateTime.class),
                        resultSet.getString("venue")
                ),
                winnerTeam,
                PlacementTime.valueOf(resultSet.getString("placed"))
        );
    }

    @Override
    public Collection<Bet> findAllBets() {
        List<Bet> bets = new ArrayList<>();
        String queryString = "SELECT bets.id AS bet_id, " +
                "u.id AS user_id, " +
                "firstname, " +
                "lastname, " +
                "username, " +
                "password, " +
                "g.id AS game_id, " +
                "t1.id AS team1_id, " +
                "t1.name AS team1_name, " +
                "t2.id AS team2_id, " +
                "t2.name AS team2_name, " +
                "score_team1, " +
                "score_team2, " +
                "start_time, " +
                "venue, " +
                "winner_team, " +
                "placed " +
                "FROM bets " +
                "JOIN users u on u.id = bets.user " +
                "JOIN games g on g.id = bets.game " +
                "JOIN teams t1 on t1.id = g.team1 " +
                "JOIN teams t2 on t2.id = g.team2";
        try (var stmt =
                     ConnectionFactory.getConnection().prepareStatement(queryString)) {
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                Bet bet = betFromResultSet(resultSet);
                bets.add(bet);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return bets;
    }

    @Override
    public Collection<Bet> findBetsByUser(User user) {
        List<Bet> bets = new ArrayList<>();
        String queryString = "SELECT bets.id AS bet_id, " +
                "u.id AS user_id, " +
                "firstname, " +
                "lastname, " +
                "username, " +
                "password, " +
                "g.id AS game_id, " +
                "t1.id AS team1_id, " +
                "t1.name AS team1_name, " +
                "t2.id AS team2_id, " +
                "t2.name AS team2_name, " +
                "score_team1, " +
                "score_team2, " +
                "start_time, " +
                "venue, " +
                "winner_team, " +
                "placed " +
                "FROM bets " +
                "JOIN users u on u.id = bets.user " +
                "JOIN games g on g.id = bets.game " +
                "JOIN teams t1 on t1.id = g.team1 " +
                "JOIN teams t2 on t2.id = g.team2 " +
                "WHERE u.id = ?";
        try (var stmt =
                     ConnectionFactory.getConnection().prepareStatement(queryString)) {
            stmt.setInt(1, user.getId());
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                Bet bet = betFromResultSet(resultSet);
                bets.add(bet);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return bets;
    }

    @Override
    public Optional<Bet> findBetByUserAndGame(User user, Game game) {
        Optional<Bet> optionalBet = Optional.empty();
        String queryString = "SELECT bets.id AS bet_id, " +
                "u.id AS user_id, " +
                "firstname, " +
                "lastname, " +
                "username, " +
                "password, " +
                "g.id AS game_id, " +
                "t1.id AS team1_id, " +
                "t1.name AS team1_name, " +
                "t2.id AS team2_id, " +
                "t2.name AS team2_name, " +
                "score_team1, " +
                "score_team2, " +
                "start_time, " +
                "venue, " +
                "winner_team, " +
                "placed " +
                "FROM bets " +
                "JOIN users u on u.id = bets.user " +
                "JOIN games g on g.id = bets.game " +
                "JOIN teams t1 on t1.id = g.team1 " +
                "JOIN teams t2 on t2.id = g.team2 " +
                "WHERE u.id = ? AND g.id = ?";
        try (var stmt =
                     ConnectionFactory.getConnection().prepareStatement(queryString)) {
            stmt.setInt(1, user.getId());
            stmt.setInt(2, game.getId());
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                Bet bet = betFromResultSet(resultSet);
                optionalBet = Optional.of(bet);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return optionalBet;
    }

    @Override
    public void insertBet(Bet bet) {
        String queryString = "INSERT INTO bets (user, game, winner_team, placed) VALUE (?, ?, ?, ?)";
        try (var stmt =
                     ConnectionFactory.getConnection().prepareStatement(queryString)) {
            stmt.setInt(1, bet.getUser().getId());
            stmt.setInt(2, bet.getGame().getId());
            stmt.setInt(3, bet.getWinner().getId());
            stmt.setString(4, bet.getPlaced().toString());
            stmt.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void updateBet(Bet bet) {
        String queryString = "UPDATE bets SET winner_team = ?, placed = ? WHERE id = ?";
        try (var stmt =
                     ConnectionFactory.getConnection().prepareStatement(queryString)) {
            stmt.setInt(1, bet.getWinner().getId());
            stmt.setString(2, bet.getPlaced().toString());
            stmt.setInt(3, bet.getId());

            stmt.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void deleteBetByGame(Game game) {
        String queryString = "DELETE FROM bets WHERE game = ?";
        try (var stmt =
                     ConnectionFactory.getConnection().prepareStatement(queryString)) {
            stmt.setInt(1, game.getId());

            stmt.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
}
