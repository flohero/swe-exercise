package swe4.client.services.clients;

import javafx.concurrent.Task;
import swe4.client.services.ServiceFactory;
import swe4.domain.User;
import swe4.server.services.UserService;

import java.rmi.RemoteException;

public class UserClientService extends ClientService {
    UserService userService = ServiceFactory.userServiceInstance();

    public void updateUser(User user) {
        new Thread(() -> {
            try {
                userService.updateUser(user);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            dataService.refreshUsers();
        }).start();
    }

    public void updateUserPassword(User user, String password) {
        new Thread(() -> {
            try {
                userService.updateUserPassword(user, password);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            dataService.refreshUsers();
        }).start();
    }

    public void insertUser(String firstname, String lastname, String username, String password) {
        new Thread(() -> {
            try {
                userService.insertUser(firstname, lastname, username, password);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            dataService.refreshUsers();
        }).start();
    }

    public Task<Boolean> userByUsernameIsPresent(final String username) {
        return new Task<>() {
            @Override
            protected Boolean call() {
                try {
                    return userService.userByUsernameIsPresent(username);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                return false;
            }
        };
    }

    public Task<User> findUserByUsernameAndPassword(final String username, final String password) {
        return new Task<>() {
            @Override
            protected User call() {
                try {
                    return userService.findUserByUsernameAndPassword(username, password);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
    }

}
