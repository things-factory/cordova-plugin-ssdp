package com.things.factory.ssdp;

import android.os.Build;
import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import static com.things.factory.ssdp.SSDPMessage.NEWLINE;

public class SSDPServer extends Thread {
    private MulticastSocket socket;
    private DeviceAndroid device = null;

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
        if (device == null) {
            device = new DeviceAndroid();
        }

        try {
            while(true) {
                this.receiving();
            }
        } catch(IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            socket.close();
        }
    }

    private void receiving() throws IOException {
        byte[] buf = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        socket.receive(packet);
        InetAddress address = packet.getAddress();
        int port = packet.getPort();

        String received = new String(packet.getData(), 0, packet.getData().length);
        if (received.contains(SSDPMessage.SL_MSEARCH) && received.contains(SSDPMessage.ST_TF) || received.contains(device.getDeviceSt())) {
        //if (received.contains(SSDPMessage.SL_MSEARCH) && this.st.equalsIgnoreCase(SSDPMessage.ST_TF)) {
            // response device info
            SSDPResponse response = new SSDPResponse();
            response.setCacheMaxAge(60);
            response.setLocation(device.getIpAddress());
            response.setServer(System.getProperty("os.name") + "/" + Build.VERSION.RELEASE + " UPnP/1.1 " + SSDPMessage.DOMAIN_NAME + "/vfree");
            //response.setServer(device.getManufacture() + ":" + device.getModel());
            //response.setSt(SSDPMessage.ST_TF + System.getProperty("os.name") + ":" + Build.VERSION.RELEASE);
            response.setSt(device.getDeviceSt());
            response.setUsn(device.getManufacture() + ":" + device.getModel() + ":" + device.getMacAddress());  // android specific

            String message = response.getMessage();
            byte[] bytes = message.getBytes();
            socket.send(new DatagramPacket(bytes, bytes.length, address, port));
            Log.d("SSDP", "response: " + NEWLINE + message);
        } else if (received.contains(SSDPMessage.SL_NOTIFY)) {
            // FIXME save?
        }

    }
}