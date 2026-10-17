package dev.panini.derivation

import dev.panini.shiksha.*
import dev.panini.sutra.*
import kotlin.test.*

class PrescribedAlternativesTest {
    private val threeOutcomes = object : Sutra<DerivationState, DerivationChange>(
        number = "9.1.1", text = "test", hindiExplanation = "test", type = SutraType.NITYA,
        chapter = 9, pada = 1, optional = false, kramaValue = 910001,
        role = SutraRole.Vidhi, action = SutraAction.ADESHA, scope = SutraScope.VARNA,
    ), DerivationSutra {
        override val hasAlternatives = true
        override fun matches(context: DerivationState) = context.stage == DerivationStage.INITIAL
        override fun apply(context: DerivationState) = outcome(context, Svara.A)
        override fun applyAll(state: DerivationState) = listOf(Svara.A, Svara.I, Svara.U).map { outcome(state, it) }
        private fun outcome(state: DerivationState, vowel: Svara) = DerivationChange(
            state.replaceTermVarna("word", 0, listOf(vowel), sutra).copy(stage = DerivationStage.FINAL),
            "Prescribed test outcome $vowel.")
    }
    private fun initial() = DerivationState(listOf(DerivationTerm("word", "क्", TermKind.PRATIPADIKA)))

    @Test fun `enumeration consumes all prescribed changes not just apply versus omission`() {
        val results = DerivationEngine(listOf(threeOutcomes)).deriveAll(initial(), DerivationConfig(computeSvara = false))
        assertEquals(setOf("अ", "इ", "उ"), results.map { it.final.surface }.toSet())
        assertTrue(results.all { it.applications.single().sutra == "9.1.1" })
        assertTrue(results.all { r -> r.events.any { it is DerivationEvent.BranchCreated && it.branchCount == 3 } })
    }

    @Test fun `explicit choice does not turn prescribed outcomes into optional omission`() {
        val engine = DerivationEngine(listOf(threeOutcomes))
        val selected = engine.derive(initial(), DerivationConfig(computeSvara = false,
            alternativeChoices = mapOf("9.1.1" to 2), optionalRulePolicy = OptionalRulePolicy.SKIP_ALL))
        assertEquals("उ", selected.final.surface)
        assertEquals(1, selected.applications.size)
        assertFailsWith<IllegalArgumentException> {
            engine.derive(initial(), DerivationConfig(computeSvara = false, alternativeChoices = mapOf("9.1.1" to 3)))
        }
    }

    @Test fun `application-level frontier history survives copy and equality`() {
        val initial = initial().recordAppliedSutra("8.3.37")
        val retained = initial.recordAppliedSutra("8.3.37", advancesTripadiFrontier = false)
        assertEquals(setOf(1), retained.nonAdvancingTripadiApplications)
        assertEquals(retained, retained.copy())
        val advancing = retained.copy(nonAdvancingTripadiApplications = emptySet())
        assertEquals(2, setOf(retained, advancing).size)
    }

    @Test fun `changing an earlier choice discards later context dependent indices`() {
        fun rule(number: String, from: DerivationStage, to: DerivationStage,
            vowels: (DerivationState) -> List<Svara>) = object : Sutra<DerivationState, DerivationChange>(
            number = number, text = "test", hindiExplanation = "test", type = SutraType.NITYA,
            chapter = 9, pada = 1, optional = false, kramaValue = if (number == "9.1.1") 910001 else 910002,
            role = SutraRole.Vidhi, action = SutraAction.ADESHA, scope = SutraScope.VARNA,
        ), DerivationSutra {
            override val hasAlternatives = true
            override fun matches(context: DerivationState) = context.stage == from
            override fun apply(context: DerivationState) = applyAll(context).first()
            override fun applyAll(state: DerivationState) = vowels(state).map { vowel ->
                DerivationChange(state.replaceTermVarna("word", 0, listOf(vowel), sutra).copy(stage = to),
                    "Context-dependent test outcome $vowel.")
            }
        }
        val first = rule("9.1.1", DerivationStage.INITIAL, DerivationStage.PRATYAYA_SELECTED) { listOf(Svara.A, Svara.I) }
        val second = rule("9.1.2", DerivationStage.PRATYAYA_SELECTED, DerivationStage.FINAL) {
            if (it.terms.single().varnas.single() == Svara.A) listOf(Svara.U, Svara.UU, Svara.E) else listOf(Svara.O, Svara.AU)
        }
        val results = DerivationEngine(listOf(first, second)).deriveAll(initial(), DerivationConfig(computeSvara = false))
        assertEquals(setOf("उ", "ऊ", "ए", "ओ", "औ"), results.map { it.final.surface }.toSet())
        assertEquals(5, results.size)
    }
}
