plugins {
    id("com.android.application")
}

android {
    namespace = "com.capa1.esptouch"
    compileSdk = 34
    defaultConfig {
        applicationId = "com.capa1.esptouch"
        minSdk = 27
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        //val ESPTOUCH_VERSION: String = BuildConfig.VERSION_NAME
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}
