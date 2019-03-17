package com.tor.clientserver.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.tor.clientserver.model.Connect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

@RestController
public class ClientController {
    @Autowired
    RestTemplate restTemplate;

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public String createProducts() throws IOException {
        Connect connect = new Connect();

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Wants to connect: ");
        String line = br.readLine();

        if (line.equals("true")) {
            connect.setLetsConnect(true);
        } else {
            connect.setLetsConnect(false);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        return new ObjectMapper().writeValueAsString(connect);
    }

}
