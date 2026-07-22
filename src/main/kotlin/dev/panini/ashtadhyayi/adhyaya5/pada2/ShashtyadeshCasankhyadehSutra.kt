package dev.panini.ashtadhyayi.adhyaya5.pada2

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.shiksha.Samjna
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

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
    override fun matches(context: DerivationState): Boolean {
        if (context.samjnas.none { it.samjna == Samjna.PURANA }) return false
        if (context.hasTamat()) return false
        val datIndex = context.datIndex()
        return datIndex > 0 && context.terms[datIndex - 1].surface in PuranaNumeralClasses.shashtyadiHeads
    }

    override fun apply(context: DerivationState): DerivationChange {
        return context.insertTamat(sutra, "$text: डट् में नित्य तमट् आगम।")
    }
}
