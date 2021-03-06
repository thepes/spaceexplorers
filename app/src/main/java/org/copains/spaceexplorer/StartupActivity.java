package org.copains.spaceexplorer;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import org.copains.spaceexplorer.backend.game.endpoints.gameApi.model.Game;
import org.copains.spaceexplorer.game.manager.GameMg;
import org.copains.spaceexplorer.gamemaster.activities.CreateGameActivity;
import org.copains.spaceexplorer.network.services.PendingGameCheckService;
import org.copains.spaceexplorer.profile.manager.ProfileMg;
import org.copains.spaceexplorer.profile.manager.PropertyMg;
import org.copains.spaceexplorer.profile.objects.UserProfile;
import org.copains.spaceexplorer.tactical.objects.CurrentMission;
import org.copains.spaceexplorer.tactical.objects.StarshipMap;

import java.util.List;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class StartupActivity extends Activity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    private List<Game> masterGames;
    private List<Game> playerGames;
    private UserProfile prof;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            /*mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);*/
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
   private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        Intent in = new Intent(this, PendingGameCheckService.class);
        startService(in);

        setContentView(R.layout.startup_menu);

        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);

        findViewById(R.id.dummy_button).setOnTouchListener(mDelayHideTouchListener);
        PropertyMg.getByName("test");
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.

        delayedHide(0);
        prof = ProfileMg.getPlayerProfile();
        playerGames = GameMg.getPlayerGames(prof);
        masterGames = GameMg.getMasterGames(prof);
        int masterGamesCount = 0;
        int masterPending = 0;
        if (null != playerGames) {
            int pending = 0;
            for (Game game : playerGames) {
                if (null != game.getStatus()) {
                    if (game.getStatus() == GameMg.STATUS_PLAYER_TURN) {
                        if (prof.getOnlineId().equals(game.getNextPlayer())) {
                            pending++;
                        }
                    }
                }
            }
            if (null != masterGames) {
                for (Game game : masterGames) {
                    if (null != game.getStatus()) {
                        if (game.getStatus() == GameMg.STATUS_MASTER_TURN) {
                            if (prof.getOnlineId().equals(game.getMasterId())) {
                                masterPending++;
                            }
                        }
                    }
                    if (null != game.getMasterId())
                        if (game.getMasterId().equals(prof.getOnlineId()))
                            masterGamesCount++;
                }
            }
            Button continueBtn = (Button)findViewById(R.id.continue_btn);
            continueBtn.setText(getResources().getText(R.string.continue_btn_lbl) + " ("+
                    pending +"/" + playerGames.size() + ")");
            if (pending > 0) {
                continueBtn.setEnabled(true);
            }
            continueBtn = (Button)findViewById(R.id.continue_master_btn);
            continueBtn.setText(getResources().getText(R.string.continue_master_lbl) + " ("+
                    masterPending +"/" + masterGames.size() + ")");
            if (masterPending > 0) {
                continueBtn.setEnabled(true);
            }
        }
    }


   private void hide() {
        // Hide UI first
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }


    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    public void onNewGame(View v) {
        Log.i("spaceexplorers","Click");

        // Initialize map and Current Mission
        // TODO : save the map on server (or mapID)
        StarshipMap map = StarshipMap.getInstance(getResources().openRawResource(R.raw.first_ship));
        map.setName(getResources().getResourceName(R.raw.first_ship));
        //getResources().
        Game game = GameMg.createGame(prof,map.getName());
        //game.setLocalMapName(map.getName());
        CurrentMission mission = CurrentMission.getInstance();
        mission.setGameId(game.getId());
        Intent intent = new Intent(this,SpaceExplorer.class);
        startActivity(intent);
    }

    public void onMaster(View v) {
        Log.i("spaceexplorers","Click Master");

        Intent intent = new Intent(this,CreateGameActivity.class);
        startActivity(intent);
    }

    public void onContinueMaster(View v) {
        StarshipMap map = StarshipMap.getInstance(getResources().openRawResource(R.raw.first_ship));
        // get first pending game
        for (Game game : masterGames) {
            if (null != game.getStatus()) {
                if (game.getStatus() == GameMg.STATUS_MASTER_TURN) {
                    if (prof.getOnlineId().equals(game.getMasterId())) {
                        CurrentMission mission = CurrentMission.getInstance(game);
                        mission.setMissionMode(CurrentMission.MISSION_MODE_MASTER);
                        Intent intent = new Intent(this,SpaceExplorer.class);
                        startActivity(intent);
                        return;
                    }
                }
            }
        }
    }

    public void onContinue(View v) {
        StarshipMap map = StarshipMap.getInstance(getResources().openRawResource(R.raw.first_ship));
        for (Game game : playerGames) {
            if (null != game.getStatus()) {
                if (game.getStatus() == GameMg.STATUS_PLAYER_TURN) {
                    if (prof.getOnlineId().equals(game.getNextPlayer())) {
                        CurrentMission mission = CurrentMission.getInstance(game);
                        mission.setMissionMode(CurrentMission.MISSION_MODE_PLAYER);
                        Intent intent = new Intent(this,SpaceExplorer.class);
                        startActivity(intent);
                        return;
                    }
                }
            }
        }
    }

    public void onStartNetworkGame(View v) {
        Log.i("spaceexplorers","Click Master");

        Intent intent = new Intent(this,NewGameSelectorActivity.class);
        startActivity(intent);
    }
}
