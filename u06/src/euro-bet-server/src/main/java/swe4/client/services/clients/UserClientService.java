package swe4.client.services.clients;

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

}
