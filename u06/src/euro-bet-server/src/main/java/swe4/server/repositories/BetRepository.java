package swe4.server.repositories;

import swe4.domain.entities.Bet;
import swe4.domain.entities.Game;
import swe4.domain.entities.User;

import java.util.Collection;
import java.util.Optional;

public interface BetRepository {

    Collection<Bet> findAllBets();

    Collection<Bet> findBetsByUser(User user);

    Optional<Bet> findBetByUserAndGame(User user, Game game);

    void insertBet(Bet bet);

    void updateBet(Bet bet);

    void deleteBetByGame(Game game);
}
