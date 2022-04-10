package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class TCPClient_Waiting extends AppCompatActivity {

    private PrintWriter pw;
    private BufferedReader br;
    private Socket socket;
    private String input = "";
    private byte[] SendMsg = new byte[0];
    private String RecMsg = "";
    private final String serverip = "192.168.0.122";
    private final int serverport = 5422;

    private void send(String msg){
        pw.println(msg);
        pw.flush();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("hihihih");
        setContentView(R.layout.activity_tcpclient_waiting);

        //Bundle test = this.getIntent().getExtras();
        //input = test.getString("test");
        //System.out.println(input);

        Bundle sendmsg_resource = this.getIntent().getExtras();
        byte[] sendmsg = sendmsg_resource.getByteArray("all_photo");
        SendMsg = addBytes(SendMsg, sendmsg);
        System.out.println(sendmsg.length);


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
            send(input);
            //send(SendMsg);
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
        System.out.println(RecMsg);
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

}