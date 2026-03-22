import sys

filepath = r"c:\Users\edwar\MultiAppShare-\build_error_app.txt"

encodings = ['utf-8', 'utf-16', 'utf-16-le', 'cp1252']

text = ""
for enc in encodings:
    try:
        with open(filepath, 'r', encoding=enc) as f:
            text = f.read()
            if len(text) > 100:
                print(f"Read successful with {enc} (Length: {len(text)})")
                break
    except Exception:
        continue

if not text:
    print("Could not read file with any encoding.")
    sys.exit(1)

# Find errors
lines = text.splitlines()
errors = []
for i, line in enumerate(lines):
    l_lower = line.lower()
    if "error:" in l_lower or "exception" in l_lower or "failed" in l_lower or "e: " in line:
        # Include a bit of context
        context = "\n".join(lines[max(0, i-2):min(len(lines), i+3)])
        errors.append(f"--- Line {i+1} ---\n{context}\n")

print(f"Found {len(errors)} matching lines.")
for e in errors[:5]:  # Print first 5
    print(e)
