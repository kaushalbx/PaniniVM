package dev.panini.ashtadhyayi.adhyaya6.pada1

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.SamjnaAssignment
import dev.panini.derivation.TermKind
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.shiksha.firstVarna
import dev.panini.shiksha.lastVarna
import dev.panini.shiksha.toDevanagari
import dev.panini.shiksha.toVarnas
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

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
        val isAPrefix = prefix.surface.lastVarna() in setOf(Svara.A, Svara.AA)
        if (!isAPrefix) return false

        // 2. Root must start with 'e' or 'o'
        return root.surface.firstVarna() in setOf(Svara.E, Svara.O)
    }

    override fun apply(context: DerivationState): DerivationChange {
        val terms = context.terms
        val prefix = terms[terms.size - 2]
        val root = terms.last()

        val prefixVarnas = prefix.varnas
        val rootVarnas = root.varnas
        val source = prefixVarnas.last()
        val replacement = listOf(rootVarnas.first())
        val newSurface = (prefixVarnas.dropLast(1) + replacement + rootVarnas.drop(1)).toDevanagari()

        return DerivationChange(
            state = context.mergeTermsByVarnaSubstitution(
                prefix.id, root.id, newSurface, source, replacement, sutra,
            ).copy(stage = DerivationStage.ANGAKARYA),
            explanation = "6.1.94: Pararūpa substitution (${replacement.toDevanagari()}) for prefix-a + root-e/o."
        )
    }
}
