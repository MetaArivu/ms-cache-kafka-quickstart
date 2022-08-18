package io.fusion.air.microservice.server.config;


import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.StringSchema;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;

/**
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */
@Configuration
public class SwaggerConfiguration {
    @Bean
    public OperationCustomizer customGlobalHeaders() {
        return (Operation operation, HandlerMethod handlerMethod) -> {
            Parameter missingParam1 = new Parameter()
                    .in(ParameterIn.HEADER.toString())
                    .schema(new StringSchema())
                    .name("Authorization")
                    .description("Authorization Bearer Token")
                    .required(true);

            Parameter missingParam2 = new Parameter()
                    .in(ParameterIn.HEADER.toString())
                    .schema(new StringSchema())
                    .name("Refresh-Token")
                    .description("Authorization Refresh Token")
                    .required(true);

            operation.addParametersItem(missingParam1);
            operation.addParametersItem(missingParam2);
            return operation;
        };
    }
}