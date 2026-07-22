package dev.panini.ashtadhyayi.adhyaya5.pada2

import dev.panini.derivation.*
import dev.panini.shiksha.Samjna
import dev.panini.sutra.*

/** 5.2.58: षष्ट्यादेश्चासंख्यादेः — तमट् is obligatory for unprefixed षष्टि etc. */
object ShashtyadeshCasankhyadehSutra : Sutra<DerivationState, DerivationChange>(
    number = "5.2.58",
    text = "षष्ट्यादेश्चासंख्यादेः",
    hindiExplanation = "संख्यावाचक पूर्वपद से रहित षष्टि आदि के डट् में तमट् आगम नित्य होता है।",
    type = SutraType.APAVADA,
    chapter = 5,
    pada = 2,
    optional = false,
    kramaValue = 520058,
    role = SutraRole.Apavada,
    action = SutraAction.AGAMA,
    scope = SutraScope.DERIVATION,
    blocks = setOf("5.2.56"),
), DerivationSutra {
    private val bases = setOf("षष्टि", "सप्तति", "अशीति", "नवति")

    override fun matches(context: DerivationState): Boolean {
        if (context.samjnas.none { it.samjna == Samjna.PURANA }) return false
        if (context.terms.any { it.upadesha == "तमट्" }) return false
        val datIndex = context.terms.indexOfLast { it.upadesha == "डट्" }
        return datIndex > 0 && context.terms[datIndex - 1].surface in bases
    }

    override fun apply(context: DerivationState): DerivationChange {
        val datIndex = context.terms.indexOfLast { it.upadesha == "डट्" }
        val tamat = DerivationTerm("purana_tamat", "तम", TermKind.AGAMA, upadesha = "तमट्")
        val terms = context.terms.toMutableList().apply { add(datIndex, tamat) }
        return DerivationChange(context.copy(terms = terms), "$text: डट् में नित्य तमट् आगम।")
    }
}
