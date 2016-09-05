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

import org.copains.spaceexplorer.backend.game.objects.GameTurn;

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
        name = "gameTurnApi",
        version = "v1",
        resource = "gameTurn",
        namespace = @ApiNamespace(
                ownerDomain = "endpoints.game.backend.spaceexplorer.copains.org",
                ownerName = "endpoints.game.backend.spaceexplorer.copains.org",
                packagePath = ""
        )
)
public class GameTurnEndpoint {

    private static final Logger logger = Logger.getLogger(GameTurnEndpoint.class.getName());

    private static final int DEFAULT_LIST_LIMIT = 20;

    static {
        // Typically you would register this inside an OfyServive wrapper. See: https://code.google.com/p/objectify-appengine/wiki/BestPractices
        ObjectifyService.register(GameTurn.class);
    }

    /**
     * Returns the {@link GameTurn} with the corresponding ID.
     *
     * @param id the ID of the entity to be retrieved
     * @return the entity with the corresponding ID
     * @throws NotFoundException if there is no {@code GameTurn} with the provided ID.
     */
    @ApiMethod(
            name = "get",
            path = "gameTurn/{id}",
            httpMethod = ApiMethod.HttpMethod.GET)
    public GameTurn get(@Named("id") Long id) throws NotFoundException {
        logger.info("Getting GameTurn with ID: " + id);
        GameTurn gameTurn = ofy().load().type(GameTurn.class).id(id).now();
        if (gameTurn == null) {
            throw new NotFoundException("Could not find GameTurn with ID: " + id);
        }
        return gameTurn;
    }

    /**
     * Inserts a new {@code GameTurn}.
     */
    @ApiMethod(
            name = "insert",
            path = "gameTurn",
            httpMethod = ApiMethod.HttpMethod.POST)
    public GameTurn insert(GameTurn gameTurn) {
        // Typically in a RESTful API a POST does not have a known ID (assuming the ID is used in the resource path).
        // You should validate that gameTurn.id has not been set. If the ID type is not supported by the
        // Objectify ID generator, e.g. long or String, then you should generate the unique ID yourself prior to saving.
        //
        // If your client provides the ID then you should probably use PUT instead.
        ofy().save().entity(gameTurn).now();
        logger.info("Created GameTurn with ID: " + gameTurn.getId());

        return ofy().load().entity(gameTurn).now();
    }

    /**
     * Updates an existing {@code GameTurn}.
     *
     * @param id       the ID of the entity to be updated
     * @param gameTurn the desired state of the entity
     * @return the updated version of the entity
     * @throws NotFoundException if the {@code id} does not correspond to an existing
     *                           {@code GameTurn}
     */
    @ApiMethod(
            name = "update",
            path = "gameTurn/{id}",
            httpMethod = ApiMethod.HttpMethod.PUT)
    public GameTurn update(@Named("id") Long id, GameTurn gameTurn) throws NotFoundException {
        // TODO: You should validate your ID parameter against your resource's ID here.
        checkExists(id);
        ofy().save().entity(gameTurn).now();
        logger.info("Updated GameTurn: " + gameTurn);
        return ofy().load().entity(gameTurn).now();
    }

    /**
     * Deletes the specified {@code GameTurn}.
     *
     * @param id the ID of the entity to delete
     * @throws NotFoundException if the {@code id} does not correspond to an existing
     *                           {@code GameTurn}
     */
    @ApiMethod(
            name = "remove",
            path = "gameTurn/{id}",
            httpMethod = ApiMethod.HttpMethod.DELETE)
    public void remove(@Named("id") Long id) throws NotFoundException {
        checkExists(id);
        ofy().delete().type(GameTurn.class).id(id).now();
        logger.info("Deleted GameTurn with ID: " + id);
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
            path = "gameTurn",
            httpMethod = ApiMethod.HttpMethod.GET)
    public CollectionResponse<GameTurn> list(@Nullable @Named("cursor") String cursor, @Nullable @Named("limit") Integer limit) {
        limit = limit == null ? DEFAULT_LIST_LIMIT : limit;
        Query<GameTurn> query = ofy().load().type(GameTurn.class).limit(limit);
        if (cursor != null) {
            query = query.startAt(Cursor.fromWebSafeString(cursor));
        }
        QueryResultIterator<GameTurn> queryIterator = query.iterator();
        List<GameTurn> gameTurnList = new ArrayList<GameTurn>(limit);
        while (queryIterator.hasNext()) {
            gameTurnList.add(queryIterator.next());
        }
        return CollectionResponse.<GameTurn>builder().setItems(gameTurnList).setNextPageToken(queryIterator.getCursor().toWebSafeString()).build();
    }

    /**
     * List all entities.
     *
     * @param gameId the gameId
     * @return a response that encapsulates the result list and the next page token/cursor
     */
    @ApiMethod(
            name = "listForGame",
            path = "gameTurn/listForGame",
            httpMethod = ApiMethod.HttpMethod.GET)
    public CollectionResponse<GameTurn> listForGame(@Nullable @Named("gameId") Long gameId) {

        Query<GameTurn> query = ofy().load().type(GameTurn.class).filter("gameId =",gameId)
                .order("localId");

        QueryResultIterator<GameTurn> queryIterator = query.iterator();
        List<GameTurn> gameTurnList = new ArrayList<GameTurn>();
        while (queryIterator.hasNext()) {
            gameTurnList.add(queryIterator.next());
        }
        return CollectionResponse.<GameTurn>builder().setItems(gameTurnList).build();
    }

    private void checkExists(Long id) throws NotFoundException {
        try {
            ofy().load().type(GameTurn.class).id(id).safe();
        } catch (com.googlecode.objectify.NotFoundException e) {
            throw new NotFoundException("Could not find GameTurn with ID: " + id);
        }
    }
}