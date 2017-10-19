package com.example.myfirstapp.services;

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
    public void handleClassifierResponse(String cmd, String cls, ExecCallback callback) {
        switch (cls) {
            case "lifx":
                IFTTTService.getInstance().execute(cmd, callback);
                break;
            case "nest":
                NestService.getInstance().execute(cmd, callback);
                break;
            default:
                break;
        }
    }

}
