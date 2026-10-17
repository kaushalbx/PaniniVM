package dev.panini.derivation

import dev.panini.ashtadhyayi.adhyaya8.pada3.OtoGargyasyaSutra
import dev.panini.shiksha.*
import kotlin.test.*

class OtoGargyasyaSandhiTest {
    private val engine = SandhiEngine()
    @Test fun `ordinary yatva after o has mandatory lopa and does not reopen purvarupa`() {
        for ((left, base) in listOf("भोः" to "भो", "भगोः" to "भगो", "अघोः" to "अघो")) {
            for (right in listOf("अत्र", "इदम्")) {
                val input = SandhiEngine.Pada(DerivationTerm("left", left, TermKind.PRATIPADIKA), visargaSource = VisargaSource.S)
                val next = SandhiEngine.Pada(DerivationTerm("right", right, TermKind.PRATIPADIKA))
                val result = engine.join(input, next, DerivationConfig(optionalRulePolicy = OptionalRulePolicy.SKIP_ALL))
                assertEquals("$base $right", engine.render(result))
                assertTrue(result.applications.any { it.sutra == "8.3.17" })
                assertTrue(result.applications.any { it.sutra == "8.3.20" })
                assertTrue(result.applications.none { it.sutra == "6.1.109" || it.sutra == "8.3.19" })
                assertEquals(setOf("$base $right"), engine.joinAll(input, next).map(engine::render).toSet())
            }
        }
    }

    private fun state(left: String = "भोँय्ऽ", right: String = "इदम्") = DerivationState(listOf(
        DerivationTerm("left", left, TermKind.PRATIPADIKA, formedPadaRupa = Rupa()),
        DerivationTerm("right", right, TermKind.PRATIPADIKA, formedPadaRupa = Rupa())),
        samjnas = setOf(SamjnaAssignment("left", Samjna.PADA)))

    @Test fun `exact final y deletion retains unrelated annotations and identity`() {
        val input = state()
        assertTrue(OtoGargyasyaSutra.matches(input))
        val result = OtoGargyasyaSutra.apply(input).state
        val term = result.terms.first()
        assertEquals(listOf(Vyanjana.BHA, Svara.O), term.varnas)
        assertTrue(term.phonologicalText.effectiveVarnas.last().nasalized)
        assertEquals(input.terms.first().id, term.id)
        assertEquals(input.terms.first().upadesha, term.upadesha)
        assertEquals(input.terms.last(), result.terms.last())
        assertEquals(input.terms.first().varnas.lastIndex, result.substitutions.single().sourceVarnaIndex)
        assertFalse(OtoGargyasyaSutra.matches(result))
    }

    @Test fun `v other vowels internal y and non ash followers are excluded`() {
        for (input in listOf(state("भोव्"), state("भाय्"), state("भोयति"), state(right = "क"),
            state().copy(samjnas = emptySet()), state().let { it.replaceTerm("left", it.terms.first().copy(formedPadaRupa = null)) })) {
            assertFalse(OtoGargyasyaSutra.matches(input))
        }
    }
}
