package org.copains.spaceexplorer.tactical.tools;

import java.util.ArrayList;
import java.util.List;

import org.copains.spaceexplorer.R;
import org.copains.spaceexplorer.game.lifeforms.Alien;
import org.copains.spaceexplorer.game.lifeforms.Human;
import org.copains.spaceexplorer.game.lifeforms.LifeForm;
import org.copains.spaceexplorer.game.objects.Door;
import org.copains.spaceexplorer.tactical.actions.AttackMg;
import org.copains.spaceexplorer.tactical.events.LifeFormBlock;
import org.copains.spaceexplorer.tactical.events.TeamPositioningBlock;
import org.copains.spaceexplorer.tactical.events.VisibleMapBlock;
import org.copains.spaceexplorer.tactical.objects.AttackResult;
import org.copains.spaceexplorer.tactical.objects.Coordinates;
import org.copains.spaceexplorer.tactical.objects.CurrentMission;
import org.copains.spaceexplorer.tactical.ui.AttackDetails;
import org.copains.spaceexplorer.tactical.ui.LifeFormDetails;
import org.copains.spaceexplorer.tactical.ui.ModalInfo;
import org.copains.spaceexplorer.tactical.ui.ModalMessage;
import org.copains.spaceexplorer.tactical.views.MapView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.util.Log;
import android.view.MotionEvent;

public class MapViewEvents {

    public enum MapViewMode {
		TEAM_POSITIONING,
		STANDARD,
		ACTION_HIGLIGHT,
        LIFEFORM_DETAILS,
        MODAL_DISPLAYED
	}

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
    private ModalInfo modalInfo;
	
	private MapViewEvents() {
		positioningEvents = new ArrayList<>();
		visibleMapEvents = new ArrayList<>();
		lifeFormEvents = new ArrayList<>();
	}
	
	public static MapViewEvents getInstance() {
		if (null == instance) {
			instance = new MapViewEvents();
		}
		return (instance);
	}

	public void reinitVisibleMapEvents() {
		visibleMapEvents = new ArrayList<>();
		lifeFormEvents = new ArrayList<>();
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
                if (null == positioningSelectedMember) {
                    for (TeamPositioningBlock block : positioningEvents) {
                        if (block.isTargeted(event.getX(), event.getY())) {
                            Log.i("Space", "Member selected : " + block.getMemberIndex());
                            positioningSelectedMember = CurrentMission.getInstance().getTeamMember(block.getMemberIndex());
                            break;
                        }
                    }
                } else {
                    for (VisibleMapBlock block : visibleMapEvents) {
                        if (block.isTargeted(event.getX(), event.getY())) {
                            if (!block.isHumanStartZone()) {
                                viewMode = MapViewMode.MODAL_DISPLAYED;
                                modalInfo = new ModalMessage("Pas une zone de départ");
                                positioningSelectedMember = null;
                                parentView.invalidate();
                                return true;
                            }
                            positioningSelectedMember.setPosX((short) block.getMapPosition().getX());
                            positioningSelectedMember.setPosY((short) block.getMapPosition().getY());
                            positioningEvents = new ArrayList<>();
                            positioningSelectedMember = null;
                            // checking if there's remaining members to place
                            int remaining = 0;
                            for (int i = 0; i < 6; i++) {
                                LifeForm marine = mission.getTeamMember(i);
                                if (marine.getPosX() == -1) {
                                    remaining++;
                                }
                            }
                            if (remaining == 0) {
                                mission.setTeamInPosition(true);
                                viewMode = MapViewMode.STANDARD;
                            }
                            return (true);
                        }
                    }
                }
                break;
            case STANDARD:
                visibleMapEvents = new ArrayList<>();
                for (LifeFormBlock block : lifeFormEvents) {
                    if (block.isTargeted(event.getX(), event.getY())) {
                        // use context
                        LifeForm form = block.getLifeForm();
                        selectedLifeForm = form;
                        if (selectedLifeForm instanceof Human) {
                            createHumanActionMenu(form);
                        } else if (selectedLifeForm instanceof Alien) {
                            displayLifeFormDetails(form);
                        }
                    }
                }
                break;
            case ACTION_HIGLIGHT:
                for (VisibleMapBlock block : visibleMapEvents) {
                    if (block.isTargeted(event.getX(), event.getY())) {

                        // handling door opening actions
                        if (highlightAction == R.string.open) {
                            Door d = mission.getDoor(block.getMapPosition());
                            if (null == d) {
                                return (false);
                            }
                            Log.i("SpaceExplorer", "opening door : " + d.toString());
                            mission.openDoor(d);
                            //d.setOpen(true);
                            //if (null != selectedLifeForm)
                            LifeForm form = getSelectedLifeForm();
                            short remaining = (short) (form.getMovementPoints() - 1);
                            form.setMovementPoints(remaining);
                            viewMode = MapViewMode.STANDARD;
                            visibleMapEvents = new ArrayList<>();
                            selectedLifeForm = null;
                            parentView.invalidate();
                            return (true);
                        }

                        // handling movement
                        // a unit can only move once
                        if (highlightAction == R.string.move) {
                            selectedLifeForm.setPosX((short) block.getMapPosition().getX());
                            selectedLifeForm.setPosY((short) block.getMapPosition().getY());
                            selectedLifeForm.setMovementPoints((short) 0);
                            viewMode = MapViewMode.STANDARD;
                            visibleMapEvents = new ArrayList<>();
                            selectedLifeForm = null;
                            parentView.invalidate();
                            return (true);
                        }

                        // handling action (shoot)
                        // a unit can only shoot once
                        if (highlightAction == R.string.action) {
                            Coordinates coord = block.getMapPosition();
                            selectedLifeForm.setActionPoints((short) 0);
                            AttackResult attackResult = AttackMg.shoot(selectedLifeForm,coord);
                            if (!attackResult.hasError()) {
                                AttackDetails details = new AttackDetails(attackResult);
                                modalInfo = details;
                            } else {
                                ModalMessage msg = new ModalMessage((attackResult.getErrorMessage()));
                                modalInfo = msg;
                            }
                            viewMode = MapViewMode.MODAL_DISPLAYED;
                            visibleMapEvents = new ArrayList<>();
                            selectedLifeForm = null;
                            parentView.invalidate();
                            return (true);
                        }
                    }
                }
                break;
            case LIFEFORM_DETAILS:
            case MODAL_DISPLAYED:
                viewMode = MapViewMode.STANDARD;
                Log.i("spaceexplorers","Fin du mode modal");
                modalInfo = null;
                selectedLifeForm = null;
                parentView.invalidate();
                return (true);
        }
		return (false);
	}

    /**
     * Long press event displays info on lifeform at this moment
     * @param event
     * @return
     */
    public boolean checkLongPressEvent(MotionEvent event) {
        for (LifeFormBlock block : lifeFormEvents) {
            if (block.isTargeted(event.getX(), event.getY())) {
                // use context
                LifeForm form = block.getLifeForm();
                selectedLifeForm = form;
                displayLifeFormDetails(form);
            }
        }
        return true;
    }

    private void displayLifeFormDetails(LifeForm form) {
        viewMode = MapViewMode.LIFEFORM_DETAILS;
        parentView.invalidate();
    }

    private void createHumanActionMenu(LifeForm form) {
        ArrayList<CharSequence> list = new ArrayList<>();
        currentActions = new ArrayList<>();
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
        list.add(context.getString(R.string.info));
        currentActions.add(R.string.info);
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
                if (currentActions.get(which) == R.string.info) {
                    Log.i("spaceexplorers","Info selectionné");
                    displayLifeFormDetails(getSelectedLifeForm());
                    return;
                }
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

	/**
	 * @param context the context to set
	 */
	public void setContext(Context context) {
		this.context = context;
	}

	/**
	 * @return the highlightAction
	 */
    protected int getHighlightAction() {
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

    public ModalInfo getModalInfo() {
        return modalInfo;
    }
	
}
