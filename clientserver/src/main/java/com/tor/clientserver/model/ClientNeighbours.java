package com.tor.clientserver.model;

public class ClientNeighbours {

    private String ips;

    @Override
    public String toString() {
        return "ClientNeighbours{" +
                "ips='" + ips + '\'' +
                '}';
    }

    public String getIps() {
        return ips;
    }

    public void setIps(String ips) {
        this.ips = ips;
    }
}
