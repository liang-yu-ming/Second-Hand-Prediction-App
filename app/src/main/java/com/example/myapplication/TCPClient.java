package com.example.myapplication;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class TCPClient implements Runnable {
    private final String  serverIP;
    private final int serverPort;
    private boolean isSend = false;
    private PrintWriter pw;
    private BufferedReader br;
    private Socket socket;
    private Context context;
    public  String RecMsg = "";

    public TCPClient(String ip, int port, Context context){
        this.serverIP = ip;
        this.serverPort = port;
        this.context = context;
    }
    //public boolean getStatus(){
        //return isRun;
    //}determine the device whether connect or not

    public void send(String msg){
        pw.println(msg);
        pw.flush();
        pw.close();
        isSend = true;
    }

    public String receive(){
        String text = "";
        try {
            text = br.readLine();
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return text;
    }

    @Override
    public void run() {
        try {
            /*將Socket指向指定的IP & Port*/
            System.out.println(serverIP + " " + serverPort);
            socket = new Socket(serverIP,serverPort);
            socket.setSoTimeout(5000);
            System.out.println("231231");

            //isRun = true;

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
        while(!isSend);
        try {
            RecMsg = br.readLine();
            br.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

