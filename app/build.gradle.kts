plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "comp3350.reshop"
    compileSdk = 34

    defaultConfig {
        applicationId = "comp3350.reshop"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(libs.guava)
    implementation(group = "org.hsqldb", name = "hsqldb", version = "2.4.1")
    testImplementation(group = "org.mockito", name = "mockito-core", version = "4.+")
    androidTestImplementation("androidx.test.espresso:espresso-contrib:3.6.1")

}
