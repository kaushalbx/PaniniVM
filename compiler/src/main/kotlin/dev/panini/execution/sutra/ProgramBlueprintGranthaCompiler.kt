package dev.panini.execution.sutra

import dev.panini.sutra.runtime.SutraBlueprintGrantha
import dev.panini.sutra.runtime.SutraBlueprintGranthaValidator
import dev.panini.sutra.runtime.SutraArthaValue
import dev.panini.sutra.runtime.SutraGrantha
import dev.panini.sutra.runtime.SutraNirnaya

sealed interface ProgramGranthaCompilation {
    data class Success(
        val grantha: SutraGrantha<ProgramAvastha>,
    ) : ProgramGranthaCompilation

    data class Invalid(
        val diagnostics: List<ProgramBlueprintDiagnostic>,
    ) : ProgramGranthaCompilation
}

/** Attaches program-domain behavior to a complete evaluator-free grantha. */
object ProgramBlueprintGranthaCompiler {
    fun compile(
        blueprintGrantha: SutraBlueprintGrantha,
        context: ProgramBlueprintContext,
    ): ProgramGranthaCompilation {
        val validation = SutraBlueprintGranthaValidator.validate(blueprintGrantha)
        if (!validation.isValid) {
            return ProgramGranthaCompilation.Invalid(
                validation.diagnostics.map {
                    ProgramBlueprintDiagnostic(
                        ProgramBlueprintDiagnosticCode.INVALID_GRANTHA,
                        it.message,
                    )
                },
            )
        }

        val diagnostics = mutableListOf<ProgramBlueprintDiagnostic>()
        val compiled = validation.orderedSutras.mapNotNull { blueprint ->
            when (val compilation = ProgramBlueprintCompiler.compile(blueprint, context)) {
                is ProgramBlueprintCompilation.Success -> compilation
                is ProgramBlueprintCompilation.Invalid -> {
                    diagnostics += compilation.diagnostics
                    null
                }
            }
        }
        if (diagnostics.isNotEmpty()) return ProgramGranthaCompilation.Invalid(diagnostics)
        val compiledById = compiled.associateBy { it.sutra.id }
        val sutras = compiled.map { compilation ->
            val conditionId = (
                compilation.sutra.artha.fields["repeatWhile"] as? SutraArthaValue.SutraReference
            )?.id ?: return@map compilation.sutra
            val condition = compiledById[conditionId]
            if (condition == null) {
                diagnostics += ProgramBlueprintDiagnostic(
                    ProgramBlueprintDiagnosticCode.INVALID_FIELD,
                    "Repeated sūtra ${compilation.sutra.id} refers to missing condition $conditionId.",
                )
                return@map compilation.sutra
            }
            val maximumIterations = (
                compilation.sutra.artha.fields["maximumIterations"] as? SutraArthaValue.Number
            )?.value?.toInt() ?: 1_000
            if (maximumIterations <= 0) {
                diagnostics += ProgramBlueprintDiagnostic(
                    ProgramBlueprintDiagnosticCode.INVALID_FIELD,
                    "Repeated sūtra ${compilation.sutra.id} requires a positive iteration limit.",
                )
                return@map compilation.sutra
            }
            val original = compilation.sutra
            original.copy(
                evaluator = { sutra, state ->
                    when (val decision = original.evaluator.evaluate(sutra, state)) {
                        is SutraNirnaya.Applicable -> SutraNirnaya.Applicable(
                            effects = listOf(
                                RepeatWhileEffect(
                                    condition = InvokeDhatuEffect(
                                        condition.invocation,
                                        condition.ukti,
                                    ),
                                    body = InvokeDhatuEffect(
                                        compilation.invocation,
                                        compilation.ukti,
                                    ),
                                    maximumIterations = maximumIterations,
                                ),
                            ),
                            reasons = decision.reasons +
                                "The kriyā remains eligible only while $conditionId yields satya.",
                        )
                        else -> decision
                    }
                },
            )
        }
        if (diagnostics.isNotEmpty()) return ProgramGranthaCompilation.Invalid(diagnostics)

        return ProgramGranthaCompilation.Success(
            SutraGrantha(
                id = blueprintGrantha.id,
                sutras = sutras,
                imports = blueprintGrantha.imports,
                adhikaras = blueprintGrantha.adhikaras,
                samjnas = blueprintGrantha.samjnas,
                exports = blueprintGrantha.exports,
            ),
        )
    }
}
