package org.copains.spaceexplorer.network.objects;

import com.orm.SugarRecord;

/**
 * Created by Sébastien Delaire <the.pes@gmail.com>
 * on 11/07/2016.
 */

public class LifeFormAction extends SugarRecord {

    public static final Integer ACTION_CREATION = 1;
    public static final Integer ACTION_OPEN = 2;
    public static final Integer ACTION_MOVE = 3;
    public static final Integer ACTION_SHOOT = 4;

    private Long id;
    private Integer actionType;
    private String actorUuid;
    private String targetUuid;
    private String actionResult;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Integer getActionType() {
        return actionType;
    }

    public void setActionType(Integer actionType) {
        this.actionType = actionType;
    }

    public String getActorUuid() {
        return actorUuid;
    }

    public void setActorUuid(String actorUuid) {
        this.actorUuid = actorUuid;
    }

    public String getTargetUuid() {
        return targetUuid;
    }

    public void setTargetUuid(String targetUuid) {
        this.targetUuid = targetUuid;
    }

    public String getActionResult() {
        return actionResult;
    }

    public void setActionResult(String actionResult) {
        this.actionResult = actionResult;
    }
}
