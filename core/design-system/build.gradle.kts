plugins {
    id("emptyapp.android.library")
    id("emptyapp.android.compose")
}

android {
    namespace = "com.example.emptyapp.core.designsystem"
}

dependencies {
    implementation(libs.androidx.core.ktx)

    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.turbine)
    testImplementation(libs.kotlinx.coroutines.test)
}
