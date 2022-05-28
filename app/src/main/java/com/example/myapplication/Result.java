package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
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
    private int[] pageContent = new int[6];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Bundle bundle = this.getIntent().getExtras();
        String fromActivity = bundle.getString("from");
        folderPath = bundle.getString("folder_path");

        String bookInfor = readFromFile(folderPath, "book_information.txt");
        String[] bookInforSplit = bookInfor.split("_");
        String bookName = bookInforSplit[1];
        bookName = bookName.replace("\"", "");
        String bookPageContentStr = bookInforSplit[5];
        String[] bookPageContentSplit = bookPageContentStr.split(",");
        for(int i = 0; i < 6; i++)
            pageContent[i] = Integer.valueOf(bookPageContentSplit[i]);
        String scoreStr = bookInforSplit[6];
        int scoreInt = Integer.valueOf(scoreStr);
        String originalPriceStr = bookInforSplit[7];
        originalPriceStr = originalPriceStr.replace("$","");
        String discountStr = bookInforSplit[8];
        int originalPriceInt = Integer.valueOf(originalPriceStr);
        double discountInt = Double.valueOf(discountStr) * 100;
        discountStr = String.valueOf((int)discountInt) + "%";
        if(scoreInt == 0)
            scoreStr += "  " + "(A 級:書況優良)";
        else if(scoreInt < 50)
            scoreStr += "  " +  "(B 級:書況良好)";
        else if(scoreInt < 100)
            scoreStr += "  " +  "(C 級:書況正常)";
        else if(scoreInt < 150)
            scoreStr += "  " +  "(D 級:書況不佳)";
        else
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
            predictPriceText.setText("TWD " + predictPriceStr);
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

        TextView originPage = (TextView) findViewById(R.id.originPage);
        TextView page = (TextView) findViewById(R.id.page);
        ImageView tv = (ImageView)findViewById(R.id.imageView);
        Bitmap bmp = BitmapFactory.decodeFile(folderPath + "/photo" + String.valueOf(imageCount) + ".png");
        BitmapDrawable bmpDraw = new BitmapDrawable(bmp);
        tv.setImageDrawable(bmpDraw);
        String currentPage = String.valueOf(imageCount + 1) + "/14";
        page.setText(currentPage);
        originPage.setText("第" + pageContent[imageCount] + "頁");

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
                if(imageCount >= 0 && imageCount < 6)
                    originPage.setText("第" + pageContent[imageCount] + "頁");
                else if(imageCount == 6)
                    originPage.setText("書頁側面");
                else if(imageCount == 7 || imageCount ==8)
                    originPage.setText("書頁接縫");
                else if(imageCount == 9)
                    originPage.setText("封面");
                else if(imageCount == 10)
                    originPage.setText("書背");
                else if(imageCount == 11)
                    originPage.setText("封底");
                else if(imageCount == 12 || imageCount == 13)
                    originPage.setText("折口");
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
                System.out.println("imageCount = " + imageCount);
                if(imageCount >= 0 && imageCount < 6)
                    originPage.setText("第" + pageContent[imageCount] + "頁");
                else if(imageCount == 6)
                    originPage.setText("書頁側面");
                else if(imageCount == 7 || imageCount ==8)
                    originPage.setText("書頁接縫");
                else if(imageCount == 9)
                    originPage.setText("封面");
                else if(imageCount == 10)
                    originPage.setText("書背");
                else if(imageCount == 11)
                    originPage.setText("封底");
                else if(imageCount == 12 || imageCount == 13)
                    originPage.setText("折口");
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