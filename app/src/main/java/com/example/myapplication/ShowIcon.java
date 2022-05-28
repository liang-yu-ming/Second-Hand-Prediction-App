package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class ShowIcon extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_icon);

        Thread t = new Thread(() -> { // lambda 簡化 runnable 和 run 的宣告
            try {
                Thread.sleep(2000);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
            Intent intent = new Intent();
            intent.setClass(ShowIcon.this, MainActivity.class);
            startActivity(intent);
        });
        t.start();
    }
}