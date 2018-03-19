package com.atmira.springboot.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.atmira.springboot.app.service.IUploadFileService;

@SpringBootApplication
public class ProjectBootDataJpaApplication implements CommandLineRunner {
    
    @Autowired
    IUploadFileService uploadService;
    
    @Autowired
    BCryptPasswordEncoder passwordEncoder;
    
    public static void main(
        String[] args){
        SpringApplication.run(ProjectBootDataJpaApplication.class, args);
    }
    
    @Override
    public void run(
        String... arg0) throws Exception{
        uploadService.deleteAll();
        uploadService.init();
        
        String password = "12345";
        
        for (int i = 0; i < 2; i++) {
            String bcryptPassword = passwordEncoder.encode(password);
            System.out.println(bcryptPassword);
        }
    }
}
