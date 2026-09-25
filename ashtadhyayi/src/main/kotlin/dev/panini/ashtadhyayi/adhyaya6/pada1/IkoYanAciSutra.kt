package dev.panini.ashtadhyayi.adhyaya6.pada1

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.core.Vacana
import dev.panini.core.Vibhakti
import dev.panini.core.Lakara
import dev.panini.core.TingAffix
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.pratyahara.Pratyahara
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Varna
import dev.panini.shiksha.Varnamala
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.firstVarna
import dev.panini.shiksha.lastVarna
import dev.panini.shiksha.toDevanagari
import dev.panini.shiksha.toVarnas
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraStage
import dev.panini.sutra.SutraType

/** 6.1.77: Substituting yan for ik vowels before an ac (vowel). */
object IkoYanAciSutra : Sutra<DerivationState, DerivationChange>(
    number = "6.1.77",
    text = "इको यणचि",
    hindiExplanation = "इक् (इ, उ, ऋ, लृ) के स्थान पर यण् (य्, व्, र्, ल्) होता है यदि बाद में अच् (कोई स्वर) हो।",
    type = SutraType.UTSARGA,
    chapter = 6,
    pada = 1,
    optional = false,
    kramaValue = 610077,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.VARNA,
    stage = SutraStage.VOWEL_SANDHI,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.stage !in setOf(DerivationStage.IT_PROCESSED, DerivationStage.ANGAKARYA, DerivationStage.PADA_FORMED)) return false

        val terms = context.terms
        val (leftIndex, rightIndex) = targetPair(context) ?: return false
        val rightTerm = terms[rightIndex]
        val isPresentSystemTing = context.effectiveContext.rupa.lakara in setOf(
            Lakara.LAT, Lakara.LOT, Lakara.LANG, Lakara.LING,
        ) && TingAffix.entries.any { it.upadesha == rightTerm.upadesha }
        val presentStemEstablished = context.allEffectiveTerms.any {
            it.upadesha in setOf("शप्", "श्यन्", "श्नु", "श", "श्नम्", "श्ना", "उ")
        }
        if (isPresentSystemTing && !presentStemEstablished) return false
        val left = terms[leftIndex].surface.lastVarna() ?: return false
        val right = terms[rightIndex].surface.firstVarna() ?: return false
        val isGhiFirstOrSecondDual = context.effectiveContext.rupa.vacana == Vacana.DVIVACANA &&
            context.effectiveContext.rupa.vibhakti in setOf(Vibhakti.PRATHAMA, Vibhakti.DVITIYA) &&
            context.samjnas.any { it.targetId == terms[leftIndex].id && it.samjna == Samjna.GHI }
        if (isGhiFirstOrSecondDual) return false

        val engine = Ashtadhyayi.pratyaharaEngine
        val sankhyaPair = context.samjnas.any { it.targetId == terms[leftIndex].id && it.samjna == Samjna.SANKHYA } &&
            context.samjnas.any { it.targetId == terms[rightIndex].id && it.samjna == Samjna.SANKHYA }
        val leftIsIk = engine.contains(Pratyahara.IK, left) || (sankhyaPair && left in yan)
        val rightIsAc = engine.contains(Pratyahara.AC, right) || (sankhyaPair && right is Svara)
        return leftIsIk &&
               rightIsAc &&
               !Varnamala.areSavarna(left, right) // Savarṇa-dīrgha (6.1.101) takes precedence
    }

    override fun apply(context: DerivationState): DerivationChange {
        val terms = context.terms
        val (leftIndex, rightIndex) = requireNotNull(targetPair(context))
        val leftTerm = terms[leftIndex]
        val rightTerm = terms[rightIndex]

        val leftVowel = requireNotNull(leftTerm.surface.lastVarna() as? Svara)
        val replacement = listOf(requireNotNull(yan[leftVowel]))
        val leftBase = leftTerm.varnas.dropLast(1) + replacement
        val rightVarnas = rightTerm.varnas
        if (rightTerm.id == "siyut") {
            return DerivationChange(
                state = context.redistributeAdjacentTermsByVarnaSubstitution(
                    leftId = leftTerm.id,
                    rightId = rightTerm.id,
                    leftSurface = (leftBase + rightVarnas.first()).toDevanagari(),
                    rightSurface = rightVarnas.drop(1).toDevanagari(),
                    source = leftVowel,
                    replacement = replacement,
                    sutra = sutra,
                ).copy(stage = DerivationStage.PADA_FORMED),
                explanation = "6.1.77: substituted ${replacement.toDevanagari()} for $leftVowel before the vowel of सीयुट्.",
            )
        }

        val mergedSurface = (leftBase + rightVarnas).toDevanagari()

        return DerivationChange(
            state = context.mergeTermsByVarnaSubstitution(
                leftTerm.id, rightTerm.id, mergedSurface, leftVowel, replacement, sutra,
            ).copy(stage = DerivationStage.PADA_FORMED),
            explanation = "6.1.77: substituted ${replacement.toDevanagari()} for $leftVowel before vowel and merged terms."
        )
    }

    private fun targetPair(context: DerivationState): Pair<Int, Int>? {
        val siyutIndex = context.terms.indexOfFirst { it.id == "siyut" && it.surface.isNotEmpty() }
        if (siyutIndex > 0) return (siyutIndex - 1) to siyutIndex
        if (context.terms.size < 2) return null
        if (context.terms.size > 2 && context.terms.all { it.id.startsWith("sankhya_") }) {
            return (0 until context.terms.lastIndex).firstOrNull { index ->
                context.terms[index].surface.lastVarna() in yan &&
                    context.terms[index + 1].surface.firstVarna() is Svara
            }?.let { it to it + 1 }
        }
        return (context.terms.lastIndex - 1) to context.terms.lastIndex
    }

    private val yan: Map<Varna, Vyanjana> = mapOf(
        Svara.I to Vyanjana.YA,
        Svara.II to Vyanjana.YA,
        Svara.U to Vyanjana.VA,
        Svara.UU to Vyanjana.VA,
        Svara.R to Vyanjana.RA,
        Svara.RR to Vyanjana.RA,
        Svara.L to Vyanjana.LA,
        Svara.LL to Vyanjana.LA,
    )
}
