package org.copains.spaceexplorer.backend.game.objects;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import java.util.Date;
import java.util.List;

/**
 * Created by Sébastien Delaire <the.pes@gmail.com>
 * on 13/07/2016.
 */

@Entity
public class Game {

    @Id
    private Long id;
    @Index
    private List<Long> playersIds;
    @Index
    private Long masterId;

    @Index
    private Integer status;
    @Index
    private Long nextPlayer;
    @Index
    private Date creationDate;

    private String localMapName;
    private Long onlineMapId;
    private Short maxPlayers;
    @Index
    private Short freeSlots;
    // limit duration for each player turn (in hours)
    private Integer turnLimitByPlayer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Long> getPlayersIds() {
        return playersIds;
    }

    public void setPlayersIds(List<Long> playersIds) {
        this.playersIds = playersIds;
    }

    public Long getMasterId() {
        return masterId;
    }

    public void setMasterId(Long masterId) {
        this.masterId = masterId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Long getNextPlayer() {
        return nextPlayer;
    }

    public void setNextPlayer(Long nextPlayer) {
        this.nextPlayer = nextPlayer;
    }

    public String getLocalMapName() {
        return localMapName;
    }

    public void setLocalMapName(String localMapName) {
        this.localMapName = localMapName;
    }

    public Long getOnlineMapId() {
        return onlineMapId;
    }

    public void setOnlineMapId(Long onlineMapId) {
        this.onlineMapId = onlineMapId;
    }

    public Short getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(Short maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public Short getFreeSlots() {
        return freeSlots;
    }

    public void setFreeSlots(Short freeSlots) {
        this.freeSlots = freeSlots;
    }

    public Integer getTurnLimitByPlayer() {
        return turnLimitByPlayer;
    }

    public void setTurnLimitByPlayer(Integer turnLimitByPlayer) {
        this.turnLimitByPlayer = turnLimitByPlayer;
    }
}
