package swe4.client.services.clients;

import swe4.client.services.DataService;
import swe4.client.services.ServiceFactory;

public abstract class ClientService {
    protected DataService dataService = ServiceFactory.dataServiceInstance();
}
