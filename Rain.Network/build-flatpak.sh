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

# Check if desktop entry exists
if [ ! -f "ch.nivisan.raincloud.network.client.desktop" ]; then
    echo -e "${RED}.desktop file not found.${NC}"
    exit 1
fi

# Check if appdata file exists
if [ ! -f "ch.nivisan.raincloud.network.client.appdata.xml" ]; then
    echo -e "${RED}.appdata.xml file not found.${NC}"
    exit 1
fi

# Clean existing build directories
echo -e "${GREEN}Cleaning build directories...${NC}"
rm -rf build-dir repo

# Build the flatpak with repository
echo -e "${GREEN}Building Flatpak...${NC}"
sudo flatpak-builder --repo=repo --default-permissions build-dir ch.nivisan.raincloud.network.client.json

# Create bundle from repository
echo -e "${GREEN}Creating bundle...${NC}"
flatpak build-bundle repo rainchat.flatpak ch.nivisan.raincloud.network.client

# Cleanup build directories to save space
rm -rf build-dir repo

echo -e "${GREEN}Flatpak bundle created: rainchat.flatpak${NC}"
echo -e "${YELLOW}To install: flatpak install rainchat.flatpak${NC}"
echo -e "${YELLOW}To run: flatpak run com.nivisan.raincloud.network.client${NC}"