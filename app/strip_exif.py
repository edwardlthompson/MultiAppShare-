from PIL import Image
import os

def strip_exif(image_path):
    print(f"Stripping EXIF from {image_path}...")
    img = Image.open(image_path)
    data = list(img.getdata())
    img_without_exif = Image.new(img.mode, img.size)
    img_without_exif.putdata(data)
    img_without_exif.save(image_path)
    print("Done.")

if __name__ == "__main__":
    paths = [
        "c:/Users/edwar/MultiAppShare-/metadata/icon.png",
        "c:/Users/edwar/MultiAppShare-/metadata/logo.png"
    ]
    for p in paths:
        if os.path.exists(p):
            strip_exif(p)
        else:
            print(f"File not found: {p}")
