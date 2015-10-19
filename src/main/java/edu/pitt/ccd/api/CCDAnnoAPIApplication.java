package edu.pitt.ccd.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;

/**
 * @author Mark Silvis
 */
@SpringBootApplication
public class CCDAnnoAPIApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(CCDAnnoAPIApplication.class, args);
    }

    @Override
    public void run(String... strings) {
        System.out.println("\nAPI application running");
    }
}
