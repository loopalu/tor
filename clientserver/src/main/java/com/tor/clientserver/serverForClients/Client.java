package com.tor.clientserver.serverForClients;

import com.tor.clientserver.clientServer.CreateClient;

import java.io.IOException;

public class Client {

    public static void main(String[] args) throws IOException {
        CreateClient createClient = new CreateClient();
        createClient.startAskingClient("1", 1);
    }
}


class Client2 {

    public static void main(String[] args) throws IOException {
        CreateClient createClient = new CreateClient();
        createClient.startAskingClient("2",  1);
    }
}

class Client3 {

    public static void main(String[] args) throws IOException {
        CreateClient createClient = new CreateClient();
        createClient.startAskingClient("3", 1);
    }
}

class Client4 {

    public static void main(String[] args) throws IOException {

    }
}
