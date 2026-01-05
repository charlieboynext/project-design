package org.example.demo.controller;

import java.lang.Exception;
import java.util.Map;
import org.example.demo.model.CommonResponse;
import org.example.demo.model.bo.AssetBalancesInputBO;
import org.example.demo.model.bo.AssetIssueInputBO;
import org.example.demo.model.bo.AssetSendInputBO;
import org.example.demo.service.AssetService;
import org.fisco.bcos.sdk.transaction.model.dto.CallResponse;
import org.fisco.bcos.sdk.transaction.model.dto.TransactionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/asset")
@ConditionalOnBean(AssetService.class)
@ConditionalOnProperty(prefix = "system", name = "bcos-enabled", havingValue = "true")
public class AssetController {

  @Autowired
  private AssetService assetService; // 默认使用应用当前密钥对

  // 可选：按地址路由到指定账户对应的 AssetService（由 ServiceManager 装配）
  @Autowired(required = false)
  @Qualifier("AssetService")
  private Map<String, AssetService> assetServiceMap;

  private AssetService resolveService(String fromAddress) {
    if (fromAddress != null && assetServiceMap != null) {
      AssetService svc = assetServiceMap.get(fromAddress);
      if (svc != null) {
        return svc;
      }
    }
    return assetService;
  }

  @PostMapping("/issue")
  public CommonResponse issue(@RequestBody AssetIssueInputBO input,
                              @RequestParam(value = "from", required = false) String from) {
    try {
      TransactionResponse resp = resolveService(from).issue(input);
      return CommonResponse.ok(resp);
    } catch (Exception ex) {
      return CommonResponse.fail("TX_ERROR", ex);
    }
  }

  @PostMapping("/send")
  public CommonResponse send(@RequestBody AssetSendInputBO input,
                             @RequestParam(value = "from", required = false) String from) {
    try {
      TransactionResponse resp = resolveService(from).send(input);
      return CommonResponse.ok(resp);
    } catch (Exception ex) {
      return CommonResponse.fail("TX_ERROR", ex);
    }
  }

  @GetMapping("/balances")
  public CommonResponse balances(@RequestParam("address") String address,
                                 @RequestParam(value = "from", required = false) String from) {
    try {
      AssetBalancesInputBO input = new AssetBalancesInputBO(address);
      CallResponse resp = resolveService(from).balances(input);
      return CommonResponse.ok(resp);
    } catch (Exception ex) {
      return CommonResponse.fail("CALL_ERROR", ex);
    }
  }

  @GetMapping("/issuer")
  public CommonResponse issuer(@RequestParam(value = "from", required = false) String from) {
    try {
      CallResponse resp = resolveService(from).issuer();
      return CommonResponse.ok(resp);
    } catch (Exception ex) {
      return CommonResponse.fail("CALL_ERROR", ex);
    }
  }
}
