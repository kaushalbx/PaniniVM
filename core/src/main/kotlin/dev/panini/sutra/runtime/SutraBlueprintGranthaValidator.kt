package dev.panini.sutra.runtime

enum class SutraBlueprintGranthaDiagnosticCode {
    DUPLICATE_IMPORT_ALIAS,
    DUPLICATE_SAMJNA,
    DUPLICATE_SUTRA_ID,
    MISSING_ADHIKARA_SUTRA,
    MISSING_ADHIKARA_MEMBER,
    MISSING_EXPORT,
    INVALID_BLUEPRINT,
    MISSING_DEPENDENCY,
    MISSING_BLOCK_TARGET,
    MISSING_FLOW_SOURCE,
    MISSING_FLOW_TARGET,
    DEPENDENCY_CYCLE,
}

data class SutraBlueprintGranthaDiagnostic(
    val code: SutraBlueprintGranthaDiagnosticCode,
    val message: String,
)

data class SutraBlueprintGranthaValidation(
    val diagnostics: List<SutraBlueprintGranthaDiagnostic>,
    val orderedSutras: List<SutraBlueprint>,
) {
    val isValid: Boolean get() = diagnostics.isEmpty()
}

/** Validates an evaluator-free package before any domain attaches behavior. */
object SutraBlueprintGranthaValidator {
    fun validate(grantha: SutraBlueprintGrantha): SutraBlueprintGranthaValidation {
        val diagnostics = mutableListOf<SutraBlueprintGranthaDiagnostic>()
        grantha.imports.groupBy { it.alias }.filterValues { it.size > 1 }.keys.forEach { alias ->
            diagnostics += diagnostic(
                SutraBlueprintGranthaDiagnosticCode.DUPLICATE_IMPORT_ALIAS,
                "Grantha ${grantha.id} declares import alias '$alias' more than once.",
            )
        }
        grantha.samjnas.groupBy { it.name }.filterValues { it.size > 1 }.keys.forEach { name ->
            diagnostics += diagnostic(
                SutraBlueprintGranthaDiagnosticCode.DUPLICATE_SAMJNA,
                "Grantha ${grantha.id} declares saṃjñā '$name' more than once.",
            )
        }
        grantha.sutras.groupBy { it.id }.filterValues { it.size > 1 }.keys.forEach { id ->
            diagnostics += diagnostic(
                SutraBlueprintGranthaDiagnosticCode.DUPLICATE_SUTRA_ID,
                "Grantha ${grantha.id} declares sūtra $id more than once.",
            )
        }

        val ids = grantha.sutras.mapTo(linkedSetOf()) { it.id }
        grantha.adhikaras.forEach { adhikara ->
            if (adhikara.sutraId !in ids) {
                diagnostics += diagnostic(
                    SutraBlueprintGranthaDiagnosticCode.MISSING_ADHIKARA_SUTRA,
                    "Adhikāra ${adhikara.sutraId} is not present in grantha ${grantha.id}.",
                )
            }
            adhikara.members.filter { it !in ids }.forEach { missing ->
                diagnostics += diagnostic(
                    SutraBlueprintGranthaDiagnosticCode.MISSING_ADHIKARA_MEMBER,
                    "Adhikāra ${adhikara.sutraId} contains missing sūtra $missing.",
                )
            }
        }
        grantha.exports.filter { it !in ids }.forEach { missing ->
            diagnostics += diagnostic(
                SutraBlueprintGranthaDiagnosticCode.MISSING_EXPORT,
                "Grantha ${grantha.id} exports missing sūtra $missing.",
            )
        }

        val dependencies = linkedMapOf<SutraId, Set<SutraId>>()
        grantha.sutras.forEach { sutra ->
            SutraBlueprintValidator.validate(sutra).forEach { invalid ->
                diagnostics += diagnostic(
                    SutraBlueprintGranthaDiagnosticCode.INVALID_BLUEPRINT,
                    invalid.message,
                )
            }
            val prerequisites = sutra.relations
                .filterIsInstance<SutraRelation.DependsOn>()
                .mapTo(linkedSetOf()) { it.prerequisite }
            dependencies[sutra.id] = prerequisites
            prerequisites.filter { it !in ids }.forEach { missing ->
                diagnostics += diagnostic(
                    SutraBlueprintGranthaDiagnosticCode.MISSING_DEPENDENCY,
                    "Sūtra ${sutra.id} depends on missing sūtra $missing.",
                )
            }
            sutra.relations.filterIsInstance<SutraRelation.Blocks>()
                .filter { it.target !in ids }
                .forEach { block ->
                    diagnostics += diagnostic(
                        SutraBlueprintGranthaDiagnosticCode.MISSING_BLOCK_TARGET,
                        "Sūtra ${sutra.id} blocks missing sūtra ${block.target}.",
                    )
                }
            sutra.relations.filterIsInstance<SutraRelation.PhalaPravaha>().forEach { flow ->
                if (flow.source !in ids) {
                    diagnostics += diagnostic(
                        SutraBlueprintGranthaDiagnosticCode.MISSING_FLOW_SOURCE,
                        "Result flow declared by ${sutra.id} has missing source ${flow.source}.",
                    )
                }
                if (flow.target !in ids) {
                    diagnostics += diagnostic(
                        SutraBlueprintGranthaDiagnosticCode.MISSING_FLOW_TARGET,
                        "Result flow declared by ${sutra.id} has missing target ${flow.target}.",
                    )
                }
            }
        }

        val ordered = topologicalOrder(grantha.sutras, dependencies)
        if (ids.size == grantha.sutras.size && ordered.size != grantha.sutras.size) {
            diagnostics += diagnostic(
                SutraBlueprintGranthaDiagnosticCode.DEPENDENCY_CYCLE,
                "Sūtra dependency cycle detected in grantha ${grantha.id}.",
            )
        }
        return SutraBlueprintGranthaValidation(
            diagnostics,
            if (ordered.size == grantha.sutras.size) ordered else grantha.sutras,
        )
    }

    private fun topologicalOrder(
        sutras: List<SutraBlueprint>,
        dependencies: Map<SutraId, Set<SutraId>>,
    ): List<SutraBlueprint> {
        val byId = sutras.associateBy { it.id }
        if (byId.size != sutras.size) return emptyList()
        val sourceOrder = sutras.mapIndexed { index, sutra -> sutra.id to index }.toMap()
        val incoming = sutras.associate { sutra ->
            sutra.id to dependencies.getValue(sutra.id).count { it in byId }
        }.toMutableMap()
        val outgoing = sutras.associate { it.id to mutableListOf<SutraId>() }
        dependencies.forEach { (dependent, prerequisites) ->
            prerequisites.filter { it in byId }.forEach { prerequisite ->
                outgoing.getValue(prerequisite) += dependent
            }
        }
        val ready = incoming.filterValues { it == 0 }.keys
            .sortedBy(sourceOrder::getValue)
            .toMutableList()
        val ordered = mutableListOf<SutraBlueprint>()
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
        return ordered
    }

    private fun diagnostic(
        code: SutraBlueprintGranthaDiagnosticCode,
        message: String,
    ) = SutraBlueprintGranthaDiagnostic(code, message)
}
