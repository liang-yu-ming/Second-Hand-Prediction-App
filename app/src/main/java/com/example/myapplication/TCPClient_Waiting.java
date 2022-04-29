package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class TCPClient_Waiting extends AppCompatActivity {

    private PrintWriter pw;
    private BufferedReader br;
    private Socket socket;
    private String fileName = "test";
    private int floderCount = 0;
    private String folderName = "";
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
        folderName = dir.getPath() + "/" + String.valueOf(floderCount);
        File folder = new File(folderName);
        byte[] tmp = read_tmp(folder, fileName);
        SendMsg = addBytes(SendMsg, tmp);

        TCPClientThread tcpClientThread = new TCPClientThread();
        Thread t = new Thread(tcpClientThread);
        t.start();
        /*
        try {
            t.join();
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
         */

        String bookname = Data.Book.bookname;
        String bookmonth = Data.Book.bookmonth;
        String bookprize = Data.Book.bookprize;
        String bookkind = Data.Book.bookkind;



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
            socket.setSoTimeout(10000000);
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("123", String.valueOf(e));
        }
        send(SendMsg);
        System.out.println("already send");
        getServerData();
        try{
            socket.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        Intent intent = new Intent(TCPClient_Waiting.this, Result.class);

        Bundle bundle = new Bundle();
        bundle.putString("result", RecMsg);
        intent.putExtras(bundle);

        startActivity(intent);
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

    public void getServerData(){
        System.out.println("in get data");
        int imageCount = 0;
        try {
            byte[] frame =  new byte[1024];
            byte[] data = new byte[0];
            byte[] current_data;
            while (data.length < 4){
                int current_rec = socket.getInputStream().read(frame);
                current_data = new byte[current_rec];
                System.arraycopy(frame, 0, current_data, 0,current_rec);
                data = addBytes(data, current_data);
            }
            byte[] alldatalength_byte = new byte[4];
            System.arraycopy(data, 0, alldatalength_byte, 0,alldatalength_byte.length);
            data = cliparray(data, 4);
            int alldatalength_int = ByteBuffer.wrap(alldatalength_byte).getInt();
            System.out.println("alldatalength = " + alldatalength_int);

            while (data.length < 4){
                int current_rec = socket.getInputStream().read(frame);
                current_data = new byte[current_rec];
                System.arraycopy(frame, 0, current_data, 0,current_rec);
                data = addBytes(data, current_data);
            }
            byte[] score_byte = new byte[4];
            System.arraycopy(data, 0, score_byte, 0,score_byte.length);
            //System.arraycopy(data, 4, data, 0, data.length - 4);
            data = cliparray(data, 4);
            int score_int = ByteBuffer.wrap(score_byte).getInt();
            System.out.println("score = " + score_int);
            alldatalength_int -= 4;
            System.out.println("alldatalength = " + alldatalength_int);
            String scoreStr = "_" + String.valueOf(score_int);
            File scorefile = new File(folderName + "/book_information.txt");
            FileOutputStream outputStream;
            try {
                outputStream = new FileOutputStream(scorefile, true);
                outputStream.write(scoreStr.getBytes(StandardCharsets.UTF_8));
                outputStream.flush();
                outputStream.close();
            }catch (IOException e){
                e.printStackTrace();
            }

            while (alldatalength_int > 0){
                while (data.length < 4){
                    int current_rec = socket.getInputStream().read(frame);
                    current_data = new byte[current_rec];
                    System.arraycopy(frame, 0, current_data, 0,current_rec);
                    data = addBytes(data, current_data);
                }
                byte[] oneimagelength_byte = new byte[4];
                System.arraycopy(data, 0, oneimagelength_byte, 0,oneimagelength_byte.length);
                //System.arraycopy(data, 4,data, 0, data.length - 4);
                data = cliparray(data, 4);
                int oneimagelength_int = ByteBuffer.wrap(oneimagelength_byte).getInt();
                System.out.println("one image length = " + oneimagelength_int);
                alldatalength_int -= 4;
                System.out.println("alldatalength = " + alldatalength_int);
                while (data.length < oneimagelength_int){
                    int current_rec = socket.getInputStream().read(frame);
                    current_data = new byte[current_rec];
                    System.arraycopy(frame, 0, current_data, 0,current_rec);
                    data = addBytes(data, current_data);
                }
                byte[] oneimage = new byte[oneimagelength_int];
                System.arraycopy(data, 0, oneimage, 0,oneimage.length);
                //System.arraycopy(data, oneimage.length, data, 0, data.length - oneimage.length);
                data = cliparray(data, oneimage.length);
                alldatalength_int -= oneimagelength_int;
                System.out.println("qwe alldatalength = " + alldatalength_int);

                File imagefile = new File(folderName + "/detected" + String.valueOf(imageCount) + ".jpg");
                FileOutputStream fos = new FileOutputStream(imagefile);
                fos.write(oneimage, 0, oneimage.length);
                fos.flush();
                fos.close();
                imageCount++;
            }
        } catch (EOFException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public byte[] cliparray(byte[] array, int x){
        byte[] array1 = new byte[array.length-x];
        System.arraycopy(array, x,array1, 0, array1.length);
        return array1;
    }

}