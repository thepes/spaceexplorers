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
    private List<Long> playersIds;
    private Long masterId;

    @Index
    private Integer status;
    private Date creationDate;

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
}
