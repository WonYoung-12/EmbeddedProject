package com.example.wonyoungkim.embeddedproject;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.connectToPi)
    LinearLayout connectToPi;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.ipAddress)
    EditText ipAddress;
    @BindView(R.id.portNum)
    EditText portNum;
    @BindView(R.id.connectButton)
    Button connectButton;

    private Unbinder unbinder;
    // pi에 연결되면 true로 바꿔주고 connectToPi 레이아웃 visibility 바꿔주자.
    private boolean isConnected = false;
    private Socket socket;
    private Handler mHandler;

    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    private static final String ip = "192."

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);
        init();
    }

    public void init(){
        toolbar.setTitle("My SOS");
        ipAddress.setText("ip Address");
        portNum.setText("portNum");
    }

    @OnClick(R.id.connectButton)
    public void connectToPi(){
        String ip = ipAddress.getText().toString();
        int port = Integer.parseInt(portNum.getText().toString());

        mHandler = new Handler();
        try{
            setSocket(ip, port);
        } catch (IOException e){
            Log.e("Socket", "소켓 생성 에러");
        }

        thread.start();
    }

    public void setSocket(String ip, int port) throws IOException{
        try{
            socket = new Socket(ip, port);
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch(IOException e){
            Log.e("Socket", "소켓 생성 에러");
        }
    }

    private Thread thread = new Thread(){
        char[] buffer = {'a', 'b', 'c', 'd'};
        public void run(){
            try{
//                String line;
                while(true){
//                    line = bufferedReader.readLine();
//                    Log.d("통신 test", line);
                    bufferedWriter.write(buffer, 0, buffer.length);
                }
            } catch (Exception e){
                Log.e("쓰레드 에러", "에러");
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
