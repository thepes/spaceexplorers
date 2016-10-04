package org.copains.spaceexplorer.network.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import org.copains.spaceexplorer.backend.game.endpoints.gameApi.model.Game;
import org.copains.spaceexplorer.game.manager.GameMg;
import org.copains.spaceexplorer.profile.manager.ProfileMg;
import org.copains.spaceexplorer.profile.manager.PropertyMg;
import org.copains.spaceexplorer.profile.objects.UserProfile;
import org.copains.spaceexplorer.profile.objects.UserProperty;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
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

        private String PropLastRetrievedPendingPlayerGame = "last_retrieved_player_game";

        @Override
        public void run() {
            Log.i("spaceexplorers", "Service Handler Tread launched : " +
                    Calendar.getInstance().getTime());
            UserProfile prof = ProfileMg.getPlayerProfile();
            List<Game> pendingPlayerGames = GameMg.getPlayerGames(prof);
            Date dt = null;
            if (null != pendingPlayerGames) {
                for (Game g : pendingPlayerGames) {
                    if (null == dt) {
                        dt = new Date(0);
                    }
                    if (null != g.getLastActionDate()) {
                        Date lastAction = new Date(g.getLastActionDate().getValue());
                        if (lastAction.after(dt)) {
                            dt = lastAction;
                        }
                    }
                }
                if (null != dt) {
                    UserProperty lastPlayer = PropertyMg.getByName(PropLastRetrievedPendingPlayerGame);
                    if (null != lastPlayer) {
                        Date last = new Date(Long.parseLong(lastPlayer.getValue()));
                        if (last.before(dt)) {
                            // TODO: notify
                            Log.i("spaceexplorers", "New Pending game : " + dt);
                            lastPlayer.setValue("" + dt.getTime());
                            PropertyMg.save(lastPlayer);
                        }
                    } else {
                        // TODO: Notify
                        lastPlayer = new UserProperty(PropLastRetrievedPendingPlayerGame, "" + dt.getTime());
                        PropertyMg.save(lastPlayer);
                    }
                }
            }

        }
    }
}
