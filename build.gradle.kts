buildscript {
    dependencies {
        classpath("com.google.gms:google-services:4.3.14")
    }
}
plugins {
    id("com.android.application") version "8.1.1" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    id("com.android.library") version "7.4.2" apply false
}