package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TCPClient_Waiting extends AppCompatActivity {

    private PrintWriter pw;
    private BufferedReader br;
    private Socket socket;
    private String fileName = "packet";
    private int floderCount = 0;
    private String folderName = "";
    private byte[] SendMsg = new byte[0];
    private String RecMsg = "";
    private final String serverip = "192.168.0.122";
    private final int serverport = 5422;
    private String bookname = Data.Book.bookname;
    private String bookmonth = Data.Book.bookmonth;
    private String bookprize = Data.Book.bookprize;
    private String bookkind = Data.Book.bookkind;
    private int score = 0;
    private long CTR = 0;

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
        bundle.putString("result", RecMsg);
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
            //System.arraycopy(data, 4, data, 0, data.length - 4);
            data = cliparray(data, 4);
            int score_int = ByteBuffer.wrap(score_byte).getInt();
            score = score_int;
            System.out.println("score = " + score_int);
            alldatalength_int -= 4;
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
                while (data.length < oneimagelength_int){
                    int current_rec = socket.getInputStream().read(frame);
                    //System.out.println("current_rec = " + current_rec);
                    current_data = new byte[current_rec];
                    System.arraycopy(frame, 0, current_data, 0,current_rec);
                    data = addBytes(data, current_data);
                }
                byte[] oneimage = new byte[oneimagelength_int];
                System.arraycopy(data, 0, oneimage, 0,oneimage.length);
                //System.arraycopy(data, oneimage.length, data, 0, data.length - oneimage.length);
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
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss");
        String currentTime = format.format(new Date());
        String[] currentYear = currentTime.split("/");
        int currentAge = Integer.valueOf(currentYear[0]);
        String[] bookYear = bookmonth.split("-");
        int age =  currentAge - Integer.valueOf(bookYear[0]);
        System.out.println("age = " + age);
        double depreciation = 1 / score;
        double discount = 0;
        String t = "";
        if(bookkind.equals("文學小說") || bookkind.equals("人文史地")){
            System.out.println("in 文史");
            discount = Math.log(Math.log(CTR)) * Math.pow(1.5, 2 + depreciation + age);
        } else {
            System.out.println("in other");
            discount = 0.95 * Math.log(CTR) * depreciation / age;
        }
        discount = Math.round(discount*100.0)/100.0;
        System.out.println("discount = " + discount);
        if(bookprize.equals("$")){
            t = "_" + String.valueOf(0) + "_" + String.valueOf(discount);
        }
        else {
            t = "_" + bookprize + "_" + String.valueOf(discount);
        }
        System.out.println("t = " + t);
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
                System.out.println("tmp = " + tmp);
            }catch (IOException e){
                e.printStackTrace();
            }
        }else {
            System.out.println("Network error");
        }
        String[] tmpSplit = tmp.split(" ");
        System.out.println("tmpSplit = " + tmpSplit[1]);
        NumberFormat format = NumberFormat.getInstance(Locale.US);
        Number number = 0;
        try {
            number = format.parse(tmpSplit[1]);
            System.out.println("number = " + number);
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