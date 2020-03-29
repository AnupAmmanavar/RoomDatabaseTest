plugins {
    id(BuildPlugins.androidLibrary)
    kotlin("android")
    id(BuildPlugins.kotlinAndroid)
    id(BuildPlugins.kotlinAndroidExt)
    id(BuildPlugins.kotlinKapt)
    kotlin("kapt")
}

android {
    compileSdkVersion(App.compileSdkVersion)

    defaultConfig {
        minSdkVersion(App.minSdkVersion)
        targetSdkVersion(App.targetSdkVersion)
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }
}

dependencies {
    implementation(BuildPlugins.kotlinStdLib)
    implementation(Libs.coreKtx)
    testImplementation(TestLibs.junit)

    implementation(Libs.roomRuntime)
    kapt(Libs.roomCompiler)
    implementation(Libs.roomKtx)
    testImplementation(TestLibs.room)

    implementation(Libs.roomRx)

    testImplementation(TestLibs.robolectric)
    testImplementation(TestLibs.testRules)
    testImplementation(TestLibs.testCore)
    testImplementation(TestLibs.archCoreTesting)

}
