package org.copains.spaceexplorer.backend.game.objects;

import com.google.appengine.api.datastore.Text;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import java.util.Date;

/**
 * Created by Sébastien Delaire <the.pes@gmail.com>
 * on 25/08/2016.
 */
@Entity
public class GameMap {

    @Id
    private Long id;
    @Index
    private String name;
    @Index
    private Long creatorID;
    private String  creatorName;
    @Index
    private Integer minPlayers;
    @Index
    private Integer maxPayers;
    private Date creationDate;
    private Integer engineVersion;
    private Text map;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCreatorID() {
        return creatorID;
    }

    public void setCreatorID(Long creatorID) {
        this.creatorID = creatorID;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Integer getEngineVersion() {
        return engineVersion;
    }

    public void setEngineVersion(Integer engineVersion) {
        this.engineVersion = engineVersion;
    }

    public Text getMap() {
        return map;
    }

    public void setMap(Text map) {
        this.map = map;
    }

    public Integer getMinPlayers() {
        return minPlayers;
    }

    public void setMinPlayers(Integer minPlayers) {
        this.minPlayers = minPlayers;
    }

    public Integer getMaxPayers() {
        return maxPayers;
    }

    public void setMaxPayers(Integer maxPayers) {
        this.maxPayers = maxPayers;
    }
}
