package swe4.server.services;

import swe4.domain.Game;
import swe4.domain.Team;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.util.Collection;

public interface GameService extends Remote {

    void insertGame(Game game) throws RemoteException;

    void updateGame(Game game) throws RemoteException;

    void deleteGame(Game game) throws RemoteException;

    Collection<Game> findAllGames() throws RemoteException;

    Collection<Game> findByTeam(final Team team) throws RemoteException;

    Collection<Game> findByTeamAndGameIsDuringTime(final Team team, final LocalDateTime dateTime) throws RemoteException;

    Collection<Game> findByTeamAndGameOverlapsTimeFrame(final Team team, final LocalDateTime startTime, final LocalDateTime endTime) throws RemoteException;

}
