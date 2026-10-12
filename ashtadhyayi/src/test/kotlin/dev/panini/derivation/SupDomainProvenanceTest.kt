package dev.panini.derivation

import dev.panini.ashtadhyayi.adhyaya7.pada3.BahuvacaneJhalyetSutra
import dev.panini.ashtadhyayi.adhyaya7.pada3.SupiCaSutra
import dev.panini.core.SupAffix
import dev.panini.core.Vacana
import dev.panini.core.Linga
import dev.panini.ashtadhyayi.adhyaya7.pada2.AstanAaVibhaktuSutra
import dev.panini.ashtadhyayi.adhyaya7.pada2.YusmadAsmadSutra
import dev.panini.ashtadhyayi.adhyaya7.pada2.TyadadinamAhSutra
import dev.panini.ashtadhyayi.adhyaya7.pada2.TisrCatasruSutra
import dev.panini.ashtadhyayi.adhyaya7.pada2.KimahKahSutra
import dev.panini.shiksha.Samjna
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SupDomainProvenanceTest {
    private fun state(affix: DerivationTerm, stem: String = "राम") = DerivationState(
        listOf(DerivationTerm("stem", stem, TermKind.PRATIPADIKA), affix),
        samjnas = setOf(SamjnaAssignment(affix.id, Samjna.PRATYAYA)),
        stage = DerivationStage.IT_PROCESSED,
        context = DerivationalContext(rupa = Rupa(vacana = Vacana.BAHUVACANA)),
    )

    @Test
    fun `pronominal and numeral rules use sup origin rather than names`() {
        val predicates: List<Pair<String, (DerivationState) -> Boolean>> = listOf(
            "अष्टन्" to AstanAaVibhaktuSutra::matches,
            "युष्मद्" to YusmadAsmadSutra::matches,
            "तद्" to TyadadinamAhSutra::matches,
            "त्रि" to TisrCatasruSutra::matches,
            "किम्" to KimahKahSutra::matches,
        )
        for ((stem, matches) in predicates) {
            val typed = state(SupAffix.JAS.term().copy(id = "renamed"), stem).copy(
                activeAdhikaras = setOf("6.4.1"),
                context = DerivationalContext(rupa = Rupa(linga = Linga.STRI, vacana = Vacana.BAHUVACANA)),
            )
            assertTrue(matches(typed), stem)
            val fake = typed.copy(terms = typed.terms.dropLast(1) + DerivationTerm("sup-fake", "जस्", TermKind.PRATYAYA))
            assertFalse(matches(fake), stem)
        }
    }

    @Test
    fun `dropped sup provenance survives a renamed lifecycle term`() {
        val dropped = consumeAffixForDrop(SupAffix.JAS.term().copy(id = "renamed"), "test")
        val original = state(DerivationTerm("other", "अ", TermKind.PRATYAYA), "किम्")
        assertFalse(KimahKahSutra.matches(original))
        assertTrue(KimahKahSutra.matches(original.copy(droppedTerms = listOf(dropped))))
        assertFalse(KimahKahSutra.matches(original.copy(droppedTerms = listOf(
            DerivationTerm("sup-fake", "", TermKind.PRATYAYA)))))
    }

    @Test
    fun `typed sup domain is independent of term names`() {
        assertTrue(SupiCaSutra.matches(state(SupAffix.BHYAM_3.term().copy(id = "renamed"))))
        assertTrue(BahuvacaneJhalyetSutra.matches(state(SupAffix.BHIS.term().copy(id = "renamed"))))
        assertFalse(SupiCaSutra.matches(state(DerivationTerm("sup-fake", "भ्याम्", TermKind.PRATYAYA))))
        assertFalse(BahuvacaneJhalyetSutra.matches(state(DerivationTerm("sup-fake", "भिस्", TermKind.PRATYAYA))))
    }

    @Test
    fun `retained source identity does not override current substitute exclusions`() {
        val shi = SupAffix.JAS.term().replaceWholeAffix("शि", "शि", "test", WholeAffixDesignationPolicy.Consume)
        assertFalse(BahuvacaneJhalyetSutra.matches(state(shi)))
        assertFalse(SupiCaSutra.matches(state(SupAffix.TA.term())))
    }
}
