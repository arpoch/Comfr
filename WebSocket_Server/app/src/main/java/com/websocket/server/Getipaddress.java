package com.websocket.server;

import android.util.Log;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.List;

public abstract class Getipaddress {
    private static final String TAG = "Getipaddress";
    public static String getStringipv4() throws SocketException {
        InetAddress ip = getInetipv4();
        return ip.getHostAddress();
    }

    public static InetAddress getInetipv4() {
        InetAddress localIP = null;
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        boolean isIPv4 = addr instanceof Inet4Address;
                        if (isIPv4) {
                            localIP = addr;
                            break;
                        }
                    }
                }
            }
        } catch (SocketException e) {
            Log.e(TAG, "Listening: ", e);
        }
        return localIP;
    }
}
