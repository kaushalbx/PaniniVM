package dev.panini.ashtadhyayi.adhyaya7.pada2

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.sutra.NimittaScope
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 7.2.102: tyadādīnām aḥ.
 * Substitutes 'a' for the final letter of tyadādi pronominal stems before a case affix (vibhakti).
 */
object TyadadinamAhSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.2.102",
    text = "त्यदादीनामः",
    hindiExplanation = "त्यदादि गण के शब्दों के अन्त्य वर्ण के स्थान पर अकार आदेश होता है विभक्तौ परे होने पर।",
    type = SutraType.NITYA,
    chapter = 7,
    pada = 2,
    optional = false,
    kramaValue = 720102,
    role = SutraRole.Apavada,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION,
    nimittaScope = NimittaScope.BOTH,
    dependencies = setOf("6.4.1"),
), DerivationSutra {
    private val TYADADI_STEMS = setOf("त्यद्", "तद्", "यद्", "एतद्", "किम्", "इदम्", "अदस्", "द्वि")

    override fun matches(context: DerivationState): Boolean {
        if (context.terms.size < 2) return false
        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()
        if (stem.surface == "अयम्") return false
        val isTyadadi = stem.upadesha in TYADADI_STEMS || stem.surface in TYADADI_STEMS
        val hasConsonantEnding = stem.surface.endsWith("्") || stem.surface in setOf("किम्", "इदम्", "द्वि")
        return isTyadadi && hasConsonantEnding &&
            (affix.id.startsWith("sup-") || context.droppedTerms.any { it.id.startsWith("sup-") })
    }

    override fun apply(context: DerivationState): DerivationChange {
        val stem = context.terms[context.terms.size - 2]
        val surface = stem.surface
        val newSurface = when (surface) {
            "किम्" -> "क"
            "इदम्" -> "इम"
            "द्वि" -> "द्व"
            else -> if (surface.endsWith("्")) surface.dropLast(2) else surface.dropLast(1)
        }

        return DerivationChange(
            state = context.replaceTerm(stem.id, stem.copy(surface = newSurface))
                .copy(stage = DerivationStage.ANGAKARYA),
            explanation = "7.2.102: Substitutes 'a' for the final letter of tyadādi stem '$surface'."
        )
    }
}
