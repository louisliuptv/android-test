plugins {
    id("emptyapp.android.library")
    id("emptyapp.android.hilt")
    id("emptyapp.kotlin.serialization")
}

android {
    namespace = "com.example.emptyapp.core.network"

    defaultConfig {
        buildConfigField("String", "BASE_URL", "\"https://ios-gp-fake-booking-api.vercel.app/\"")
    }

    buildTypes {
        debug {
            buildConfigField("String", "BASE_URL", "\"https://ios-gp-fake-booking-api.vercel.app/\"")
        }
        release {
            buildConfigField("String", "BASE_URL", "\"https://ios-gp-fake-booking-api.vercel.app/\"")
        }
    }
}

dependencies {
    api(project(":core:common"))

    api(libs.retrofit)
    api(libs.okhttp)
    implementation(libs.retrofit.kotlinx.serialization)
    implementation(libs.okhttp.logging)
    implementation(libs.kotlinx.coroutines.android)

    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.turbine)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.okhttp.mockwebserver)
}
