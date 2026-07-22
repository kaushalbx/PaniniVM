package dev.panini.ashtadhyayi.adhyaya6.pada1

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.core.Lakara
import dev.panini.derivation.TingAffix
import dev.panini.shiksha.Samjna
import dev.panini.pratyahara.Pratyahara
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/** 6.1.66: lopo vyor vali. Deletes final व् or य् before a consonant. */
object LopoVyorValiSutra : Sutra<DerivationState, DerivationChange>(
    number = "6.1.66",
    text = "लोपो व्योर्वलि",
    hindiExplanation = "वल् परे होने पर व् और य् का लोप होता है।",
    type = SutraType.NITYA,
    chapter = 6,
    pada = 1,
    optional = false,
    kramaValue = 610066,
    role = SutraRole.Vidhi,
    action = SutraAction.LOPA,
    scope = SutraScope.VARNA,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        for (index in 0 until context.terms.lastIndex) {
            val leftTerm = context.terms[index]
            if (leftTerm.id in setOf("yasut", "nic")) continue
            val left = leftTerm.surface
            val rightTerm = context.terms[index + 1]
            val hasPadaScope = context.samjnas.any { it.targetId == leftTerm.id && it.samjna == Samjna.PADA }
            val isLateLingVikarana = context.effectiveContext.rupa.lakara == Lakara.LING &&
                leftTerm.upadesha in setOf("शप्", "श्यन्", "श") &&
                TingAffix.entries.any { it.upadesha == rightTerm.upadesha }
            val isLingSiyut = context.effectiveContext.rupa.lakara == Lakara.LING &&
                leftTerm.id == "siyut" && TingAffix.entries.any { it.upadesha == rightTerm.upadesha }
            if (!hasPadaScope && !isLateLingVikarana && !isLingSiyut) continue
            if (context.effectiveContext.rupa.lakara == Lakara.LUNG &&
                leftTerm.id == "vuk" && rightTerm.upadesha in setOf("च्लि", "सिच्")
            ) continue
            if (context.effectiveContext.rupa.lakara == Lakara.LING &&
                rightTerm.matchesUpadesha("मिप्") &&
                context.allEffectiveTerms.any { it.matchesUpadesha("यासुट्") }
            ) continue
            val right = rightTerm.surface.firstOrNull() ?: continue
            if ((left.endsWith("व्") || left.endsWith("य्")) &&
                Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.HAL, right)
            ) return true
        }
        return false
    }

    override fun apply(context: DerivationState): DerivationChange {
        for (index in 0 until context.terms.lastIndex) {
            val left = context.terms[index]
            if (left.id in setOf("yasut", "nic")) continue
            val rightTerm = context.terms[index + 1]
            val hasPadaScope = context.samjnas.any { it.targetId == left.id && it.samjna == Samjna.PADA }
            val isLateLingVikarana = context.effectiveContext.rupa.lakara == Lakara.LING &&
                left.upadesha in setOf("शप्", "श्यन्", "श") &&
                TingAffix.entries.any { it.upadesha == rightTerm.upadesha }
            val isLingSiyut = context.effectiveContext.rupa.lakara == Lakara.LING &&
                left.id == "siyut" && TingAffix.entries.any { it.upadesha == rightTerm.upadesha }
            if (!hasPadaScope && !isLateLingVikarana && !isLingSiyut) continue
            if (context.effectiveContext.rupa.lakara == Lakara.LUNG &&
                left.id == "vuk" && rightTerm.upadesha in setOf("च्लि", "सिच्")
            ) continue
            if (context.effectiveContext.rupa.lakara == Lakara.LING &&
                rightTerm.matchesUpadesha("मिप्") &&
                context.allEffectiveTerms.any { it.matchesUpadesha("यासुट्") }
            ) continue
            val right = rightTerm.surface.firstOrNull() ?: continue
            if ((left.surface.endsWith("व्") || left.surface.endsWith("य्")) &&
                Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.HAL, right)
            ) {
                return DerivationChange(
                    context.replaceTerm(left.id, left.copy(surface = left.surface.dropLast(2))),
                    "6.1.66 deletes the final ${left.surface.takeLast(2)} before val.",
                )
            }
        }
        return DerivationChange(context, "6.1.66 found no eligible व् or य्.")
    }
}
