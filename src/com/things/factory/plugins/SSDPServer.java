package com.things.factory.plugins;

import android.os.Build;
import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import static com.things.factory.plugins.SSDPMessage.NEWLINE;

public class SSDPServer extends Thread {
    private MulticastSocket socket;

    public SSDPServer() {
        try {
            socket = new MulticastSocket(SSDPMessage.PORT);
            InetAddress inetAddress = InetAddress.getByName(SSDPMessage.ADDRESS);
            socket.joinGroup(inetAddress);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void listen() {
        SSDPServer server = new SSDPServer();
        server.start();
    }

    public void run() {
        try {
            this.receiving();
        } catch(IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            socket.close();
        }
    }

    private void receiving() throws IOException {
        while(true) {
            byte[] buf = new byte[1024];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            socket.receive(packet);
            InetAddress address = packet.getAddress();
            int port = packet.getPort();

            String received = new String(packet.getData(), 0, packet.getLength());
            if (received.contains("M-SEARCH * HTTP/1.1") && received.contains(SSDPMessage.ST_TF)) {
                // response device info
                SSDPResponse response = new SSDPResponse();
                //Log.d("SSDP", System.getProperty("os.name"));   // Linux
                //Log.d("SSDP", String.valueOf(Build.VERSION.SDK_INT));   // 26
                //Log.d("SSDP", Build.VERSION.RELEASE);   // release version 8.0.0
                //Log.d("SSDP", Build.VERSION.CODENAME);  // REL
                //Log.d("SSDP", "BASE_OS: " + Build.VERSION.BASE_OS);   //
                //Log.d("SSDP", Build.DEVICE);    // gts3lwifi
                //Log.d("SSDP", Build.MODEL);     // SM-T820
                Device device = new Device();
                response.setLocation(device.getIpAddress());
                response.setServer(System.getProperty("os.name") + "/" + Build.VERSION.RELEASE + " UPnP/1.1 " + "THINGS-FACTORY/vfree");
                response.setSt(SSDPMessage.ST_TF + System.getProperty("os.name") + ":" + Build.VERSION.RELEASE);
                response.setUsn(device.getMacAddress());

                String message = response.getMessage();
                socket.send(new DatagramPacket(message.getBytes(), message.length(), address, port));
                Log.d("SSDP", "response: " + NEWLINE + message);
            } else if (received.contains("NOTIFY * HTTP/1.1")) {
                // FIXME save?
            }
        }
    }
}