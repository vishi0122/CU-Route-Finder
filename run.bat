@echo off
echo ============================================
echo   CU Campus Route Finder - Build Script
echo ============================================
echo.

if not exist "out" mkdir out

echo Compiling Java files...
javac -d out src\*.java

if %errorlevel% neq 0 (
    echo.
    echo [ERROR] Compilation failed! Check Java files.
    pause
    exit /b 1
)

echo Compilation successful!
echo.
echo Choose mode:
echo   1. Console Mode
echo   2. GUI Mode
echo.
set /p mode="Enter 1 or 2: "

if "%mode%"=="2" (
    echo Launching GUI...
    java -cp out Main gui
) else (
    echo Launching Console...
    java -cp out Main
)
pause
