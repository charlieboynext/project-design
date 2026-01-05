package org.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;

@SpringBootApplication
public class Application {

    @Autowired
    private Environment environment;

    public static void main(String[] args){
        SpringApplication.run(Application.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void printAccessInfo() {
        String port = environment.getProperty("server.port", "8080");
        System.out.println("\n");
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║     后端服务器启动成功！              ║");
        System.out.println("╠════════════════════════════════════════╣");
        System.out.println("║  后端 API 地址: http://localhost:" + port + "  ║");
        System.out.println("║  前端访问地址: http://localhost:5173   ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.println();
    }
}
