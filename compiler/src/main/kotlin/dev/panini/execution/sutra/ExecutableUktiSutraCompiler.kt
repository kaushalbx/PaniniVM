package dev.panini.execution.sutra

import dev.panini.execution.ExecutableUkti
import dev.panini.execution.VakyaPrayojana
import dev.panini.sutra.SutraRole
import dev.panini.sutra.runtime.GranthaId
import dev.panini.sutra.runtime.RuntimeSutra
import dev.panini.sutra.runtime.SutraGrantha
import dev.panini.sutra.runtime.SutraGranthaCompiler
import dev.panini.sutra.runtime.SutraGranthaLowering
import dev.panini.sutra.runtime.SutraId
import dev.panini.sutra.runtime.SutraNirnaya
import dev.panini.sutra.runtime.SutraProgram
import dev.panini.sutra.runtime.SutraRelation
import dev.panini.sutra.runtime.SutraSource

/**
 * Compatibility compiler used during the incremental migration. It preserves
 * the current binding model while normalizing each dhātu occurrence to one
 * runtime sūtra.
 */
object ExecutableUktiSutraCompiler {
    fun compile(ukti: ExecutableUkti): SutraProgram<ProgramAvastha> {
        return when (val lowering = SutraGranthaCompiler.lower(compileGrantha(ukti))) {
            is SutraGranthaLowering.Success -> lowering.program
            is SutraGranthaLowering.Invalid -> error(
                lowering.diagnostics.joinToString(separator = "\n") { it.message },
            )
        }
    }

    fun compileGrantha(ukti: ExecutableUkti): SutraGrantha<ProgramAvastha> {
        val dependenciesByTarget = ukti.dependencies.groupBy { it.after }
        val sutras = ukti.invocations.mapIndexed { index, invocation ->
            val id = SutraId(invocation.id)
            val prerequisites = dependenciesByTarget[invocation.id]
                .orEmpty()
                .mapTo(linkedSetOf()) { SutraId(it.before) }
            RuntimeSutra<ProgramAvastha>(
                id = id,
                source = SutraSource.Vakya(
                    uktiId = "ukti",
                    vakyaIndex = index,
                    text = ukti.text,
                ),
                role = when (ukti.prayojana) {
                    VakyaPrayojana.NISHEDHA -> SutraRole.Nishedha
                    else -> SutraRole.Vidhi
                },
                evaluator = { _, state ->
                    when {
                        state.halted -> SutraNirnaya.NotApplicable(
                            listOf("Program execution has been suspended or terminated."),
                        )
                        id in state.completedSutras -> SutraNirnaya.NotApplicable(
                            listOf("The sūtra has already been applied."),
                        )
                        prerequisites.any { it !in state.completedSutras } -> {
                            val missing = prerequisites.first { it !in state.completedSutras }
                            SutraNirnaya.Blocked(
                                missing,
                                listOf("A prerequisite sūtra has not completed."),
                            )
                        }
                        else -> SutraNirnaya.Applicable(
                            effects = listOf(InvokeDhatuEffect(invocation, ukti)),
                            reasons = listOf("The dhātu invocation is ready."),
                        )
                    }
                },
                relations = prerequisites.mapTo(linkedSetOf()) { SutraRelation.DependsOn(it) },
            )
        }
        return SutraGrantha(
            id = GranthaId("ukti"),
            sutras = sutras,
            exports = sutras.mapTo(linkedSetOf()) { it.id },
        )
    }
}
