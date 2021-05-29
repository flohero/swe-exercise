package swe4.client.services;

public class RefreshService extends Thread {

    private static final int WAIT_TIME = 10000;
    private final DataService dataService = ServiceFactory.dataServiceInstance();
    private boolean stop = false;

    @Override
    public synchronized void run() {
        while(!stop) {
            try {
                wait(WAIT_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            dataService.refresh();
            System.out.println("Refreshing!");
        }
    }

    public synchronized void stopRefreshing() {
        stop = true;
    }
}
