package com.vipunsanjana.order_service.service;

import brave.Span;
import brave.Tracer;
import com.vipunsanjana.order_service.dto.InventoryResponse;
import com.vipunsanjana.order_service.dto.OrderLineItemsDto;
import com.vipunsanjana.order_service.dto.OrderRequest;
import com.vipunsanjana.order_service.event.OrderPlacedEvent;
import com.vipunsanjana.order_service.model.Order;
import com.vipunsanjana.order_service.model.OrderLineItems;
import com.vipunsanjana.order_service.repository.OrderRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;
    private final Tracer tracer;
    private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

    @CircuitBreaker(name = "inventory", fallbackMethod = "fallbackMethod")
    @TimeLimiter(name = "inventory")
    @Retry(name = "inventory")
    public CompletableFuture<String> placeOrder(OrderRequest orderRequest) {
        return CompletableFuture.supplyAsync(() -> {

            Order order = new Order();
            order.setOrderNumber(UUID.randomUUID().toString());

            List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList()
                    .stream()
                    .map(this::mapToEntity)
                    .toList();

            order.setOrderLineItemsList(orderLineItems);

            List<String> skuCodes = order.getOrderLineItemsList().stream()
                    .map(OrderLineItems::getSkuCode)
                    .toList();

            log.info("Calling Inventory Service for SKUs: {}", skuCodes);

            // Brave tracing
            Span inventorySpan = tracer.nextSpan().name("InventoryServiceLookup");
            try (Tracer.SpanInScope ws = tracer.withSpanInScope(inventorySpan.start())) {

                InventoryResponse[] inventoryResponses = webClientBuilder.build()
                        .get()
                        .uri("http://inventory-service/api/inventory",
                                uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                        .retrieve()
                        .bodyToMono(InventoryResponse[].class)
                        .block();

                if (inventoryResponses == null) {
                    throw new IllegalStateException("Inventory service returned null");
                }

                boolean allInStock = Arrays.stream(inventoryResponses)
                        .allMatch(InventoryResponse::isInStock);

                if (allInStock) {
                    orderRepository.save(order);
                    kafkaTemplate.send("notificationTopic", new OrderPlacedEvent(order.getOrderNumber()));
                    log.info("Order {} saved successfully", order.getOrderNumber());
                    return "Order placed successfully! Order Number: " + order.getOrderNumber();
                } else {
                    throw new IllegalArgumentException("Some products are out of stock!");
                }

            } finally {
                inventorySpan.finish(); // Brave uses finish() to end the span
            }
        });
    }

    // Fallback method for Resilience4j
    public CompletableFuture<String> fallbackMethod(OrderRequest orderRequest, Throwable throwable) {
        log.error("Fallback triggered: {}", throwable.getMessage());
        return CompletableFuture.supplyAsync(() ->
                "Oops! Something went wrong. Please try again later.");
    }

    private OrderLineItems mapToEntity(OrderLineItemsDto dto) {
        OrderLineItems entity = new OrderLineItems();
        entity.setPrice(dto.getPrice());
        entity.setQuantity(dto.getQuantity());
        entity.setSkuCode(dto.getSkuCode());
        return entity;
    }
}
