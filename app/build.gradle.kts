plugins {
    id(BuildPlugins.androidApplication)
    kotlin("android")
    id(BuildPlugins.kotlinAndroid)
    id(BuildPlugins.kotlinAndroidExt)

    kotlin("kapt")
}

android {
    compileSdkVersion(App.compileSdkVersion)

    defaultConfig {
        applicationId = "com.kinley.roomtestingdemo"
        minSdkVersion(App.minSdkVersion)
        targetSdkVersion(App.targetSdkVersion)
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

dependencies {

    implementation(project(":repository"))

    implementation(BuildPlugins.kotlinStdLib)
    implementation(Libs.appCompat)
    implementation(Libs.coreKtx)
    implementation(Libs.constraintLayout)

    testImplementation(TestLibs.junit)

    androidTestImplementation(InstrumentationTestLibs.junit)
    androidTestImplementation(InstrumentationTestLibs.espressoCore)
}
