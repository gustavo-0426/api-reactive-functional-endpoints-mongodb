package com.co.softworld.handler;

import com.co.softworld.configuration.Config;
import com.co.softworld.configuration.Path;
import com.co.softworld.model.Category;
import com.co.softworld.model.Product;
import com.co.softworld.service.IProductService;
import com.co.softworld.util.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.FormFieldPart;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;

import static com.co.softworld.configuration.IConstants.EXT_NAME_PHOTO;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
public class ProductHandler implements IProductHandler {

    @Autowired
    private IProductService productService;
    @Autowired
    private Config config;
    @Autowired
    private Validator validator;

    @Override
    public Mono<ServerResponse> upload(ServerRequest request) {
        Path path = Utility.builderPath(config);
        Mono<Product> productMono = request.multipartData().map(multiValueMap -> {
            FormFieldPart name = (FormFieldPart) multiValueMap.toSingleValueMap().get("name");
            FormFieldPart price = (FormFieldPart) multiValueMap.toSingleValueMap().get("price");
            FormFieldPart categoryId = (FormFieldPart) multiValueMap.toSingleValueMap().get("category.id");
            FormFieldPart categoryName = (FormFieldPart) multiValueMap.toSingleValueMap().get("category.name");
            Category category = Category.builder().id(categoryId.value()).name(categoryName.value()).build();
            return Product.builder().name(name.value()).price(Double.parseDouble(price.value())).category(category).build();
        });
        return request.multipartData().map(multiValueMap -> multiValueMap.toSingleValueMap().get("file"))
                .cast(FilePart.class)
                .flatMap(filePart -> productMono.flatMap(product -> {
                    product.setPhoto(product.getName().concat(EXT_NAME_PHOTO));
                    return filePart.transferTo(new File(path.getPhoto().concat(product.getPhoto())))
                            .then(productService.save(product));
                })).flatMap(product -> ServerResponse.created(request.uri())
                        .bodyValue(product));
    }

    @Override
    public Mono<ServerResponse> uploadId(ServerRequest request) {
        String id = request.pathVariable("id");
        Path path = Utility.builderPath(config);
        return request.multipartData().map(multiValueMap -> multiValueMap.toSingleValueMap().get("file"))
                .cast(FilePart.class)
                .flatMap(filePart -> productService.findById(id).flatMap(product -> {
                    product.setPhoto(product.getName().concat(EXT_NAME_PHOTO));
                    return filePart.transferTo(new File(path.getPhoto().concat(product.getPhoto())))
                            .then(productService.save(product));
                })).flatMap(product -> ServerResponse.created(request.uri())
                        .bodyValue(product))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

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
                .flatMap(product -> {
                    Errors errors = new BeanPropertyBindingResult(product, Product.class.getName());
                    validator.validate(product, errors);
                    if (errors.hasErrors())
                        return Flux.fromIterable(errors.getFieldErrors())
                                .map(fieldError -> String.format("Error: %s %s", fieldError.getField(), fieldError.getDefaultMessage()))
                                .collectList()
                                .flatMap(list -> ServerResponse.badRequest()
                                        .bodyValue(list));
                    return productService.save(product)
                            .flatMap(productDb -> ServerResponse
                                    .created(request.uri())
                                    .bodyValue(productDb));
                });
    }

    @Override
    public Mono<ServerResponse> update(ServerRequest request) {
        Mono<Product> productMonoBody = request.bodyToMono(Product.class);
        String id = request.pathVariable("id");
        Mono<Product> productMonoDb = productService.findById(id);
        return productMonoDb.zipWith(productMonoBody, (productDb, productBody) -> {
                    productDb.setName(productBody.getName());
                    productDb.setPrice(productBody.getPrice());
                    productDb.setPhoto(productBody.getPhoto());
                    productDb.setCategory(productBody.getCategory());
                    return productDb;
                }).flatMap(productMono -> ServerResponse
                        .created(request.uri())
                        .body(productService.save(productMono), Product.class))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    @Override
    public Mono<ServerResponse> delete(ServerRequest request) {
        String id = request.pathVariable("id");
        Mono<Product> productMonoDb = productService.findById(id);
        return productMonoDb.flatMap(product -> productService.remove(product)
                        .then(ServerResponse.noContent().build()))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

}
