package com.example.wonyoungkim.embeddedproject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import me.relex.circleindicator.CircleIndicator;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.streamingButton)
    Button streamingButton;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.circleIndicator)
    CircleIndicator circleIndicator;

    private Unbinder unbinder;
    // pi에 연결되면 true로 바꿔주고 connectToPi 레이아웃 visibility 바꿔주자.
    private boolean isConnected = false;
    private Socket socket;
    private Handler mHandler;

    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);
        init();
    }

    public void init() {
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        toolbar.setTitle("My SOS");
        toolbar.setTitleTextColor(Color.WHITE);

        // set ViewPager
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
        viewPager.setCurrentItem(0);

        // set Indicator
        circleIndicator.setViewPager(viewPager);

        // 서비스 실행하자.
        Intent intent = new Intent(this, MyService.class);
        startService(intent);
    }

    @OnClick(R.id.streamingButton)
    public void showVideo(){
        if(StaticData.isSOS()) {
            Intent intent = getPackageManager().getLaunchIntentForPackage("org.videolan.vlc");
            startActivity(intent);

            return;
        }

        Toast.makeText(this, "긴급 상황이 아닙니다.", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
