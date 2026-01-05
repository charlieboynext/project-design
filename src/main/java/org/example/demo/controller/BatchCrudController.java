package org.example.demo.controller;

import org.example.demo.model.CommonResponse;
import org.example.demo.model.dto.BatchRequest;
import org.example.demo.model.dto.PageResponse;
import org.example.demo.model.entity.Batch;
import org.example.demo.service.BatchService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/batches")
public class BatchCrudController {

  private final BatchService batchService;

  public BatchCrudController(BatchService batchService) {
    this.batchService = batchService;
  }

  @GetMapping
  public CommonResponse list(@RequestParam(defaultValue = "0") int page,
                             @RequestParam(defaultValue = "20") int size,
                             @RequestParam(required = false) String keyword) {
    Pageable pageable = PageRequest.of(page, size);
    Page<Batch> result = batchService.findPaged(keyword, pageable);
    return CommonResponse.ok(new PageResponse<>(result.getContent(), result.getTotalElements()));
  }

  @GetMapping("/{id}")
  public CommonResponse get(@PathVariable Long id) {
    return CommonResponse.ok(batchService.findById(id));
  }

  @PostMapping
  public CommonResponse create(@Valid @RequestBody BatchRequest request) {
    return CommonResponse.ok(batchService.create(request));
  }

  @PutMapping("/{id}")
  public CommonResponse update(@PathVariable Long id, @Valid @RequestBody BatchRequest request) {
    return CommonResponse.ok(batchService.update(id, request));
  }

  @DeleteMapping("/{id}")
  public CommonResponse delete(@PathVariable Long id) {
    batchService.delete(id);
    return CommonResponse.ok(null);
  }
}
