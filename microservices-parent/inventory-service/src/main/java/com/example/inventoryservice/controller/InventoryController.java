package com.example.inventoryservice.controller;

import com.example.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {
    private final InventoryService inventoryService;
    // Allow the sku-code to put in the path to be used as a variable.
    // Ex. "/{variable}".
    @GetMapping("/{sku-code}")
    @ResponseStatus(HttpStatus.OK)
    // PathVariable takes the value from the variable with the corresponding name.
    public boolean isInStock(@PathVariable("sku-code") String skuCode) {
        return inventoryService.isInStock(skuCode);
    }

}
