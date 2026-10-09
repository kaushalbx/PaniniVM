package dev.panini.ashtadhyayi.adhyaya8.pada3

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.core.Lakara
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.ItProcessingPhase
import dev.panini.derivation.TermKind
import dev.panini.pratyahara.Pratyahara
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Varna
import dev.panini.shiksha.Vyanjana

/**
 * 8.3.59: ādeśapratyayayoḥ.
 * Substitutes 'ṣ' for 's' if 's' is part of an ādeśa (substitute) or pratyaya (affix),
 * and is preceded by a sound in the Iṇ pratyāhāra or Ku (ka-varga).
 */
object AdesapratyayayohSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.3.59",
    text = "आदेशप्रत्यययोः",
    hindiExplanation = "इण् (इ, उ, ऋ, लृ, ए, ओ, ऐ, औ, ह, य, व, र, ल) या कु (क-वर्ग) के बाद आदेश या प्रत्यय के 'स' का 'ष' होता है।",
    type = SutraType.NITYA,
    chapter = 8,
    pada = 3,
    optional = false,
    kramaValue = 830059,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean = findRetroflexTarget(context) != null

    override fun apply(context: DerivationState): DerivationChange {
        val target = findRetroflexTarget(context) ?: return DerivationChange(context, "8.3.59: No match found")
        return DerivationChange(
            state = context.replaceTermVarna(
                target.term.id,
                target.varnaIndex,
                listOf(Vyanjana.SSA),
                sutra,
            )
                .copy(stage = DerivationStage.FINAL),
            explanation = "8.3.59: Retroflexed 's' to 'ṣ' after Iṇ/Ku."
        )
    }

    private fun findRetroflexTarget(context: DerivationState): RetroflexTarget? {
        val isSipLet = context.allEffectiveTerms.any { it.id == "sip-aorist" }
        val isLungSic = context.effectiveContext.rupa.lakara == Lakara.LUNG &&
            context.allEffectiveTerms.any { it.upadesha == "सिँच्" || it.upadesha == "क्स" }
        val isLabhPerfect = context.effectiveContext.rupa.lakara == Lakara.LIT &&
            context.allEffectiveTerms.any { it.kind == TermKind.DHATU && it.upadesha == "डुलभँष्" }
        val isFutureSya = context.effectiveContext.rupa.lakara in setOf(Lakara.LRT, Lakara.LRNG) &&
            context.allEffectiveTerms.any { it.upadesha == "स्य" }
        if (context.stage != DerivationStage.PADA_FORMED && context.stage != DerivationStage.FINAL &&
            !isSipLet && !isLungSic && !isLabhPerfect && !isFutureSya
        ) return null
        if (isSipLet) {
            val sipIndex = context.terms.indexOfFirst { it.id == "sip-aorist" && Vyanjana.SA in it.varnas }
            if (sipIndex > 0 && context.terms[sipIndex - 1].varnas.lastOrNull() == Svara.I) {
                return RetroflexTarget(context.terms[sipIndex], context.terms[sipIndex].varnas.indexOf(Vyanjana.SA))
            }
        }
        if (isLungSic) {
            val sicIndex = context.terms.indexOfFirst { it.upadesha == "सिँच्" && Vyanjana.SA in it.varnas }
            if (sicIndex > 0 && context.terms[sicIndex - 1].varnas.lastOrNull() == Svara.I) {
                return RetroflexTarget(context.terms[sicIndex], context.terms[sicIndex].varnas.indexOf(Vyanjana.SA))
            }
        }
        if (isLabhPerfect) {
            val endingIndex = context.terms.indexOfFirst { it.upadesha == "थास्" && Vyanjana.SA in it.varnas }
            if (endingIndex > 0 && context.terms[endingIndex - 1].varnas.lastOrNull() == Svara.I) {
                return RetroflexTarget(context.terms[endingIndex], context.terms[endingIndex].varnas.indexOf(Vyanjana.SA))
            }
        }

        val engine = Ashtadhyayi.pratyaharaEngine
        for (i in 0 until context.terms.size) {
            val term = context.terms[i]
            if (term.kind != TermKind.PRATYAYA) continue
            if (term.itProcessingPhase != ItProcessingPhase.PROCESSED) continue
            if (term.itDesignations.isNotEmpty() || term.deferredItDesignations.isNotEmpty()) continue
            val sIndex = term.varnas.indexOf(Vyanjana.SA)
            if (sIndex == -1) continue

            // 8.3.55: apādāntasya - target must not be at the end of the word
            val isAtEnd = term.varnas.drop(sIndex + 1).isEmpty() &&
                context.terms.drop(i + 1).all { it.varnas.isEmpty() }
            val followsStandaloneTanadiU = i > 0 &&
                context.terms[i - 1].id == "tanadi-u" && context.terms[i - 1].varnas == listOf(Svara.U)
            if (isAtEnd && !followsStandaloneTanadiU) continue

            val precedingVarna = if (sIndex == 0) {
                if (i == 0) continue
                context.terms[i - 1].varnas.lastOrNull() ?: continue
            } else {
                term.varnas[sIndex - 1]
            }

            val isInIn = engine.contains(Pratyahara.IN, precedingVarna)
            val isInKu = precedingVarna in ku
            val isTanadiStrongStem = i > 0 &&
                context.terms[i - 1].id == "tanadi-u" && context.terms[i - 1].varnas == listOf(Svara.O)

            if (!isTanadiStrongStem && (isInIn || isInKu)) return RetroflexTarget(term, sIndex)
        }
        return null
    }

    private data class RetroflexTarget(val term: DerivationTerm, val varnaIndex: Int)

    private val ku: Set<Varna> = setOf(Vyanjana.KA, Vyanjana.KHA, Vyanjana.GA, Vyanjana.GHA, Vyanjana.NGA)
}
