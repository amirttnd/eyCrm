package com.zaloni;

import com.zaloni.oData.service.ODataClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication(scanBasePackages = "com.zaloni")
public class MainApplication implements CommandLineRunner {

    @Autowired
    ODataClientService clientService;

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        try{

            clientService.perform("http://localhost:8080/odata");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
