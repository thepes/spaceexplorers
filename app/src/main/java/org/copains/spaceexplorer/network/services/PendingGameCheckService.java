package org.copains.spaceexplorer.network.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.copains.spaceexplorer.R;
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
        Log.i("spaceexplorers","Sarting service");
        ServiceHandler thread = new ServiceHandler();
        // If we get killed, after returning from here, restart
        scheduler.scheduleAtFixedRate(thread,0,2, TimeUnit.MINUTES);

        return START_STICKY;
    }

    private class ServiceHandler extends Thread {

        private String PropLastRetrievedPendingPlayerGame = "last_retrieved_player_game";
        private String PropLastRetrievedPendingMasterGame = "last_retrieved_master_game";

        @Override
        public void run() {
            Log.i("spaceexplorers", "Service Handler Tread launched : " +
                    Calendar.getInstance().getTime());
            UserProfile prof = ProfileMg.getPlayerProfile();
            List<Game> pendingPlayerGames = GameMg.getPlayerGames(prof);

            if (null != pendingPlayerGames) {
                if (shouldNotify(pendingPlayerGames,PropLastRetrievedPendingPlayerGame)) {
                    notify("player");
                }
            }
            List<Game> pendingMasterGames = GameMg.getMasterGames(prof);
            if (null != pendingMasterGames) {
                if (shouldNotify(pendingMasterGames,PropLastRetrievedPendingMasterGame)) {
                    notify("master");
                }
            }


        }

        private void notify(String mode) {
            String notifText = getApplicationContext().getString(R.string.pending_game_player_text);
            if (mode.compareToIgnoreCase("master") == 0) {
                notifText = getApplicationContext().getString(R.string.pending_game_master_text);
            }
            Notification notif =  new NotificationCompat.Builder(getApplicationContext())
                    .setSmallIcon(R.drawable.ic_status_notify_alien)
                    .setContentTitle(getApplicationContext().getString(R.string.pending_game_notif_title))
                    .setContentText(notifText).build();
            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(getApplicationContext().NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
            mNotificationManager.notify(1,notif);
        }

        private boolean shouldNotify(List<Game> games, String propertyName) {
            Date dt = null;
            for (Game g : games) {
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
                UserProperty lastPlayer = PropertyMg.getByName(propertyName);
                if (null != lastPlayer) {
                    Date last = new Date(Long.parseLong(lastPlayer.getValue()));
                    if (last.before(dt)) {
                        Log.i("spaceexplorers", "New Pending game : " + dt);
                        lastPlayer.setValue("" + dt.getTime());
                        PropertyMg.save(lastPlayer);
                        return true;
                    }
                } else {
                    lastPlayer = new UserProperty(propertyName, "" + dt.getTime());
                    PropertyMg.save(lastPlayer);
                    return true;
                }
            }
            return false;
        }
    }
}
