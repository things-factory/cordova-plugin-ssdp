package com.things.factory.ssdp;

/**
 * Msg的实体类，格式详见toString()
 */
class SSDPSearch extends SSDPMessage {
    private String method;
    private String host;
    private String man;
    private int mx; /* seconds to delay response */
    private String st; /* Search target */

    SSDPSearch() {
        this.method = SL_MSEARCH;
        this.host = HOST;
        this.man = MAN;
        this.mx = 1;
        //this.st = ST_ALL;
        this.st = ST_TF;
    }

    public String getMessage() {
        StringBuilder content = new StringBuilder();
        content.append(method).append(NEWLINE);
        content.append(host).append(NEWLINE);
        content.append(man).append(NEWLINE);
        content.append("MX:" + mx).append(NEWLINE);
        content.append(st).append(NEWLINE);
        content.append(NEWLINE);
        return content.toString();
    }
}