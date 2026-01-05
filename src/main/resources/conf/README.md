# 区块链证书文件放置说明

本目录用于放置 FISCO-BCOS SDK 连接所需的证书/私钥文件（例如 `ca.crt`、`sdk.crt`、`sdk.key` 等）。

为避免泄露敏感信息，本项目的 `.gitignore` 已忽略本目录下除本文件外的所有内容。

如需启用链连接：
1. 将证书文件放入本目录（或按 `application.properties` 中的 `system.certPath` 配置放到其它目录）。
2. 在 `application.properties` 中配置节点、群组、合约地址等参数，并设置 `system.bcos-enabled=true`。

