package com.example.orderservice.service;

import com.example.orderservice.dto.InventoryResponse;
import com.example.orderservice.dto.OrderLineItemsDto;
import com.example.orderservice.dto.OrderRequest;
import com.example.orderservice.model.Order;
import com.example.orderservice.model.OrderLineItems;
import com.example.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    // Provides access to the database.
    private final OrderRepository orderRepository;

    private final WebClient webClient;
    // Create an Order from an OrderRequest and save to teh database.
    public void placeOrder(OrderRequest orderRequest){
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList()
                .stream()
                .map(this::mapToDto)
                .toList();
        order.setOrderLineItemsList(orderLineItems);

        List<String> skuCodes = order.getOrderLineItemsList().stream()
                .map(OrderLineItems::getSkuCode)
                .toList();
        // Call Inventory Service, and place order if product is in stock.
        // The Inventory's Get aka checks the uri for a skuCode but
        // the Order contains OrderLineItems each with their own skuCode.
        InventoryResponse[] inventoryResponseArray = webClient.get()
                .uri("http://localhost:8082/api/inventory",
                        uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build() )
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();

        // Determine whether to save the order or throw an error based on
        // if all InventoryResponses are in-stock.
        boolean AllProductsInStock = Arrays.stream(inventoryResponseArray).allMatch(InventoryResponse::isInStock);
        if (AllProductsInStock){
            orderRepository.save(order);
        } else {
            // If there are products out of stock, state which products.
            List<String> outOfStockProducts = Arrays.stream(inventoryResponseArray)
                    .filter(inventoryResponse -> !inventoryResponse.isInStock())
                    .map(InventoryResponse::getSkuCode).toList();
            String outOfStockProductString = outOfStockProducts.toString();
            if (outOfStockProducts.size() > 1){
                throw new IllegalArgumentException("Products: "+ outOfStockProductString + ", are not in stock, please try again later");
            }
            throw new IllegalArgumentException("Product: "+ outOfStockProductString +"  is not in stock, please try again later");
        }
    }
    // Creates a OrderLineItems from an OrderLineItemsDto 's data.
    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto){
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        return orderLineItems;
    }

}
