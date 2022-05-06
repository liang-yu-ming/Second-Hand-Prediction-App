package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Result extends AppCompatActivity {

    private String folderPath = "";
    private int i=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Bundle bundle = this.getIntent().getExtras();
        String fromActivity = bundle.getString("from");
        System.out.println("fromActivity = " + fromActivity);
        folderPath = bundle.getString("folder_path");

        String bookInfor = readFromFile(folderPath, "book_information.txt");
        String[] bookInforSplit = bookInfor.split("_");
        String bookName = bookInforSplit[1];
        bookName = bookName.replace("\"", "");
        String scoreStr = bookInforSplit[5];
        int scoreInt = Integer.valueOf(scoreStr);
        String originalPriceStr = bookInforSplit[6];
        originalPriceStr = originalPriceStr.replace("$","");
        String discountStr = bookInforSplit[7];
        int originalPriceInt = Integer.valueOf(originalPriceStr);
        double discountInt = Double.valueOf(discountStr) * 100;
        discountStr = String.valueOf((int)discountInt) + "%";
        if(scoreInt >= 0 && scoreInt < 1000)
            scoreStr += "  " + "(A 級:書況優良)";
        else if(scoreInt >= 1000 && scoreInt < 2000)
            scoreStr += "  " +  "(B 級:書況良好)";
        else if(scoreInt >= 2000 && scoreInt < 3000)
            scoreStr += "  " +  "(C 級:書況正常)";
        else if(scoreInt >= 3000 && scoreInt < 4000)
            scoreStr += "  " +  "(D 級:書況不佳)";
        else if(scoreInt >= 4000 && scoreInt < 5000)
            scoreStr += "  " +  "(E 級:書況極遭)";

        TextView bookNameText = (TextView) findViewById(R.id.Title);
        bookNameText.setText(bookName);

        TextView scoreText = (TextView) findViewById(R.id.Grade);
        scoreText.setText(scoreStr);

        TextView discountText = (TextView) findViewById(R.id.Propotion);
        discountText.setText(discountStr);

        TextView predictPriceText = (TextView) findViewById(R.id.Price);
        if(originalPriceInt == 0){
            predictPriceText.setText("未輸入原始價格");
        }else {
            String predictPriceStr = String.valueOf((int)Math.ceil(originalPriceInt * discountInt / 100));
            predictPriceText.setText(predictPriceStr);
        }

        Button to_main_page = (Button) findViewById(R.id.button4);
        to_main_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Result.this, MainActivity.class);
                startActivity(intent);
            }
        });

        Button to_result_second_page = (Button) findViewById(R.id.ImageProblem);
        to_result_second_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Result.this, Result_second.class);
                Bundle bundle = new Bundle();
                bundle.putString("folder_path", folderPath);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        ImageView tv = (ImageView)findViewById(R.id.imageView);
        Button next_image = (Button) findViewById(R.id.button2);
        Bitmap bmp = BitmapFactory.decodeFile(folderPath + "/photo" + String.valueOf(0) + ".png");
        BitmapDrawable bmpDraw=new BitmapDrawable(bmp);
        tv.setImageDrawable(bmpDraw);
        i++ ;

        next_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (i) {
                    case 0:
                        Bitmap bmp = BitmapFactory.decodeFile(folderPath + "/photo" + String.valueOf(i) + ".png");
                        BitmapDrawable bmpDraw=new BitmapDrawable(bmp);
                        tv.setImageDrawable(bmpDraw); i++ ;break;
                    case 1:
                        Bitmap bmp1 = BitmapFactory.decodeFile(folderPath + "/photo" + String.valueOf(i) + ".png");
                        BitmapDrawable bmpDraw1=new BitmapDrawable(bmp1);
                        tv.setImageDrawable(bmpDraw1); i++ ;break;
                    case 2:
                        Bitmap bmp2 = BitmapFactory.decodeFile(folderPath + "/photo" + String.valueOf(i) + ".png");
                        BitmapDrawable bmpDraw2=new BitmapDrawable(bmp2);
                        tv.setImageDrawable(bmpDraw2); i++ ;break;
                    case 3:
                        Bitmap bmp3 = BitmapFactory.decodeFile(folderPath + "/photo" + String.valueOf(i) + ".png");
                        BitmapDrawable bmpDraw3=new BitmapDrawable(bmp3);
                        tv.setImageDrawable(bmpDraw3); i++ ;break;
                    case 4:
                        Bitmap bmp4 = BitmapFactory.decodeFile(folderPath + "/photo" + String.valueOf(i) + ".png");
                        BitmapDrawable bmpDraw4=new BitmapDrawable(bmp4);
                        tv.setImageDrawable(bmpDraw4); i++ ;break;
                    case 5:
                        Bitmap bmp5 = BitmapFactory.decodeFile(folderPath + "/photo" + String.valueOf(i) + ".png");
                        BitmapDrawable bmpDraw5=new BitmapDrawable(bmp5);
                        tv.setImageDrawable(bmpDraw5); i++ ;break;
                    case 6:
                        Bitmap bmp6 = BitmapFactory.decodeFile(folderPath + "/photo" + String.valueOf(i) + ".png");
                        BitmapDrawable bmpDraw6=new BitmapDrawable(bmp6);
                        tv.setImageDrawable(bmpDraw6); i++ ;break;
                    case 7:
                        Bitmap bmp7 = BitmapFactory.decodeFile(folderPath + "/photo" + String.valueOf(i) + ".png");
                        BitmapDrawable bmpDraw7=new BitmapDrawable(bmp7);
                        tv.setImageDrawable(bmpDraw7); i++ ;break;
                    case 8:
                        Bitmap bmp8 = BitmapFactory.decodeFile(folderPath + "/photo" + String.valueOf(i) + ".png");
                        BitmapDrawable bmpDraw8=new BitmapDrawable(bmp8);
                        tv.setImageDrawable(bmpDraw8); i++ ;break;
                    case 9:
                        Bitmap bmp9 = BitmapFactory.decodeFile(folderPath + "/photo" + String.valueOf(i) + ".png");
                        BitmapDrawable bmpDraw9=new BitmapDrawable(bmp9);
                        tv.setImageDrawable(bmpDraw9); i++ ;break;
                    case 10:
                        Bitmap bmp10 = BitmapFactory.decodeFile(folderPath + "/photo" + String.valueOf(i) + ".png");
                        BitmapDrawable bmpDraw10=new BitmapDrawable(bmp10);
                        tv.setImageDrawable(bmpDraw10); i++ ;break;
                    case 11:
                        Bitmap bmp11 = BitmapFactory.decodeFile(folderPath + "/photo" + String.valueOf(i) + ".png");
                        BitmapDrawable bmpDraw11=new BitmapDrawable(bmp11);
                        tv.setImageDrawable(bmpDraw11); i++ ;break;
                    case 12:
                        Bitmap bmp12 = BitmapFactory.decodeFile(folderPath + "/photo" + String.valueOf(i) + ".png");
                        BitmapDrawable bmpDraw12=new BitmapDrawable(bmp12);
                        tv.setImageDrawable(bmpDraw12); i++ ;break;
                    case 13:
                        Bitmap bmp13 = BitmapFactory.decodeFile(folderPath + "/photo" + String.valueOf(i) + ".png");
                        BitmapDrawable bmpDraw13=new BitmapDrawable(bmp13);
                        tv.setImageDrawable(bmpDraw13); i++ ;break;
                }
                if(i >= 14)
                    i = 0;
            }
        });
    }

    private String readFromFile(String folderName, String fileName){
        String path = folderName + "/" + fileName;
        String fileAsString = "";
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line = br.readLine();
            StringBuilder sb = new StringBuilder();

            while (line != null) {
                sb.append(line).append("\n");
                line = br.readLine();
            }

            fileAsString = sb.toString();
            System.out.println(fileAsString);
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        return fileAsString;
    }
}