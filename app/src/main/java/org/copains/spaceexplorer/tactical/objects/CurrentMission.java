package org.copains.spaceexplorer.tactical.objects;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.copains.spaceexplorer.SpaceExplorerApplication;
import org.copains.spaceexplorer.ai.manager.AlienMg;
import org.copains.spaceexplorer.backend.game.endpoints.gameApi.model.Game;
import org.copains.spaceexplorer.backend.game.endpoints.gameTurnApi.GameTurnApi;
import org.copains.spaceexplorer.backend.game.endpoints.gameTurnApi.model.GameTurn;
import org.copains.spaceexplorer.game.WeaponType;
import org.copains.spaceexplorer.game.lifeforms.Alien;
import org.copains.spaceexplorer.game.lifeforms.HeavyMarine;
import org.copains.spaceexplorer.game.lifeforms.LifeForm;
import org.copains.spaceexplorer.game.lifeforms.Marine;
import org.copains.spaceexplorer.game.manager.GameMg;
import org.copains.spaceexplorer.game.manager.LifeFormMg;
import org.copains.spaceexplorer.game.objects.Door;
import org.copains.spaceexplorer.game.objects.Room;
import org.copains.spaceexplorer.network.manager.LifeFormActionMg;
import org.copains.spaceexplorer.network.objects.LifeFormAction;
import org.copains.spaceexplorer.profile.manager.ProfileMg;

import android.util.Log;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.google.api.client.util.DateTime;
import com.google.gson.Gson;

public class CurrentMission {

    public static final int MISSION_MODE_MASTER = 1;
    public static final int MISSION_MODE_PLAYER = 2;

    private static CurrentMission instance = null;
	
	private boolean teamInPosition = false;
	private LifeForm team[] = new LifeForm[6];
	private List<LifeForm> aliens;
    private List<LifeForm> graveyard;
	private List<LifeForm> targetableLifeForms;
    private Hashtable<UUID, LifeForm> lifeFormsByUUID;
	private List<Door> doors;
	private Long gameId;

    private int missionMode = MISSION_MODE_PLAYER;

	private CurrentMission() {
        lifeFormsByUUID = new Hashtable<>();
		for (int i = 0 ; i < 3 ; i++) {
			team[i] = new Marine();
            lifeFormsByUUID.put(team[i].getUuid(),team[i]);
		}
		team[3] = new HeavyMarine(WeaponType.EXPLOSIVE);
        lifeFormsByUUID.put(team[3].getUuid(),team[3]);
		team[4] = new HeavyMarine(WeaponType.LASER);
        lifeFormsByUUID.put(team[4].getUuid(),team[4]);
		team[5] = new HeavyMarine(WeaponType.HEAVY_RIFLE);
        lifeFormsByUUID.put(team[5].getUuid(),team[5]);
		initDoors();
		initAliens();
        graveyard = new ArrayList<>();
	}

    public CurrentMission(Game g) {
        lifeFormsByUUID = new Hashtable<>();

        graveyard = new ArrayList<>();
        initDoors();
        loadGame(g);
    }

    private void initAliens() {
		Random rnd = new Random();
		aliens = new ArrayList<>();
		StarshipMap map = StarshipMap.getInstance();
		AlienMg alienMg = AlienMg.getInstance();
		while (alienMg.getRemainingAliens() > 0) {
			for (int x = 0 ; x < map.getSizeX() ; x ++)
				for (int y = 0 ; y < map.getSizeY() ; y++) {
					if (map.getRelief(x, y) == StarshipMap.FLOOR) {
						// une chance sur 6 d'avoir un alien sur la case
						if (rnd.nextInt(6) == 0) {
							LifeForm alien = alienMg.getRandomAlienForPlacement();
							if (null == alien)
								break;
							alien.setPosX((short)x);
							alien.setPosY((short)y);
							alien.setVisibleOnMap(false);
                            lifeFormsByUUID.put(alien.getUuid(),alien);
							aliens.add(alien);
							LifeFormActionMg.lifeFormCreated(alien);
							if (alienMg.getRemainingAliens() == 0)
								break;
						}
					}
				}
		}
	}

    private void loadGame(Game game) {
        // Cleaning old records
        gameId = game.getId();
        LifeFormActionMg.deleteAll();
        aliens = new ArrayList<>();
        team = new LifeForm[6];
        short  currentTeamMember = 0;

        List<GameTurn> turns = GameMg.getTurns(game);
        if (null == turns) {
            for (int i = 0 ; i < 3 ; i++) {
                team[i] = new Marine();
                lifeFormsByUUID.put(team[i].getUuid(),team[i]);
            }
            team[3] = new HeavyMarine(WeaponType.EXPLOSIVE);
            lifeFormsByUUID.put(team[3].getUuid(),team[3]);
            team[4] = new HeavyMarine(WeaponType.LASER);
            lifeFormsByUUID.put(team[4].getUuid(),team[4]);
            team[5] = new HeavyMarine(WeaponType.HEAVY_RIFLE);
            lifeFormsByUUID.put(team[5].getUuid(),team[5]);
            initDoors();
            initAliens();
            return;
        }

        for (GameTurn turn : turns) {
            teamInPosition = true;
            switch (turn.getActionType()) {
                case LifeFormAction.ACTION_CREATION:
                    LifeForm lf = LifeFormMg.getFromTurnData(turn.getActionData());
                    if (null != lf) {
                        if (lf instanceof Alien) {
                            aliens.add(lf);
                        } else {
                            team[currentTeamMember] = lf;
                            currentTeamMember++;
                        }
                        lifeFormsByUUID.put(lf.getUuid(), lf);
                    }
                    break;
                case LifeFormAction.ACTION_OPEN:
                    Door d = new Gson().fromJson(turn.getActionData(),Door.class);
                    Door target = getDoor(d.getTopLeft());
                    if (null != target) {
                        target.setOpen(d.isOpen());
                    }
                    break;
                case LifeFormAction.ACTION_MOVE:
                    LifeForm lf2 = LifeFormMg.getFromTurnData(turn.getActionData());
                    LifeForm targetLf = lifeFormsByUUID.get(lf2.getUuid());
                    if (null != targetLf) {
                        targetLf.setPosX(lf2.getPosX());
                        targetLf.setPosY(lf2.getPosY());
                    }
                    break;
                case LifeFormAction.ACTION_SHOOT:
                case LifeFormAction.ACTION_SHOOT_MULTI:
                    AttackResult attackResult = new Gson().fromJson(turn.getActionData(),AttackResult.class);
                    if (null != attackResult) {
                        if (attackResult.isAttackIsSuccess()) {
                            if (null != attackResult.getDefender()) {
                                LifeForm defender = attackResult.getDefender();
                                LifeForm localLf = lifeFormsByUUID.get(defender.getUuid());
                                localLf.setLife(defender.getLife());
                                if (defender.getLife() == 0) {
                                    removeLifeFormFromMap(localLf);
                                }
                            }
                            if (null != attackResult.getMultipleDefenders()) {
                                for (LifeForm defender : attackResult.getMultipleDefenders()) {
                                    lifeFormsByUUID.get(defender.getUuid()).setLife(defender.getLife());
                                    LifeForm localLf = lifeFormsByUUID.get(defender.getUuid());
                                    if (defender.getLife() == 0) {
                                        removeLifeFormFromMap(localLf);
                                    }
                                }
                            }
                        }
                    }
                    break;
            }
        }

    }

	private void initDoors() {
		doors = new ArrayList<>();
		StarshipMap map = StarshipMap.getInstance();
		for (int y = 0 ; y < map.getSizeY() ; y++) {
            for (int x = 0; x < map.getSizeX(); x++) {
                if (map.getRelief(x, y) == StarshipMap.DOOR) {
                    boolean newDoor = true;
                    Coordinates start = new Coordinates(x, y);
                    for (Door d : doors) {
                        if (start.isInside(d.getTopLeft(), d.getBottomRight())) {
                            newDoor = false;
                        }
                    }
                    // if new Door, looking for door end
                    if (newDoor) {
                        if (map.getRelief(x + 1, y) == StarshipMap.DOOR) {
                            while (map.getRelief(x, y) == StarshipMap.DOOR) {
                                x++;
                            }
                            Coordinates stop = new Coordinates(x - 1, y);
                            Door d = new Door(start, stop);
                            Room room = map.getRoom(x - 1 , y - 1);
                            if (null != room) {
                                d.addAdjacentRoom(room);
                            }
                            room = map.getRoom(x - 1 , y + 1);
                            if (null != room) {
                                d.addAdjacentRoom(room);
                            }
                            Log.i("Space", d.toString());
                            doors.add(d);
                        } else if (map.getRelief(x, y + 1) == StarshipMap.DOOR) {
                            int tmpY = y;
                            while (map.getRelief(x, tmpY) == StarshipMap.DOOR) {
                                tmpY++;
                            }
                            Coordinates stop = new Coordinates(x, tmpY - 1);
                            Door d = new Door(start, stop);
                            Room room = map.getRoom(x - 1 , stop.getY());
                            if (null != room) {
                                d.addAdjacentRoom(room);
                            }
                            room = map.getRoom(x + 1 ,stop.getY());
                            if (null != room) {
                                d.addAdjacentRoom(room);
                            }
                            Log.i("Space", d.toString());
                            doors.add(d);
                        } else {
                            Door d = new Door(start, start);
                            Room room = map.getRoom(start.getX()-1 , start.getY());
                            if (null != room) {
                                d.addAdjacentRoom(room);
                            }
                            room = map.getRoom(start.getX()+1 , start.getY());
                            if (null != room) {
                                d.addAdjacentRoom(room);
                            }
                            room = map.getRoom(start.getX() , start.getY()-1);
                            if (null != room) {
                                d.addAdjacentRoom(room);
                            }
                            room = map.getRoom(start.getX() , start.getY()+1);
                            if (null != room) {
                                d.addAdjacentRoom(room);
                            }
                            Log.i("Space", d.toString());
                            doors.add(d);
                        }
                    }
                }
            }
        }
	}
	
	public Door getDoor(Coordinates c) {
		for (Door d : doors) {
			if (c.isInside(d.getTopLeft(), d.getBottomRight())) {
				return (d);
			}
		}
		return (null);
	}
	
	public boolean doorIsOpenable(Coordinates c) {
		Door d = getDoor(c);
		if (null != d) {
			return (!d.isOpen());
		}
		return (false);
	}
	
	public List<Door> getAdjacentDoors(Coordinates coord, boolean onlyClosed) {
		if (null == coord) {
			return (null);
		}
		ArrayList<Door> doors = new ArrayList<>();
		Coordinates tmp = new Coordinates(coord.getX()-1,coord.getY());
		Door d = getDoor(tmp);
		if (canAddDoor(d, onlyClosed)) {
				doors.add(d);
		}
		tmp = new Coordinates(coord.getX()+1,coord.getY());
		d = getDoor(tmp);
		if (canAddDoor(d, onlyClosed)) {
			doors.add(d);
		}
		tmp = new Coordinates(coord.getX(),coord.getY()-1);
		d = getDoor(tmp);
		if (canAddDoor(d, onlyClosed)) {
			doors.add(d);
		}
		tmp = new Coordinates(coord.getX(),coord.getY()+1);
		d = getDoor(tmp);
		if (canAddDoor(d, onlyClosed)) {
			doors.add(d);
		}
		return (doors);
	}
	
	private boolean canAddDoor(Door d, boolean onlyClosed) {
		if (null != d) {
            if (onlyClosed) {
                if (!d.isOpen()) {
                    return (true);
                }
            } else {
                return (true);
            }
        }
		return (false);
	}

	public static CurrentMission getInstance() {
		if (null == instance) {
			instance = new CurrentMission();
		}
		return (instance);
	}

    public static CurrentMission getInstance(Game g) {
        instance = new CurrentMission(g);
        return instance;
    }
	
	public LifeForm getTeamMember(int i) {
		return (team[i]);
	}
	
	public LifeForm getLifeFormOnMap(Coordinates pos) {
		for (LifeForm marine : team) {
			if ((marine.getPosX() == (short)pos.getX()) && (marine.getPosY() == (short)pos.getY())) {
				return (marine);
			}
		}
		for (LifeForm alien : aliens) {
			if ((alien.getPosX() == (short)pos.getX()) && (alien.getPosY() == (short)pos.getY())) {
				return (alien);
			}
		}
		return (null);
	}
	
	public boolean isTeamInPosition() {
		return (teamInPosition);
	}
	
	public void setTeamInPosition(boolean pos) {
		teamInPosition = pos;
	}

	/**
	 * fin du tour. 
	 * reinitialisation des Points d'action 
	 * TODO: actions alien ou MDJ
	 * @return
	 */
	public boolean endTurn() {
		for (LifeForm lf : team) {
			lf.endTurn();
		}

		return (GameMg.endGameTurn(gameId));
	}

    /**
     * this method should be called when a lifeform is dead.
     * we put it in a graveyard to be able to do end of game stats
     * @param attackedLf the lifeform to remove
     */
	public void removeLifeFormFromMap(LifeForm attackedLf) {
        graveyard.add(attackedLf);
        if (attackedLf instanceof Alien) {
            aliens.remove(attackedLf);
        }
	}

	public void addTargetableLifeForm(LifeForm lifeFormOnMap) {
        if (null == targetableLifeForms) {
            targetableLifeForms = new ArrayList<>();
        }
        targetableLifeForms.add(lifeFormOnMap);
	}

    public void reinitAttack() {
        targetableLifeForms = null;
    }

    public List<LifeForm> getTargetableLifeForms() {
        return targetableLifeForms;
    }

    public boolean isTargeted(LifeForm attackedLf) {
        if (null == targetableLifeForms) {
            return false;
        }
        return targetableLifeForms.contains(attackedLf);
    }

    public void openDoor(Door d) {
        //d.
        d.setOpen(true);
    }

	public void setGameId(Long gameId) {
		this.gameId = gameId;
	}


    public Long getGameId() {
        return gameId;
    }

    public int getMissionMode() {
        return missionMode;
    }

    public void setMissionMode(int missionMode) {
        this.missionMode = missionMode;
    }
}
