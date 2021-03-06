package org.copains.spaceexplorer.network.objects;

import com.orm.SugarRecord;

import org.copains.spaceexplorer.backend.game.endpoints.gameTurnApi.model.GameTurn;

/**
 * Created by Sébastien Delaire <the.pes@gmail.com>
 * on 11/07/2016.
 */

public class LifeFormAction extends SugarRecord {

    public static final int ACTION_CREATION = 1;
    public static final int ACTION_OPEN = 2;
    public static final int ACTION_MOVE = 3;
    public static final int ACTION_SHOOT = 4;
    public static final int ACTION_SHOOT_MULTI = 5;

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

    public GameTurn toGameTurn() {
        GameTurn turn = new GameTurn();
        turn.setActionData(actionResult);
        turn.setActionType(actionType);
        turn.setActorUuid(actorUuid);
        turn.setLocalId(id);
        turn.setTargetId(targetUuid);
        return turn;
    }
}
