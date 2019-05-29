package com.things.factory.plugins;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SSDPClient extends Thread {

//    private MainActivity mainActivity;  // FIXME
    private MulticastSocket socket;

    public SSDPClient() {
        try {
            socket = new MulticastSocket(SSDPMessage.PORT);
            InetAddress inetAddress = InetAddress.getByName(SSDPMessage.ADDRESS);
            socket.joinGroup(inetAddress);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

//    public SSDPClient(MainActivity mainActivity) {  // FIXME: for test
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
            String message = search.getMessage();
            // FIXME if message.getBytes().length == message.length
            InetAddress inetAddress = InetAddress.getByName(SSDPMessage.ADDRESS);
            DatagramPacket p = new DatagramPacket(message.getBytes(), message.length(), inetAddress, SSDPMessage.PORT);
            socket.send(p);

            long time = System.currentTimeMillis();
            long curTime = System.currentTimeMillis();
            String[] result = null;
            List<String[]> results = new ArrayList<>();
            while (curTime - time < 1000) {
                DatagramPacket packet = new DatagramPacket(new byte[1024], 1024);
                socket.receive(packet);
                //String received = new String(packet.getData(), 0, packet.getLength());
                result = parse(packet);
                results.add(result);
                curTime = System.currentTimeMillis();
            }

//            mainActivity.receiveResult(results);  // FIXME: for test

            SSDPLauncher launcher = new SSDPLauncher();
            launcher.updateResult(results);
        } catch(Exception e) {
            e.printStackTrace();
        }

        socket.close();
    }

    public static void search() {
        SSDPClient client = new SSDPClient();
        client.start();
    }

//    public static void search(MainActivity mainActivity) {  // FIXME: for test
//        SSDPClient client = new SSDPClient(mainActivity);
//        client.start();
//    }

    public static String[] parse(DatagramPacket ssdpResult) {
        HashMap<String, String> headers = new HashMap<String, String>();
        Pattern pattern = Pattern.compile("(.*): (.*)");

        String[] lines = new String(ssdpResult.getData()).split("\r\n");

        for (String line : lines) {
            Matcher matcher = pattern.matcher(line);
            if(matcher.matches()) {
                headers.put(matcher.group(1).toUpperCase(), matcher.group(2));
            }
        }

        String[] result = {headers.get("LOCATION"), headers.get("SERVER"), headers.get("ST"), headers.get("USN")};
        return result;
    }
}
