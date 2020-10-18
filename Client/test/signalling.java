import java.io.BufferedInputStream;
//import java.io.BufferedReader;
//import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
//import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Array;
//import java.lang.ProcessBuilder.Redirect.Type;
//import java.io.StringReader;
import java.net.InetAddress;
import java.net.ServerSocket; //This class implements server sockets. A server socket waits for requests to come in over the network.
import java.net.Socket; //This class implements client sockets (also called just "sockets")
import java.nio.charset.Charset;
//import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Arrays;


//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//import com.google.gson.JsonDeserializationContext;
//import com.google.gson.JsonDeserializer;
//import com.google.gson.JsonElement;
//import com.google.gson.JsonParseException;
//import com.google.gson.JsonPrimitive;
//import com.google.gson.JsonSerializer;
//import com.google.gson.stream.JsonReader;

public class signalling {

    static void showbits(byte[] decoded, int size) {
        // int length = decoded.length;
        for (int i = 0; i < size; i++) {
            byte aByte = (byte) decoded[i];
            int result = aByte & 0xff;
            System.out.print(Integer.toBinaryString(result) + " "); // 1111 1110
        }
        System.out.println();
    }

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        int d= 128;
        System.out.println(d);
        InetAddress addr = InetAddress.getByName("192.168.1.107");
        InetAddress addr_1 = InetAddress.getLocalHost();
        System.out.println("Server has started on " + addr_1.getHostName() + ".\r\nWaiting for connection...");

        ServerSocket webSoc = new ServerSocket(8080, 5, addr);
        try {

            System.out.println("Server has started on " + addr + ".\r\nWaiting for connection...");
            Socket client = webSoc.accept();
            System.out.println("A client connected.");
            InputStream in = client.getInputStream();
            OutputStream out = client.getOutputStream();
            Scanner s = new Scanner(in, "UTF-8");
            try {
                String data = s.useDelimiter("\\r\\n\\r\\n").next();
                Matcher get = Pattern.compile("^GET").matcher(data);

                // Creating Response Hearer[HandShake]
                if (get.find()) {
                    Matcher match = Pattern.compile("Sec-WebSocket-Key: (.*)").matcher(data);
                    match.find();
                    byte[] response = ("HTTP/1.1 101 Switching Protocols\r\n" + "Connection: Upgrade\r\n"
                            + "Upgrade: websocket\r\n" + "Sec-WebSocket-Accept: "
                            + Base64.getEncoder().encodeToString(MessageDigest.getInstance("SHA-1").digest(
                                    (match.group(1) + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11").getBytes("UTF-8")))
                            + "\r\n\r\n").getBytes();
                    out.write(response, 0, response.length);
                    DataInputStream stdIn = new DataInputStream(new BufferedInputStream(client.getInputStream()));
                    while (true) {
                        byte[] bytes = new byte[8192];
                        int size = stdIn.read(bytes);
                        byte payloadByte = (byte) bytes[1];
                        byte[] key = new byte[4];
                        int payloadSize = payloadByte & 0xff;

                        if (payloadSize >= 128) {
                            System.out.println("The Payload Data is Masked");
                            if ((payloadSize - 128) <= 125) {
                                byte[] decoded = new byte[size - 6];
                                for (int i = 0; i < 4; i++) {
                                    key[i] = (byte) bytes[i + 2];
                                }
                                for (int i = 0; i < size - 6; i++) {
                                    decoded[i] = (byte) (bytes[i + 6] ^ key[i % 4]);
                                }
                                System.out.print("Message:\n");
                                /*
                                 * for (int i = 0; i < decoded.length; i++) {
                                 * 
                                 * byte aByte = (byte) decoded[i]; int unsigned = aByte & 0xff; char result =
                                 * (char) unsigned; System.out.print((result)); // 1111 1110 }
                                 */
                                String result = new String(decoded, Charset.forName("UTF-8"));
                                System.out.print((result));
                            } else if ((payloadSize - 128) == 126) {
                                byte[] decoded = new byte[size - 8];
                                for (int i = 0; i < 4; i++) {
                                    key[i] = (byte) bytes[i + 4];
                                }
                                for (int i = 0; i < size - 8; i++) {
                                    decoded[i] = (byte) (bytes[i + 8] ^ key[i & 0x3]);
                                }
                                // for (int i = 0; i < decoded.length; i++) {
                                // byte aByte = (byte) decoded[i];
                                // int unsigned = aByte & 0xff;
                                // char result = (char) aByte;
                                String result = new String(decoded, Charset.forName("UTF-8"));
                                System.out.print((result)); // 1111 1110
                                // }

                            } else if ((payloadSize - 128) == 127) {
                                byte[] decoded = new byte[size - 16];
                                for (int i = 0; i < 4; i++) {
                                    key[i] = (byte) bytes[i + 12];
                                }
                                for (int i = 0; i < size - 8; i++) {
                                    decoded[i] = (byte) (bytes[i + 16] ^ key[i & 0x3]);
                                }
                                for (int i = 0; i < decoded.length; i++) {
                                    // byte aByte = (byte) decoded[i];
                                    // int unsigned = aByte & 0xff;
                                    // har result = (char) aByte;
                                    String result = new String(decoded, Charset.forName("UTF-8"));
                                    System.out.print((result)); // 1111 1110
                                }
                            }
                        } else {
                            System.out.print("The Payload Data is not Masked");
                        }
                        System.out.println();
                    }
                }
            } finally {
                in.close();
                out.close();
                client.close();
                s.close();
            }
        } finally {
            webSoc.close();
        }
    }
}

/*
 * class Base64toJson implements JsonDeserializer<String> {
 * 
 * @Override public String deserialize(JsonElement src, java.lang.reflect.Type
 * typeOfSrc, JsonDeserializationContext context) throws JsonParseException {
 * return new String(src.getAsJsonPrimitive().getAsString());
 * 
 * } }
 */
class Json {
    public String candidate​;
    public int sdpMLineIndex;
    public String sdpMid;
    public String usernameFragment;
    /*
     * candidate; public int sdpMLineIndex; public String sdpMid; public String
     * usernameFragment;
     */
}