object App {
    const val minSdkVersion = 23
    const val targetSdkVersion = 29
    const val compileSdkVersion = 29
    const val buildToolsVersion = "29.0.2"
}

const val kotlinVersion = "1.3.70"
const val gradleBuildToolVersion = "3.5.3"

object BuildPlugins {

    const val androidLibrary = "com.android.library"
    const val androidApplication = "com.android.application"
    const val kotlinAndroid = "kotlin-android"
    const val kotlinAndroidExt = "kotlin-android-extensions"
    const val kotlinKapt = "kotlin-kapt"

    const val kotlinStdLib = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlinVersion"
    const val gradleBuildTool = "com.android.tools.build:gradle:$gradleBuildToolVersion"
    const val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion"
}

object Version {
    const val room = "2.2.5"
}

object Libs {
    private object Versions {
        const val coreKtx = "1.2.0"
        const val room = Version.room
        const val appCompat = "1.1.0"
        const val constraintLayout = "1.1.3"

    }

    const val coreKtx = "androidx.core:core-ktx:${Versions.coreKtx}"

    const val roomRuntime = "androidx.room:room-runtime:${Versions.room}"
    const val roomCompiler = "androidx.room:room-compiler:${Versions.room}"
    const val roomKtx = "androidx.room:room-ktx:${Versions.room}"
    const val roomRx = "androidx.room:room-rxjava2:${Version.room}"

    const val appCompat = "androidx.appcompat:appcompat:${Versions.appCompat}"
    const val constraintLayout =
        "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"

}

object TestLibs {

    private object Versions {
        const val junit = "4.12"
        const val robolectric = "4.3"
        const val testRules = "1.2.0"
        const val testCore = "1.2.0"
        const val archCoreTesting = "2.1.0"
        const val room = Version.room
    }

    const val room = "androidx.room:room-testing:${Versions.room}"
    const val robolectric = "org.robolectric:robolectric:${Versions.robolectric}"
    const val testRules = "androidx.test:rules:${Versions.testRules}"
    const val archCoreTesting = "androidx.arch.core:core-testing:${Versions.archCoreTesting}"
    const val testCore = "androidx.test:core:${Versions.testCore}"
    const val junit = "junit:junit:${Versions.junit}"

}

object InstrumentationTestLibs {
    private object Versions {
        const val junit = "1.1.1"
        const val espresso = "3.2.0"
    }

    const val junit = "androidx.test.ext:junit:${Versions.junit}"
    const val espressoCore = "androidx.test.espresso:espresso-core:${Versions.espresso}"
}

