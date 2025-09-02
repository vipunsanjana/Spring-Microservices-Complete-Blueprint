package com.vipunsanjana.inventory_service.service;

import com.vipunsanjana.inventory_service.dto.InventoryResponse;
import com.vipunsanjana.inventory_service.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    @Transactional(readOnly = true)
    public List<InventoryResponse> isInStock(List<String> skuCode) {
        log.info("Checking Inventory");

        try {
            Thread.sleep(10000); // simulate delay
        } catch (InterruptedException e) {
            log.error("Thread was interrupted while checking inventory", e);
            Thread.currentThread().interrupt(); // restore interrupt flag
        }

        log.info("Checking Ended");

        return inventoryRepository.findBySkuCodeIn(skuCode).stream()
                .map(inventory ->
                        InventoryResponse.builder()
                                .skuCode(inventory.getSkuCode())
                                .isInStock(inventory.getQuantity() > 0)
                                .build()
                ).toList();
    }
}
