package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.myapplication.databinding.FragmentFirstBinding;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
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
/*
        LinearLayout linear=(LinearLayout) getView().findViewById(R.id.history_button_layout);
        File dir = this.getActivity().getFilesDir();
        File[] files = dir.listFiles();;
        for (int i = 0; i < files.length; i++){
            String str = read(i);
            Button btn = new Button(getActivity());
            btn.setText(str);
            linear.addView(btn);
        }

 */

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public String read(int file_account){
        FileInputStream fis;
        BufferedReader reader=null;//這裡要初始化null
        StringBuilder builder=new StringBuilder();
        try{
            fis = getActivity().openFileInput("data" + String.valueOf(file_account));
            reader=new BufferedReader(new InputStreamReader(fis));
            String lines="";
            while((lines=reader.readLine())!=null){
                builder.append(lines);
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try {
                reader.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        return builder.toString();
    }
}