package com.example.lab3;

import org.springframework.boot.SpringApplication;

public class TestLab3Application {

    public static void main(String[] args) {
        SpringApplication.from(Lab3Application::main).with(TestcontainersConfiguration.class).run(args);
    }

}
