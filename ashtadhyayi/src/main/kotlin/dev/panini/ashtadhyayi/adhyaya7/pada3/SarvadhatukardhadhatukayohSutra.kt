package dev.panini.ashtadhyayi.adhyaya7.pada3

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.ashtadhyayi.adhyaya1.pada2.AsamyogallitKitSutra
import dev.panini.core.DhatuGana
import dev.panini.core.Lakara
import dev.panini.core.PadaType
import dev.panini.core.TingAffix
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationalEnvironment
import dev.panini.derivation.HasDerivationalEnvironment
import dev.panini.derivation.TermKind
import dev.panini.derivation.VarnaSubstitution
import dev.panini.pratyahara.Pratyahara
import dev.panini.shiksha.ItStatus
import dev.panini.shiksha.Varnamala
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 7.3.84: sārvadhātukārdhadhātukayoḥ.
 * Substitutes guna for the final ik vowel of an anga before a sarvadhatuka or ardhadhatuka affix.
 * Now correctly checks for the 'Aṅgasya' jurisdiction (6.4.1).
 */
object SarvadhatukardhadhatukayohSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.3.84",
    text = "सार्वधातुकार्धधातुकयोः",
    hindiExplanation = "सार्वधातुक या आर्धधातुक प्रत्यय परे होने पर अङ्ग के अन्त्य इक् का गुण होता है।",
    type = SutraType.NITYA,
    chapter = 7,
    pada = 3,
    optional = false,
    kramaValue = 730084,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.DHATU,
    dependencies = setOf("6.4.1")
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        // Jurisdictional check: Must be in the Aṅga section
        if ("6.4.1" !in context.activeAdhikaras) return false

        val strongUGrade = strongUGrade(context)
        if (strongUGrade != null) return Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.IK, strongUGrade.surface.last())

        val stemIndex = context.terms.indexOfFirst { it.kind == TermKind.DHATU && it.id != "abhyasa" }
        if (stemIndex < 0) return false
        val stem = context.terms[stemIndex]
        if (stem.gana == DhatuGana.JUHOTYADI && context.droppedTerms.none { it.upadesha == "शप्" }) return false
        if (stem.gana == DhatuGana.BHVADI &&
            context.effectiveContext.rupa.lakara in setOf(Lakara.LAT, Lakara.LOT, Lakara.LANG, Lakara.LING, Lakara.LET) &&
            context.allEffectiveTerms.none { it.upadesha == "शप्" }
        ) return false
        val affix = context.terms.getOrNull(stemIndex + 1) ?: return false
        if (stem.gana == DhatuGana.KRYADI && context.terms.any { it.upadesha == "श्ना" }) return false
        if (stem.gana == DhatuGana.JUHOTYADI) {
            val isLangJus = context.effectiveContext.rupa.lakara == Lakara.LANG &&
                context.terms.lastOrNull()?.upadesha == TingAffix.JHI.upadesha
            if ((!isLangJus && context.terms.lastOrNull()?.upadesha !in strongAffixes(context).map { it.upadesha }) ||
                !lotAtmanepadaReady(context)
            ) return false
        }
        if (affix.kind != TermKind.PRATYAYA) return false
        if ("1.2.5" in affix.establishedBySutras || AsamyogallitKitSutra.matches(context)) return false
        if (context.effectiveContext.rupa.lakara == Lakara.LIT && affix.surface == affix.upadesha) return false
        if (context.effectiveContext.rupa.lakara == Lakara.LIT && stem.itStatus == ItStatus.SET) return false
        if (context.effectiveContext.rupa.lakara == Lakara.LUNG && stem.matchesUpadesha("भू")) return false

        val isSarvaOrArdha = HasDerivationalEnvironment(DerivationalEnvironment.ARDHADHATUKA).matches(context) ||
            affix.id == "shap" || affix.id.startsWith("ting-")

        if (!isSarvaOrArdha) return false

        // Guna should not apply to the it-augment
        if (context.allEffectiveTerms.any { it.id == "it-agama" }) return false

        val lastChar = stem.surface.lastOrNull() ?: return false
        return Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.IK, lastChar)
    }

    override fun apply(context: DerivationState): DerivationChange {
        val stem = strongUGrade(context)
            ?: context.terms.first { it.kind == TermKind.DHATU && it.id != "abhyasa" }
        val lastChar = stem.surface.last()
        val replacement = requireNotNull(Varnamala.getGuna(lastChar))
        val newSurface = stem.surface.dropLast(1) + replacement

        return DerivationChange(
            state = context.replaceTerm(stem.id, stem.copy(surface = newSurface))
                .copy(stage = DerivationStage.ANGAKARYA)
                .addSubstitution(VarnaSubstitution(stem.id, lastChar, replacement, sutra)),
            explanation = "7.3.84: Applied guna ($replacement) within Aṅgasya jurisdiction."
        )
    }

    private fun strongUGrade(context: DerivationState) =
        context.terms.firstOrNull { it.id in setOf("shnu", "tanadi-u") }?.takeIf {
            context.terms.none { term -> term.id == "yasut" || term.id == "siyut" } &&
                context.terms.lastOrNull()?.upadesha in strongAffixes(context).map { affix -> affix.upadesha } &&
                lotAtmanepadaReady(context)
        }

    private fun strongAffixes(context: DerivationState): Set<TingAffix> =
        when (context.effectiveContext.rupa.lakara) {
            Lakara.LOT -> setOf(
                TingAffix.TIP, TingAffix.MIP, TingAffix.VAS, TingAffix.MAS,
                TingAffix.IT, TingAffix.VAHI, TingAffix.MAHING,
            )
            Lakara.LING -> emptySet()
            else -> setOf(TingAffix.TIP, TingAffix.SIP, TingAffix.MIP)
        }

    private fun lotAtmanepadaReady(context: DerivationState): Boolean {
        if (context.effectiveContext.rupa.lakara != Lakara.LOT) return true
        val affix = TingAffix.entries.firstOrNull { it.upadesha == context.terms.lastOrNull()?.upadesha } ?: return true
        return affix.pada != PadaType.ATMANEPADA ||
            context.allEffectiveTerms.any { it.id == "lot-at-agama" }
    }
}
