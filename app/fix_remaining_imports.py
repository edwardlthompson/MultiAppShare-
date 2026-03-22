import sys

def fix_imports(filepath, replacements):
    with open(filepath, 'r', encoding='utf-8') as f:
        text = f.read()
    for old, new in replacements.items():
        text = text.replace(old, new)
    with open(filepath, 'w', encoding='utf-8') as f:
        f.write(text)
    print(f"Fixed imports in {filepath}")

# 1. RepositoryModule.kt
fix_imports(
    r"c:\Users\edwar\MultiAppShare-\app\src\main\java\com\multiappshare\di\RepositoryModule.kt",
    {
        "import com.multiappshare.GroupsRepository": "import com.multiappshare.domain.GroupsRepository",
        "import com.multiappshare.HistoryRepository": "import com.multiappshare.domain.HistoryRepository",
        "import com.multiappshare.SettingsRepository": "import com.multiappshare.domain.SettingsRepository"
    }
)

with open(r"c:\Users\edwar\MultiAppShare-\app\src\main\java\com\multiappshare\MainViewModel.kt", 'r', encoding='utf-8') as f:
    text = f.read()

pkg_str = "package com.multiappshare\n"
imports = """
import com.multiappshare.domain.GroupsRepository
import com.multiappshare.domain.HistoryRepository
import com.multiappshare.domain.SettingsRepository
"""

if pkg_str in text:
    text = text.replace(pkg_str, pkg_str + imports)
    with open(r"c:\Users\edwar\MultiAppShare-\app\src\main\java\com\multiappshare\MainViewModel.kt", 'w', encoding='utf-8') as f:
        f.write(text)
    print("Fixed MainViewModel.kt")

with open(r"c:\Users\edwar\MultiAppShare-\app\src\main\java\com\multiappshare\GroupEditViewModel.kt", 'r', encoding='utf-8') as f:
    text = f.read()

if pkg_str in text:
    text = text.replace(pkg_str, pkg_str + "\nimport com.multiappshare.domain.GroupsRepository\n")
    with open(r"c:\Users\edwar\MultiAppShare-\app\src\main\java\com\multiappshare\GroupEditViewModel.kt", 'w', encoding='utf-8') as f:
        f.write(text)
    print("Fixed GroupEditViewModel.kt")
