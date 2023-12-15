package com.example.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
// This file was copied over from the inventory-service project
// because you can't import class from other projects.
public class InventoryResponse {
    private String skuCode;
    private boolean isInStock;
}
