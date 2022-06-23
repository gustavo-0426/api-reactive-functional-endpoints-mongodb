package com.co.softworld.handler;

import com.co.softworld.model.Category;
import com.co.softworld.model.Product;
import com.co.softworld.service.ICategoryService;
import com.co.softworld.service.IProductService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.co.softworld.configuration.IConstants.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
class ProductHandlerTests {

    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private IProductService productService;
    @Autowired
    private ICategoryService categoryService;
    private final String ID = "id";
    private final String APPLE = "apple";
    private final String HOME = "home";
    private final String MOUSE = "mouse";
    private final String DIGIT = "digit";
    private final String WINDOWS = "windows";
    private final Double FIVE = 5.0;

    @AfterEach
    void tearDown() {
        webTestClient = null;
        productService = null;
        categoryService = null;
    }

    @Test
    void testList() {
        log.info("testList...");
        webTestClient.get()
                .uri(DEFAULT_BASE)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Product.class)
                .consumeWith(exchangeResult -> {
                    List<Product> productList = exchangeResult.getResponseBody();
                    if (Optional.ofNullable(productList).isPresent())
                        assertThat(productList.size() > 0).isTrue();
                });
    }

    @Test
    void testDetail() {
        Product productMono = productService.findByName(APPLE).block();
        if (Optional.ofNullable(productMono).isPresent()) {
            log.info("testDetail... id: " + productMono.getId());
            webTestClient.get()
                    .uri(DEFAULT_ID, Collections.singletonMap(ID, productMono.getId()))
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .jsonPath("$.id").isNotEmpty()
                    .jsonPath("$.name").isEqualTo(APPLE);
        }
    }


    @Test
    void testDetailConsumeWith() {
        Product productMono = productService.findByName(APPLE).block();
        if (Optional.ofNullable(productMono).isPresent()) {
            log.info("testDetailConsumeWith... id: " + productMono.getId());
            webTestClient.get()
                    .uri(DEFAULT_ID, Collections.singletonMap(ID, productMono.getId()))
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody(Product.class)
                    .consumeWith(exchangeResult -> {
                        Product product = exchangeResult.getResponseBody();
                        if (Optional.ofNullable(product).isPresent())
                            assertThat(product.getName()).isEqualTo(APPLE);
                    });
        }
    }

    @Test
    void testDetailConsumeWithNotFound() {
        log.info("testDetailConsumeWithNotFound...");
        webTestClient.get()
                .uri(DEFAULT_ID, Collections.singletonMap(ID, SPACE))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(Product.class)
                .consumeWith(exchangeResult -> assertThat(exchangeResult.getResponseBody()).isNull()
                );
    }

    @Test
    void testCreateConsumeWith() {
        Mono<Category> category = categoryService.findByName(HOME);
        Product product = Product.builder().name(WINDOWS).price(50.0).category(category.block()).build();
        log.info("testCreate... name: {}", product.getName());
        webTestClient.post()
                .uri(DEFAULT_BASE)
                .bodyValue(product)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Product.class)
                .consumeWith(exchangeResult -> {
                    Product productResponse = exchangeResult.getResponseBody();
                    if (Optional.ofNullable(productResponse).isPresent()) {
                        assertThat(productResponse.getId()).isNotEmpty();
                        assertThat(productResponse.getName()).isEqualTo(WINDOWS);
                        assertThat(productResponse.getCategory().getId()).isNotEmpty();
                        assertThat(productResponse.getCategory().getName()).isEqualTo(HOME);
                    }
                });
    }

    @Test
    void testDelete() {
        Product productMono = productService.findByName(WINDOWS).block();
        if (Optional.ofNullable(productMono).isPresent()) {
            webTestClient.delete()
                    .uri(DEFAULT_ID, Collections.singletonMap(ID, productMono.getId()))
                    .exchange()
                    .expectStatus().isNoContent()
                    .expectBody()
                    .isEmpty();
        }
    }

    @Test
    void testUpdate() {
        Product productMono = productService.findByName(MOUSE).block();
        Mono<Category> category = categoryService.findByName(DIGIT);
        Product product = Product.builder().price(FIVE).category(category.block()).build();
        if (Optional.ofNullable(productMono).isPresent()) {
            log.info("testUpdate... name: {}", productMono.getName());
            webTestClient.put()
                    .uri(DEFAULT_ID, Collections.singletonMap(ID, productMono.getId()))
                    .bodyValue(product)
                    .exchange()
                    .expectStatus().isCreated()
                    .expectBody(Product.class)
                    .consumeWith(exchangeResult -> {
                        Product productResponse = exchangeResult.getResponseBody();
                        if (Optional.ofNullable(productResponse).isPresent()) {
                            assertThat(productResponse.getId()).isNotEmpty();
                            assertThat(productResponse.getName()).isEqualTo(MOUSE);
                            assertThat(productResponse.getPrice()).isEqualTo(FIVE);
                            assertThat(productResponse.getCategory().getId()).isNotEmpty();
                            assertThat(productResponse.getCategory().getName()).isEqualTo(DIGIT);
                        }
                    });
        }
    }
}