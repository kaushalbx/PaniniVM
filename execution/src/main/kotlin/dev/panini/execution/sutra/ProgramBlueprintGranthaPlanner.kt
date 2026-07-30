package dev.panini.execution.sutra

import dev.panini.execution.ActionDependency
import dev.panini.execution.ExecutableUkti
import dev.panini.execution.ExecutionProgram
import dev.panini.sutra.runtime.SutraBlueprintGrantha
import dev.panini.sutra.runtime.SutraBlueprintGranthaValidator
import dev.panini.sutra.runtime.SutraRelation

sealed interface ProgramGranthaPlanning {
    data class Success(
        val program: ExecutionProgram,
    ) : ProgramGranthaPlanning

    data class Invalid(
        val diagnostics: List<ProgramBlueprintDiagnostic>,
    ) : ProgramGranthaPlanning
}

/** Reconstructs the existing planner IR from evaluator-free sūtra software. */
object ProgramBlueprintGranthaPlanner {
    fun plan(
        grantha: SutraBlueprintGrantha,
        context: ProgramBlueprintContext,
    ): ProgramGranthaPlanning {
        val validation = SutraBlueprintGranthaValidator.validate(grantha)
        if (!validation.isValid) {
            return ProgramGranthaPlanning.Invalid(
                validation.diagnostics.map {
                    ProgramBlueprintDiagnostic(
                        ProgramBlueprintDiagnosticCode.INVALID_GRANTHA,
                        it.message,
                    )
                },
            )
        }

        val diagnostics = mutableListOf<ProgramBlueprintDiagnostic>()
        val invocations = validation.orderedSutras.mapNotNull { blueprint ->
            when (val compilation = ProgramBlueprintCompiler.compile(blueprint, context)) {
                is ProgramBlueprintCompilation.Success -> compilation.invocation
                is ProgramBlueprintCompilation.Invalid -> {
                    diagnostics += compilation.diagnostics
                    null
                }
            }
        }
        if (diagnostics.isNotEmpty()) return ProgramGranthaPlanning.Invalid(diagnostics)
        if (invocations.isEmpty()) {
            return ProgramGranthaPlanning.Invalid(
                listOf(
                    ProgramBlueprintDiagnostic(
                        ProgramBlueprintDiagnosticCode.INVALID_GRANTHA,
                        "Program grantha ${grantha.id} contains no executable sūtras.",
                    ),
                ),
            )
        }

        val dependencies = grantha.sutras.flatMapTo(linkedSetOf()) { blueprint ->
            blueprint.relations.filterIsInstance<SutraRelation.DependsOn>().map {
                ActionDependency(it.prerequisite.value, blueprint.id.value)
            }
        }
        val ukti = ExecutableUkti(
            speaker = context.speaker,
            listener = context.listener,
            text = context.text,
            prayojana = context.prayojana,
            polarity = context.polarity,
            lakara = context.lakara,
            invocations = invocations,
            dependencies = dependencies,
        )
        return ProgramGranthaPlanning.Success(ExecutionProgram(ukti, dependencies))
    }
}
