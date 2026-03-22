def append_strategy(filepath):
    with open(filepath, 'r', encoding='utf-8') as f:
        text = f.read()
    
    strategy = """
// Force kotlin-stdlib and related libraries to 2.0.21 to resolve metadata version conflict.
configurations.all {
    resolutionStrategy {
        force("org.jetbrains.kotlin:kotlin-stdlib:2.0.21")
        force("org.jetbrains.kotlin:kotlin-stdlib-jdk8:2.0.21")
        force("org.jetbrains.kotlin:kotlin-stdlib-jdk7:2.0.21")
        force("org.jetbrains.kotlin:kotlin-reflect:2.0.21")
    }
}
"""
    if "configurations.all" not in text:
        text = text.rstrip() + strategy + "\n"
        with open(filepath, 'w', encoding='utf-8') as f:
            f.write(text)
        print(f"Appended strategy to {filepath}")
    else:
         print(f"Strategy already present in {filepath}")

append_strategy(r"c:\Users\edwar\MultiAppShare-\core-domain\build.gradle.kts")
append_strategy(r"c:\Users\edwar\MultiAppShare-\feature-dashboard\build.gradle.kts")
