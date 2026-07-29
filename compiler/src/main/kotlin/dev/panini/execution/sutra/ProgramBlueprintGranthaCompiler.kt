package dev.panini.execution.sutra

import dev.panini.sutra.runtime.SutraBlueprintGrantha
import dev.panini.sutra.runtime.SutraBlueprintGranthaValidator
import dev.panini.sutra.runtime.SutraGrantha

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
        val sutras = validation.orderedSutras.mapNotNull { blueprint ->
            when (val compilation = ProgramBlueprintCompiler.compile(blueprint, context)) {
                is ProgramBlueprintCompilation.Success -> compilation.sutra
                is ProgramBlueprintCompilation.Invalid -> {
                    diagnostics += compilation.diagnostics
                    null
                }
            }
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
