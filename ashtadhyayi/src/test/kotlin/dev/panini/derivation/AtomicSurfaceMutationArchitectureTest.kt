package dev.panini.derivation

import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.extension
import kotlin.io.path.isDirectory
import kotlin.io.path.readText
import kotlin.test.Test
import kotlin.test.assertTrue

class AtomicSurfaceMutationArchitectureTest {
    @Test
    fun `executable grammar rules use atomic surface mutation APIs`() {
        val sourceRoot = sequenceOf(
            Path.of("ashtadhyayi", "src", "main", "kotlin"),
            Path.of("src", "main", "kotlin"),
        ).firstOrNull { it.isDirectory() } ?: error("Cannot locate the Aṣṭādhyāyī source tree.")

        val violations = Files.walk(sourceRoot).use { paths ->
            val lifecycleOwners = setOf(
                "AdyantauTakitauSutra.kt",
                "MidacoAntyatParahSutra.kt",
                "TasyaLopahSutra.kt",
            )
            paths.filter { it.extension == "kt" }
                .filter { it.fileName.toString() !in lifecycleOwners }
                .map { path -> path to path.readText() }
                .filter { (_, source) ->
                    Regex("""copy\s*\(\s*surface\s*=""").containsMatchIn(source)
                }
                .map { (path, _) -> sourceRoot.relativize(path).toString() }
                .sorted()
                .toList()
        }

        assertTrue(
            violations.isEmpty(),
            "Surface changes must use substituteTermSurface, mergeTermsByVarnaSubstitution, " +
                "redistributeAdjacentTermsByVarnaSubstitution, or replaceWholeAffix: $violations",
        )
    }
}
