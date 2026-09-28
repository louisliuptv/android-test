import com.example.emptyapp.buildlogic.catalog
import com.example.emptyapp.buildlogic.library

plugins {
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
}

dependencies {
    add("implementation", catalog.library("hilt-android"))
    add("ksp", catalog.library("hilt-compiler"))
}
