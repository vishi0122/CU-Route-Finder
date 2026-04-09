#!/bin/bash
echo "============================================"
echo "  CU Campus Route Finder - Build Script"
echo "============================================"
echo ""

mkdir -p out

echo "Compiling Java files..."
javac -d out src/*.java

if [ $? -ne 0 ]; then
    echo "[ERROR] Compilation failed!"
    exit 1
fi

echo "Compilation successful!"
echo ""
echo "Choose mode:"
echo "  1. Console Mode"
echo "  2. GUI Mode"
echo ""
read -p "Enter 1 or 2: " mode

if [ "$mode" = "2" ]; then
    echo "Launching GUI..."
    java -cp out Main gui
else
    echo "Launching Console..."
    java -cp out Main
fi
