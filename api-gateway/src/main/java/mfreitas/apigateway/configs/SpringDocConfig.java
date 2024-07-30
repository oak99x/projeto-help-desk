package mfreitas.apigateway.configs;

import static org.springdoc.core.utils.Constants.DEFAULT_API_DOCS_URL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springdoc.core.properties.AbstractSwaggerUiConfigProperties.SwaggerUrl;
import org.springdoc.core.properties.SwaggerUiConfigParameters;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;

@Configuration
public class SpringDocConfig {

	private Map<String, String> urlsSwaggerMap = new HashMap<>();

	@Bean
	@Lazy(false)
	public Set<SwaggerUrl> apis(RouteDefinitionLocator locator, SwaggerUiConfigParameters swaggerUiConfigParameters) {
		Set<SwaggerUrl> urls = new HashSet<>();
		List<RouteDefinition> definitions = locator.getRouteDefinitions().collectList().block();
		definitions.stream().filter(routeDefinition -> routeDefinition.getId().startsWith("ms-"))
				.forEach(routeDefinition -> {
					String name = routeDefinition.getId();
                    String url = DEFAULT_API_DOCS_URL + "/" + name;
                    urlsSwaggerMap.put(name, url);
                    SwaggerUrl swaggerUrl = new SwaggerUrl(name, url, null);
					urls.add(swaggerUrl);
				});
		swaggerUiConfigParameters.setUrls(urls);
		return urls;
	}

	@Bean
	@DependsOn("apis")
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        RouteLocatorBuilder.Builder routesBuilder = builder.routes();

        // Iterar sobre o map de URLs Swagger
        urlsSwaggerMap.forEach((serviceName, url) -> {
            routesBuilder.route(r -> r
					.path(url)
                    .and().method(HttpMethod.GET)
                    .uri("lb://" + serviceName));
        });

        return routesBuilder.build();
    }
}
