package org.example.demo.service;

import java.math.BigInteger;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.example.demo.model.dto.InventoryRequest;
import org.example.demo.model.entity.Batch;
import org.example.demo.model.entity.Inventory;
import org.example.demo.model.entity.Product;
import org.example.demo.repository.BatchRepository;
import org.example.demo.repository.InventoryRepository;
import org.example.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InventoryService {

  private final InventoryRepository inventoryRepository;
  private final ProductRepository productRepository;
  private final BatchRepository batchRepository;
  private final TraceabilityContractService contractService;

  @Autowired(required = false)
  public InventoryService(InventoryRepository inventoryRepository,
                          ProductRepository productRepository,
                          BatchRepository batchRepository,
                          TraceabilityContractService contractService) {
    this.inventoryRepository = inventoryRepository;
    this.productRepository = productRepository;
    this.batchRepository = batchRepository;
    this.contractService = contractService;
  }

  public List<Inventory> findAll() {
    return inventoryRepository.findAll();
  }

  public Page<Inventory> findPaged(String keyword, Pageable pageable) {
    if (keyword == null || keyword.isEmpty()) {
      return inventoryRepository.findAll(pageable);
    }
    return inventoryRepository.findByTraceCodeContainingIgnoreCaseOrProductNameContainingIgnoreCase(
        keyword, keyword, pageable);
  }

  public Inventory findById(Long id) {
    return inventoryRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Inventory not found"));
  }

  public Inventory findByTraceCode(String traceCode) {
    return inventoryRepository.findByTraceCode(traceCode)
        .orElseThrow(() -> new IllegalArgumentException("Inventory not found"));
  }

  @Transactional
  public Inventory create(InventoryRequest request) {
    Product product = productRepository.findById(request.getProductId())
        .orElseThrow(() -> new IllegalArgumentException("Product not found"));
    
    Batch batch = null;
    if (request.getBatchId() != null) {
      batch = batchRepository.findById(request.getBatchId())
          .orElseThrow(() -> new IllegalArgumentException("Batch not found"));
    }

    Inventory inventory = new Inventory();
    inventory.setTraceCode(request.getTraceCode());
    inventory.setProduct(product);
    inventory.setBatch(batch);
    inventory.setShipmentQuantity(request.getShipmentQuantity());
    inventory.setShipmentDate(request.getShipmentDate());
    inventory.setOrigin(request.getOrigin() != null ? request.getOrigin() : product.getOrigin());
    inventory.setProductName(request.getProductName() != null ? request.getProductName() : product.getName());
    inventory.setDeliveryAddress(request.getDeliveryAddress());

    Inventory saved = inventoryRepository.save(inventory);

    // 同步到区块链
    if (contractService != null && contractService.isAvailable()) {
      try {
        String shipDateStr = request.getShipmentDate() != null 
            ? request.getShipmentDate().format(DateTimeFormatter.ISO_LOCAL_DATE) 
            : "";
        contractService.addInventory(
            request.getTraceCode(),
            BigInteger.valueOf(request.getShipmentQuantity()),
            shipDateStr,
            inventory.getOrigin(),
            inventory.getProductName(),
            request.getDeliveryAddress() != null ? request.getDeliveryAddress() : ""
        );
        // 区块链交易已提交
      } catch (Exception e) {
        // 记录错误但不阻止保存
        System.err.println("Failed to sync inventory to blockchain: " + e.getMessage());
      }
    }

    return saved;
  }

  @Transactional
  public Inventory update(Long id, InventoryRequest request) {
    Inventory inventory = findById(id);
    Product product = productRepository.findById(request.getProductId())
        .orElseThrow(() -> new IllegalArgumentException("Product not found"));
    
    if (request.getBatchId() != null) {
      Batch batch = batchRepository.findById(request.getBatchId())
          .orElseThrow(() -> new IllegalArgumentException("Batch not found"));
      inventory.setBatch(batch);
    }

    inventory.setTraceCode(request.getTraceCode());
    inventory.setProduct(product);
    inventory.setShipmentQuantity(request.getShipmentQuantity());
    inventory.setShipmentDate(request.getShipmentDate());
    inventory.setOrigin(request.getOrigin() != null ? request.getOrigin() : product.getOrigin());
    inventory.setProductName(request.getProductName() != null ? request.getProductName() : product.getName());
    inventory.setDeliveryAddress(request.getDeliveryAddress());

    Inventory saved = inventoryRepository.save(inventory);

    // 同步到区块链
    if (contractService != null && contractService.isAvailable()) {
      try {
        String shipDateStr = request.getShipmentDate() != null 
            ? request.getShipmentDate().format(DateTimeFormatter.ISO_LOCAL_DATE) 
            : "";
        contractService.addInventory(
            request.getTraceCode(),
            BigInteger.valueOf(request.getShipmentQuantity()),
            shipDateStr,
            saved.getOrigin(),
            saved.getProductName(),
            request.getDeliveryAddress() != null ? request.getDeliveryAddress() : ""
        );
        // 区块链交易已提交
      } catch (Exception e) {
        // 记录错误但不阻止保存
        System.err.println("Failed to sync inventory update to blockchain: " + e.getMessage());
      }
    }

    return saved;
  }

  public void delete(Long id) {
    inventoryRepository.deleteById(id);
  }
}

