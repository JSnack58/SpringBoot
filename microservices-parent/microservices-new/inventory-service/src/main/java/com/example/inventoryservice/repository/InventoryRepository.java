package com.example.inventoryservice.repository;

import com.example.inventoryservice.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

// Interface should have the <model class, model's id>
public interface  InventoryRepository extends JpaRepository<Inventory, Long> {

//    Optional<Inventory> findBySkuCode(String skuCode);

    // Searches and return a list of Inventory objects from the
    // skuCodes.
    List<Inventory> findBySkuCodeIn(List<String> skuCodes);
}
