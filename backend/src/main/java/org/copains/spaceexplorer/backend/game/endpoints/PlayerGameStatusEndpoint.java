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

import org.copains.spaceexplorer.backend.game.manager.PlayerGameStatusMg;
import org.copains.spaceexplorer.backend.game.objects.PlayerGameStatus;

import java.util.ArrayList;
import java.util.Calendar;
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
        name = "playerGameStatusApi",
        version = "v1",
        resource = "playerGameStatus",
        namespace = @ApiNamespace(
                ownerDomain = "endpoints.game.backend.spaceexplorer.copains.org",
                ownerName = "endpoints.game.backend.spaceexplorer.copains.org",
                packagePath = ""
        )
)
public class PlayerGameStatusEndpoint {

    private static final Logger logger = Logger.getLogger(PlayerGameStatusEndpoint.class.getName());

    private static final int DEFAULT_LIST_LIMIT = 20;

    static {
        // Typically you would register this inside an OfyServive wrapper. See: https://code.google.com/p/objectify-appengine/wiki/BestPractices
        ObjectifyService.register(PlayerGameStatus.class);
    }

    /**
     * Returns the {@link PlayerGameStatus} with the corresponding ID.
     *
     * @param id the ID of the entity to be retrieved
     * @return the entity with the corresponding ID
     * @throws NotFoundException if there is no {@code PlayerGameStatus} with the provided ID.
     */
    @ApiMethod(
            name = "get",
            path = "playerGameStatus/{id}",
            httpMethod = ApiMethod.HttpMethod.GET)
    public PlayerGameStatus get(@Named("id") Long id) throws NotFoundException {
        logger.info("Getting PlayerGameStatus with ID: " + id);
        PlayerGameStatus playerGameStatus = ofy().load().type(PlayerGameStatus.class).id(id).now();
        if (playerGameStatus == null) {
            throw new NotFoundException("Could not find PlayerGameStatus with ID: " + id);
        }
        return playerGameStatus;
    }

    /**
     * Inserts a new {@code PlayerGameStatus}.
     */
    @ApiMethod(
            name = "insert",
            path = "playerGameStatus",
            httpMethod = ApiMethod.HttpMethod.POST)
    public PlayerGameStatus insert(PlayerGameStatus playerGameStatus) {
        // Typically in a RESTful API a POST does not have a known ID (assuming the ID is used in the resource path).
        // You should validate that playerGameStatus.id has not been set. If the ID type is not supported by the
        // Objectify ID generator, e.g. long or String, then you should generate the unique ID yourself prior to saving.
        //
        // If your client provides the ID then you should probably use PUT instead.
        return PlayerGameStatusMg.save(playerGameStatus);
    }

    /**
     * Deletes the specified {@code PlayerGameStatus}.
     *
     * @param id the ID of the entity to delete
     * @throws NotFoundException if the {@code id} does not correspond to an existing
     *                           {@code PlayerGameStatus}
     */
    @ApiMethod(
            name = "remove",
            path = "playerGameStatus/{id}",
            httpMethod = ApiMethod.HttpMethod.DELETE)
    public void remove(@Named("id") Long id) throws NotFoundException {
        checkExists(id);
        ofy().delete().type(PlayerGameStatus.class).id(id).now();
        logger.info("Deleted PlayerGameStatus with ID: " + id);
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
            path = "playerGameStatus",
            httpMethod = ApiMethod.HttpMethod.GET)
    public CollectionResponse<PlayerGameStatus> list(@Nullable @Named("cursor") String cursor, @Nullable @Named("limit") Integer limit) {
        limit = limit == null ? DEFAULT_LIST_LIMIT : limit;
        Query<PlayerGameStatus> query = ofy().load().type(PlayerGameStatus.class).limit(limit);
        if (cursor != null) {
            query = query.startAt(Cursor.fromWebSafeString(cursor));
        }
        QueryResultIterator<PlayerGameStatus> queryIterator = query.iterator();
        List<PlayerGameStatus> playerGameStatusList = new ArrayList<PlayerGameStatus>(limit);
        while (queryIterator.hasNext()) {
            playerGameStatusList.add(queryIterator.next());
        }
        return CollectionResponse.<PlayerGameStatus>builder().setItems(playerGameStatusList).setNextPageToken(queryIterator.getCursor().toWebSafeString()).build();
    }

    private void checkExists(Long id) throws NotFoundException {
        try {
            ofy().load().type(PlayerGameStatus.class).id(id).safe();
        } catch (com.googlecode.objectify.NotFoundException e) {
            throw new NotFoundException("Could not find PlayerGameStatus with ID: " + id);
        }
    }
}