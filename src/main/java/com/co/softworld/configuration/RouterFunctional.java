package com.co.softworld.configuration;

import com.co.softworld.handler.ProductHandler;
import com.co.softworld.util.Utility;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static com.co.softworld.util.Utility.builderPath;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterFunctional {

    @Bean
    public RouterFunction<ServerResponse> routes(ProductHandler productHandler, Config config) {
        Path path = builderPath(config);
        return route(GET(path.getList()), request ->  productHandler.list())
                .andRoute(GET(path.getDetail()), productHandler::detail)
                .andRoute(POST(path.getSave()), productHandler::save)
                .andRoute(PUT(path.getUpdate()), productHandler::update)
                .andRoute(DELETE(path.getDelete()), productHandler::delete)
                .andRoute(POST(path.getUpload()), productHandler::upload)
                .andRoute(POST(path.getUploadId()), productHandler::uploadId);
    }
}
