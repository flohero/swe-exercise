package swe4.repositories;

import swe4.domain.Bet;
import swe4.domain.Game;
import swe4.domain.User;

import java.util.Collection;
import java.util.Optional;

public interface BetRepository {

    Collection<Bet> findAllBets();

    Collection<Bet> findBetsByUser(User user);

    Optional<Bet> findBetByUserAndGame(User user, Game game);

    void insertBet(Bet bet);

    void updateBet(Bet bet);
}
