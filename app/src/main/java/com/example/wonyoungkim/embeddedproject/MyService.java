package com.example.wonyoungkim.embeddedproject;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by user on 2017-12-05.
 */

public class MyService extends Service {
    private ConnectTask connectTask;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("Service onCreate", "onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("Service onStartCommand", "onStartCommand");
        // pi 서버에 연결한다.
        connectTask = new ConnectTask(this, StaticData.getIp(), StaticData.getPort());
        connectTask.execute();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
