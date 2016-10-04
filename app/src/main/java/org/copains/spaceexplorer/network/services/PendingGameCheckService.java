package org.copains.spaceexplorer.network.services;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.util.Log;

import java.util.Calendar;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PendingGameCheckService extends Service {

    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;

    private final ScheduledExecutorService scheduler =
            Executors.newScheduledThreadPool(1);

    public PendingGameCheckService() {
    }

    @Override
    public void onCreate() {
        HandlerThread thread = new HandlerThread("ServiceStartArguments",
                Process.THREAD_PRIORITY_BACKGROUND);
        /*thread.start();

        // Get the HandlerThread's Looper and use it for our Handler
        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);*/
        //scheduler.scheduleAtFixedRate(thread,0,60, TimeUnit.MINUTES);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        /*Message msg = mServiceHandler.obtainMessage();
        msg.arg1 = startId;
        mServiceHandler.sendMessage(msg);*/
        Log.i("spaceexplorers","Sarting service");
        ServiceHandler thread = new ServiceHandler();
        // If we get killed, after returning from here, restart
        scheduler.scheduleAtFixedRate(thread,0,2, TimeUnit.MINUTES);

        return START_STICKY;
    }

    private class ServiceHandler extends Thread {

        @Override
        public void run() {
            Log.i("spaceexplorers", "Service Handler Tread launched : " +
                    Calendar.getInstance().getTime());
        }
    }
}
