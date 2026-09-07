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
        val modules = listOf("ashtadhyayi", "derivation", "core")
        val sourceRoots = modules.mapNotNull { module ->
            sequenceOf(
                Path.of(module, "src", "main", "kotlin"),
                Path.of("..", module, "src", "main", "kotlin"),
                Path.of("src", "main", "kotlin").takeIf { module == "ashtadhyayi" },
            ).filterNotNull().firstOrNull { it.isDirectory() }
        }.distinct()
        require(sourceRoots.isNotEmpty()) { "Cannot locate the derivation source trees." }

        val lifecycleOwners = setOf(
            "AdyantauTakitauSutra.kt",
            "MidacoAntyatParahSutra.kt",
            "TasyaLopahSutra.kt",
            "DerivationState.kt",
            "DerivationEngine.kt",
        )
        val violations = sourceRoots.flatMap { sourceRoot ->
            Files.walk(sourceRoot).use { paths ->
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
        }

        assertTrue(
            violations.isEmpty(),
            "Surface changes must use substituteTermSurface, mergeTermsByVarnaSubstitution, " +
                "redistributeAdjacentTermsByVarnaSubstitution, or replaceWholeAffix: $violations",
        )
    }
}
