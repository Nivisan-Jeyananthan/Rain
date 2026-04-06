#!/usr/bin/env python3
"""
Generate a professional icon for RainChat application
Creates a 256x256 pixels icon with rain drops and chat bubbles
"""

try:
    from PIL import Image, ImageDraw
    
    # Create a new image with gradient-like background
    size = 256
    img = Image.new('RGB', (size, size), color=(25, 118, 210))  # Dark blue
    draw = ImageDraw.Draw(img, 'RGBA')
    
    # Draw gradient effect (light to dark blue)
    for y in range(size):
        ratio = y / size
        r = int(25 + (100 * ratio))
        g = int(118 + (40 * ratio))
        b = int(210)
        draw.rectangle([(0, y), (size, y+1)], fill=(r, g, b, 255))
    
    # Draw rain drops (water droplets) - left side
    # Drop 1
    draw.ellipse([40, 50, 70, 90], fill=(100, 200, 255, 200), outline=(255, 255, 255, 200), width=2)
    # Drop 2
    draw.ellipse([80, 40, 105, 75], fill=(120, 220, 255, 200), outline=(255, 255, 255, 150), width=2)
    # Drop 3
    draw.ellipse([130, 60, 155, 95], fill=(100, 200, 255, 180), outline=(255, 255, 255, 150), width=2)
    
    # Draw chat bubbles - right side
    # Left chat bubble (sender)
    bubble_left = [40, 130, 130, 190]
    draw.rounded_rectangle(bubble_left, radius=15, fill=(100, 200, 255, 220), outline=(255, 255, 255, 200), width=2)
    # Bubble pointer
    draw.polygon([(40, 180), (30, 200), (50, 190)], fill=(100, 200, 255, 220))
    
    # Right chat bubble (receiver)
    bubble_right = [150, 120, 240, 180]
    draw.rounded_rectangle(bubble_right, radius=15, fill=(255, 255, 255, 240), outline=(100, 200, 255, 200), width=2)
    # Bubble pointer
    draw.polygon([(240, 170), (260, 180), (240, 160)], fill=(255, 255, 255, 240))
    
    # Draw lines in bubbles (text representation)
    # Left bubble text
    draw.line([(50, 150), (110, 150)], fill=(255, 255, 255, 200), width=2)
    draw.line([(50, 165), (100, 165)], fill=(255, 255, 255, 150), width=2)
    
    # Right bubble text
    draw.line([(165, 140), (225, 140)], fill=(50, 100, 150, 200), width=2)
    draw.line([(165, 155), (220, 155)], fill=(50, 100, 150, 150), width=2)
    
    # Save as PNG and ICO
    img.save('Rain.Network/icon.png')
    img.save('Rain.Network/icon.ico')
    print("Professional icon created: Rain.Network/icon.png and icon.ico")
    
except ImportError as e:
    print(f"PIL not installed ({e}). Creating basic blue icon...")
    try:
        from PIL import Image
        img = Image.new('RGB', (256, 256), color=(25, 118, 210))
        img.save('Rain.Network/icon.ico')
        print("Basic icon created: Rain.Network/icon.ico")
    except ImportError:
        print("PIL completely unavailable. Skipping icon creation.")
        import struct
        # Very basic ICO header (this won't work well, but prevents crash)
        ico_data = b'\x00\x00\x01\x00\x01\x00\x10\x10\x00\x00\x01\x00 \x00h\x04\x00\x00'
        with open('Rain.Network/icon.ico', 'wb') as f:
            f.write(ico_data)
        print("Minimal fallback icon created (may not display correctly)")

