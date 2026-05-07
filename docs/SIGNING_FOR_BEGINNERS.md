# Release signing for beginners

You only need this when you want an APK that is **signed with your own key**—for example to **install on your phone with adb**, or to **upload to GitHub Releases** or the Play Store.  
Debug builds from Android Studio already work without any of this.

### The whole process in three commands (after JDK 21 is installed)

1. In PowerShell, `cd` to the folder that contains **`gradlew.bat`**, then run **`.\scripts\setup-release-keystore.ps1`** once.
2. Build: **`.\scripts\build-signed-release-apk.ps1`**
3. Optional install: **`adb install -r`** path to the APK shown by the build (USB debugging on).

Details and troubleshooting are below.

---

## Words you will see

| Word | Plain meaning |
| :--- | :--- |
| **Keystore** | A small password-protected file that holds your “signature” for the app. Google and users trust updates that match the same signature. |
| **Alias** | A short name inside the keystore for one key (this project defaults to **`multiappshare`**). |
| **`keystore.properties`** | A **local** text file Gradle reads so it knows your passwords and file paths. It must **never** be committed to git (it is already listed in **`.gitignore`**). |

If you **lose** the keystore file or forget the passwords, you **cannot** ship updates that replace an existing Play Store or user install under the same signing identity. **Back up `release.keystore`** somewhere safe (password manager attachment, encrypted drive—not email in plain text).

---

## Step 1 — One-time: create the key (Windows)

1. Open **PowerShell**.
2. Go to your project folder (the same folder that contains **`gradlew.bat`**):

   ```powershell
   cd path\to\MultiAppShare-
   ```

3. Run the helper script:

   ```powershell
   .\scripts\setup-release-keystore.ps1
   ```

4. Answer the prompts:
   - **Alias**: press **Enter** to use **`multiappshare`** (recommended).
   - **Passwords**: choose strong passwords you can store safely. The script can use **one password** for both the keystore and the key (simplest).
   - **Your name or org**: appears on the certificate only; it does not have to match your GitHub username.

When it finishes you should have:

- **`release.keystore`** — your secret key file (do **not** upload to GitHub).
- **`keystore.properties`** — passwords for Gradle on **your** PC only (do **not** commit).

If **`release.keystore` already exists**, the script stops so you do not overwrite it by accident.

---

## Step 2 — Build the signed release APK

In the same project folder:

```powershell
.\scripts\build-signed-release-apk.ps1
```

Or:

```powershell
.\gradlew.bat :app:assembleRelease
```

The APK path is printed by the script. By default it looks like:

`app\build\outputs\apk\release\MultiAppShare-v*-release.apk`

(More detail: **`docs/LOCAL_RELEASE_BUILD.md`**.)

---

## Step 3 — Install on your phone with adb (optional)

1. On the phone: **Settings → Developer options → USB debugging** → On.  
2. Plug in USB; accept the computer’s RSA prompt on the phone.  
3. On the PC (with [platform-tools](https://developer.android.com/tools/releases/platform-tools) installed):

   ```powershell
   adb devices
   adb install -r app\build\outputs\apk\release\MultiAppShare-v1.8.0-release.apk
   ```

Change the file name if your version string differs.  
If Android says the install failed because an **existing app was signed differently**, uninstall the old app first or see **`docs/BASELINE_PROFILE.md`** (**INSTALL_FAILED_UPDATE_INCOMPATIBLE**).

---

## Step 4 — GitHub Releases (optional)

1. On GitHub: **Releases → Draft a new release**.  
2. Attach **only the `.apk`** file users should install.  
3. **Never** attach **`release.keystore`** or **`keystore.properties`**.

---

## If you get stuck

- **“Could not find keytool”**: Install **JDK 21**, then set **JAVA_HOME** to that JDK (Android Studio’s **Settings → Build → Build Tools → Gradle** JDK path is a good hint).  
- **Build works but signing seems wrong**: Confirm **`keystore.properties`** exists next to **`gradlew.bat`** and matches the keystore path and passwords.  
- **Advanced / CI / Play App Bundle**: **`docs/LOCAL_RELEASE_BUILD.md`**.
