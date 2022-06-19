package com.co.softworld.handler;

import com.co.softworld.model.Product;
import com.co.softworld.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
public class ProductHandler {

    @Autowired
    private IProductService productService;

    public Mono<ServerResponse> list(ServerRequest request) {
        return ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(productService.findAll(), Product.class);
    }
}
