package swe4.server.services;

import swe4.domain.User;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Collection;

public interface UserService extends Remote {

    void insertUser(String firstname, String lastname, String username, String password) throws RemoteException;

    void updateUser(final User user) throws RemoteException;

    void updateUserPassword(final User user, final String password) throws RemoteException;

    void deleteUser(User user) throws RemoteException;

    Collection<User> findAllUsers() throws RemoteException;

    User findUserByUsername(final String username) throws RemoteException;

    boolean userByUsernameIsPresent(final String username) throws RemoteException;

    User findUserByUsernameAndPassword(final String username, final String password) throws RemoteException;

}
