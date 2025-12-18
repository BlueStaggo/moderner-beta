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
    implementation(plugin("com.modrinth.minotaur", "2.+"))
}