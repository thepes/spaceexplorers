package org.copains.spaceexplorer;

import android.app.Application;

import com.orm.SugarContext;

/**
 * Created by Sébastien Delaire <the.pes@gmail.com>
 * on 04/07/2016.
 */

public class SpaceExplorerApplication extends Application {

    //public static final String BASE_WS_URL = "http://192.168.0.120:8080/_ah/api/";
    //public static final String BASE_WS_URL = "http://10.0.2.2:8080/_ah/api/";
    public static final String BASE_WS_URL = "http://10.0.0.5:8080/_ah/api/";
    //public static final String BASE_WS_URL = "https://spaceexplorerscopains.appspot.com/_ah/api/";

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
