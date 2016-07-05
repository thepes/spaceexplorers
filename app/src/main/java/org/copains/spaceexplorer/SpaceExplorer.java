package org.copains.spaceexplorer;

import org.copains.spaceexplorer.backend.userRecordApi.UserRecordApi;
import org.copains.spaceexplorer.profile.manager.ProfileMg;
import org.copains.spaceexplorer.profile.objects.UserProfile;
import org.copains.spaceexplorer.tactical.objects.CurrentMission;
import org.copains.spaceexplorer.tactical.objects.StarshipMap;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class SpaceExplorer extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StarshipMap map = StarshipMap.getInstance(getResources().openRawResource(R.raw.first_ship));
        setContentView(R.layout.main);
        Log.i("space","map size : X=" + map.getSizeX() + " Y=" + map.getSizeY() );
//        Mes
        //UserRecordApi recordApi = UserRecordApi.Builder
        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        UserProfile prof = ProfileMg.getPlayerProfile();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	menu.add(Menu.NONE,R.string.end_turn,1,getText(R.string.end_turn));
    	return (true);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
    	case R.string.end_turn:
    		// running end of turn
    		CurrentMission mission = CurrentMission.getInstance();
    		mission.endTurn();
    		break;
    	default:
			break;
    	}
    	return (true);
    }
}