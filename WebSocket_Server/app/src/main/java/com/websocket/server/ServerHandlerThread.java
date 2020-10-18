package com.websocket.server;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.Process;

public class ServerHandlerThread extends HandlerThread {
    private Handler serverRequest;

    public ServerHandlerThread() {
        super("ServerHandler", Process.THREAD_PRIORITY_DEFAULT);
    }

    @Override
    protected void onLooperPrepared() {
        serverRequest = new Handler();
    }

    public Handler getServerHandler() {
        return serverRequest;
    }
}
