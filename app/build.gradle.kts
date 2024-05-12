plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id ("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.capa1.switchcontrol"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.capa1.switchcontrol"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

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
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation ("androidx.core:core-ktx:1.13.1")
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation ("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation ("androidx.compose.runtime:runtime-livedata:1.6.7")
    implementation ("androidx.activity:activity-compose:1.9.0")
    implementation (platform("androidx.compose:compose-bom:2023.08.00"))
    implementation ("androidx.compose.ui:ui")
    implementation ("androidx.compose.ui:ui-graphics")
    implementation ("androidx.compose.ui:ui-tooling-preview")
    implementation ("androidx.compose.material3:material3-android:1.2.1")
    testImplementation ("junit:junit:4.13.2")
    androidTestImplementation ("androidx.test.ext:junit:1.1.5")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation (platform("androidx.compose:compose-bom:2023.08.00"))
    androidTestImplementation ("androidx.compose.ui:ui-test-junit4")
    debugImplementation ("androidx.compose.ui:ui-tooling")
    debugImplementation ("androidx.compose.ui:ui-test-manifest")
    // store
    implementation ("androidx.datastore:datastore-core:1.1.1")
    implementation ("androidx.datastore:datastore-preferences-core:1.1.1")
    implementation ("androidx.datastore:datastore-preferences-rxjava2:1.1.1")
    implementation ("androidx.datastore:datastore-preferences-rxjava3:1.1.1")
    //extra icons
    implementation("androidx.compose.material:material-icons-extended-android:1.6.7")
    //di
    implementation ("com.google.dagger:hilt-android:2.48")
    kapt ("com.google.dagger:hilt-android-compiler:2.48")
    //kapt ("androidx.hilt:hilt-compiler:1.2.0")
    implementation ("androidx.hilt:hilt-navigation-compose:1.2.0")
    //mqtt
    implementation ("org.eclipse.paho:org.eclipse.paho.client.mqttv3:1.2.5")
    implementation ("org.eclipse.paho:org.eclipse.paho.android.service:1.1.1")
    //json
    implementation ("com.google.code.gson:gson:2.10")
    //Permissions
    implementation ("com.google.accompanist:accompanist-permissions:0.28.0")
    //esptouch
    implementation ("com.github.EspressifApp:lib-esptouch-android:1.1.1")
    implementation ("com.github.EspressifApp:lib-esptouch-v2-android:2.2.1")
}
// Allow references to generated code
kapt {
    correctErrorTypes = true
}