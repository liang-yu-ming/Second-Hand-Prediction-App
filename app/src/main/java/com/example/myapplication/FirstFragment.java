package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.myapplication.databinding.FragmentFirstBinding;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LinearLayout linear=(LinearLayout) getView().findViewById(R.id.history_button_layout);

        File root = this.getActivity().getFilesDir();
        File[] files = root.listFiles();

        for (int i = 0; i < files.length; i++){
            if(files[i].getName().equals("tmp") == true){
                continue;
            }
            String file_path = files[i].getPath();
            File file = new File(file_path);
            String file_name = "book_information.txt";
            String str = read_tmp(file, file_name);
            String[] title = str.split("_");
            Button btn = new Button(getActivity());
            btn.setText(title[0] + " " + title[1]);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), Result.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("result", title[1]);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
            linear.addView(btn);
            //tem.out.println(str);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    public  String  read_tmp( File file, String name){
        File inFile = new File(file, name);
        String str = str = readFromFile(inFile);
        return str;
    }

    private String readFromFile(File fin) {
        StringBuilder data = new StringBuilder();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(fin), "utf-8"));
            String line;
            while ((line = reader.readLine()) != null) {
                data.append(line);
            }
        } catch (Exception e) {
            ;
        } finally {
            try {
                reader.close();
            } catch (Exception e) {
                ;
            }
        }
        return data.toString();
    }
}