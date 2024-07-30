package mfreitas.msticket.configs;

import java.util.Enumeration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import feign.RequestInterceptor;
import jakarta.servlet.http.HttpServletRequest;

@Configuration
public class FeignConfig {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (requestAttributes != null) {
                HttpServletRequest request = requestAttributes.getRequest();
                String userId = request.getHeader("X-User-Id");
                
                if (userId != null) {
                    requestTemplate.header("X-User-Id", userId);
                }
            }
        };
    }
}

//Passa tudo
// @Configuration
// public class FeignConfig {

//     //Repassa todos os dados do header
//     @Bean
//     public RequestInterceptor requestInterceptor() {
//         return requestTemplate -> {
//             ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//             if (requestAttributes != null) {
//                 HttpServletRequest request = requestAttributes.getRequest();

//                 // Exemplo de repasse de múltiplos cabeçalhos
//                 Enumeration<String> headerNames = request.getHeaderNames();
//                 if (headerNames != null) {
//                     while (headerNames.hasMoreElements()) {
//                         String name = headerNames.nextElement();
//                         String value = request.getHeader(name);

//                         // Repassa o cabeçalho para a chamada Feign
//                         requestTemplate.header(name, value);
//                     }
//                 }
//             }
//         };
//     }
// }
