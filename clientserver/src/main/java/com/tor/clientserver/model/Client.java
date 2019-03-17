package com.tor.clientserver.model;

import java.util.List;

public class Client {
    private String ip;
    private String action;

    @Override
    public String toString() {
        return "CreateClient{" +
                "ip='" + ip + '\'' +
                ", action='" + action + '\'' +
                '}';
    }

    public Client(String ip, String action) {
        this.ip = ip;
        this.action = action;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
