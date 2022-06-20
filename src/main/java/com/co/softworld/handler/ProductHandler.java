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
public class ProductHandler implements IProductHandler {

    @Autowired
    private IProductService productService;

    @Override
    public Mono<ServerResponse> list() {
        return ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(productService.findAll(), Product.class);
    }

    @Override
    public Mono<ServerResponse> detail(ServerRequest request) {
        return productService.findById(request.pathVariable("id"))
                .flatMap(product -> ServerResponse
                        .ok()
                        .bodyValue(product)
                ).switchIfEmpty(ServerResponse.notFound().build());
    }

    @Override
    public Mono<ServerResponse> save(ServerRequest request) {
        Mono<Product> productMono = request.bodyToMono(Product.class);
        return productMono
                .flatMap(product -> productService.save(product))
                .flatMap(product -> ServerResponse
                        .created(request.uri())
                        .bodyValue(product))
                .switchIfEmpty(ServerResponse.badRequest().build());
    }

}
