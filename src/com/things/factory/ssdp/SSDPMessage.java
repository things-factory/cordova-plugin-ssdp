package com.things.factory.ssdp;

public class SSDPMessage {
    public static final String NEWLINE = "\r\n";
    public static final String ADDRESS = "239.255.255.250";
    public static final int PORT = 1900;
    public static final String DOMAIN_NAME = "things-factory";
    public static final String SL_OK = "HTTP/1.1 200 OK";
    public static final String SL_MSEARCH = "M-SEARCH * HTTP/1.1";
    public static final String SL_NOTIFY = "NOTIFY * HTTP/1.1";
    public static final String HOST = new StringBuffer("HOST: ").append(ADDRESS).append(":").append(PORT).toString();
    public static final String MAN = "MAN: \"ssdp:discover\"";
    //urn:domain-name:device:deviceType:ver
    public static final String ST_TF = new StringBuffer("urn:").append(DOMAIN_NAME).append(":device").append(":all").append(":all").toString();

    private String location;
    private String server;
    private String st;
    private String usn;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getSt() {
        return st;
    }

    public void setSt(String st) {
        this.st = st;
    }

    public String getUsn() {
        return usn;
    }

    public void setUsn(String usn) {
        this.usn = usn;
    }
}
