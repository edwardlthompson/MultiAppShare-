from PIL import Image
from PIL.ExifTags import TAGS
import sys

def check_exif(image_path):
    print(f"Checking EXIF for {image_path}...")
    try:
        img = Image.open(image_path)
        exif_data = img._getexif()
        if exif_data:
            print(f"EXIF found: {len(exif_data)} tags.")
            for tag, value in exif_data.items():
                print(f"  {TAGS.get(tag, tag)}: {value}")
        else:
            print("No EXIF found.")
    except Exception as e:
        print(f"Error check: {e}")

def strip_exif_v2(image_path):
    print(f"Stripping EXIF (v2) from {image_path}...")
    try:
        img = Image.open(image_path)
        # Using convert + save(quality=...) to force re-encoding and strip tags
        data = img.convert('RGB')
        data.save(image_path, "PNG") 
        print("Done.")
    except Exception as e:
        print(f"Error strip: {e}")

if __name__ == "__main__":
    icon_path = "c:/Users/edwar/MultiAppShare-/metadata/icon.png"
    logo_path = "c:/Users/edwar/MultiAppShare-/metadata/logo.png"
    
    check_exif(icon_path)
    strip_exif_v2(icon_path)
    check_exif(icon_path)
    
    check_exif(logo_path)
    strip_exif_v2(logo_path)
    check_exif(logo_path)
