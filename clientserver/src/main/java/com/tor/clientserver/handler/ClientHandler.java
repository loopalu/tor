package com.tor.clientserver.handler;

import com.tor.clientserver.communication.ClientCommunication;

import java.io.IOException;

public class ClientHandler {

    public static void main(String[] args) throws IOException {
        ClientCommunication createClient = new ClientCommunication();
        createClient.startAskingClient( 9000);
    }
}


class ClientHandler2 {

    public static void main(String[] args) throws IOException {
        ClientCommunication createClient = new ClientCommunication();
        createClient.startAskingClient(  9000);
    }
}

class ClientHandler3 {

    public static void main(String[] args) throws IOException {
        ClientCommunication createClient = new ClientCommunication();
        createClient.startAskingClient( 9000);
    }
}
