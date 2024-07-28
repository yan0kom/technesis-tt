package technesis.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import technesis.regulator.Regulator;
import technesis.regulator.RegulatorFactory;

@SpringBootApplication
public class ServerApp {
    public static void main(String[] args) {
        SpringApplication.run(ServerApp.class, args);
    }

    @Bean
    public Regulator getRegulator() {
        return RegulatorFactory.createInstance();
    }
}
