package swe4.client.services;

public class RefreshService extends Thread {

    DataService dataService = ServiceFactory.dataServiceInstance();

    @Override
    public synchronized void run() {
        boolean stop = false;
        while(!stop) {
            try {
                wait(10000);
            } catch (InterruptedException e) {
                stop = true;
            }
            dataService.refresh();
            System.out.println("Refreshing!");
        }
    }
}
