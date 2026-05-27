plugins {
    id("java")
    id("idea")
    id("multiloader-common")
    id("me.modmuss50.mod-publish-plugin")
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

publishMods {
    displayName = "Moderner Beta " + commonMod.version
    version = project.version.toString() + "-" + loader

    changelog = rootProject.file("CHANGELOG.md").readText()
    type = STABLE

    modLoaders.addAll(supported_loaders!!)

    modrinth {
        accessToken = System.getenv("MODRINTH_TOKEN")
        projectId = commonMod.prop("modrinth_project_id")
        minecraftVersions.addAll(commonMod.prop("supported_versions").split(",").toList())
    }

    curseforge {
        accessToken = System.getenv("CURSEFORGE_TOKEN")
        projectId = commonMod.prop("curseforge_project_id")
        minecraftVersions.addAll(commonMod.prop("supported_versions").split(",").toList())

        client = true
        server = true
    }

    github {
        accessToken = System.getenv("_GITHUB_TOKEN")
        parent(project(":").tasks.named("publishGithub"))
    }

    forgejo {
        accessToken = System.getenv("FORGEJO_TOKEN")
        parent(project(":").tasks.named("publishForgejo"))
    }
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