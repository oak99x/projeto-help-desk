package mfreitas.apigateway.configs;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
@EnableMethodSecurity
public class ResourceServerConfig {

	@Value("${cors.origins}")
	private String corsOrigins;

	@Value("${jwt.publicKeyUri}") // URL da API que fornece as chaves JWT
	private String publicKeyUri;

	@Value("${jwt.client-id}")
	private String clientId;

	private static final String[] PUBLIC = {
			"/oauth2/**",
			"/v3/api-docs/**",
			"/swagger-ui/**",
			"/webjars/**",
			"/swagger-ui.html"
	};

	private static final String[] ADMIN = { "/api/admin/**" };

	private static final String[] ALLROLES = { "/api/user/**", "/api/ticket/**" };

	@Bean
	@Order(1)
	public SecurityWebFilterChain SecurityFilterChain(ServerHttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable())
			.authorizeExchange((authorize) -> authorize
					.pathMatchers(PUBLIC).permitAll()
					.pathMatchers(ADMIN).hasAuthority("ADMIN")
					.pathMatchers(ALLROLES).hasAnyAuthority("ADMIN", "USER")
					.anyExchange().authenticated());

		http.oauth2ResourceServer(oauth2 -> oauth2
				.jwt(jwt -> jwt
						.jwkSetUri(publicKeyUri)
						.jwtAuthenticationConverter(grantedAuthoritiesExtractor())));

		http.cors(cors -> cors.configurationSource(corsConfigurationSource()));
		//http.cors(cors -> cors.disable());
		return http.build();
	}

	Converter<Jwt, Mono<AbstractAuthenticationToken>> grantedAuthoritiesExtractor() {
		JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
		jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new GrantedAuthoritiesExtractor(clientId));
		return new ReactiveJwtAuthenticationConverterAdapter(jwtAuthenticationConverter);
	}

	static class GrantedAuthoritiesExtractor
			implements Converter<Jwt, Collection<GrantedAuthority>> {

		private final String clientId;

		public GrantedAuthoritiesExtractor(String clientId) {
			this.clientId = clientId;
		}

		public Collection<GrantedAuthority> convert(Jwt jwt) {

			// Verifique se o emissor (issuer) no token JWT é o esperado
			String issuer = jwt.getClaimAsString("iss");
			if (issuer == null || !clientId.equals(issuer)) {
				throw new OAuth2AuthenticationException("Token JWT inválido");
			}

			Collection<?> authorities = (Collection<?>) jwt.getClaims().getOrDefault("authorities",
					Collections.emptyList());

			return authorities.stream()
					.map(Object::toString)
					.map(authority -> {
						System.out.println(authority);
						int index = authority.indexOf('_'); // Encontra a posição do caractere '_'
						if (index != -1) {
							authority = authority.substring(index + 1); // Remove tudo antes de '_'
						}
						System.out.println(authority);
						return new SimpleGrantedAuthority(authority);
					})
					// .map(SimpleGrantedAuthority::new)
					.collect(Collectors.toList());
		}
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {

		String[] origins = corsOrigins.split(",");

		CorsConfiguration corsConfig = new CorsConfiguration();
		corsConfig.setAllowedOriginPatterns(Arrays.asList(origins));
		corsConfig.setAllowedMethods(Arrays.asList("POST", "GET", "PUT", "DELETE",
				"PATCH"));
		corsConfig.setAllowCredentials(true);
		corsConfig.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", corsConfig);
		return source;
	}

	@Bean
	public CorsWebFilter corsFilter() {
		return new CorsWebFilter(corsConfigurationSource());
	}

}
