package org.example.demo.service;

import java.util.List;
import org.example.demo.model.dto.ProductRequest;
import org.example.demo.model.entity.Product;
import org.example.demo.repository.ProductRepository;
import org.fisco.bcos.sdk.transaction.model.dto.TransactionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

  private final ProductRepository productRepository;
  private final TraceabilityContractService contractService;

  @Autowired(required = false)
  public ProductService(ProductRepository productRepository, TraceabilityContractService contractService) {
    this.productRepository = productRepository;
    this.contractService = contractService;
  }

  public List<Product> findAll() {
    return productRepository.findAll();
  }

  public Page<Product> findPaged(String keyword, Pageable pageable) {
    if (keyword == null || keyword.isEmpty()) {
      return productRepository.findAll(pageable);
    }
    return productRepository.findByNameContainingIgnoreCaseOrBrandContainingIgnoreCase(keyword, keyword, pageable);
  }

  public Product findById(Long id) {
    return productRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Product not found"));
  }

  @Transactional
  public Product create(ProductRequest request) {
    Product product = new Product();
    apply(product, request);
    Product saved = productRepository.save(product);

    // 同步到区块链
    if (contractService != null && contractService.isAvailable() && saved.getProductCode() != null) {
      try {
        String traceCode = saved.getProductCode();
        String manufacturer = saved.getFactory() != null ? saved.getFactory() : "";
        TransactionResponse txResponse = contractService.addProduct(
            traceCode,
            saved.getName(),
            saved.getOrigin() != null ? saved.getOrigin() : "",
            manufacturer
        );
        // 可以保存 txHash 如果需要
      } catch (Exception e) {
        // 记录错误但不阻止保存
        System.err.println("Failed to sync product to blockchain: " + e.getMessage());
      }
    }

    return saved;
  }

  @Transactional
  public Product update(Long id, ProductRequest request) {
    Product product = findById(id);
    apply(product, request);
    Product saved = productRepository.save(product);

    // 同步到区块链
    if (contractService != null && contractService.isAvailable() && saved.getProductCode() != null) {
      try {
        String traceCode = saved.getProductCode();
        String manufacturer = saved.getFactory() != null ? saved.getFactory() : "";
        TransactionResponse txResponse = contractService.updateProduct(
            traceCode,
            saved.getName(),
            saved.getOrigin() != null ? saved.getOrigin() : "",
            manufacturer
        );
        // 可以保存 txHash 如果需要
      } catch (Exception e) {
        // 记录错误但不阻止保存
        System.err.println("Failed to sync product update to blockchain: " + e.getMessage());
      }
    }

    return saved;
  }

  public void delete(Long id) {
    productRepository.deleteById(id);
  }

  private void apply(Product product, ProductRequest request) {
    product.setName(request.getName());
    product.setBrand(request.getBrand());
    product.setCategory(request.getCategory());
    product.setSpec(request.getSpec());
    product.setOrigin(request.getOrigin());
    product.setProductCode(request.getProductCode());
    product.setFactory(request.getFactory());
  }
}
