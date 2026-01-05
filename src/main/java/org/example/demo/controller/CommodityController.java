package org.example.demo.controller;

import javax.validation.Valid;
import org.example.demo.model.CommonResponse;
import org.example.demo.model.dto.CommodityRequest;
import org.example.demo.model.dto.PageResponse;
import org.example.demo.model.entity.Commodity;
import org.example.demo.service.CommodityService;
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

@RestController
@RequestMapping("/api/commodities")
public class CommodityController {

  private final CommodityService commodityService;

  public CommodityController(CommodityService commodityService) {
    this.commodityService = commodityService;
  }

  @GetMapping
  public CommonResponse list(@RequestParam(defaultValue = "0") int page,
                             @RequestParam(defaultValue = "20") int size,
                             @RequestParam(required = false) String keyword) {
    Pageable pageable = PageRequest.of(page, size);
    Page<Commodity> result = commodityService.findPaged(keyword, pageable);
    return CommonResponse.ok(new PageResponse<>(result.getContent(), result.getTotalElements()));
  }

  @GetMapping("/{id}")
  public CommonResponse get(@PathVariable Long id) {
    return CommonResponse.ok(commodityService.findById(id));
  }

  @PostMapping
  public CommonResponse create(@Valid @RequestBody CommodityRequest request) {
    return CommonResponse.ok(commodityService.create(request));
  }

  @PutMapping("/{id}")
  public CommonResponse update(@PathVariable Long id, @Valid @RequestBody CommodityRequest request) {
    return CommonResponse.ok(commodityService.update(id, request));
  }

  @DeleteMapping("/{id}")
  public CommonResponse delete(@PathVariable Long id) {
    commodityService.delete(id);
    return CommonResponse.ok(null);
  }
}

