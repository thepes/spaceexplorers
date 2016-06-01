package org.copains.spaceexplorer.ai.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.copains.spaceexplorer.game.lifeforms.AlienClone;
import org.copains.spaceexplorer.game.lifeforms.AlienCrawler;
import org.copains.spaceexplorer.game.lifeforms.AlienDreadnough;
import org.copains.spaceexplorer.game.lifeforms.AlienHeavyClone;
import org.copains.spaceexplorer.game.lifeforms.LifeForm;
import org.copains.spaceexplorer.tactical.objects.StarshipMap;

import android.util.Log;

public class AlienMg {

	private static AlienMg instance = null;
	
	private int mapAlienCount;
	private List<LifeForm> aliensOnMap;
	private Random random;
	
	private AlienMg() {
		computeAliensCount();
		random = new Random();
		initAliensOnMap();
	}

	/**
	 * retourne un alien au hasard et le retire de la liste
	 * @return une {@link LifeForm}
	 */
	public LifeForm getRandomAlienForPlacement() {
		int size = aliensOnMap.size();
		if (size == 0)
			return (null);
		int pos = random.nextInt(size);
		LifeForm alien = aliensOnMap.get(pos);
		aliensOnMap.remove(pos);
		return (alien);
	}
	
	/**
	 * renvoi le nombre d'aliens restants � placer.
	 * @return
	 */
	public int getRemainingAliens() {
		return (aliensOnMap.size());
	}
	
	public static AlienMg getInstance() {
		if (null == instance) {
			instance = new AlienMg();
		}
		return (instance);
	}
	
	private void initAliensOnMap() {
		aliensOnMap = new ArrayList<LifeForm>();
		// tireurs legers
		int nb = mapAlienCount / 2;
		for (int i = 0 ; i < nb ; i++) {
			aliensOnMap.add(new AlienClone());
		}
		// tireurs lourds
		nb = mapAlienCount / 4;
		for (int i = 0 ; i < nb ; i++) {
			aliensOnMap.add(new AlienHeavyClone());
		}
		// crawler
		nb = mapAlienCount / 5;
		for (int i = 0 ; i < nb ; i++) {
			aliensOnMap.add(new AlienCrawler());
		}
		// dreadnough
		nb = mapAlienCount / 20;
		for (int i = 0 ; i < nb ; i++) {
			aliensOnMap.add(new AlienDreadnough());
		}
		int added = aliensOnMap.size();
		if (added < mapAlienCount)
			while (added < mapAlienCount) {
				added++;
				aliensOnMap.add(new AlienClone());
			}
	}

	/**
	 * calcule le nombre d'aliens � placer sur la map.
	 * Pour l'instant le ratio est de 1 alien pour 6 cases libres
	 * Ensuite ratio d'aliens :
	 *   - 50 % tireur l�ger
	 *   - 25 % tireur lourd
	 *   - 20 % corps � corps
	 *   - 5 % dreadnough
	 * @return
	 */
	private boolean computeAliensCount() {
		StarshipMap map = StarshipMap.getInstance();
		int mapSize = map.getSizeX() * map.getSizeY();
		int freeCells = 0;
		for (int x = 0 ; x < map.getSizeX() ; x++)
			for (int y = 0 ; y < map.getSizeY() ; y++)
				if (map.getRelief(x, y) == StarshipMap.FLOOR)
					freeCells ++;
		mapAlienCount = freeCells / 6;
		Log.i("SpaceExplorers", "Nb d'aliens a placer : " + mapAlienCount);
		return true;
	}
	
}
