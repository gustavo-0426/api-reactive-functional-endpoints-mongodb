package com.co.softworld.configuration;

import com.co.softworld.handler.ProductHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterFunctional {

    @Bean
    public RouterFunction<ServerResponse> routes(ProductHandler productHandler) {
        return route(GET("/api/routerFunction/product"), request ->  productHandler.list())
                .andRoute(GET("/api/routerFunction/product/{id}"), productHandler::detail)
                .andRoute(POST("/api/routerFunction/product"), productHandler::save)
                .andRoute(PUT("/api/routerFunction/product/{id}"), productHandler::update)
                .andRoute(DELETE("/api/routerFunction/product/{id}"), productHandler::delete);
    }

}
