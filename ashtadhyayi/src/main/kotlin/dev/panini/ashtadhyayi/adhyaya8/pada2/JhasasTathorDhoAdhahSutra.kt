package dev.panini.ashtadhyayi.adhyaya8.pada2

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.TermKind
import dev.panini.shiksha.Vyanjana
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/** 8.2.40: झषस्तथोर्धोऽधः. त or थ of an ending becomes ध after a jhaṣ-final root. */
object JhasasTathorDhoAdhahSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.2.40",
    text = "झषस्तथोर्धोऽधः",
    hindiExplanation = "झष्-वर्णान्त धातु से परे प्रत्यय के त अथवा थ के स्थान पर ध होता है।",
    type = SutraType.NITYA,
    chapter = 8,
    pada = 2,
    optional = false,
    kramaValue = 820040,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.PRATYAYA,
), DerivationSutra {
    private val jhash = setOf(Vyanjana.JHA, Vyanjana.BHA, Vyanjana.GHA, Vyanjana.DDHA, Vyanjana.DHA)

    override fun matches(context: DerivationState): Boolean {
        val dhatuIndex = context.terms.indexOfFirst { it.kind == TermKind.DHATU }
        if (dhatuIndex < 0) return false
        val dhatu = context.terms[dhatuIndex]
        val affix = context.terms.getOrNull(dhatuIndex + 1) ?: return false
        return dhatu.varnas.lastOrNull() in jhash && affix.varnas.firstOrNull() in setOf(Vyanjana.TA, Vyanjana.THA)
    }

    override fun apply(context: DerivationState): DerivationChange {
        val dhatuIndex = context.terms.indexOfFirst { it.kind == TermKind.DHATU }
        val affix = context.terms[dhatuIndex + 1]
        val source = affix.varnas.first()
        return DerivationChange(
            context.replaceTermVarna(affix.id, 0, listOf(Vyanjana.DHA), sutra),
            "8.2.40 substitutes ध for $source after a jhaṣ-final root.",
        )
    }

}
