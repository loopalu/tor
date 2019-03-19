package com.tor.clientserver.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.tor.clientserver.model.Client;
import com.tor.clientserver.model.Connect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

@RestController
public class ClientController {
    @Autowired
    RestTemplate restTemplate;

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public String connectWithNeighbour(HttpServletRequest request) throws IOException {
        Connect connect = new Connect();
        connect.setLetsConnect(true);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        HttpEntity<Connect> entity = new HttpEntity<Connect>(connect, headers);

//        StringBuilder buffer = new StringBuilder();
//        BufferedReader re = request.getReader();
//
//        String line;
//        while ((line = re.readLine()) != null) {
//            buffer.append(line);
//        }
//        String data = buffer.toString();
//
//        Connect getData = new ObjectMapper().readValue(data, Connect.class);
//
//        if (getData.getPort() != null) {
//
//            String url = "http://localhost:" + getData.getPort();
//
//            System.out.println(url);
//            System.out.println(Objects.requireNonNull(entity.getBody()).toString());
//
//            return restTemplate.exchange(url, HttpMethod.POST, entity, Connect.class);
//        }

        return new ObjectMapper().writeValueAsString(connect);
    }

}
