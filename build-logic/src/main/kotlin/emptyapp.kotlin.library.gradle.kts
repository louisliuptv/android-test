import com.example.emptyapp.buildlogic.catalog
import com.example.emptyapp.buildlogic.library
import org.gradle.api.JavaVersion

plugins {
    id("org.jetbrains.kotlin.jvm")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    add("implementation", catalog.library("kotlinx-coroutines-core"))
}
