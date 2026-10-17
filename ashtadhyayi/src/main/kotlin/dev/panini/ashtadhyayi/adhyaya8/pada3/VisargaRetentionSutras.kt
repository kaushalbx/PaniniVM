package dev.panini.ashtadhyayi.adhyaya8.pada3

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.derivation.*
import dev.panini.pratyahara.Pratyahara
import dev.panini.shiksha.*
import dev.panini.sutra.*

internal data class VisargaBoundary(val left: DerivationTerm, val right: DerivationTerm, val following: List<Varna>)

/** Written visarga is already explicit; no underlying s/r origin is inferred. */
internal fun visargaBoundaries(context: DerivationState): Sequence<VisargaBoundary> = sequence {
    for ((index, left) in context.terms.withIndex()) {
        if (left.varnas.lastOrNull() != Ayogavaha.VISARGA) continue
        val followingTerms = context.terms.drop(index + 1)
        val right = followingTerms.firstOrNull { it.varnas.isNotEmpty() } ?: continue
        val following = followingTerms.asSequence().flatMap { it.varnas.asSequence() }.take(2).toList()
        yield(VisargaBoundary(left, right, following))
    }
}

object SharpareVisarjaniyahSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.3.35", text = "शर्परे विसर्जनीयः",
    hindiExplanation = "जिस खर् के तुरन्त बाद शर् हो, उसके पहले विसर्ग का विसर्गादेश होता है।",
    type = SutraType.APAVADA, chapter = 8, pada = 3, optional = false, kramaValue = 830035,
    role = SutraRole.Apavada, action = SutraAction.ADESHA, scope = SutraScope.VARNA,
    stage = SutraStage.SIBILANT_SANDHI, blocks = setOf("8.3.34", "8.3.36", "8.3.37"),
), DerivationSutra {
    override val advancesTripadiFrontier: Boolean = false
    private fun target(context: DerivationState): VisargaBoundary? = visargaBoundaries(context).firstOrNull { boundary ->
        !context.isBlockedAtBoundary("8.3.34", boundary.left.id, boundary.right.id) &&
            boundary.following.size == 2 &&
            Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.KHAR, boundary.following[0]) &&
            Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.SHAR, boundary.following[1])
    }
    override fun matches(context: DerivationState): Boolean = target(context) != null
    override fun apply(context: DerivationState): DerivationChange {
        val (left, right) = requireNotNull(target(context))
        var protected = context
        for (rule in blocks) protected = protected.blockAtBoundary(rule, left.id, right.id, sutra)
        return DerivationChange(protected,
            "8.3.35 retains visarga before khar immediately followed by śar, preventing its alterations at this boundary.")
    }
}

object VaShariSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.3.36", text = "वा शरि",
    hindiExplanation = "शर् परे होने पर विसर्ग का विकल्प से विसर्गादेश होता है।",
    type = SutraType.VIBHASHA, chapter = 8, pada = 3, optional = true, kramaValue = 830036,
    role = SutraRole.Apavada, action = SutraAction.ADESHA, scope = SutraScope.VARNA,
    stage = SutraStage.SIBILANT_SANDHI, blocks = setOf("8.3.34"),
), DerivationSutra {
    override val advancesTripadiFrontier: Boolean = false
    private fun target(context: DerivationState): VisargaBoundary? = visargaBoundaries(context).firstOrNull { boundary ->
        !context.isBlockedAtBoundary("8.3.34", boundary.left.id, boundary.right.id) &&
            !context.isBlockedAtBoundary(sutra, boundary.left.id, boundary.right.id) &&
            Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.SHAR, boundary.following.first())
    }
    override fun matches(context: DerivationState): Boolean = target(context) != null
    override fun apply(context: DerivationState): DerivationChange {
        val (left, right) = requireNotNull(target(context))
        return DerivationChange(context.blockAtBoundary("8.3.34", left.id, right.id, sutra),
            "8.3.36 optionally retains visarga before śar; declining it leaves the 8.3.34 s-substitution branch.")
    }
}
