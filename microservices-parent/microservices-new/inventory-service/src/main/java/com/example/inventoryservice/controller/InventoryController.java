package com.example.inventoryservice.controller;

import com.example.inventoryservice.dto.InventoryResponse;
import com.example.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {
    private final InventoryService inventoryService;
    // Originally, there was one skuCode in the uri:
    // http://localhost:8082/api/inventory/{skuCode}, meaning
    // @PathVariable could be used to retrieve the skuCode
    //    public boolean isInStock(@PathVariable("sku-code") String skuCode)
    // but this doesn't work for multiple skuCodes.
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    // Orders have a list of OrderLineItems each with their own skuCode, so list
    // of codes in the uri is:
    // http://localhost:8082/api/inventory?skuCode={skuCode}&skuCode={skuCode}&skuCode={skuCode},
    // @RequestParam will gather the skuCodes into list.
    public List<InventoryResponse> isInStock(@RequestParam List<String> skuCode) {
        return inventoryService.isInStock(skuCode);
    }
}
