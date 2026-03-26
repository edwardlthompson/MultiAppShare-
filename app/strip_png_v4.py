import struct
import sys
import os

def strip_png(path):
    print(f"Binary stripping PNG: {path}")
    if not os.path.exists(path):
        print("File not found.")
        return
    
    with open(path, 'rb') as f:
        header = f.read(8)
        if header != b'\x89PNG\r\n\x1a\n':
            print("Not a PNG file.")
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
            if type in [b'IHDR', b'IDAT', b'IEND']:
                chunks.append((length, type, data, crc))
            else:
                print(f"Stripping chunk: {type.decode('ascii')} ({length} bytes)")

    with open(path, 'wb') as f:
        f.write(b'\x89PNG\r\n\x1a\n')
        for length, type, data, crc in chunks:
            f.write(struct.pack('>I4s', length, type))
            f.write(data)
            f.write(crc)
    print("Strip complete.")

if __name__ == "__main__":
    paths = [
        "c:/Users/edwar/MultiAppShare-/metadata/icon.png",
        "c:/Users/edwar/MultiAppShare-/metadata/logo.png"
    ]
    for p in paths:
        strip_png(p)
