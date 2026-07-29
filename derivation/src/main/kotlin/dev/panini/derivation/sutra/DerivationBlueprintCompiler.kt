package dev.panini.derivation.sutra

import dev.panini.sutra.SamjnaDefinitionArtha
import dev.panini.sutra.runtime.RuntimeSutra
import dev.panini.sutra.runtime.SutraBlueprint
import dev.panini.sutra.runtime.SutraNirnaya

/** Lowers evaluator-free grammatical meanings into derivation runtime rules. */
object DerivationBlueprintCompiler {
    fun compile(blueprint: SutraBlueprint): RuntimeSutra<DerivationAvastha> =
        when (blueprint.artha.kind) {
            SamjnaDefinitionArtha.KIND -> compileSamjnaDefinition(blueprint)
            else -> error(
                "Unsupported derivation blueprint meaning '${blueprint.artha.kind}' for ${blueprint.id}.",
            )
        }

    private fun compileSamjnaDefinition(
        blueprint: SutraBlueprint,
    ): RuntimeSutra<DerivationAvastha> {
        val artha = SamjnaDefinitionArtha.fromSutraArtha(blueprint.artha)
        val definition = SamjnaDefinition(
            samjni = artha.samjni,
            samjna = artha.samjna,
            definingSutra = blueprint.id,
        )

        return RuntimeSutra(
            id = blueprint.id,
            source = blueprint.source,
            role = blueprint.role,
            artha = blueprint.artha,
            evaluator = { _, state ->
                if (definition in state.samjnaDefinitions) {
                    SutraNirnaya.NotApplicable(
                        listOf("The saṃjñā definition is already registered."),
                    )
                } else {
                    SutraNirnaya.Applicable(
                        effects = listOf(DefineSamjna(definition)),
                        reasons = listOf("The interpretive saṃjñā sūtra defines a technical term."),
                    )
                }
            },
            relations = blueprint.relations,
            governance = blueprint.governance,
        )
    }
}
