#!/bin/bash

# Books Online - Start Script (Mac/Linux)
# This script starts both backend and frontend servers

echo "=========================================="
echo "Books Online - Starting Application"
echo "=========================================="
echo ""

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Check if Java 17 is available
echo "Checking Java version..."
JAVA_VERSION=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}' | cut -d'.' -f1)

if [ "$JAVA_VERSION" != "17" ]; then
    echo -e "${RED}Error: Java 17 is required but found Java $JAVA_VERSION${NC}"
    echo ""
    echo "To fix this:"
    echo "1. Install Java 17:"
    echo "   brew install openjdk@17"
    echo ""
    echo "2. Set JAVA_HOME for this session:"
    echo "   export JAVA_HOME=\$(/usr/libexec/java_home -v 17)"
    echo ""
    echo "3. Run this script again"
    echo ""
    exit 1
fi

echo -e "${GREEN}✓ Java 17 detected${NC}"
echo ""

# Check if Node.js is installed
if ! command -v node &> /dev/null; then
    echo -e "${RED}Error: Node.js is not installed${NC}"
    echo "Please install Node.js from https://nodejs.org/"
    exit 1
fi

echo -e "${GREEN}✓ Node.js detected: $(node -v)${NC}"
echo ""

# Function to cleanup background processes on exit
cleanup() {
    echo ""
    echo "Shutting down servers..."
    kill $BACKEND_PID $FRONTEND_PID 2>/dev/null
    exit
}

trap cleanup SIGINT SIGTERM

# Start Backend
echo "=========================================="
echo "Starting Backend (Spring Boot)..."
echo "=========================================="
cd backend

# Check if mvn is available
if ! command -v mvn &> /dev/null; then
    echo -e "${RED}Error: Maven is not installed${NC}"
    echo "Please install Maven"
    exit 1
fi

# Build and run backend
echo "Building backend..."
mvn clean install -DskipTests > ../backend-build.log 2>&1

if [ $? -ne 0 ]; then
    echo -e "${RED}✗ Backend build failed. Check backend-build.log for details${NC}"
    exit 1
fi

echo -e "${GREEN}✓ Backend built successfully${NC}"
echo "Starting backend server..."

mvn spring-boot:run > ../backend.log 2>&1 &
BACKEND_PID=$!

echo -e "${GREEN}✓ Backend started (PID: $BACKEND_PID)${NC}"
echo "   URL: http://localhost:8080"
echo "   Swagger UI: http://localhost:8080/swagger-ui.html"
echo "   H2 Console: http://localhost:8080/h2-console"
echo ""

# Wait for backend to start
echo "Waiting for backend to start (30 seconds)..."
sleep 30

cd ..

# Start Frontend
echo "=========================================="
echo "Starting Frontend (React + Vite)..."
echo "=========================================="
cd frontend

# Install dependencies if node_modules doesn't exist
if [ ! -d "node_modules" ]; then
    echo "Installing frontend dependencies..."
    npm install > ../frontend-install.log 2>&1

    if [ $? -ne 0 ]; then
        echo -e "${RED}✗ Frontend dependency installation failed. Check frontend-install.log${NC}"
        kill $BACKEND_PID
        exit 1
    fi

    echo -e "${GREEN}✓ Frontend dependencies installed${NC}"
fi

echo "Starting frontend server..."
npm run dev > ../frontend.log 2>&1 &
FRONTEND_PID=$!

echo -e "${GREEN}✓ Frontend started (PID: $FRONTEND_PID)${NC}"
echo "   URL: http://localhost:5173"
echo ""

cd ..

# Display success message
echo "=========================================="
echo -e "${GREEN}Application Started Successfully!${NC}"
echo "=========================================="
echo ""
echo "Access the application:"
echo "  Frontend:  http://localhost:5173"
echo "  Backend:   http://localhost:8080"
echo "  Swagger:   http://localhost:8080/swagger-ui.html"
echo "  H2 Console: http://localhost:8080/h2-console"
echo ""
echo "Logs:"
echo "  Backend:  backend.log"
echo "  Frontend: frontend.log"
echo ""
echo -e "${YELLOW}Press Ctrl+C to stop both servers${NC}"
echo ""

# Wait for user to stop
wait $BACKEND_PID $FRONTEND_PID
