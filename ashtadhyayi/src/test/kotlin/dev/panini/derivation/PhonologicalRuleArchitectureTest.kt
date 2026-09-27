package dev.panini.derivation

import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.extension
import kotlin.io.path.isDirectory
import kotlin.io.path.readText
import kotlin.test.Test
import kotlin.test.assertTrue

class PhonologicalRuleArchitectureTest {
    @Test
    fun `sutras do not interpret surface text as phonological structure`() {
        val sourceRoot = sequenceOf(
            Path.of("ashtadhyayi", "src", "main", "kotlin"),
            Path.of("..", "ashtadhyayi", "src", "main", "kotlin"),
            Path.of("src", "main", "kotlin"),
        ).first { it.isDirectory() }
        val orthographicLifecycleOwners = setOf(
            "AdyantauTakitauSutra.kt",
            "AyaneyInIyiyahSutra.kt",
            "HalantyamSutra.kt",
            "MidacoAntyatParahSutra.kt",
            "TasyaLopahSutra.kt",
            "UpadesheAjanunasikaItSutra.kt",
        )
        val forbidden = listOf(
            Regex("""\.surface\??\.(?:startsWith|endsWith|last|dropLast|substring|take|drop|contains|replace|indexOf|removeSuffix|removePrefix|trimEnd|dropWhile)\s*\("""),
            Regex("""\.surface\s*\["""),
            Regex("""\.surface\s*\+\s*["]"""),
            Regex("""\.surface\.toVarnas\s*\("""),
            Regex("""Varnamala\.(?:isVowel|isConsonant|fromMatra|endsWithA)\s*\("""),
        )

        val sutraPaths = Files.walk(sourceRoot).use { paths ->
            paths.filter { it.extension == "kt" && it.fileName.toString().endsWith("Sutra.kt") }
                .filter { it.fileName.toString() !in orthographicLifecycleOwners }
                .toList()
        }
        val violations = sutraPaths.flatMap { path ->
            path.readText().lineSequence().mapIndexedNotNull { index, line ->
                if (forbidden.any { it.containsMatchIn(line) }) {
                    "${sourceRoot.relativize(path)}:${index + 1}: ${line.trim()}"
                } else null
            }
        }.sorted()

        assertTrue(
            violations.isEmpty(),
            "Sūtras must reason over DerivationTerm.varnas; only explicit orthographic lifecycle owners may inspect written spans:\n" +
                violations.joinToString("\n"),
        )
    }
}
