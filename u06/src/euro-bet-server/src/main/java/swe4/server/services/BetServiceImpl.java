package swe4.server.services;

import swe4.domain.dto.UserScoreDto;
import swe4.domain.entities.Bet;
import swe4.domain.entities.Game;
import swe4.domain.entities.User;
import swe4.server.repositories.BetRepository;
import swe4.server.repositories.RepositoryFactory;
import swe4.server.repositories.UserRepository;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

public class BetServiceImpl implements BetService {

    private final BetRepository betRepository = RepositoryFactory.betRepositoryInstance();
    private final UserRepository userRepository = RepositoryFactory.userRepositoryInstance();

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

    @Override
    public Collection<UserScoreDto> findAllUsersWithScore() throws RemoteException {
        return userRepository.findAllUsers()
                .stream()
                .map(user -> new UserScoreDto(user.getUsername(), (float) this.totalScorePerUser(user)))
                .collect(Collectors.toList());
    }
}
