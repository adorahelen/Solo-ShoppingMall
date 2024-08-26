package edu.example.restz.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

// http://localhost:8080/swagger-ui/index.html
// http://localhost:8080/v3/api-docs
// http://localhost:8080/v3/api-docs.yaml
@OpenAPIDefinition(
        info=@Info (
        title = "REST API",
        version = "ver 0.1",
        description = "RESTful API Documentation"
        ),
        servers = { @Server(
                description = "Prod ENV",
                url = "http://localhost:8080/"
        ),
                @Server(
                        description = "Staging ENV",
                        url = "http://localhost:8080/staging"
                )
        }
)
public class SwaggerOpenAPIConfig {

}
