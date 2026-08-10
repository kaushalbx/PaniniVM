package dev.panini.plugin.run

import java.io.File
import java.net.URI

/** Builds and starts the isolated CLI process used by IntelliJ Run configurations. */
internal class PvmCliProcessLauncher(
    private val javaHome: String = System.getProperty("java.home"),
    private val classpathProvider: () -> String = ::pluginRuntimeClasspath,
) {
    fun command(script: File): List<String> = listOf(
        File(javaHome, "bin/java").absolutePath,
        "-Dfile.encoding=UTF-8",
        "-cp",
        classpathProvider(),
        "dev.panini.MainKt",
        "--eval",
        script.absolutePath,
    )

    fun start(script: File, workingDirectory: File): Process = ProcessBuilder(command(script))
        .directory(workingDirectory)
        .redirectErrorStream(true)
        .start()

    private companion object {
        fun pluginRuntimeClasspath(): String {
            val entries = linkedSetOf<String>()
            System.getProperty("java.class.path")
                ?.split(File.pathSeparatorChar)
                ?.filter(String::isNotBlank)
                ?.let(entries::addAll)

            var loader: ClassLoader? = PvmCliProcessLauncher::class.java.classLoader
            while (loader != null) {
                runCatching {
                    val method = loader.javaClass.methods.firstOrNull { it.name == "getUrls" && it.parameterCount == 0 }
                    val urls = when (val value = method?.invoke(loader)) {
                        is Iterable<*> -> value.toList()
                        is Array<*> -> value.toList()
                        else -> emptyList()
                    }
                    urls.mapNotNull { value ->
                        value ?: return@mapNotNull null
                        runCatching { File(URI(value.toString())).absolutePath }.getOrNull()
                    }.let(entries::addAll)
                }
                loader = loader.parent
            }

            listOf("dev.panini.MainKt", "dev.panini.cli.PaniniCli").forEach { className ->
                runCatching {
                    Class.forName(className).protectionDomain.codeSource.location.toURI()
                }.map { File(it).absolutePath }.getOrNull()?.let(entries::add)
            }
            check(entries.isNotEmpty()) { "Unable to resolve the PaniniVM plugin runtime classpath." }
            return entries.joinToString(File.pathSeparator)
        }
    }
}
