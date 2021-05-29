package swe4.server.services;

import swe4.domain.entities.Team;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Collection;

public interface TeamService extends Remote {


    Collection<Team> findAllTeams() throws RemoteException;
}
