package dev.panini.derivation

import dev.panini.ashtadhyayi.adhyaya7.pada2.KitiCaSutra
import dev.panini.ashtadhyayi.adhyaya7.pada2.TaddhitesvAcamAdehSutra
import dev.panini.core.*
import kotlin.test.*

class TaddhitaIdentityVarnaTest {
    @Test fun `source affix and substitute identities preserve the previous exact domain`() {
        val identities: List<TypedAffix> = TaddhitaAffix.entries + TaddhitaAdesha.entries
        assertEquals(13, identities.size)
        assertEquals(13, identities.map { it.upadesha }.distinct().size)
        for (identity in identities) {
            for ((rule, marker) in listOf(TaddhitesvAcamAdehSutra to ItMarker.NIT, KitiCaSutra to ItMarker.KIT)) {
                val stem = DerivationTerm("stem", "गँर्गिँऽ", TermKind.PRATIPADIKA, upadesha = "गर्गि", createdBySutra = "fixture")
                val affix = identity.term("affix").copy(surface = "इ", itMarkers = setOf(marker))
                val original = DerivationState(listOf(stem, affix))
                assertTrue(rule.matches(original), identity.upadesha)
                val result = rule.apply(original).state
                val changed = result.terms.first()
                assertEquals("गाँर्गिँऽ", changed.surface)
                assertEquals(stem.id, changed.id)
                assertEquals(stem.upadesha, changed.upadesha)
                assertEquals(stem.createdBySutra, changed.createdBySutra)
                assertEquals(affix, result.terms.last())
                assertEquals(1, result.substitutions.single().sourceVarnaIndex)
                assertFalse(rule.matches(result))
            }
        }
    }

    @Test fun `a krt identity cannot become taddhita through a suggestive id`() {
        val original = DerivationState(listOf(DerivationTerm("stem", "नर", TermKind.PRATIPADIKA),
            KrtAffix.GHAN.term("taddhita").copy(itMarkers = setOf(ItMarker.NIT, ItMarker.KIT))))
        assertFalse(TaddhitesvAcamAdehSutra.matches(original))
        assertFalse(KitiCaSutra.matches(original))
    }
}
