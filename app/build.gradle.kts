plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id ("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.capa1.switchcontrol"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.capa1.switchcontrol"
        minSdk = 29
        targetSdk = 36
        versionCode = 19
        versionName = "1.19"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    // store
    implementation (libs.androidx.datastore.preferences)
    //extra icons
    implementation(libs.androidx.material.icons.extended.android)
    //di
    implementation (libs.hilt.android)
    kapt (libs.hilt.android.compiler)
    //kapt ("androidx.hilt:hilt-compiler:1.2.0")
    implementation (libs.androidx.hilt.navigation.compose)
    //mqtt
    implementation (libs.org.eclipse.paho.client.mqttv3)
    implementation (libs.org.eclipse.paho.android.service)
    //json
    implementation (libs.gson)
    //Permissions
    implementation (libs.accompanist.permissions)
    //esptouch
    implementation (libs.lib.esptouch.android)
    implementation (libs.lib.esptouch.v2.android)
}
// Allow references to generated code
kapt {
    correctErrorTypes = true
}