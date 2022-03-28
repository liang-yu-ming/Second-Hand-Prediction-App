package com.example.myapplication;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Data extends AppCompatActivity {

    TCPClient tcpClient;
    ExecutorService exec = Executors.newCachedThreadPool();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
    /*
        tcpClient = new TCPClient("10.201.30.55", 5422, this);
        exec.execute(tcpClient);
        exec.execute(()->tcpClient.run());
        System.out.println("hihihihihih");
     */
        AdapterView.OnItemSelectedListener spnOnItemSelected
                = new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };

        EditText test = (EditText)findViewById(R.id.editTextTextPersonName);
        Button to_data_page = (Button) findViewById(R.id.button3);
        to_data_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Data.this, Result.class);

                Bundle bundle = new Bundle();
                bundle.putString("test", test.getText().toString());
                intent.putExtras(bundle);
                /*
                if (tcpClient == null) return;
                if (test.getText().toString().length() == 0 || !tcpClient.getStatus()) ;
                exec.execute(() -> tcpClient.send(test.getText().toString()));
                tcpClient.closeClient();
                 */
                startActivity(intent);
            }
        });

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(this,
                        R.array.spinner_of_data,
                        android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(2, false);
        spinner.setOnItemSelectedListener(spnOnItemSelected);


    }
}