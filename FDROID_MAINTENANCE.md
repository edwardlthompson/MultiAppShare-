# 🏁 F-Droid Maintenance & Release Guide

This guide ensures that future version updates of MultiAppShare follow the strict, proven template required by the F-Droid buildserver and audit scripts.

## 📐 Current Structure (Template)

### 1. Metadata Location
- **App repo (this GitHub project)**: `metadata/com.edwardlthompson.multiappshare.yml` is the **canonical** copy to paste into your GitLab `fdroiddata` fork. It intentionally has **no** `Summary`, `Description`, or `AutoName` so F-Droid pulls store text from **Fastlane** under `fastlane/metadata/android/` in the same GitHub repo.
- **F-Droid data repo (GitLab fork of `fdroid/fdroiddata`)**: after editing, open a **Merge Request** to [gitlab.com/fdroid/fdroiddata](https://gitlab.com/fdroid/fdroiddata). Your fork’s default branch is usually `master`; CI there runs `fdroid lint`, schema checks, and `check-fastlane.py`.
- **Optional `en-US` folder in `fdroiddata`**: only if you are **not** using Fastlane in the app source; this project uses Fastlane, so you normally **do not** add `metadata/com.edwardlthompson.multiappshare/en-US/` on GitLab.

### 2. Version Alignment (CRITICAL)
Before every release, ensure these three fields match **exactly**:
1.  `app/build.gradle.kts` → `versionCode: 174`, `versionName: "1.7.4"`
2.  `metadata/com.edwardlthompson.multiappshare.yml` → `CurrentVersionCode: 174`, `CurrentVersion: 1.7.4` (use `CurrentVersion`, not `CurrentVersionName`)
3.  **Git Tag**: `v1.7.4`

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
**Problem:** Duplicate YAML keys (GitLab CI: `duplication of key "AutoName"`) or `rewritemeta` / `check-fastlane` parse errors.
**Solution:** Keep a single canonical block. Do **not** set `AutoName` in the `.yml` if Fastlane already defines the title (`fastlane/.../title.txt`). Do **not** add `Summary:` / `Description:` to the `.yml` for this app; Fastlane supplies them.

### ❌ GitLab `fdroiddata` CI failures
**Problem:** Personal fork pipeline fails on `metadata/com.edwardlthompson.multiappshare.yml`.
**Solution:** Copy the YAML from this repo’s `metadata/` folder, push to your fork, and fix any merge residue (duplicate keys). Publication still requires an accepted **MR to `fdroid/fdroiddata`**, not only a green fork pipeline.

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
4.  **Update GitLab `fdroiddata` fork**: Copy `metadata/com.edwardlthompson.multiappshare.yml` from this repo into the same path in your fork; set `CurrentVersion`, `CurrentVersionCode`, and the `Builds:` `commit:` to the new tag. Push `master` (or your default branch), then open/update an MR to `fdroid/fdroiddata`. The helper scripts `release_v1_7_4.sh` / `automate_v174.sh` expect a sibling clone at `../fdroiddata`.

### GitHub branch `fdroiddata`
This branch is kept **merged with `main`** so the same metadata and source snapshot match. F-Droid still builds from **GitHub** using the tag in the YAML, not from this branch name.

### Automate GitLab fork sync (token)
From the repo root, with a GitLab PAT that has **`api`** and **`write_repository`**:

```powershell
$env:GITLAB_TOKEN = "glpat-..."   # or put GITLAB_TOKEN= in scripts/.env.local
.\scripts\sync-fdroiddata-gitlab.ps1
```

Optional: `-GitLabForkPath yourname/fdroiddata`, `-FdroidDataPath D:\src\fdroiddata`, `-SkipMr`, `-DryRun`. Versions and tag default from `app/build.gradle.kts` (`v` + `versionName`).
