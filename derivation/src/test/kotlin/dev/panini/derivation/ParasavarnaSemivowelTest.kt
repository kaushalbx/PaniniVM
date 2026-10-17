package dev.panini.derivation

import dev.panini.ashtadhyayi.adhyaya8.pada4.AnusvarasyaYayiParasavarnahSutra
import dev.panini.ashtadhyayi.adhyaya8.pada4.VaPadantasyaSutra
import dev.panini.shiksha.*
import kotlin.test.*

class ParasavarnaSemivowelTest {
    @Test fun `padanta anusvara has optional nasalized y v l counterparts`() {
        val engine = SandhiEngine()
        for ((word, counterpart) in listOf("याति" to Vyanjana.YA, "वदति" to Vyanjana.VA, "लोकः" to Vyanjana.LA)) {
            val results = engine.joinAll("सम्", word)
            assertTrue(results.any { it.applications.none { step -> step.sutra == "8.4.59" } })
            val changed = results.single { it.applications.any { step -> step.sutra == "8.4.59" } }
            val term = changed.final.terms.first()
            val last = term.phonologicalText.effectiveVarnas.last()
            assertEquals(counterpart, last.varna)
            assertTrue(last.nasalized)
            assertFalse(term.phonologicalText.effectiveVarnas.first { it.varna is Svara }.nasalized)
            val trace = changed.applications.single { it.sutra == "8.4.59" }
            assertEquals(Ayogavaha.ANUSVARA, trace.before.terms.first().varnas.last())
            assertEquals(term.id, trace.after.terms.first().id)
            assertEquals(term.upadesha, trace.before.terms.first().upadesha)
            assertEquals(term.phonologicalText.effectiveVarnas.map { it.varna to it.nasalized },
                term.surface.toSanskritText().effectiveVarnas.map { it.varna to it.nasalized })
        }
    }

    @Test fun `ra and sibilants do not receive an invented dental nasal`() {
        for (follower in listOf("र", "श", "ष", "स", "ह")) {
            val internal = DerivationState(listOf(DerivationTerm("word", "अं$follower", TermKind.PRATIPADIKA)))
            assertFalse(AnusvarasyaYayiParasavarnahSutra.matches(internal), follower)
            val external = SandhiEngine().joinAll("सं", follower)
            assertTrue(external.all { result -> result.applications.none { it.sutra in setOf("8.4.58", "8.4.59") } })
        }
    }

    @Test fun `semivowel replacement preserves unrelated nasalization signs and identity`() {
        val left = DerivationTerm("left", "अँंऽ", TermKind.PRATIPADIKA, upadesha = "source")
        val initial = DerivationState(listOf(left, DerivationTerm("right", "ल", TermKind.PRATIPADIKA)),
            samjnas = setOf(SamjnaAssignment(left.id, Samjna.PADA)))
        val after = VaPadantasyaSutra.apply(initial).state
        val term = after.terms.first()
        assertEquals(left.id, term.id)
        assertEquals(left.upadesha, term.upadesha)
        assertTrue(term.phonologicalText.effectiveVarnas.first().nasalized)
        assertTrue(term.phonologicalText.effectiveVarnas.last().nasalized)
        assertEquals(listOf(OrthographicSignPlacement(OrthographicSign.AVAGRAHA, 2)), term.orthographicSigns)
        assertEquals(initial.terms.last(), after.terms.last())
        assertEquals(1, after.substitutions.single().sourceVarnaIndex)
    }

    @Test fun `five stop classes retain their ordinary nasal counterparts`() {
        for ((stop, nasal) in listOf(Vyanjana.KA to Vyanjana.NGA, Vyanjana.CA to Vyanjana.NYA,
            Vyanjana.TTA to Vyanjana.NNA, Vyanjana.TA to Vyanjana.NA, Vyanjana.PA to Vyanjana.MA)) {
            assertEquals(NasalParasavarna(nasal), stop.nasalParasavarna())
        }
    }
}
