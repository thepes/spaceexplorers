package org.copains.spaceexplorer;

import android.app.Application;

import com.orm.SugarContext;

/**
 * Created by Sébastien Delaire <the.pes@gmail.com>
 * on 04/07/2016.
 */

public class SpaceExplorerApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SugarContext.init(getApplicationContext());
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        SugarContext.terminate();
    }
}
