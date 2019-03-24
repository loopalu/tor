package com.tor.clientserver.listener;

import com.tor.clientserver.serverClient.ClientServerPeerToPeer;

import java.io.*;

public class ClientListener {
    public static void main(String[] args) throws IOException {
        new ClientServerPeerToPeer(9000);
    }

}

class ClientListener2 {
    public static void main(String[] args) {
        new ClientServerPeerToPeer(9000);
    }
}


class ClientListener3 {
    public static void main(String[] args) {
        new ClientServerPeerToPeer(9000);
    }
}


