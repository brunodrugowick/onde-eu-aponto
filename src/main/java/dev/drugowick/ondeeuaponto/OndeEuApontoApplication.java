package dev.drugowick.ondeeuaponto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class OndeEuApontoApplication {

    public static void main(String[] args) {
        SpringApplication.run(OndeEuApontoApplication.class, args);
    }

}
