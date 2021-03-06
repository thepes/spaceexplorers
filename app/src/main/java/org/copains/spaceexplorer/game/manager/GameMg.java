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

    public static Game get(Long id) {
        GameApi api = getGameApi();
        Game game = null;
        try {
            game = api.get(id).execute();
        } catch (IOException e) {
            // retry once
            try {
                game = api.get(id).execute();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
        return game;
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
        game.setCurrentTurnId(0);
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

    public static List<Game> getPendingPlayerGames(UserProfile profile) {
        List<Game> playerGames = getPlayerGames(profile);
        if (null == playerGames)
            return null;
        List<Game> pending = new ArrayList<>();
        for (Game game : playerGames) {
            if (null != game.getStatus()) {
                if (game.getStatus() == GameMg.STATUS_PLAYER_TURN) {
                    if (profile.getOnlineId().equals(game.getNextPlayer())) {
                       pending.add(game);
                    }
                }
            }
        }
        return pending;
    }

    /**
     * returns all game for a given gameMaster
     * @param profile the user profile
     * @return a List of Game
     */
    public static List<Game> getMasterGames(UserProfile profile) {
        GameApi api = getGameApi();
        try {
            CollectionResponseGame games = api.getAllForMaster(profile.getOnlineId()).execute();
            return games.getItems();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Game> getPendingMasterGames(UserProfile profile) {
        List<Game> masterGames = getMasterGames(profile);
        if (null == masterGames)
            return null;
        List<Game> ret = new ArrayList<>();
        for (Game game : masterGames) {
            if (null != game.getStatus()) {
                if (game.getStatus() == GameMg.STATUS_MASTER_TURN) {
                    if (profile.getOnlineId().equals(game.getMasterId())) {
                        ret.add(game);
                    }
                }
            }
        }
        return ret;
    }

    public static final boolean endGameTurn(Long gameId) {
        // sending actions to server
        List<LifeFormAction> actions = LifeFormActionMg.list();
        Game game = get(gameId);
        for (LifeFormAction action : actions) {
            Log.i("spaceexplorers","Action id : " + action.getId());
            // TODO: send action to server / delete action
            GameTurnApi api = getGameTurnApi();
            GameTurn turn = action.toGameTurn();
            DateTime dt = new DateTime(Calendar.getInstance().getTime());
            turn.setCreationDate(dt);
            turn.setGameId(gameId);
            turn.setPlayerId(ProfileMg.getPlayerProfile().getOnlineId());
            turn.setTurnId(game.getCurrentTurnId());
            try {
                api.insert(turn).execute();
                action.delete();
            } catch (IOException e) {
                Log.w("spaceexplorers","Retrying end turn");
                try {
                    api.insert(turn).execute();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                action.delete();
                e.printStackTrace();
            }
        }
        GameApi gameApi = getGameApi();
        try {
            gameApi.endPlayerTurn(gameId,ProfileMg.getPlayerProfile().getOnlineId()).execute();
        } catch (IOException e) {
            e.printStackTrace();
            Log.w("spaceexplorers","Retrying end turn");
            try {
                gameApi.endPlayerTurn(gameId,ProfileMg.getPlayerProfile().getOnlineId()).execute();
            } catch (IOException e1) {
                e1.printStackTrace();
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

    public static Game addPlayer(Game g, Long onlineId) {
        List<Long> playerIds = new ArrayList<>();
        playerIds.add(onlineId);
        g.setPlayersIds(playerIds);
        int remaining = g.getFreeSlots();
        g.setFreeSlots(remaining-1);
        if (remaining-1 == 0) {
            g.setStatus(STATUS_INIT);
        }
        GameApi api = getGameApi();
        try {
            api.update(g.getId(),g).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return g;
    }
}
