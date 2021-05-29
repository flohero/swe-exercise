package swe4.client.services;

public class RefreshService extends Thread {

    private static final int WAIT_TIME = 10000;
    private final DataService dataService = ServiceFactory.dataServiceInstance();

    @Override
    public synchronized void run() {
        boolean stop = false;
        while(!stop) {
            try {
                wait(WAIT_TIME);
            } catch (InterruptedException e) {
                stop = true;
            }
            dataService.refresh();
            System.out.println("Refreshing!");
        }
    }
}
