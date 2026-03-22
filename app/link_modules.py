import sys

filepath = r"c:\Users\edwar\MultiAppShare-\app\build.gradle.kts"

with open(filepath, 'r', encoding='utf-8') as f:
    lines = f.readlines()

# Index 98 is 0-indexed for line 99
target_line = 98

if "debugImplementation(libs.androidx.compose.ui.test.manifest)" in lines[target_line - 1]:
    print("Verification passed, modifying file.")
    insert_str = """
    // Independent Modules
    implementation(project(":core-database"))
    implementation(project(":core-domain"))
    implementation(project(":core-ui"))
    implementation(project(":feature-dashboard"))
"""
    lines.insert(target_line, insert_str)
    
    with open(filepath, 'w', encoding='utf-8') as f:
        f.writelines(lines)
    print("Linked modules successfully.")
else:
    print("Verification failed! Layout mismatch.")
    # Print surrounding lines for safety
    for i in range(max(0, target_line-2), min(len(lines), target_line+2)):
        print(f"{i+1}: {lines[i].strip()}")
    sys.exit(1)
