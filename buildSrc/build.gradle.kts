plugins {
    `kotlin-dsl`
    kotlin("jvm") version "2.3.0"
}

repositories {
    mavenCentral()
    gradlePluginPortal()
    maven("https://maven.kikugie.dev/snapshots")
}

dependencies {
    fun plugin(id: String, version: String) = "$id:$id.gradle.plugin:$version"
    //FIXME: This is hardcoded! Bad!
    implementation("dev.kikugie:stonecutter:0.9.4")
    implementation(plugin("me.modmuss50.mod-publish-plugin", "2.0.0-beta.2"))

    implementation("org.ow2.asm:asm:9.9")
    implementation("org.ow2.asm:asm-tree:9.9")
}