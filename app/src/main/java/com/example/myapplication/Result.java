package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.io.File;
import java.io.FileOutputStream;

public class Result extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Bundle test = this.getIntent().getExtras();
        String input = test.getString("result");
        //System.out.println("input: " + test.getString("test"));

        File dir = this.getFilesDir();
        //System.out.println(this.getFilesDir());
        //write_tmp(dir, "test1", "tmp1");
        File[] files = dir.listFiles();
        int count = files.length;
        String filename = "test" + String.valueOf(count+1);
        //System.out.println("filename: " + filename);
        //write_tmp(dir, filename, input);


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
                startActivity(intent);
            }
        });
    }

    public  void  write_tmp( File file, String name, String input){
        File outFile = new File(file, name);
        writeToFile(outFile, input);
    }

    private void writeToFile(File fout, String data) {
        FileOutputStream osw = null;
        try {
            osw = new FileOutputStream(fout);
            osw.write(data.getBytes());
            osw.flush();
        } catch (Exception e) {
            ;
        } finally {
            try {
                osw.close();
            } catch (Exception e) {
                ;
            }
        }
    }
}