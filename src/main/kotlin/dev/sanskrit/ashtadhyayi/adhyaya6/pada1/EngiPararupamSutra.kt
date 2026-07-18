package dev.sanskrit.ashtadhyayi.adhyaya6.pada1

import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationStage
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.derivation.SamjnaAssignment
import dev.sanskrit.derivation.TermKind
import dev.sanskrit.derivation.VarnaSubstitution
import dev.sanskrit.shiksha.Samjna
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

/**
 * 6.1.94: eṅi pararūpam.
 * When a prefix ending in 'a' or 'ā' is followed by a root starting with 'e' or 'o',
 * the single substitute for both is the latter (pararūpa).
 * This is an exception to 6.1.88 (Vṛddhir eci).
 */
object EngiPararupamSutra : Sutra<DerivationState, DerivationChange>(
    number = "6.1.94",
    text = "एङि पररूपम्",
    hindiExplanation = "अकारान्त उपसर्ग के बाद यदि एङ् (ए, ओ) से आरम्भ होने वाली धातु हो, तो पररूप एकादेश होता है।",
    type = SutraType.APAVADA,
    chapter = 6,
    pada = 1,
    optional = false,
    kramaValue = 610094,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.terms.size < 2) return false
        val prefix = context.terms[context.terms.size - 2]
        val root = context.terms.last()
        if (root.kind != TermKind.DHATU || SamjnaAssignment(prefix.id, Samjna.UPASARGA) !in context.samjnas) return false

        // 1. Must be an a-ending prefix.
        val isAPrefix = prefix.surface.endsWith('अ') || prefix.surface.endsWith('ा')
        if (!isAPrefix) return false

        // 2. Root must start with 'e' or 'o'
        val firstChar = root.surface.firstOrNull() ?: return false
        return firstChar == 'ए' || firstChar == 'ओ' || firstChar == 'े' || firstChar == 'ो'
    }

    override fun apply(context: DerivationState): DerivationChange {
        val terms = context.terms
        val prefix = terms[terms.size - 2]
        val root = terms.last()

        val replacement = root.surface.first().toString()
        val newSurface = prefix.surface.dropLast(1) + replacement + root.surface.drop(1)

        return DerivationChange(
            state = context.copy(
                terms = terms.dropLast(2) + prefix.copy(surface = newSurface),
                stage = DerivationStage.ANGAKARYA
            ).addSubstitution(VarnaSubstitution(prefix.id, prefix.surface.last(), replacement, sutra)),
            explanation = "6.1.94: Pararūpa substitution ($replacement) for prefix-a + root-e/o."
        )
    }
}
