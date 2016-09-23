package org.copains.spaceexplorer.backend.game.manager;

import com.google.appengine.api.datastore.QueryResultIterator;
import com.googlecode.objectify.cmd.Query;

import org.copains.spaceexplorer.backend.game.objects.PlayerGameStatus;

import java.util.Calendar;
import java.util.logging.Logger;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * Created by Sébastien Delaire <the.pes@gmail.com>
 * on 23/09/2016.
 */

public class PlayerGameStatusMg {

    private static final Logger logger = Logger.getLogger(PlayerGameStatusMg.class.getName());

    public static PlayerGameStatus  save(PlayerGameStatus playerGameStatus) {
        PlayerGameStatus existing = getByGameIdAndPlayerId(playerGameStatus.getGameId(), playerGameStatus.getPlayerId());
        if (null == existing) {
            playerGameStatus.setLastUpdate(Calendar.getInstance().getTime());
            ofy().save().entity(playerGameStatus).now();
            logger.info("Created PlayerGameStatus with ID: " + playerGameStatus.getId());

            return ofy().load().entity(playerGameStatus).now();
        }
        existing.setLastPlayedTurn(playerGameStatus.getLastPlayedTurn());
        existing.setLastUpdate(Calendar.getInstance().getTime());
        ofy().save().entity(existing).now();
        logger.info("Updated PlayerGameStatus with ID: " + existing.getId());

        return ofy().load().entity(existing).now();
    }

    private static PlayerGameStatus getByGameIdAndPlayerId(Long gameId, Long playerId) {
        Query<PlayerGameStatus> query = ofy().load().type(PlayerGameStatus.class)
                .filter("gameId =",gameId).filter("playerId =",playerId);
        QueryResultIterator<PlayerGameStatus> queryIterator = query.iterator();
        if (!queryIterator.hasNext()) {
            return null;
        }
        return queryIterator.next();
    }
}
