#!/bin/bash
# Build script for RainChat Flatpak

set -e

# Colors for output (disable in CI)
if [ -t 1 ]; then
    RED='\033[0;31m'
    GREEN='\033[0;32m'
    YELLOW='\033[1;33m'
    NC='\033[0m'
else
    RED=''
    GREEN=''
    YELLOW=''
    NC=''
fi

echo -e "${YELLOW}Building RainChat Flatpak...${NC}"

# Check if flatpak-builder is installed
if ! command -v flatpak-builder &> /dev/null; then
    echo -e "${RED}flatpak-builder not found. Install it first:${NC}"
    echo "sudo apt install flatpak-builder"
    exit 1
fi

# Check if Client.jar exists
if [ ! -f "Client.jar" ]; then
    echo -e "${RED}Client.jar not found. Please build the JAR first.${NC}"
    exit 1
fi

# Clean existing build directory
echo -e "${GREEN}Cleaning build directory...${NC}"
rm -rf build-dir

# Build the flatpak (without --force-clean for better bundle compatibility)
echo -e "${GREEN}Building Flatpak...${NC}"
sudo flatpak-builder --default-permissions build-dir com.nivisan.raincloud.network.client.json

# Create bundle
echo -e "${GREEN}Creating bundle...${NC}"
flatpak build-bundle build-dir rainchat.flatpak com.nivisan.raincloud.network.client

# Cleanup build directory to save space
rm -rf build-dir

echo -e "${GREEN}Flatpak bundle created: rainchat.flatpak${NC}"
echo -e "${YELLOW}To install: flatpak install rainchat.flatpak${NC}"
echo -e "${YELLOW}To run: flatpak run com.nivisan.raincloud.network.client${NC}"