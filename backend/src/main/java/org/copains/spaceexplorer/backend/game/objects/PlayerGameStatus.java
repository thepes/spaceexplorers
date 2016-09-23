package org.copains.spaceexplorer.backend.game.objects;

/**
 * Created by Sébastien Delaire <the.pes@gmail.com>
 * on 23/09/2016.
 */

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import java.util.Calendar;
import java.util.Date;

/**
 * records status of player for each game
 */
@Entity
public class PlayerGameStatus {

    @Id
    private Long id;
    @Index
    private Long playerId;
    @Index
    private Long gameId;
    private Integer lastPlayedTurn;
    @Index
    private Date lastUpdate;

    public PlayerGameStatus() {

    }

    public PlayerGameStatus(Long gameId, Long playerId) {
        this.gameId = gameId;
        this.playerId = playerId;
        lastUpdate = Calendar.getInstance().getTime();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public Integer getLastPlayedTurn() {
        return lastPlayedTurn;
    }

    public void setLastPlayedTurn(Integer lastPlayedTurn) {
        this.lastPlayedTurn = lastPlayedTurn;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
