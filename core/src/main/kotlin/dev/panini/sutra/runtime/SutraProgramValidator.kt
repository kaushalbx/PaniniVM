package dev.panini.sutra.runtime

enum class SutraProgramDiagnosticCode {
    MISSING_DEPENDENCY,
    MISSING_FLOW_SOURCE,
    MISSING_FLOW_TARGET,
    DEPENDENCY_CYCLE,
}

data class SutraProgramDiagnostic(
    val code: SutraProgramDiagnosticCode,
    val sutraId: SutraId,
    val message: String,
)

data class SutraProgramValidation<S : SutraAvastha>(
    val diagnostics: List<SutraProgramDiagnostic>,
    val orderedSutras: List<RuntimeSutra<S>>,
) {
    val isValid: Boolean get() = diagnostics.isEmpty()
}

/**
 * Validates hard intra-program references and produces a stable topological
 * order. Unrelated sūtras retain their source order.
 */
object SutraProgramValidator {
    fun <S : SutraAvastha> validate(
        program: SutraProgram<S>,
    ): SutraProgramValidation<S> {
        val byId = program.sutras.associateBy { it.id }
        val diagnostics = mutableListOf<SutraProgramDiagnostic>()
        val dependencies = linkedMapOf<SutraId, Set<SutraId>>()

        program.sutras.forEach { sutra ->
            val prerequisites = sutra.relations
                .filterIsInstance<SutraRelation.DependsOn>()
                .mapTo(linkedSetOf()) { it.prerequisite }
            dependencies[sutra.id] = prerequisites
            prerequisites.filter { it !in byId }.forEach { missing ->
                diagnostics += SutraProgramDiagnostic(
                    SutraProgramDiagnosticCode.MISSING_DEPENDENCY,
                    sutra.id,
                    "Sūtra ${sutra.id} depends on missing sūtra $missing.",
                )
            }
            sutra.relations.filterIsInstance<SutraRelation.PhalaPravaha>().forEach { flow ->
                if (flow.source !in byId) {
                    diagnostics += SutraProgramDiagnostic(
                        SutraProgramDiagnosticCode.MISSING_FLOW_SOURCE,
                        sutra.id,
                        "Result flow declared by ${sutra.id} has missing source ${flow.source}.",
                    )
                }
                if (flow.target !in byId) {
                    diagnostics += SutraProgramDiagnostic(
                        SutraProgramDiagnosticCode.MISSING_FLOW_TARGET,
                        sutra.id,
                        "Result flow declared by ${sutra.id} has missing target ${flow.target}.",
                    )
                }
            }
        }

        val sourceOrder = program.sutras.mapIndexed { index, sutra -> sutra.id to index }.toMap()
        val incoming = program.sutras.associate { sutra ->
            sutra.id to dependencies.getValue(sutra.id).count { it in byId }
        }.toMutableMap()
        val outgoing = program.sutras.associate { it.id to mutableListOf<SutraId>() }
        dependencies.forEach { (dependent, prerequisites) ->
            prerequisites.filter { it in byId }.forEach { prerequisite ->
                outgoing.getValue(prerequisite) += dependent
            }
        }
        val ready = incoming.filterValues { it == 0 }
            .keys
            .sortedBy(sourceOrder::getValue)
            .toMutableList()
        val ordered = mutableListOf<RuntimeSutra<S>>()
        while (ready.isNotEmpty()) {
            val id = ready.removeAt(0)
            ordered += byId.getValue(id)
            outgoing.getValue(id).forEach { dependent ->
                incoming[dependent] = incoming.getValue(dependent) - 1
                if (incoming.getValue(dependent) == 0) {
                    ready += dependent
                    ready.sortBy(sourceOrder::getValue)
                }
            }
        }

        if (ordered.size != program.sutras.size) {
            val cyclic = incoming.filterValues { it > 0 }.keys.sortedBy(sourceOrder::getValue)
            val owner = cyclic.first()
            diagnostics += SutraProgramDiagnostic(
                SutraProgramDiagnosticCode.DEPENDENCY_CYCLE,
                owner,
                "Sūtra dependency cycle detected: ${cyclic.joinToString(" → ")}.",
            )
        }

        return SutraProgramValidation(
            diagnostics = diagnostics,
            orderedSutras = if (ordered.size == program.sutras.size) ordered else program.sutras,
        )
    }
}
