package com.example.myfirstapp.services;

import android.app.Activity;

/**
 * Created by simonrouse9461 on 10/19/17.
 */

public class RootService extends Service {

    // Singleton Class Construction
    private static final RootService instance = new RootService();
    public static RootService getInstance() {
        return instance;
    }
    private RootService() { }

    @Override
    public void handleClassifierResponse(String cmd, String cls, Activity activity, ExecCallback callback) {
        switch (cls) {
            case "lifx":
                IFTTTService.getInstance().execute(cmd, activity, callback);
                break;
            case "nest":
                NestService.getInstance().execute(cmd, activity, callback);
                break;
            default:
                break;
        }
    }

}
