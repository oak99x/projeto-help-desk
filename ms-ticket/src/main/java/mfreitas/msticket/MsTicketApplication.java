package mfreitas.msticket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@EnableFeignClients(basePackages = "mfreitas.msticket.feignClients")
@EnableDiscoveryClient
@OpenAPIDefinition(info =
@Info(title = "Ticket API", description = "Documentation Ticket API v1.0")
)
public class MsTicketApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsTicketApplication.class, args);
	}

}
