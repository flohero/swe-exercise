package swe4.server.services;

import swe4.domain.Bet;
import swe4.domain.Game;
import swe4.domain.User;
import swe4.server.repositories.BetRepository;
import swe4.server.repositories.RepositoryFactory;

import java.util.Collection;
import java.util.Objects;

public class BetServiceImpl implements BetService {

    private final BetRepository betRepository = RepositoryFactory.betRepositoryInstance();

    @Override
    public Collection<Bet> findAllBets() {
        return betRepository.findAllBets();
    }

    @Override
    public Collection<Bet> findBetsByUser(User user) {
        Objects.requireNonNull(user);
        return betRepository.findBetsByUser(user);
    }

    @Override
    public Bet findBetByUserAndGame(User user, Game game) {
        Objects.requireNonNull(user);
        Objects.requireNonNull(game);
        return betRepository.findBetByUserAndGame(user, game).orElse(null);
    }

    @Override
    public void insertBet(Bet bet) {
        Objects.requireNonNull(bet);
        betRepository.insertBet(bet);
    }

    @Override
    public void updateBet(Bet bet) {
        Objects.requireNonNull(bet);
        betRepository.updateBet(bet);
    }

    @Override
    public double totalScorePerUser(User user) {
        return betRepository
                .findBetsByUser(user)
                .stream()
                .filter(Bet::wasSuccessful)
                .mapToDouble(Bet::getPoints)
                .sum();
    }
}
