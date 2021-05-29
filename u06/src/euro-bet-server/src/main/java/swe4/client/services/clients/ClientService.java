package swe4.client.services.clients;

import swe4.client.services.DataService;
import swe4.client.services.ServiceFactory;
import swe4.domain.entities.Game;

public abstract class ClientService {
    protected DataService dataService = ServiceFactory.dataServiceInstance();
}
