package dev.sanskrit.ashtadhyayi.adhyaya8.pada2

import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.derivation.TermKind
import dev.sanskrit.derivation.VarnaSubstitution
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

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
    private val jhash = setOf('झ', 'भ', 'घ', 'ढ', 'ध')

    override fun matches(context: DerivationState): Boolean {
        val dhatuIndex = context.terms.indexOfFirst { it.kind == TermKind.DHATU }
        if (dhatuIndex < 0) return false
        val dhatu = context.terms[dhatuIndex]
        val affix = context.terms.getOrNull(dhatuIndex + 1) ?: return false
        return finalConsonant(dhatu.surface) in jhash && affix.surface.firstOrNull() in setOf('त', 'थ')
    }

    override fun apply(context: DerivationState): DerivationChange {
        val dhatuIndex = context.terms.indexOfFirst { it.kind == TermKind.DHATU }
        val affix = context.terms[dhatuIndex + 1]
        val source = affix.surface.first()
        return DerivationChange(
            context.replaceTerm(affix.id, affix.copy(surface = "ध" + affix.surface.drop(1)))
                .addSubstitution(VarnaSubstitution(affix.id, source, "ध", sutra)),
            "8.2.40 substitutes ध for $source after a jhaṣ-final root.",
        )
    }

    private fun finalConsonant(surface: String): Char? = when {
        surface.endsWith('्') && surface.length >= 2 -> surface[surface.length - 2]
        else -> surface.lastOrNull()
    }
}
