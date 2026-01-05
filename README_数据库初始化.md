# 数据库初始化说明

## 当前情况

MySQL 服务正在运行，但使用密码 "root" 无法连接。

## 解决方案

### 方法一：手动执行 SQL（最简单）

1. **打开命令提示符或 PowerShell**

2. **执行以下命令**（会提示输入密码）：
   ```bash
   mysql -u root -p < init_database.sql
   ```
   
   然后输入你的 MySQL root 密码

3. **或者直接执行 SQL 语句**：
   ```bash
   mysql -u root -p
   ```
   
   然后输入密码，进入 MySQL 后执行：
   ```sql
   DROP DATABASE IF EXISTS traceability;
   CREATE DATABASE traceability CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   SHOW DATABASES LIKE 'traceability';
   ```

### 方法二：使用 MySQL Workbench

1. 打开 MySQL Workbench
2. 连接到本地 MySQL 服务器（localhost）
3. 打开 `init_database.sql` 文件
4. 执行 SQL 语句

### 方法三：如果密码确实是 "root"

如果确认密码是 "root" 但仍然无法连接，可能是 MySQL 8.0 的认证插件问题。

可以尝试：
```bash
mysql -u root -proot --default-auth=mysql_native_password
```

## SQL 脚本内容

```sql
DROP DATABASE IF EXISTS traceability;
CREATE DATABASE traceability 
    CHARACTER SET utf8mb4 
    COLLATE utf8mb4_unicode_ci;
```

## 验证数据库

执行成功后，验证数据库：
```bash
mysql -u root -p -e "SHOW DATABASES LIKE 'traceability';"
```

应该看到 `traceability` 数据库。

## 下一步

数据库创建成功后：
1. 启动应用程序：`gradlew bootRun`
2. 系统会自动创建所有表结构
3. 访问：http://localhost:8080

## 注意事项

- 确保 MySQL 服务正在运行
- 如果忘记密码，可能需要重置 MySQL root 密码
- 数据库创建后，应用启动时会自动创建表结构

