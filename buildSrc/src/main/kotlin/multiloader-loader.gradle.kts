plugins {
    id("java")
    id("idea")
    id("multiloader-common")
//    id("com.modrinth.minotaur") version "2.+"
}

val commonJava: Configuration by configurations.creating {
    isCanBeResolved = true
}
val commonResources: Configuration by configurations.creating {
    isCanBeResolved = true
}

dependencies {
    val commonPath = common.hierarchy.toString()
    compileOnly(project(path = commonPath))
    commonJava(project(path = commonPath, configuration = "commonJava"))
    commonResources(project(path = commonPath, configuration = "commonResources"))
}

/*modrinth {
    token = System.getenv("MODRINTH_TOKEN")
    projectId = commonMod.prop("modrinth_project_id")
    versionName = "Moderner Beta " + commonMod.version
    versionNumber = commonMod.version
    gameVersions = commonMod.prop("supported_versions").split(",").toList()
    changelog = rootProject.file("CHANGELOG.md").text
    loaders = supported_loaders
    uploadFile = tasks.remapJar
}*/

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

        from(commonResources) {
            filesMatching("**/*.nbt") {
                duplicatesStrategy = DuplicatesStrategy.EXCLUDE
            }
        }
    }
}