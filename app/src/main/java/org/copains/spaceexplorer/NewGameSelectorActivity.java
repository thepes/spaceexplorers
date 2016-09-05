package org.copains.spaceexplorer;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.copains.spaceexplorer.backend.game.endpoints.gameApi.model.Game;
import org.copains.spaceexplorer.game.manager.GameMg;

import java.util.ArrayList;
import java.util.List;

public class NewGameSelectorActivity extends Activity {

    private List<Game> pendingGames;
    private int selectedGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game_selector);

        ListView gamesView = (ListView)findViewById(R.id.pending_games_listview);
        // TODO: implement multi player and player count selection
        pendingGames = GameMg.getPendingGames(1);
        List<String> games = new ArrayList<>();
        for (Game g : pendingGames) {
            games.add("" + g.getId() + " (" + g.getFreeSlots() + "/" + g.getMaxPlayers() + ")");
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(NewGameSelectorActivity.this,
                android.R.layout.simple_list_item_1, games);
        gamesView.setAdapter(adapter);
        gamesView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i("spaceexplorers","selected game : " +i);
                Log.i("spaceexplorers","Game : " + pendingGames.get(i).getId());
               //adapterView.setBackgroundColor(R.color.spaceexplorers_blue);
                for (int idx = 0 ; idx < pendingGames.size(); idx++) {
                    adapterView.getChildAt(idx).setBackgroundColor(getResources().getColor(R.color.spaceexplorers_blue));
                }
                view.setSelected(true);
                view.setBackgroundColor(Color.LTGRAY);
                selectedGame = i;
            }
        });
    }

}
