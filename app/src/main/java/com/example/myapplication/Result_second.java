package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Result_second extends AppCompatActivity {
    int i=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_second);

        Button to_result_page = (Button) findViewById(R.id.button_second);
        Button next_image = (Button) findViewById(R.id.button);
        TextView tv = (TextView)findViewById(R.id.textView);


        to_result_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Result_second.this, Result.class);
                startActivity(intent);
            }
        });

        next_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (i) {
                    case 0: tv.setBackgroundResource(R.drawable.picture); i++ ;break;
                    case 1: tv.setBackgroundResource(R.drawable.picture2); i++ ;break;
                    case 2: tv.setBackgroundResource(R.drawable.picture3); i++ ;break;
                    case 3: tv.setBackgroundResource(R.drawable.picture4); i++ ;break;
                    case 4: tv.setBackgroundResource(R.drawable.picture5); i++ ;break;
                    case 5: tv.setBackgroundResource(R.drawable.picture6); i++ ;break;
                }
                if(i >= 6)
                    i = 0;
            }
        });
    }

}