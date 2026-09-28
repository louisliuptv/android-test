import com.example.emptyapp.buildlogic.catalog
import com.example.emptyapp.buildlogic.library

plugins {
    id("org.jetbrains.kotlin.plugin.compose")
}

pluginManager.withPlugin("com.android.library") {
    extensions.configure<com.android.build.api.dsl.LibraryExtension> {
        buildFeatures {
            compose = true
        }
    }
}

pluginManager.withPlugin("com.android.application") {
    extensions.configure<com.android.build.api.dsl.ApplicationExtension> {
        buildFeatures {
            compose = true
        }
    }
}

dependencies {
    val bom = platform(catalog.library("androidx-compose-bom"))
    add("implementation", bom)
    add("androidTestImplementation", bom)

    add("implementation", catalog.library("androidx-compose-ui"))
    add("implementation", catalog.library("androidx-compose-ui-graphics"))
    add("implementation", catalog.library("androidx-compose-ui-tooling-preview"))
    add("implementation", catalog.library("androidx-compose-material3"))

    add("debugImplementation", catalog.library("androidx-compose-ui-tooling"))
    add("androidTestImplementation", catalog.library("androidx-compose-ui-test-junit4"))
    add("debugImplementation", catalog.library("androidx-compose-ui-test-manifest"))
}
