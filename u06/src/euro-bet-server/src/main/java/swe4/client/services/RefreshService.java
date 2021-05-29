package swe4.client.services;

import swe4.client.utils.DialogUtils;

public class RefreshService extends Thread {

    private static final int WAIT_TIME = 1000;
    public static final int WAIT_TIME_MULT = 10;
    private final DataService dataService = ServiceFactory.dataServiceInstance();
    private boolean stop = false;
    private int ticker = 0;

    @Override
    public synchronized void run() {
        while(!stop) {
            try {
                wait(WAIT_TIME);
            } catch (InterruptedException e) {
                DialogUtils.showErrorDialog(e);
            }
            ticker++;
            if(ticker == WAIT_TIME_MULT) {
                dataService.refresh();
                System.out.println("Refreshing!");
            }
            ticker = ticker % WAIT_TIME_MULT;
        }
    }

    public synchronized void stopRefreshing() {
        stop = true;
    }
}
