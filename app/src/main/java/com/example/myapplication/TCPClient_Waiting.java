package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TCPClient_Waiting extends AppCompatActivity {

    private PrintWriter pw;
    private BufferedReader br;
    private Socket socket;
    private String fileName = "packet";
    private int floderCount = 0;
    private String folderName = "";
    private byte[] SendMsg = new byte[0];
    private String RecMsg = "";
    private final String serverip = "120.126.151.184";
    private final int serverport = 5422;
    private String bookname = Data.Book.bookname;
    private String bookmonth = Data.Book.bookmonth;
    private String bookprize = Data.Book.bookprize;
    private String bookkind = Data.Book.bookkind;
    private int score = 0;
    private long CTR = 0;

    public void send(byte[] msg){
        System.out.println("SendMsg length = " + msg.length);
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

        ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        TCPClientThread tcpClientThread = new TCPClientThread();
        Thread t = new Thread(tcpClientThread);
        t.start();
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
        getServerData();
        try{
            socket.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        Intent intent = new Intent(TCPClient_Waiting.this, Result.class);

        Bundle bundle = new Bundle();
        bundle.putString("folder_path", folderName);
        bundle.putString("from", "TCPClient_Waiting");
        intent.putExtras(bundle);
        reptile();
        calculatePrice();

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
        int imageCount = 0;
        try {
            byte[] frame =  new byte[1024];
            byte[] data = new byte[0];
            byte[] current_data;
            while (data.length < 4){
                int current_rec = socket.getInputStream().read(frame);
                System.out.println("current_rec = " + current_rec);
                current_data = new byte[current_rec];
                System.arraycopy(frame, 0, current_data, 0,current_rec);
                data = addBytes(data, current_data);
            }
            byte[] alldatalength_byte = new byte[4];
            System.arraycopy(data, 0, alldatalength_byte, 0,alldatalength_byte.length);
            data = cliparray(data, 4);
            int alldatalength_int = ByteBuffer.wrap(alldatalength_byte).getInt();
            while (data.length < 4){
                int current_rec = socket.getInputStream().read(frame);
                current_data = new byte[current_rec];
                System.arraycopy(frame, 0, current_data, 0,current_rec);
                data = addBytes(data, current_data);
            }
            byte[] score_byte = new byte[4];
            System.arraycopy(data, 0, score_byte, 0,score_byte.length);
            data = cliparray(data, 4);
            score = ByteBuffer.wrap(score_byte).getInt();
            System.out.println("origin score = " + score);
            if(score > 1600)
                score = (score - 1600);
            else
                score = 0;
            System.out.println("after score = " + score);
            alldatalength_int -= 4;
            String scoreStr = "_" + String.valueOf(score);
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
                data = cliparray(data, 4);
                int oneimagelength_int = ByteBuffer.wrap(oneimagelength_byte).getInt();
                alldatalength_int -= 4;
                while (data.length < oneimagelength_int){
                    int current_rec = socket.getInputStream().read(frame);
                    current_data = new byte[current_rec];
                    System.arraycopy(frame, 0, current_data, 0,current_rec);
                    data = addBytes(data, current_data);
                }
                byte[] oneimage = new byte[oneimagelength_int];
                System.arraycopy(data, 0, oneimage, 0,oneimage.length);
                data = cliparray(data, oneimage.length);
                alldatalength_int -= oneimagelength_int;

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
    public void calculatePrice(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss"); // 取得目前的時間
        String currentTimeStr = format.format(new Date()); // 轉成 String
        String[] tmpSplit = currentTimeStr.split("-");
        String[] currentTimeSplit = tmpSplit[0].split("/"); // 將 String 分割
        int currentTimeInt = Integer.valueOf(currentTimeSplit[0]) * 365 + Integer.valueOf(currentTimeSplit[1]) * 30 + Integer.valueOf(currentTimeSplit[2]);
        String[] bookTimeSplit = bookmonth.split("-"); // 取得書本的時間
        int bookTimeInt = Integer.valueOf(bookTimeSplit[0]) * 365 + Integer.valueOf(bookTimeSplit[1]) * 30 + Integer.valueOf(bookTimeSplit[2]);
        double age =  currentTimeInt - bookTimeInt;
        age = age / 365.0;// 取得年齡
        System.out.println("age = " + age);
        double discount = 0; // 宣告折舊比例
        String t = "";
        double item1 = (double)CTR / ((double)CTR + 1000.0);
        double item2 = Math.pow(2.71, -Math.pow(score / 150.0, 2.0));
        if(bookkind.equals("文學小說") || bookkind.equals("人文史地")){
            System.out.println("in 文史");
            double item3 = 30.0 / (Math.log(age + 1.0) + 30.0);
            discount = item1 * item2 * item3;
        } else {
            System.out.println("in other");
            double item3 = ((4.0 / 5.0) * (1.0 / (Math.pow(1.8, age - 8) + 1.0))) + (1.0 / 5.0 * 50.0 / (age + 50.0));
            discount = item1 * item2 * item3;
        }
        discount = (float)Math.round(discount*100.0)/100.0;
        System.out.println("discount = " + discount);
        if(bookprize.equals("")){
            t = "_" + String.valueOf(0) + "_" + String.valueOf(discount);
        }
        else {
            t = "_" + bookprize + "_" + String.valueOf(discount);
        }
        File dir = new File(folderName);
        String index = "book_information.txt";
        write_string(dir, index, t);
    }

    public void write_string(File file, String name, String input){
        File outFile = new File(file, name);
        FileOutputStream outputStream;
        try {
            outputStream = new FileOutputStream(outFile, true);
            outputStream.write(input.getBytes(StandardCharsets.UTF_8));
            outputStream.flush();
            outputStream.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void reptile(){
        String tmp = "";
        if(isNetworkAvailable(this)){
            String NetUrl = "https://www.google.com/search?q=" + bookname;
            System.out.println("Url = " + NetUrl);
            Connection conn = Jsoup.connect(NetUrl);
            conn.header("User-Agent",
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.66 Safari/537.36");
            try {
                final Document docs = conn.get();
                tmp = docs.select("div[id=result-stats]").text();
            }catch (IOException e){
                e.printStackTrace();
            }
        }else {
            System.out.println("Network error");
        }
        String[] tmpSplit = tmp.split(" ");
        NumberFormat format = NumberFormat.getInstance(Locale.US);
        Number number = 0;
        try {
            number = format.parse(tmpSplit[1]);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        CTR = number.longValue();
        System.out.println("CTR = " + CTR);
    }

    public boolean isNetworkAvailable(Activity activity){
        Context context = activity.getApplicationContext();
        ConnectivityManager cm = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE) ;
        if(cm == null)
            return false;
        else {
            NetworkInfo[] networkInfo = cm.getAllNetworkInfo();
            if (networkInfo != null && networkInfo.length > 0){
                for (int i = 0;i < networkInfo.length; i++)
                    if(networkInfo[i].getState() == NetworkInfo.State.CONNECTED)
                        return  true;
            }
        }
        return false;
    }
}