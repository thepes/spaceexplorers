package org.copains.spaceexplorer.backend.game.endpoints;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.api.server.spi.response.NotFoundException;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.cmd.Query;

import org.copains.spaceexplorer.backend.game.objects.Game;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.Nullable;
import javax.inject.Named;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * WARNING: This generated code is intended as a sample or starting point for using a
 * Google Cloud Endpoints RESTful API with an Objectify entity. It provides no data access
 * restrictions and no data validation.
 * <p>
 * DO NOT deploy this code unchanged as part of a real application to real users.
 */
@Api(
        name = "gameApi",
        version = "v1",
        resource = "game",
        namespace = @ApiNamespace(
                ownerDomain = "endpoints.game.backend.spaceexplorer.copains.org",
                ownerName = "endpoints.game.backend.spaceexplorer.copains.org",
                packagePath = ""
        )
)
public class GameEndpoint {

    private static final Logger logger = Logger.getLogger(GameEndpoint.class.getName());

    private static final int DEFAULT_LIST_LIMIT = 20;
    public static final int STATUS_AWAITING_PLAYERS = 0;
    public static final int STATUS_INIT = 1;
    public static final int STATUS_PLAYER_TURN = 2;
    public static final int STATUS_MASTER_TURN = 3;
    public static final int STATUS_FINISHED = 10;

    static {
        // Typically you would register this inside an OfyServive wrapper. See: https://code.google.com/p/objectify-appengine/wiki/BestPractices
        ObjectifyService.register(Game.class);
    }

    /**
     * Returns the {@link Game} with the corresponding ID.
     *
     * @param id the ID of the entity to be retrieved
     * @return the entity with the corresponding ID
     * @throws NotFoundException if there is no {@code Game} with the provided ID.
     */
    @ApiMethod(
            name = "get",
            path = "game/{id}",
            httpMethod = ApiMethod.HttpMethod.GET)
    public Game get(@Named("id") Long id) throws NotFoundException {
        logger.info("Getting Game with ID: " + id);
        Game game = ofy().load().type(Game.class).id(id).now();
        if (game == null) {
            throw new NotFoundException("Could not find Game with ID: " + id);
        }
        return game;
    }

    /**
     * Inserts a new {@code Game}.
     */
    @ApiMethod(
            name = "insert",
            path = "game",
            httpMethod = ApiMethod.HttpMethod.POST)
    public Game insert(Game game) {
        // Typically in a RESTful API a POST does not have a known ID (assuming the ID is used in the resource path).
        // You should validate that game.id has not been set. If the ID type is not supported by the
        // Objectify ID generator, e.g. long or String, then you should generate the unique ID yourself prior to saving.
        //
        // If your client provides the ID then you should probably use PUT instead.
        ofy().save().entity(game).now();
        logger.info("Created Game with ID: " + game.getId());

        return ofy().load().entity(game).now();
    }

    /**
     * Updates an existing {@code Game}.
     *
     * @param id   the ID of the entity to be updated
     * @param game the desired state of the entity
     * @return the updated version of the entity
     * @throws NotFoundException if the {@code id} does not correspond to an existing
     *                           {@code Game}
     */
    @ApiMethod(
            name = "update",
            path = "game/{id}",
            httpMethod = ApiMethod.HttpMethod.PUT)
    public Game update(@Named("id") Long id, Game game) throws NotFoundException {
        // TODO: You should validate your ID parameter against your resource's ID here.
        checkExists(id);
        ofy().save().entity(game).now();
        logger.info("Updated Game: " + game);
        return ofy().load().entity(game).now();
    }

    /**
     * Deletes the specified {@code Game}.
     *
     * @param id the ID of the entity to delete
     * @throws NotFoundException if the {@code id} does not correspond to an existing
     *                           {@code Game}
     */
    @ApiMethod(
            name = "remove",
            path = "game/{id}",
            httpMethod = ApiMethod.HttpMethod.DELETE)
    public void remove(@Named("id") Long id) throws NotFoundException {
        checkExists(id);
        ofy().delete().type(Game.class).id(id).now();
        logger.info("Deleted Game with ID: " + id);
    }

    /**
     * List all entities.
     *
     * @param cursor used for pagination to determine which page to return
     * @param limit  the maximum number of entries to return
     * @return a response that encapsulates the result list and the next page token/cursor
     */
    @ApiMethod(
            name = "list",
            path = "game",
            httpMethod = ApiMethod.HttpMethod.GET)
    public CollectionResponse<Game> list(@Nullable @Named("cursor") String cursor, @Nullable @Named("limit") Integer limit) {
        limit = limit == null ? DEFAULT_LIST_LIMIT : limit;
        Query<Game> query = ofy().load().type(Game.class).limit(limit);
        if (cursor != null) {
            query = query.startAt(Cursor.fromWebSafeString(cursor));
        }
        QueryResultIterator<Game> queryIterator = query.iterator();
        List<Game> gameList = new ArrayList<Game>(limit);
        while (queryIterator.hasNext()) {
            gameList.add(queryIterator.next());
        }
        return CollectionResponse.<Game>builder().setItems(gameList).setNextPageToken(queryIterator.getCursor().toWebSafeString()).build();
    }

    /**
     * List all entities.
     *
     * @param cursor used for pagination to determine which page to return
     * @param limit  the maximum number of entries to return
     * @return a response that encapsulates the result list and the next page token/cursor
     */
    @ApiMethod(
            name = "listAwaitingPlayers",
            path = "game/listAwaitingPlayers",
            httpMethod = ApiMethod.HttpMethod.GET)
    public CollectionResponse<Game> listAwaitingPlayers(@Nullable @Named("cursor") String cursor, @Nullable @Named("limit") Integer limit) {
        limit = limit == null ? DEFAULT_LIST_LIMIT : limit;
        com.google.appengine.api.datastore.Query.FilterPredicate freeSlotsFilter = new com.google.appengine.api.datastore.Query.FilterPredicate(
                "freeSlots", com.google.appengine.api.datastore.Query.FilterOperator.GREATER_THAN, 0);
        Query<Game> query = ofy().load().type(Game.class)
                .order("freeSlots")
                .order("creationDate")
                .filter(freeSlotsFilter)
                .limit(limit);
        if (cursor != null) {
            query = query.startAt(Cursor.fromWebSafeString(cursor));
        }
        QueryResultIterator<Game> queryIterator = query.iterator();
        List<Game> gameList = new ArrayList<Game>(limit);
        while (queryIterator.hasNext()) {
            gameList.add(queryIterator.next());
        }
        return CollectionResponse.<Game>builder().setItems(gameList).setNextPageToken(queryIterator.getCursor().toWebSafeString()).build();
    }

    @ApiMethod(
            name = "getAllForPlayer",
            path = "game/listForPlayer/{id}",
            httpMethod = ApiMethod.HttpMethod.GET)
    public CollectionResponse<Game> getAllForPlayer(@Named("id") Long id) {
        //limit = limit == null ? DEFAULT_LIST_LIMIT : limit;
        //com.google.appengine.api.datastore.Query.Filter
        Query<Game> query = ofy().load().type(Game.class).filter(new com.google.appengine.api.datastore.Query.FilterPredicate(
                "playersIds", com.google.appengine.api.datastore.Query.FilterOperator.EQUAL, id));

        QueryResultIterator<Game> queryIterator = query.iterator();
        List<Game> gameList = new ArrayList<Game>();
        while (queryIterator.hasNext()) {
            gameList.add(queryIterator.next());
        }
        return CollectionResponse.<Game>builder().setItems(gameList).build();
    }

    @ApiMethod(
            name = "getAllForMaster",
            path = "game/listForMaster/{id}",
            httpMethod = ApiMethod.HttpMethod.GET)
    public CollectionResponse<Game> getAllForMaster(@Named("id") Long id) {
        //limit = limit == null ? DEFAULT_LIST_LIMIT : limit;
        //com.google.appengine.api.datastore.Query.Filter
        Query<Game> query = ofy().load().type(Game.class).filter(new com.google.appengine.api.datastore.Query.FilterPredicate(
                "masterId", com.google.appengine.api.datastore.Query.FilterOperator.EQUAL, id));

        QueryResultIterator<Game> queryIterator = query.iterator();
        List<Game> gameList = new ArrayList<Game>();
        while (queryIterator.hasNext()) {
            gameList.add(queryIterator.next());
        }
        return CollectionResponse.<Game>builder().setItems(gameList).build();
    }

    /**
     * This method ends a player turn:
     * - if there's more than one player in the game, switch to the next player on the list,
     * or the master if there's no more player.
     * - if we are ending a master turn, switch to the first player
     * - if game is in INIT status (MASTER has initialized the game, roll on each players for the
     * INIT (troops placement). After the INIT of each player, the first player starts his turn.
     *
     * game is finished when all the players lost their troops or all the master lifeforms are dead
     *
     * @param id the game id
     * @param playerId the current player id (player ending his turn
     * @return the modified game
     * @throws NotFoundException
     */
    @ApiMethod(
            name = "endPlayerTurn",
            path = "game/endPlayerTurn/{id}",
            httpMethod = ApiMethod.HttpMethod.PUT)
    public Game endPlayerTurn(@Named("id") Long id, @Named("playerId") Long playerId) throws NotFoundException {
        Game game = ofy().load().type(Game.class).id(id).now();
        if (game == null) {
            throw new NotFoundException("Could not find Game with ID: " + id);
        }
        List<Long> players = game.getPlayersIds();
        if (null == game.getCurrentTurnId())
            game.setCurrentTurnId(0);
        switch (game.getStatus()) {
            case STATUS_INIT:
                game.setCurrentTurnId(0);
                if (players.size() == 1) {
                    game.setStatus(STATUS_PLAYER_TURN);
                    game.setNextPlayer(playerId);
                } else {
                    int playerPos = players.indexOf(playerId);
                    logger.info("player index in list : " + playerPos);
                    if (playerPos < (players.size()-1)) {
                        game.setStatus(STATUS_INIT);
                        game.setNextPlayer(players.get(playerPos+1));
                    } else {
                        game.setStatus(STATUS_PLAYER_TURN);
                        game.setNextPlayer(players.get(0));
                    }
                }
                break;
            case STATUS_PLAYER_TURN:
                game.setCurrentTurnId(game.getCurrentTurnId()+1);
                if (players.size() == 1) {
                    game.setStatus(STATUS_MASTER_TURN);
                    game.setNextPlayer(game.getMasterId());
                } else {
                    int playerPos = players.indexOf(playerId);
                    logger.info("player index in list : " + playerPos);
                    if (playerPos < (players.size() - 1)) {
                        game.setStatus(STATUS_PLAYER_TURN);
                        game.setNextPlayer(players.get(playerPos + 1));
                    } else {
                        game.setStatus(STATUS_MASTER_TURN);
                        game.setNextPlayer(game.getMasterId());
                    }
                }
                break;
            case STATUS_MASTER_TURN:
                game.setCurrentTurnId(game.getCurrentTurnId()+1);
                game.setStatus(STATUS_PLAYER_TURN);
                game.setNextPlayer(players.get(0));
                break;

        }
        ofy().save().entity(game).now();
        return game;
    }

    private void checkExists(Long id) throws NotFoundException {
        try {
            ofy().load().type(Game.class).id(id).safe();
        } catch (com.googlecode.objectify.NotFoundException e) {
            throw new NotFoundException("Could not find Game with ID: " + id);
        }
    }
}