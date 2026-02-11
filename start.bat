@echo off
REM Books Online - Start Script (Windows)
REM This script starts both backend and frontend servers

echo ==========================================
echo Books Online - Starting Application
echo ==========================================
echo.

REM Check if Java is installed
where java >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Java is not installed or not in PATH
    echo Please install Java 17 from https://adoptium.net/
    pause
    exit /b 1
)

REM Check Java version
echo Checking Java version...
java -version 2>&1 | findstr /i "version \"17" >nul
if %ERRORLEVEL% NEQ 0 (
    echo [WARNING] Java 17 is required but a different version was found
    echo Please ensure JAVA_HOME points to Java 17
    echo.
    echo Current Java version:
    java -version
    echo.
    pause
)

REM Check if Node.js is installed
where node >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Node.js is not installed
    echo Please install Node.js from https://nodejs.org/
    pause
    exit /b 1
)

echo [OK] Node.js detected
node -v
echo.

REM Check if Maven is installed
where mvn >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Maven is not installed
    echo Please install Maven from https://maven.apache.org/
    pause
    exit /b 1
)

echo ==========================================
echo Starting Backend (Spring Boot)...
echo ==========================================
cd backend

echo Building backend...
call mvn clean install -DskipTests > ..\backend-build.log 2>&1

if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Backend build failed. Check backend-build.log for details
    pause
    exit /b 1
)

echo [OK] Backend built successfully
echo Starting backend server...

REM Start backend in new window
start "Books Online - Backend" cmd /c "mvn spring-boot:run > ..\backend.log 2>&1"

echo [OK] Backend starting in new window...
echo    URL: http://localhost:8080
echo    Swagger UI: http://localhost:8080/swagger-ui.html
echo    H2 Console: http://localhost:8080/h2-console
echo.

echo Waiting for backend to start (30 seconds)...
timeout /t 30 /nobreak >nul

cd ..

echo ==========================================
echo Starting Frontend (React + Vite)...
echo ==========================================
cd frontend

REM Install dependencies if node_modules doesn't exist
if not exist "node_modules\" (
    echo Installing frontend dependencies...
    call npm install > ..\frontend-install.log 2>&1

    if %ERRORLEVEL% NEQ 0 (
        echo [ERROR] Frontend dependency installation failed
        echo Check frontend-install.log for details
        pause
        exit /b 1
    )

    echo [OK] Frontend dependencies installed
)

echo Starting frontend server...

REM Start frontend in new window
start "Books Online - Frontend" cmd /c "npm run dev > ..\frontend.log 2>&1"

echo [OK] Frontend starting in new window...
echo    URL: http://localhost:5173
echo.

cd ..

echo ==========================================
echo Application Started Successfully!
echo ==========================================
echo.
echo Access the application:
echo   Frontend:   http://localhost:5173
echo   Backend:    http://localhost:8080
echo   Swagger:    http://localhost:8080/swagger-ui.html
echo   H2 Console: http://localhost:8080/h2-console
echo.
echo Logs:
echo   Backend:  backend.log
echo   Frontend: frontend.log
echo.
echo Both servers are running in separate windows.
echo Close those windows to stop the servers.
echo.
pause
