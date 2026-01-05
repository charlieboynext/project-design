package org.example.demo.service;

import java.lang.Exception;
import java.util.Arrays;
import javax.annotation.PostConstruct;
import org.example.demo.model.bo.AssetBalancesInputBO;
import org.example.demo.model.bo.AssetIssueInputBO;
import org.example.demo.model.bo.AssetSendInputBO;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.transaction.manager.AssembleTransactionProcessor;
import org.fisco.bcos.sdk.transaction.manager.TransactionProcessorFactory;
import org.fisco.bcos.sdk.transaction.model.dto.CallResponse;
import org.fisco.bcos.sdk.transaction.model.dto.TransactionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnBean(Client.class)
public class AssetService {
  public static final String ABI = org.example.demo.utils.IOUtil.readResourceAsString("abi/Asset.abi");
  public static final String BINARY = org.example.demo.utils.IOUtil.readResourceAsString("bin/ecc/Asset.bin");
  public static final String SM_BINARY = org.example.demo.utils.IOUtil.readResourceAsString("bin/sm/Asset.bin");

  @Value("${system.contract.assetAddress}")
  private String address;

  @Autowired
  private Client client;

  private AssembleTransactionProcessor txProcessor;

  @PostConstruct
  public void init() throws Exception {
    this.txProcessor = TransactionProcessorFactory.createAssembleTransactionProcessor(this.client, this.client.getCryptoSuite().getCryptoKeyPair());
  }

  public TransactionResponse issue(AssetIssueInputBO input) throws Exception {
    return this.txProcessor.sendTransactionAndGetResponse(this.address, ABI, "issue", input.toArgs());
  }

  public TransactionResponse send(AssetSendInputBO input) throws Exception {
    return this.txProcessor.sendTransactionAndGetResponse(this.address, ABI, "send", input.toArgs());
  }

  public CallResponse balances(AssetBalancesInputBO input) throws Exception {
    return this.txProcessor.sendCall(this.client.getCryptoSuite().getCryptoKeyPair().getAddress(), this.address, ABI, "balances", input.toArgs());
  }

  public CallResponse issuer() throws Exception {
    return this.txProcessor.sendCall(this.client.getCryptoSuite().getCryptoKeyPair().getAddress(), this.address, ABI, "issuer", Arrays.asList());
  }

  // manual getters/setters
  public void setAddress(String address) { this.address = address; }
  public String getAddress() { return this.address; }
  public void setClient(Client client) { this.client = client; }
  public Client getClient() { return this.client; }
  public void setTxProcessor(AssembleTransactionProcessor txProcessor) { this.txProcessor = txProcessor; }
  public AssembleTransactionProcessor getTxProcessor() { return this.txProcessor; }
}
