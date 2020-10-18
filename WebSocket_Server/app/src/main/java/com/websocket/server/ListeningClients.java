package com.websocket.server;

import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ListeningClients implements Runnable{
    private static final String TAG = "ServerSocketInit";
    int port,backlog;
    InetAddress localIP;

    public ListeningClients(int port, int backlog, InetAddress ip){
        this.port = port;
        this.backlog = backlog;
        this.localIP = ip;

    }

    @Override
    public void run() {
        try {
            while (true) {
                if (localIP == null) throw new NullPointerException("The IP Address is null");
                ServerSocket server = new ServerSocket(port, backlog, localIP);
                Log.i(TAG, "Listening...");
                Socket client = server.accept();

            }
        }catch (IOException | NullPointerException  e){
            Log.e(TAG, "Listening: ", e);
        }
    }
}
