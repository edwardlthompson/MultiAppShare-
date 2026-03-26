from PIL import Image
import os

def strip_all_metadata(image_path):
    print(f"Aggressively stripping ALL metadata from {image_path}...")
    try:
        img = Image.open(image_path)
        # Convert to a clean image without any metadata dictionaries
        data = list(img.getdata())
        clean_img = Image.new(img.mode, img.size)
        clean_img.putdata(data)
        
        # Save explicitly without any info or icc profile
        clean_img.save(image_path, "PNG", optimize=True)
        print("Done.")
    except Exception as e:
        print(f"Error: {e}")

if __name__ == "__main__":
    paths = [
        "c:/Users/edwar/MultiAppShare-/metadata/icon.png",
        "c:/Users/edwar/MultiAppShare-/metadata/logo.png"
    ]
    for p in paths:
        if os.path.exists(p):
            strip_all_metadata(p)
