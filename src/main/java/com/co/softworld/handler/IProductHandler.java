package com.co.softworld.handler;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public interface IProductHandler {

    Mono<ServerResponse> upload(ServerRequest request);
    Mono<ServerResponse> uploadId(ServerRequest request);
    Mono<ServerResponse> list();
    Mono<ServerResponse> detail(ServerRequest request);
    Mono<ServerResponse> save(ServerRequest request);
    Mono<ServerResponse> update(ServerRequest request);
    Mono<ServerResponse> delete(ServerRequest request);
}
