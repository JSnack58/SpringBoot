package com.example.inventoryservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

// Hold the data need service end-goal.
public class InventoryResponse {
    private String skuCode;
    private boolean isInStock;
}
