import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

fun com.android.build.api.dsl.ApkSigningConfig.setupSigningConfig(fileName: String) {
    val properties = Properties().apply {
        load(File(fileName).reader())
    }
    keyAlias = properties.getProperty("keyAlias")
    keyPassword = properties.getProperty("keyPassword")
    storeFile = file(properties.getProperty("storeFile"))
    storePassword = properties.getProperty("storePassword")
}

android {
    namespace = "com.example.demofirebasedistribution"
    compileSdk = 34

    signingConfigs {
        create("staging") {
//            setupSigningConfig("./keystore-staging.properties")
        }

        create("dev") {
//            setupSigningConfig("./keystore-dev.properties")
        }
    }

    defaultConfig {
        applicationId = "com.example.demofirebasedistribution"
        minSdk = 24
        targetSdk = 34
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isDebuggable = false
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        debug {
            isDebuggable = true
            isMinifyEnabled = false
        }
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }

    flavorDimensions += "env"
    productFlavors {
        val dev by creating {
            dimension = "env"
            applicationIdSuffix = ".dev"
            versionCode = 1
            versionNameSuffix = "-dev${versionCode.toString().padStart(2, '0')}"
            signingConfig = signingConfigs.getByName("dev")
        }

        val staging by creating {
            dimension = "env"
            applicationIdSuffix = ".staging"
            versionCode = 1
            versionNameSuffix = "-staging${versionCode.toString().padStart(2, '0')}"
            signingConfig = signingConfigs.getByName("staging")
        }

        val prod by creating {
            dimension = "env"
        }
    }
}

dependencies {
    coreLibraryDesugaring(libs.desugar.jdk.libs)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
