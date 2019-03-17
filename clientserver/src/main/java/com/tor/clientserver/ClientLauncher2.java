package com.tor.clientserver;

import com.tor.clientserver.clientServer.CreateClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.io.IOException;

@Configuration
@PropertySource("classpath:application-client2.properties")
@SpringBootApplication
class ClientLauncher2 implements CommandLineRunner {

    @Value("${server.port}")
    String port;

    public static void main(String[] args) {
        new SpringApplicationBuilder()
                .sources(ClientLauncher2.class)
                .profiles("client2")
                .run(args);
    }

    @Override
    public void run(String... args) throws IOException {
        CreateClient networkClient = new CreateClient();
        networkClient.startAskingClient(port);
    }
}