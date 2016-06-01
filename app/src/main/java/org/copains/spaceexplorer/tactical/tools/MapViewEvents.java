package org.copains.spaceexplorer.tactical.tools;

import java.util.ArrayList;
import java.util.List;

import org.copains.spaceexplorer.R;
import org.copains.spaceexplorer.game.lifeforms.LifeForm;
import org.copains.spaceexplorer.game.objects.Door;
import org.copains.spaceexplorer.tactical.events.LifeFormBlock;
import org.copains.spaceexplorer.tactical.events.TeamPositioningBlock;
import org.copains.spaceexplorer.tactical.events.VisibleMapBlock;
import org.copains.spaceexplorer.tactical.objects.Coordinates;
import org.copains.spaceexplorer.tactical.objects.CurrentMission;
import org.copains.spaceexplorer.tactical.views.MapView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Resources;
import android.util.Log;
import android.view.MotionEvent;

public class MapViewEvents {
	
	public enum MapViewMode {
		TEAM_POSITIONING,
		STANDARD,
		ACTION_HIGLIGHT
	};

	private static MapViewEvents instance = null;
	
	private Context context;
	private MapViewMode viewMode;
	private List<TeamPositioningBlock> positioningEvents;
	private List<VisibleMapBlock> visibleMapEvents;
	private List<LifeFormBlock> lifeFormEvents;
	private LifeForm positioningSelectedMember = null;
	private LifeForm selectedLifeForm;
	private List<Integer> currentActions;
	private int highlightAction;
	private MapView parentView;
	
	private MapViewEvents() {
		positioningEvents = new ArrayList<TeamPositioningBlock>();
		visibleMapEvents = new ArrayList<VisibleMapBlock>();
		lifeFormEvents = new ArrayList<LifeFormBlock>();
	}
	
	public static MapViewEvents getInstance() {
		if (null == instance) {
			instance = new MapViewEvents();
		}
		return (instance);
	}

	public void reinitVisibleMapEvents() {
		visibleMapEvents = new ArrayList<VisibleMapBlock>();		
		lifeFormEvents = new ArrayList<LifeFormBlock>();
	}
	
	/**
	 * @return the viewMode
	 */
	public MapViewMode getViewMode() {
		return viewMode;
	}

	/**
	 * @param viewMode the viewMode to set
	 */
	public void setViewMode(MapViewMode viewMode) {
		this.viewMode = viewMode;
	}
	
	public void addPositioningEvent(int x1, int y1, int x2, int y2, int memberIndex) {
		TeamPositioningBlock block = new TeamPositioningBlock(x1, y1, x2, y2);
		block.setMemberIndex(memberIndex);
		positioningEvents.add(block);
	}

	public void addLifeFormEvent(int x1, int y1, int x2, int y2, LifeForm lifeform) {
		LifeFormBlock block = new LifeFormBlock(x1, y1, x2, y2);
		block.setLifeForm(lifeform);
		lifeFormEvents.add(block);
	}

	public void addVisibleMapEvent(int x1, int y1, int x2, int y2, Coordinates c) {
		VisibleMapBlock block = new VisibleMapBlock(x1, y1, x2, y2);
		block.setMapPosition(c);
		visibleMapEvents.add(block);
	}
	
	public boolean checkEvent(MotionEvent event) {
		CurrentMission mission = CurrentMission.getInstance();
		switch (viewMode) {
		case TEAM_POSITIONING:
			if (null == positioningSelectedMember)
			{
				for (TeamPositioningBlock block : positioningEvents) {
					if (block.isTargeted(event.getX(), event.getY())) {
						Log.i("Space","Member selected : " + block.getMemberIndex());
						positioningSelectedMember = CurrentMission.getInstance().getTeamMember(block.getMemberIndex());
						break;
					}
				}
			} else {
			for (VisibleMapBlock block : visibleMapEvents) {
				if (block.isTargeted(event.getX(), event.getY())) {
					Log.i("space","map select : x="+block.getMapPosition().getX()
							+ " y="+block.getMapPosition().getY());
					positioningSelectedMember.setPosX((short)block.getMapPosition().getX());
					positioningSelectedMember.setPosY((short)block.getMapPosition().getY());
					positioningEvents = new ArrayList<TeamPositioningBlock>();
					positioningSelectedMember = null;
					// checking if there's remaining members to place
					int remaining = 0;
					for (int i = 0 ; i < 6 ; i++) {
						LifeForm marine = mission.getTeamMember(i);
						if (marine.getPosX() == -1) {
							remaining++;
						}
					}
					if (remaining == 0)
					{
						mission.setTeamInPosition(true);
						viewMode = MapViewMode.STANDARD;
					}
					return (true);
				}
			}}
			break;
		case STANDARD:
			visibleMapEvents = new ArrayList<VisibleMapBlock>();
			Log.i("Space","Dans STD event");
			for (LifeFormBlock block : lifeFormEvents) {
				if (block.isTargeted(event.getX(), event.getY())) {
					// use context
					Log.i("Space","lifeform block targeted");
					ArrayList<CharSequence> list = new ArrayList<CharSequence>();
					LifeForm form = block.getLifeForm();
					selectedLifeForm = form;
					currentActions = new ArrayList<Integer>();
					if (form.canMove()) {
						list.add(context.getResources().getText(R.string.move));
						currentActions.add(R.string.move);
					}
					if (form.canOpenDoor()) {
						list.add(context.getResources().getText(R.string.open));						
						currentActions.add(R.string.open);
					}
					if (form.canDoAction()) {
						list.add(context.getResources().getText(R.string.action));
						currentActions.add(R.string.action);
					}
					list.add(context.getResources().getText(R.string.cancel));
					currentActions.add(R.string.cancel);
					AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
					dialogBuilder.setTitle(context.getResources().getText(R.string.action_box_title));
					CharSequence[] contents = new CharSequence[1];
					dialogBuilder.setItems(list.toArray(contents), new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							if (currentActions.get(which) == R.string.cancel)
								return;
							Log.i("Space","Item selected: (" +which+") " );
							Log.i("Space","Action: " +context.getResources().getText(currentActions.get(which)));
							viewMode = MapViewMode.ACTION_HIGLIGHT;
							highlightAction = currentActions.get(which);
							parentView.invalidate();
						}
						
					});
					AlertDialog alert = dialogBuilder.create();
					alert.show();
				}
			}
			break;
		case ACTION_HIGLIGHT:
			for (VisibleMapBlock block : visibleMapEvents) {
				if (block.isTargeted(event.getX(), event.getY())) {
					
					// handling door opening actions
					if (highlightAction == R.string.open) {
						Door d = mission.getDoor(block.getMapPosition());
						Log.i("SpaceExplorer", "opening door : " + d.toString());
						d.setOpen(true);
						//if (null != selectedLifeForm)
						LifeForm form = getSelectedLifeForm();
						short remaining = (short) (form.getMovementPoints() - 1);
						form.setMovementPoints(remaining);
						viewMode = MapViewMode.STANDARD;
						visibleMapEvents = new ArrayList<VisibleMapBlock>();
						selectedLifeForm = null;
						parentView.invalidate();
						return (true);
					}
					
					// handling movement
					// a unit can only move once
					if (highlightAction == R.string.move) {
						selectedLifeForm.setPosX((short)block.getMapPosition().getX());
						selectedLifeForm.setPosY((short)block.getMapPosition().getY());
						selectedLifeForm.setMovementPoints((short)0);
						viewMode = MapViewMode.STANDARD;
						visibleMapEvents = new ArrayList<VisibleMapBlock>();
						selectedLifeForm = null;
						parentView.invalidate();
						return (true);
					}
					
					// handling action (shoot)
					// a unit can only shoot once
					if (highlightAction == R.string.action) {
						// TODO: do shoot
						selectedLifeForm.setActionPoints((short)0);
						viewMode = MapViewMode.STANDARD;
						visibleMapEvents = new ArrayList<VisibleMapBlock>();
						selectedLifeForm = null;
						parentView.invalidate();
						return (true);
					}
				}
			}
			break;
		}
		return (false);
	}

	/**
	 * @param context the context to set
	 */
	public void setContext(Context context) {
		this.context = context;
	}

	/**
	 * @return the highlightAction
	 */
	public int getHighlightAction() {
		return highlightAction;
	}

	/**
	 * @param highlightAction the highlightAction to set
	 */
	public void setHighlightAction(int highlightAction) {
		this.highlightAction = highlightAction;
	}

	/**
	 * @return the selectedLifeForm
	 */
	public LifeForm getSelectedLifeForm() {
		return selectedLifeForm;
	}

	/**
	 * @param selectedLifeForm the selectedLifeForm to set
	 */
	public void setSelectedLifeForm(LifeForm selectedLifeForm) {
		this.selectedLifeForm = selectedLifeForm;
	}

	/**
	 * @return the parentView
	 */
	public MapView getParentView() {
		return parentView;
	}

	/**
	 * @param parentView the parentView to set
	 */
	public void setParentView(MapView parentView) {
		this.parentView = parentView;
	}
	
}