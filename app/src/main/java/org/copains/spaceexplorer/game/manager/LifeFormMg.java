package org.copains.spaceexplorer.game.manager;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.copains.spaceexplorer.game.lifeforms.AlienClone;
import org.copains.spaceexplorer.game.lifeforms.AlienCrawler;
import org.copains.spaceexplorer.game.lifeforms.AlienDreadnough;
import org.copains.spaceexplorer.game.lifeforms.AlienHeavyClone;
import org.copains.spaceexplorer.game.lifeforms.HeavyMarine;
import org.copains.spaceexplorer.game.lifeforms.Human;
import org.copains.spaceexplorer.game.lifeforms.LifeForm;
import org.copains.spaceexplorer.game.lifeforms.Marine;

/**
 * Created by Sébastien Delaire <the.pes@gmail.com>
 * on 05/09/2016.
 */

public class LifeFormMg {

    public static final LifeForm getFromTurnData(String actionData) {
        LifeForm lf = new Gson().fromJson(actionData,LifeForm.class);
        Log.i("spaceexplorers","lf type : " + lf.getName());
        switch (lf.getName()) {
            case "Marine":
                return new Marine(lf);
            case "Marine blindé":
                return new HeavyMarine(lf);
            case "Clone Blindé":
                return new AlienHeavyClone(lf);
            case "Clone":
                return new AlienClone(lf);
            case "Crawler":
                return new AlienCrawler(lf);
            case "Dreadnough":
                return new AlienDreadnough(lf);
        }
        return null;
    }

}
