package dev.panini.ashtadhyayi.adhyaya8.pada3

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.core.Lakara
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.TermKind
import dev.panini.pratyahara.Pratyahara
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

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
        val term = findRetroflexTarget(context) ?: return DerivationChange(context, "8.3.59: No match found")
        val newSurface = term.surface.replace('स', 'ष')
        return DerivationChange(
            state = context.replaceTerm(term.id, term.copy(surface = newSurface))
                .copy(stage = DerivationStage.FINAL),
            explanation = "8.3.59: Retroflexed 's' to 'ṣ' after Iṇ/Ku."
        )
    }

    private fun findRetroflexTarget(context: DerivationState): DerivationTerm? {
        val isSipLet = context.allEffectiveTerms.any { it.id == "sip-aorist" }
        val isLungSic = context.effectiveContext.rupa.lakara == Lakara.LUNG &&
            context.allEffectiveTerms.any { it.upadesha == "सिच्" }
        val isLabhPerfect = context.effectiveContext.rupa.lakara == Lakara.LIT &&
            context.allEffectiveTerms.any { it.kind == TermKind.DHATU && it.upadesha == "डुलभँष्" }
        val isFutureSya = context.effectiveContext.rupa.lakara in setOf(Lakara.LRT, Lakara.LRNG) &&
            context.allEffectiveTerms.any { it.upadesha == "स्य" }
        if (context.stage != DerivationStage.PADA_FORMED && context.stage != DerivationStage.FINAL &&
            !isSipLet && !isLungSic && !isLabhPerfect && !isFutureSya
        ) return null
        if (isSipLet) {
            val sipIndex = context.terms.indexOfFirst { it.id == "sip-aorist" && 'स' in it.surface }
            if (sipIndex > 0 && context.terms[sipIndex - 1].surface.endsWith("इ")) {
                return context.terms[sipIndex]
            }
        }
        if (isLungSic) {
            val sicIndex = context.terms.indexOfFirst { it.upadesha == "सिच्" && 'स' in it.surface }
            if (sicIndex > 0 && context.terms[sicIndex - 1].surface.endsWith("इ")) {
                return context.terms[sicIndex]
            }
        }
        if (isLabhPerfect) {
            val endingIndex = context.terms.indexOfFirst { it.upadesha == "थास्" && 'स' in it.surface }
            if (endingIndex > 0 && context.terms[endingIndex - 1].surface.endsWith("इ")) {
                return context.terms[endingIndex]
            }
        }

        val engine = Ashtadhyayi.pratyaharaEngine
        val surface = context.surface
        for (i in 0 until context.terms.size) {
            val term = context.terms[i]
            if (term.kind != TermKind.PRATYAYA) continue
            val termSurface = term.surface

            val sIndex = termSurface.indexOf('स')
            if (sIndex == -1) continue

            // 8.3.55: apādāntasya - target must not be at the end of the word
            val prefixLength = context.terms.take(i).sumOf { it.surface.length }
            val absoluteSIndex = prefixLength + sIndex
            val isAtEnd = absoluteSIndex == surface.length - 1 ||
                (absoluteSIndex == surface.length - 2 && surface.endsWith('्'))
            val followsStandaloneTanadiU = i > 0 &&
                context.terms[i - 1].id == "tanadi-u" && context.terms[i - 1].surface == "उ"
            if (isAtEnd && !followsStandaloneTanadiU) continue

            val preChar = if (sIndex == 0) {
                if (i == 0) continue
                val stem = context.terms[i - 1].surface
                val stemFinal = stem.lastOrNull() ?: continue
                when {
                    stemFinal == '्' && stem.length >= 2 -> stem[stem.length - 2]
                    stemFinal !in dev.panini.shiksha.Varnamala.independentVowelsOrMarks -> 'अ'
                    else -> stemFinal
                }
            } else {
                termSurface[sIndex - 1]
            }

            val isInIn = engine.contains(Pratyahara.IN, independentVowel(preChar))
            val isInKu = isKu(preChar)
            val isTanadiStrongStem = i > 0 &&
                context.terms[i - 1].id == "tanadi-u" && context.terms[i - 1].surface == "ओ"

            if (!isTanadiStrongStem && (isInIn || isInKu)) return term
        }
        return null
    }

    private fun independentVowel(c: Char): Char = when (c) {
        'ि' -> 'इ'
        'ी' -> 'ई'
        'ु' -> 'उ'
        'ू' -> 'ऊ'
        'ृ' -> 'ऋ'
        'ॄ' -> 'ॠ'
        'ॢ' -> 'ऌ'
        'े' -> 'ए'
        'ै' -> 'ऐ'
        'ो' -> 'ओ'
        'ौ' -> 'औ'
        else -> c
    }

    private fun isKu(c: Char): Boolean = c in setOf('क', 'ख', 'ग', 'घ', 'ङ')
}
