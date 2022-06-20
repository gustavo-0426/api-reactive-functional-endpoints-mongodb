package com.co.softworld.handler;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public interface IProductHandler {

    Mono<ServerResponse> list();
    Mono<ServerResponse> detail(ServerRequest request);
    Mono<ServerResponse> save(ServerRequest request);
}