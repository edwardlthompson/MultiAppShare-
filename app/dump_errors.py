import sys

filepath = r"c:\Users\edwar\MultiAppShare-\build_error_app.txt"

with open(filepath, 'r', encoding='utf-8') as f:
    text = f.read()

lines = text.splitlines()
errors = []
for i, line in enumerate(lines):
    l_lower = line.lower()
    # Looking for Kotlin/Java compiler error markers
    if "e: " in line or "error:" in l_lower or "failed" in l_lower:
        # Include context
        context = "\n".join(lines[max(0, i-3):min(len(lines), i+3)])
        errors.append(f"--- Line {i+1} ---\n{context}\n")

print(f"Found {len(errors)} matching lines.")
for e in errors:
    print(e)
