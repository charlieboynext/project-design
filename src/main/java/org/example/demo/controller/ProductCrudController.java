package org.example.demo.controller;

import java.util.List;
import javax.validation.Valid;
import org.example.demo.model.CommonResponse;
import org.example.demo.model.dto.PageResponse;
import org.example.demo.model.dto.ProductRequest;
import org.example.demo.model.entity.Product;
import org.example.demo.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
public class ProductCrudController {

  private final ProductService productService;

  public ProductCrudController(ProductService productService) {
    this.productService = productService;
  }

  @GetMapping
  public CommonResponse list(@RequestParam(defaultValue = "0") int page,
                             @RequestParam(defaultValue = "20") int size,
                             @RequestParam(required = false) String keyword) {
    Pageable pageable = PageRequest.of(page, size);
    Page<Product> result = productService.findPaged(keyword, pageable);
    return CommonResponse.ok(new PageResponse<>(result.getContent(), result.getTotalElements()));
  }

  @GetMapping("/{id}")
  public CommonResponse get(@PathVariable Long id) {
    return CommonResponse.ok(productService.findById(id));
  }

  @PostMapping
  public CommonResponse create(@Valid @RequestBody ProductRequest request) {
    return CommonResponse.ok(productService.create(request));
  }

  @PutMapping("/{id}")
  public CommonResponse update(@PathVariable Long id, @Valid @RequestBody ProductRequest request) {
    return CommonResponse.ok(productService.update(id, request));
  }

  @PostMapping("/{id}/delete")
  public CommonResponse delete(@PathVariable Long id) {
    productService.delete(id);
    return CommonResponse.ok(null);
  }
}
