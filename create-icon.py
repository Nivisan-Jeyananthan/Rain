#!/usr/bin/env python3
"""
Generate a simple icon for RainChat application
Creates a 256x256 pixels icon
"""

try:
    from PIL import Image, ImageDraw
    
    # Create a new image with blue background
    img = Image.new('RGB', (256, 256), color=(70, 130, 180))  # Steel blue
    draw = ImageDraw.Draw(img)
    
    # Draw a simple chat bubble / network icon
    # Outer circle (representing network)
    draw.ellipse([30, 30, 226, 226], outline=(255, 255, 255), width=5)
    
    # Inner chat bubbles
    # Left bubble
    draw.ellipse([50, 80, 130, 140], fill=(255, 255, 255), outline=(70, 130, 180), width=2)
    
    # Right bubble
    draw.ellipse([140, 130, 220, 190], fill=(255, 255, 255), outline=(70, 130, 180), width=2)
    
    # Connection line
    draw.line([(130, 110), (140, 160)], fill=(255, 255, 255), width=3)
    
    # Save as PNG and ICO
    img.save('Rain.Network/icon.png')
    img.save('Rain.Network/icon.ico')
    print("✓ Icon created: Rain.Network/icon.png and icon.ico")
    
except ImportError:
    print("PIL not installed. Creating basic white square icon...")
    from PIL import Image
    img = Image.new('RGB', (256, 256), color=(70, 130, 180))
    img.save('Rain.Network/icon.ico')
    print("✓ Basic icon created: Rain.Network/icon.ico")
