package com.chenyh.netdisc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class NetdiscApplication {
    public static void main(String[] args) {
        SpringApplication.run(NetdiscApplication.class, args);
    }
}
