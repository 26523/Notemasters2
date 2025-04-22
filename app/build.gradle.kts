plugins {
    alias(libs.plugins.android.application)
    kotlin("android") version "1.9.0"  // 如果用了Kotlin
}

android {
    namespace = "net.micode.notes"
    compileSdk = 35

    defaultConfig {
        applicationId = "net.micode.notes"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    // 修复后的fileTree
    implementation(fileTree("libs") {
        include("*.aar", "*.jar")
    })

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
dependencies {
    // 新增依赖
    implementation("androidx.work:work-runtime-ktx:2.9.0")  // WorkManager
    implementation("com.squareup.okhttp3:okhttp:4.12.0")    // 网络请求
    implementation("com.google.code.gson:gson:2.10.1")         // JSON解析
    implementation("androidx.core:core-ktx:1.12.0")          // Kotlin扩展
}
