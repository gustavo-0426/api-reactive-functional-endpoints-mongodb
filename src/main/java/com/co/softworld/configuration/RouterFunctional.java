package com.co.softworld.configuration;

import com.co.softworld.handler.ProductHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterFunctional {

    @Bean
    public RouterFunction<ServerResponse> routes(ProductHandler productHandler) {
        return route(GET("/api/routerFunction/product/findAll"), request ->  productHandler.list())
                .andRoute(GET("/api/routerFunction/product/findById/{id}"), productHandler::detail)
                .andRoute(POST("/api/routerFunction/product/save"), productHandler::save);
    }

}
