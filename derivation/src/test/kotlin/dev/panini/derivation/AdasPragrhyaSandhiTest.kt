package dev.panini.derivation

import dev.panini.ashtadhyayi.adhyaya1.pada1.AdasoMatSutra
import dev.panini.core.*
import dev.panini.shiksha.*
import kotlin.test.*

class AdasPragrhyaSandhiTest {
    private val engine = SandhiEngine()
    private fun adas(surface: String, vacana: Vacana) = SandhiEngine.Pada(
        DerivationTerm("adas", surface, TermKind.PRATIPADIKA, upadesha = "अदस्"), Rupa(vacana = vacana))
    private fun following(surface: String) = SandhiEngine.Pada(DerivationTerm("next", surface, TermKind.PRATIPADIKA))

    @Test fun `adas plural and dual forms remain in hiatus before vowels`() {
        for ((word, vacana, next) in listOf(Triple("अमी", Vacana.BAHUVACANA, "अत्र"),
            Triple("अमी", Vacana.BAHUVACANA, "ईशाः"), Triple("अमू", Vacana.DVIVACANA, "अत्र"),
            Triple("अमू", Vacana.DVIVACANA, "आसाते"))) {
            val results = engine.joinAll(adas(word, vacana), following(next))
            assertEquals(setOf("$word $next"), results.map(engine::render).toSet())
            assertTrue(results.all { r -> r.applications.any { it.sutra == "1.1.12" } })
            assertTrue(results.all { r -> r.applications.any { it.sutra == "6.1.125" } })
            assertTrue(results.all { r -> r.applications.none { it.sutra in setOf("6.1.77", "6.1.101") } })
        }
    }

    @Test fun `producer establishes 81 and 12 before completed pada handoff`() {
        val produced = SubantaEngine().derive(SubantaDerivationRequest("अदस्", Vibhakti.PRATHAMA,
            Vacana.BAHUVACANA, Linga.PUMS))
        assertEquals("अमी", produced.final.surface)
        val trace = produced.applications.map { it.sutra }
        assertTrue("8.2.81" in trace && "1.1.12" in trace)
        assertTrue(trace.indexOf("8.2.81") < trace.indexOf("1.1.12"))
        assertFalse("8.2.80" in trace)
        val term = produced.final.terms.single { it.varnas.isNotEmpty() }
        val samjnas = produced.final.samjnas.filter { it.targetId == term.id }.map { it.samjna }.toSet()
        assertTrue(Samjna.PRAGRHYA in samjnas)
        val result = engine.join(SandhiEngine.Pada(term, produced.final.context.rupa, samjnas), following("अत्र"))
        assertEquals("अमी अत्र", engine.render(result))
        assertTrue(result.applications.any { it.sutra == "6.1.125" })
        assertTrue(result.applications.none { it.sutra == "6.1.77" })
    }

    @Test fun `raw spelling or other lexical m does not establish adas origin`() {
        for (word in listOf("अमी", "शमी", "दाडिमी")) {
            val result = engine.join(word, "अत्र")
            assertTrue(result.applications.none { it.sutra == "1.1.12" }, word)
        }
        assertEquals("शम्यत्र", engine.render(engine.join("शमी", "अत्र")))
    }

    @Test fun `e and an intervening consonant do not receive the designation`() {
        val result = engine.join(adas("अमुके", Vacana.BAHUVACANA), following("अत्र"))
        assertEquals("अमुकेऽत्र", engine.render(result))
        assertTrue(result.applications.none { it.sutra == "1.1.12" })
        val state = DerivationState(listOf(DerivationTerm("adas", "अमे", TermKind.PRATIPADIKA,
            upadesha = "अदस्", formedPadaRupa = Rupa())))
        assertFalse(AdasoMatSutra.matches(state))
    }

    @Test fun `internal stem before an active sup is not a completed pragrihya pada`() {
        val state = DerivationState(listOf(DerivationTerm("adas", "अमी", TermKind.PRATIPADIKA, upadesha = "अदस्"),
            DerivationTerm("suffix", "भिस्", TermKind.PRATYAYA, sourceSupAffix = SupAffix.BHIS)),
            stage = DerivationStage.PADA_FORMED)
        assertFalse(AdasoMatSutra.matches(state))
    }

    @Test fun `designation retains the annotated term and does not repeat`() {
        val state = DerivationState(listOf(DerivationTerm("adas", "अँमीँऽ", TermKind.PRATIPADIKA,
            upadesha = "अदस्", formedPadaRupa = Rupa())))
        val changed = AdasoMatSutra.apply(state).state
        assertEquals(state.terms, changed.terms)
        assertEquals(state.substitutions, changed.substitutions)
        assertTrue(SamjnaAssignment("adas", Samjna.PRAGRHYA) in changed.samjnas)
        assertFalse(AdasoMatSutra.matches(changed))
    }
}
