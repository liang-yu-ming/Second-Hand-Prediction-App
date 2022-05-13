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
    private int imageCount=0;

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

        TextView scoreText = (TextView) findViewById(R.id.GradeText);
        scoreText.setText(scoreStr);

        TextView discountText = (TextView) findViewById(R.id.discountText);
        discountText.setText(discountStr);

        TextView bookclassText = (TextView) findViewById(R.id.bookclassText);
        bookclassText.setText(bookInforSplit[4]);

        TextView predictPriceText = (TextView) findViewById(R.id.priceText);
        if(originalPriceInt == 0){
            TextView predictPriceTitle = (TextView) findViewById(R.id.priceTitle);
            predictPriceTitle.setText("未輸入價格");
            predictPriceText.setText("");
        }else {
            String predictPriceStr = String.valueOf((int)Math.ceil(originalPriceInt * discountInt / 100));
            predictPriceText.setText(predictPriceStr);
        }

        TextView ageText = (TextView) findViewById(R.id.ageText);
        ageText.setText(bookInforSplit[2]);

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

        TextView page = (TextView) findViewById(R.id.page);
        ImageView tv = (ImageView)findViewById(R.id.imageView);
        Bitmap bmp = BitmapFactory.decodeFile(folderPath + "/photo" + String.valueOf(imageCount) + ".png");
        BitmapDrawable bmpDraw = new BitmapDrawable(bmp);
        tv.setImageDrawable(bmpDraw);
        String currentPage = String.valueOf(imageCount + 1) + "/14";
        page.setText(currentPage);

        Button next_image = (Button) findViewById(R.id.next);
        next_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageCount++ ;
                Bitmap bmp = BitmapFactory.decodeFile(folderPath + "/photo" + String.valueOf(imageCount % 14) + ".png");
                BitmapDrawable bmpDraw=new BitmapDrawable(bmp);
                tv.setImageDrawable(bmpDraw);
                if (imageCount >= 14)
                    imageCount = imageCount % 14;
                String currentPage = String.valueOf(imageCount + 1) + "/14";
                page.setText(currentPage);
            }
        });

        Button previous_image = (Button) findViewById(R.id.previous);
        previous_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageCount-- ;
                if (imageCount < 0)
                    imageCount = 13;
                Bitmap bmp = BitmapFactory.decodeFile(folderPath + "/photo" + String.valueOf(imageCount % 14) + ".png");
                BitmapDrawable bmpDraw=new BitmapDrawable(bmp);
                tv.setImageDrawable(bmpDraw);
                String currentPage = String.valueOf(imageCount + 1) + "/14";
                page.setText(currentPage);
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