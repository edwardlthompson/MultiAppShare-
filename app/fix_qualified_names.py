path = r"c:\Users\edwar\MultiAppShare-\app\src\main\java\com\multiappshare\MainActivity.kt"

with open(path, "r", encoding="utf-8") as f:
    content = f.read()

# Fix chained fully qualified names (The Compile Errors)
content = content.replace(".androidx.compose.foundation.layout.height(", ".height(")
content = content.replace(".androidx.compose.foundation.layout.width(", ".width(")
content = content.replace(".androidx.compose.foundation.layout.size(", ".size(")
content = content.replace(".androidx.compose.foundation.background(", ".background(")
content = content.replace(".androidx.compose.foundation.verticalScroll(", ".verticalScroll(")
content = content.replace(".androidx.compose.foundation.rememberScrollState()", ".rememberScrollState()")
content = content.replace(".androidx.compose.ui.draw.clip(", ".clip(")

with open(path, "w", encoding="utf-8") as f:
    f.write(content)

print("Cleanup complete!")
