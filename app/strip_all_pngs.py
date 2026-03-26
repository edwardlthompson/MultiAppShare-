import struct
import sys
import os
import glob

def strip_png(path):
    print(f"Binary stripping PNG: {path}")
    if not os.path.exists(path):
        print("File not found.")
        return
    
    with open(path, 'rb') as f:
        header = f.read(8)
        if header != b'\x89PNG\r\n\x1a\n':
            print(f"Not a PNG file (Header: {header}). Skipping.")
            return
        
        chunks = []
        while True:
            chunk_header = f.read(8)
            if not chunk_header:
                break
            length, type = struct.unpack('>I4s', chunk_header)
            data = f.read(length)
            crc = f.read(4)
            
            # Keep only essential chunks
            if type in [b'IHDR', b'IDAT', b'IEND', b'PLTE', b'tRNS']:
                chunks.append((length, type, data, crc))
            else:
                print(f"  Stripping chunk: {type.decode('ascii')} ({length} bytes)")

    with open(path, 'wb') as f:
        f.write(b'\x89PNG\r\n\x1a\n')
        for length, type, data, crc in chunks:
            f.write(struct.pack('>I4s', length, type))
            f.write(data)
            f.write(crc)
    print("  Strip complete.")

if __name__ == "__main__":
    # Expand to find all potential metadata images in the source repo
    patterns = [
        "c:/Users/edwar/MultiAppShare-/metadata/*.png",
        "c:/Users/edwar/MultiAppShare-/fastlane/metadata/android/**/*.png",
        "c:/Users/edwar/MultiAppShare-/app/src/main/res/**/*.png"
    ]
    
    for pattern in patterns:
        for path in glob.glob(pattern, recursive=True):
            strip_png(path)
            
    # Also strip in the fdroiddata clone if they exist
    strip_png("c:/Users/edwar/MultiAppShare-/fdroiddata_clone/metadata/com.edwardlthompson.multiappshare.png")
