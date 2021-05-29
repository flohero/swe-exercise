package swe4.client.services;

import swe4.domain.entities.User;

public class StateService {
    private static StateService instance = null;

    private User currentUser;

    public static StateService getInstance() {
        if(instance == null) {
            instance = new StateService();
        }
        return instance;
    }

    private StateService() {}

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
}
