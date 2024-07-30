package mfreitas.msticket.configs;

import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class InternalizationConfig {

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setDefaultLocale(Locale.getDefault());
        return messageSource;
    }

    @Bean
    public LocalValidatorFactoryBean validatorFactoryBean() {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource());
        return bean;
    }

//     @Bean
//   public OpenAPI customOpenAPI() {
//     final String securitySchemeName = "bearerAuth";
//     return new OpenAPI()
//         .servers(List.of(new Server().url("http://localhost:8765")))
//         .components(
//             new Components()
//                 .addSecuritySchemes(
//                     securitySchemeName,
//                     new SecurityScheme()
//                         .type(SecurityScheme.Type.HTTP)
//                         .scheme("bearer")
//                         .bearerFormat("JWT")))
//         .security(List.of(new SecurityRequirement().addList(securitySchemeName)))
//         .info(new Info().title("serviceTitle").version("serviceVersion"));
//   }
}
