package dev.panini.derivation

import dev.panini.ashtadhyayi.adhyaya1.pada2.AsamyogallitKitSutra
import dev.panini.ashtadhyayi.adhyaya3.pada1.SibbahulamLetiSutra
import dev.panini.ashtadhyayi.adhyaya3.pada4.LetodatauSutra
import dev.panini.ashtadhyayi.adhyaya7.pada3.AtoDirghoYaniSutra
import dev.panini.core.Lakara
import dev.panini.core.TingAffix
import dev.panini.ashtadhyayi.adhyaya3.pada4.TiptasjhiSutra
import dev.panini.core.Purusha
import dev.panini.core.Vacana
import kotlin.test.assertEquals
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class TingDomainProvenanceTest {
    @Test
    fun `lakara substitution establishes the selected typed slot`() {
        val original = state(DerivationTerm("lakara", "लट्", TermKind.PRATYAYA), Lakara.LAT)
        val requested = original.copy(context = DerivationalContext(rupa = Rupa(
            lakara = Lakara.LAT, purusha = Purusha.PRATHAMA, vacana = Vacana.EKAVACANA,
        )))
        assertTrue(TiptasjhiSutra.matches(requested))
        val selected = TiptasjhiSutra.apply(requested).state.terms.last()
        assertEquals(TingAffix.TIP, selected.sourceTingAffix)
    }

    private fun state(ending: DerivationTerm, lakara: Lakara) = DerivationState(
        terms = listOf(DerivationTerm("root", "भव", TermKind.DHATU), ending),
        stage = DerivationStage.IT_PROCESSED,
        activeAdhikaras = setOf("6.4.1"),
        context = DerivationalContext(rupa = Rupa(lakara = lakara)),
    )

    @Test
    fun `lit apit classification follows typed slot after renaming`() {
        assertTrue(AsamyogallitKitSutra.matches(state(TingAffix.TAS.term().copy(id = "renamed"), Lakara.LIT)))
        for (pit in listOf(TingAffix.TIP, TingAffix.SIP, TingAffix.MIP)) {
            assertFalse(AsamyogallitKitSutra.matches(state(pit.term().copy(id = "renamed"), Lakara.LIT)))
        }
        assertFalse(AsamyogallitKitSutra.matches(state(DerivationTerm("ting-fake", "तस्", TermKind.PRATYAYA), Lakara.LIT)))
    }

    @Test
    fun `lengthening requires a typed ting not a suggestive identifier`() {
        assertTrue(AtoDirghoYaniSutra.matches(state(TingAffix.MAS.term().copy(id = "renamed"), Lakara.LAT)))
        assertFalse(AtoDirghoYaniSutra.matches(state(DerivationTerm("ting-fake", "मस्", TermKind.PRATYAYA), Lakara.LAT)))
    }

    @Test
    fun `let rules retain their formation and readiness guards`() {
        val ready = state(TingAffix.TAS.term().copy(id = "renamed", surface = "तस्"), Lakara.LET)
        assertTrue(LetodatauSutra.matches(ready))
        val aorist = ready.copy(context = ready.context.copy(letFormation = LetFormation.SIP_AORIST))
        assertTrue(SibbahulamLetiSutra.matches(aorist))
        assertFalse(LetodatauSutra.matches(aorist))
        val fake = aorist.copy(terms = aorist.terms.dropLast(1) + DerivationTerm("ting-fake", "तस्", TermKind.PRATYAYA))
        assertFalse(SibbahulamLetiSutra.matches(fake))
        assertFalse(LetodatauSutra.matches(ready.copy(terms = fake.terms)))
    }
}
