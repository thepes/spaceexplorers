package org.copains.spaceexplorer.game.manager;

import android.util.Log;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.google.api.client.util.DateTime;

import org.copains.spaceexplorer.SpaceExplorerApplication;
import org.copains.spaceexplorer.backend.game.endpoints.gameApi.GameApi;
import org.copains.spaceexplorer.backend.game.endpoints.gameApi.model.CollectionResponseGame;
import org.copains.spaceexplorer.backend.game.endpoints.gameApi.model.Game;
import org.copains.spaceexplorer.backend.game.endpoints.gameTurnApi.GameTurnApi;
import org.copains.spaceexplorer.backend.game.endpoints.gameTurnApi.model.CollectionResponseGameTurn;
import org.copains.spaceexplorer.backend.game.endpoints.gameTurnApi.model.GameTurn;
import org.copains.spaceexplorer.network.manager.LifeFormActionMg;
import org.copains.spaceexplorer.network.objects.LifeFormAction;
import org.copains.spaceexplorer.profile.manager.ProfileMg;
import org.copains.spaceexplorer.profile.objects.UserProfile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Sébastien Delaire <the.pes@gmail.com>
 * on 11/08/2016.
 */

public class GameMg {

    public static final int STATUS_AWAITING_PLAYERS = 0;
    public static final int STATUS_INIT = 1;
    public static final int STATUS_PLAYER_TURN = 2;
    public static final int STATUS_MASTER_TURN = 3;
    public static final int STATUS_FINISHED = 10;

    private static GameApi getGameApi() {
        GameApi.Builder apiBuilder = new GameApi.Builder(AndroidHttp.newCompatibleTransport(),
                new AndroidJsonFactory(), null).setRootUrl(SpaceExplorerApplication.BASE_WS_URL)
                .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                    @Override
                    public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest)
                            throws IOException {
                        abstractGoogleClientRequest.setDisableGZipContent(true);
                    }
                });
        apiBuilder.setApplicationName("spaceexplorers");
        return apiBuilder.build();
    }

    private static  GameTurnApi getGameTurnApi() {
        GameTurnApi.Builder apiBuilder = new GameTurnApi.Builder(AndroidHttp.newCompatibleTransport(),
                new AndroidJsonFactory(), null).setRootUrl(SpaceExplorerApplication.BASE_WS_URL)
                .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                    @Override
                    public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest)
                            throws IOException {
                        abstractGoogleClientRequest.setDisableGZipContent(true);
                    }
                });
        apiBuilder.setApplicationName("spaceexplorers");
        return apiBuilder.build();
    }

    public static Game createGame(UserProfile prof, String localMapName) {
        GameApi api = getGameApi();
        Game game = new Game();
        List<Long> playerIds = new ArrayList<>();
        playerIds.add(prof.getOnlineId());
        game.setPlayersIds(playerIds);
        DateTime dt = new DateTime(Calendar.getInstance().getTime());
        game.setCreationDate(dt);
        game.setLocalMapName(localMapName);
        Game result;
        try {
            result = api.insert(game).execute();
            Log.i("spaceexplorers",""+result.getId());
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Game> getPlayerGames(UserProfile profile) {
        GameApi api = getGameApi();
        try {
            CollectionResponseGame games = api.getAllForPlayer(profile.getOnlineId()).execute();
            return games.getItems();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static final boolean endGameTurn(Long gameId) {
        // sending actions to server
        List<LifeFormAction> actions = LifeFormActionMg.list();
        for (LifeFormAction action : actions) {
            Log.i("spaceexplorers","Action id : " + action.getId());
            // TODO: send action to server / delete action
            GameTurnApi api = getGameTurnApi();
            GameTurn turn = action.toGameTurn();
            DateTime dt = new DateTime(Calendar.getInstance().getTime());
            turn.setCreationDate(dt);
            turn.setGameId(gameId);
            turn.setPlayerId(ProfileMg.getPlayerProfile().getOnlineId());
            try {
                api.insert(turn).execute();
                action.delete();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        //api.
        return true;
    }

    public static Game createGame(Game game) {
        GameApi api = getGameApi();
        game.setStatus(STATUS_AWAITING_PLAYERS);
        DateTime dt = new DateTime(Calendar.getInstance().getTime());
        game.setCreationDate(dt);
        Game ret = null;
        try {
            ret = api.insert(game).execute();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return ret;
    }

    public static List<Game> getPendingGames(int i) {
        GameApi api = getGameApi();
        try {
            CollectionResponseGame games = api.listAwaitingPlayers().execute();
            return games.getItems();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<GameTurn> getTurns(Game game) {
        GameTurnApi api = getGameTurnApi();
        try {
           CollectionResponseGameTurn turns = api.listForGame().setGameId(game.getId()).execute();
           return turns.getItems();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
