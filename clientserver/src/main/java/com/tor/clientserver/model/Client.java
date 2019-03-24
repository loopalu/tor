package com.tor.clientserver.model;

public class Client {
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
