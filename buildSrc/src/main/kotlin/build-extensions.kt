import dev.kikugie.stonecutter.build.StonecutterBuildExtension
import dev.kikugie.stonecutter.controller.StonecutterControllerExtension
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.kotlin.dsl.*

val Project.mod: ModData get() = ModData(this)
fun Project.prop(key: String): String? = findProperty(key)?.toString()
fun String.upperCaseFirst() = replaceFirstChar { if (it.isLowerCase()) it.uppercaseChar() else it }

fun RepositoryHandler.strictMaven(url: String, alias: String, vararg groups: String) = exclusiveContent {
    forRepository { maven(url) { name = alias } }
    filter { groups.forEach(::includeGroup) }
}

val Project.stonecutterBuild get() = extensions.getByType<StonecutterBuildExtension>()
val Project.stonecutterController get() = extensions.getByType<StonecutterControllerExtension>()

val Project.common get() = requireNotNull(stonecutterBuild.node.sibling("")) {
    "No common project for $project"
}
val Project.commonProject get() = rootProject.project(stonecutterBuild.current.project)
val Project.commonMod get() = commonProject.mod

val Project.loader: String? get() = prop("loader")
val Project.supported_loaders: List<String>? get() = prop("supported_loaders")?.split(",")

@JvmInline
value class ModData(private val project: Project) {
    val id: String get() = prop("archives_base_name")
    val version: String get() = prop("mod_version")
    val mc: String get() = propOrNull("minecraft_version_override") ?: project.stonecutterBuild.current.version

    fun propOrNull(key: String): String? = project.prop(key)
    fun prop(key: String): String = requireNotNull(propOrNull(key)) { "Missing '$key'" }
}