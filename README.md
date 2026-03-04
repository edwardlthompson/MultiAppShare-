# Multi App Share

**Multi App Share** is a utility Android application designed to streamline the process of sharing content across multiple applications. Instead of manually sharing a photo, video, link, or text to each social media platform or messaging app one by one, you can create custom groups and share to all of them in a sequential, guided workflow.

## 🚀 Features

- **Custom App Groups**: Organize your target apps into groups like "Social Media", "Work", or "Friends".
- **Sequential Sharing**: Share content once, and the app guides you through each target app in your group.
- **Smart Compatibility**: Automatically detects and skips apps that do not support the specific media type (photo, video, link, text) you are sharing.
- **Persistent State**: Remembers your group configurations and even whether they were expanded or collapsed.
- **History Tracking**: Keeps a log of your last 50 sharing activities, highlighting any failures in red.
- **Universal Content Support**: Works with images, videos, URLs, plain text, and generic files.
- **Reorderable Lists**: Easily adjust the order in which apps are launched within each group.

## 🛠 Tech Stack

- **Language**: [Kotlin](https://kotlinlang.org/)
- **UI Framework**: [Jetpack Compose](https://developer.android.com/jetpack/compose)
- **Architecture**: MVVM (Model-View-ViewModel)
- **Concurrency**: Kotlin Coroutines & Flow
- **Data Persistence**: Kotlin Serialization (JSON)
- **Design System**: Material 3

## 📦 Installation & Setup

### 📥 Download the APK (Recommended)
You can download the latest pre-built version of the app directly from the [Releases](https://github.com/[Your-GitHub-Username]/MultiAppShare/releases) page. 

1. Download the `app-debug.apk` (or `app-release.apk`) to your Android device.
2. Open the file to install.
3. If prompted, allow "Install from unknown sources" in your device settings.

### 💻 Build from Source (Optional)
If you prefer to build the app yourself:
1. Clone the repository:
   ```bash
   git clone https://github.com/[Your-GitHub-Username]/MultiAppShare.git
   ```
2. Open the project in [Android Studio Ladybug](https://developer.android.com/studio) or newer.
3. Build and run the app on your device.

## 📖 How to Use

1. **Create a Group**: Tap the "Add Group" button and give it a name.
2. **Add Apps**: Open the group menu (three dots) and select "Modify Apps" to add your favorite sharing targets.
3. **Reorder**: Use "Reorder Apps" in the menu to set your preferred sharing sequence.
4. **Start Sharing**: 
   - Open any other app (e.g., Photos, YouTube, Chrome).
   - Tap the system **Share** button.
   - Select **Multi App Share** from the list.
   - Choose the target group.
   - Follow the "Next App" prompts until finished.

## 🤝 Support the Developer

If you find this tool useful, consider supporting the development!

- **Telegram**: [@EdwardLeeThompson](https://t.me/EdwardLeeThompson)
- **Donate**: [Venmo](https://venmo.com/code?user_id=1857304970395648420)

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
