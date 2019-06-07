package com.things.factory.ssdp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.things.factory.ssdp.SSDPMessage.NEWLINE;

public class SSDPClient extends Thread {
    public static final String ACTION_DISPATCH_DEVICE = "com.things.factory.ssdp.ACTION_DISPATCH_DEVICE";
    public static final String SSDP_MESSAGE = "SSDP_MESSAGE";

    private Context context;
    private MulticastSocket socket;
    private String st = null;  // search target
    private long timeout;

    private SSDPClient(Context context, String st, long timeout) {
        this.context = context;
        this.st = st;
        this.timeout = timeout;
        try {
            socket = new MulticastSocket(SSDPMessage.PORT);
            InetAddress inetAddress = InetAddress.getByName(SSDPMessage.ADDRESS);
            socket.joinGroup(inetAddress);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

//    // FIXME: for test
//    SSDPClient(MainActivity mainActivity) {
//        this.mainActivity = mainActivity;
//        try {
//            socket = new MulticastSocket(SSDPMessage.PORT);
//            InetAddress inetAddress = InetAddress.getByName(SSDPMessage.ADDRESS);
//            socket.joinGroup(inetAddress);
//        } catch(IOException e) {
//            e.printStackTrace();
//        }
//    }

    public void run() {
        try {
            SSDPSearch search = new SSDPSearch();
            if (this.st != null) {
                search.setSt(this.st);
            }
            String message = search.getMessage();
            byte[] bytes = message.getBytes();
            InetAddress inetAddress = InetAddress.getByName(SSDPMessage.ADDRESS);
            DatagramPacket p = new DatagramPacket(bytes, bytes.length, inetAddress, SSDPMessage.PORT);
            socket.send(p);

            long time = System.currentTimeMillis();
            long curTime = System.currentTimeMillis();
            while (curTime - time < timeout) {
                DatagramPacket packet = new DatagramPacket(new byte[1024], 1024);
                socket.receive(packet);
                String result = new String(packet.getData(), 0, packet.getData().length).trim();
                if (!result.contains("HTTP/1.1 200 OK")) {
                    continue;
                }
                Log.d("SSDP", "client received: " + NEWLINE + result);
                //Device device = this.parseHeader(packet);
                //SSDPLauncher launcher = new SSDPLauncher();
                //launcher.updateResult(device);

                SSDPMessage ssdpMessage = this.parseHeader(packet,true);
                //SSDPLauncher launcher = new SSDPLauncher();
                //launcher.updateResult(ssdpMessage);

                Intent intent = new Intent();
                intent.setAction(ACTION_DISPATCH_DEVICE);
                String json = new Gson().toJson(ssdpMessage);
                Log.d("SSDP", "device::" + json);
                intent.putExtra(SSDP_MESSAGE, json);
                context.sendBroadcast(intent);

//                // FIXME: for test
//                mainActivity.receiveResult(results);
                curTime = System.currentTimeMillis();
            }
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            socket.close();
        }

        socket.close();
    }

    protected static void search(Context context, String st, long timeout) {
        timeout = timeout > 0 ? timeout : 5 * 1000;
        SSDPClient client = new SSDPClient(context, st, timeout);
        client.start();
    }

//    // FIXME: for test
//    public static void search(Context context, long timeout) {
//        timeout = timeout > 0 ? timeout : 5 * 1000;
//        SSDPClient client = new SSDPClient(context, timeout);
//        client.start();
//    }

    private DeviceAndroid parseHeader(DatagramPacket ssdpResult) {
        Map<String, String> headers = this.parse(ssdpResult);

        DeviceAndroid device = new DeviceAndroid();
        //headers.get("LOCATION");
        //String server = headers.get("SERVER");
        //String st = headers.get("ST");
        //String usn = headers.get("USN");

        // TODO

        return device;
    }

    private SSDPMessage parseHeader(DatagramPacket ssdpResult, boolean isOriginal) {
        Map<String, String> headers = this.parse(ssdpResult);

        SSDPMessage message = new SSDPMessage();
        message.setLocation(headers.get("LOCATION"));
        message.setServer(headers.get("SERVER"));
        message.setSt(headers.get("ST"));
        message.setUsn(headers.get("USN"));

        return message;
    }

    private Map<String, String> parse(DatagramPacket packet) {
        HashMap<String, String> headers = new HashMap<String, String>();
        Pattern pattern = Pattern.compile("(.*): (.*)");

        String[] lines = new String(packet.getData(), 0, packet.getData().length).trim().split("\r\n");

        for (String line : lines) {
            Matcher matcher = pattern.matcher(line);
            if(matcher.matches()) {
                headers.put(matcher.group(1).toUpperCase(), matcher.group(2));
            }
        }

        return headers;
    }
}
