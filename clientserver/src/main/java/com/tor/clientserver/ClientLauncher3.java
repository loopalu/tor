package com.tor.clientserver;

import com.tor.clientserver.clientServer.CreateClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Configuration
@PropertySource("classpath:application-client3.properties")
@SpringBootApplication
public class ClientLauncher3 implements CommandLineRunner {

    @Value("${server.port}")
    String port;

    public static void main(String[] args) {
        new SpringApplicationBuilder()
                .sources(ClientLauncher3.class)
                .profiles("client3")
                .run(args);
    }

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }


    @Override
    public void run(String... args) throws IOException {
        CreateClient networkClient = new CreateClient();
        networkClient.startAskingClient(port);
    }



}

