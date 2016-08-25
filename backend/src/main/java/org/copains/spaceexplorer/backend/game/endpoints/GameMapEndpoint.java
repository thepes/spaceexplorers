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

import org.copains.spaceexplorer.backend.game.objects.GameMap;

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
        name = "gameMapApi",
        version = "v1",
        resource = "gameMap",
        namespace = @ApiNamespace(
                ownerDomain = "endpoints.game.backend.spaceexplorer.copains.org",
                ownerName = "endpoints.game.backend.spaceexplorer.copains.org",
                packagePath = ""
        )
)
public class GameMapEndpoint {

    private static final Logger logger = Logger.getLogger(GameMapEndpoint.class.getName());

    private static final int DEFAULT_LIST_LIMIT = 20;

    static {
        // Typically you would register this inside an OfyServive wrapper. See: https://code.google.com/p/objectify-appengine/wiki/BestPractices
        ObjectifyService.register(GameMap.class);
    }

    /**
     * Returns the {@link GameMap} with the corresponding ID.
     *
     * @param id the ID of the entity to be retrieved
     * @return the entity with the corresponding ID
     * @throws NotFoundException if there is no {@code GameMap} with the provided ID.
     */
    @ApiMethod(
            name = "get",
            path = "gameMap/{id}",
            httpMethod = ApiMethod.HttpMethod.GET)
    public GameMap get(@Named("id") Long id) throws NotFoundException {
        logger.info("Getting GameMap with ID: " + id);
        GameMap gameMap = ofy().load().type(GameMap.class).id(id).now();
        if (gameMap == null) {
            throw new NotFoundException("Could not find GameMap with ID: " + id);
        }
        return gameMap;
    }

    /**
     * Inserts a new {@code GameMap}.
     */
    @ApiMethod(
            name = "insert",
            path = "gameMap",
            httpMethod = ApiMethod.HttpMethod.POST)
    public GameMap insert(GameMap gameMap) {
        // Typically in a RESTful API a POST does not have a known ID (assuming the ID is used in the resource path).
        // You should validate that gameMap.id has not been set. If the ID type is not supported by the
        // Objectify ID generator, e.g. long or String, then you should generate the unique ID yourself prior to saving.
        //
        // If your client provides the ID then you should probably use PUT instead.
        ofy().save().entity(gameMap).now();
        logger.info("Created GameMap with ID: " + gameMap.getId());

        return ofy().load().entity(gameMap).now();
    }

    /**
     * Updates an existing {@code GameMap}.
     *
     * @param id      the ID of the entity to be updated
     * @param gameMap the desired state of the entity
     * @return the updated version of the entity
     * @throws NotFoundException if the {@code id} does not correspond to an existing
     *                           {@code GameMap}
     */
    @ApiMethod(
            name = "update",
            path = "gameMap/{id}",
            httpMethod = ApiMethod.HttpMethod.PUT)
    public GameMap update(@Named("id") Long id, GameMap gameMap) throws NotFoundException {
        // TODO: You should validate your ID parameter against your resource's ID here.
        checkExists(id);
        ofy().save().entity(gameMap).now();
        logger.info("Updated GameMap: " + gameMap);
        return ofy().load().entity(gameMap).now();
    }

    /**
     * Deletes the specified {@code GameMap}.
     *
     * @param id the ID of the entity to delete
     * @throws NotFoundException if the {@code id} does not correspond to an existing
     *                           {@code GameMap}
     */
    @ApiMethod(
            name = "remove",
            path = "gameMap/{id}",
            httpMethod = ApiMethod.HttpMethod.DELETE)
    public void remove(@Named("id") Long id) throws NotFoundException {
        checkExists(id);
        ofy().delete().type(GameMap.class).id(id).now();
        logger.info("Deleted GameMap with ID: " + id);
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
            path = "gameMap",
            httpMethod = ApiMethod.HttpMethod.GET)
    public CollectionResponse<GameMap> list(@Nullable @Named("cursor") String cursor, @Nullable @Named("limit") Integer limit) {
        limit = limit == null ? DEFAULT_LIST_LIMIT : limit;
        Query<GameMap> query = ofy().load().type(GameMap.class).limit(limit);
        if (cursor != null) {
            query = query.startAt(Cursor.fromWebSafeString(cursor));
        }
        QueryResultIterator<GameMap> queryIterator = query.iterator();
        List<GameMap> gameMapList = new ArrayList<GameMap>(limit);
        while (queryIterator.hasNext()) {
            gameMapList.add(queryIterator.next());
        }
        return CollectionResponse.<GameMap>builder().setItems(gameMapList).setNextPageToken(queryIterator.getCursor().toWebSafeString()).build();
    }

    private void checkExists(Long id) throws NotFoundException {
        try {
            ofy().load().type(GameMap.class).id(id).safe();
        } catch (com.googlecode.objectify.NotFoundException e) {
            throw new NotFoundException("Could not find GameMap with ID: " + id);
        }
    }
}