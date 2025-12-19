import gradle.kotlin.dsl.accessors._bb7407e43c252c8005654a0b9f579d81.base

plugins {
    id("java")
    id("idea")
    id("multiloader-common")
    id("com.modrinth.minotaur")
}

val commonJava: Configuration by configurations.creating {
    isCanBeResolved = true
}
val commonResources: Configuration by configurations.creating {
    isCanBeResolved = true
}

base {
    archivesName.set("${commonMod.id}-$loader")
}

dependencies {
    val commonPath = common.hierarchy.toString()
    compileOnly(project(path = commonPath))
    commonJava(project(path = commonPath, configuration = "commonJava"))
    commonResources(project(path = commonPath, configuration = "commonResources"))
}

modrinth {
    token.set(System.getenv("MODRINTH_TOKEN"))
    projectId.set(commonMod.prop("modrinth_project_id"))
    versionName.set("Moderner Beta " + commonMod.version)
    versionNumber.set(version.toString())
    gameVersions.addAll(commonMod.prop("supported_versions").split(",").toList())
    changelog.set(rootProject.file("CHANGELOG.md").readText())
    loaders.addAll(supported_loaders!!)
}

tasks {
    compileJava {
        dependsOn(commonJava)
        source(commonJava)
    }

    processResources {
        dependsOn(commonResources)
        from(commonProject.file("src/main/generated")) {
            include("**/**")
            exclude(".cache")
        }

        from(commonProject.tasks.processResources.map { it.outputs }) {
            filesMatching("**/*.nbt") {
                duplicatesStrategy = DuplicatesStrategy.EXCLUDE
            }
        }
    }

    withType<Jar> {
        destinationDirectory = rootProject.layout.buildDirectory.dir("libs/$loader")
    }
}