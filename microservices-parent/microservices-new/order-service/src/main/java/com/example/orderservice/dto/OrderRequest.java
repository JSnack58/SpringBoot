package com.example.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class OrderRequest {
    // When testing request of Postman use name of this
    // variable in the JSON variable name.
    private List<OrderLineItemsDto> orderLineItemsDtoList;

}
