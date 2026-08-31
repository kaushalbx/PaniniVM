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
import dev.panini.derivation.VarnaSubstitution
import dev.panini.pratyahara.Pratyahara
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Vyanjana
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
        val left = terms[leftIndex].surface.lastOrNull() ?: return false
        val right = terms[rightIndex].surface.firstOrNull() ?: return false
        val isGhiFirstOrSecondDual = context.effectiveContext.rupa.vacana == Vacana.DVIVACANA &&
            context.effectiveContext.rupa.vibhakti in setOf(Vibhakti.PRATHAMA, Vibhakti.DVITIYA) &&
            context.samjnas.any { it.targetId == terms[leftIndex].id && it.samjna == Samjna.GHI }
        if (isGhiFirstOrSecondDual) return false

        val engine = Ashtadhyayi.pratyaharaEngine
        val sankhyaPair = context.samjnas.any { it.targetId == terms[leftIndex].id && it.samjna == Samjna.SANKHYA } &&
            context.samjnas.any { it.targetId == terms[rightIndex].id && it.samjna == Samjna.SANKHYA }
        val leftIsIk = engine.contains(Pratyahara.IK, left) ||
            (sankhyaPair && normalizeIk(left) in setOf('इ', 'ई', 'उ', 'ऊ', 'ऋ', 'ॠ', 'ऌ'))
        val rightIsAc = engine.contains(Pratyahara.AC, right) ||
            (sankhyaPair && right in setOf('अ', 'आ', 'इ', 'ई', 'उ', 'ऊ', 'ऋ', 'ॠ', 'ऌ', 'ए', 'ऐ', 'ओ', 'औ'))
        return leftIsIk &&
               rightIsAc &&
               !isSavarna(left, right) // Savarna-dirgha (6.1.101) takes precedence
    }

    override fun apply(context: DerivationState): DerivationChange {
        val terms = context.terms
        val (leftIndex, rightIndex) = requireNotNull(targetPair(context))
        val leftTerm = terms[leftIndex]
        val rightTerm = terms[rightIndex]

        val leftVowel = leftTerm.surface.last()
        val replacement = yanFor(leftVowel)

        val isMatra = leftVowel in setOf('ि', 'ी', 'ु', 'ू', 'ृ', 'ॄ', 'ॢ')
        val newLeftSurface = if (isMatra) {
            leftTerm.surface.dropLast(1) + "्" + replacement
        } else {
            leftTerm.surface.dropLast(1) + replacement
        }
        if (rightTerm.id == "siyut") {
            val newTerms = terms.toMutableList()
            newTerms[leftIndex] = leftTerm.copy(surface = merge(newLeftSurface, rightTerm.surface.take(1)))
            newTerms[rightIndex] = rightTerm.copy(surface = rightTerm.surface.drop(1))
            return DerivationChange(
                state = context.copy(terms = newTerms, stage = DerivationStage.PADA_FORMED)
                    .addSubstitution(VarnaSubstitution(leftTerm.id, leftVowel, replacement, sutra)),
                explanation = "6.1.77: substituted $replacement for $leftVowel before the vowel of सीयुट्.",
            )
        }

        val mergedSurface = merge(newLeftSurface, rightTerm.surface)

        return DerivationChange(
            state = context.copy(
                terms = terms.take(leftIndex) + leftTerm.copy(surface = mergedSurface) + terms.drop(rightIndex + 1),
                droppedTerms = context.droppedTerms + rightTerm.copy(surface = ""),
                stage = DerivationStage.PADA_FORMED
            ).addSubstitution(VarnaSubstitution(leftTerm.id, leftVowel, replacement, sutra)),
            explanation = "6.1.77: substituted $replacement for $leftVowel before vowel and merged terms."
        )
    }

    private fun targetPair(context: DerivationState): Pair<Int, Int>? {
        val siyutIndex = context.terms.indexOfFirst { it.id == "siyut" && it.surface.isNotEmpty() }
        if (siyutIndex > 0) return (siyutIndex - 1) to siyutIndex
        if (context.terms.size < 2) return null
        if (context.terms.size > 2 && context.terms.all { it.id.startsWith("sankhya_") }) {
            return (0 until context.terms.lastIndex).firstOrNull { index ->
                normalizeIk(context.terms[index].surface.lastOrNull() ?: return@firstOrNull false) in
                    setOf('इ', 'ई', 'उ', 'ऊ', 'ऋ', 'ॠ', 'ऌ') &&
                    context.terms[index + 1].surface.firstOrNull() in
                    setOf('अ', 'आ', 'इ', 'ई', 'उ', 'ऊ', 'ऋ', 'ॠ', 'ऌ', 'ए', 'ऐ', 'ओ', 'औ')
            }?.let { it to it + 1 }
        }
        return (context.terms.lastIndex - 1) to context.terms.lastIndex
    }

    private fun merge(left: String, right: String): String {
        if (left.endsWith('्') && right.isNotEmpty()) {
            val first = right.first()
            val matra = when (first) {
                'अ' -> ""
                'आ' -> "ा"
                'इ' -> "ि"
                'ई' -> "ी"
                'उ' -> "ु"
                'ऊ' -> "ू"
                'ऋ' -> "ृ"
                'ॠ' -> "ॄ"
                'ऌ' -> "ॢ"
                'ए' -> "े"
                'ऐ' -> "ै"
                'ओ' -> "ो"
                'औ' -> "ौ"
                'ा' -> "ा"
                'ि' -> "ि"
                'ी' -> "ी"
                'ु' -> "ु"
                'ू' -> "ू"
                'ृ' -> "ृ"
                'ॄ' -> "ॄ"
                'ॢ' -> "ॢ"
                'े' -> "े"
                'ै' -> "ै"
                'ो' -> "ो"
                'ौ' -> "ौ"
                else -> null
            }
            if (matra != null) {
                return left.dropLast(1) + matra + right.drop(1)
            }
        }
        return left + right
    }

    private fun isSavarna(left: Char, right: Char): Boolean {
        // Simple savarna check: same vowel family
        val iks = setOf('इ', 'ई', 'ि', 'ी')
        val uks = setOf('उ', 'ऊ', 'ु', 'ू')
        val rks = setOf('ऋ', 'ॠ', 'ृ', 'ॄ')
        return (left in iks && right in iks) || (left in uks && right in uks) || (left in rks && right in rks)
    }

    private fun yanFor(c: Char): String = when(c) {
        'इ', 'ई', 'ि', 'ी' -> Vyanjana.YA.halanta
        'उ', 'ऊ', 'ु', 'ू' -> Vyanjana.VA.halanta
        'ऋ', 'ॠ', 'ृ', 'ॄ' -> Vyanjana.RA.halanta
        'ऌ', 'ॢ' -> Vyanjana.LA.halanta
        else -> ""
    }

    private fun normalizeIk(c: Char): Char = when (c) {
        'ि', 'ी' -> 'इ'
        'ु', 'ू' -> 'उ'
        'ृ', 'ॄ' -> 'ऋ'
        'ॢ' -> 'ऌ'
        else -> c
    }
}
