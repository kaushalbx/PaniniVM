package dev.panini.derivation

import dev.panini.sutra.NimittaScope
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraPriority
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraStage
import dev.panini.sutra.SutraType
import dev.panini.sutra.SutraVisibility
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class DerivationPipelineTest {
    @Test
    fun `runs metadata phases in order and consolidates provenance`() {
        val pipeline = pipeline(
            TestSutra("test.1", SutraStage.ANGAKARYA, "a", "b"),
            TestSutra("test.2", SutraStage.PADA_FORMATION, "b", "c"),
        )

        val result = pipeline.derive(state("a"))

        assertEquals("c", result.final.surface)
        assertEquals(listOf("test.1", "test.2"), result.applications.map { it.sutra })
        assertEquals(1, result.events.count { it is DerivationEvent.Completed })
        assertEquals(2, assertIs<DerivationEvent.Completed>(result.events.last()).applicationCount)
    }

    @Test
    fun `branches only stages selected by the workflow`() {
        val pipeline = pipeline(
            TestSutra("test.optional", SutraStage.ANGAKARYA, "a", "b", optional = true),
            TestSutra("test.final", SutraStage.PADA_FORMATION, "b", "c"),
        )

        val results = pipeline.deriveAll(state("a"), setOf(SutraStage.ANGAKARYA))

        assertEquals(setOf("a", "c"), results.map { it.final.surface }.toSet())
        assertEquals(2, results.size)
    }

    private fun pipeline(vararg sutras: DerivationSutra): DerivationPipeline {
        val byStage = sutras.groupBy { it.stage }
        return DerivationPipeline(
            stages = listOf(SutraStage.ANGAKARYA, SutraStage.PADA_FORMATION),
            sutrasForStage = { stage -> byStage.getValue(stage) },
        )
    }

    private fun state(surface: String) = DerivationState(
        terms = listOf(DerivationTerm("term", surface, TermKind.PRATIPADIKA)),
    )

    private class TestSutra(
        override val sutra: String,
        override val stage: SutraStage,
        private val input: String,
        private val output: String,
        override val optional: Boolean = false,
    ) : DerivationSutra {
        override val krama: Int = if (stage == SutraStage.ANGAKARYA) 1 else 2
        override val type: SutraType = SutraType.NITYA
        override val role: SutraRole = SutraRole.Vidhi
        override val action: SutraAction = SutraAction.VIDHI
        override val scope: SutraScope = SutraScope.DERIVATION
        override val nimittaScope: NimittaScope = NimittaScope.BOTH
        override val priority: SutraPriority = SutraPriority.NORMAL
        override val visibility: SutraVisibility = SutraVisibility.NORMAL
        override val blocks: Set<String> = emptySet()
        override val traceTemplate: String? = null

        override fun matches(context: DerivationState): Boolean = context.surface == input

        override fun apply(context: DerivationState): DerivationChange = DerivationChange(
            state = context.replaceTerm("term", context.terms.single().copy(surface = output)),
            explanation = "$input → $output",
        )
    }
}
