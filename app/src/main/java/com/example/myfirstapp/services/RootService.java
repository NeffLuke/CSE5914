package com.example.myfirstapp.services;

import android.app.Activity;
import android.content.Intent;
import com.example.myfirstapp.DiscoveryActivity;

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
    public void handleClassifierResponse(String cmd, String cls, final Activity activity, final ExecCallback callback) {
        switch (cls) {
            case "lifx":
                IFTTTService.getInstance().execute(cmd, activity, callback);
                break;
            case "nest":
                NestService.getInstance().execute(cmd, activity, callback);
                break;
            case "discovery":
                Discovery2Service.getInstance().execute(cmd, activity, new ExecCallback() {
                    @Override
                    public void onResponse(String message) {
                        Intent disc = new Intent(activity, DiscoveryActivity.class);
                        disc.putExtra("response",message);
                        activity.startActivity(disc);
                    }

                    @Override
                    public void onFailure(Exception e) {

                    }
                });
                break;
            default:
                break;
        }
    }

}
