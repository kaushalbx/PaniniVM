package dev.panini.ashtadhyayi.adhyaya8.pada4

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.shiksha.Varna
import dev.panini.shiksha.Vyanjana
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraStage
import dev.panini.sutra.SutraType

/**
 * 8.4.40: stoḥ ścunā ścuḥ.
 * The sounds of 'stu' (s and tu-varga) are replaced by 'ścu' (ś and cu-varga)
 * when they are in contact with 'ścu' sounds.
 */
object StosShcunaShcuhSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.4.40",
    text = "स्तोः श्चुना श्चुः",
    hindiExplanation = "सकार और त-वर्ग के स्थान पर शकार और च-वर्ग आदेश होते हैं, यदि शकार या च-वर्ग का योग हो।",
    type = SutraType.NITYA,
    chapter = 8,
    pada = 4,
    optional = false,
    kramaValue = 840040,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.VARNA,
    stage = SutraStage.SIBILANT_SANDHI,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean = findMatch(context) != null

    override fun apply(context: DerivationState): DerivationChange {
        val match = findMatch(context)!!
        val targetTerm = context.terms[match.termIndex]
        val source = targetTerm.varnas[match.varnaIndex]
        val replacement = getReplacement(source)

        return DerivationChange(
            state = context.replaceTermVarna(targetTerm.id, match.varnaIndex, listOf(replacement), sutra),
            explanation = "8.4.40: Palatalized ${source.devanagari} to ${replacement.devanagari} in contact with ${match.trigger.devanagari}."
        )
    }

    private fun findMatch(context: DerivationState): Match? {
        val varnas = context.terms.flatMapIndexed { termIndex, term ->
            term.varnas.mapIndexed { varnaIndex, varna -> OwnedVarna(termIndex, varnaIndex, varna) }
        }
        for (i in 0 until varnas.lastIndex) {
            val curr = varnas[i]
            val next = varnas[i + 1]
            if (isStu(curr.varna) && isShcu(next.varna)) {
                return Match(curr.termIndex, curr.varnaIndex, next.varna)
            }
            if (isShcu(curr.varna) && isStu(next.varna)) {
                return Match(next.termIndex, next.varnaIndex, curr.varna)
            }
        }
        return null
    }

    private fun isStu(varna: Varna): Boolean = varna in stu

    private fun isShcu(varna: Varna): Boolean = varna in shcu

    private fun getReplacement(target: Varna): Varna = replacements[target] ?: target

    private val stu = setOf(Vyanjana.SA, Vyanjana.TA, Vyanjana.THA, Vyanjana.DA, Vyanjana.DHA, Vyanjana.NA)
    private val shcu = setOf(Vyanjana.SHA, Vyanjana.CA, Vyanjana.CHA, Vyanjana.JA, Vyanjana.JHA, Vyanjana.NYA)
    private val replacements = mapOf<Varna, Varna>(
        Vyanjana.SA to Vyanjana.SHA, Vyanjana.TA to Vyanjana.CA, Vyanjana.THA to Vyanjana.CHA,
        Vyanjana.DA to Vyanjana.JA, Vyanjana.DHA to Vyanjana.JHA, Vyanjana.NA to Vyanjana.NYA,
    )
    private data class OwnedVarna(val termIndex: Int, val varnaIndex: Int, val varna: Varna)
    private data class Match(val termIndex: Int, val varnaIndex: Int, val trigger: Varna)
}
