package org.example.demo.controller;

import java.math.BigInteger;
import org.example.demo.model.CommonResponse;
import org.example.demo.service.TraceabilityContractService;
import org.fisco.bcos.sdk.transaction.model.dto.CallResponse;
import org.fisco.bcos.sdk.transaction.model.dto.TransactionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/contract/test")
@PreAuthorize("hasRole('ADMIN')")
public class ContractTestController {

  @Autowired(required = false)
  private TraceabilityContractService contractService;

  @GetMapping("/status")
  public CommonResponse getStatus() {
    if (contractService == null) {
      return CommonResponse.fail("SERVICE_UNAVAILABLE", new IllegalStateException("TraceabilityContractService not available"));
    }
    boolean available = contractService.isAvailable();
    String address = contractService.getContractAddress();
    return CommonResponse.ok(new StatusInfo(available, address));
  }

  @GetMapping("/product")
  public CommonResponse getProduct(@RequestParam String traceCode) {
    try {
      if (contractService == null || !contractService.isAvailable()) {
        return CommonResponse.fail("SERVICE_UNAVAILABLE", new IllegalStateException("Contract service not available"));
      }
      CallResponse response = contractService.getProduct(traceCode);
      return CommonResponse.ok(response.getValues());
    } catch (Exception e) {
      return CommonResponse.fail("GET_PRODUCT_FAILED", e);
    }
  }

  @GetMapping("/products")
  public CommonResponse getAllProducts() {
    try {
      if (contractService == null || !contractService.isAvailable()) {
        return CommonResponse.fail("SERVICE_UNAVAILABLE", new IllegalStateException("Contract service not available"));
      }
      CallResponse response = contractService.getAllProducts();
      return CommonResponse.ok(response.getValues());
    } catch (Exception e) {
      return CommonResponse.fail("GET_PRODUCTS_FAILED", e);
    }
  }

  @PostMapping("/product")
  public CommonResponse addProduct(
      @RequestParam String traceCode,
      @RequestParam String name,
      @RequestParam String origin,
      @RequestParam String manufacturer) {
    try {
      if (contractService == null || !contractService.isAvailable()) {
        return CommonResponse.fail("SERVICE_UNAVAILABLE", new IllegalStateException("Contract service not available"));
      }
      TransactionResponse response = contractService.addProduct(traceCode, name, origin, manufacturer);
      return CommonResponse.ok(new TransactionResult(
          response.getTransactionReceipt().getTransactionHash(),
          response.getTransactionReceipt().getBlockNumber().toString()));
    } catch (Exception e) {
      return CommonResponse.fail("ADD_PRODUCT_FAILED", e);
    }
  }

  @PostMapping("/inventory")
  public CommonResponse addInventory(
      @RequestParam String traceCode,
      @RequestParam Integer shipQuantity,
      @RequestParam String shipDate,
      @RequestParam String origin,
      @RequestParam String productName,
      @RequestParam String receiverAddress) {
    try {
      if (contractService == null || !contractService.isAvailable()) {
        return CommonResponse.fail("SERVICE_UNAVAILABLE", new IllegalStateException("Contract service not available"));
      }
      TransactionResponse response = contractService.addInventory(
          traceCode, BigInteger.valueOf(shipQuantity), shipDate, origin, productName, receiverAddress);
      return CommonResponse.ok(new TransactionResult(
          response.getTransactionReceipt().getTransactionHash(),
          response.getTransactionReceipt().getBlockNumber().toString()));
    } catch (Exception e) {
      return CommonResponse.fail("ADD_INVENTORY_FAILED", e);
    }
  }

  @PostMapping("/goods")
  public CommonResponse addGoods(
      @RequestParam String traceCode,
      @RequestParam String productName,
      @RequestParam String origin,
      @RequestParam Integer quantity,
      @RequestParam Long price,
      @RequestParam String listDate,
      @RequestParam String shipDate,
      @RequestParam String receiveDate) {
    try {
      if (contractService == null || !contractService.isAvailable()) {
        return CommonResponse.fail("SERVICE_UNAVAILABLE", new IllegalStateException("Contract service not available"));
      }
      TransactionResponse response = contractService.addGoods(
          traceCode, productName, origin, BigInteger.valueOf(quantity),
          BigInteger.valueOf(price), listDate, shipDate, receiveDate);
      return CommonResponse.ok(new TransactionResult(
          response.getTransactionReceipt().getTransactionHash(),
          response.getTransactionReceipt().getBlockNumber().toString()));
    } catch (Exception e) {
      return CommonResponse.fail("ADD_GOODS_FAILED", e);
    }
  }

  public static class StatusInfo {
    private boolean available;
    private String contractAddress;

    public StatusInfo(boolean available, String contractAddress) {
      this.available = available;
      this.contractAddress = contractAddress;
    }

    public boolean isAvailable() { return available; }
    public String getContractAddress() { return contractAddress; }
  }

  public static class TransactionResult {
    private String txHash;
    private String blockNumber;

    public TransactionResult(String txHash, String blockNumber) {
      this.txHash = txHash;
      this.blockNumber = blockNumber;
    }

    public String getTxHash() { return txHash; }
    public String getBlockNumber() { return blockNumber; }
  }
}

