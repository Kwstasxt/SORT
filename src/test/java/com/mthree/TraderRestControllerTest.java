package com.mthree;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.URL;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
public class TraderRestControllerTest {

    @LocalServerPort
    private int port; // bind the above RANDOM_PORT

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void getWelcome() throws Exception {
    	
    	// TODO:

//        ResponseEntity<String> response = restTemplate.getForEntity(
//			new URL("http://localhost:" + port + "/").toString(), String.class);
//        
//        assertEquals("Hello Controller", response.getBody());
    }
    
    @Test
    public void getHome() throws Exception {
    	
    	// TODO:

//        ResponseEntity<String> response = restTemplate.getForEntity(
//			new URL("http://localhost:" + port + "/").toString(), String.class);
//        
//        assertEquals("Hello Controller", response.getBody());
    }
}