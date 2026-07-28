package dev.panini.unadipatha.sutra

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.TermKind
import dev.panini.derivation.sutra.ApplyDerivationChange
import dev.panini.derivation.sutra.DerivationAvastha
import dev.panini.sutra.SutraGovernance
import dev.panini.sutra.SutraRole
import dev.panini.sutra.runtime.RuntimeSutra
import dev.panini.sutra.runtime.SutraNirnaya
import dev.panini.unadipatha.UnadiDerivationBridge
import dev.panini.unadipatha.UnadiSutra

/**
 * Extension method converting an [UnadiSutra] directly into a [RuntimeSutra] using its native metadata.
 */
fun UnadiSutra.toRuntimeSutra(): RuntimeSutra<DerivationAvastha> = RuntimeSutra(
    id = sutraId,
    source = source,
    role = SutraRole.Vidhi,
    artha = artha,
    evaluator = { _, state ->
        val rootTerm = state.derivation.terms.firstOrNull { it.kind == TermKind.DHATU }
        if (rootTerm == null) {
            SutraNirnaya.NotApplicable(listOf("No dhātu term found in derivation state."))
        } else {
            val matchingRoot = roots.firstOrNull { r ->
                r.upadesha == rootTerm.upadesha ||
                    r.sourceSurface == rootTerm.surface ||
                    r.upadesha == rootTerm.surface ||
                    (r.surfaceAliases.isNotEmpty() && r.surfaceAliases.contains(rootTerm.surface))
            }
            if (matchingRoot == null) {
                SutraNirnaya.NotApplicable(listOf("Dhātu '${rootTerm.surface}' does not match sūtra $number."))
            } else {
                val match = matchFor(matchingRoot)
                val bridgedState = UnadiDerivationBridge.createInitialState(matchingRoot, match)
                SutraNirnaya.Applicable(
                    effects = listOf(
                        ApplyDerivationChange(
                            sutraId = sutraId,
                            change = DerivationChange(
                                state = bridgedState,
                                explanation = "Applied Uṇādi sūtra $number ($text) assigning '${match.pratyaya}' to '${matchingRoot.upadesha}'.",
                            ),
                        ),
                    ),
                    reasons = listOf("Dhātu matches Uṇādi sūtra condition."),
                )
            }
        }
    },
    relations = emptySet(),
    governance = SutraGovernance(),
)
