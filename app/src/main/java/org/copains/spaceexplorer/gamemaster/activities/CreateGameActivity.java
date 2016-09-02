package org.copains.spaceexplorer.gamemaster.activities;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Spinner;

import org.copains.spaceexplorer.R;
import org.copains.spaceexplorer.backend.game.endpoints.gameApi.model.Game;
import org.copains.spaceexplorer.game.manager.GameMg;
import org.copains.spaceexplorer.profile.manager.ProfileMg;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class CreateGameActivity extends Activity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

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

        setContentView(R.layout.activity_create_game);

        mVisible = true;




    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mVisible = false;


    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
               mVisible = true;


    }

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    public boolean createMasterGame(View v) {
        Spinner spinner = (Spinner)findViewById(R.id.map_select_fld);
        String map = spinner.getSelectedItem().toString();
        Log.i("spaceexplorers","Selected map : " + map);
        spinner = (Spinner)findViewById(R.id.turn_duration_fld);
        int position = spinner.getSelectedItemPosition();
        Log.i("spaceexplores","Select duration position : " + position);
        int turnDuration = 6;
        switch (position) {
            case 0:
            default:
                turnDuration = 6;
                break;
            case 1:
                turnDuration = 12;
                break;
            case 2:
                turnDuration = 24;
                break;
            case 3:
                turnDuration = 48;
                break;
            case 4:
                turnDuration = 168;
                break;
        }
        RadioGroup group = (RadioGroup)findViewById(R.id.nb_players_radiogrp_fld);
        int playerCount = 1;
        switch (group.getCheckedRadioButtonId()) {
            case R.id.radioplayer_1:
            default:
                playerCount = 1;
                break;
            case R.id.radioplayer_2:
                playerCount = 2;
                break;
            case R.id.radioplayer_3:
                playerCount = 3;
                break;
            case R.id.radioplayer_4:
                playerCount = 4;
                break;
        }
        Game game = new Game();
        game.setLocalMapName(getResources().getResourceName(R.raw.first_ship));
        game.setMasterId(ProfileMg.getPlayerProfile().getOnlineId());
        game.setMaxPlayers(playerCount);
        game.setFreeSlots(playerCount);
        game.setTurnLimitByPlayer(turnDuration);
        game = GameMg.createGame(game);
        // for now we will place master units randomly
        // TODO: redirect the master to the unit placement activity (not yet created)
        return true;
    }
}
