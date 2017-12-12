package com.example.wonyoungkim.embeddedproject;

import android.Manifest;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Build;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.ArrayList;

import static android.os.Build.VERSION_CODES.M;

/**
 * Created by user on 2017-12-05.
 */

public class ConnectTask extends AsyncTask <Void, Void, Void>{
    private Context context;

    private String ip;
    private int port;

    public ConnectTask(Context context, String ip, int port) {
        this.context = context;
        this.ip = ip;
        this.port = port;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        Log.d("doInBackground", "doInBackground called");

        try{
            Socket socket = new Socket(ip, port);
            InputStream inputStream = socket.getInputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;

            while((bytesRead = inputStream.read(buffer)) != -1){
                String string = new String(buffer);
                Log.d("read from pi", string);
                // 파이에서 버튼 눌렀을 때.
                // 비상연락망에 등록된 사람들에게 문자도 보내주자.
                if(string.startsWith("start CCTV")){
                    StaticData.setIsSOS(true);
                    // permission 있는지 체크하자.
                    if(Build.VERSION.SDK_INT >= M)
                        checkPermission();
                    else{
                        for(int i=0; i<StaticData.getPhoneNumbers().length; i++){
                            sendSMS(StaticData.getPhoneNumbers()[i], "비상비상 삐용삐용");
                        }
                    }
                    Log.e("get CCTV", "get CCTV message from pi");
                    break;
                }
            }

            socket.close();

        }
        catch(IOException e){
            Log.e("socket", "소켓 생성 에러");
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        Log.d("onPostExecute", "pi와 통신 성공");
        super.onPostExecute(aVoid);
    }

    // Android 6.0 이상일 때에는 런타임에 퍼미션 체크를 해주어야 함.
    public void checkPermission(){
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show();

                for(int i=0; i<StaticData.getPhoneNumbers().length; i++){
                    sendSMS(StaticData.getPhoneNumbers()[i], "비상비상 삐용삐용");
                }
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(context, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };

        TedPermission.with(context)
                .setPermissionListener(permissionListener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.RECEIVE_SMS, Manifest.permission.SEND_SMS, Manifest.permission.READ_PHONE_STATE)
                .check();
    }

    public void sendSMS(String phoneNumber, String message){
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";

        PendingIntent sentPI = PendingIntent.getBroadcast(context, 0, new Intent(SENT), 0);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(context, 0, new Intent(DELIVERED), 0);

        // when the SMS has been sent
        context.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()){
                    case MainActivity.RESULT_OK:
                        Toast.makeText(context, "문자가 전송 되었습니다.", Toast.LENGTH_SHORT).show();
                    break;
                }
            }
        }, new IntentFilter(SENT));

        SmsManager manager = SmsManager.getDefault();
        manager.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);

    }
}
