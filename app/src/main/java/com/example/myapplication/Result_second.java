package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;




import org.w3c.dom.Text;

import java.io.File;

public class Result_second extends AppCompatActivity {
    int i=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_second);

        Bundle getFromResult = this.getIntent().getExtras();
        String folderPath = getFromResult.getString("folder_path");

        Button to_result_page = (Button) findViewById(R.id.button_second);
        Button next_image = (Button) findViewById(R.id.button);
        ImageView tv = (ImageView)findViewById(R.id.imageView);


        to_result_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Result_second.this, Result.class);
                Bundle bundle = new Bundle();
                bundle.putString("folder_path", folderPath);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        Bitmap bmp = BitmapFactory.decodeFile(folderPath + "/detected" + String.valueOf(0) + ".jpg");
        BitmapDrawable bmpDraw=new BitmapDrawable(bmp);
        tv.setImageDrawable(bmpDraw);
        i++ ;

        next_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (i) {
                    case 0:
                        Bitmap bmp = BitmapFactory.decodeFile(folderPath + "/detected" + String.valueOf(i) + ".jpg");
                        BitmapDrawable bmpDraw=new BitmapDrawable(bmp);
                        tv.setImageDrawable(bmpDraw); i++ ;break;
                    case 1:
                        Bitmap bmp1 = BitmapFactory.decodeFile(folderPath + "/detected" + String.valueOf(i) + ".jpg");
                        BitmapDrawable bmpDraw1=new BitmapDrawable(bmp1);
                        tv.setImageDrawable(bmpDraw1); i++ ;break;
                    case 2:
                        Bitmap bmp2 = BitmapFactory.decodeFile(folderPath + "/detected" + String.valueOf(i) + ".jpg");
                        BitmapDrawable bmpDraw2=new BitmapDrawable(bmp2);
                        tv.setImageDrawable(bmpDraw2); i++ ;break;
                    case 3:
                        Bitmap bmp3 = BitmapFactory.decodeFile(folderPath + "/detected" + String.valueOf(i) + ".jpg");
                        BitmapDrawable bmpDraw3=new BitmapDrawable(bmp3);
                        tv.setImageDrawable(bmpDraw3); i++ ;break;
                    case 4:
                        Bitmap bmp4 = BitmapFactory.decodeFile(folderPath + "/detected" + String.valueOf(i) + ".jpg");
                        BitmapDrawable bmpDraw4=new BitmapDrawable(bmp4);
                        tv.setImageDrawable(bmpDraw4); i++ ;break;
                    case 5:
                        Bitmap bmp5 = BitmapFactory.decodeFile(folderPath + "/detected" + String.valueOf(i) + ".jpg");
                        BitmapDrawable bmpDraw5=new BitmapDrawable(bmp5);
                        tv.setImageDrawable(bmpDraw5); i++ ;break;
                }
                if(i >= 6)
                    i = 0;
            }
        });
    }

}