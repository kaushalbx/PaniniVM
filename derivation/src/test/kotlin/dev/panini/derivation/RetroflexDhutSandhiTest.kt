package dev.panini.derivation

import dev.panini.ashtadhyayi.adhyaya8.pada4.NaPadantatTorAnamSutra
import dev.panini.ashtadhyayi.adhyaya8.pada4.StunaShtuhSutra
import dev.panini.core.SupAffix
import dev.panini.shiksha.*
import kotlin.test.*

class RetroflexDhutSandhiTest {
    private val engine = SandhiEngine()

    @Test fun `retroflex dhut augments following word and retains dental stop`() {
        val results = engine.joinAll("षट्", "सन्तः")
        assertEquals(setOf("षट्त्सन्तः", "षट्सन्तः"), results.map { engine.render(it) }.toSet())
        val applied = results.single { it.applications.any { step -> step.sutra == "8.3.29" } }
        assertEquals(Vyanjana.TTA, applied.final.terms.first().varnas.last())
        assertEquals(Vyanjana.TA, applied.final.terms.last().varnas.first())
        assertTrue(applied.applications.any { it.sutra == "8.4.42" })
        assertTrue(applied.applications.any { it.sutra == "1.1.46" })
        assertTrue(applied.final.terms.last().itMarkerProvenance.any { it.designationSutra == "1.3.3" })
        assertFalse(applied.applications.any { it.sutra == "8.4.41" })
    }

    private fun boundary(left: String, right: DerivationTerm) = DerivationState(
        listOf(DerivationTerm("left", left, TermKind.PRATIPADIKA), right),
        samjnas = setOf(SamjnaAssignment("left", Samjna.PADA)))

    @Test fun `genitive plural nam is typed not guessed from spelling`() {
        val lexical = boundary("षड्", DerivationTerm("right", "नाम्", TermKind.PRATIPADIKA))
        assertTrue(NaPadantatTorAnamSutra.matches(lexical))
        val typed = lexical.replaceTerm("right", SupAffix.AM_6.term("right").copy(surface = "नाम्"))
        assertFalse(NaPadantatTorAnamSutra.matches(typed))
        assertTrue(StunaShtuhSutra.matches(typed))
    }

    @Test fun `restriction requires pada-final retroflex class not sibilant or internal stop`() {
        val right = DerivationTerm("right", "तरति", TermKind.PRATIPADIKA)
        assertFalse(NaPadantatTorAnamSutra.matches(boundary("सर्पिष्", right)))
        val internal = boundary("ईड्", right).copy(samjnas = emptySet())
        assertFalse(NaPadantatTorAnamSutra.matches(internal))
        assertTrue(StunaShtuhSutra.matches(internal))
        val external = boundary("मधुलिड्", right)
        assertTrue(NaPadantatTorAnamSutra.matches(external))
        val protected = NaPadantatTorAnamSutra.apply(external).state
        assertFalse(StunaShtuhSutra.matches(protected))
        assertFalse(NaPadantatTorAnamSutra.matches(protected))
    }

    @Test fun `boundary protection leaves internal retroflexion in following term available`() {
        val state = boundary("मधुलिड्", DerivationTerm("right", "तड्द", TermKind.PRATIPADIKA))
        val protected = NaPadantatTorAnamSutra.apply(state).state
        assertTrue(StunaShtuhSutra.matches(protected))
        val after = StunaShtuhSutra.apply(protected).state
        assertEquals(Vyanjana.TA, after.terms.last().varnas.first())
        assertEquals(Vyanjana.DDA, after.terms.last().varnas[3])
        assertEquals(3, after.substitutions.last().sourceVarnaIndex)
    }
}
