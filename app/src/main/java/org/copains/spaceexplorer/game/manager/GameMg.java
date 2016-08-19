package org.copains.spaceexplorer.game.manager;

import android.util.Log;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.google.api.client.util.DateTime;

import org.copains.spaceexplorer.SpaceExplorerApplication;
import org.copains.spaceexplorer.backend.game.endpoints.gameApi.GameApi;
import org.copains.spaceexplorer.backend.game.endpoints.gameApi.model.Game;
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

    public static Game createGame(UserProfile prof) {
        GameApi api = getGameApi();
        Game game = new Game();
        List<Long> playerIds = new ArrayList<>();
        playerIds.add(prof.getOnlineId());
        game.setPlayersIds(playerIds);
        DateTime dt = new DateTime(Calendar.getInstance().getTime());
        game.setCreationDate(dt);
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

}
