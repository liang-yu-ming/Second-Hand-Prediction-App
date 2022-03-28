package com.example.myapplication;

import android.content.Context;
import android.content.Intent;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class TCPClient implements Runnable {
    //private String TAG = TCPServer.TAG;
    private PrintWriter pw;
    private InputStream is;
    private DataInputStream dis;
    private String  serverIP;
    private int serverPort;
    private boolean isRun = true;
    private Socket socket;
    private Context context;
    private  int button_count = 0;

    public TCPClient(String ip , int port,Context context){
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

    public void send(byte[] msg){
        try {
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(msg);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }//send msg in byte array

    public void send(String msg){
        pw.print(msg);
        pw.flush();
    }//send msg in string

    @Override
    public void run() {
        byte[] buff = new byte[100];
        try {
            /*將Socket指向指定的IP & Port*/
            socket = new Socket(serverIP,serverPort);
            socket.setSoTimeout(5000);
            pw = new PrintWriter(socket.getOutputStream(),true);
            is = socket.getInputStream();
            dis = new DataInputStream(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (isRun){
            try {
                int rcvLen = dis.read(buff);
                String rcvMsg = new String(buff, 0, rcvLen, "utf-8");
                //Log.d(TAG, "收到訊息: "+ rcvMsg);

                Intent intent =new Intent();
                //intent.setAction(TCPServer.RECEIVE_ACTION);
                //intent.putExtra(TCPServer.RECEIVE_STRING, rcvMsg);
                context.sendBroadcast(intent);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            pw.close();
            is.close();
            dis.close();
            socket.close();
            //Log.d(TAG, "關閉Client");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void write(String inputText){
        FileOutputStream fos;
        BufferedWriter writer=null;//這裡要初始化一個null
        try{
            fos=context.openFileOutput("data"+ String.valueOf(button_count), Context.MODE_APPEND);
            writer=new BufferedWriter(new OutputStreamWriter(fos));
            writer.write(inputText);//這裡寫入引數
        }catch(IOException e){
            e.printStackTrace();
        }finally {
            try{
                if(writer != null){
                    writer.close();//關閉字元緩衝輸出流
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}

