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
    int imageCount=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_second);

        Bundle getFromResult = this.getIntent().getExtras();
        String folderPath = getFromResult.getString("folder_path");

        Button to_result_page = (Button) findViewById(R.id.button_second);
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

        TextView page = (TextView) findViewById(R.id.page);
        ImageView tv = (ImageView)findViewById(R.id.imageView);
        Bitmap bmp = BitmapFactory.decodeFile(folderPath + "/photo" + String.valueOf(imageCount) + ".png");
        BitmapDrawable bmpDraw = new BitmapDrawable(bmp);
        tv.setImageDrawable(bmpDraw);
        String currentPage = String.valueOf(imageCount + 1) + "/6";
        page.setText(currentPage);

        Button next_image = (Button) findViewById(R.id.next);
        next_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageCount++ ;
                Bitmap bmp = BitmapFactory.decodeFile(folderPath + "/photo" + String.valueOf(imageCount % 6) + ".png");
                BitmapDrawable bmpDraw=new BitmapDrawable(bmp);
                tv.setImageDrawable(bmpDraw);
                if (imageCount >= 6)
                    imageCount = imageCount % 6;
                String currentPage = String.valueOf(imageCount + 1) + "/6";
                page.setText(currentPage);
            }
        });

        Button previous_image = (Button) findViewById(R.id.previous);
        previous_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageCount-- ;
                if (imageCount < 0)
                    imageCount = 5;
                Bitmap bmp = BitmapFactory.decodeFile(folderPath + "/photo" + String.valueOf(imageCount % 6) + ".png");
                BitmapDrawable bmpDraw=new BitmapDrawable(bmp);
                tv.setImageDrawable(bmpDraw);
                String currentPage = String.valueOf(imageCount + 1) + "/6";
                page.setText(currentPage);
            }
        });
    }

}