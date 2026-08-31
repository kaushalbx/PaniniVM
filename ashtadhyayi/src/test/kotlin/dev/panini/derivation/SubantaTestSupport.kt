package dev.panini.derivation

import dev.panini.core.Linga
import dev.panini.core.SupAffix
import kotlin.test.assertEquals

internal fun assertSubantaParadigm(
    stem: String,
    linga: Linga,
    expected: String,
    stemFormation: NominalStemFormation = NominalStemFormation.UNSPECIFIED,
) {
    val expectedSurfaces = expected.trim().split(Regex("\\s+"))
    assertEquals(SupAffix.entries.size, expectedSurfaces.size, "Expected one surface for each sup slot")

    val engine = DerivationEngine(dev.panini.ashtadhyayi.Ashtadhyayi.executableSutras)
    val results = SupAffix.entries.associateWith { affix ->
        engine.derive(
            SubantaDerivationRequest(stem, affix.vibhakti, affix.vacana, linga, stemFormation).initialState(),
        )
    }
    val expectedByAffix = SupAffix.entries.zip(expectedSurfaces).toMap()
    val actual = results.mapValues { it.value.final.surface }
    val traces = results.filter { (affix, result) -> expectedByAffix[affix] != result.final.surface }
        .entries.joinToString("\n\n") { (affix, result) ->
            "$affix:\n" + result.applications.joinToString("\n") {
                val terms = it.after.terms.joinToString { term ->
                    "${term.id}=${term.surface}[${term.upadesha};${term.itProcessingPhase}]"
                }
                "${it.sutra}: ${it.before.surface} -> ${it.after.surface} {$terms}"
            }
        }
    assertEquals(expectedByAffix, actual, traces)
}
