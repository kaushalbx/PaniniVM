package dev.panini.derivation

import dev.panini.core.SupAffix
import kotlin.test.assertEquals

internal fun assertSubantaParadigm(
    stem: String,
    stemClass: SubantaStemClass,
    expected: String,
) {
    val expectedSurfaces = expected.trim().split(Regex("\\s+"))
    assertEquals(SupAffix.entries.size, expectedSurfaces.size, "Expected one surface for each sup slot")

    val engine = DerivationEngine(dev.panini.ashtadhyayi.Ashtadhyayi.executableSutras)
    val actual = SupAffix.entries.associateWith { affix ->
        engine.derive(
            SubantaDerivationRequest(stem, affix.vibhakti, affix.vacana, stemClass).initialState(),
        ).final.surface
    }
    assertEquals(SupAffix.entries.zip(expectedSurfaces).toMap(), actual)
}
