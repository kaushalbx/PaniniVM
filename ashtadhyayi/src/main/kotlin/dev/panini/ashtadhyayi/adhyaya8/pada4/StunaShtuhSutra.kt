package dev.panini.ashtadhyayi.adhyaya8.pada4

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Varna
import dev.panini.shiksha.Vyanjana
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraStage
import dev.panini.sutra.SutraType

/**
 * 8.4.41: ṣṭunā ṣṭuḥ.
 * The sounds of 'stu' (s and tu-varga) are replaced by 'ṣṭu' (ṣ and ṭu-varga)
 * when they are in contact with 'ṣṭu' sounds.
 */
object StunaShtuhSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.4.41",
    text = "ष्टुना ष्टुः",
    hindiExplanation = "सकार और त-वर्ग के स्थान पर षकार और ट-वर्ग आदेश होते हैं, यदि षकार या ट-वर्ग का योग हो।",
    type = SutraType.NITYA,
    chapter = 8,
    pada = 4,
    optional = false,
    kramaValue = 840041,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.VARNA,
    stage = SutraStage.THUK_PHONOLOGY,
    dependencies = setOf("8.4.40")
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        val shnaIndex = context.terms.indexOfFirst { it.id == "shna" }
        if (shnaIndex >= 0 && context.terms[shnaIndex].varnas.lastOrNull() == Vyanjana.NNA &&
            context.terms.getOrNull(shnaIndex + 1)?.varnas?.firstOrNull() == Vyanjana.NA) return false
        // In the LET सिप् formation the following त् belongs to अट् + त्;
        // the intervening अ prevents actual ṣṭutva (तारिषत्, not *तारिषट्).
        if (context.terms.any { it.id == "sip-aorist" && Vyanjana.SSA in it.varnas }) return false
        val lungSicIndex = context.terms.indexOfFirst { it.upadesha == "सिँच्" && it.varnas.lastOrNull() == Vyanjana.SSA }
        if (lungSicIndex >= 0 && context.terms.getOrNull(lungSicIndex + 1)?.varnas?.firstOrNull() is Svara) return false
        return findMatch(context) != null
    }

    override fun apply(context: DerivationState): DerivationChange {
        val match = requireNotNull(findMatch(context))
        val targetTerm = context.terms[match.termIndex]
        val source = targetTerm.varnas[match.varnaIndex]
        val replacement = getReplacement(source)
        val result = targetTerm.varnas.toMutableList().also { it[match.varnaIndex] = replacement }

        return DerivationChange(
            state = context.substituteTermVarnas(
                targetTerm.id, result, emptyList(), source, listOf(replacement), sutra,
            ),
            explanation = "8.4.41: Retroflexed ${source.devanagari} to ${replacement.devanagari} in contact with ${match.trigger.devanagari}."
        )
    }

    private fun findMatch(context: DerivationState): Match? {
        val positions = context.terms.flatMapIndexed { termIndex, term ->
            term.varnas.mapIndexed { varnaIndex, varna -> OwnedVarna(termIndex, varnaIndex, varna) }
        }
        for (i in 0 until positions.lastIndex) {
            val curr = positions[i]
            val next = positions[i + 1]
            if (curr.varna in stu && next.varna in shtu) {
                return Match(curr.termIndex, curr.varnaIndex, next.varna)
            }
            if (curr.varna in shtu && next.varna in stu) {
                return Match(next.termIndex, next.varnaIndex, curr.varna)
            }
        }
        return null
    }

    private fun getReplacement(target: Varna): Varna = replacements[target] ?: target

    private val stu = setOf(Vyanjana.SA, Vyanjana.TA, Vyanjana.THA, Vyanjana.DA, Vyanjana.DHA, Vyanjana.NA)
    private val shtu = setOf(Vyanjana.SSA, Vyanjana.TTA, Vyanjana.TTHA, Vyanjana.DDA, Vyanjana.DDHA)
    private val replacements = mapOf<Varna, Varna>(
        Vyanjana.SA to Vyanjana.SSA, Vyanjana.TA to Vyanjana.TTA, Vyanjana.THA to Vyanjana.TTHA,
        Vyanjana.DA to Vyanjana.DDA, Vyanjana.DHA to Vyanjana.DDHA, Vyanjana.NA to Vyanjana.NNA,
    )
    private data class OwnedVarna(val termIndex: Int, val varnaIndex: Int, val varna: Varna)
    private data class Match(val termIndex: Int, val varnaIndex: Int, val trigger: Varna)
}
