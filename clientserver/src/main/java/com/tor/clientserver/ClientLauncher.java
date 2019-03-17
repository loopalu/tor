package com.tor.clientserver;

import com.tor.clientserver.clientServer.CreateClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.io.IOException;

@SpringBootApplication
class ClientLauncher  implements CommandLineRunner {

    @Value("${server.port}")
    String port;

    public static void main(String[] args) {
        new SpringApplicationBuilder()
                .sources(ClientLauncher.class)
                .profiles("client")
                .run(args);
    }

    @Override
    public void run(String... args) throws IOException {
        CreateClient networkClient = new CreateClient();
        networkClient.startAskingClient(port);
    }
}