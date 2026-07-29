package dev.panini.derivation.sutra

import dev.panini.sutra.InterpretivePrincipleArtha
import dev.panini.sutra.SamjnaDefinitionArtha
import dev.panini.sutra.SamjnaSetDefinitionArtha
import dev.panini.sutra.runtime.RuntimeSutra
import dev.panini.sutra.runtime.SutraBlueprint
import dev.panini.sutra.runtime.SutraNirnaya

/** Lowers evaluator-free grammatical meanings into derivation runtime rules. */
object DerivationBlueprintCompiler {
    fun compile(blueprint: SutraBlueprint): RuntimeSutra<DerivationAvastha> =
        when (blueprint.artha.kind) {
            InterpretivePrincipleArtha.KIND -> compileInterpretivePrinciple(blueprint)
            SamjnaDefinitionArtha.KIND -> compileSamjnaDefinition(blueprint)
            SamjnaSetDefinitionArtha.KIND -> compileSamjnaSetDefinition(blueprint)
            else -> error(
                "Unsupported derivation blueprint meaning '${blueprint.artha.kind}' for ${blueprint.id}.",
            )
        }

    private fun compileInterpretivePrinciple(
        blueprint: SutraBlueprint,
    ): RuntimeSutra<DerivationAvastha> {
        val artha = InterpretivePrincipleArtha.fromSutraArtha(blueprint.artha)
        val definition = InterpretivePrincipleDefinition(
            principle = artha.principle,
            definingSutra = blueprint.id,
        )

        return RuntimeSutra(
            id = blueprint.id,
            source = blueprint.source,
            role = blueprint.role,
            artha = blueprint.artha,
            evaluator = { _, state ->
                if (definition in state.interpretivePrinciples) {
                    SutraNirnaya.NotApplicable(
                        listOf("The interpretive principle is already registered."),
                    )
                } else {
                    SutraNirnaya.Applicable(
                        effects = listOf(DefineInterpretivePrinciple(definition)),
                        reasons = listOf("The paribhāṣā establishes an interpretation principle."),
                    )
                }
            },
            relations = blueprint.relations,
            governance = blueprint.governance,
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

    private fun compileSamjnaSetDefinition(
        blueprint: SutraBlueprint,
    ): RuntimeSutra<DerivationAvastha> {
        val artha = SamjnaSetDefinitionArtha.fromSutraArtha(blueprint.artha)
        val definitions = artha.samjnas.mapTo(linkedSetOf()) { samjna ->
            SamjnaDefinition(
                samjni = artha.samjni,
                samjna = samjna,
                definingSutra = blueprint.id,
            )
        }

        return RuntimeSutra(
            id = blueprint.id,
            source = blueprint.source,
            role = blueprint.role,
            artha = blueprint.artha,
            evaluator = { _, state ->
                val missing = definitions - state.samjnaDefinitions
                if (missing.isEmpty()) {
                    SutraNirnaya.NotApplicable(
                        listOf("The saṃjñā-set definition is already registered."),
                    )
                } else {
                    SutraNirnaya.Applicable(
                        effects = missing.map { DefineSamjna(it) },
                        reasons = listOf(
                            "The interpretive saṃjñā sūtra defines a set of technical terms.",
                        ),
                    )
                }
            },
            relations = blueprint.relations,
            governance = blueprint.governance,
        )
    }
}
