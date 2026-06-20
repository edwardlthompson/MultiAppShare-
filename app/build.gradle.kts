import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.hilt)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.androidx.baselineprofile)
    alias(libs.plugins.paparazzi)
    alias(libs.plugins.ksp)

}



val appVersion = "1.9.0"

/** Local signing: copy `keystore.properties.example` → `keystore.properties` (gitignored). Env vars override file. */
val keystoreProperties = Properties().apply {
    val f = rootProject.file("keystore.properties")
    if (f.isFile) {
        f.inputStream().use { load(it) }
    }
}

android {
    namespace = "com.multiappshare"
    compileSdk = 37
    defaultConfig {
        applicationId = "com.edwardlthompson.multiappshare"
        minSdk = 26
        targetSdk = 36
        versionCode = 176
        versionName = "1.9.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        create("release") {
            val storeRel = System.getenv("RELEASE_KEYSTORE_PATH")
                ?: keystoreProperties.getProperty("storeFile")
                ?: "release.keystore"
            val keystoreFile = rootProject.file(storeRel)
            if (keystoreFile.isFile) {
                storeFile = keystoreFile
                storePassword = System.getenv("RELEASE_KEYSTORE_PASSWORD")
                    ?: keystoreProperties.getProperty("storePassword").orEmpty()
                keyAlias = System.getenv("RELEASE_KEY_ALIAS")
                    ?: keystoreProperties.getProperty("keyAlias").orEmpty()
                keyPassword = System.getenv("RELEASE_KEY_PASSWORD")
                    ?: keystoreProperties.getProperty("keyPassword").orEmpty()
            }
        }
    }

    buildTypes {
        debug {
            // O.4: pseudo-locales (en-XA, ar-XB) in Developer options for truncation / RTL smoke
            isPseudoLocalesEnabled = true
        }
        release {
            val releaseSigning = signingConfigs.getByName("release")
            val signingReady = releaseSigning.storeFile?.let { it.isFile } == true &&
                !releaseSigning.storePassword.isNullOrBlank() &&
                !releaseSigning.keyAlias.isNullOrBlank() &&
                !releaseSigning.keyPassword.isNullOrBlank()
            if (signingReady) {
                signingConfig = releaseSigning
            }
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    
    lint {
        abortOnError = true
        checkReleaseBuilds = false
    }


    buildFeatures {
        compose = true
        buildConfig = true
    }

}

baselineProfile {
    // Merged under app/src/main/generated/baselineProfiles/; regenerate: `./gradlew :app:generateBaselineProfile`
    mergeIntoMain = true
}



// Custom APK naming for v1.8.0
base {
   archivesName.set("MultiAppShare-v$appVersion")
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    ksp(libs.kotlin.metadata.jvm)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)
    implementation(libs.hilt.navigation.compose)
    implementation(libs.coil.compose)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.accompanist.drawablepainter)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.timber)
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.robolectric)
    testImplementation(libs.androidx.test.core)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.test.uiautomator)
    androidTestImplementation(libs.androidx.test.core)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.leakcanary.android)

    // Independent Modules
    implementation(project(":core-database"))
    implementation(project(":core-domain"))
    implementation(project(":core-ui"))
    implementation(project(":feature-dashboard"))

    baselineProfile(project(":baselineprofile"))
}

tasks.whenTaskAdded {
    if (name.contains("CheckAarMetadata", ignoreCase = true)) {
        enabled = false
    }
}

composeCompiler {
    reportsDestination = layout.buildDirectory.dir("compose_compiler")
}

tasks.withType<Test>().configureEach {
    reports.html.required.set(false)
}
