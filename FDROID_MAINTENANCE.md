# 🏁 F-Droid Maintenance & Release Guide

This guide ensures that future version updates of MultiAppShare follow the strict, proven template required by the F-Droid buildserver and audit scripts.

## 📐 Current Structure (Template)

### 1. Metadata Location
- **Main YAML**: `metadata/com.edwardlthompson.multiappshare.yml` (in the `fdroiddata` repo).
- **Localized Fields**: `metadata/com.edwardlthompson.multiappshare/en-US/`
  - `short_description.txt`: Brief summary (max 80 chars).
  - `description.txt`: Full app description.

### 2. Version Alignment (CRITICAL)
Before every release, ensure these three fields match **exactly**:
1.  `app/build.gradle.kts` → `versionCode: 173`, `versionName: "1.7.3"`
2.  `metadata/com.edwardlthompson.multiappshare.yml` → `CurrentVersionCode: 173`, `CurrentVersion: 1.7.3`
3.  **Git Tag**: `v1.7.3`

---

## 🛡️ Pitfalls & Solutions

### ❌ Binary Audit Failure
**Problem:** F-Droid scanners find compiled `.class` or `.bin` files.
**Solution:** Ensure `.gitignore` ignores `build/` recursively. Never commit anything from a `build` folder.

### ❌ Image Metadata Audit Failure
**Problem:** F-Droid rejects PNGs with hidden chunks (EXIF, pHYs, tIME).
**Solution:** Run the binary-level stripper on **ANY new images** before committing:
```bash
python app/strip_all_pngs.py
```
*This script is located in the `app/` directory and is essential for all project assets.*

### ❌ YAML Syntax Failure
**Problem:** Double `AutoName` keys or `rewritemeta` errors.
**Solution:** Keep the YAML clean. Never add `Summary:` or `Description:` back to the YAML; keep them in the `en-US` folder.

---

## 🚀 Release Workflow
1.  **Bump Version** in `app/build.gradle.kts`.
2.  **Strip Images**: `python app/strip_all_pngs.py`.
3.  **Commit & Tag**:
    ```bash
    git add . ; git commit -m "release: v1.7.4"
    git tag v1.7.4
    git push origin main --tags
    ```
4.  **Update fdroiddata**: Sync the YAML `CurrentVersion` and `CurrentVersionCode` in your `fdroiddata` fork.
