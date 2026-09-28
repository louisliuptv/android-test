plugins {
    id("emptyapp.android.library")
    id("emptyapp.android.compose")
    id("emptyapp.android.hilt")
}

android {
    namespace = "com.example.emptyapp.core.designsystem"
}

dependencies {
    api(project(":core:common"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.annotation)

    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.turbine)
    testImplementation(libs.kotlinx.coroutines.test)
}
