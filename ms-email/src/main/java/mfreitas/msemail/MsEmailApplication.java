package mfreitas.msemail;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@EnableDiscoveryClient
@OpenAPIDefinition(info =
@Info(title = "Email API", description = "Documentation Email API v1.0")
)
public class MsEmailApplication{

	public static void main(String[] args) {
		SpringApplication.run(MsEmailApplication.class, args);
	}

}

	
