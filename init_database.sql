-- 商品溯源系统数据库初始化脚本
-- 删除旧数据库（如果存在）并创建新数据库

-- 删除旧数据库（如果存在）
DROP DATABASE IF EXISTS traceability;

-- 创建新数据库
CREATE DATABASE traceability 
    CHARACTER SET utf8mb4 
    COLLATE utf8mb4_unicode_ci;

-- 使用新数据库
USE traceability;

-- 显示成功信息
SELECT 'Database traceability created successfully!' AS message;

