package dev.panini.derivation.sutra

import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.RuleVisibility
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraGovernance
import dev.panini.sutra.runtime.RuntimeSutra
import dev.panini.sutra.runtime.SutraEffect
import dev.panini.sutra.runtime.SutraEffectApplication
import dev.panini.sutra.runtime.SutraEffectInterpreter
import dev.panini.sutra.runtime.SutraId
import dev.panini.sutra.runtime.SutraNirnaya
import dev.panini.sutra.runtime.SutraRelation
import dev.panini.sutra.runtime.SutraSource

/**
 * Compatibility bridge for executing one already-selected legacy derivation
 * rule through the shared runtime model. Conflict resolution and agenda
 * scheduling remain owned by DerivationEngine during this migration stage.
 */
object DerivationSutraRuntimeAdapter {
    fun adapt(legacy: DerivationSutra): RuntimeSutra<DerivationAvastha> {
        val id = SutraId(legacy.sutra)
        val catalogSutra = legacy as? Sutra<*, *>
        return RuntimeSutra(
            id = id,
            source = SutraSource.Ashtadhyayi(
                number = legacy.sutra,
                text = catalogSutra?.text ?: legacy.sutra,
            ),
            role = legacy.role,
            evaluator = { _, state ->
                val blocker = state.derivation.blockedSutras[legacy.sutra]
                when {
                    blocker != null -> SutraNirnaya.Blocked(
                        blocker = SutraId(blocker),
                        reasons = listOf("The derivation state prohibits this sūtra."),
                    )
                    !RuleVisibility.permits(legacy, state.derivation) ->
                        SutraNirnaya.NotApplicable(
                            listOf("The sūtra is not visible in the current derivation regime."),
                        )
                    else -> {
                        val visible = RuleVisibility.view(
                            legacy,
                            state.derivation,
                            mapOf(legacy.sutra to legacy),
                        )
                        if (!legacy.matches(visible)) {
                            SutraNirnaya.NotApplicable(
                                listOf("The grammatical condition does not match."),
                            )
                        } else {
                            SutraNirnaya.Applicable(
                                effects = listOf(
                                    ApplyDerivationChange(
                                        sutraId = id,
                                        change = legacy.apply(state.derivation),
                                    ),
                                ),
                                reasons = listOf("The legacy derivation condition matches."),
                            )
                        }
                    }
                }
            },
            relations = legacy.blocks.mapTo(linkedSetOf()) {
                SutraRelation.Blocks(SutraId(it))
            },
            governance = SutraGovernance(
                optional = legacy.optional,
                priority = legacy.priority,
                blocks = legacy.blocks,
                visibility = legacy.visibility,
            ),
        )
    }
}
object DerivationSutraEffectInterpreter : SutraEffectInterpreter<DerivationAvastha> {
    override fun apply(
        effect: SutraEffect<DerivationAvastha>,
        state: DerivationAvastha,
    ): SutraEffectApplication<DerivationAvastha> {
        if (effect !is ApplyDerivationChange) {
            return SutraEffectApplication.Failed(
                "Unsupported derivation sūtra effect: ${effect::class.simpleName}",
            )
        }
        return SutraEffectApplication.Applied(
            state = state.copy(
                derivation = effect.change.state,
                appliedSutras = state.appliedSutras + effect.sutraId,
                explanations = state.explanations + effect.change.explanation,
            ),
            explanation = effect.change.explanation,
        )
    }
}
