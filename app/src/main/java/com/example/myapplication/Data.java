package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class Data extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        AdapterView.OnItemSelectedListener spnOnItemSelected
                = new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        };

        EditText test = (EditText) findViewById(R.id.editTextTextPersonName);
        Button to_tcpclientwaiting_page = (Button) findViewById(R.id.button3);
        to_tcpclientwaiting_page.setOnClickListener(v -> {
            Intent intent = new Intent(Data.this, TCPClient_Waiting.class);

            Bundle bundle = new Bundle();
            bundle.putString("test", test.getText().toString());
            intent.putExtras(bundle);

            startActivity(intent);
        });

    }
    /*
    public void onclickSend(View view){

        Spinner spinner = (Spinner) findViewById(R.id.spinner);  //取得spinner的參考
        String kind = String.valueOf(spinner.getSelectedItem()); //取得使用者在spinner選擇的項目

        EditText BookName = (EditText) findViewById(R.id.editTextTextPersonName); //取得BookName的參考
        String name = BookName.getText().toString(); //取得書籍名稱
        byte[] bookname = name.getBytes(StandardCharsets.UTF_8); //bookname為書籍名稱的byte型式

        EditText BookMonth = (EditText) findViewById(R.id.editTextTextPersonName2); //取得BookMonth的參考
        String month = BookMonth.getText().toString(); //取得書籍年齡(string)
        byte[] bookmonth = month.getBytes(StandardCharsets.UTF_8); //bookmonth為書籍年齡的byte型式
        //int bookmonth;
        //bookmonth = Integer.valueOf(month);//取得書籍年齡(int)

        EditText BookPrize = (EditText) findViewById(R.id.editTextTextPersonName3); //取得BookPrize的參考
        String prize = BookPrize.getText().toString(); //取得原始價格(string)
        byte[] bookprize = prize.getBytes(StandardCharsets.UTF_8); //bookprize為原始價格的byte型式
        //int bookprize;
        //bookprize = Integer.valueOf(prize);//取得原始價格(int)
    }
     */
}