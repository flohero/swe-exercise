package swe4.server.repositories;

import swe4.domain.entities.Bet;
import swe4.domain.entities.Game;
import swe4.domain.entities.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class FakeBetRepository implements BetRepository {

    private final List<Bet> bets = new ArrayList<>();


    @Override
    public Collection<Bet> findAllBets() {
        return bets;
    }

    @Override
    public Collection<Bet> findBetsByUser(final User user) {
        return bets.stream()
                .filter(bet -> bet.getUser().equals(user))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Bet> findBetByUserAndGame(User user, Game game) {
        return bets.stream()
                .filter(bet -> bet.getUser().equals(user))
                .filter(bet -> bet.getGame().equals(game))
                .findAny();
    }

    @Override
    public void insertBet(Bet bet) {
        Optional<Bet> findBet = findBetByUserAndGame(bet.getUser(), bet.getGame());
        if (findBet.isEmpty()) {
            bets.add(bet);
        }
    }

    @Override
    public void updateBet(Bet bet) {
        Optional<Bet> findBet = findBetByUserAndGame(bet.getUser(), bet.getGame());
        if (findBet.isPresent()) {
            int index = bets.indexOf(findBet.get());
            bets.set(index, bet);
        }
    }

    @Override
    public void deleteBetByGame(Game game) {
        List<Bet> betsToDelete = findAllBets()
                .stream()
                .filter(bet -> bet.getGame().equals(game))
                .collect(Collectors.toList());
        for (Bet bet : betsToDelete) {
            bets.remove(bet);
        }
    }
}
