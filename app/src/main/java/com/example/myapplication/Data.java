package com.example.myapplication;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
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
                String sPos=String.valueOf(pos);
                String sInfo=parent.getItemAtPosition(pos).toString();
                //String sInfo=parent.getSelectedItem().toString()
            }
            public void onNothingSelected(AdapterView<?> parent) {
                //
            }

        };
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