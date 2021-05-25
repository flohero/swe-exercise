package swe4.server.services;

import swe4.domain.Bet;
import swe4.domain.Game;
import swe4.domain.User;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Collection;

public interface BetService extends Remote {

    Collection<Bet> findAllBets() throws RemoteException;

    Collection<Bet> findBetsByUser(User user) throws RemoteException;

    Bet findBetByUserAndGame(User user, Game game) throws RemoteException;

    void insertBet(Bet bet) throws RemoteException;

    void updateBet(Bet bet) throws RemoteException;

    double totalScorePerUser(User user) throws RemoteException;
}
