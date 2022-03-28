package com.example.myapplication;

import android.content.Context;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class TCPClient implements Runnable {
    private String  serverIP;
    private int serverPort;
    private boolean isRun = false;
    private PrintWriter pw;
    private BufferedReader br;
    private Socket socket;
    private Context context;

    public TCPClient(String ip, int port, Context context){
        this.serverIP = ip;
        this.serverPort = port;
        this.context = context;
    }
    public boolean getStatus(){
        return isRun;
    }//determine the device whether connect or not

    public void closeClient(){
        isRun = false;
    }//stop connect

    public void send(String msg){
        pw.println(msg);
        pw.flush();
    }

    @Override
    public void run() {
        try {
            /*將Socket指向指定的IP & Port*/
            socket = new Socket(serverIP,serverPort);
            System.out.println(serverIP + " " + serverPort);
            socket.setSoTimeout(5000);
            System.out.println("231231");

            isRun = true;

        } catch (IOException e) {
            e.printStackTrace();
        }
        while (isRun){
            try {
                while (socket.isConnected()) {
                    pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                    br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            if (pw != null)
                pw.close();
            if (br != null)
                br.close();
            if (socket != null)
                socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
