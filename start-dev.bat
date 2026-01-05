@echo off
chcp 65001 >nul
echo ==========================================
echo 开发模式启动指南
echo ==========================================
echo.
echo 请使用以下方式启动系统：
echo.
echo 方式1: 使用统一启动脚本
echo   start.bat
echo.
echo 方式2: 手动启动（推荐开发时使用）
echo   终端1: gradlew bootRun          (启动后端)
echo   终端2: cd frontend ^&^& npm run dev  (启动前端)
echo.
echo 访问地址：
echo   前端: http://localhost:5173
echo   后端: http://localhost:8080
echo.
echo ==========================================
pause

