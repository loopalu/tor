package com.tor.clientserver.model;


public class Client {
    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    private String ip;


    @Override
    public String toString() {
        return "Client{" +
                "ip='" + ip + '\'' +
                '}';
    }

    public Client(String ip) {
        this.ip = ip;
    }
}
