package dev.panini.sutra.runtime

import dev.panini.sutra.SutraGovernance
import dev.panini.sutra.SutraRole
import dev.panini.sutra.Sutra
import dev.panini.sutra.ArthavatSutra

/**
 * Evaluator-free sūtra definition suitable for inspection, generation,
 * transformation, persistence, and later compilation by a domain.
 */
data class SutraBlueprint(
    val id: SutraId,
    val source: SutraSource,
    val role: SutraRole,
    val artha: SutraArtha,
    val relations: Set<SutraRelation> = emptySet(),
    val governance: SutraGovernance = SutraGovernance(),
) {
    fun withArthaField(
        name: String,
        value: SutraArthaValue,
    ): SutraBlueprint {
        require(name.isNotBlank()) { "A sūtra semantic field requires a non-blank name." }
        return copy(artha = artha.copy(fields = artha.fields + (name to value)))
    }

    fun withoutArthaField(name: String): SutraBlueprint =
        copy(artha = artha.copy(fields = artha.fields - name))

    fun specializedAs(
        newId: SutraId,
        semanticFields: Map<String, SutraArthaValue>,
    ): SutraBlueprint = copy(
        id = newId,
        artha = artha.copy(fields = artha.fields + semanticFields),
    )
}

fun RuntimeSutra<*>.toBlueprint(): SutraBlueprint = SutraBlueprint(
    id = id,
    source = source,
    role = role,
    artha = artha,
    relations = relations,
    governance = governance,
)

/** Builds evaluator-free runtime metadata from an authoritative catalog sūtra. */
fun Sutra<*, *>.toBlueprint(artha: SutraArtha): SutraBlueprint = SutraBlueprint(
    id = SutraId(number),
    source = SutraSource.Ashtadhyayi(
        number = number,
        text = text,
        segmentedSource = segmentedSource,
    ),
    role = role,
    artha = artha,
    relations = buildSet {
        dependencies.mapTo(this) { SutraRelation.DependsOn(SutraId(it)) }
        blocks.mapTo(this) { SutraRelation.Blocks(SutraId(it)) }
    },
    governance = governance,
)

fun Sutra<*, *>.toBlueprint(): SutraBlueprint =
    (this as? ArthavatSutra)?.let { toBlueprint(it.artha.toSutraArtha()) }
        ?: error("Sūtra $number does not define native evaluator-free artha.")

enum class SutraBlueprintDiagnosticCode {
    SELF_DEPENDENCY,
    BLOCK_RELATION_MISMATCH,
}

data class SutraBlueprintDiagnostic(
    val code: SutraBlueprintDiagnosticCode,
    val message: String,
)

object SutraBlueprintValidator {
    fun validate(blueprint: SutraBlueprint): List<SutraBlueprintDiagnostic> {
        val diagnostics = mutableListOf<SutraBlueprintDiagnostic>()
        if (
            blueprint.relations
                .filterIsInstance<SutraRelation.DependsOn>()
                .any { it.prerequisite == blueprint.id }
        ) {
            diagnostics += SutraBlueprintDiagnostic(
                SutraBlueprintDiagnosticCode.SELF_DEPENDENCY,
                "Sūtra ${blueprint.id} cannot depend on itself.",
            )
        }
        val relationBlocks = blueprint.relations
            .filterIsInstance<SutraRelation.Blocks>()
            .mapTo(mutableSetOf()) { it.target.value }
        if (relationBlocks != blueprint.governance.blocks) {
            diagnostics += SutraBlueprintDiagnostic(
                SutraBlueprintDiagnosticCode.BLOCK_RELATION_MISMATCH,
                "Sūtra ${blueprint.id} has inconsistent block relations and governance.",
            )
        }
        return diagnostics
    }
}
