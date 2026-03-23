plugins {
    id("com.android.library")
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.hilt)
    id("kotlin-kapt")
}

android {
    namespace = "com.multiappshare.core.database"
    compileSdk = 36

    defaultConfig {
        minSdk = 26
        consumerProguardFiles("consumer-rules.pro")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    kotlinOptions {
        jvmTarget = "21"
    }

    lint {
        abortOnError = false
        checkReleaseBuilds = false
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    kapt(libs.androidx.room.compiler)
    implementation(libs.kotlinx.serialization.json)
    
    // We may need model dependencies if it's placed in a separate module later.
    // For now, it will use existing model which is currently in app/model folder.
    // Wait, if we move Database without moving Model, we cannot import Model!
    // So Model must ALSO be moved or made a module, or placed in core-database!
    // The previous agent already added @Entity to AppGroup.
    // So AppGroup MUST go to core-database or a shared core-model module!
    // I'll assume we move Model to core-domain or core-database, or leave it in an accessible layer.
}



// Force kotlin-stdlib and related libraries to 2.0.21 to resolve Room metadata version conflict.
configurations.all {
    resolutionStrategy {
        force("org.jetbrains.kotlin:kotlin-stdlib:2.0.21")
        force("org.jetbrains.kotlin:kotlin-stdlib-jdk8:2.0.21")
        force("org.jetbrains.kotlin:kotlin-stdlib-jdk7:2.0.21")
        force("org.jetbrains.kotlin:kotlin-reflect:2.0.21")
    }
}
