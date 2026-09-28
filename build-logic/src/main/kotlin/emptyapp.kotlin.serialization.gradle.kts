import com.example.emptyapp.buildlogic.catalog
import com.example.emptyapp.buildlogic.library

plugins {
    id("org.jetbrains.kotlin.plugin.serialization")
}

dependencies {
    add("implementation", catalog.library("kotlinx-serialization-json"))
}
