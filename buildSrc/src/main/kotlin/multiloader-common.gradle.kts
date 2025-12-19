plugins {
    id("java")
    id("idea")
    id("java-library")
}

version = "${commonMod.version}+${commonMod.mc}"

base {
    archivesName.set(commonMod.id)
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(commonProject.prop("java_version")!!)
    // withSourcesJar()
    // withJavadocJar()
}

repositories {
    mavenCentral()
    exclusiveContent {
        forRepository {
            maven("https://repo.spongepowered.org/repository/maven-public") { name = "Sponge" }
        }
        filter { includeGroupAndSubgroups("org.spongepowered") }
    }
    exclusiveContent {
        forRepositories(
            maven("https://maven.parchmentmc.org") { name = "ParchmentMC" },
            maven("https://maven.neoforged.net/releases") { name = "NeoForge" },
            maven("https://maven.minecraftforge.net/") { name = "MinecraftForge" }
        )
        filter { includeGroup("org.parchmentmc.data") }
    }
    maven("https://maven.terraformersmc.com/releases/") { name = "TerraformersMC" }
    maven("https://maven.kikugie.dev/releases") { name = "KikuGie Releases" }
    maven("https://maven.kikugie.dev/snapshots") { name = "KikuGie Snapshots" }
}

tasks {
    processResources {
        val expandProps = mapOf(
            "version" to version as String,
            "java_version" to commonMod.propOrNull("java_version"),
            "forgelike_loader_version_range" to commonMod.propOrNull("forgelike_loader_version_range"),
            "forgelike_minecraft_version_range" to commonMod.propOrNull("forgelike_minecraft_version_range"),
            "fabric_minecraft_version_range" to commonMod.propOrNull("fabric_minecraft_version_range"),
            "loader" to loader
        ).filterValues { it?.isNotEmpty() == true }.mapValues { (_, v) -> v!! }

        val jsonExpandProps = expandProps.mapValues { (_, v) -> v.replace("\n", "\\\\n") }

        filesMatching(listOf("META-INF/mods.toml", "META-INF/neoforge.mods.toml")) {
            expand(expandProps)
        }

        filesMatching(listOf("pack.mcmeta", "fabric.mod.json", "*.mixins.json")) {
            expand(jsonExpandProps)
        }

        if (project.stonecutterBuild.eval(project.stonecutterBuild.current.version, "<1.21")) {
            filesMatching("data/moderner_beta/structure/**/*") {
                path = path.replace("/structure/", "/structures/")
            }
        }

        inputs.properties(expandProps)
    }
}