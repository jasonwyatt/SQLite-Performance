package co.jasonwyatt.sqliteperf;

import android.app.Application;

/**
 * @author jason
 */

public class App extends Application {
    private static App sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }

    public static App getInstance() {
        return sInstance;
    }
}
