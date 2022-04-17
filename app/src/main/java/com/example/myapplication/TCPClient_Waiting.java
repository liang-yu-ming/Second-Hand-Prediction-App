package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class TCPClient_Waiting extends AppCompatActivity {

    private PrintWriter pw;
    private BufferedReader br;
    private Socket socket;
    private String fileName = "test";
    private int floderCount = 0;
    private byte[] SendMsg = new byte[0];
    private String RecMsg = "";
    private final String serverip = "192.168.0.122";
    private final int serverport = 5422;

    //private void send(String msg){
    //    pw.println(msg);
    //    pw.flush();
    //}

    public void send(byte[] msg){
        try {
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(msg);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tcpclient_waiting);

        Bundle test = this.getIntent().getExtras();
        floderCount = test.getInt("folderName");
        File dir = this.getFilesDir();
        String folderName = dir.getPath() + "/" + String.valueOf(floderCount);
        File folder = new File(folderName);
        byte[] tmp = read_tmp(folder, fileName);
        SendMsg = addBytes(SendMsg, tmp);

        TCPClientThread tcpClientThread = new TCPClientThread();
        Thread t = new Thread(tcpClientThread);
        t.start();
        try {
            t.join();
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }

        String bookname = Data.Book.bookname;
        String bookmonth = Data.Book.bookmonth;
        String bookprize = Data.Book.bookprize;
        String bookkind = Data.Book.bookkind;


        Intent intent = new Intent(TCPClient_Waiting.this, Result.class);

        Bundle bundle = new Bundle();
        bundle.putString("result", RecMsg);
        intent.putExtras(bundle);

        startActivity(intent);

    }

    private class TCPClientThread implements Runnable{
        @Override
        public void run(){
            setTcpConnect();
        }
    }

    private void setTcpConnect(){
        try {
            socket = new Socket(serverip,serverport);
            socket.setSoTimeout(5000);
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("123", String.valueOf(e));
        }
        try {
            pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "utf-8")), true);
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            //send(fileName);
            send(SendMsg);
            RecMsg = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try{
            pw.close();
            br.close();
            socket.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        //System.out.println("RecMsg : " + RecMsg);
    }

    public static byte[] addBytes(byte[] data1, byte[] data2) {
        byte[] data3 = new byte[data1.length + data2.length];
        System.arraycopy(data1, 0, data3, 0, data1.length);
        System.arraycopy(data2, 0, data3, data1.length, data2.length);
        return data3;
    }

    public  byte[]  read_tmp(File file, String name){
        File inFile = new File(file, name);
        byte[] tmp = readFromFile(inFile);
        return tmp;
    }

    private byte[] readFromFile(File fin) {
        int size = (int) fin.length();
        byte bytes[] = new byte[size];
        byte tmpBuff[] = new byte[size];
        FileInputStream fis = null;
        try {
            fis= new FileInputStream(fin);
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
        }
        try {
            int read = fis.read(bytes, 0, size);
            if (read < size) {
                int remain = size - read;
                while (remain > 0) {
                    read = fis.read(tmpBuff, 0, remain);
                    System.arraycopy(tmpBuff, 0, bytes, size - remain, read);
                    remain -= read;
                }
            }
        }  catch (IOException e){
            e.printStackTrace();
        }
        try {
            fis.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }

        return bytes;
    }

}