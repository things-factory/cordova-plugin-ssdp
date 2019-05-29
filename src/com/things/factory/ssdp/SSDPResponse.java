package com.things.factory.ssdp;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SSDPResponse extends SSDPMessage {
    private String method;
    private int cacheMaxAge;
    private String date;
    private String location;
    private String server;
    private String st;
    private String usn; // composite identifier for the advertisement
    private String bootId;
    private String configId;
    private String searchPort;

    SSDPResponse() {
        this.cacheMaxAge = 600;
        this.date = new Date().toString();
        this.location = HOST;
        // this.server = ;     // OS/version UPnP/1.1 product/version
        // this.usn
        // this.bootId
        // this.configId
        // this.searchPort
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public int getCacheMaxAge() {
        return cacheMaxAge;
    }

    public void setCacheMaxAge(int cacheMaxAge) {
        this.cacheMaxAge = cacheMaxAge;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

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

    public String getBootId() {
        return bootId;
    }

    public void setBootId(String bootId) {
        this.bootId = bootId;
    }

    public String getConfigId() {
        return configId;
    }

    public void setConfigId(String configId) {
        this.configId = configId;
    }

    public String getSearchPort() {
        return searchPort;
    }

    public void setSearchPort(String searchPort) {
        this.searchPort = searchPort;
    }

    public String getMessage() {
        StringBuilder content = new StringBuilder();
        content.append(SL_OK);
        content.append("CACHE-CONTROL: max-age=" + this.getCacheMaxAge()).append(NEWLINE);
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        content.append("DATE: " + dateString).append(NEWLINE);
        content.append("EXT: ").append(NEWLINE);
        content.append("LOCATION: ").append(this.getLocation()).append(NEWLINE);
        content.append("SERVER: ").append(this.getServer()).append(NEWLINE);
        content.append("ST: ").append(this.getSt()).append(NEWLINE);
        content.append("USN: ").append(this.getUsn()).append(NEWLINE);

        return content.toString();
    }
}
