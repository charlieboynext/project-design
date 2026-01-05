@echo off
chcp 65001 >nul
echo ==========================================
echo 启动商品溯源系统
echo ==========================================
echo.

:: 检查端口是否被占用
netstat -ano | findstr ":8080" >nul
if %errorlevel% == 0 (
    echo [警告] 端口 8080 已被占用，正在尝试关闭...
    for /f "tokens=5" %%a in ('netstat -ano ^| findstr ":8080" ^| findstr "LISTENING"') do (
        taskkill /F /PID %%a >nul 2>&1
    )
    timeout /t 2 /nobreak >nul
)

netstat -ano | findstr ":5173" >nul
if %errorlevel% == 0 (
    echo [警告] 端口 5173 已被占用，正在尝试关闭...
    for /f "tokens=5" %%a in ('netstat -ano ^| findstr ":5173" ^| findstr "LISTENING"') do (
        taskkill /F /PID %%a >nul 2>&1
    )
    timeout /t 2 /nobreak >nul
)

echo [1/2] 启动前端开发服务器...
start "前端服务器" cmd /k "cd /d %~dp0frontend && npm run dev"
timeout /t 3 /nobreak >nul

echo [2/2] 启动后端服务器...
echo.
echo ==========================================
echo 系统启动中，请稍候...
echo ==========================================
echo.
echo 前端地址: http://localhost:5173
echo 后端地址: http://localhost:8080
echo.
echo 提示: 
echo   - 前端服务器运行在新窗口中
echo   - 后端服务器运行在当前窗口
echo   - 按 Ctrl+C 可停止后端服务器
echo   - 关闭前端窗口可停止前端服务器
echo.
echo ==========================================
echo.

:: 启动后端
call gradlew.bat bootRun

