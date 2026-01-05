package org.example.demo.service;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.transaction.manager.AssembleTransactionProcessor;
import org.fisco.bcos.sdk.transaction.manager.TransactionProcessorFactory;
import org.fisco.bcos.sdk.transaction.model.dto.CallResponse;
import org.fisco.bcos.sdk.transaction.model.dto.TransactionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.example.demo.utils.IOUtil;

@Service
@ConditionalOnBean(Client.class)
public class TraceabilityContractService {

  private static final String ABI = IOUtil.readResourceAsString("abi/TraceabilityFull.abi");

  @Value("${system.contract.traceabilityAddress:}")
  private String contractAddress;

  @Autowired
  private Client client;

  private AssembleTransactionProcessor txProcessor;

  @PostConstruct
  public void init() throws Exception {
    if (client != null && isContractConfigured()) {
      this.txProcessor = TransactionProcessorFactory.createAssembleTransactionProcessor(
          this.client, this.client.getCryptoSuite().getCryptoKeyPair());
    }
  }

  // ============ 产品相关方法 ============

  public TransactionResponse addProduct(String traceCode, String name, String origin, String manufacturer) throws Exception {
    checkContract();
    return txProcessor.sendTransactionAndGetResponse(
        contractAddress, ABI, "addProduct",
        Arrays.asList(traceCode, name, origin, manufacturer));
  }

  public TransactionResponse updateProduct(String traceCode, String name, String origin, String manufacturer) throws Exception {
    checkContract();
    return txProcessor.sendTransactionAndGetResponse(
        contractAddress, ABI, "updateProduct",
        Arrays.asList(traceCode, name, origin, manufacturer));
  }

  public CallResponse getProduct(String traceCode) throws Exception {
    checkContract();
    return txProcessor.sendCall(
        client.getCryptoSuite().getCryptoKeyPair().getAddress(),
        contractAddress, ABI, "getProduct", Arrays.asList(traceCode));
  }

  public CallResponse getAllProducts() throws Exception {
    checkContract();
    return txProcessor.sendCall(
        client.getCryptoSuite().getCryptoKeyPair().getAddress(),
        contractAddress, ABI, "getAllProducts", Arrays.asList());
  }

  // ============ 库存相关方法 ============

  public TransactionResponse addInventory(String traceCode, BigInteger shipQuantity, String shipDate,
                                          String origin, String productName, String receiverAddress) throws Exception {
    checkContract();
    return txProcessor.sendTransactionAndGetResponse(
        contractAddress, ABI, "addInventory",
        Arrays.asList(traceCode, shipQuantity, shipDate, origin, productName, receiverAddress));
  }

  public CallResponse getInventory(String traceCode) throws Exception {
    checkContract();
    return txProcessor.sendCall(
        client.getCryptoSuite().getCryptoKeyPair().getAddress(),
        contractAddress, ABI, "getInventory", Arrays.asList(traceCode));
  }

  // ============ 商品相关方法 ============

  public TransactionResponse addGoods(String traceCode, String productName, String origin,
                                      BigInteger quantity, BigInteger price,
                                      String listDate, String shipDate, String receiveDate) throws Exception {
    checkContract();
    return txProcessor.sendTransactionAndGetResponse(
        contractAddress, ABI, "addGoods",
        Arrays.asList(traceCode, productName, origin, quantity, price, listDate, shipDate, receiveDate));
  }

  public CallResponse getGoods(String traceCode) throws Exception {
    checkContract();
    return txProcessor.sendCall(
        client.getCryptoSuite().getCryptoKeyPair().getAddress(),
        contractAddress, ABI, "getGoods", Arrays.asList(traceCode));
  }

  // ============ 工具方法 ============

  private void checkContract() {
    if (!isContractConfigured()) {
      throw new IllegalStateException("TraceabilityFull contract not configured. Please set system.contract.traceabilityAddress");
    }
  }

  private boolean isContractConfigured() {
    if (client == null) {
      return false;
    }
    if (!StringUtils.hasText(contractAddress)) {
      return false;
    }
    String normalized = contractAddress.toLowerCase();
    if (normalized.startsWith("0x")) {
      normalized = normalized.substring(2);
    }
    return normalized.replace("0", "").length() > 0;
  }

  public boolean isAvailable() {
    return isContractConfigured() && txProcessor != null;
  }

  public String getContractAddress() {
    return contractAddress;
  }
}

