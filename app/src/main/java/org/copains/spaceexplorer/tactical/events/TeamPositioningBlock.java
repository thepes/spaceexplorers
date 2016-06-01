package org.copains.spaceexplorer.tactical.events;

public class TeamPositioningBlock extends EventBlock {

	private int memberIndex;
	
	public TeamPositioningBlock(int startX, int startY, int endX, int endY) {
		super(startX, startY, endX, endY);
	}

	/**
	 * @return the memberIndex
	 */
	public int getMemberIndex() {
		return memberIndex;
	}

	/**
	 * @param memberIndex the memberIndex to set
	 */
	public void setMemberIndex(int memberIndex) {
		this.memberIndex = memberIndex;
	}
	
	

}
