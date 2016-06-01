package org.copains.spaceexplorer.tactical.events;

import org.copains.spaceexplorer.game.lifeforms.LifeForm;

public class LifeFormBlock extends EventBlock {

	private LifeForm lifeForm;
	
	public LifeFormBlock(int startX, int startY, int endX, int endY) {
		super(startX, startY, endX, endY);
	}

	/**
	 * @return the lifeForm
	 */
	public LifeForm getLifeForm() {
		return lifeForm;
	}

	/**
	 * @param lifeForm the lifeForm to set
	 */
	public void setLifeForm(LifeForm lifeForm) {
		this.lifeForm = lifeForm;
	}

}
