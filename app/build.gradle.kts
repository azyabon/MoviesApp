import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ksp)
}

val localProps = Properties()
val localProperties = File("local.properties")
if (localProperties.exists() && localProperties.isFile) {
    localProperties.inputStream().use {
        localProps.load(it)
    }
}

android {
    namespace = "com.azyabon.moviesapp"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.azyabon.moviesapp"
        minSdk = 24
        targetSdk = 36
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
            buildConfigField("String", "TMDB_API_KEY", "\"${localProps.getProperty("TMDB_API_KEY").orEmpty()}\"")
        }
        debug {
            buildConfigField("String", "TMDB_API_KEY", "\"${localProps.getProperty("TMDB_API_KEY").orEmpty()}\"")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    // Updated section
    kotlin {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
        }
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // Coil for image loading
    implementation(libs.coil.android)
    implementation(libs.coil.network.okhttp)

    // Hilt for Dependency Injection
    implementation(libs.hilt.android)

    // Annotation processor for Hilt
    ksp(libs.hilt.compiler)

    // Retrofit + Gson Converter
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    // OkHttp + Logging Interceptor
    implementation(libs.okhttp)
//    implementation(libs.logging.interceptor)

    // Lifecycle
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.fragment)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
