package mfreitas.apigateway.configs;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;

@Configuration
public class JwtTokenRelayFilterConfig {

    @Value("${jwt.publicKeyUri}")
    private String publicKeyUri;

    @Bean
    @Order(2)
    public GlobalFilter customGlobalFilter() {
        // Cria um decodificador JWT baseado na URI do conjunto de chaves JWK
        ReactiveJwtDecoder jwtDecoder = NimbusReactiveJwtDecoder.withJwkSetUri(publicKeyUri).build();

        return (exchange, chain) -> {
            String token = exchange.getRequest().getHeaders().getFirst("Authorization");

            if (token != null && token.startsWith("Bearer ")) {
                String jwtToken = token.substring(7);
                return jwtDecoder.decode(jwtToken).flatMap(jwt -> {
                    // Aqui você extrairia o ID do usuário ou qualquer identificador relevante das claims do JWT
                    String id = jwt.getClaimAsString("id");
                    String username = jwt.getClaimAsString("username");
                    String mode = jwt.getClaimAsString("mode");

                    List<String> authorities = jwt.getClaimAsStringList("authorities");
                    String roles = authorities.stream()
                        .map(authority -> authority.replaceFirst("ROLE_", "")) // Remove o prefixo "ROLE_"
                        .collect(Collectors.joining(","));

                    // Adiciona o ID do usuário como um cabeçalho na requisição
                    ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
                        .header("X-User-Id", id)
                        .header("X-User-Username", username)
                        .header("X-User-Roles", roles)
                        .header("X-User-Mode", mode)
                        .build();
                    return chain.filter(exchange.mutate().request(modifiedRequest).build());
                }).onErrorResume(e -> chain.filter(exchange));
            }

            return chain.filter(exchange);
        };
    }
}
