package org.example.demo.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.example.demo.model.dto.CommodityRequest;
import org.example.demo.model.entity.Batch;
import org.example.demo.model.entity.Commodity;
import org.example.demo.model.entity.Product;
import org.example.demo.repository.BatchRepository;
import org.example.demo.repository.CommodityRepository;
import org.example.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommodityService {

  private final CommodityRepository commodityRepository;
  private final ProductRepository productRepository;
  private final BatchRepository batchRepository;
  private final TraceabilityContractService contractService;

  @Autowired(required = false)
  public CommodityService(CommodityRepository commodityRepository,
                          ProductRepository productRepository,
                          BatchRepository batchRepository,
                          TraceabilityContractService contractService) {
    this.commodityRepository = commodityRepository;
    this.productRepository = productRepository;
    this.batchRepository = batchRepository;
    this.contractService = contractService;
  }

  public List<Commodity> findAll() {
    return commodityRepository.findAll();
  }

  public Page<Commodity> findPaged(String keyword, Pageable pageable) {
    if (keyword == null || keyword.isEmpty()) {
      return commodityRepository.findAll(pageable);
    }
    return commodityRepository.findByTraceCodeContainingIgnoreCaseOrProductNameContainingIgnoreCase(
        keyword, keyword, pageable);
  }

  public Commodity findById(Long id) {
    return commodityRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Commodity not found"));
  }

  public Commodity findByTraceCode(String traceCode) {
    return commodityRepository.findByTraceCode(traceCode)
        .orElseThrow(() -> new IllegalArgumentException("Commodity not found"));
  }

  @Transactional
  public Commodity create(CommodityRequest request) {
    Product product = productRepository.findById(request.getProductId())
        .orElseThrow(() -> new IllegalArgumentException("Product not found"));
    
    Batch batch = null;
    if (request.getBatchId() != null) {
      batch = batchRepository.findById(request.getBatchId())
          .orElseThrow(() -> new IllegalArgumentException("Batch not found"));
    }

    Commodity commodity = new Commodity();
    commodity.setTraceCode(request.getTraceCode());
    commodity.setProduct(product);
    commodity.setBatch(batch);
    commodity.setProductName(request.getProductName() != null ? request.getProductName() : product.getName());
    commodity.setOrigin(request.getOrigin() != null ? request.getOrigin() : product.getOrigin());
    commodity.setQuantity(request.getQuantity());
    commodity.setListingPrice(request.getListingPrice());
    commodity.setListingDate(request.getListingDate());
    commodity.setShipmentDate(request.getShipmentDate());
    commodity.setReceiptDate(request.getReceiptDate());

    Commodity saved = commodityRepository.save(commodity);

    // 同步到区块链
    if (contractService != null && contractService.isAvailable()) {
      try {
        String listDateStr = request.getListingDate() != null 
            ? request.getListingDate().format(DateTimeFormatter.ISO_LOCAL_DATE) 
            : "";
        String shipDateStr = request.getShipmentDate() != null 
            ? request.getShipmentDate().format(DateTimeFormatter.ISO_LOCAL_DATE) 
            : "";
        String receiveDateStr = request.getReceiptDate() != null 
            ? request.getReceiptDate().format(DateTimeFormatter.ISO_LOCAL_DATE) 
            : "";
        
        BigInteger price = request.getListingPrice() != null 
            ? request.getListingPrice().multiply(BigDecimal.valueOf(100)).toBigInteger() 
            : BigInteger.ZERO;
        
        contractService.addGoods(
            request.getTraceCode(),
            commodity.getProductName(),
            commodity.getOrigin(),
            BigInteger.valueOf(request.getQuantity()),
            price,
            listDateStr,
            shipDateStr,
            receiveDateStr
        );
        // 区块链交易已提交
      } catch (Exception e) {
        // 记录错误但不阻止保存
        System.err.println("Failed to sync commodity to blockchain: " + e.getMessage());
      }
    }

    return saved;
  }

  @Transactional
  public Commodity update(Long id, CommodityRequest request) {
    Commodity commodity = findById(id);
    Product product = productRepository.findById(request.getProductId())
        .orElseThrow(() -> new IllegalArgumentException("Product not found"));
    
    if (request.getBatchId() != null) {
      Batch batch = batchRepository.findById(request.getBatchId())
          .orElseThrow(() -> new IllegalArgumentException("Batch not found"));
      commodity.setBatch(batch);
    }

    commodity.setTraceCode(request.getTraceCode());
    commodity.setProduct(product);
    commodity.setProductName(request.getProductName() != null ? request.getProductName() : product.getName());
    commodity.setOrigin(request.getOrigin() != null ? request.getOrigin() : product.getOrigin());
    commodity.setQuantity(request.getQuantity());
    commodity.setListingPrice(request.getListingPrice());
    commodity.setListingDate(request.getListingDate());
    commodity.setShipmentDate(request.getShipmentDate());
    commodity.setReceiptDate(request.getReceiptDate());

    Commodity saved = commodityRepository.save(commodity);

    // 同步到区块链
    if (contractService != null && contractService.isAvailable()) {
      try {
        String listDateStr = request.getListingDate() != null 
            ? request.getListingDate().format(DateTimeFormatter.ISO_LOCAL_DATE) 
            : "";
        String shipDateStr = request.getShipmentDate() != null 
            ? request.getShipmentDate().format(DateTimeFormatter.ISO_LOCAL_DATE) 
            : "";
        String receiveDateStr = request.getReceiptDate() != null 
            ? request.getReceiptDate().format(DateTimeFormatter.ISO_LOCAL_DATE) 
            : "";
        
        BigInteger price = request.getListingPrice() != null 
            ? request.getListingPrice().multiply(BigDecimal.valueOf(100)).toBigInteger() 
            : BigInteger.ZERO;
        
        contractService.addGoods(
            request.getTraceCode(),
            saved.getProductName(),
            saved.getOrigin(),
            BigInteger.valueOf(request.getQuantity()),
            price,
            listDateStr,
            shipDateStr,
            receiveDateStr
        );
        // 区块链交易已提交
      } catch (Exception e) {
        // 记录错误但不阻止保存
        System.err.println("Failed to sync commodity update to blockchain: " + e.getMessage());
      }
    }

    return saved;
  }

  public void delete(Long id) {
    commodityRepository.deleteById(id);
  }
}

