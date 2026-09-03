package dev.panini.ashtadhyayi.adhyaya7

import dev.panini.ashtadhyayi.adhyaya7.pada2.AcoNnitiSutra
import dev.panini.ashtadhyayi.adhyaya7.pada2.KitiCaSutra
import dev.panini.ashtadhyayi.adhyaya7.pada2.TaddhitesvAcamAdehSutra
import dev.panini.ashtadhyayi.adhyaya6.pada4.TehSutra
import dev.panini.core.ItMarker
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.SthaniProperties
import dev.panini.derivation.TermKind
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ItProvenanceConsumerTest {
    @Test
    fun `aco nniti requires preserved marker provenance not final nna spelling`() {
        val stem = DerivationTerm("dhatu", "नी", TermKind.DHATU)
        val spellingOnly = DerivationTerm("affix", "अण्", TermKind.PRATYAYA, upadesha = "अण्")
        val context = DerivationState(terms = listOf(stem, spellingOnly), activeAdhikaras = setOf("6.4.1"))

        assertFalse(AcoNnitiSutra.matches(context))
        assertTrue(AcoNnitiSutra.matches(context.copy(terms = listOf(
            stem,
            spellingOnly.copy(
                surface = "अ",
                sthaniProps = SthaniProperties("अण्", setOf(ItMarker.NIT)),
            ),
        ))))
    }

    @Test
    fun `kiti ca requires preserved kit provenance not phak spelling`() {
        val stem = DerivationTerm("stem", "वत्स", TermKind.PRATIPADIKA)
        val spellingOnly = DerivationTerm("affix", "आयन्", TermKind.PRATYAYA, upadesha = "फक्")
        val context = DerivationState(terms = listOf(stem, spellingOnly), activeAdhikaras = setOf("4.1.76"))

        assertFalse(KitiCaSutra.matches(context))
        assertTrue(KitiCaSutra.matches(context.copy(terms = listOf(
            stem,
            spellingOnly.copy(sthaniProps = SthaniProperties("फक्", setOf(ItMarker.KIT))),
        ))))
    }

    @Test
    fun `teh requires preserved dit provenance not dat spelling`() {
        val stem = DerivationTerm(
            "numeral", "षष्टि", TermKind.PRATIPADIKA, compoundHeadUpadesha = "षष्टि",
        )
        val spellingOnly = DerivationTerm("dat", "अ", TermKind.PRATYAYA, upadesha = "डट्")
        val context = DerivationState(terms = listOf(stem, spellingOnly))

        assertFalse(TehSutra.matches(context))
        assertTrue(TehSutra.matches(context.copy(terms = listOf(
            stem,
            spellingOnly.copy(sthaniProps = SthaniProperties("डट्", setOf(ItMarker.T))),
        ))))
    }

    @Test
    fun `taddhita vrddhi requires preserved nit provenance not an spelling`() {
        val stem = DerivationTerm("stem", "दशरथ", TermKind.PRATIPADIKA)
        val spellingOnly = DerivationTerm("affix", "अ", TermKind.PRATYAYA, upadesha = "अण्")
        val context = DerivationState(terms = listOf(stem, spellingOnly), activeAdhikaras = setOf("4.1.76"))

        assertFalse(TaddhitesvAcamAdehSutra.matches(context))
        assertTrue(TaddhitesvAcamAdehSutra.matches(context.copy(terms = listOf(
            stem,
            spellingOnly.copy(sthaniProps = SthaniProperties("अण्", setOf(ItMarker.NIT))),
        ))))
    }
}
