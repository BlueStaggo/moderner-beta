plugins {
    `kotlin-dsl`
    kotlin("jvm") version "2.2.0"
}

repositories {
    mavenCentral()
    gradlePluginPortal()
    maven("https://maven.kikugie.dev/snapshots")
}

dependencies {
    fun plugin(id: String, version: String) = "$id:$id.gradle.plugin:$version"
    //FIXME: This is hardcoded! Bad!
    implementation("dev.kikugie:stonecutter:0.8-alpha.10")

    implementation(plugin("org.jetbrains.kotlin.jvm", "2.2.0"))
    implementation(plugin("com.google.devtools.ksp", "2.2.0-2.0.2"))
    implementation(plugin("dev.kikugie.fletching-table", "0.1.0-alpha.22"))
}