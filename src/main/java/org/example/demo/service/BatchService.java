package org.example.demo.service;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.example.demo.model.dto.BatchRequest;
import org.example.demo.model.entity.Batch;
import org.example.demo.model.entity.BatchStatus;
import org.example.demo.model.entity.Product;
import org.example.demo.repository.BatchRepository;
import org.example.demo.repository.ProductRepository;
import org.fisco.bcos.sdk.transaction.model.dto.TransactionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BatchService {

  private final BatchRepository batchRepository;
  private final ProductRepository productRepository;
  private final TraceabilityContractService contractService;

  @Autowired(required = false)
  public BatchService(BatchRepository batchRepository, 
                      ProductRepository productRepository,
                      TraceabilityContractService contractService) {
    this.batchRepository = batchRepository;
    this.productRepository = productRepository;
    this.contractService = contractService;
  }

  public List<Batch> findAll() {
    refreshExpiredBatches();
    return batchRepository.findAll();
  }

  public Page<Batch> findPaged(String keyword, Pageable pageable) {
    refreshExpiredBatches();
    if (keyword == null || keyword.isEmpty()) {
      return batchRepository.findAll(pageable);
    }
    return batchRepository.findByBatchNoContainingIgnoreCase(keyword, pageable);
  }

  public Batch findById(Long id) {
    refreshExpiredBatches();
    return batchRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Batch not found"));
  }

  @Transactional
  public Batch create(BatchRequest request) {
    Product product = productRepository.findById(request.getProductId()).orElseThrow(() -> new IllegalArgumentException("Product not found"));
    Batch batch = new Batch();
    batch.setProduct(product);
    apply(batch, request);
    Batch saved = batchRepository.save(batch);

    // 同步到区块链 - 使用 batchNo 作为 traceCode
    if (contractService != null && contractService.isAvailable() && saved.getBatchNo() != null) {
      try {
        String traceCode = saved.getBatchNo();
        String shipDateStr = saved.getManufactureDate() != null 
            ? saved.getManufactureDate().format(DateTimeFormatter.ISO_LOCAL_DATE) 
            : "";
        BigInteger quantity = saved.getQuantity() != null 
            ? BigInteger.valueOf(saved.getQuantity()) 
            : BigInteger.ZERO;
        
        TransactionResponse txResponse = contractService.addInventory(
            traceCode,
            quantity,
            shipDateStr,
            product.getOrigin() != null ? product.getOrigin() : "",
            product.getName(),
            saved.getFactory() != null ? saved.getFactory() : ""
        );
        // 可以保存 txHash 如果需要
      } catch (Exception e) {
        // 记录错误但不阻止保存
        System.err.println("Failed to sync batch to blockchain: " + e.getMessage());
      }
    }

    return saved;
  }

  @Transactional
  public Batch update(Long id, BatchRequest request) {
    Batch batch = findById(id);
    Product product = productRepository.findById(request.getProductId()).orElseThrow(() -> new IllegalArgumentException("Product not found"));
    batch.setProduct(product);
    apply(batch, request);
    Batch saved = batchRepository.save(batch);

    // 同步到区块链 - 使用 batchNo 作为 traceCode
    if (contractService != null && contractService.isAvailable() && saved.getBatchNo() != null) {
      try {
        String traceCode = saved.getBatchNo();
        String shipDateStr = saved.getManufactureDate() != null 
            ? saved.getManufactureDate().format(DateTimeFormatter.ISO_LOCAL_DATE) 
            : "";
        BigInteger quantity = saved.getQuantity() != null 
            ? BigInteger.valueOf(saved.getQuantity()) 
            : BigInteger.ZERO;
        
        TransactionResponse txResponse = contractService.addInventory(
            traceCode,
            quantity,
            shipDateStr,
            product.getOrigin() != null ? product.getOrigin() : "",
            product.getName(),
            saved.getFactory() != null ? saved.getFactory() : ""
        );
        // 可以保存 txHash 如果需要
      } catch (Exception e) {
        // 记录错误但不阻止保存
        System.err.println("Failed to sync batch update to blockchain: " + e.getMessage());
      }
    }

    return saved;
  }

  public void delete(Long id) {
    batchRepository.deleteById(id);
  }

  private void refreshExpiredBatches() {
    batchRepository.markExpired(LocalDate.now());
  }

  private void apply(Batch batch, BatchRequest request) {
    batch.setBatchNo(request.getBatchNo());
    batch.setManufactureDate(request.getManufactureDate());
    batch.setExpireDate(request.getExpireDate());
    batch.setQuantity(request.getQuantity());
    batch.setUnit(request.getUnit());
    batch.setFactory(request.getFactory());
    if (request.getStatus() != null) {
      batch.setStatus(BatchStatus.valueOf(request.getStatus().toUpperCase()));
    }
  }
}
