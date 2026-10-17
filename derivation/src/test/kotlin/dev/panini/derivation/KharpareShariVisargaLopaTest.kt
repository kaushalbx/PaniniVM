package dev.panini.derivation

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.ashtadhyayi.vartika.KharpareShariVisargaLopaVartika
import dev.panini.sutra.runtime.SutraSource
import dev.panini.shiksha.*
import dev.panini.vyakaranam.lexicon.StandardPratipadikaLexicon
import kotlin.test.*

class KharpareShariVisargaLopaTest {
    private val rule = KharpareShariVisargaLopaVartika
    private fun state(right: String) = DerivationState(listOf(
        DerivationTerm("left", "वृक्षः", TermKind.PRATIPADIKA),
        DerivationTerm("right", right, TermKind.PRATIPADIKA)))

    @Test fun `kashika example has deletion retention and s substitution alternatives`() {
        val engine = SandhiEngine()
        val results = engine.joinAll("वृक्षाः", "स्थातारः")
        assertEquals(setOf("वृक्षास्थातारः", "वृक्षाःस्थातारः", "वृक्षास्स्थातारः"), results.map(engine::render).toSet())
        val deleted = results.single { rule.sutra in it.applications.map { step -> step.sutra } }
        assertEquals(rule.source, deleted.applications.single { it.sutra == rule.sutra }.source)
        assertTrue(results.all { it.final.stage == DerivationStage.FINAL })
    }

    @Test fun `sibilant must be immediately followed by khar not a vowel or voiced consonant`() {
        for (right in listOf("स्थातारः", "ष्क", "श्करः")) assertTrue(rule.matches(state(right)), right)
        for (right in listOf("शक्रः", "शेते", "साये", "षण्डे", "क्षुरम्", "श्ग", "श्लोकः")) {
            assertFalse(rule.matches(state(right)), right)
        }
        assertFalse(rule.matches(state("शक्रः").copy(terms = listOf(state("शक्रः").terms.first().copy(surface = "वृक्षस्"),
            state("शक्रः").terms.last()))))
    }

    @Test fun `exact visarga deletion preserves preceding annotations and source metadata`() {
        val original = state("स्थातारः")
        val left = original.terms.first().copy(surface = "अँःऽ", createdBySutra = "4.1.2", sourceSuffixUpadeshas = setOf("क्त"))
        val changed = rule.apply(original.copy(terms = listOf(left, original.terms.last()))).state
        val target = changed.terms.first()
        assertEquals("अँऽ", target.surface)
        assertEquals(left.phonologicalText.effectiveVarnas.first(), target.phonologicalText.effectiveVarnas.first())
        assertEquals(left.id, target.id)
        assertEquals(left.upadesha, target.upadesha)
        assertEquals(left.sourceSuffixUpadeshas, target.sourceSuffixUpadeshas)
        assertEquals(original.terms.last(), changed.terms.last())
        assertEquals(1, changed.substitutions.single().sourceVarnaIndex)
        assertFalse(rule.matches(changed))
    }

    @Test fun `vartika is registered separately and does not force unsupported corpus lopa`() {
        assertSame(rule, Ashtadhyayi.registry.require(rule.sutra))
        assertTrue(rule in Ashtadhyayi.vartikas)
        assertTrue(Ashtadhyayi.cataloguedSutras.none { it.number == rule.sutra })
        assertEquals("8.3.36", assertIs<SutraSource.Vartika>(rule.source).parentSutra)
        val outcomes = SandhiSequenceEvaluator(lexicon = DerivedVisargaLexicon.build(
            StandardPratipadikaLexicon.declaredPratipadikas()))
            .evaluate(listOf("सः", "शक्रः", "शक्रः", "इति"), alternatives = true)
        assertTrue(outcomes.none { it.surface.filterNot(Char::isWhitespace) == "सशक्रशक्रइति" })
        assertTrue(outcomes.all { it.steps.flatMap { step -> step.applications }.none { application -> application.sutra == rule.sutra } })
        assertTrue(outcomes.all { it.inputs[1].sourceDerivation != null })
    }
}
