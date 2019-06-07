package com.things.factory.ssdp;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

public class Device {
    private String osName;
    private String st;
    private String macAddress;
    private String ipAddress;

    public String getOsName() {
        return System.getProperty("os.name");
    }

    public String getIpAddress() {
        List<NetworkInterface> all = null;
        try {
            all = Collections.list(NetworkInterface.getNetworkInterfaces());
        } catch(SocketException e) {
            e.printStackTrace();
            return null;
        }

        String ipAddress = null;
        for (NetworkInterface nif : all) {
            Enumeration<InetAddress> addresses = nif.getInetAddresses();
            while (addresses.hasMoreElements() && (ipAddress == null || ipAddress.isEmpty())) {
                InetAddress address = addresses.nextElement();
                if (!address.isLoopbackAddress() && address.isSiteLocalAddress()) {
                    ipAddress = address.getHostAddress();
                }
            }
        }

        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getDeviceSt() {
        if (this.st != null) {
            return this.st;
        }

        try {
            this.st = this.getFileContent("/etc/strongswan.conf");
            return st;
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getMacAddress() {
        if (macAddress != null && !macAddress.isEmpty()) {
            return this.macAddress;
        }

        //wifi
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) {
                    continue;
                }

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return null;
                }

                StringBuilder temp = new StringBuilder();
                for (int i = 0; i < macBytes.length; i++) {   //macBytes: { 8, 120, 8, -82, 18, 2 }
                    byte b = macBytes[i];
                    String block = String.format("%02X", b).toUpperCase();
                    String t = i == macBytes.length - 1 ? block : block + ":";
                    temp.append(t);
                }

                //temp: 08:78:08:AE:12:02
                return temp.toString();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

        //ethernet ?
        try {
            return getFileContent("/sys/class/net/eth0/address").toUpperCase().substring(0, 17);
        } catch (IOException e) {
            e.printStackTrace();
            return "02:00:00:00:00:00";
        }
    }

    private String getFileContent(String filePath) throws java.io.IOException {
        StringBuffer data = new StringBuffer(1000);
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        char[] buf = new char[1024];
        int numRead = 0;
        while ((numRead = reader.read(buf)) != -1) {
            String readData = String.valueOf(buf, 0, numRead);
            data.append(readData);
        }

        reader.close();
        return data.toString();
    }
}
