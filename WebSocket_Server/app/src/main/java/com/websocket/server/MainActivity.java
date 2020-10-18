package com.websocket.server;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private Handler uiHandler = new Handler(Looper.getMainLooper());
    private ServerHandlerThread serverHanlder = new ServerHandlerThread();
    ListeningClients init = new ListeningClients(8080,5,Getipaddress.getInetipv4());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        uiHandler.post(new fetchIP());
        serverHanlder.start();
    }

    public void sendMessage(View view) throws IOException {
        serverHanlder.getServerHandler().post(init);
    }

    public void stopServer(View view){
        serverHanlder.getServerHandler().removeCallbacks(init);;
    }

    public class Request implements Runnable {
        ServerSocket server;
        Socket client;

        @Override
        public void run() {
            try {

                InputStream in = client.getInputStream();
                OutputStream out = client.getOutputStream();
                Scanner s = new Scanner(in, "UTF-8");
                String data = s.useDelimiter("\\r\\n\\r\\n").next();
                Matcher get = Pattern.compile("^GET").matcher(data);
                // Creating Response Hearer[HandShake]
                if (get.find()) {
                    Matcher match = Pattern.compile("Sec-WebSocket-Key: (.*)").matcher(data);
                    match.find();
                    byte[] response = ("HTTP/1.1 101 Switching Protocols\r\n" + "Connection: Upgrade\r\n"
                            + "Upgrade: websocket\r\n" + "Sec-WebSocket-Accept: "
                            + android.util.Base64.encodeToString(MessageDigest.getInstance("SHA-1").digest(
                            (match.group(1) + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11").getBytes("UTF-8")), Base64.DEFAULT)
                            + "\r\n\r\n").getBytes();
                    out.write(response, 0, response.length);
                }
                client.close();
                server.close();
            } catch (IOException | NoSuchAlgorithmException e) {
                Log.e(TAG, "Error: Clinet Not Accepted");
                e.printStackTrace();
            }
        }
    }

    private class fetchIP implements Runnable{
        @Override
        public void run() {
            TextView textView = (TextView) findViewById(R.id.ip_value);
            try {
                textView.setText(Getipaddress.getStringipv4());
            } catch (SocketException e) {
                e.printStackTrace();
            }
        }
    }
}