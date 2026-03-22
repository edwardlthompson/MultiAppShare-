filepath = r"c:\Users\edwar\MultiAppShare-\build_error_app3.txt"

with open(filepath, 'r', encoding='utf-8') as f:
    text = f.read()

lines = text.splitlines()
errors = []
for i, line in enumerate(lines):
    l_lower = line.lower()
    if "e: " in line or "error:" in l_lower or "failed" in l_lower:
        context = "\n".join(lines[max(0, i-4):min(len(lines), i+4)])
        errors.append(f"--- Line {i+1} ---\n{context}\n\n")

with open(r"c:\Users\edwar\MultiAppShare-\app\errors_final3.txt", 'w', encoding='utf-8') as f:
    f.writelines(errors)

print(f"Saved {len(errors)} errors to errors_final3.txt")
