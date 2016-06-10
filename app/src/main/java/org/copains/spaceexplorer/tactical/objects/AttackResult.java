package org.copains.spaceexplorer.tactical.objects;

/**
 * Created by DELAIRE on 10/06/2016.
 */

public class AttackResult {

    private boolean hasError;
    private String errorMessage;

    private boolean attackIsSuccess;
    private short diceResult;
    private short lostLifePoints;

    public boolean isHasError() {
        return hasError;
    }

    public void setHasError(boolean hasError) {
        this.hasError = hasError;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public boolean isAttackIsSuccess() {
        return attackIsSuccess;
    }

    public void setAttackIsSuccess(boolean attackIsSuccess) {
        this.attackIsSuccess = attackIsSuccess;
    }

    public short getDiceResult() {
        return diceResult;
    }

    public void setDiceResult(short diceResult) {
        this.diceResult = diceResult;
    }

    public short getLostLifePoints() {
        return lostLifePoints;
    }

    public void setLostLifePoints(short lostLifePoints) {
        this.lostLifePoints = lostLifePoints;
    }

}
