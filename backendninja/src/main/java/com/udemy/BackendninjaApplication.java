package com.udemy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

// TODO: Auto-generated Javadoc
/**
 * The Class BackendninjaApplication.
 */
@SpringBootApplication
@EnableScheduling
public class BackendninjaApplication {
    
    /**
     * The main method.
     *
     * @param args the arguments
     */
    public static void main(
        String[] args){
        SpringApplication.run(BackendninjaApplication.class, args);
    }
}
