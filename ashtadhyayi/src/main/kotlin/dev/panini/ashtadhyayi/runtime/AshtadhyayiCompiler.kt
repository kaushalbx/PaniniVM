package dev.panini.ashtadhyayi.runtime

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.sutra.ApplyDerivationChange
import dev.panini.derivation.sutra.DefineInterpretivePrinciple
import dev.panini.derivation.sutra.DefineSamjna
import dev.panini.derivation.sutra.DerivationAvastha
import dev.panini.derivation.sutra.InterpretivePrincipleDefinition
import dev.panini.derivation.sutra.SamjnaDefinition
import dev.panini.sutra.ContextualSamjnaAssignmentArtha
import dev.panini.sutra.ArthavatSutra
import dev.panini.sutra.ContextualProhibitionArtha
import dev.panini.sutra.InterpretivePrincipleArtha
import dev.panini.sutra.ProhibitionTarget
import dev.panini.sutra.SamjnaDefinitionArtha
import dev.panini.sutra.SamjnaSetDefinitionArtha
import dev.panini.sutra.SamjniSetDefinitionArtha
import dev.panini.sutra.runtime.RuntimeSutra
import dev.panini.sutra.runtime.SutraBlueprint
import dev.panini.sutra.runtime.SutraNirnaya

/** Lowers evaluator-free grammatical meanings into derivation runtime rules. */
object AshtadhyayiCompiler {
    fun compile(blueprint: SutraBlueprint): RuntimeSutra<DerivationAvastha> =
        when (blueprint.artha.kind) {
            ContextualProhibitionArtha.KIND ->
                compileContextualProhibition(blueprint)
            ContextualSamjnaAssignmentArtha.KIND ->
                compileContextualSamjnaAssignment(blueprint)
            InterpretivePrincipleArtha.KIND -> compileInterpretivePrinciple(blueprint)
            SamjnaDefinitionArtha.KIND -> compileSamjnaDefinition(blueprint)
            SamjnaSetDefinitionArtha.KIND -> compileSamjnaSetDefinition(blueprint)
            SamjniSetDefinitionArtha.KIND -> compileSamjniSetDefinition(blueprint)
            else -> error(
                "Unsupported derivation blueprint meaning '${blueprint.artha.kind}' for ${blueprint.id}.",
            )
        }

    private fun compileContextualProhibition(
        blueprint: SutraBlueprint,
    ): RuntimeSutra<DerivationAvastha> {
        val artha = ContextualProhibitionArtha.fromSutraArtha(blueprint.artha)
        require(artha.target == ProhibitionTarget.VIBHAKTI_FINAL_TUSMA) {
            "Unsupported prohibition target '${artha.target}' for ${blueprint.id}."
        }

        return RuntimeSutra(
            id = blueprint.id,
            source = blueprint.source,
            role = blueprint.role,
            artha = blueprint.artha,
            evaluator = { _, state ->
                val derivation = state.derivation
                val matching = derivation.terms.any { term ->
                    derivation.samjnas.any {
                        it.targetId == term.id && it.samjna == dev.panini.shiksha.Samjna.PRATYAYA
                    } && term.surface.hasTusmaEnding()
                }
                if (!matching) {
                    SutraNirnaya.NotApplicable(
                        listOf("No vibhakti has a final tu-s-ma sound."),
                    )
                } else {
                    SutraNirnaya.Applicable(
                        effects = listOf(
                            ApplyDerivationChange(
                                sutraId = blueprint.id,
                                change = DerivationChange(
                                    state = derivation.blockSutra(
                                        artha.prohibitedSutra.value,
                                        blueprint.id.value,
                                    ),
                                    explanation =
                                        "${blueprint.id} prohibits ${artha.prohibitedSutra} for the matching vibhakti.",
                                ),
                            ),
                        ),
                        reasons = listOf(
                            "A vibhakti-final tu-s-ma sound triggers the prohibition.",
                        ),
                    )
                }
            },
            relations = blueprint.relations,
            governance = blueprint.governance,
        )
    }

    private fun compileContextualSamjnaAssignment(
        blueprint: SutraBlueprint,
    ): RuntimeSutra<DerivationAvastha> {
        val artha = ContextualSamjnaAssignmentArtha.fromSutraArtha(blueprint.artha)
        val sutra = Ashtadhyayi.registry.require(blueprint.id.value)
        require((sutra as? ArthavatSutra)?.artha == artha) {
            "Blueprint meaning for ${blueprint.id} differs from the registered sūtra meaning."
        }
        val executable = sutra as? ContextualSamjnaSutra
            ?: error(
                "Contextual assignment ${blueprint.id} must implement ContextualSamjnaSutra.",
            )

        return RuntimeSutra(
            id = blueprint.id,
            source = blueprint.source,
            role = blueprint.role,
            artha = blueprint.artha,
            evaluator = { _, state ->
                val derivation = state.derivation
                if (!executable.hasSamjnaTarget(derivation)) {
                    SutraNirnaya.NotApplicable(
                        listOf("No material matching ${artha.target} requires designation."),
                    )
                } else {
                    SutraNirnaya.Applicable(
                        effects = listOf(
                            ApplyDerivationChange(
                                sutraId = blueprint.id,
                                change = executable.assignSamjna(derivation),
                            ),
                        ),
                        reasons = listOf(
                            "The registered sūtra assigns ${artha.samjna} to ${artha.target}.",
                        ),
                    )
                }
            },
            relations = blueprint.relations,
            governance = blueprint.governance,
        )
    }

    private fun String.hasTusmaEnding(): Boolean =
        TUSMA_ENDINGS.any(::endsWith)

    private val TUSMA_ENDINGS = setOf(
        "त्", "थ्", "द्", "ध्", "न्", "स्", "म्",
        "त", "थ", "द", "ध", "न", "स", "म",
    )

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

    private fun compileSamjniSetDefinition(
        blueprint: SutraBlueprint,
    ): RuntimeSutra<DerivationAvastha> {
        val artha = SamjniSetDefinitionArtha.fromSutraArtha(blueprint.artha)
        val definitions = artha.samjnis.mapTo(linkedSetOf()) { samjni ->
            SamjnaDefinition(
                samjni = samjni,
                samjna = artha.samjna,
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
                        listOf("The saṃjñin-set definition is already registered."),
                    )
                } else {
                    SutraNirnaya.Applicable(
                        effects = missing.map { DefineSamjna(it) },
                        reasons = listOf(
                            "The interpretive saṃjñā sūtra assigns one technical term to a set of concepts.",
                        ),
                    )
                }
            },
            relations = blueprint.relations,
            governance = blueprint.governance,
        )
    }
}
